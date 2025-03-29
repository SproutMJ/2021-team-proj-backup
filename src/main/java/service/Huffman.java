package service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {
    class HuffmanNode implements Comparable<HuffmanNode> {
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
            return Long.compare(this.symbol, other.symbol);
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

        Map<Integer, Integer> literalLengths = buildTreeLengthWithLimit(frequencies.get(0), 15);
        Map<Integer, Long> literalCodes = generateCanonicalCodes(literalLengths);

        Map<Integer, Integer> offsetLengths = buildTreeLengthWithLimit(frequencies.get(1), 15);
        Map<Integer, Long> offsetCodes = generateCanonicalCodes(offsetLengths);

        return new HuffmanCodes(literalCodes, offsetCodes, literalLengths, offsetLengths);
    }

    public Map.Entry<Map<Integer, Long>, Map<Integer, Integer>> buildHuffmanCodes(int[] arr, int limit) {
        Map<Integer, Long> frequencies = makeFrequencies(arr);
        Map<Integer, Integer> codeLength = buildTreeLengthWithLimit(frequencies, limit);
        Map<Integer, Long> code = generateCanonicalCodes(codeLength);

        return new AbstractMap.SimpleEntry<>(code, codeLength);
    }

    public Map<Integer, Long> buildHuffmanCodesWithLengths(int[] codeLengths) {
        Map<Integer, Integer> codeLength = new HashMap<>();
        for (int i = 0; i < codeLengths.length; i++) {
            if (codeLengths[i] > 0) {
                codeLength.put(i, codeLengths[i]);
            }
        }

        return generateCanonicalCodes(codeLength);
    }

    public Map<Integer, Long> buildHuffmanCodesWithLengths(Map<Integer, Integer> codeLengths) {
        for (Map.Entry<Integer, Integer> entry : codeLengths.entrySet()) {
            if (entry.getValue() == 0) {
                codeLengths.remove(entry.getKey());
            }
        }

        return generateCanonicalCodes(codeLengths);
    }

    private Map<Integer, Long> makeFrequencies(int[] arr) {
        Map<Integer, Long> frequency = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            frequency.put(i, (long) arr[i]);
        }

        return frequency;
    }

    private List<Map<Integer, Long>> makeFrequencies(List<LZ77.Triple> compressed) {
        Map<Integer, Long> literalLengthTable = new HashMap<>();
        Map<Integer, Long> offsetTable = new HashMap<>();

        for (LZ77.Triple triple : compressed) {
            int[] code = LengthTables.LENGTH_EQUAL_CODE_BASE_EXTRABIT[triple.length];
            literalLengthTable.put(code[0], literalLengthTable.getOrDefault(code[0], 0L) + 1);
            literalLengthTable.put((int) triple.nextByte, literalLengthTable.getOrDefault((int) triple.nextByte, 0L) + 1);

            if (triple.offset > 0) {
                int[] offsetCode = DistanceTables.search(triple.offset);
                offsetTable.put(offsetCode[1], offsetTable.getOrDefault(offsetCode[1], 0L) + 1);
            }
        }

        literalLengthTable.put(256, 1L);
        return List.of(literalLengthTable, offsetTable);
    }

    private Map<Integer, Integer> buildTreeLengthWithLimit(Map<Integer, Long> frequencies, int limit) {
        Map<Integer, Integer> codeLengths = new HashMap<>();
        List<HuffmanNode> nodes = calculateInitialCodeLengths(frequencies, codeLengths);
        codeLengths = limitCodeLengths(nodes, limit);
        return codeLengths;
    }

    private List<HuffmanNode> calculateInitialCodeLengths(Map<Integer, Long> frequencyTable,
                                                          Map<Integer, Integer> codeLengths) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Integer, Long> entry : frequencyTable.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        if (pq.size() == 1) {
            HuffmanNode node = pq.poll();
            codeLengths.put(node.symbol, 1);
            return List.of(node);
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
        List<HuffmanNode> nodes = new ArrayList<>();
        calculateNodeDepths(root, 0, codeLengths, nodes);
        return nodes;
    }

    private void calculateNodeDepths(HuffmanNode node, int depth, Map<Integer, Integer> codeLengths, List<HuffmanNode> nodes) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            codeLengths.put(node.symbol, depth);
            node.depth = depth;
            nodes.add(node);
            return;
        }

        calculateNodeDepths(node.left, depth + 1, codeLengths, nodes);
        calculateNodeDepths(node.right, depth + 1, codeLengths, nodes);
    }

    private Map<Integer, Integer> limitCodeLengths(List<HuffmanNode> nodes, int maxLength) {
        boolean needsAdjustment = false;
        int oldMaxLength = 0;
        for (HuffmanNode huffmanNode : nodes) {
            oldMaxLength = Math.max(oldMaxLength, huffmanNode.depth);
            if (huffmanNode.depth > maxLength) {
                needsAdjustment = true;
            }
        }

        Map<Integer, Integer> codeLengths = new HashMap<>();
        if (!needsAdjustment) {
            for (HuffmanNode huffmanNode : nodes) {
                codeLengths.put(huffmanNode.symbol, huffmanNode.depth);
            }
            return codeLengths;
        }

        int n = nodes.size();

        // 1. 히스토그램 생성 (인덱스 1부터 사용)
        int[] histNumBits = new int[oldMaxLength + 1];
        for (HuffmanNode huffmanNode : nodes) {
            if (huffmanNode.depth > 0) {
                histNumBits[huffmanNode.depth]++;
            }
        }

        // 2. 히스토그램 조정
        int finalMax = limitedMinizInPlace(maxLength, oldMaxLength, histNumBits);

        // 3. 조정된 히스토그램을 바탕으로 심볼에 새 길이 할당
        // 원래 코드 길이 기준으로 심볼 인덱스를 오름차순 정렬(길이가 짧은 심볼부터 할당)
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        nodes.sort(Comparator.comparingInt(a -> a.depth));

        int index = 0;
        for (int len = 1; len <= finalMax; len++) {
            int count = histNumBits[len];
            for (int i = 0; i < count && index < nodes.size(); i++) {
                codeLengths.put(nodes.get(index).symbol, len);
                index++;
            }
        }
        while (index < nodes.size()) {
            codeLengths.put(nodes.get(index).symbol, 0);
            index++;
        }

        return codeLengths;
    }

    public static int limitedMinizInPlace(int newMaxLength, int oldMaxLength, int[] histNumBits) {
        if (newMaxLength <= 1)
            return 0;
        if (newMaxLength > oldMaxLength)
            return 0;
        if (newMaxLength == oldMaxLength)
            return newMaxLength;
        // newMaxLength보다 긴 모든 코드를 newMaxLength 길이로 이동
        for (int i = newMaxLength + 1; i <= oldMaxLength; i++) {
            histNumBits[newMaxLength] += histNumBits[i];
            histNumBits[i] = 0;
        }
        // Kraft 합 계산 (모든 항을 2^newMaxLength 배)
        long total = 0;
        for (int i = newMaxLength; i > 0; i--) {
            total += ((long) histNumBits[i]) << (newMaxLength - i);
        }
        long one = 1L << newMaxLength;
        // Kraft 합이 1 이하가 될 때까지 코드 길이를 조정
        while (total > one) {
            histNumBits[newMaxLength]--;
            for (int i = newMaxLength - 1; i > 0; i--) {
                if (histNumBits[i] > 0) {
                    histNumBits[i]--;
                    histNumBits[i + 1] += 2;
                    break;
                }
            }
            total--;
        }
        return newMaxLength;
    }

    private Map<Integer, Long> generateCanonicalCodes(Map<Integer, Integer> codeLengths) {
        Map<Integer, Long> codeMap = new HashMap<>();
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(codeLengths.entrySet());
        entries.removeIf(entry -> entry.getValue() <= 0);
        entries.sort(Comparator.comparingInt((Map.Entry<Integer, Integer> a) -> a.getValue()).thenComparingInt(Map.Entry::getKey));

        long code = 0;
        int prevLen = 0;

        Map<Long, Integer> prefix = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : entries) {
            int symbol = entry.getKey();
            int len = entry.getValue();

            code <<= (len - prevLen);

            code = processOverflow(prefix, code, len);

            codeMap.put(symbol, BitUtil.init(code, len));
            prefix.put(code, len);
            if (code == ((1L << (len)) - 1)) {
                code = (1L << (len - 1));
            }
            code++;
            prevLen = len;
        }

        return codeMap;
    }

    private long processOverflow(Map<Long, Integer> prefix, long code, int len) {
        while (checkPrefix(prefix, code, len)) {
            if (code == ((1L << (len)) - 1)) {
                code = (1L << (len - 1));
            }
            code++;
        }
        return code;
    }

    private boolean checkPrefix(Map<Long, Integer> prefixMap, long code, int currentLen) {
        for (Map.Entry<Long, Integer> entry : prefixMap.entrySet()) {
            long prefixCode = entry.getKey();
            int prefixLen = entry.getValue();

            if (prefixLen <= currentLen) {
                int shift = currentLen - prefixLen;
                long extracted = (code >>> shift);
                if (extracted == prefixCode) return true;
            } else {
                int shift = prefixLen - currentLen;
                long extracted = (prefixCode >>> shift);
                if (extracted == code) return true;
            }
        }
        return false;
    }
}
