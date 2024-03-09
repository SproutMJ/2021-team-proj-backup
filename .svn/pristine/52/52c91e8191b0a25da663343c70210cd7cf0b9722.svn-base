package AlgorithmTest;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LZ77Test {
	public static void main(String[] args) {
		/*
		 * 예제 1
		 * List<Byte> a = new LinkedList<>();
		 * a.add((byte) 97); a.add((byte) 111); a.add((byte) 104); a.add((byte) 108);
		 * 
		 * a.add((byte) 98); a.add((byte) 111); a.add((byte) 104); a.add((byte) 108);
		 * 
		 * a.add((byte) 99); a.add((byte) 111); a.add((byte) 104); a.add((byte) 108);
		 * 
		 * List<Tuple> tuples = lz77Encoding(a, (byte) 100, (byte) 100);
		 * for (Tuple t : tuples) System.out.println(t);
		 */

		/* 예제 2
		// i가 1억 이상에서 메모리 에러뜸
		List<Byte> a = new LinkedList<>();
		Random rand = new Random();
		int i = 0;
		for (; i < 10000000; i++) {
			a.add((byte) rand.nextInt(255));
		}

		Instant begin = Instant.now();
		List<Tuple> tuples = lz77Encoding(a, (byte) 100, (byte) 100);
		Instant end = Instant.now();
		Duration between = Duration.between(begin, end);

		// for (Tuple t : tuples) System.out.println(t);
		System.out.println(between.toSeconds() + "초 " + i + "=>" + tuples.size() + "="
				+ (double) tuples.size() / (double) i * 100.0 + "%");
		*/
		
		//반례1
		//2단계에서 || lookaheadBuffer.size() == 1를 제거하면 IndexOutOfBoundsException이 발생한다.
		//이유: lookaheadBuffer에 원소가 하나남은 상태에서 searchBuffer에 동일한 원소가 있을 때, 기존에는 (index, 1 해당원소)로 처리해버려 lookaheadBuffer에 남는게 없어짐
		 List<Byte> a = new LinkedList<>();
		 a.add((byte) 99); a.add((byte) 111); a.add((byte) 104); a.add((byte) 104);
		 
		 List<Tuple> tuples = lz77Encoding(a, (byte) 3, (byte) 3);
		 for (Tuple t : tuples) System.out.println(t);
	}

	/*
	 * 바이트 리스트를 매개변수로 받는 LZ77 2번째버전이다. (다 사용한
	 * 바이트들)....[searchBuffer][lookaheadBuffer][origin]으로 작동한다. origin에서
	 * lookaheadBuffer로 급여하는 1단계, searchBuffer에서 lookaheadBuffer의 처음 부분과 일치하는 부분이
	 * 있는지 탐색하고 tuple 리스트에 삽입하는 2단계, searchBuffer에서 넘치는 부분을 자르는 3단계로 이루어져 있다.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Tuple> lz77Encoding(List<Byte> origin, byte searchBufSize, byte lookaheadBufSize) {
		List<Tuple> tuples = new LinkedList<>();
		List<Byte> searchBuffer = new LinkedList<>();
		List<Byte> lookaheadBuffer = new LinkedList<>();

		while (true) {
			// 1단계
			int lastBufferSize = lookaheadBuffer.size();
			for (int i = 0; i < lookaheadBufSize - lastBufferSize; i++) {
				if (origin.size() == 0) {
					break;
				}
				lookaheadBuffer.add(origin.get(0));
				origin.remove(0);
			}

			if (origin.size() == 0 && lookaheadBuffer.size() == 0) {
				break;
			}

			// 2단계
			byte index = (byte) searchBuffer.indexOf(lookaheadBuffer.get(0));
			if (index == -1 || lookaheadBuffer.size() == 1) {
				tuples.add(new Tuple((byte) 0, (byte) 0, lookaheadBuffer.get(0)));
			} else {
				byte length = 1;
				for (int i = 1; i + index < searchBuffer.size() && i < lookaheadBuffer.size() - 1; i++) {
					if (searchBuffer.get(index + i) == lookaheadBuffer.get(i)) {
						length++;
					} else {
						break;
					}
				}
				byte tupleIndex = (byte) (searchBuffer.size() - index);
				for (int i = 0; i < length; i++) {
					searchBuffer.add(lookaheadBuffer.get(0));
					lookaheadBuffer.remove(0);
				}
				tuples.add(new Tuple(tupleIndex, (byte) length, lookaheadBuffer.get(0)));
			}
			searchBuffer.add(lookaheadBuffer.get(0));
			lookaheadBuffer.remove(0);

			// 3단계
			while (searchBuffer.size() > searchBufSize) {
				searchBuffer.remove(0);
			}
			// System.out.println(tuples.get(tuples.size() - 1));
		}

		return tuples;

	}

}

class Tuple<T> {
	public byte index;
	public byte length;
	public T literal;

	public Tuple(byte index, byte length, T literal) {
		this.index = index;
		this.length = length;
		this.literal = literal;
	}

	@Override
	public String toString() {
		return "Tuple [index=" + index + ", length=" + length + ", literal=" + literal + "]";
	}

}
