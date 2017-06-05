package net.server;

import mbean.CommandsCount;
import mbean.UsersInfo;
import net.protocol.MPacket;
import ru.ifmo.cs.korm.Session;
import trolls.Moomintroll;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MoomintrollsServer {
    public static Logger log = Logger.getLogger(MoomintrollsServer.class.getName());
    public Map<InetSocketAddress, ClientManager> clientManagers;
    private DatagramChannel datagramChannel;
    private byte[] inputData;
    private ByteBuffer inputDataBuffer;
    private boolean isAlive;
    private ChangesManager changesManager;

    private UsersInfo usersInfo;
    private CommandsCount commandsCount;

    private Session databaseSession;

    public MoomintrollsServer(InetSocketAddress inetSocketAddress, Session databaseSession) throws IOException, SQLException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(inetSocketAddress);
        this.databaseSession = databaseSession.addClass(Moomintroll.class);
        inputData = new byte[MPacket.PACKETS_LENGTH];
        inputDataBuffer = ByteBuffer.wrap(inputData);
        isAlive = true;
        clientManagers = new ConcurrentHashMap<>();
        changesManager = new ChangesManager(datagramChannel);
        new Thread(changesManager).start();
    }

    public static void main(String[] args) {
        try {
            // TODO: replace logging.properties
            LogManager.getLogManager().readConfiguration(MoomintrollsServer.class.getResourceAsStream(
                    "logging.properties"));
        } catch (Exception e) {
            log.warning("Could not setup logger configuration");
        }
        InetSocketAddress isa = new InetSocketAddress(1111);
        MoomintrollsServer moomintrollsServer = null;
        try {
            log.info("Connecting to database...");
            DriverManager.registerDriver(new org.postgresql.Driver());
            String url = "jdbc:postgresql://localhost:5432/mooman?user=usr&password=123456";
            Session session = new Session(DriverManager.getConnection(url), MPostgreSQLSyntax.get());
            log.info("Connected to database " + session.getUrl());

            if (args.length >= 2) {
                try {
                    isa = new InetSocketAddress(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
                } catch (Exception ignored) {
                }
            }
            moomintrollsServer = new MoomintrollsServer(isa, session);
            log.info("Started UDP server " + isa);

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to start server " + isa + ": failed to create channel", e);
            return;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed to start server " + isa +
                    ": failed to connect to database", e);
            return;
        }

        MoomintrollsServer finalMoomintrollsServer = moomintrollsServer;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("The server was stopped");
            finalMoomintrollsServer.close();
        }));

        try {
            moomintrollsServer.registerMBean();
        } catch (MalformedObjectNameException | NotCompliantMBeanException | InstanceAlreadyExistsException | MBeanRegistrationException e) {
            log.log(Level.SEVERE, "Failed to init MBean utils", e);
        }

        while (moomintrollsServer.isAlive()) {
            moomintrollsServer.receivePacket();
        }
    }

    public void registerMBean() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        usersInfo = new UsersInfo(this);
        commandsCount = new CommandsCount();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName userInfoMbean = new ObjectName("mbean:name=UserInfo");
        ObjectName commandMbean = new ObjectName("mbean:name=CommandsCount");

        mbs.registerMBean(usersInfo, userInfoMbean);
        mbs.registerMBean(commandsCount, commandMbean);
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

        ClientManager ce = new ClientManager(receiverAddress, changesManager, databaseSession);
        ce.registerCommandsCounter(commandsCount);
        clientManagers.put(sReceiverAddress, ce);
        changesManager.addRecipient(ce);

        ce.setDisconnectionHandler(() -> {
            ce.stop();
            if (usersInfo != null) {
                usersInfo.reportDisconnect(ce.getSocketAddress());
            }
            clientManagers.remove(sReceiverAddress);
            changesManager.removeRecipient(ce);
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
        changesManager.stop();
        log.info("Client managers stopped");
        try {
            databaseSession.close();
            log.info("Database on " + databaseSession.getUrl() + " stopped");
            datagramChannel.close();
            log.info("Channel on " + datagramChannel.getLocalAddress() + " closed");
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed to close database session", e);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to close channel", e);
        }
    }
}