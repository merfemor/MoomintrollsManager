package net.client;

import net.protocol.MPacket;
import net.protocol.MProtocol;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoomintrollsClient {

    public static Logger log = Logger.getLogger(MoomintrollsClient.class.getName());

    private final SocketAddress socketAddress;
    private DatagramSocket datagramSocket;

    public MoomintrollsClient(InetSocketAddress socketAddress) throws SocketException {
        this.socketAddress = socketAddress;
        datagramSocket = new DatagramSocket();
        datagramSocket.connect(socketAddress);
    }

    public void sendMessage(String message) throws IOException {
        MPacket mPacket = MProtocol.generatePacket(message);
        DatagramPacket packet = new DatagramPacket(mPacket.getContent(), mPacket.getContent().length);
        datagramSocket.send(packet);
    }

    public static void main(String[] args) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 1238);
        MoomintrollsClient moomintrollsClient = null;
        try {
            moomintrollsClient = new MoomintrollsClient(inetSocketAddress);
            log.fine("Started client on " + inetSocketAddress.getHostString());
        } catch (SocketException e) {
            log.log(Level.SEVERE, "Failed to create client on " + inetSocketAddress.getHostString(), e);
            return;
        }
        try {
            log.info("Sending 1 package...");
            moomintrollsClient.sendMessage("WAT_test");
            log.info("Sending 2 package...");
            moomintrollsClient.sendMessage("ischo test");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
