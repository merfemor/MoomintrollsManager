package mbean;

import net.server.MoomintrollsServer;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.net.SocketAddress;

public class UsersInfo extends NotificationBroadcasterSupport implements UsersInfoMBean {
    private long sequenceNumber = 0;
    private MoomintrollsServer moomintrollsServer;

    public UsersInfo(MoomintrollsServer moomintrollsServer) {
        this.moomintrollsServer = moomintrollsServer;
    }

    public void reportDisconnect(SocketAddress socketAddress) {
        sendNotification(new Notification(
                "Disconnect",
                this.getClass().getName(),
                sequenceNumber++,
                "User " + socketAddress + " disconnected"));
    }

    @Override
    public int getUsersNumber() {
        return moomintrollsServer.clientManagers.size();
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{
                AttributeChangeNotification.ATTRIBUTE_CHANGE
        };

        String name = AttributeChangeNotification.class.getName();
        String description = "Someone disconnected";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }
}
