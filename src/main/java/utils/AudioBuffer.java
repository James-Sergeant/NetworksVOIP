package utils;

import java.util.Vector;

import static audioLayer.AudioLayer.BLOCK_LENGTH;
import static audioLayer.AudioLayer.BLOCK_SIZE;

public class AudioBuffer {

    private final int MAX_PACKET_NUM = 255;

    private final double BUFFER_DELAY; // Seconds
    public final int BUFFER_LENGTH;
    private final Vector<byte[]> BUFFER;
    private int startPacketNumber;

    public AudioBuffer(double buffer_delay){
        BUFFER_DELAY = buffer_delay;
        BUFFER_LENGTH = (int)Math.ceil(BUFFER_DELAY/BLOCK_LENGTH);
        BUFFER = new Vector<>();
        startPacketNumber = 0;
    }

    public void addBlock(int packetNumber, byte[] block){
        if(isWithinBufferLength(packetNumber)){
            int bufferIndex = calculateBufferIndex(packetNumber);
            BUFFER.insertElementAt(block, bufferIndex);
        }
    }

    private int calculateBufferIndex(int packetNumber){
        if(startPacketNumber < MAX_PACKET_NUM - BUFFER_LENGTH){ // 0 < packetNum < 239
            return packetNumber - startPacketNumber;
        }else{
            int circularMax = (startPacketNumber+BUFFER_LENGTH)-MAX_PACKET_NUM;
            int distToMax = MAX_PACKET_NUM - startPacketNumber;
            if(packetNumber > MAX_PACKET_NUM - distToMax){
                return packetNumber - startPacketNumber;
            }else if(packetNumber <= circularMax){
                return distToMax + packetNumber;
            }
        }
    }

    private boolean isWithinBufferLength(int packetNumber){
        if(startPacketNumber < MAX_PACKET_NUM - BUFFER_LENGTH){ // 0 < packetNum < 239
            if(packetNumber >= startPacketNumber && packetNumber <= startPacketNumber + BUFFER_LENGTH){
                return true;
            }
        }else{
            int circularMax = (startPacketNumber+BUFFER_LENGTH)-MAX_PACKET_NUM;
            if(packetNumber >= startPacketNumber && packetNumber <= circularMax){
                return true;
            }
        }
        return false;
    }

    public byte[] popBlock(){
        byte[] block = BUFFER.get(0);
        BUFFER.remove(0);
        startPacketNumber++;
        return block;
    }

    public int size(){
        return BUFFER.size();
    }
}
