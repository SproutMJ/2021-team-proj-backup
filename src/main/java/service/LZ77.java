package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LZ77 {

    private static final int WINDOW_SIZE = 32768;
    private static final int LOOKAHEAD_SIZE = 258;

    static class Triple {
        int offset;
        int length;
        byte nextByte;

        public Triple(int offset, int length, byte nextByte) {
            this.offset = offset;
            this.nextByte = nextByte;
            this.length = length;
        }

        @Override
        public String toString() {
            return "(" + offset + ", " + length + ", " + nextByte + ")";
        }
    }

    public List<Triple> generateCodes(byte[] data) throws IOException {
        List<Triple> compressed = new ArrayList<>();
        int i = 0;
        while (i < data.length) {
            int matchDistance = 0;
            int matchLength = 0;
            int startIndex = Math.max(0, i - WINDOW_SIZE);

            for (int j = startIndex; j < i; j++) {
                int length = 0;
                while (i + length < data.length && j + length < i &&
                        length < LOOKAHEAD_SIZE && data[j + length] == data[i + length]) {
                    length++;
                }
                if (length > matchLength) {
                    matchLength = length;
                    matchDistance = i - j;
                    if (matchLength == LOOKAHEAD_SIZE) {
                        break;
                    }
                }
            }

            if (matchLength < 3) {
                compressed.add(new Triple(0, 0, data[i]));
                i++;
            } else {
                byte nextByte = (i + matchLength < data.length) ? data[i + matchLength] : 0;
                compressed.add(new Triple(matchDistance, matchLength, nextByte));
                i += matchLength + 1;
            }
        }
        return compressed;
    }
}
