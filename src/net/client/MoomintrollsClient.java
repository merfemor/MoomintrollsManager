package net.client;

import net.protocol.MCommand;
import net.protocol.MPacket;

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

    private void sendPacket(MPacket packet) throws IOException {
        DatagramPacket datagramPacket =
                new DatagramPacket(packet.getContent(), packet.getContent().length);
        datagramSocket.send(datagramPacket);
    }

    public void remove(long[] ids) throws IOException {
        for (MPacket packet : MCommand.createRemoveCommand(ids).toPackets()) {
            sendPacket(packet);
        }
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
        long[] ids = {234, 235, 230};
        try {
            moomintrollsClient.remove(ids);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
