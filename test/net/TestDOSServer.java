package net;

import net.client.MoomintrollsClient;
import org.junit.Test;
import trolls.Moomintroll;
import trolls.utils.Random;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TestDOSServer {
    private final long REQUESTS_NUMBER = 100000;
    private final int CLIENTS_NUMBER = 1000;
    private final InetSocketAddress localhost;

    public TestDOSServer() throws UnknownHostException {
        localhost = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 1111);
    }

    @Test
    public void ddosFromOneClient() throws IOException {
        MoomintrollsClient client = createNewClient();
        for (long i = 1; i <= REQUESTS_NUMBER; i++) {
            if (i % 100 == 0) {
                System.out.println("Sending " + (double) (i) * 100.0 / REQUESTS_NUMBER + "%");
            }
            client.collectionRequest();
        }
        client.close();
    }

    @Test
    public void addDdosOneClient() throws IOException {
        MoomintrollsClient client = createNewClient();
        for (long i = 1; i <= REQUESTS_NUMBER; i++) {
            Moomintroll[] moomintrolls = {Random.randomTroll()};
            client.add(moomintrolls);
        }
        client.close();
    }


    @Test
    public void manyCLientsOneThread() throws IOException {
        MoomintrollsClient[] moomintrollsClients = new MoomintrollsClient[CLIENTS_NUMBER];
        for (int i = 0; i < CLIENTS_NUMBER; i++) {
            moomintrollsClients[i] = createNewClient();
            moomintrollsClients[i].collectionRequest();
        }
        Moomintroll[] moomintrolls = {Random.randomTroll()};
        moomintrollsClients[0].add(moomintrolls);

        for (int i = 0; i < CLIENTS_NUMBER; i++) {
            moomintrollsClients[i].close();
        }
    }

    public MoomintrollsClient createNewClient() throws SocketException {
        return new MoomintrollsClient(this.localhost);
    }
}
