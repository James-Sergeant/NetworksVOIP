package securityLayer.Session;

import audioLayer.AudioLayer;
import securityLayer.Securitylayer;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import voipLayer.VoipLayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.*;

public class Sender{

    private final int PORT;
    private final InetAddress IP;
    private final DatagramSocket SENDER_SOCKET;
    public static boolean requestReceived = false;


    /**
     * Creates a default sender using localhost and the port 55555
     * @throws UnknownHostException
     * @throws SocketException
     * @throws LineUnavailableException
     */
    public Sender() throws UnknownHostException, SocketException, LineUnavailableException {
        PORT = 55555;
        IP = InetAddress.getByName("localhost");
        SENDER_SOCKET = new DatagramSocket3();
    }

    /**
     * Creates a sender with a chosen client IP, with the default port 55555
     * @param IP String: the users IP address.
     * @throws UnknownHostException
     * @throws SocketException
     * @throws LineUnavailableException
     */
    public Sender(String IP) throws UnknownHostException, SocketException, LineUnavailableException {
        PORT = 55555;
        this.IP = InetAddress.getByName(IP);
        SENDER_SOCKET = new DatagramSocket3();
    }

    /**
     * Creates a sender with the IP and the port configured.
     * @param IP String: The clients IP.
     * @param PORT Int: The port to be used.
     * @throws UnknownHostException
     * @throws SocketException
     * @throws LineUnavailableException
     */
    public Sender(String IP,int PORT) throws UnknownHostException, SocketException, LineUnavailableException {
        this.PORT = PORT;
        this.IP = InetAddress.getByName(IP);
        SENDER_SOCKET = new DatagramSocket3();
    }

    /**
     * Sends a packet.
     * @param data byte[]: The data to be sent.
     */
    public void send(byte[] data){
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length,IP,PORT);
        try {
            SENDER_SOCKET.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
