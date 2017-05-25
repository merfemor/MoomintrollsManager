import cui.ConsoleMoomintrollsManager;
import gui.MainWindow;
import net.client.MoomintrollsClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class MoomintrollsManager {
    public static void main(String[] args) {
        // cmd-mode
        if(args.length > 0 && args[0].equals("--cmd")) {
            ConsoleMoomintrollsManager consoleMoomintrollsManager = new ConsoleMoomintrollsManager();
            if(args.length > 1) {
                String envName = args[1];
                consoleMoomintrollsManager.setPathVariableName(envName);
            }
            consoleMoomintrollsManager.start();
        } else {
            MainWindow mainWindow = new MainWindow("LAB_PATH");
            if (args.length > 0) {
                try {
                    InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
                    mainWindow.setDefaultSocketAdress(socketAddress);
                } catch (Exception e) {
                    MoomintrollsClient.log.warning("Failed to get SocketAddress from " + args[0]);
                }
            }
            mainWindow.setVisible(true);
        }
    }
}
