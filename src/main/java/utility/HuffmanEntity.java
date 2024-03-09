package utility;

import java.util.Hashtable;

// 허프만 코딩에 관련되는 값들을 단순히 전달하기 위해 선언한 클래스이다
public class HuffmanEntity {
	// 인코딩에 사용되는 맵 데이터를 저장한다
	private Hashtable<Integer, String> encodingMap;
	// 디코딩에 사용되는 맵 데이터를 저장한다
	private Hashtable<String, Integer> decodingMap;
	// 허프만 코딩으로 인코딩된 데이터를 저장한다.
	private String encodedCode;
	// 인코딩된 데이터는 가변비트이기때문에 byte단위로 떨어지지않는다 그러나 파일입출력의경우 byte단위로 처리해야하기때문에 인코딩후 남는 bit를 여기다 저장한다.
	private String lastBinary;
	
	public HuffmanEntity(Hashtable<Integer, String> encodingMap, Hashtable<String, Integer> decodingMap,
			String encodedCode, String lastBinary) {
		this.encodingMap = encodingMap;
		this.decodingMap = decodingMap;
		this.encodedCode = encodedCode;
		this.lastBinary = lastBinary;
	}
	
	public Hashtable<Integer, String> getEncodingMap() {
		return encodingMap;
	}
	public Hashtable<String, Integer> getDecodingMap() {
		return decodingMap;
	}
	public String getEncodedCode() {
		return encodedCode;
	}
	public String getLastBinary() {
		return lastBinary;
	}
}
