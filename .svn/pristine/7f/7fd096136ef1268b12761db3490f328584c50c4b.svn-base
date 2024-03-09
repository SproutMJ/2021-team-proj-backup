package AlgorithmTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.nio.Buffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class LZ77DecoderTest {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			try (ObjectInputStream reader = new ObjectInputStream(
					new FileInputStream(fileChooser.getSelectedFile().getPath()));
					FileOutputStream writer = new FileOutputStream("압축 해제 위치")) {
				long debug = 0;
				LinkedList<Byte> buffer = new LinkedList<>();
				Object t;
				while ((t = reader.readObject()) != null) {
					long length, distance;
					if (t instanceof CompressedTuple) {
						LinkedList<Byte> temp = new LinkedList<>();

						if (((CompressedTuple) t).distance instanceof Byte) {
							if (((CompressedTuple) t).length instanceof Byte) {
								if (((CompressedTuple) t).distance.byteValue() < 0)
									distance = ((CompressedTuple) t).distance.byteValue() + 256;
								else
									distance = ((CompressedTuple) t).distance.byteValue();
								if (((CompressedTuple) t).length.byteValue() < 0)
									length = ((CompressedTuple) t).length.byteValue() + 256;
								else
									length = ((CompressedTuple) t).length.byteValue();

							} else if (((CompressedTuple) t).length instanceof Short) {
								if (((CompressedTuple) t).distance.byteValue() < 0)
									distance = ((CompressedTuple) t).distance.byteValue() + 256;
								else
									distance = ((CompressedTuple) t).distance.byteValue();
								if (((CompressedTuple) t).length.shortValue() < 0)
									length = ((CompressedTuple) t).length.shortValue() + 65536;
								else
									length = ((CompressedTuple) t).length.shortValue();
							} else {
								if (((CompressedTuple) t).distance.byteValue() < 0)
									distance = ((CompressedTuple) t).distance.byteValue() + 256;
								else
									distance = ((CompressedTuple) t).distance.byteValue();
								if (((CompressedTuple) t).length.intValue() < 0)
									length = ((CompressedTuple) t).length.intValue() + 4294967296L;
								else
									length = ((CompressedTuple) t).length.intValue();
							}
						} else if (((CompressedTuple) t).distance instanceof Short) {
							if (((CompressedTuple) t).length instanceof Byte) {
								if (((CompressedTuple) t).distance.shortValue() < 0)
									distance = ((CompressedTuple) t).distance.shortValue() + 65536;
								else
									distance = ((CompressedTuple) t).distance.shortValue();
								if (((CompressedTuple) t).length.byteValue() < 0)
									length = ((CompressedTuple) t).length.byteValue() + 256;
								else
									length = ((CompressedTuple) t).length.byteValue();
							} else if (((CompressedTuple) t).length instanceof Short) {
								if (((CompressedTuple) t).distance.shortValue() < 0)
									distance = ((CompressedTuple) t).distance.shortValue() + 65536;
								else
									distance = ((CompressedTuple) t).distance.shortValue();
								if (((CompressedTuple) t).length.shortValue() < 0)
									length = ((CompressedTuple) t).length.shortValue() + 65536;
								else
									length = ((CompressedTuple) t).length.shortValue();
							} else {
								if (((CompressedTuple) t).distance.shortValue() < 0)
									distance = ((CompressedTuple) t).distance.shortValue() + 65536;
								else
									distance = ((CompressedTuple) t).distance.shortValue();
								if (((CompressedTuple) t).length.intValue() < 0)
									length = ((CompressedTuple) t).length.intValue() + 4294967296L;
								else
									length = ((CompressedTuple) t).length.intValue();
							}
						} else {
							if (((CompressedTuple) t).length instanceof Byte) {
								if (((CompressedTuple) t).distance.intValue() < 0)
									distance = ((CompressedTuple) t).distance.intValue() + 4294967296L;
								else
									distance = ((CompressedTuple) t).distance.intValue();
								if (((CompressedTuple) t).length.byteValue() < 0)
									length = ((CompressedTuple) t).length.byteValue() + 256;
								else
									length = ((CompressedTuple) t).length.byteValue();
							} else if (((CompressedTuple) t).length instanceof Short) {
								if (((CompressedTuple) t).distance.intValue() < 0)
									distance = ((CompressedTuple) t).distance.intValue() + 4294967296L;
								else
									distance = ((CompressedTuple) t).distance.intValue();
								if (((CompressedTuple) t).length.shortValue() < 0)
									length = ((CompressedTuple) t).length.shortValue() + 65536;
								else
									length = ((CompressedTuple) t).length.shortValue();
							} else {
								if (((CompressedTuple) t).distance.intValue() < 0)
									distance = ((CompressedTuple) t).distance.intValue() + 4294967296L;
								else
									distance = ((CompressedTuple) t).distance.intValue();
								if (((CompressedTuple) t).length.intValue() < 0)
									length = ((CompressedTuple) t).length.intValue() + 4294967296L;
								else
									length = ((CompressedTuple) t).length.intValue();
							}
						}
						debug+=length;
						
						for (long i = 0; i < length; i++) {
							temp.add((buffer.get((int) ((int) (buffer.size() - distance) + i))));
						}
						temp.add(((CompressedTuple) t).literal);
						buffer.addAll(temp);
						byte[] byteArray = new byte[temp.size()];
						for (int index = 0; index < temp.size(); index++) {
						    byteArray[index] = temp.get(index);
						}
						writer.write(byteArray);
						//System.out.println(debug += length);
					} else {
						buffer.add(((Tuple) t).literal);
						byte[] byteArray = new byte[1];
						byteArray[0] = ((Tuple) t).literal;
						writer.write(byteArray);
						//System.out.println(debug += 1);
					}
					writer.flush();
					while (buffer.size() > 64000)
						buffer.remove(0);
				}
				System.out.println("complete");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
