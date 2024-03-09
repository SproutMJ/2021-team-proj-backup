package AlgorithmTest;

import java.util.LinkedList;
import java.util.List;

import AlgorithmTest.Entity.HuffmanEntity;

public class HuffmanDecompress {

	public static Byte[] decompress(HuffmanEntity entity) {
		return hexToByteArray(decode(entity, hexToBinary(entity)));
		
	}

	public static String hexToBinary(HuffmanEntity entity) {
		String binary = "";
		for (int i = 0; i < entity.getEncodedCode().length(); i++) {
			binary += entity.getHexToBi().get(entity.getEncodedCode().charAt(i));
		}
		return binary + entity.getLastBinary();
	}

	public static String decode(HuffmanEntity entity, String binaryCode) {
		String tmp = "";
		String decodingStr = "";
		for (int i = 0; i < binaryCode.length(); i++) {
			tmp += binaryCode.charAt(i);
			if (entity.getDecodingMap().get(tmp) != null) {
				decodingStr += entity.getDecodingMap().get(tmp);
				tmp = "";
			}
		}
		return decodingStr;
	}

	public static Byte[] hexToByteArray(String hex) {
		List<Byte> list = new LinkedList<>();
		for (int i=0; i <hex.length(); i++) {
			switch(hex.charAt(i)) {
			case '0':
				list.add((byte)0); list.add((byte)0); list.add((byte)0); list.add((byte)0);
				break;
			case '1':
				list.add((byte)0); list.add((byte)0); list.add((byte)0); list.add((byte)1);
				break;
			case '2':
				list.add((byte)0); list.add((byte)0); list.add((byte)1); list.add((byte)0);
				break;
			case '3':
				list.add((byte)0); list.add((byte)0); list.add((byte)1); list.add((byte)1);
				break;
			case '4':
				list.add((byte)0); list.add((byte)1); list.add((byte)0); list.add((byte)0);
				break;
			case '5':
				list.add((byte)0); list.add((byte)1); list.add((byte)0); list.add((byte)1);
				break;
			case '6':
				list.add((byte)0); list.add((byte)1); list.add((byte)1); list.add((byte)0);
				break;
			case '7':
				list.add((byte)0); list.add((byte)1); list.add((byte)1); list.add((byte)1);
				break;
			case '8':
				list.add((byte)1); list.add((byte)0); list.add((byte)0); list.add((byte)0);
				break;
			case '9':
				list.add((byte)1); list.add((byte)0); list.add((byte)0); list.add((byte)1);
				break;
			case 'A':
				list.add((byte)1); list.add((byte)0); list.add((byte)1); list.add((byte)0);
				break;
			case 'B':
				list.add((byte)1); list.add((byte)0); list.add((byte)1); list.add((byte)1);
				break;
			case 'C':
				list.add((byte)1); list.add((byte)1); list.add((byte)0); list.add((byte)0);
				break;
			case 'D':
				list.add((byte)1); list.add((byte)1); list.add((byte)0); list.add((byte)1);
				break;
			case 'E':
				list.add((byte)1); list.add((byte)1); list.add((byte)1); list.add((byte)0);
				break;
			case 'F':
				list.add((byte)1); list.add((byte)1); list.add((byte)1); list.add((byte)1);
				break;
			}
		}
		return list.toArray(new Byte[0]);
	}

}
