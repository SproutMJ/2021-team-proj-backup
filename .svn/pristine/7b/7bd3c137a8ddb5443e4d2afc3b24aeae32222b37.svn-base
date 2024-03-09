package filestructure;

import java.io.Serializable;
import java.util.Hashtable;

public class MjFileHeader implements Serializable{
	private String fileName; // 저장할 파일 이름 
	private String originName; // 원래 파일 이름 
	private int fileSize; // 원래 파일 사이즈
	private int compressedFileSize; // 압축된 파일 사이즈
	private String lastBinary; // 8바이트 단위로 자른 후 남는 바이너리;
	private Hashtable<String, Integer> decodingMap;
	
	public MjFileHeader(String fileName, String originName, int fileSize, int compressedFileSize, String lastBinary, Hashtable<String, Integer> decodingMap) {
		this.fileName = fileName;
		this.originName = originName;
		this.fileSize = fileSize;
		this.compressedFileSize = compressedFileSize;
		this.lastBinary = lastBinary;
		this.decodingMap = decodingMap;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getOriginName() {
		return originName;
	}

	public int getFileSize() {
		return fileSize;
	}
	
	public int getCompressedFileSize() {
		return compressedFileSize;
	}

	public String getLastBinary() {
		return lastBinary;
	}
	
	public Hashtable<String, Integer> getDecodingMap(){
		return decodingMap;
	}
}
