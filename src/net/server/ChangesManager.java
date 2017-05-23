package net.server;

import net.protocol.MAnswer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ChangesManager implements Runnable {

    private Map<SocketAddress, ChangesNotifier> recipients;
    private BlockingQueue<MAnswer> answerQueue;
    private DatagramChannel channel;
    private boolean stop = false;

    public ChangesManager(DatagramChannel channel) {
        this.channel = channel;
        recipients = new HashMap<>();
        answerQueue = new LinkedBlockingDeque<>();
    }

    public void addRecipient(ClientManager clientManager) {
        InetSocketAddress isa = (InetSocketAddress) clientManager.getSocketAddress();
        ChangesNotifier changesNotifier =
                new ChangesNotifier(channel, new InetSocketAddress(isa.getHostName(), isa.getPort()));
        recipients.put(isa, changesNotifier);
        new Thread(changesNotifier).start();
        clientManager.setAnswerHandler(answer -> answerQueue.add(answer));
    }

    public void removeRecipient(ClientManager clientManager) {
        recipients.remove(clientManager.getSocketAddress()).stop();
    }

    public void stop() {
        stop = true;
    }

    public void sendOneAnswer(MAnswer answer, SocketAddress socketAddress) {
        recipients.get(socketAddress).addAnswer(answer);
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                MAnswer mAnswer = answerQueue.poll(10, TimeUnit.SECONDS);
                if (mAnswer == null)
                    continue;
                recipients.values().forEach(a -> a.addAnswer(mAnswer));

            } catch (InterruptedException e) {
                MoomintrollsServer.log.log(Level.SEVERE,
                        "Interrupted when trying to get new answer", e);
                break;
            }
        }
    }
}
