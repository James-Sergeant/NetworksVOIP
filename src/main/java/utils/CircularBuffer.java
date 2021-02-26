package utils;

import java.util.Arrays;
import java.util.Iterator;

import static audioLayer.AudioLayer.BLOCK_LENGTH;


public class CircularBuffer implements Iterable<CircularBuffer.Block> {
    public static int BUFFER_DELAY = 1;
    public static int BUFFER_LENGTH = (int)Math.ceil(BUFFER_DELAY/BLOCK_LENGTH);
    //public static int BUFFER_LENGTH = 4;
    private Block[] buffer;
    private int startPointer;
    private int endPointer;

    /**
     * Creates a new buffer object.
     */
    public CircularBuffer(){
        startPointer = 0;
        endPointer = 0;
        buffer = new Block[BUFFER_LENGTH];
    }

    /**
     * Allows a block to be added to the buffer.
     * @param block Object: The block to be added to the buffer.
     */
    public void addBlock(byte[] block){
        if(buffer[endPointer] != null){
            startPointer = nextPointer(startPointer);
        }
        buffer[endPointer] = new Block(block);
        endPointer = nextPointer(endPointer);
    }

    /**
     * Checks to see if the buffer is empty.
     * @return Boolean: True if the buffer is empty.
     */
    private boolean isEmpty(){
        if(buffer[startPointer] == null){
            return true;
        }
        return false;
    }

    /**
     * Finds the next pointer, and implements the circular nature of the buffer.
     * @param currentPointer Integer: The current value of the pointer.
     * @return Integer: The next value for the pointer.
     */
    private int nextPointer(int currentPointer){
        if(currentPointer == BUFFER_LENGTH - 1){
            return 0;
        }
        return ++currentPointer;
    }

    public byte[] popBlock(){
        Block next = buffer[startPointer];
        buffer[startPointer] = null;
        startPointer = nextPointer(startPointer);
        return next.block;
    }

    /**
     * Returns the size of the buffer.
     * @return Integer: Size of the buffer
     */
    public int size(){
        if(endPointer > startPointer){
            return endPointer - startPointer;
        }else if(isEmpty()){
            return 0;
        }else {
            return (BUFFER_LENGTH - startPointer) + endPointer;
        }
    }

    /**
     * The iterator method, that provided the defined BufferIterator class.
     * @return BufferIterator
     */
    @Override
    public Iterator<Block> iterator() {
        return new BufferIterator();
    }

    /**
     * Lets you know if the buffer has any values left in it.
     * @return Boolean: True if there is a value in the buffer.
     */
    public boolean hasNext() {
        return !isEmpty();
    }

    /**
     * The BufferIterator class implementing the Iterator functionality from the interface.
     */
    private class BufferIterator implements Iterator<Block> {
        /**
         * Used to tell if there is a next block in the buffer.
         * @return Boolean: True if there is a next block.
         */
        @Override
        public boolean hasNext() {
            return !isEmpty();
        }

        /**
         * Returns the next block and removes it from the buffer.
         * @return Object\<Block>: The next block in the buffer.
         */
        @Override
        public Block next() {
            Block next = buffer[startPointer];
            buffer[startPointer] = null;
            startPointer = nextPointer(startPointer);
            return next;
        }
    }

    /**
     * A block object used to wrap the byte[] array allowing them to be passed by reference.
     */
     class Block {
        private final byte[] block;


        /**
         * Block iterface used to create the block.
         * @param block: byte[]: The data to be buffered.
         */
        Block(byte[] block){
            this.block = block;
        }

        /**
         * Returns the data in the block.
         * @return byte[]: the blocks data.
         */
        public byte[] getBlock() {
            return block;
        }

        /**
         * A utility toString method for debugging.
         * @return String: the byte[] values.
         */
        @Override
        public String toString() {
            return Arrays.toString(block);
        }
    }

    /**
     * A utility toString method for debugging.
     * @return String: Contains the pointer values and the values within the blocks.
     */
    @Override
    public String toString() {
        return "Buffer{" +
                "buffer=" + Arrays.toString(buffer) +
                ", startPointer=" + startPointer +
                ", endPointer=" + endPointer +
                '}';
    }

    public static void main(String[] args) {
        CircularBuffer buffer = new CircularBuffer();
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
