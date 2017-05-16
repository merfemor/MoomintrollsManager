package net.client;

import net.protocol.IdentifiedMoomintroll;
import net.protocol.MPacket;
import net.protocol.MRequest;
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
        sendPackets(MRequest.createRemoveRequest(ids).toPackets());
    }

    public void add(Moomintroll[] moomintrolls) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Command: ADD");
            if (log.isLoggable(Level.FINEST)) {
                Arrays.stream(moomintrolls).forEach((m) -> log.finest(m.toString()));
            }
        }
        sendPackets(MRequest.createAddRequest(moomintrolls).toPackets());
    }

    public void update(long id, Moomintroll moomintroll) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Command: UPDATE " + id + ": " + moomintroll);
        }
        sendPackets(MRequest.createUpdateRequest(new IdentifiedMoomintroll(id, moomintroll)).toPackets());
    }

    public void collectionRequest() throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Command: COLLECTION REQUEST");
        }
        sendPackets(MRequest.createSelectAllRequest().toPackets());
    }

    public void close() {
        try {
            sendPackets(MRequest.createDisconnectRequest().toPackets());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to send disconnect request to server", e);
            return;
        } finally {
            this.datagramSocket.disconnect();
            this.datagramSocket.close();
        }
        log.info("Connection with " + socketAddress.toString() + " closed");
    }
}
