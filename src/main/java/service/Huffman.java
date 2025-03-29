package service;

import java.util.*;

public class Huffman {
    static class HuffmanNode implements Comparable<HuffmanNode> {
        int symbol;
        long frequency;
        HuffmanNode left;
        HuffmanNode right;
        int depth;

        public HuffmanNode(int symbol, long frequency) {
            this.symbol = symbol;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
            this.depth = 0;
        }

        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.symbol = -1;
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
            this.depth = 0;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            int freqCompare = Long.compare(this.frequency, other.frequency);
            if (freqCompare != 0) {
                return freqCompare;
            }
            return Integer.compare(this.depth, other.depth);
        }
    }

    class HuffmanCodes {
        Map<Integer, Long> literalCode;
        Map<Integer, Long> offsetCode;
        Map<Integer, Integer> literalCodeLengths;
        Map<Integer, Integer> offsetCodeLengths;

        public HuffmanCodes(Map<Integer, Long> literalCode, Map<Integer, Long> offsetCode,
                            Map<Integer, Integer> literalCodeLengths, Map<Integer, Integer> offsetCodeLengths) {
            this.literalCode = literalCode;
            this.offsetCode = offsetCode;
            this.literalCodeLengths = literalCodeLengths;
            this.offsetCodeLengths = offsetCodeLengths;
        }

        public Map<Integer, Long> getLiteralCode() {
            return literalCode;
        }

        public Map<Integer, Long> getOffsetCode() {
            return offsetCode;
        }

        public Map<Integer, Integer> getLiteralCodeLengths() {
            return literalCodeLengths;
        }

        public Map<Integer, Integer> getOffsetCodeLengths() {
            return offsetCodeLengths;
        }
    }

    public HuffmanCodes buildHuffmanCodes(List<LZ77.Triple> compressed) {
        List<Map<Integer, Long>> frequencies = makeFrequencies(compressed);

        Map<Integer, Long> literalCodes = new HashMap<>();
        Map<Integer, Integer> literalLengths = new HashMap<>();
        buildTreeWithLengthLimit(frequencies.get(0), literalCodes, literalLengths);

        Map<Integer, Long> offsetCodes = new HashMap<>();
        Map<Integer, Integer> offsetLengths = new HashMap<>();
        buildTreeWithLengthLimit(frequencies.get(1), offsetCodes, offsetLengths);

        return new HuffmanCodes(literalCodes, offsetCodes, literalLengths, offsetLengths);
    }

    private List<Map<Integer, Long>> makeFrequencies(List<LZ77.Triple> compressed) {
        Map<Integer, Long> literalLengthTable = new HashMap<>();
        Map<Integer, Long> offsetTable = new HashMap<>();

        for (LZ77.Triple triple : compressed) {
            int[] code = LengthTables.LENGTH_CODE[triple.length];
            literalLengthTable.put(code[0], literalLengthTable.getOrDefault(code[0], 0L) + 1);
            literalLengthTable.put((int) triple.nextByte, literalLengthTable.getOrDefault((int) triple.nextByte, 0L) + 1);

            int[] offsetCode = OffsetTables.binarySearch(triple.offset);
            offsetTable.put(offsetCode[1], offsetTable.getOrDefault(offsetCode[1], 0L) + 1);
        }

        literalLengthTable.put(256, 1L);
        return List.of(literalLengthTable, offsetTable);
    }

    private void buildTreeWithLengthLimit(Map<Integer, Long> frequencyTable,
                                          Map<Integer, Long> codeMap,
                                          Map<Integer, Integer> codeLengths) {
        calculateInitialCodeLengths(frequencyTable, codeLengths);

        limitCodeLengths(codeLengths, 15);

        generateCanonicalCodes(codeLengths, codeMap);
    }

    private void calculateInitialCodeLengths(Map<Integer, Long> frequencyTable,
                                             Map<Integer, Integer> codeLengths) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Integer, Long> entry : frequencyTable.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        if (pq.size() == 1) {
            HuffmanNode node = pq.poll();
            codeLengths.put(node.symbol, 1);
            return;
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode(left, right);

            if (left.left != null || left.right != null) {
                left.depth++;
            }
            if (right.left != null || right.right != null) {
                right.depth++;
            }

            pq.add(parent);
        }

        HuffmanNode root = pq.poll();
        calculateNodeDepths(root, 0, codeLengths);
    }

    private void calculateNodeDepths(HuffmanNode node, int depth, Map<Integer, Integer> codeLengths) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            codeLengths.put(node.symbol, depth);
            return;
        }

        calculateNodeDepths(node.left, depth + 1, codeLengths);
        calculateNodeDepths(node.right, depth + 1, codeLengths);
    }

    private void limitCodeLengths(Map<Integer, Integer> codeLengths, int maxLength) {
        boolean needsAdjustment = false;
        for (int length : codeLengths.values()) {
            if (length > maxLength) {
                needsAdjustment = true;
                break;
            }
        }

        if (!needsAdjustment) return;

        int[] lengthCounts = new int[maxLength + 1];
        for (int length : codeLengths.values()) {
            if (length > maxLength) {
                lengthCounts[maxLength]++;
            } else {
                lengthCounts[length]++;
            }
        }

        int totalNodes = 0;
        for (int i = maxLength; i > 0; i--) {
            totalNodes += lengthCounts[i] << (maxLength - i);
            if (totalNodes > (1 << maxLength)) {
                adjustCodeLengths(codeLengths, maxLength);
                break;
            }
        }

        for (Map.Entry<Integer, Integer> entry : codeLengths.entrySet()) {
            if (entry.getValue() > maxLength) {
                codeLengths.put(entry.getKey(), maxLength);
            }
        }
    }

    private void adjustCodeLengths(Map<Integer, Integer> codeLengths, int maxLength) {
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(codeLengths.entrySet());

        entries.sort((a, b) -> {
            int lengthCompare = Integer.compare(b.getValue(), a.getValue());
            if (lengthCompare != 0) return lengthCompare;
            return Integer.compare(a.getKey(), b.getKey());
        });

        for (Map.Entry<Integer, Integer> entry : entries) {
            if (entry.getValue() > maxLength) {
                codeLengths.put(entry.getKey(), maxLength);
            }
        }

        boolean repeat;
        do {
            repeat = false;

            int[] lengthCounts = new int[maxLength + 1];
            for (int length : codeLengths.values()) {
                lengthCounts[length]++;
            }

            int nodesSum = 0;
            for (int i = maxLength; i > 0; i--) {
                nodesSum += lengthCounts[i] << (maxLength - i);
            }

            if (nodesSum > (1 << maxLength)) {
                for (Map.Entry<Integer, Integer> entry : entries) {
                    if (entry.getValue() == maxLength) {
                        codeLengths.put(entry.getKey(), maxLength - 1);
                        repeat = true;
                        break;
                    }
                }
            }
        } while (repeat);
    }

    private void generateCanonicalCodes(Map<Integer, Integer> codeLengths, Map<Integer, Long> codeMap) {
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(codeLengths.entrySet());
        entries.sort(Comparator.comparingInt((Map.Entry<Integer, Integer> a) -> a.getValue()).thenComparingInt(Map.Entry::getKey));

        long code = 0;
        int prevLen = 0;

        for (Map.Entry<Integer, Integer> entry : entries) {
            int symbol = entry.getKey();
            int len = entry.getValue();

            for(int i = 0; i < (len - prevLen); i++){
                code = BitUtil.addBit(code, 0);
            }

            codeMap.put(symbol, code);
            code++;
            prevLen = len;
        }
    }
}
