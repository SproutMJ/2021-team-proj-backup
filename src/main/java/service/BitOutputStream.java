package service;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.BitSet;

class BitOutputStream implements Closeable {
    private OutputStream out;
    private int currentByte;
    private int numBitsFilled;

    public BitOutputStream(OutputStream out) {
        this.out = out;
        this.currentByte = 0;
        this.numBitsFilled = 0;
    }

    public void writeBit(BitSet bits) throws IOException {
        for (int i = 0; i < bits.length(); i++) {
            writeBit(bits.get(i) ? 1 : 0);
        }
    }

    public void writeBit(int bit) throws IOException {
        currentByte = (currentByte << 1) | bit;
        numBitsFilled++;
        if (numBitsFilled == 8) {
            out.write(currentByte);
            numBitsFilled = 0;
            currentByte = 0;
        }
    }

    public void flush() throws IOException {
        if (numBitsFilled > 0) {
            currentByte <<= (8 - numBitsFilled);
            out.write(currentByte);
        }
        out.flush();
    }

    public void close() throws IOException {
        flush();
        out.close();
    }

    public void writeBit(int value, int length) throws IOException {
        BitSet bitSet = new BitSet(length);
        for (int i = 0; i < length; i++) {
            if ((value & (1 << (length - i - 1))) != 0) {
                bitSet.set(i);
            }
        }
        writeBit(bitSet);
    }
}
