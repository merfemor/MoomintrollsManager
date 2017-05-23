package net.server;

import net.protocol.MAnswer;
import net.protocol.MCommand;
import net.protocol.MPacket;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ChangesNotifier implements Runnable {
    private BlockingQueue<MAnswer> answerQueue;
    private SocketAddress recipientAddress;
    private DatagramChannel datagramChannel;
    private boolean stop = false;

    public ChangesNotifier(DatagramChannel datagramChannel, SocketAddress recipientAddress) {
        this.datagramChannel = datagramChannel;
        this.recipientAddress = recipientAddress;
        answerQueue = new LinkedBlockingQueue<>();
    }

    public void addAnswer(MAnswer mAnswer) {
        answerQueue.add(mAnswer);
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                MAnswer mAnswer = answerQueue.poll(10, TimeUnit.SECONDS);
                if (mAnswer == null) {
                    continue;
                }
                sendAnswer(mAnswer);
                if (MoomintrollsServer.log.isLoggable(Level.FINE))
                    MoomintrollsServer.log.fine(
                            "Sent change of type " + MCommand.Type.name(mAnswer.type()) + " to " + recipientAddress);
            } catch (InterruptedException e) {
                MoomintrollsServer.log.log(Level.SEVERE,
                        "Interrupted when waiting for the packet", e);
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void sendAnswer(MAnswer answer) throws IOException {
        byte[] b = new byte[MPacket.PACKETS_LENGTH];
        ByteBuffer buffer = ByteBuffer.wrap(b);
        for (MPacket packet : answer.toPackets()) {
            buffer.clear();
            buffer.put(packet.getContent());
            buffer.flip();
            datagramChannel.send(buffer, recipientAddress);
        }
    }
}
