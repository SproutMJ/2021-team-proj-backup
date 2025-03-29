package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Deflate {

    private static final int BUFFER_SIZE = 64 * 1024;

    LZ77 lz77 = new LZ77();
    Huffman huffman = new Huffman();

    public void compress(String inputFile, String outputFile) {
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            File file = new File(inputFile);
            long fileSize = file.length(); // 파일 전체 크기
            long bytesReadTotal = 0;       // 지금까지 읽은 바이트 수
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

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
        Map<Integer, Long> literalCode = huffmanCodes.getLiteralCode();
        Map<Integer, Long> offsetCode = huffmanCodes.getOffsetCode();

        // 동적 변환 필요(마지막거까지)
        int[] literalLengths = new int[286];
        for (int symbol = 0; symbol < 286; symbol++) {
            int add = 0;
            if (symbol <= 255) {
                add = -128;
            }
            Long code = literalCode.get(symbol + add);
            if (code != null) {
                literalLengths[symbol] = Math.toIntExact(BitUtil.extractBits(code).get(1));
            } else {
                literalLengths[symbol] = 0;
            }
        }

        int[] distanceLengths = new int[30];
        for (int symbol = 0; symbol < 30; symbol++) {
            Long code = offsetCode.get(symbol);
            if (code != null) {
                distanceLengths[symbol] = Math.toIntExact(BitUtil.extractBits(code).get(1));
            } else {
                distanceLengths[symbol] = 0;
            }
        }

        HeaderEncoder headerEncoder = new HeaderEncoder();
        HeaderEncoder.HeaderInfo headerInfo = headerEncoder.encodeHeader(BitUtil.extractBits(bfinal).get(0) == 1, 2, literalLengths, distanceLengths);

        //3단계 출력
        try (BitOutputStream bitOut = new BitOutputStream(new FileOutputStream(outputFile, true))) {
            //헤더 출력
            bitOut.writeBit(bfinal, 1);
            bitOut.writeBit(btype, 2);
            bitOut.writeBit(literalCode.size() - 257, 5);
            bitOut.writeBit(distanceLengths.length - 1, 5);
            bitOut.writeBit(headerInfo.getHclen(), 4);

            // 코드 길이 알파벳 코드 길이 출력
            for (int i = 0; i < headerInfo.getHclen() + 4; i++) {
                bitOut.writeBit(headerInfo.getCodeLengthCodeLengths()[i], 3);
            }

            // RLE 인코딩된 코드 길이 출력
            List<Integer> rleEncoded = headerInfo.getRleEncodedLengths();
            Map<Integer, Long> codeLengthCodes = headerInfo.getCodeLengthCodes();

            for (int i = 0; i < rleEncoded.size(); i++) {
                int symbol = rleEncoded.get(i);
                Long code = codeLengthCodes.get(symbol);

                // 코드 작성
                bitOut.writeBit(BitUtil.extractBits(code).get(0), Math.toIntExact(BitUtil.extractBits(code).get(1)));

                // 특수 코드의 추가 비트 작성
                if (symbol == 16) {
                    // 이전 코드 반복 (3-6회): 2비트 추가
                    int repeatCount = rleEncoded.get(++i);
                    bitOut.writeBit(repeatCount, 2);
                } else if (symbol == 17) {
                    // 0 반복 (3-10회): 3비트 추가
                    int repeatCount = rleEncoded.get(++i);
                    bitOut.writeBit(repeatCount, 3);
                } else if (symbol == 18) {
                    // 0 반복 (11-138회): 7비트 추가
                    int repeatCount = rleEncoded.get(++i);
                    bitOut.writeBit(repeatCount, 7);
                }
            }

            for (LZ77.Triple triple : compressed) {
                if (triple.length == 0) {
                    bitOut.writeBit(literalCode.get((int) triple.nextByte), Math.toIntExact(BitUtil.extractBits(literalCode.get((int) triple.nextByte)).get(1)));
                } else {
                    int[] codee = LengthTables.LENGTH_EQUAL_CODE_BASE_EXTRABIT[triple.length];
                    bitOut.writeBit(literalCode.get(codee[0]), Math.toIntExact(BitUtil.extractBits(literalCode.get(codee[0])).get(1)));
                    int extraBitCount = codee[2];
                    if (extraBitCount > 0) {
                        bitOut.writeBit(triple.length - codee[1], extraBitCount);
                    }

                    int[] offset = DistanceTables.search(triple.offset);
                    bitOut.writeBit(offsetCode.get(offset[1]), Math.toIntExact(BitUtil.extractBits(offsetCode.get(offset[1])).get(1)));
                    extraBitCount = offset[2];
                    if (extraBitCount > 0) {
                        bitOut.writeBit(triple.offset - offset[0], extraBitCount);
                    }

                    bitOut.writeBit(literalCode.get((int) triple.nextByte), Math.toIntExact(BitUtil.extractBits(literalCode.get((int) triple.nextByte)).get(1)));
                }
            }
            bitOut.writeBit(literalCode.get(256), Math.toIntExact(BitUtil.extractBits(literalCode.get(256)).get(1)));
            bitOut.flush();
        }
    }

    public void decompress(String inputFile, String outputFile) throws IOException {
        try (BitInputStream bis = new BitInputStream(new FileInputStream(inputFile));
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            boolean lastBlock = false;

            while (!lastBlock) {
                // 헤더 정보 디코딩
                HeaderDecoder headerDecoder = new HeaderDecoder();
                HeaderDecoder.HeaderInfo headerInfo = headerDecoder.decodeHeader(bis);

                lastBlock = headerInfo.getBfinal() == 1;
                Map<Long, Integer> literalTree = headerInfo.getLiteralTree();

                Map<Long, Integer> distanceTree = headerInfo.getDistanceTree();

                // LZ77 블록 복구 및 원본 파일 복원
                List<LZ77.Triple> triples = decompressBlock(bis, literalTree, distanceTree);
            }
        }
    }

    private List<LZ77.Triple> decompressBlock(BitInputStream bis,
                                              Map<Long, Integer> literalTree,
                                              Map<Long, Integer> distanceTree) throws IOException {

        List<LZ77.Triple> triples = new ArrayList<>();
        int a = 0;
        while (true) {
            // 리터럴/길이 코드 읽기
            int symbol = decodeSymbol(literalTree, 15, bis);
            if (symbol == 256) {
                break;
            } else if (symbol < 256) {
                triples.add(new LZ77.Triple(0, 0, (byte) symbol));
            } else {
                // 길이-거리 쌍 처리
                int length = decodeLength(symbol, bis, a);

                // 거리 코드 읽기
                int distSymbol = decodeSymbol(distanceTree, 15, bis);
                int distance = decodeDistance(distSymbol, bis);

                symbol = decodeSymbol(literalTree, 15, bis);

                // 다음 바이트 읽기
                byte nextByte = (byte) symbol;
                triples.add(new LZ77.Triple(distance, length, nextByte));
            }
        }

        return triples;
    }

    private int decodeSymbol(Map<Long, Integer> tree, int limit, BitInputStream bis) throws IOException {
        Integer symbol = null;
        long code = 0;
        while (symbol == null) {
            int bit = bis.readBit();
            if (bit == -1) {
                throw new IOException("예상치 못한 스트림 종료");
            }

            code = BitUtil.addBit(code, bit);

            // 리터럴/길이 트리에서 코드 검색
            symbol = tree.get(code);

            if (BitUtil.extractBits(code).get(1) > limit) {
                throw new IOException("유효하지 않은 리터럴/길이 코드");
            }
        }

        return symbol;
    }

    private int decodeLength(int symbol, BitInputStream bis, int a) throws IOException {
        if (symbol < 257 || symbol > 285) {
            throw new IOException("유효하지 않은 길이 심볼: " + symbol);
        }

        int[] lengthEntry = LengthTables.CODE_EQUAL_BASE_EXTRABIT_CODE[symbol - 257];
        int baseLength = lengthEntry[0];
        int extraBits = lengthEntry[1];

        if (extraBits > 0) {
            int extraValue = bis.readBits(extraBits);
            return baseLength + extraValue;
        } else {
            return baseLength;
        }
    }

    private int decodeDistance(int symbol, BitInputStream bis) throws IOException {
        if (symbol < 0 || symbol > 29) {
            throw new IOException("유효하지 않은 거리 심볼: " + symbol);
        }
        int[] distanceEntry = DistanceTables.CODE_EQUAL_BASE_CODE_EXTRABIT[symbol];
        int baseDistance = distanceEntry[0];
        int extraBits = distanceEntry[2];

        if (extraBits > 0) {
            int extraValue = bis.readBits(extraBits);
            return baseDistance + extraValue;
        }
        return baseDistance;
    }
}
