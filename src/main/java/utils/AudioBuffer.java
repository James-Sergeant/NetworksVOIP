package utils;

import java.util.Vector;

import static audioLayer.AudioLayer.BLOCK_LENGTH;
import static audioLayer.AudioLayer.BLOCK_SIZE;

public class AudioBuffer {

    private final int MAX_PACKET_NUM = 255;
    private final int HEAD_ROOM = 8;

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
        endPacketNumber = BUFFER_LENGTH-1;
        currentLength = 0;

        for(int i = 0; i < BUFFER_LENGTH; i++){
            BUFFER.add(null);
        }
    }

    public void insertBlock(int packetNumber, byte[] block){
        /*
            Check if packet number within buffer range
            If True ->  insert into correct slot
            if false:
                Check if to add extra newpacket
                if true -> increase size of buffer and insert at end
                if false:
                    discard packet
         */

        Logger.log("Packet Num: "+packetNumber);
        if(isWithinRange(packetNumber)){
            // INSERT INTO BUFFER
            int bufferIndex = calculateBufferIndex(packetNumber);
            BUFFER.set(bufferIndex, block);
            Logger.log("#1: "+bufferIndex);
            Logger.log(this);
        }else if(isWithinExtraHeadRoom(packetNumber)){
            for(int i = 0; i < HEAD_ROOM; i++){
                BUFFER.add(null);
                endPacketNumber = nextPointer(endPacketNumber);
            }
            // INSERT INTO BUFFER
            int bufferIndex = calculateBufferIndex(packetNumber);
            BUFFER.set(bufferIndex, block);
            Logger.log("#2");
            Logger.log(this);
        }
    }

    private boolean isWithinExtraHeadRoom(int packetNumber){
        if(packetNumber >= startPacketNumber){
            if(packetNumber <= endPacketNumber + HEAD_ROOM){
                return true;
            }else if(packetNumber <= MAX_PACKET_NUM){
                return true;
            }
        }else if(packetNumber <= endPacketNumber + HEAD_ROOM){
            return true;
        }
        return false;
    }

    private boolean isWithinRange(int packetNumber){
        Logger.log("s ="+ startPacketNumber + ", e = "+endPacketNumber);
        if(packetNumber >= startPacketNumber){
            if(packetNumber <= endPacketNumber){
                return true;
            }else if(startPacketNumber > endPacketNumber && packetNumber <= MAX_PACKET_NUM){
                return true;
            }
        }else if(packetNumber <= endPacketNumber){
            return true;
        }
        return false;
    }

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
    }

    public byte[] popBlock(){
        byte[] block = BUFFER.get(0);
        startPacketNumber = nextPointer(startPacketNumber);
        endPacketNumber = nextPointer(endPacketNumber);

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
        return currentLength;
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i< BUFFER.size(); i++){
            if(BUFFER.get(i) == null){
                s += "-, ";
            }else{
                s += BUFFER.get(i)+", ";
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
