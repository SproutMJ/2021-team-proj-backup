package service;

import java.util.*;

public class Huffman {
    static class HuffmanNode implements Comparable<HuffmanNode> {
        int symbol;
        long frequency;
        HuffmanNode left;
        HuffmanNode right;

        public HuffmanNode(int symbol, long frequency) {
            this.symbol = symbol;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }

        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.symbol = -1;
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return Long.compare(this.frequency, other.frequency);
        }
    }

    class HuffmanCodes {
        Map<Integer, Long> literalCode;
        Map<Integer, Long> offsetCode;

        public HuffmanCodes(Map<Integer, Long> literalCode, Map<Integer, Long> offsetCode) {
            this.literalCode = literalCode;
            this.offsetCode = offsetCode;
        }

        public Map<Integer, Long> getLiteralCode() {
            return literalCode;
        }

        public Map<Integer, Long> getOffsetCode() {
            return offsetCode;
        }
    }

    public HuffmanCodes buildHuffmanCodes(List<LZ77.Triple> compressed) {
        List<Map<Integer, Long>> frequencies = makeFrequencies(compressed);
        return new HuffmanCodes(buildTree(frequencies.get(0)), buildTree(frequencies.get(1)));
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

    private Map<Integer, Long> buildTree(Map<Integer, Long> frequencyTable){
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Integer, Long> entry : frequencyTable.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode(left, right);
            pq.add(parent);
        }

        HuffmanNode root = pq.poll();
        Map<Integer, Long> huffmanCodes = new HashMap<>();

        generateCodes(root, 0L, huffmanCodes);
        return huffmanCodes;
    }

    private void generateCodes(HuffmanNode node, long code, Map<Integer, Long> codeMap) {
        if (node.left == null && node.right == null) {
            codeMap.put(node.symbol, code);
            return;
        }
        if (node.left != null) {
            generateCodes(node.left, BitUtil.addBit(code, 0), codeMap);
        }

        if (node.right != null) {
            generateCodes(node.right, BitUtil.addBit(code, 1), codeMap);
        }
    }
}
