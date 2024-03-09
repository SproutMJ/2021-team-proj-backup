package service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JProgressBar;

import filestructure.MjFile;
import filestructure.MjFileBody;
import filestructure.MjFileHeader;
import utility.HuffmanCompress;
import utility.HuffmanDecompress;
import utility.HuffmanEntity;

public class CompressionServiceImpl implements CompressionService{

	//파일을 압축한다. 저장될 파일 경로, 원본 파일 경로, 압축 진행상황 처리를 위한 progressbar를 인자로 받는다
	@Override
	public void compressFile(File saveFile, File originFile, JProgressBar progressBar) {
			
		//byte 크기만큼 buffer 선언
		byte[] buffer = new byte[(int)originFile.length()];
		
		FileInputStream fis;
		BufferedInputStream bis;
		
		//파일을 읽어들여 buffer안에 저장한다.
		try {
			fis = new FileInputStream(originFile.getAbsoluteFile());
			bis = new BufferedInputStream(fis);
			bis.read(buffer);
			bis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		//압축을 위한 서비스를 받기위해 huffmancompress 객체 초기화
		HuffmanCompress hc = new HuffmanCompress(progressBar);
		//파일을 압축시켜 entity안에 저장한다.
		HuffmanEntity entity = hc.compress(buffer);

		//인코딩된 코드
		String encodedCode = entity.getEncodedCode();
		
		//인코딩된 코드는 2진수 상태이므로 byte배열 형태로 만들어주기 위하여 길이/8을 하여 byte배열을 선언한다
		byte[] compressedByte = new byte[encodedCode.length()/8];
		int cnt = 50;
		int percent1 = compressedByte.length / 50;
		
		//이진수를 파싱하여 바이트 형태로 만들어 배열에 넣는다. O(n)이 큰 구간이므로 진행상황또한 갱신해준다.
		for(int i = 0; i < compressedByte.length; i++) {
			compressedByte[i] = (byte) Integer.parseInt(encodedCode.substring(8*i, 8*i+8), 2);
			
			if(i % percent1 == 0) {
				cnt++;
				progressBar.setValue(cnt);
			}
		}
		
		// 저장을 위한 파일 경로
		String compressedFileName = saveFile.getAbsolutePath();
		
		//직렬화를 위해 MjFile값을 알맞게 초기화해준다.
		MjFileHeader header = new MjFileHeader(compressedFileName, originFile.getName(), (int) originFile.length(), (int) compressedByte.length, entity.getLastBinary(), entity.getDecodingMap());
		MjFileBody body = new MjFileBody(compressedByte);
		MjFile mjFile = new MjFile(header, body);
		
		FileOutputStream fos;
		ObjectOutputStream oos;
		
		//직렬화 입출력 스트림을 통해 mjFile형태로 압축된 데이터를 내보낸다.
		try {
			fos = new FileOutputStream(compressedFileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(mjFile);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	//파일을 압축 해제 한다. 압축된 파일 경로, 압축 해제할 파일 경로, 진행상황 갱신을 위한 progressbar를 인자로 받는다.
	@Override
	public void deCompressFile(File compressedFile, String decompressedFilePath, JProgressBar progressBar) {
		
		FileInputStream fis;
		ObjectInputStream ois;
		MjFile mjFile = null;
		
		//파일 입출력을 통해 mj파일을 읽어들여 저장한다
		try {
			fis = new FileInputStream(compressedFile.getAbsoluteFile());
			ois = new ObjectInputStream(fis);
			mjFile = (MjFile)ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//인코딩된 데이터를 저장한다
		byte[] encodedBinary = mjFile.getBody().getBinary();
		//압축 해제 전 파일 크기를 저장한다.
		int originFileSize = mjFile.getHeader().getFileSize();
		StringBuilder builder = new StringBuilder();
		
		//byte을 이진수 binary 문자열 형태로 만든다.
		int cnt = 0;
		int percent1 = encodedBinary.length / 50;
		for(int i = 0; i < encodedBinary.length; i++) {
			builder.append(byteToBinaryString(encodedBinary[i]));
			if(i % percent1 == 0) {
				cnt++;
				progressBar.setValue(cnt);
			}
		}
		builder.append(mjFile.getHeader().getLastBinary());
		
		//bianry 문자열을 저장
		String stringBinary = builder.toString();
		//압축 해제 서비스를 받기 위해 HuffmanDecompress 객체를 초기화 한다.
		HuffmanDecompress hfdc = new HuffmanDecompress();
		//압축 해제 서비스를 받아 원본 바이너리를 저장한다.
		byte[] decodedBinary = hfdc.decompress(mjFile.getHeader().getDecodingMap(), stringBinary, originFileSize, progressBar);
		
		//압축 해제 전 파일 명을 저장
		String originName = mjFile.getHeader().getOriginName();
		
		FileOutputStream fos;
		BufferedOutputStream bos;
		
		String originFilePath = decompressedFilePath += "\\";
		originFilePath += originName;
		
		//선택한 파일 경로로 압축 해제된 파일을 파일 입출력으로 내보낸다.
		try {
			fos = new FileOutputStream(originFilePath);
			bos = new BufferedOutputStream(fos);
			bos.write(decodedBinary);
			bos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//byte값을 받아 이진수 string 형태로 만들어 주는 함수 비트 연산을 사용하여 구현하였다.
	public String byteToBinaryString(byte n) {
	    StringBuilder sb = new StringBuilder("00000000");
	    for (int bit = 0; bit < 8; bit++) {
	        if (((n >> bit) & 1) > 0) {
	            sb.setCharAt(7 - bit, '1');
	        }
	    }
	    return sb.toString();
	}
}
