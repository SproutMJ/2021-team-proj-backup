package datastructure;

//허프만 코딩을 위한 트리를 구축하기 위해 사용되는 노드 클래스이다
public class Frequency {
	//빈도수 계산 대상이 되는 변수 0~255 사이의 숫자이다
	private Integer oneByte;
	//빈도수를 저장하기 위한 변수
	private int frequency;
	//트리의 왼쪽 노드를 의미한다
	private Frequency left;
	//트리의 오른쪽 노드를 의미한다
	private Frequency right;

	public Frequency(int oneByte, int frequency) {
		this.oneByte = oneByte;
		this.frequency = frequency;
		this.left = null;
		this.right = null;
	}

	@Override
	public String toString() {
		return "Frequency [oneByte=" + oneByte + ", frequency=" + frequency + ", left=" + left + ", right=" + right + "]";
	}

	public Frequency(Frequency left, Frequency right) {
		this.oneByte = null;
		this.frequency = left.getFrequency() + right.getFrequency();
		this.left = left;
		this.right = right;
	}

	public Integer getOneByte() {
		return oneByte;
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
