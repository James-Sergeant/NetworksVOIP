package audioLayer;

import CMPC3M06.AudioPlayer;
import CMPC3M06.AudioRecorder;
import utils.Utils;
import voipLayer.Interpolator;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

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
        block = Interpolator.getInterpolatedBlock(AudioLayer.NOISE_BLOCK, AudioLayer.NOISE_BLOCK, 1, 0);
        /*Random random = new Random();
        for(int i = 0; i < block.length; i+=2){
            short sample = (short) random.nextInt(65536);
            block[i] = (byte) (sample);
            block[i+1] = (byte) (sample >> 8);
        }*/
        return block;
    }

    public static byte[] reduceAudioVolume(byte[] audio, float percentage){
        byte[] newAudio = new byte[audio.length];
        for(int i = 0; i < audio.length; i+=2){
            short sample = (short) ((float) Utils.blockToShort(audio[i], audio[i+1]) * percentage);
            newAudio[i] = (byte) (sample);
            newAudio[i+1] = (byte) (sample >> 8);
        }
        return newAudio;
    }

    public static byte[] removeLowerAudio(byte[] audio){
        byte[] newAudio = new byte[audio.length];

        for(int i = 0; i < audio.length; i+= 2){

            short sample = Utils.blockToShort(audio[i], audio[i+1]);

            if(sample > 200 || sample < -200) {
                newAudio[i] = audio[i];
                newAudio[i+1] = audio[i+1];
            }else{
                newAudio[i] = (byte) 0;
                newAudio[i+1] = (byte) 0;
            }
        }

        return newAudio;
    }

    public static byte[] averageAudio(byte[] audio){
        byte[] avgAudio = new byte[audio.length];
        for(int i = 0; i < audio.length; i+= 2){
            short total = 0;
            int num = 1;
            total += Utils.blockToShort(audio[i], audio[i+1]);
            System.out.print(total+" ");
            if(i != 0){
                total += Utils.blockToShort(audio[i-2], audio[i-1]);
                num++;
            }
            if(i != audio.length-2){
                total += Utils.blockToShort(audio[i+2], audio[i+3]);
                num++;
            }

            short average = (short) (total / num);
            avgAudio[i] = (byte) average;
            avgAudio[i+1] = (byte) (average >> 8);
        }

        return avgAudio;
    }
}
