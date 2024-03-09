package AlgorithmTest.Entity;

import java.io.Serializable;
import java.util.Hashtable;

//이진 쓰기할 때 사용하기 위한 객체
public class HuffmanEntity implements Serializable {
	private Hashtable<Character, String> encodingMap;
	private Hashtable<String, Character> decodingMap;
	private Hashtable<Character, String> hexToBi;
	private Hashtable<String, Character> biToHex;
	private String fileName;
	private String encodedCode;
	private String lastBinary;
	
	public HuffmanEntity(Hashtable<Character, String> encodingMap, Hashtable<String, Character> decodingMap,
			Hashtable<Character, String> hexToBi, Hashtable<String, Character> biToHex, String fileName,
			String encodedCode, String lastBinary) {
		this.encodingMap = encodingMap;
		this.decodingMap = decodingMap;
		this.hexToBi = hexToBi;
		this.biToHex = biToHex;
		this.fileName = fileName;
		this.encodedCode = encodedCode;
		this.lastBinary = lastBinary;
	}

	@Override
	public String toString() {
		return "HuffmanCompress [fileName=" + fileName + ", encodedCode=" + encodedCode + ", lastBinary="
				+ lastBinary + "]";
	}

	public Hashtable<Character, String> getEncodingMap() {
		return encodingMap;
	}

	public Hashtable<String, Character> getDecodingMap() {
		return decodingMap;
	}

	public Hashtable<Character, String> getHexToBi() {
		return hexToBi;
	}

	public Hashtable<String, Character> getBiToHex() {
		return biToHex;
	}

	public String getFileName() {
		return fileName;
	}

	public String getEncodedCode() {
		return encodedCode;
	}

	public String getLastBinary() {
		return lastBinary;
	}
		
	
	
}
