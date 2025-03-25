package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import filestructure.LZFile;
import filestructure.MjFile;

public class FileProcessingService {

	//직렬화 입출력을 통해 파일을 읽어들여 mjFile 객체를 반환한다.
	public MjFile getMjFileData(File file) {
		
		FileInputStream fis;
		ObjectInputStream ois;
		MjFile mjFile = null;
		
		try {
			fis = new FileInputStream(file.getAbsolutePath());
			ois = new ObjectInputStream(fis);
			mjFile = (MjFile)ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return mjFile;
	}

	public LZFile getLZFileData(File file) {
		
		FileInputStream fis;
		ObjectInputStream ois;
		LZFile lzFile = null;
		
		try {
			fis = new FileInputStream(file.getAbsolutePath());
			ois = new ObjectInputStream(fis);
			lzFile = (LZFile)ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return lzFile;
	}
	
	//파일을 삭제한다
	public void deleteFile(File file) {
		file.delete();
	}
}
