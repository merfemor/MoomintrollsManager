package net.server;

import mbean.CommandsCount;
import net.protocol.MAnswer;
import net.protocol.MCommand;
import net.protocol.MPacket;
import net.protocol.MRequest;
import psql.MoomintrollsDatabase;
import ru.ifmo.cs.korm.Session;
import trolls.Moomintroll;

import java.io.IOException;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static net.server.MoomintrollsServer.log;

public class ClientManager implements Runnable {
    private static ThreadLocal<Moomintroll[]> fullData;
    protected final SocketAddress socketAddress;
    private BlockingQueue<MPacket> packetsQueue;
    private boolean stop = false;
    private MoomintrollsDatabase database;
    private Session databaseSession;
    private Runnable disconnectionHandler;
    private AnswerHandler answerHandler;
    private ChangesManager changesManager;
    private CommandsCount commandsCounter;

    public ClientManager(SocketAddress socketAddress, MoomintrollsDatabase database, ChangesManager changesManager, Session databaseSession) {
        this.socketAddress = socketAddress;
        this.database = database;
        this.changesManager = changesManager;
        this.databaseSession = databaseSession;
        packetsQueue = new LinkedBlockingDeque<>();
        fullData = new ThreadLocal<>();
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

    public void registerCommandsCounter(CommandsCount commandsCounter) {
        this.commandsCounter = commandsCounter;
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
                packet = packetsQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                MoomintrollsServer.log.log(Level.SEVERE,
                        "Interrupted when waiting for the packet", e);
                break;
            }
            if (packet == null) {
                continue;
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
        long startTime = System.nanoTime();
        int numberOfCommands = 1;
        switch (command.type()) {
            case MCommand.Type.ADD:
                Moomintroll[] moomintrolls = MRequest.parseAddRequest(command);
                if (moomintrolls.length == 0) {
                    log.warning("ADD canceled: nothing to add");
                    break;
                }
                databaseSession.add(moomintrolls);
                log.fine("Command from " + socketAddress + ": ADD" + Arrays.toString(moomintrolls));
                answerHandler.handleAnswer(MAnswer.createAddAnswer(moomintrolls));
                break;
            case MCommand.Type.REMOVE:
                long[] removedIds = MRequest.parseRemoveRequest(command);
                numberOfCommands = removedIds.length;
                if (removedIds.length == 0) {
                    log.warning("REMOVE canceled: nothing to remove");
                    break;
                }
                Moomintroll[] deadSouls = new Moomintroll[removedIds.length];
                for (int i = 0; i < deadSouls.length; i++) {
                    deadSouls[i] = new Moomintroll();
                    deadSouls[i].setId(removedIds[i]);
                }
                databaseSession.remove(deadSouls);

                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": REMOVE" + Arrays.toString(removedIds));
                }

                answerHandler.handleAnswer(new MAnswer(command.data()));
                break;
            case MCommand.Type.UPDATE:
                Moomintroll im = MRequest.parseUpdateRequest(command);
                databaseSession.update(new Moomintroll[]{im});
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": UPDATE " + im.getId() + " " + im);
                }
                answerHandler.handleAnswer(new MAnswer(command.data()));
                break;
            case MCommand.Type.SELECT_ALL:
                fullData.set(databaseSession.getData(Moomintroll.class));
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Command from " + socketAddress + ": SELECT_ALL");
                }
                changesManager.sendOneAnswer(
                        MAnswer.createSelectAllAnswer(fullData.get()),
                        socketAddress);
                break;
            default:
                throw new IllegalArgumentException("Illegal type of command = " + command.type());
        }
        long executingTime = System.nanoTime() - startTime;
        if (commandsCounter != null) {
            commandsCounter.reportCommand(command.type(),
                    numberOfCommands,
                    (double) (executingTime) / 1000000.0);
        }

    }
}
