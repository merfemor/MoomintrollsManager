package net.client;

import net.protocol.MCommand;
import net.protocol.MPacket;
import trolls.Moomintroll;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoomintrollsClient {

    public static Logger log = Logger.getLogger(MoomintrollsClient.class.getName());

    private final SocketAddress socketAddress;
    private DatagramSocket datagramSocket;

    public MoomintrollsClient(InetSocketAddress socketAddress) throws SocketException {
        this.socketAddress = socketAddress;
        datagramSocket = new DatagramSocket();
        log.info("Started client on port " + datagramSocket.getLocalPort());
        datagramSocket.connect(socketAddress);
        log.info("Connected to " + socketAddress);
    }

    private void sendPackets(List<MPacket> packets) throws IOException {
        DatagramPacket datagramPacket;
        for (MPacket packet : packets) {
            datagramPacket = new DatagramPacket(packet.getContent(), packet.getContent().length);
            datagramSocket.send(datagramPacket);
        }
    }

    public void remove(long[] ids) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Command: REMOVE" + Arrays.toString(ids));
        }
        sendPackets(MCommand.createRemoveCommand(ids).toPackets());
    }

    public void add(Moomintroll[] moomintrolls) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Command: ADD");
            if (log.isLoggable(Level.FINEST)) {
                Arrays.stream(moomintrolls).forEach((m) -> log.finest(m.toString()));
            }
        }
        sendPackets(MCommand.createAddCommand(moomintrolls).toPackets());
    }

    public void close() {
        this.datagramSocket.disconnect();
        this.datagramSocket.close();
        log.info("Connection with " + socketAddress.toString() + " closed");
    }
}
