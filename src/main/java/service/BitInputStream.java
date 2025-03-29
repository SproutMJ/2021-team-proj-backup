package service;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream implements AutoCloseable {
    private InputStream in;
    private int buffer;
    private int bitsInBuffer;

    public BitInputStream(InputStream in) {
        this.in = in;
        this.buffer = 0;
        this.bitsInBuffer = 0;
    }

    public int readBit() throws IOException {
        if (bitsInBuffer == 0) {
            buffer = in.read();
            if (buffer == -1) {
                throw new IOException("End of stream reached");
            }
            bitsInBuffer = 8;
        }

        int bit = (buffer >> (bitsInBuffer - 1)) & 1;
        bitsInBuffer--;
        return bit;
    }

    public int readBits(int numBits) throws IOException {
        int result = 0;
        for (int i = 0; i < numBits; i++) {
            result = (result << 1) | readBit();
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
