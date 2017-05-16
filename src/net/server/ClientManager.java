package net.server;

import net.protocol.MCommand;
import net.protocol.MPacket;
import psql.MoomintrollsDatabase;
import trolls.Moomintroll;

import java.io.IOException;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

import static net.server.MoomintrollsServer.log;

public class ClientManager implements Runnable {
    protected final SocketAddress socketAddress;
    private BlockingQueue<MPacket> packetsQueue;
    private boolean stop = false;
    private MoomintrollsDatabase database;

    public ClientManager(SocketAddress socketAddress, MoomintrollsDatabase database) {
        this.socketAddress = socketAddress;
        this.database = database;
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
        // parse packet
        int waitingForPackets = 0;
        List<MPacket> packetList = new ArrayList<>();

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
                waitingForPackets = packet.packetsNumber();
                packetList = new ArrayList<>();
            }

            packetList.add(packet);
            waitingForPackets--;

            if (waitingForPackets == 0) {
                MCommand command = MCommand.fromPackets(packetList);
                if (command.type() == MCommand.Type.DISCONNECT) {
                    // disconnect
                }
                try {
                    executeCommand(command);
                } catch (IllegalArgumentException | IOException | ClassNotFoundException | SQLException e) {
                    MoomintrollsServer.log.log(Level.SEVERE,
                            "Error when executing command from " + socketAddress, e);
                }
            } else if (waitingForPackets < 0) {
                waitingForPackets = 0;
            }
        }
    }

    private void executeCommand(MCommand command) throws IllegalArgumentException, IOException, ClassNotFoundException, SQLException {
        switch (command.type()) {
            case MCommand.Type.ADD:
                Moomintroll[] moomintrolls = MCommand.parseAddCommand(command);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": ADD" + Arrays.toString(moomintrolls));
                }

                if (moomintrolls.length == 1) {
                    long id = database.insert(moomintrolls[0]);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Get id = " + id);
                    }
                } else {
                    long[] ids = database.insert(moomintrolls);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Get ids = " + Arrays.toString(ids));
                    }
                }

                break;
            case MCommand.Type.REMOVE:
                long[] ids = MCommand.parseRemoveCommand(command);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": REMOVE" + Arrays.toString(ids));
                }
                if (ids.length == 1) {
                    database.delete(ids[0]);
                } else {
                    database.delete(ids);
                }
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Deleted ids " + Arrays.toString(ids));
                }
                break;
            case MCommand.Type.SELECT_ALL:
                break;
            case MCommand.Type.UPDATE:
                break;
            default:
                throw new IllegalArgumentException("Illegal type of command = " + command.type());
        }
    }
}
