package net.server;

import net.IdentifiedMoomintroll;
import net.protocol.MAnswer;
import net.protocol.MCommand;
import net.protocol.MPacket;
import net.protocol.MRequest;
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
    private Runnable disconnectionHandler;
    private AnswerHandler answerHandler;
    private ChangesNotifier changesNotifier;

    public ClientManager(SocketAddress socketAddress, MoomintrollsDatabase database, ChangesNotifier changesNotifier) {
        this.socketAddress = socketAddress;
        this.database = database;
        this.changesNotifier = changesNotifier;
        packetsQueue = new LinkedBlockingDeque<>();
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void addPacket(MPacket packet) {
        packetsQueue.add(packet);
    }

    public void stop() {
        this.stop = true;
    }

    public void setDisconnectionHandler(Runnable disconnectionHandler) {
        this.disconnectionHandler = disconnectionHandler;
    }

    public void setAnswerHandler(AnswerHandler answerHandler) {
        this.answerHandler = answerHandler;
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
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Command from " + socketAddress + ": DISCONNECT");
                    }
                    if (this.disconnectionHandler != null) {
                        disconnectionHandler.run();
                    }
                    continue;
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
                Moomintroll[] moomintrolls = MRequest.parseAddRequest(command);
                IdentifiedMoomintroll[] identifiedMoomintrolls =
                        new IdentifiedMoomintroll[moomintrolls.length];

                long addedIds[] = new long[moomintrolls.length];

                if (moomintrolls.length == 1) {
                    addedIds[0] = database.insert(moomintrolls[0]);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Command from " + socketAddress + ": ADD" + Arrays.toString(moomintrolls));
                        log.fine("Get id = " + addedIds[0]);
                    }
                } else {
                    addedIds = database.insert(moomintrolls);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Command from " + socketAddress + ": ADD" + Arrays.toString(moomintrolls));
                        log.fine("Get ids = " + Arrays.toString(addedIds));
                    }
                }
                for (int i = 0; i < moomintrolls.length; i++) {
                    identifiedMoomintrolls[i] = new IdentifiedMoomintroll(addedIds[i], moomintrolls[i]);
                }
                answerHandler.handleAnswer(MAnswer.createAddAnswer(identifiedMoomintrolls));
                break;
            case MCommand.Type.REMOVE:
                long[] removedIds = MRequest.parseRemoveRequest(command);
                if (removedIds.length == 1) {
                    database.delete(removedIds[0]);
                } else {
                    database.delete(removedIds);
                }
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": REMOVE" + Arrays.toString(removedIds));
                }

                answerHandler.handleAnswer(new MAnswer(command.data()));
                break;
            case MCommand.Type.UPDATE:
                IdentifiedMoomintroll im = MRequest.parseUpdateRequest(command);
                database.update(im.id(), im.moomintroll());
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": UPDATE " + im.id() + " " + im.moomintroll());
                }

                answerHandler.handleAnswer(new MAnswer(command.data()));
                break;
            case MCommand.Type.SELECT_ALL:
                IdentifiedMoomintroll[] selectedMoomintrolls = database.toArray();
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": SELECT_ALL");
                }
                changesNotifier.sendAnswer(
                        MAnswer.createSelectAllAnswer(selectedMoomintrolls),
                        socketAddress);
                break;
            default:
                throw new IllegalArgumentException("Illegal type of command = " + command.type());
        }
    }
}
