package audioLayer;

import CMPC3M06.AudioPlayer;
import CMPC3M06.AudioRecorder;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Random;

public class AudioUtils {

    private static AudioRecorder RECORDER = null;
    private static AudioPlayer PLAYER = null;

    static {
        try {
            RECORDER = new AudioRecorder();
            PLAYER = new AudioPlayer();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays a block of audio
     * @param audioBlock
     */
    public static void play(byte[] audioBlock){
        try{
            PLAYER.playBlock(audioBlock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Records the users audio.
     * @return byte[] if users audio data.
     */
    public static byte[] record(){
        try {
            byte[] block = RECORDER.getBlock();
            //PLAYER.playBlock(block);
            return block;
        } catch (IOException e) {
            System.out.println("Failed to recoded block...");
            return new byte[0];
        }
    }

    public static byte[] generateNoiseBlock(){
        byte[] block = new byte[AudioLayer.BLOCK_SIZE];
        Random random = new Random();
        for(int i = 0; i < block.length; i++){
            block[i] = (byte) random.nextInt(256);
        }
        return block;
    }
}
