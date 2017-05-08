package net;

import java.io.IOException;
import java.net.*;

public abstract class InetPackageManager {
    protected SocketAddress socketAddress;
    protected DatagramSocket datagramSocket;

    public InetPackageManager(int port) {
        this.socketAddress = new InetSocketAddress(port);
    }

    protected boolean startSocket() {
        try {
            datagramSocket = new DatagramSocket(socketAddress);
            return true;
        } catch (SocketException e) {
            return false;
        }
    }

    public DatagramPacket getPacket(int packetLength) throws IOException {
        byte[] content = new byte[packetLength];
        DatagramPacket packet = new DatagramPacket(content, content.length);
        datagramSocket.receive(packet);
        return packet;
    }

    public void sendPacket(byte[] message, InetAddress address, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
        datagramSocket.send(packet);
    }


}
