package voipLayer;

import utils.AudioBuffer;

public class OrderPackets {
    public static int MAX_PACKET_NUMBER = 255;




    public static AudioBuffer BUFFER_ONE = new AudioBuffer(0.5);
    public static AudioBuffer BUFFER_TWO = new AudioBuffer(0.5);

    private static int ONE_LAST_PACKET_NUM = 0;
    private static int TWO_PACKET_NUM = 0;


    public static void addAudio(int packetNumber, byte[] data){
        if(ONE_ACTIVE){
            int pos = getPos(packetNumber,ONE_LAST_PACKET);
            BUFFER_ONE.addBlock(data,pos);
            if (BUFFER_ONE.size() == BUFFER_ONE.BUFFER_LENGTH) ACTIVE = TWO_ACTIVE;
        }
    }

    private static int isInRange(int packetNum){
        if(getPos(packetNum,ONE_PACKET_NUM)<BUFFER_ONE.BUFFER_LENGTH){

        }
    }

    private static int getMaxPacketNumber(int startNum){

    }

    private static int getPos(int packetNumber, int bufferPos){
        //Case where not overflow has occurred or is the first packet.
        if(packetNumber > bufferPos || bufferPos == 0){
            return packetNumber - bufferPos;
        }

        //If packet number has overflowed
        if(packetNumber < bufferPos){
            return (packetNumber + MAX_PACKET_NUMBER) - bufferPos;
        }

        return 0;
    }

}
