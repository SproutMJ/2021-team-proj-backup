package service;

import java.io.File;
import java.io.IOException;

public class ProcessServiceImpl implements ProcessService{

	// 새 창 띄우기의 구현이며 프로젝트 내부에 존재하는 jar파일을 java -jar 커맨드로 실행시킨다.
	// 새로운 프로세스가 생성되어야 한다고 생각했기떄문에 Process 처리를 담당하는 ProcessBuilder를 선언하여 구현하였다
	@Override
	public void openNewWindow() {
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "mzip.jar");
		builder.directory(new File(getClass().getResource("/").getPath()));
		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
