package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import filestructure.MjFile;

public class FileProcessingServiceImpl implements FileProcessingService{

	//직렬화 입출력을 통해 파일을 읽어들여 mjFile 객체를 반환한다.
	@Override
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

	//파일을 삭제한다
	@Override
	public void deleteFile(File file) {
		file.delete();
	}

}
