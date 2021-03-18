package com;

public class Config {

    public static PRESET preset;

    public enum PACKET_LOSS_SOLUTION {REPETITION, BLANK_FILL_IN, INTERPOLATION, NOISE}

    public enum PRESET{
        SOCKET1(1,0.0,false,0,1, Config.PACKET_LOSS_SOLUTION.BLANK_FILL_IN, false, 3, false),
        SOCKET2(2,1.5,true,32,1, Config.PACKET_LOSS_SOLUTION.INTERPOLATION, true, 5, false),
        SOCKET3(3,2.0,true,32,1, Config.PACKET_LOSS_SOLUTION.INTERPOLATION, true, 4, false),
        SOCKET1XOR(1,0.0,false,0,1, Config.PACKET_LOSS_SOLUTION.BLANK_FILL_IN, false, 3, false),
        CUSTOM1(2,1.0,true,32,1, Config.PACKET_LOSS_SOLUTION.REPETITION, false, 3, false);

        // DATAGRAM SOCKET
        private final int DATAGRAM_SOCKET; // 1, 2 or 3

        // AUDIO RECEIVING BUFFER

        private final double BUFFER_DELAY; // Seconds
        private final boolean REORDER; // Re-Order packets when receiving
        private final int TIMEOUT; // 1000ms for d1, 32ms for d2

        // PACKET EFFICIENCY

        private final int BLOCKS_PER_PACKET;

        // AUDIO SOLUTIONS

        private final Config.PACKET_LOSS_SOLUTION PACKET_LOSS_SOLUTION;
        private final boolean INTERLEAVER; // Enable / Disable Block Interleaving
        private final int INTERLEAVER_SIZE; // Size of block interleaver (3*3, 4*4, etc.)

        // SECURITY

        private final boolean ENCRYPTION;

        PRESET(int DATAGRAM_SOCKET, double BUFFER_DELAY, boolean REORDER, int TIMEOUT, int BLOCKS_PER_PACKET, Config.PACKET_LOSS_SOLUTION PACKET_LOSS_SOLUTION,
               boolean INTERLEAVER, int INTERLEAVER_SIZE, boolean ENCRYPTION) {
            this.DATAGRAM_SOCKET = DATAGRAM_SOCKET;
            this.BUFFER_DELAY = BUFFER_DELAY;
            this.REORDER = REORDER;
            this.TIMEOUT = TIMEOUT;
            this.BLOCKS_PER_PACKET = BLOCKS_PER_PACKET;
            this.PACKET_LOSS_SOLUTION = PACKET_LOSS_SOLUTION;
            this.INTERLEAVER = INTERLEAVER;
            this.INTERLEAVER_SIZE = INTERLEAVER_SIZE;
            this.ENCRYPTION = ENCRYPTION;
        }

        public int getDATAGRAM_SOCKET() {
            return DATAGRAM_SOCKET;
        }

        public double getBUFFER_DELAY() {
            return BUFFER_DELAY;
        }

        public boolean isREORDER() {
            return REORDER;
        }

        public int getTIMEOUT() {
            return TIMEOUT;
        }

        public int getBLOCKS_PER_PACKET() {
            return BLOCKS_PER_PACKET;
        }

        public Config.PACKET_LOSS_SOLUTION getPACKET_LOSS_SOLUTION() {
            return PACKET_LOSS_SOLUTION;
        }

        public boolean isINTERLEAVER() {
            return INTERLEAVER;
        }

        public int getINTERLEAVER_SIZE() {
            return INTERLEAVER_SIZE;
        }

        public boolean isENCRYPTION() {
            return ENCRYPTION;
        }
    }
}
