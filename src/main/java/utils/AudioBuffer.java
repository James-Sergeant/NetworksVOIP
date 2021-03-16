package utils;

import com.Config;

import java.util.Vector;

import static audioLayer.AudioLayer.BLOCK_LENGTH;
import static audioLayer.AudioLayer.BLOCK_SIZE;

public class AudioBuffer {

    private final int MAX_PACKET_NUM;
    private final int HEAD_ROOM = 8;
    private final int BUFFER_LENGTH;
    private final Vector<byte[]> BUFFER;

    private int startPacketNumber;
    private int endPacketNumber;
    private int currentLength;
    private boolean refill;

    public AudioBuffer(double buffer_delay, int maxPacketNumber){
        MAX_PACKET_NUM = maxPacketNumber;
        BUFFER_LENGTH = (int)Math.ceil(buffer_delay/BLOCK_LENGTH) + HEAD_ROOM;
        BUFFER = new Vector<>();

        startPacketNumber = 0;
        endPacketNumber = BUFFER_LENGTH-1;
        currentLength = 0;
        refill = true;
    }

    public void insertBlock(int packetNumber, byte[] block){

        if(isEmpty()) firstPacketSetup(packetNumber);
        Logger.log("Packet Num: "+packetNumber);

        if(Config.preset.isREORDER()) {
            int bufferIndex = calculateBufferIndex(packetNumber);
            if (bufferIndex != -1) { // If packet number within range
                Logger.log("BufferIndex = " + bufferIndex + " currentLength = " + currentLength);
                // If packet number is greater than vector length add nulls
                if (bufferIndex > BUFFER.size()) {
                    increaseBufferSizeTo(bufferIndex);
                    BUFFER.add(block);
                    currentLength++;
                } else if (isEmpty() || bufferIndex == currentLength) {
                    BUFFER.add(block);
                    currentLength++;
                } else {
                    BUFFER.setElementAt(block, bufferIndex);
                }

                Logger.log(this);
            }
        }else{
            BUFFER.add(block);
            currentLength++;
        }
        if(currentLength >= BUFFER_LENGTH - HEAD_ROOM) refill = false;
    }


    private void increaseBufferSizeTo(int bufferIndex){
        for(int i = BUFFER.size(); i < bufferIndex; i++){
            BUFFER.add(null);
            currentLength++;
        }
    }


    private void firstPacketSetup(int packetNumber){
        startPacketNumber = packetNumber;
        endPacketNumber = packetNumber;
        for(int i = 0; i < BUFFER_LENGTH; i++){
            endPacketNumber = nextPointer(endPacketNumber);
        }
    }


    private int calculateBufferIndex(int packetNumber){
        Logger.log("s ="+ startPacketNumber + ", e = "+endPacketNumber);

        if(packetNumber >= startPacketNumber){
            if(packetNumber <= endPacketNumber){
                return packetNumber - startPacketNumber;
            }else if(startPacketNumber > endPacketNumber && packetNumber <= MAX_PACKET_NUM){
                return packetNumber - startPacketNumber;
            }
        }else if(endPacketNumber < startPacketNumber && packetNumber <= endPacketNumber){
            return (MAX_PACKET_NUM - startPacketNumber) + packetNumber;
        }

        return -1;
    }


    public byte[] popBlock(){
        byte[] block = BUFFER.get(0);

        startPacketNumber = nextPointer(startPacketNumber);
        endPacketNumber = nextPointer(endPacketNumber);

        BUFFER.remove(0);
        currentLength--;
        if(isEmpty()) refill = true;

        Logger.log("LENGTH: "+currentLength);
        Logger.log("POP: "+ (startPacketNumber-1));
        Logger.log(this);

        return block;
    }

    private int nextPointer(int pointer){
        if(pointer == MAX_PACKET_NUM) return 0;
        return pointer + 1;
    }

    public boolean isEmpty(){
        return currentLength == 0;
    }

    public boolean isRefilling(){
        return refill;
    }

    public byte[] getBlock(int index){
        return BUFFER.get(index);
    }

    public int getLength(){
        return BUFFER_LENGTH;
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i< BUFFER.size(); i++){
            if(BUFFER.get(i) == null){
                s += "-, ";
            }else{
                s += "1, ";
            }
        }
        return "AudioBuffer{" +s+ '}';
    }

    public static void main(String[] args) {
        AudioBuffer buffer = new AudioBuffer(0.5, 255);
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

        buffer.insertBlock(23, testBlock);
        */
        // TESTING CIRCULAR BLOCK INSERTION
        /*
        for(int i = 0; i < 500; i++){
            buffer.insertBlock(i%256, testBlock);
        }
        */
    }
}
