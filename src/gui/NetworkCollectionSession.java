package gui;

import net.client.MoomintrollsClient;
import trolls.MoomintrollsCollection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class NetworkCollectionSession extends CollectionSession {
    private MoomintrollsClient client;

    public NetworkCollectionSession(MoomintrollsCollection moomintrollsCollection, InetSocketAddress address) throws SocketException {
        super(moomintrollsCollection);
        client = new MoomintrollsClient(address);
    }

    public MoomintrollsClient getClient() {
        return client;
    }

    public void reload() throws IOException {
        client.collectionRequest();
    }

    public boolean close() {
        client.close();
        return true;
    }
}
