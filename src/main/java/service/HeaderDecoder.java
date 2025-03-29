package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderDecoder {
    // 코드 길이 코드 순서 (RFC 1951 표준)
    private static final int[] CODE_LENGTH_CODE_ORDER = {
            16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15
    };

    public HeaderInfo decodeHeader(BitInputStream bitIn) throws IOException {
        HeaderInfo headerInfo = new HeaderInfo();

        // 1. 블록 헤더 읽기 (3비트)
        // BFINAL (1비트): 마지막 블록 여부
        int bfinal = bitIn.readBit();
        headerInfo.setBfinal(bfinal);

        // BTYPE (2비트): 압축 방식
        int btype = (bitIn.readBit() << 1) | bitIn.readBit();
        headerInfo.setBtype(btype);

        // 동적 허프만 코딩(BTYPE=10)인 경우에만 추가 헤더 필드 읽기
        if (btype == 2) {
            // 2. HLIT, HDIST, HCLEN 읽기
            // HLIT (5비트): 리터럴/길이 코드 수 - 257
            int hlit = bitIn.readBits(5);
            headerInfo.setHlit(hlit);

            // HDIST (5비트): 거리 코드 수 - 1
            int hdist = bitIn.readBits(5);
            headerInfo.setHdist(hdist);

            // HCLEN (4비트): 코드 길이 알파벳 코드 수 - 4
            int hclen = bitIn.readBits(4);
            headerInfo.setHclen(hclen);

            // 3. 코드 길이 알파벳 코드 길이 읽기 (각 3비트)
            int[] codeLengthCodeLengths = new int[19];
            for (int i = 0; i < hclen + 4; i++) {
                int symbol = CODE_LENGTH_CODE_ORDER[i];
                codeLengthCodeLengths[symbol] = bitIn.readBits(3);
            }
            headerInfo.setCodeLengthCodeLengths(codeLengthCodeLengths);

            // 4. 코드 길이 알파벳 허프만 트리 재구성
            Map<Long, Integer> codeLengthAlphabetTree = reconstructHuffmanTree(codeLengthCodeLengths);
            headerInfo.setCodeLengthAlphabetTree(codeLengthAlphabetTree);

            // 5. RLE 압축 해제 및 코드 길이 배열 복원
            List<Integer> decompressedCodeLengths = decompressRLE(
                    bitIn, codeLengthAlphabetTree, (hlit + 257) + (hdist + 1));
            headerInfo.setDecompressedCodeLengths(decompressedCodeLengths);

            // 6. 리터럴/길이 및 거리 코드 길이 분리
            int[] distanceLengths = new int[hdist + 1];

            // 리터럴/길이 코드 길이 복사
            Map<Integer, Integer> codeLengths = new HashMap<>();
            for (int i = 0; i < hlit + 257; i++) {
                int add = 0;
                if (i <= 255) {
                    add = -128;
                }
                codeLengths.put(i + add, decompressedCodeLengths.get(i));
            }

            // 거리 코드 길이 복사
            for (int i = 0; i < distanceLengths.length; i++) {
                distanceLengths[i] = decompressedCodeLengths.get(hlit + 257 + i);
            }

            headerInfo.setLiteralLengths(codeLengths);
            headerInfo.setDistanceLengths(distanceLengths);

            // 7. 리터럴/길이 및 거리 허프만 트리 재구성
            Map<Long, Integer> literalTree = reconstructHuffmanTree(codeLengths);
            Map<Long, Integer> distanceTree = reconstructHuffmanTree(distanceLengths);

            headerInfo.setLiteralTree(literalTree);
            headerInfo.setDistanceTree(distanceTree);
        }

        return headerInfo;
    }

    private Map<Long, Integer> reconstructHuffmanTree(int[] codeLengths) {
        Map<Long, Integer> huffmanTree = new HashMap<>();
        Huffman huffman = new Huffman();

        Map<Integer, Long> codes = huffman.buildHuffmanCodesWithLengths(codeLengths);
        for (Map.Entry<Integer, Long> entry : codes.entrySet()) {
            huffmanTree.put(entry.getValue(), entry.getKey());
        }

        return huffmanTree;
    }

    private Map<Long, Integer> reconstructHuffmanTree(Map<Integer, Integer> codeLengths) {
        Map<Long, Integer> huffmanTree = new HashMap<>();
        Huffman huffman = new Huffman();
        Map<Integer, Long> codes = huffman.buildHuffmanCodesWithLengths(codeLengths);
        for (Map.Entry<Integer, Long> entry : codes.entrySet()) {
            huffmanTree.put(entry.getValue(), entry.getKey());
        }

        return huffmanTree;
    }

    private List<Integer> decompressRLE(BitInputStream reader,
                                        Map<Long, Integer> huffmanTree,
                                        int totalCodeLengths) throws IOException {
        List<Integer> codeLengths = new ArrayList<>();
        long currentCode;

        while (codeLengths.size() < totalCodeLengths) {
            currentCode = 0L;

            // 허프만 코드 읽기
            while (true) {
                currentCode = BitUtil.addBit(currentCode, reader.readBit());
                if (huffmanTree.containsKey(currentCode)) {
                    break;
                }
            }

            int symbol = huffmanTree.get(currentCode);

            if (symbol <= 15) {
                // 직접 코드 길이 (0-15)
                codeLengths.add(symbol);
            } else if (symbol == 16) {
                // 이전 길이 반복 (3-6회)
                int repeat = reader.readBits(2) + 3;

                int prevLength = codeLengths.get(codeLengths.size() - 1);
                for (int i = 0; i < repeat; i++) {
                    codeLengths.add(prevLength);
                }
            } else if (symbol == 17) {
                // 0 반복 (3-10회)
                int repeat = reader.readBits(3) + 3;
                for (int i = 0; i < repeat; i++) {
                    codeLengths.add(0);
                }
            } else if (symbol == 18) {
                // 0 반복 (11-138회)
                int repeat = reader.readBits(7) + 11;
                for (int i = 0; i < repeat; i++) {
                    codeLengths.add(0);
                }
            }
        }
        return codeLengths;
    }

    public static class HeaderInfo {
        private int bfinal;
        private int btype;
        private int hlit;
        private int hdist;
        private int hclen;
        private int[] codeLengthCodeLengths;
        private Map<Long, Integer> codeLengthAlphabetTree;
        private List<Integer> decompressedCodeLengths;
        private Map<Integer, Integer> literalLengths;
        private int[] distanceLengths;
        private Map<Long, Integer> literalTree;
        private Map<Long, Integer> distanceTree;

        // Getters and setters
        public int getBfinal() {
            return bfinal;
        }

        public void setBfinal(int bfinal) {
            this.bfinal = bfinal;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public int getHlit() {
            return hlit;
        }

        public void setHlit(int hlit) {
            this.hlit = hlit;
        }

        public int getHdist() {
            return hdist;
        }

        public void setHdist(int hdist) {
            this.hdist = hdist;
        }

        public int getHclen() {
            return hclen;
        }

        public void setHclen(int hclen) {
            this.hclen = hclen;
        }

        public int[] getCodeLengthCodeLengths() {
            return codeLengthCodeLengths;
        }

        public void setCodeLengthCodeLengths(int[] codeLengthCodeLengths) {
            this.codeLengthCodeLengths = codeLengthCodeLengths;
        }

        public Map<Long, Integer> getCodeLengthAlphabetTree() {
            return codeLengthAlphabetTree;
        }

        public void setCodeLengthAlphabetTree(Map<Long, Integer> codeLengthAlphabetTree) {
            this.codeLengthAlphabetTree = codeLengthAlphabetTree;
        }

        public List<Integer> getDecompressedCodeLengths() {
            return decompressedCodeLengths;
        }

        public void setDecompressedCodeLengths(List<Integer> decompressedCodeLengths) {
            this.decompressedCodeLengths = decompressedCodeLengths;
        }

        public Map<Integer, Integer> getLiteralLengths() {
            return literalLengths;
        }

        public void setLiteralLengths(Map<Integer, Integer> literalLengths) {
            this.literalLengths = literalLengths;
        }

        public int[] getDistanceLengths() {
            return distanceLengths;
        }

        public void setDistanceLengths(int[] distanceLengths) {
            this.distanceLengths = distanceLengths;
        }

        public Map<Long, Integer> getLiteralTree() {
            return literalTree;
        }

        public void setLiteralTree(Map<Long, Integer> literalTree) {
            this.literalTree = literalTree;
        }

        public Map<Long, Integer> getDistanceTree() {
            return distanceTree;
        }

        public void setDistanceTree(Map<Long, Integer> distanceTree) {
            this.distanceTree = distanceTree;
        }
    }
}
