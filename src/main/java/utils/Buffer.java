package utils;

import java.util.Arrays;
import java.util.Iterator;

import static audioLayer.AudioLayer.BLOCK_LENGTH;
import static audioLayer.AudioLayer.BLOCK_SIZE;


public class Buffer implements Iterable<Buffer.Block> {
    public static int BUFFER_DELAY = 1;
    public static int BUFFER_LENGTH = (int)Math.ceil(BUFFER_DELAY/BLOCK_LENGTH);
    //public static int BUFFER_LENGTH = 4;
    private Block[] buffer;
    private int startPointer;
    private int endPointer;

    /**
     * Creates a new buffer object.
     */
    Buffer(){
        startPointer = 0;
        endPointer = 0;
        buffer = new Block[BUFFER_LENGTH];
    }

    /**
     *
     * @param block
     */
    public void addBlock(byte[] block){
        if(buffer[endPointer] != null){
            startPointer = nextPointer(startPointer);
        }
        buffer[endPointer] = new Block(block);
        endPointer = nextPointer(endPointer);
    }
    private boolean isEmpty(){
        if(buffer[startPointer] == null){
            return true;
        }
        return false;
    }

    private int nextPointer(int currentPointer){
        if(currentPointer == BUFFER_LENGTH - 1){
            return 0;
        }
        return ++currentPointer;
    }


    @Override
    public Iterator<Block> iterator() {
        return new BufferIterator();
    }

    public boolean hasNext() {
        return !isEmpty() && nextPointer(startPointer) < endPointer;
    }

    private class BufferIterator implements Iterator<Block> {

        @Override
        public boolean hasNext() {
            return !isEmpty();
        }

        @Override
        public Block next() {
            Block next = buffer[startPointer];
            buffer[startPointer] = null;
            startPointer = nextPointer(startPointer);
            return next;
        }
    }

    protected class Block {
        private final byte[] block;
        Block(byte[] block){
            this.block = block;
        }

        public byte[] getBlock() {
            return block;
        }

        @Override
        public String toString() {
            return Arrays.toString(block);
        }
    }

    @Override
    public String toString() {
        return "Buffer{" +
                "buffer=" + Arrays.toString(buffer) +
                ", startPointer=" + startPointer +
                ", endPointer=" + endPointer +
                '}';
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        System.out.println("Empty buffer: ");
        System.out.println(buffer);
        byte [] one= {1};
        byte [] two= {2};
        byte [] three= {3};
        byte [] four= {4};
        byte [] five= {5};

        System.out.println("Test Adding one: ");
        buffer.addBlock(one);
        System.out.println(buffer);
        System.out.println();

        System.out.println("Test Adding Two: ");
        buffer.addBlock(two);
        System.out.println(buffer);
        System.out.println();

        System.out.println("Test Adding Three: ");
        buffer.addBlock(three);
        System.out.println(buffer);
        System.out.println();

        System.out.println("Test Adding Four: ");
        buffer.addBlock(four);
        System.out.println(buffer);
        System.out.println();

        System.out.println("Test Adding Five: ");
        buffer.addBlock(five);
        System.out.println(buffer);
        System.out.println();


        System.out.println("Testing for each:");
        for (Block val: buffer) {
            System.out.println(val);
        }
        System.out.println();

        System.out.println(buffer);


    }
}
