package audioLayer;

import com.Config;
import com.Layer;
import utils.Utils;

public class AudioLayer extends Layer {

    public static final double BLOCK_LENGTH = 0.032;
    public static final int BLOCK_SIZE = 512;

    private byte[] prevAudio;
    private byte[] noiseBlock = AudioUtils.generateNoiseBlock();
    private final byte[] EMPTY_AUDIO_BLOCK = new byte[512];
    private int lossBurstLength = 0;
    private final int blocksPerPacket = Config.preset.getBLOCKS_PER_PACKET();

    public static final byte[] NOISE_BLOCK = new byte[]{21, 0, 17, 0, 27, 0, 43, 0, 23, 0, 22, 0, 28, 0, -5, -1, -1, -1, 25, 0, -1, -1, -19, -1, -28, -1, -36, -1, -43, -1, -33, -1, -63, -1, -75, -1, -59, -1, -33, -1, -32, -1, -14, -1, -4, -1, -18, -1, -2, -1, 24, 0, 41, 0, 51, 0, 46, 0, 52, 0, 71, 0, 39, 0, 36, 0, 39, 0, 45, 0, 6, 0, -8, -1, -2, -1, 31, 0, 36, 0, 41, 0, 38, 0, 1, 0, -29, -1, -15, -1, 13, 0, 9, 0, 17, 0, 7, 0, 12, 0, 18, 0, 3, 0, 2, 0, -4, -1, 9, 0, 25, 0, 31, 0, 22, 0, 21, 0, 21, 0, 30, 0, 34, 0, 20, 0, 34, 0, 13, 0, -11, -1, -14, -1, -46, -1, -50, -1, -34, -1, -37, -1, -32, -1, -58, -1, -65, -1, -36, -1, -30, -1, -41, -1, -54, -1, -52, -1, -26, -1, 2, 0, 0, 0, -16, -1, -10, -1, -19, -1, -22, -1, -14, -1, -7, -1, -28, -1, -25, -1, -9, -1, -24, -1, -37, -1, -30, -1, -24, -1, -26, -1, -15, -1, 25, 0, 30, 0, 41, 0, 48, 0, 44, 0, 54, 0, 65, 0, 63, 0, 28, 0, 37, 0, 28, 0, -10, -1, -6, -1, -37, -1, -24, -1, 3, 0, 4, 0, 7, 0, 17, 0, 40, 0, 47, 0, 67, 0, 34, 0, 24, 0, -5, -1, -18, -1, -11, -1, 4, 0, -30, -1, -63, -1, -42, -1, -43, -1, -64, -1, -51, -1, -21, -1, -16, -1, -45, -1, -67, -1, -77, -1, -90, -1, -69, -1, -45, -1, -33, -1, -26, -1, 26, 0, 44, 0, 22, 0, 15, 0, 25, 0, 43, 0, 57, 0, 33, 0, -7, -1, 17, 0, 52, 0, 70, 0, 50, 0, 66, 0, 65, 0, 79, 0, 56, 0, 18, 0, -6, -1, -22, -1, -12, -1, -2, -1, -1, -1, -3, -1, 24, 0, -14, -1, -20, -1, 8, 0, 6, 0, 17, 0, 5, 0, -15, -1, -30, -1, -26, -1, -36, -1, -32, -1, -30, -1, -13, -1, 12, 0, 0, 0, -12, -1, -14, -1, -1, -1, 23, 0, 25, 0, 27, 0, 38, 0, 22, 0, 7, 0, 6, 0, 5, 0, -5, -1, -11, -1, -4, -1, 13, 0, 23, 0, 17, 0, -32, -1, -64, -1, -72, -1, -74, -1, -81, -1, -42, -1, -17, -1, -25, -1, 1, 0, -10, -1, -8, -1, -14, -1, -23, -1, -7, -1, 6, 0, 21, 0, 28, 0, 8, 0, -9, -1, -1, -1, -32, -1, -37, -1, -52, -1, -32, -1, -5, -1, -16, -1, -33, -1, -34, -1, -20, -1, -18, -1, 13, 0, 12, 0, 11, 0, 23, 0, 26, 0, 33, 0, 42, 0, 38, 0, 38, 0, 41, 0, 41, 0, 40, 0, 55, 0, 50, 0, 47, 0, 55, 0, 67, 0, 89, 0, 61, 0, 33, 0, 9, 0, 18, 0, 14, 0, 6, 0, 7, 0, 30, 0, 29, 0, 9, 0,};


    public AudioLayer() {
        header = new byte[512];
        prevAudio = new byte[BLOCK_SIZE];

    }

    @Override
    public byte[] addHeader(byte[] payload) {
        payload = new byte[512 * blocksPerPacket];
        for(int i = 0; i < blocksPerPacket; i++) {
            header = AudioUtils.record();
            System.arraycopy(header,0, payload, i*512, header.length);
        }
        return payload;
    }

    @Override
    public byte[] removeHeader(byte[] payload) {
        for(int i = 0; i < blocksPerPacket; i++) {
            byte[] audioBlock = new byte[512];

            // If packet's audio data was lost
            if (payload == null) {
                lossBurstLength++;
                if (Config.preset.getPACKET_LOSS_SOLUTION() == Config.PACKET_LOSS_SOLUTION.REPETITION) {
                    audioBlock = AudioUtils.reduceAudioVolume(prevAudio, 1/(float)lossBurstLength);
                } else if (Config.preset.getPACKET_LOSS_SOLUTION() == Config.PACKET_LOSS_SOLUTION.BLANK_FILL_IN) {
                    audioBlock = EMPTY_AUDIO_BLOCK; // FILL-IN Empty Audio
                } else if(Config.preset.getPACKET_LOSS_SOLUTION() == Config.PACKET_LOSS_SOLUTION.NOISE){
                    audioBlock = noiseBlock;
                }else {
                    System.out.println("[ERROR] Audio Layer Payload=null");
                }
            } else {
                System.arraycopy(payload,512 * i, audioBlock, 0, audioBlock.length);
                lossBurstLength = 0;
                prevAudio = audioBlock;
            }

            // Get audio data from payload and add to the buffer
            extractHeader(audioBlock);

            for(int j = 0; j < 512; j+=2){
               System.out.println(Utils.blockToShort(audioBlock[j], audioBlock[j+1]));
            }

            // Send audio data to Player
            AudioUtils.play(header);
            //AudioUtils.play(AudioUtils.removeLowerAudio(header));
        }
        return null;
    }

}
