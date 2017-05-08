package net;

import java.io.IOException;
import java.net.*;

public class Server extends InetPackageManager {

    public Server(int port) {
        super(port);
    }

    public static void main(String[] args) {
        int port = 2222;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        Server server = new Server(port);
        if(server.startSocket()) {
            System.out.println("Created socket on port " + port);
        } else  {
            System.out.println("Failed to create socket on port " + port);
            return;
        }

        while (true) {
            DatagramPacket packet;
            try {
                packet = server.getPacket(1024);
            } catch (IOException e) {
                System.out.println("Failed to get package");
                continue;
            }
            System.out.println("Get message: " + new String(packet.getData()));

            try {
                server.sendPacket("all right".getBytes(), packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                System.out.println("Failed to send package");
                continue;
            }
            System.out.println("Package successful sent");
        }
    }
}
