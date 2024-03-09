package serialization;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import filestructure.MjFile;
import filestructure.MjFileBody;
import filestructure.MjFileHeader;

public class SerializationOutputTest {
	public static void main(String[] args) throws IOException {
		//MjFileHeader header = new MjFileHeader("test", "originFile", 300, 100);
		byte[] b = new byte[4];
		for(int i = 0; i < b.length; i++) {
			b[i] = (byte) (i+96);
		}
		MjFileBody body = new MjFileBody(b);
		//MjFile file = new MjFile(header, body);
		
		FileOutputStream fos = new FileOutputStream("test.bin");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		//oos.writeObject(file);
		oos.close();
	}
}
