package AlgorithmTest.Entity;

//문자정보와 빈도정보를 가지고 있으며, 노드로도 사용되는 객체 
public class Frequency {
	private Character ch;
	private int frequency;
	private Frequency left;
	private Frequency right;

	public Frequency(char ch, int frequency) {
		this.ch = ch;
		this.frequency = frequency;
		this.left = null;
		this.right = null;
	}

	@Override
	public String toString() {
		return "Frequency [ch=" + ch + ", frequency=" + frequency + ", left=" + left + ", right=" + right + "]";
	}

	public Frequency(Frequency left, Frequency right) {
		this.ch = null;
		this.frequency = left.getFrequency() + right.getFrequency();
		this.left = left;
		this.right = right;
	}

	public Character getCh() {
		return ch;
	}

	public int getFrequency() {
		return frequency;
	}

	public Frequency getLeft() {
		return left;
	}

	public Frequency getRight() {
		return right;
	}
}
