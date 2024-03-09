package serialization;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import filestructure.MjFile;

public class SerializationInputTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		MjFile file = null;
		
		FileInputStream fis = new FileInputStream("test.bin");
		ObjectInputStream ois = new ObjectInputStream(fis);
		file = (MjFile)ois.readObject();
		
		System.out.println(file.getHeader().getFileName());
		System.out.println(file.getHeader().getOriginName());
	}
}
