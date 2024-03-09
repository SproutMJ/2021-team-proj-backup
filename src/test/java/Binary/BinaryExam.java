package Binary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BinaryExam {

	 public static void main(String[] args) throws IOException {
		 Path src = Paths.get("..//TeamProj//src//test//java//Binary//123.hwp");
		 ByteBuffer buf = ByteBuffer.allocate(1048576);
		 StringBuilder s = new StringBuilder();
		 
		 try(FileChannel ifc = FileChannel.open(src, StandardOpenOption.READ)) {
			 int len = 0;
			 while(true) {
				 len = ifc.read(buf);
				 if(len == -1)
					 break;
				 
				 buf.flip();
				 byte[] arr = buf.array();
				 for (int i = 0; i < len; i++) {
					 s.append(String.format("%02X ", arr[i]));
				 }
				 buf.clear();
			 }
		}
		System.out.println(s); 
	}
}