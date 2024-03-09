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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JProgressBar;

import datastructure.Tuple;
import filestructure.LZFile;
import filestructure.MjFile;
import filestructure.MjFileBody;
import filestructure.MjFileHeader;
import utility.BinaryKMP;
import utility.HuffmanCompress;
import utility.HuffmanDecompress;
import utility.HuffmanEntity;

public class CompressionServiceImpl implements CompressionService{

	//LZ77의 offset을 구하기 위한 변수 바람직한 패턴은 아닐 수 있으나 편의상 선언함
	private int offset = -1; 
	
	//searchBuffer와 lookAheadBuffer의 가장 긴 일치 문자열을 이진탐색 + kmp로 찾는다
	//예상 시간 복잡도는 byte 크기가 n일때 O(nlogn) 이다 
	private int BSearch(List<Byte> searchBuffer, List<Byte> lookAheadBuffer) {
		
		int len = lookAheadBuffer.size();
		int first = 0;
		int last = lookAheadBuffer.size() - 1;
		int mid = 0;
		boolean flag = false;
		
		BinaryKMP bkmp = new BinaryKMP();
		int prevSize = -1;
		
		while(first <= last) {
			mid = (first + last) / 2;
			List<Byte> pattern = lookAheadBuffer.subList(0, mid);
			
			if(bkmp.kmp(searchBuffer, pattern)) {
				first = mid + 1;
				flag = true;
				offset = bkmp.getOffset();
				prevSize = mid;
				
				if(pattern.size() == len) {
					return mid;
				}
				
				if(pattern.size() == 1) {
					return mid;
				}
			}else {
				if(flag) {
					return prevSize;
				}
				last = mid - 1;
			}
		}
		
		return mid; // 무조건 찾는것이 보장된다
	}
	
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
	
	@Override
	public void lz77Compress(File saveFile, File originFile, JProgressBar progressBar) {
		
		int searchBufferSize = 255;    //searchBufferSize를 정한다 
		int lookAheadBufferSize = 255; //lookAheadBufferSize 크기를 정한다
		
		// 원본 파일 크기만큼 버퍼 선언
		byte[] buffer = new byte[(int)originFile.length()];
		FileInputStream fis;
		BufferedInputStream bis;
		
		try {
			fis = new FileInputStream(originFile.getAbsoluteFile());
			bis = new BufferedInputStream(fis);
			bis.read(buffer);
			bis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//binary를 Queue 형식으로 다뤄야 편하기 때문에 Queue에 옮겨담는다
		LinkedList<Byte> binaryQueue = new LinkedList<>();
		
		for(int i = 0; i < buffer.length; i++) {
			binaryQueue.add(buffer[i]);
		}
		
		// LZ77의 SearchBuffer 역할
		LinkedList<Byte> searchBuffer = new LinkedList<>();
		// LZ77의 LookAheadBuffer 역할
		LinkedList<Byte> lookAheadBuffer = new LinkedList<>();
		// 알고리즘에 의해 생성된 Tuple 저장소
		LinkedList<Tuple> tupleList = new LinkedList<>();
		
		int cnt = 0;
		int percentage = binaryQueue.size()/ 100;
		int maxSize = binaryQueue.size();
		
		//binaryQueue, lookAheadBuffer 전부 비워져야 알고리즘이 끝이 난다
		while(binaryQueue.size() != 0 || lookAheadBuffer.size() != 0) {
			
			int progress = maxSize - binaryQueue.size();
			cnt = progress / percentage;
	
			progressBar.setValue(cnt);
			
			//searchBuffer 크기를 넘는 만큼 데이터가 들어가 있으면 앞에서부터 빼준다
			while(searchBuffer.size() > searchBufferSize) {
				searchBuffer.poll();
			}
			
			//binaryQueue가 비어있지 않아야되고 lookAheadBuffer 크기가 정한 크기보다 작으면 채워준다
			while(binaryQueue.size() != 0 && lookAheadBuffer.size() < lookAheadBufferSize) {
				lookAheadBuffer.add(binaryQueue.poll());
			}
			
			//kmp 알고리즘에 의해 구한 일치 문자열의 크기를 저장하기위한 변수
			int idx = 0;
			
			//lookAheadBuffer의 맨 앞 바이트 한개가 searchBuffer에 존재하지 않거나 lookAheadBufferSize가 1이면 0,0,byte 튜플을 저장
			if(searchBuffer.indexOf(lookAheadBuffer.get(0)) == -1 || lookAheadBuffer.size() == 1) {
				Tuple tuple = new Tuple((byte) 0, (byte) 0, lookAheadBuffer.get(0));
				tupleList.add(tuple);
			}else {
				
				//searchBuffer에 집어넣어야 하는 만큼을 나타냄 또한 offset의 크기를 나타내기도 한다.
				int pollCount = BSearch(searchBuffer, lookAheadBuffer);
				idx = pollCount;
				
				Tuple tuple = new Tuple((byte)offset, (byte)pollCount, lookAheadBuffer.get(pollCount));
				tupleList.add(tuple);
			}
			
			for(int i = 0; i <= idx; i++) {
				searchBuffer.add(lookAheadBuffer.poll());
			}
		}
		
		LZFile lzFile = new LZFile(tupleList, (int)originFile.length(), originFile.getName());
		
		ObjectOutputStream writer; 
		
		try {
			writer = new ObjectOutputStream(new FileOutputStream(saveFile.getAbsoluteFile()));
			writer.writeObject(lzFile);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		progressBar.setValue(100);
		
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
	
	@Override
	public void deCompressLZFile(File compressedFile, String decompressedfilePath, JProgressBar progressBar) {
		
		FileInputStream fis;
		ObjectInputStream ois;
		LZFile lzFile = null;
		
		//파일 입출력을 통해 mj파일을 읽어들여 저장한다
		try {
			fis = new FileInputStream(compressedFile.getAbsoluteFile());
			ois = new ObjectInputStream(fis);
			lzFile = (LZFile)ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		List<Tuple> tupleList = lzFile.getTupleList();
		List<Byte> byteList = new ArrayList<>();
		
		for(Tuple tuple : tupleList) {
			if(tuple.offset == 0 && tuple.length == 0) {
				byteList.add(tuple.binary);
			}else {
				
				int off = tuple.offset;
				if(off < 0) off += 256;
				
				int length = tuple.length;
				if(length < 0) length += 256;
				
				int cpStart = byteList.size() - off;
				
				int cpEnd = cpStart + length;
				
				for(int i = cpStart; i < cpEnd; i++) {
					byteList.add(byteList.get(i));
				}

				byteList.add(tuple.binary);
			}
		}
		
		Byte[] bytes = byteList.toArray(new Byte[byteList.size()]);
		byte[] lbytes = new byte[bytes.length];
		
		for(int i = 0; i < bytes.length; i++) {
			lbytes[i] = bytes[i];
		}
		
		FileOutputStream fos;
		BufferedOutputStream bos;
		
		String originName = lzFile.getOriginFileName();
		String originFilePath = decompressedfilePath += "//";
		originFilePath += originName;
		
		try {
			fos = new FileOutputStream(originFilePath);
			bos = new BufferedOutputStream(fos);
			bos.write(lbytes);
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
