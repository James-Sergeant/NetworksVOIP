package audioLayer;

import CMPC3M06.AudioPlayer;
import voipLayer.VoipLayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Vector;

public class VoicePlayer extends AudioPlayer implements Runnable{

    private final Vector<byte[]> BUFFER;
    private boolean playing = true;

    private final byte[] EMPTY_AUDIO_BLOCK = new byte[512];
    private volatile byte[] audioBlock;

    public VoicePlayer() throws LineUnavailableException {
        BUFFER = new Vector<>();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(playing) {
            playAudioBlock();
        }
    }

    public void togglePlaying(){
        playing ^= true;
    }

    public synchronized void storeAudioBlock(byte[] audioBlock){
        BUFFER.add(audioBlock);
    }

    private void playAudioBlock(){
        try {
            if(!VoipLayer.BUFFER.isEmpty()) {
                audioBlock = VoipLayer.BUFFER.popBlock();
                playBlock(audioBlock);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
