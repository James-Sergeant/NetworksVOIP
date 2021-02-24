package audioLayer;

import CMPC3M06.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Vector;

public class VoicePlayer extends AudioPlayer implements Runnable{

    private final Vector<byte[]> BUFFER;
    private boolean playing = true;

    public VoicePlayer() throws LineUnavailableException {
        BUFFER = new Vector<>();
    }

    @Override
    public void run() {
        while(playing) {
            if (BUFFER.size() > 10) playAudioBlock();
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
            playBlock(BUFFER.get(0));
            synchronized (BUFFER) {
                BUFFER.remove(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
