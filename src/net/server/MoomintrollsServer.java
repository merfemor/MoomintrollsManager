package net.server;

import net.protocol.MPacket;
import psql.MoomintrollsDatabase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MoomintrollsServer {
    private final int PORT;
    private DatagramChannel datagramChannel;
    private MoomintrollsDatabase database;
    public static Logger log = Logger.getLogger(MoomintrollsServer.class.getName());

    private byte[] inputData;
    private ByteBuffer inputDataBuffer;

    private boolean isAlive;

    private Map<InetSocketAddress, ClientManager> clientManagers;
    private ChangesNotifier changesNotifier;

    public MoomintrollsServer(int port, MoomintrollsDatabase database) throws IOException {
        this.PORT = port;
        datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(PORT));
        this.database = database;
        inputData = new byte[MPacket.PACKETS_LENGTH];
        inputDataBuffer = ByteBuffer.wrap(inputData);
        isAlive = true;
        clientManagers = new ConcurrentHashMap<>();
        changesNotifier = new ChangesNotifier(datagramChannel);
        new Thread(changesNotifier).start();
    }

    public void receivePacket() {
        Arrays.fill(inputData, (byte) 0);
        inputDataBuffer.clear();
        SocketAddress receiverAddress;
        try {
            receiverAddress = datagramChannel.receive(inputDataBuffer);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to receive message", e);
            return;
        }

        InetSocketAddress sReceiverAddress = (InetSocketAddress) receiverAddress;

        if (log.isLoggable(Level.FINER)) {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Received packet from " + sReceiverAddress +
                        "\nData: \"" + Arrays.toString(inputData) + "\"");
            } else {
                log.finer("Received packet from " + sReceiverAddress);
            }
        }

        for (Map.Entry<InetSocketAddress, ClientManager> entry : clientManagers.entrySet()) {
            if (entry.getKey().equals(sReceiverAddress)) {
                entry.getValue().addPacket(new MPacket(inputData.clone()));
                return;
            }
        }

        ClientManager ce = new ClientManager(receiverAddress, database, changesNotifier);
        clientManagers.put(sReceiverAddress, ce);
        changesNotifier.addRecipient(ce);

        ce.setDisconnectionHandler(() -> {
            ce.stop();
            clientManagers.remove(sReceiverAddress);
            changesNotifier.removeRecipient(ce);
            log.info("Disconnected client " + receiverAddress);
        });
        log.info("Connected client " + receiverAddress);
        new Thread(ce).start();
        ce.addPacket(new MPacket(inputData.clone()));
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void close() {
        isAlive = false;
        clientManagers.values().forEach(ClientManager::stop);
        changesNotifier.stop();
        log.info("Client managers stopped");

        try {
            database.close();
            log.info("Database on " + database.getUrl() + " stopped");
            datagramChannel.close();
            log.info("Channel on port " + PORT + " closed");
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed to close database on url " + database.getUrl(), e);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to close channel on port " + PORT, e);
        }
    }

    public static void main(String[] args) {
        try {
            // TODO: replace logging.properties
            LogManager.getLogManager().readConfiguration(MoomintrollsServer.class.getResourceAsStream(
                    "logging.properties"));
        } catch (Exception e) {
            log.warning("Could not setup logger configuration");
        }

        final int port = 1238;
        MoomintrollsServer moomintrollsServer;
        MoomintrollsDatabase database = null;
        try {
            log.info("Connecting to database...");
            database = new MoomintrollsDatabase(
                    "localhost",
                    5432,
                    "mooman",
                    "usr",
                    "123456"
            );
            log.info("Connected to database " + database.getUrl());

            moomintrollsServer = new MoomintrollsServer(port, database);
            log.info("Started UDP server on port " + port);

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to start server on port " + port + ": failed to create channel", e);
            return;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed to start server on port " + port +
                    ": failed to connect to database\n" + "URL: " + database.getUrl(), e);
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("The server was stopped by user");
            moomintrollsServer.close();
        }));

        while (moomintrollsServer.isAlive()) {
            moomintrollsServer.receivePacket();
        }
    }
}