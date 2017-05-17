package net.client;

import net.IdentifiedMoomintroll;
import net.protocol.MAnswer;
import net.protocol.MCommand;
import net.protocol.MPacket;
import net.server.MoomintrollsServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static net.server.MoomintrollsServer.log;

public class ChangesLoader implements Runnable {
    private DatagramSocket datagramSocket;
    private CommandHandler commandHandler;
    private boolean stop = false;

    public ChangesLoader(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void stop() {
        stop = true;
        datagramSocket = null;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    private MPacket receivePacket() throws IOException {
        MPacket packet = new MPacket(new byte[MPacket.PACKETS_LENGTH]);
        DatagramPacket datagramPacket = new DatagramPacket(packet.getContent(), packet.getContent().length);
        datagramSocket.receive(datagramPacket);
        return packet;
    }


    // TODO: it's similar to net.server.ClientManager::run
    @Override
    public void run() {
        // parse packet
        int waitingForPackets = 0;
        List<MPacket> packetList = new ArrayList<>();

        while (!stop) {
            MPacket packet;
            try {
                packet = receivePacket();
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                MoomintrollsClient.log.log(Level.SEVERE, "Failed to receive packet", e);
                stop();
                break;
            }

            if (waitingForPackets == 0) {
                waitingForPackets = packet.packetsNumber();
                packetList = new ArrayList<>();
            }

            packetList.add(packet);
            waitingForPackets--;

            if (waitingForPackets == 0) {
                MAnswer answer = new MAnswer(MCommand.fromPackets(packetList).data());
                try {
                    executeCommand(answer);
                } catch (IllegalArgumentException | IOException | ClassNotFoundException e) {
                    MoomintrollsServer.log.log(Level.SEVERE,
                            "Error when executing command from server", e);
                }
            } else if (waitingForPackets < 0) {
                waitingForPackets = 0;
            }
        }
    }

    private void executeCommand(MAnswer answer) throws IOException, ClassNotFoundException {
        switch (answer.type()) {
            case MCommand.Type.ADD:
                IdentifiedMoomintroll[] moomintrolls = answer.parseAdd();
                if (log.isLoggable(Level.INFO)) {
                    log.info("Command: ADD" + Arrays.toString(moomintrolls));
                }
                commandHandler.add(moomintrolls);
                break;
            case MCommand.Type.REMOVE:
                long[] ids = answer.parseRemove();
                if (log.isLoggable(Level.INFO)) {
                    log.info("Command: REMOVE" + Arrays.toString(ids));
                }
                commandHandler.remove(ids);
                break;
            case MCommand.Type.UPDATE:
                IdentifiedMoomintroll im = answer.parseUpdate();
                if (log.isLoggable(Level.INFO)) {
                    log.info("Command: UPDATE " + im);
                }
                commandHandler.update(im);
                break;
            case MCommand.Type.SELECT_ALL:
                IdentifiedMoomintroll[] identifiedMoomintrolls = answer.parseSelectAll();
                commandHandler.reload(identifiedMoomintrolls);
                if (log.isLoggable(Level.INFO)) {
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Command: SELECT_ALL" + "\n" + Arrays.toString(identifiedMoomintrolls));
                    } else {
                        log.info("Command: SELECT_ALL");
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal type of command = " + answer.type());
        }
    }
}
