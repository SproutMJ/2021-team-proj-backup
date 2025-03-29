package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeaderEncoder {
    public static final int[] CODE_LENGTH_CODE_ORDER = {
            16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15
    };

    public HeaderInfo encodeHeader(boolean isFinalBlock, int btype, int[] literalLengths, int[] distanceLengths) {
        HeaderInfo headerInfo = new HeaderInfo();

        // 1. 블록 헤더 설정 (3비트)
        // BFINAL (1비트): 마지막 블록 여부
        headerInfo.setBfinal(isFinalBlock ? 1 : 0);

        // BTYPE (2비트): 압축 방식
        headerInfo.setBtype(btype);

        // 2. 코드 길이 배열 생성 및 RLE 인코딩
        int[] combinedLengths = new int[literalLengths.length + distanceLengths.length];
        System.arraycopy(literalLengths, 0, combinedLengths, 0, literalLengths.length);
        System.arraycopy(distanceLengths, 0, combinedLengths, literalLengths.length, distanceLengths.length);

        List<Integer> rleEncoded = applyRLE(combinedLengths);
        headerInfo.setRleEncodedLengths(rleEncoded);

        // 3. 코드 길이 알파벳에 대한 코드 길이 생성
        Huffman huffman = new Huffman();
        int[] rle = new int[19];
        for (int i = 0; i < rleEncoded.size(); i++) {
            int code = rleEncoded.get(i);
            if(0<=code && code <=15){
                rle[code]++;
            }else if(16<=code && code <=18){
                rle[code]++;
                i++;
            }else{
                throw new RuntimeException("rle code out of range");
            }
        }
        Map.Entry<Map<Integer, Long>, Map<Integer, Integer>> mapMapEntry = huffman.buildHuffmanCodes(rle, 7);
        int[] codeLengths = new int[mapMapEntry.getValue().size()];
        for (int i = 0; i < mapMapEntry.getValue().size(); i++) {
            int symbol = CODE_LENGTH_CODE_ORDER[i];
            codeLengths[i] = mapMapEntry.getValue().get(symbol);
        }
        headerInfo.setCodeLengthCodeLengths(codeLengths);

        // 5. HLIT, HDIST, HCLEN 계산
        // HLIT: 사용된 리터럴/길이 코드 수 - 257
        int hlit = calculateHLIT(literalLengths);
        headerInfo.setHlit(hlit);

        // HDIST: 사용된 거리 코드 수 - 1
        int hdist = calculateHDIST(distanceLengths);
        headerInfo.setHdist(hdist);

        // HCLEN: 사용된 코드 길이 알파벳 코드 수 - 4
        int hclen = calculateHCLEN(codeLengths);
        headerInfo.setHclen(hclen);

        // 6. 코드 길이 알파벳에 대한 허프만 코드 생성
        headerInfo.setCodeLengthCodes(mapMapEntry.getKey());

        return headerInfo;
    }

    private int calculateHLIT(int[] literalLengths) {
        int lastNonZero = 0;
        for (int i = literalLengths.length - 1; i >= 0; i--) {
            if (literalLengths[i] != 0) {
                lastNonZero = i;
                break;
            }
        }
        return Math.max(0, lastNonZero - 257 + 1);
    }

    private int calculateHDIST(int[] distanceLengths) {
        int lastNonZero = 0;
        for (int i = distanceLengths.length - 1; i >= 0; i--) {
            if (distanceLengths[i] != 0) {
                lastNonZero = i;
                break;
            }
        }
        return Math.max(0, lastNonZero - 1 + 1);
    }

    private List<Integer> applyRLE(int[] codeLengths) {
        List<Integer> rle = new ArrayList<>();
        int i = 0;

        while (i < codeLengths.length) {
            int currentLen = codeLengths[i];
            int runLength = 1;

            // 같은 길이 개수 세기
            i++;
            while (i < codeLengths.length && codeLengths[i] == currentLen) {
                runLength++;
                i++;
            }

            if (currentLen == 0) {
                // 0 반복 처리
                while (runLength > 0) {
                    if (runLength >= 11) {
                        // 18: 0 반복 (11-138회)
                        int repeatCount = Math.min(runLength, 138);
                        rle.add(18);
                        rle.add(repeatCount - 11);
                        runLength -= repeatCount;
                    } else if (runLength >= 3) {
                        // 17: 0 반복 (3-10회)
                        int repeatCount = Math.min(runLength, 10);
                        rle.add(17);
                        rle.add(repeatCount - 3);
                        runLength -= repeatCount;
                    } else {
                        // 직접 0 추가
                        rle.add(0);
                        runLength--;
                    }
                }
            } else {
                // 첫 번째 길이 직접 추가
                rle.add(currentLen);
                runLength--;

                // 반복 처리
                while (runLength > 0) {
                    if (runLength >= 3) {
                        // 16: 이전 길이 반복 (3-6회)
                        int repeatCount = Math.min(runLength, 6);
                        rle.add(16);
                        rle.add(repeatCount - 3);
                        runLength -= repeatCount;
                    } else {
                        // 직접 길이 추가
                        rle.add(currentLen);
                        runLength--;
                    }
                }
            }
        }

        return rle;
    }

    private int calculateHCLEN(int[] codeLengthCodeLengths) {
        int maxOrderIndex = -1;
        for (int i = 0; i < CODE_LENGTH_CODE_ORDER.length; i++) {
            int symbol = CODE_LENGTH_CODE_ORDER[i];
            if (symbol < codeLengthCodeLengths.length && codeLengthCodeLengths[symbol] > 0) {
                maxOrderIndex = i;
            }
        }
        return Math.max(0, maxOrderIndex - 4 + 1);
    }

    public static class HeaderInfo {
        private int bfinal;
        private int btype;
        private int hlit;
        private int hdist;
        private int hclen;
        private int[] codeLengthCodeLengths;
        private List<Integer> rleEncodedLengths;
        private Map<Integer, Long> codeLengthCodes;

        public int getBfinal() { return bfinal; }
        public void setBfinal(int bfinal) { this.bfinal = bfinal; }

        public int getBtype() { return btype; }
        public void setBtype(int btype) { this.btype = btype; }

        public int getHlit() { return hlit; }
        public void setHlit(int hlit) { this.hlit = hlit; }

        public int getHdist() { return hdist; }
        public void setHdist(int hdist) { this.hdist = hdist; }

        public int getHclen() { return hclen; }
        public void setHclen(int hclen) { this.hclen = hclen; }

        public int[] getCodeLengthCodeLengths() { return codeLengthCodeLengths; }
        public void setCodeLengthCodeLengths(int[] codeLengthCodeLengths) {
            this.codeLengthCodeLengths = codeLengthCodeLengths;
        }

        public List<Integer> getRleEncodedLengths() { return rleEncodedLengths; }
        public void setRleEncodedLengths(List<Integer> rleEncodedLengths) {
            this.rleEncodedLengths = rleEncodedLengths;
        }

        public Map<Integer, Long> getCodeLengthCodes() { return codeLengthCodes; }
        public void setCodeLengthCodes(Map<Integer, Long> codeLengthCodes) {
            this.codeLengthCodes = codeLengthCodes;
        }
    }
}
