package net;

import net.client.MoomintrollsClient;
import org.junit.Test;
import trolls.Moomintroll;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static junit.framework.TestCase.fail;

public class TestSendCommand {

    private MoomintrollsClient getClient() {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 1238);
        MoomintrollsClient moomintrollsClient = null;
        try {
            moomintrollsClient = new MoomintrollsClient(inetSocketAddress);
        } catch (SocketException e) {
            System.out.println("Failed to create client on " + inetSocketAddress.getHostString());
            fail();
        }
        return moomintrollsClient;
    }

    @Test
    public void testSendRemove() {
        MoomintrollsClient client = getClient();

        long[] ids = {249, 250};
        try {
            client.remove(ids);
        } catch (IOException e) {
            fail();
        } finally {
            client.close();
        }
    }

    @Test
    public void testSendUpdate() {
        MoomintrollsClient client = getClient();

        long id = 250;
        Moomintroll newTroll = trolls.utils.Random.randomTroll();
        System.out.println(id + ": " + newTroll);

        try {
            client.update(id, newTroll);
        } catch (IOException e) {
            fail();
        } finally {
            client.close();
        }
    }


    @Test
    public void testSendAdd() {
        MoomintrollsClient client = getClient();

        Moomintroll[] moomintrolls = new Moomintroll[4];
        for (int i = 0; i < moomintrolls.length; i++) {
            moomintrolls[i] = trolls.utils.Random.randomTroll();
        }

        try {
            client.add(moomintrolls);
        } catch (IOException e) {
            fail();
        } finally {
            client.close();
        }
    }
}
