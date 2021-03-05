package voipLayer;

import audioLayer.AudioLayer;
import securityLayer.Securitylayer;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver implements Runnable{
    public static byte DATAGRAM_SOCKET_ONE = 0;
    public static byte DATAGRAM_SOCKET_TWO = 1;
    public static byte DATAGRAM_SOCKET_THREE = 2;

    private final DatagramSocket SOCKET;
    private final int PORT;
    private boolean receiving = false;
    private int TIMEOUT = 32;
    private int packetsReceived = 0;

    // Layers
    private final AudioLayer audioLayer = new AudioLayer();
    private final VoipLayer voipLayer = new VoipLayer();
    private final Securitylayer securitylayer = new Securitylayer();

    /**
     * The basic receiver
     * @throws LineUnavailableException
     * @throws SocketException
     */
    public Receiver() throws LineUnavailableException, SocketException {
        this.PORT = 55555;
        this.SOCKET = new DatagramSocket3(this.PORT);
        this.SOCKET.setSoTimeout(TIMEOUT);
    }

    public Receiver(int PORT) throws LineUnavailableException, SocketException {
        this.PORT = PORT;
        this.SOCKET = new DatagramSocket3(this.PORT);
        this.SOCKET.setSoTimeout(TIMEOUT);
    }

    public void toggleReceiving(){
        receiving ^= true;
    }


    @Override
    public void run() {
        toggleReceiving();
        while (receiving) {
            getPacket();
        }
    }

    private void getPacket(){
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length);

        try {
            // Waits to recieve a packet for 32ms
            SOCKET.receive(packet);
            System.out.println("Receive");

            buffer = securitylayer.removeHeader(buffer);
            buffer = voipLayer.removeHeader(buffer);

        } catch (IOException e) {
            System.out.println("TIMEOUT");
        }


        if(voipLayer.allowPlaying()) {
            System.out.println("Play");
            audioLayer.removeHeader(voipLayer.getAudioBlock());
        }
    }
}
