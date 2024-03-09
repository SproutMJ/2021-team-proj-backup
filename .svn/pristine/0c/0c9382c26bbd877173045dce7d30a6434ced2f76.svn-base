package config;

import controller.MainFrameController;
import controller.MainPanelController;
import controller.OpnZipFileController;
import view.MainFrame;

public class DependencyManager {

	private static DependencyManager manager = new DependencyManager();
	
	//의존관계 정리를 위한 의존성 관리용 클래스이다. start함수는 싱글톤을 반환하며 생성자에서 화면과 controller들을 연결해주는 역할을 맡는다
	private DependencyManager() {
		MainFrame mainFrame = new MainFrame();
		MainFrameController mainFrameController = new MainFrameController(mainFrame);
		MainPanelController mainPanelController = new MainPanelController(mainFrame);
		OpnZipFileController opnZilFileController = new OpnZipFileController(mainFrame);
	}
	
	public static DependencyManager managerStart() {
		return manager;
	}
}
