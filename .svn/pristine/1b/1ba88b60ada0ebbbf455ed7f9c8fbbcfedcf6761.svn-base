package utility;

import java.util.Hashtable;

import javax.swing.JProgressBar;

public class HuffmanDecompress {

	// 먼저 파일크기를 인자로받아 복호화될 byte 배열을 선언한다. 이후 decodingmap이랑 일치하는 byte값을 찾아 byte배열에 순서대로 삽입한다. 진행상황 갱신을 위해 progressbar값을 계산하여 갱신해준다.
	public byte[] decompress(Hashtable<String, Integer> decodingMap, String encodedBinary, int originFileSize, JProgressBar progressBar) {
		StringBuilder tmp = new StringBuilder();
		byte[] decodedByte = new byte[originFileSize];
		int k = 0;
		int cnt = 50;
		int percent = encodedBinary.length()/50;
		
		for(int i = 0; i < encodedBinary.length(); i++) {
			tmp.append(encodedBinary.charAt(i));
			if(decodingMap.get(tmp.toString()) != null) {
				decodedByte[k] = (byte) (decodingMap.get(tmp.toString()).byteValue()-128);
				k++;
				tmp.setLength(0);
			}
			
			if(i % percent == 0) {
				cnt++;
				progressBar.setValue(cnt);
			}
		}
		return decodedByte;
	}
	
}
