package net.server;

import net.protocol.MAnswer;
import net.protocol.MPacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

public class ChangesNotifier implements Runnable {
    private List<SocketAddress> recipients;
    private BlockingQueue<MAnswer> answerQueue;
    private DatagramChannel channel;
    private boolean stop = false;

    public ChangesNotifier(DatagramChannel channel) {
        this.channel = channel;
        recipients = new ArrayList<>();
        answerQueue = new LinkedBlockingDeque<>();
    }

    public void addRecipient(ClientManager clientManager) {
        InetSocketAddress isa = (InetSocketAddress) clientManager.getSocketAddress();
        recipients.add(new InetSocketAddress(isa.getHostName(), isa.getPort()));
        clientManager.setAnswerHandler(answer -> answerQueue.add(answer));
    }

    public void removeRecipient(ClientManager clientManager) {
        recipients.remove(clientManager.getSocketAddress());
    }

    public void stop() {
        stop = true;
    }

    public void sendAnswer(MAnswer answer, SocketAddress address) throws IOException {
        byte[] b = new byte[MPacket.PACKETS_LENGTH];
        ByteBuffer buffer = ByteBuffer.wrap(b);
        for (MPacket packet : answer.toPackets()) {
            buffer.clear();
            buffer.put(packet.getContent());
            buffer.flip();
            channel.send(buffer, address);
        }
        if (MoomintrollsServer.log.isLoggable(Level.FINE))
            MoomintrollsServer.log.fine(
                    "Sent change of type " + answer.type() + " to " + address);
    }

    @Override
    public void run() {
        while (!stop) {
            MAnswer answer;
            try {
                answer = answerQueue.take();
            } catch (InterruptedException e) {
                MoomintrollsServer.log.log(Level.SEVERE, "Failed to take new answer", e);
                stop();
                break;
            }

            for (SocketAddress recipient : recipients) {
                new Thread(() -> {
                    try {
                        sendAnswer(answer, recipient);
                    } catch (IOException e) {
                        MoomintrollsServer.log.log(Level.SEVERE,
                                "Failed to send answer to " + recipient, e);
                    }
                }).start();
            }
        }
    }
}
