package net;

import trolls.MoomintrollsCollection;

import java.io.IOException;
import java.net.*;

public class Client extends InetPackageManager {

    public Client(String hostname, int port) {
        super(port);
        this.socketAddress = new InetSocketAddress(hostname, port);
    }

    public boolean send(MoomintrollsCollection moomintrollsCollection) {
        // .. serialize and send
        byte[] s = "serialized".getBytes();
        try {
            sendPacket(s,
                    ((InetSocketAddress) socketAddress).getAddress(),
                    ((InetSocketAddress) socketAddress).getPort()
            );
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
