package voipLayer;

import audioLayer.AudioLayer;
import audioLayer.AudioUtils;
import com.Analyzer;
import securityLayer.Securitylayer;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.*;

public class Sender implements Runnable{
    public static byte DATAGRAM_SOCKET_ONE = 0;
    public static byte DATAGRAM_SOCKET_TWO = 1;
    public static byte DATAGRAM_SOCKET_THREE = 2;

    private boolean sending = false;
    private final int PORT;
    private final InetAddress IP;
    private final DatagramSocket SENDER_SOCKET;

    // Layers
    private final AudioLayer audioLayer = new AudioLayer();
    private final VoipLayer voipLayer = new VoipLayer();
    private final Securitylayer securitylayer = new Securitylayer();


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
        SENDER_SOCKET = new DatagramSocket();
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
        SENDER_SOCKET = new DatagramSocket();
    }

    /**
     * Method for Runnable interface, is used when a User wishes to send voice to a client.
     */
    @Override
    public void run() {
        System.out.println("Running Sender...");
        try {
            sendDatagramSocketOne();
        } catch (AlreadySendingException e) {
            System.out.println("There is already a sender thread running for this object.");
        }
    }

    /**
     * Used to toggle if the object is currently sending.
     */
    public void toggleSending(){
        sending ^= true;
    }

    /**
     * Sends the data using the Datagram Socket One.
     * @throws AlreadySendingException
     */
    public void sendDatagramSocketOne() throws AlreadySendingException {
        if(sending){throw  new AlreadySendingException();}
        toggleSending();
        while(sending){
            DatagramPacket packet = createPacket();

            try {
                SENDER_SOCKET.send(packet);
            } catch (IOException e) {
                System.out.println("Failed to send packet...");
            }
        }
    }

    private DatagramPacket createPacket(){
        // Create Payload Buffer
        byte[] payload = new byte[0];

        payload = audioLayer.addHeader(payload);
        payload = voipLayer.addHeader(payload);
        payload = securitylayer.addHeader(payload);

        return new DatagramPacket(payload, payload.length,IP,PORT);
    }


    /**
     * Is thrown if audio is already transmitting.
     */
    class AlreadySendingException extends Exception{
        AlreadySendingException(){
            super("Audio already transmitting");
        }
    }
}
