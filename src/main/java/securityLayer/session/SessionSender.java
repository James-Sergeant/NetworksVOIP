package securityLayer.session;

import java.io.IOException;
import java.net.*;

public class SessionSender {
    private static final int PORT = 55555;
    private final InetAddress IP;
    private final DatagramSocket SOCKET;

    public SessionSender(String IP, byte[] data) throws IOException {
        this.IP = InetAddress.getByName(IP.replace("/",""));
        SOCKET = new DatagramSocket();
        DatagramPacket dp = new DatagramPacket(data,data.length,this.IP,PORT);
        SOCKET.send(dp);
        System.out.println("Session Packet Sent");
    }
}
