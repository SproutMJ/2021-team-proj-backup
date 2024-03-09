package utility;

import java.util.Hashtable;
import java.util.PriorityQueue;

import javax.swing.JProgressBar;

import datastructure.Frequency;

public class HuffmanCompress {

	// 인코딩을 위한 맵 정보 저장
	private Hashtable<Integer, String> encodingMap;
	// 디코딩을 위한 맵 정보 저장
	private Hashtable<String, Integer> decodingMap;
	// 진행상황을 progressbar에 업데이트 해주기위해 progressbar을 전달받아 처리한다.
	private JProgressBar progressbar;
	
	public HuffmanCompress(JProgressBar progressbar) {
		this.encodingMap = new Hashtable<Integer, String>();
		this.decodingMap = new Hashtable<String, Integer>();
		this.progressbar = progressbar;
	}
	
	//파일 입출력으로 획득한 바이너리를 받아 압축을 실행한다 빈도수 계산 -> minHeap을 사용한 트리 생성 -> 트리를 통해 인코딩 맵 생성 -> 인코딩 맵을 통해 -> 압축 -> 압축된 데이터 반환 순으로 진행된다
	public HuffmanEntity compress(byte[] fileBinary) {
		int freq[] = calculateFrequency(fileBinary);
		
		PriorityQueue<Frequency> pq = makeHeap();
		initFrequency(freq, pq);
		initTree(pq);
		
		Frequency root = pq.remove();
		encodingBinary(root, "");

		String encode = getEncodedCode(fileBinary);
		int restBit = encode.length() % 8;
		String lastBinary = encode.substring(encode.length()-restBit);
		
		return new HuffmanEntity(encodingMap, decodingMap, encode, lastBinary);
	}
	
	//바이너리를 받아 0~255의 빈도수를 계산하여 int 배열 형태로 반환한다
	private int[] calculateFrequency(byte[] fileBinary) {
		
		int freq[] = new int[256];
		
		for(int i = 0; i < fileBinary.length; i++) {
			freq[fileBinary[i] + 128]++;
		}
		
		return freq;
	}
	
	//최소힙을 만드는데 Frequency객체를 비교하므로 Frequency객체의 비교조건을 정의한다
	private PriorityQueue<Frequency> makeHeap(){
		return new PriorityQueue<Frequency>((a, b) -> {
			if (a.getFrequency() < b.getFrequency())
				return -1;
			else if (a.getFrequency() == b.getFrequency())
				return 0;
			else
				return 1;
		});
	}
	
	//빈도수를 최소 힙에 넣는다
	private void initFrequency(int[] freq, PriorityQueue<Frequency> pq) {
		for(int i = 0; i < freq.length; i++) {
			pq.add(new Frequency(i, freq[i]));
		}
	}
	
	//허프만 알고리즘에 의해 트리를 하나 만든다
	private void initTree(PriorityQueue<Frequency> pq) {
		while(pq.size() > 1) {
			Frequency left = pq.remove();
			Frequency right = pq.remove();
			Frequency parent = new Frequency(left, right);
			pq.add(parent);
		}
	}
	
	// 트리를 처리하여 각 문자열마다 대응되는 인코딩 테이블을 만든다
	private void encodingBinary(Frequency freq, String code) {
		if (freq.getOneByte() != null) {
			encodingMap.put(freq.getOneByte(), code.toString());
			decodingMap.put(code.toString(), freq.getOneByte());
		}
		
		if (freq.getLeft() != null) {
			encodingBinary(freq.getLeft(), code + "0");
		}
		if (freq.getRight() != null) {
			encodingBinary(freq.getRight(), code + "1");
		}
	}
	
	// 생성된 인코딩 테이블을 가지고 fileBinary와 매칭시켜 압축된 코드를 만든다
	private String getEncodedCode(byte[] fileBinary) {
		StringBuilder builder = new StringBuilder();
		int cnt = 0;
		int percent = fileBinary.length / 50;
		for(int i = 0; i < fileBinary.length; i++) {
			builder.append(this.encodingMap.get(fileBinary[i] + 128));
			
			if(i % percent == 0) {
				cnt++;
				progressbar.setValue(cnt);
			}
		}
		
		return builder.toString();
	}
}
