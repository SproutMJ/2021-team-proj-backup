package serialization;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import AlgorithmTest.HuffmanCompress;
import AlgorithmTest.Entity.HuffmanEntity;
import filestructure.MjFile;
import filestructure.MjFileBody;
import filestructure.MjFileHeader;

public class RealCompressTest {
	public static void main(String[] args) throws IOException {
		
		Instant begin = Instant.now();
		
		String fileName = "test.docx";
		
		Path path = Paths.get(fileName);
		
		long bytes = Files.size(path);
		long kilobyte = bytes / 1024;
		
		System.out.println(bytes + "bytes");
		System.out.println(kilobyte + "kbytes");
		
		byte[] b = new byte[(int)bytes];
		
		StringBuilder s = new StringBuilder();
		FileInputStream fis = new FileInputStream(fileName);
		DataInputStream dis = new DataInputStream(fis);
		
		int len;
		while ((len = dis.read(b)) > 0) {
			for(int i = 0; i < len; i++) {
				s.append(String.format("%02X", b[i]));
			}
		}
		
		dis.close();
		fis.close();
				
		String hex = s.toString();
		
		System.out.println("바이너리 사이즈 : " + bytes);
		System.out.println("bin to hex : " + hex.length());
		
		HuffmanCompress hc = new HuffmanCompress();
		HuffmanEntity entity = hc.compress(fileName, hex);
		String encodedHexString = entity.getEncodedCode();
		
		byte[] compressedByte = new byte[encodedHexString.length()/2];
		for(int i = 0; i < compressedByte.length; i++) {
			compressedByte[i] = (byte) Integer.parseInt(encodedHexString.substring(2*i, 2*i+2),16);
		}
		
		System.out.println("hex to byte : " + compressedByte.length);
		
		//MjFileHeader header = new MjFileHeader("CompressTest.mj", "test.hwp", 300, 100);
		MjFileBody body = new MjFileBody(compressedByte);
		//MjFile file = new MjFile(header, body);
		
		FileOutputStream fos = new FileOutputStream("CompressTest.mj");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		//oos.writeObject(file);
		oos.close();
		fos.close();
		
		Instant end = Instant.now();
		System.out.println(Duration.between(begin, end).toSeconds() + "초 걸림");
	}
}
