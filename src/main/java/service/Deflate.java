package service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deflate {
    private static final int BUFFER_SIZE = 64 * 1024;

    LZ77 lz77 = new LZ77();
    Huffman huffman = new Huffman();

    public void deflate(String inputFile, String outputFile) {
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((bytesRead = fis.read(buffer)) != -1) {
                bytesReadTotal += bytesRead;
                long bfinal = BitUtil.addBit(0L, 0);
                if (bytesReadTotal == fileSize) {
                    bfinal = BitUtil.addBit(0L, 1);
                }

                //1. 압축 방식 결정

                //2. 헤더 결정(3비트)
                //비압축 블록 (BTYPE=00)

                //고정 허프만 코딩 (BTYPE=01)
                fixCompress();

                //가변 허프만 코딩 (BTYPE=10)
                long btype = 0L;
                btype = BitUtil.addBit(btype, 1);
                btype = BitUtil.addBit(btype, 0);
                dynamicCompress(buffer, outputFile, bfinal, btype);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void dynamicCompress(byte[] data, String outputFile, long bfinal, long btype) throws IOException {
        //1단계 LZ77
        List<LZ77.Triple> compressed = lz77.generateCodes(data);

        //2단계 허프만 트리 생성
        Huffman.HuffmanCodes huffmanCodes = huffman.buildHuffmanCodes(compressed);
        Map<Integer, BitSet> literalCode = huffmanCodes.getLiteralCode();
        Map<Integer, BitSet> offsetCode = huffmanCodes.getOffsetCode();

        //3단계 출력
        try (BitOutputStream bitOut = new BitOutputStream(new FileOutputStream(outputFile, true))) {
            for (LZ77.Triple triple : compressed) {
                if (triple.length == 0) {
                    bitOut.writeBit(literalCode.get((int) triple.nextByte));
                } else {
                    int[] code = LengthTables.LENGTH_CODE[triple.length];
                    bitOut.writeBit(literalCode.get(code[0]));
                    int extraBitCount = code[2];
                    if (extraBitCount > 0) {
                        bitOut.writeBit(triple.length - code[1], extraBitCount);
                    }

                    int[] offset = OffsetTables.binarySearch(triple.offset);
                    bitOut.writeBit(offsetCode.get(offset[1]));
                    extraBitCount = offset[2];
                    if (extraBitCount > 0) {
                        bitOut.writeBit(triple.offset - offset[0], extraBitCount);
                    }

                    bitOut.writeBit(literalCode.get((int) triple.nextByte));
                }
            }
            bitOut.flush();
        }

    }

    private void fixCompress() {

    }
}
