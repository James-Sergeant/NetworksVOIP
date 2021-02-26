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
    private int endPacketNumber;
    private int currentLength;

    public AudioBuffer(double buffer_delay){
        BUFFER_DELAY = buffer_delay;
        BUFFER_LENGTH = (int)Math.ceil(BUFFER_DELAY/BLOCK_LENGTH);
        BUFFER = new Vector<>();

        startPacketNumber = 0;
        endPacketNumber = 0;
        currentLength = 0;

        for(int i = 0; i < BUFFER_LENGTH; i++){
            BUFFER.add(null);
        }
    }

    public void insertBlock(int packetNumber, byte[] block){
        if(currentLength == BUFFER_LENGTH) popBlock();

        int bufferIndex = calculateBufferIndex(packetNumber);
        Logger.log("P-Numbe "+packetNumber);
        Logger.log("B-Index "+bufferIndex);
        Logger.log("start "+startPacketNumber);
        Logger.log("end "+endPacketNumber);
        if(bufferIndex != -1){
            BUFFER.set(bufferIndex,block);
            if(currentLength != BUFFER_LENGTH) currentLength++;
            Logger.log(this);
        }
    }
    /*
    private int calculateBufferIndex(int packetNumber){
        if(startPacketNumber < MAX_PACKET_NUM - BUFFER_LENGTH){ // 0 < startPacketNum < 239
            return packetNumber - startPacketNumber;
        }else{// 239 <= startPacketNum < 256
            int circularMax = (startPacketNumber+BUFFER_LENGTH)-MAX_PACKET_NUM; // 0 <= circularMax < 16
            int distToMax = MAX_PACKET_NUM - startPacketNumber;// 0 <= distToMax < 16
            if(packetNumber > startPacketNumber){
                return packetNumber - startPacketNumber;
            }else if(packetNumber <= circularMax){
                return distToMax + packetNumber;
            }else{
                return -1;
            }
        }
    }
*/
    private int calculateBufferIndex(int packetNumber){
        if(packetNumber >= startPacketNumber){
            if(packetNumber <= endPacketNumber){
                return packetNumber - startPacketNumber;
            }else if(packetNumber <= MAX_PACKET_NUM){
                return packetNumber - startPacketNumber;
            }
        }else if(packetNumber <= endPacketNumber){
            return (MAX_PACKET_NUM - startPacketNumber) + packetNumber;
        }

        return -1;
        /*
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

         */
    }

    public byte[] popBlock(){
        byte[] block = BUFFER.get(0);
        startPacketNumber = nextPointer(startPacketNumber);
        endPacketNumber = nextPointer(startPacketNumber);

        BUFFER.remove(0);
        BUFFER.add(null);

        Logger.log(this);
        return block;
    }

    private int nextPointer(int pointer){
        if(pointer == MAX_PACKET_NUM) return 0;
        return pointer + 1;
    }

    public int size(){
        return BUFFER.size();
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i< BUFFER_LENGTH; i++){
            if(BUFFER.get(i) == null){
                s += "0, ";
            }else{
                s += "1, ";
            }
        }
        return "AudioBuffer{" +s+ '}';
    }

    public static void main(String[] args) {
        AudioBuffer buffer = new AudioBuffer(0.5);
        byte[] testBlock = new byte[1];

        // TESTING SIMPLE INSERTING AND POPPING BLOCKS
        /*
        buffer.insertBlock(0, testBlock);
        buffer.insertBlock(1, testBlock);
        buffer.insertBlock(2, testBlock);
        buffer.insertBlock(4, testBlock);
        buffer.insertBlock(3, testBlock);
        buffer.insertBlock(14, testBlock);

        buffer.popBlock();
        buffer.popBlock();

        buffer.insertBlock(15, testBlock);
        buffer.insertBlock(7,testBlock);
        buffer.insertBlock(8,testBlock);
        buffer.insertBlock(10,testBlock);
        buffer.insertBlock(9,testBlock);

        buffer.popBlock();
        buffer.popBlock();
        buffer.popBlock();
        buffer.popBlock();
        buffer.popBlock();
        */

        // TESTING CIRCULAR BLOCK INSERTION
        buffer.insertBlock(0, null);
        for(int i = 1; i < 400; i++){
            buffer.insertBlock(i, i % 16 == 0 ? testBlock : null);
        }

    }
}
