package voipLayer.solutions;

import org.checkerframework.checker.units.qual.A;
import utils.AudioBuffer;

import java.net.DatagramPacket;
import java.util.Arrays;

public class BlockInterleaver {

    private final int d; // dimension
    private int i; // Counter i
    private int j; // Counter j
    private final DatagramPacket[] buffer;
    private int bufferSwitch;
    private int popPointer;

    public BlockInterleaver(int dimension){
        d = dimension;
        buffer = new DatagramPacket[d*d*2];
        i = 0;
        j = 0;
        bufferSwitch = 0;
        popPointer = d*d;
    }

    public void addPacket(DatagramPacket block){
        int index = calculateInterleavedIndex() + (d*d*bufferSwitch);
        //System.out.println("index "+index);
        //System.out.println("bufferSwitch "+bufferSwitch);
        buffer[index] = block;
        incrementCounters();
    }

    public DatagramPacket popPacket(){
        return buffer[popPointer++];
    }

    private int calculateInterleavedIndex(){
        return (j * d) + (d - 1 - i);
    }

    private void incrementCounters(){
        if(j+1 == d) {
            j = 0;
            if(i+1 == d){
                i = 0;
                popPointer = d*d*bufferSwitch;
                bufferSwitch = (bufferSwitch+1) % 2; // switch between 1 and 0
            }else{
                i++;
            }
        }else{
            j++;
        }
    }

    @Override
    public String toString() {
        return "BlockInterleaver{" +
                Arrays.toString(buffer) +
                '}';
    }
}
