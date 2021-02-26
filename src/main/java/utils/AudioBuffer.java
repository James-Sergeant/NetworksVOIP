package utils;

import java.util.Vector;

import static audioLayer.AudioLayer.BLOCK_LENGTH;
import static audioLayer.AudioLayer.BLOCK_SIZE;

public class AudioBuffer {
    public final int BUFFER_DELAY;
    public final int BUFFER_LENGTH;
    private Vector<byte[]> BUFFER;

    AudioBuffer(int BUFFER_DELAY){
        this.BUFFER_DELAY = BUFFER_DELAY;
        this.BUFFER_LENGTH = (int)Math.ceil(BUFFER_DELAY/BLOCK_LENGTH);
        BUFFER = new Vector<>();
    }

    public void addBlock(byte[] block, int pos){
        BUFFER.insertElementAt(block,pos);
    }

    public Vector<byte[]> flushBuffer(){
      final Vector<byte[]> r = BUFFER;
      BUFFER = new Vector<>();
      return r;
    }



}
