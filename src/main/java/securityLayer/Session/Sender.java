package securityLayer.Session;

import audioLayer.AudioLayer;
import securityLayer.Securitylayer;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import voipLayer.VoipLayer;

import javax.sound.sampled.LineUnavailableException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Sender {

    private boolean sending = false;
    private final int PORT;
    private final InetAddress IP;
    private final DatagramSocket SENDER_SOCKET;


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
}
