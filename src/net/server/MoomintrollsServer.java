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
import java.util.HashMap;
import java.util.Map;
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

    Map<String, ClientManager> clientManagers;

    public MoomintrollsServer(int port, MoomintrollsDatabase database) throws IOException {
        this.PORT = port;
        datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(PORT));
        this.database = database;
        inputData = new byte[MPacket.PACKETS_LENGTH];
        inputDataBuffer = ByteBuffer.wrap(inputData);
        isAlive = true;
        clientManagers = new HashMap<>();
    }

    public void receivePacket() {
        Arrays.fill(inputData, (byte) 0);
        inputDataBuffer.clear();
        SocketAddress receiverAddress = null;
        try {
            receiverAddress = datagramChannel.receive(inputDataBuffer);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to receive message", e);
            return;
        }

        String sReceiverAddress = receiverAddress.toString();

        if (log.isLoggable(Level.FINER)) {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Received packet from " + sReceiverAddress +
                        "\nData: \"" + Arrays.toString(inputData) + "\"");
            } else {
                log.finer("Received packet from " + sReceiverAddress);
            }
        }

        if (!clientManagers.containsKey(sReceiverAddress)) {
            ClientManager ce = new ClientManager(receiverAddress, database);
            clientManagers.put(sReceiverAddress, ce);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Created manager for new client " + sReceiverAddress);
            }
            new Thread(ce).start();
        }
        clientManagers.get(sReceiverAddress).addPacket(new MPacket(inputData.clone()));
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void close() {
        isAlive = false;
        clientManagers.values().forEach(ClientManager::stop);
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
                    "../../logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        final int port = 1238;
        MoomintrollsServer moomintrollsServer;
        MoomintrollsDatabase database = null;
        try {
            database = new MoomintrollsDatabase(
                    "localhost",
                    5432,
                    "mooman",
                    "usr",
                    "123456"
            );

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