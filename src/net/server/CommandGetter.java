package net.server;

import net.protocol.MPacket;
import net.protocol.MProtocol;

import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

public class CommandGetter implements Runnable {
    protected final SocketAddress socketAddress;
    private BlockingQueue<MPacket> packetsQueue;
    private boolean stop = false;

    public CommandGetter(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        packetsQueue = new LinkedBlockingDeque<>();
    }

    public synchronized void addPacket(MPacket packet) {
        packetsQueue.add(packet);
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        int waitingForPackets = 0;
        StringBuilder message = null;

        while (!stop) {
            MPacket packet;
            try {
                packet = packetsQueue.take();
            } catch (InterruptedException e) {
                MoomintrollsServer.log.log(Level.SEVERE,
                        "Interrupted when waiting for the packet", e);
                break;
            }

            if (waitingForPackets == 0) {
                waitingForPackets = MProtocol.packetsNumber(packet);
                message = new StringBuilder();
            }

            waitingForPackets--;
            message.append(new String(packet.getContent()));

            if (waitingForPackets == 0) {
                if (MoomintrollsServer.log.isLoggable(Level.FINE)) {
                    if (MoomintrollsServer.log.isLoggable(Level.FINEST)) {
                        MoomintrollsServer.log.finest("Command Getter got full message from " + socketAddress + "\nContent: " + message.toString());
                    } else {
                        MoomintrollsServer.log.fine("Command Getter got full message from " + socketAddress);
                    }
                }

                byte[] b = message.toString().getBytes();
                // deserialize and parse
                // m.b. send to new thread like CommandExecutor?
            } else if (waitingForPackets < 0) {
                waitingForPackets = 0;
            }
        }
    }
}
