package view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {

	// 실행시 맨 처음 화면을 담당하는 mainPanel
	private MainPanel mainPanel;
	// 파일 조회 및 압축, 압축해제를 실행하는 화면인 opnZipFilePanel
	private OpnZipFilePanel opnZipFilePanel;
	// 압축, 압축해제에 대한 진행상황을 나타내는 팝업화면
	private ProgressBarPopup progressBarPopup;
	// 메뉴바와 메뉴 아이템
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem opnCompFile;
	private JMenuItem fileClose;
	private JMenuItem fileDelete;
	private JMenuItem openNewWindow;
	
	// 생성자에선 단순히 클래스 멤버 변수들을 초기화하고 화면을 꾸미는 역할만 맡는다. 이벤트 초기화는 컨트롤러에서 담당
	public MainFrame() {
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 800, 30);
		this.add(menuBar);
		
		fileMenu = new JMenu("파일");
		menuBar.add(fileMenu);
		
		opnCompFile = new JMenuItem("압축 파일 열기");
		fileMenu.add(opnCompFile);
		
		fileClose = new JMenuItem("파일 닫기");
		fileMenu.add(fileClose);
				
		fileDelete = new JMenuItem("파일 삭제");
		fileMenu.add(fileDelete);
		
		openNewWindow = new JMenuItem("새 창 띄우기");
		fileMenu.add(openNewWindow);
		
		// -------------------------------------------------------------
		
		JMenu JMEdit = new JMenu("편집");
		menuBar.add(JMEdit);
		
		JMenuItem ChangeName = new JMenuItem("이름 바꾸기");
		JMEdit.add(ChangeName);
		
		JMenuItem AddFile = new JMenuItem("파일 추가");
		JMEdit.add(AddFile);
		
		JMenuItem DeleteFile = new JMenuItem("파일 삭제");
		JMEdit.add(DeleteFile);
		
		JMenuItem EditFileExpl = new JMenuItem("압축 파일 설명 편집");
		JMEdit.add(EditFileExpl);
		
		// -------------------------------------------------------------
		
		JMenu JMFind = new JMenu("찾기");
		menuBar.add(JMFind);
		
		JMenuItem SearchFile = new JMenuItem("파일 찾기");
		JMFind.add(SearchFile);
		
		// -------------------------------------------------------------
		
		JMenu JMSetting = new JMenu("설정");
		menuBar.add(JMSetting);
		
		JMenuItem Setting = new JMenuItem("환경 설정");
		JMSetting.add(Setting);
		
		// -------------------------------------------------------------
		
		JMenu JMView = new JMenu("보기");
		menuBar.add(JMView);
		
		JMenu ViewFileList = new JMenu("파일 목록");
		JMView.add(ViewFileList);
		
		JMenuItem ViewDetails = new JMenuItem("자세히");
		ViewFileList.add(ViewDetails);
		
		JMenuItem ViewSimply = new JMenuItem("간단히");
		ViewFileList.add(ViewSimply);
		
		JMenuItem ViewIcon = new JMenuItem("아이콘");
		ViewFileList.add(ViewIcon);
		
		JMenuItem ViewBIcon = new JMenuItem("큰 아이콘");
		ViewFileList.add(ViewBIcon);
		
		JMenu Translate = new JMenu("언어 설정");
		JMView.add(Translate);
		
		JMenuItem Trans_Kor = new JMenuItem("한국어");
		Translate.add(Trans_Kor);
		
		JMenuItem Trans_Eng = new JMenuItem("English");
		Translate.add(Trans_Eng);
		
		// -------------------------------------------------------------
		
		JMenu JMHelp = new JMenu("도움말");
		menuBar.add(JMHelp);
		
		JMenuItem Info = new JMenuItem("정보");
		JMHelp.add(Info);
		
		mainPanel = new MainPanel();
		add(mainPanel);
		
		opnZipFilePanel = new OpnZipFilePanel();
		add(opnZipFilePanel);
		
		progressBarPopup = new ProgressBarPopup();
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public OpnZipFilePanel getOpnZipFilePanel() {
		return opnZipFilePanel;
	}

	public ProgressBarPopup getProgressBarPopup() {
		return progressBarPopup;
	}
	
	public JMenu getFileMenu() {
		return fileMenu;
	}

	public JMenuItem getOpnCompFile() {
		return opnCompFile;
	}

	public JMenuItem getFileClose() {
		return fileClose;
	}

	public JMenuItem getFileDelete() {
		return fileDelete;
	}

	public JMenuItem getOpenNewWindow() {
		return openNewWindow;
	}

	
}