package AlgorithmTest;

import java.util.Hashtable;
import java.util.PriorityQueue;

import AlgorithmTest.Entity.Frequency;
import AlgorithmTest.Entity.HuffmanEntity;

//허프만 코드 알고리즘을 테스트하는 객체
public class HuffmanCompress {

	private Hashtable<Character, String> encodingMap;
	private Hashtable<String, Character> decodingMap;
	private Hashtable<Character, String> hexToBi;
	private Hashtable<String, Character> biToHex;
	
	
	public HuffmanCompress() {
		this.encodingMap = new Hashtable<Character, String>();
		this.decodingMap = new Hashtable<String, Character>();
		this.hexToBi = new Hashtable<Character, String>();
		this.biToHex = new Hashtable<String, Character>();
		
		hexToBi.put('0', "0000");
		hexToBi.put('1', "0001");
		hexToBi.put('2', "0010");
		hexToBi.put('3', "0011");
		hexToBi.put('4', "0100");
		hexToBi.put('5', "0101");
		hexToBi.put('6', "0110");
		hexToBi.put('7', "0111");
		hexToBi.put('8', "1000");
		hexToBi.put('9', "1001");
		hexToBi.put('A', "1010");
		hexToBi.put('B', "1011");
		hexToBi.put('C', "1100");
		hexToBi.put('D', "1101");
		hexToBi.put('E', "1110");
		hexToBi.put('F', "1111");
		
		biToHex.put("0000", '0');
		biToHex.put("0001", '1');
		biToHex.put("0010", '2');
		biToHex.put("0011", '3');
		biToHex.put("0100", '4');
		biToHex.put("0101", '5');
		biToHex.put("0110", '6');
		biToHex.put("0111", '7');
		biToHex.put("1000", '8');
		biToHex.put("1001", '9');
		biToHex.put("1010", 'A');
		biToHex.put("1011", 'B');
		biToHex.put("1100", 'C');
		biToHex.put("1101", 'D');
		biToHex.put("1110", 'E');
		biToHex.put("1111", 'F');
	}

	// 빈도검색->빈도 힙에 추가->트리생성->전위순회하며 가변이진코드 및 테이블 생성->인코딩
	public HuffmanEntity compress(String filename, String origin) {
		
		// calculating frequency
		int freq[] = calculatingFrequency(origin);

		// inserting frequency to minheap
		PriorityQueue<Frequency> minHeap =  makeHeap();
		insertFrequency(freq, minHeap);

		// make a tree
		makeTree(minHeap);

		// preorder traversal and make a HashMap
		Frequency root = minHeap.remove();
		preorderTraversal(root, "");

		// encoding
		String encode = encoding(origin);
		
		StringBuilder tmp = new StringBuilder();
		System.out.println("hex to encoded bit : " + encode.length());
		StringBuilder encodingToHex = new StringBuilder();
		for (int i = 0; i < encode.length(); i++) {
			tmp.append(encode.charAt(i));
			if(tmp.length() == 4) {
				encodingToHex.append(biToHex.get(tmp.toString()));
				tmp.delete(0, 4);
			}
		}
		
		System.out.println("encoded bit to hex : " + encodingToHex.length());
		return new HuffmanEntity(encodingMap, decodingMap, hexToBi, biToHex, filename, encodingToHex.toString(), tmp.toString());
	}


	public static int[] calculatingFrequency(String origin) {
		int freq[] = new int[16];

		for (int i = 0; i < origin.length(); i++) {
			if ('0' <= origin.charAt(i) && origin.charAt(i) <= '9') {
				freq[origin.charAt(i) - '0']++;
			} else {
				freq[origin.charAt(i) - 'A' + 10]++;
			}
		}
		return freq;
	}
	
	public static PriorityQueue<Frequency> makeHeap() {
		return new PriorityQueue<Frequency>((a, b) -> {
			if (a.getFrequency() < b.getFrequency())
				return -1;
			else if (a.getFrequency() == b.getFrequency())
				return 0;
			else
				return 1;
		});
	}
	
	public static void insertFrequency(int[] freq, PriorityQueue<Frequency> heap) {
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0) {
				if (0 <= i && i <= 9)
					heap.add(new Frequency((char) (i + '0'), freq[i]));
				else
					heap.add(new Frequency((char) (i - 10 + 'A'), freq[i]));
			}
		}
	}
	
	public static void makeTree(PriorityQueue<Frequency> minHeap) {
		while (minHeap.size() > 1) {
			Frequency left = minHeap.remove();
			Frequency right = minHeap.remove();
			Frequency parent = new Frequency(left, right);
			minHeap.add(parent);
		}
	}
	
	public void preorderTraversal(Frequency freq, String code) {
		if (freq.getCh() != null) {
			encodingMap.put(freq.getCh(), code.toString());
			decodingMap.put(code.toString(), freq.getCh());
		}

		if (freq.getLeft() != null) {
			preorderTraversal(freq.getLeft(), code + "0");
		}
		if (freq.getRight() != null) {
			preorderTraversal(freq.getRight(), code + "1");
		}
	}
	
	public String encoding(String str) {
		StringBuilder encode = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			encode.append(this.encodingMap.get(str.charAt(i)));
		}

		return encode.toString();
	}

}
