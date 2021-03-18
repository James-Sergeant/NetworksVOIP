package voipLayer;

import audioLayer.AudioLayer;
import com.Config;
import securityLayer.SecurityLayer;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Receiver implements Runnable{

    private DatagramSocket RECEIVER_SOCKET;
    private final int PORT;
    private boolean receiving = false;
    private int TIMEOUT = Config.preset.getTIMEOUT();

    // Layers
    private final AudioLayer audioLayer = new AudioLayer();
    private final VoipLayer voipLayer = new VoipLayer();
    private final SecurityLayer securitylayer = new SecurityLayer();

    /**
     * The basic receiver
     * @throws SocketException
     */
    public Receiver() throws SocketException {
        this.PORT = 55555;
        setSocket();
        this.RECEIVER_SOCKET.setSoTimeout(TIMEOUT);
        
        
    }

    public Receiver(int PORT) throws SocketException {
        this.PORT = PORT;
        setSocket();
        this.RECEIVER_SOCKET.setSoTimeout(TIMEOUT);
    }

    
    private void setSocket() throws SocketException {
        switch(Config.preset.getDATAGRAM_SOCKET()){
            case 2:
                this.RECEIVER_SOCKET = new DatagramSocket2(PORT);
                break;
            case 3:
                this.RECEIVER_SOCKET = new DatagramSocket3(PORT);
                break;
            default:
                this.RECEIVER_SOCKET = new DatagramSocket(PORT);
                break;
        }
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
        byte[] buffer = new byte[8096];
        DatagramPacket packet = new DatagramPacket(buffer,0,buffer.length);

        try {
            // Waits to recieve a packet for 32ms
            RECEIVER_SOCKET.receive(packet);
            buffer = voipLayer.removeHeader(buffer);

        } catch (IOException ignored) {
        }


        if(voipLayer.allowPlaying()) {
            audioLayer.removeHeader(securitylayer.removeHeader(voipLayer.getAudioBlock()));
        }
    }
}
