package view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import AutoTranslate.Translate;

public class MainFrame extends JFrame {

	private MainPanel mainPanel;
	private OpnZipFilePanel opnZipFilePanel;
	private ProgressBarPopup progressBarPopup;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem opnCompFile;
	private JMenuItem fileClose;
	private JMenuItem fileDelete;
	private JMenuItem openNewWindow;
	
	private JMenu JMEdit;
	private JMenuItem ChangeName;
	private JMenuItem AddFile;
	private JMenuItem DeleteFile;
	private JMenuItem EditFileExpl;
	
	private JMenu JMFind;
	private JMenuItem SearchFile;
	
	private JMenu JMSetting;
	private JMenuItem Setting;
	
	private JMenu ViewFileList;
	private JMenuItem ViewDetails;
	private JMenuItem ViewSimply;
	private JMenuItem ViewIcon;
	private JMenuItem ViewBIcon;
	
	private JMenu Translate;
	private JMenuItem Trans_Kor;
	private JMenuItem Trans_Eng;
	
	private JMenu JMHelp;
	private JMenuItem Info;
	
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
		
		JMEdit = new JMenu("편집");
		menuBar.add(JMEdit);
		
		ChangeName = new JMenuItem("이름 바꾸기");
		JMEdit.add(ChangeName);
		
		AddFile = new JMenuItem("파일 추가");
		JMEdit.add(AddFile);
		
		DeleteFile = new JMenuItem("파일 삭제");
		JMEdit.add(DeleteFile);
		
		EditFileExpl = new JMenuItem("압축 파일 설명 편집");
		JMEdit.add(EditFileExpl);
		
		// -------------------------------------------------------------
		
		JMFind = new JMenu("찾기");
		menuBar.add(JMFind);
		
		SearchFile = new JMenuItem("파일 찾기");
		JMFind.add(SearchFile);
		
		// -------------------------------------------------------------
		
		JMSetting = new JMenu("설정");
		menuBar.add(JMSetting);
		
		Setting = new JMenuItem("환경 설정");
		JMSetting.add(Setting);
		
		// -------------------------------------------------------------
		
		ViewFileList = new JMenu("보기");
		menuBar.add(ViewFileList);
		
		ViewDetails = new JMenuItem("자세히");
		ViewFileList.add(ViewDetails);
		
		ViewSimply = new JMenuItem("간단히");
		ViewFileList.add(ViewSimply);
		
		ViewIcon = new JMenuItem("아이콘");
		ViewFileList.add(ViewIcon);
		
		ViewBIcon = new JMenuItem("큰 아이콘");
		ViewFileList.add(ViewBIcon);
		
		Translate = new JMenu("언어 설정");
		ViewFileList.add(Translate);
		
		Trans_Kor = new JMenuItem("한국어");
		Translate.add(Trans_Kor);
		
		Trans_Eng = new JMenuItem("English");
		Translate.add(Trans_Eng);
		
		// -------------------------------------------------------------
		
		JMHelp = new JMenu("도움말");
		menuBar.add(JMHelp);
		
		Info = new JMenuItem("정보");
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
	
	public JMenu getJMEdit() {
		return JMEdit;
	}
	
	public JMenuItem getChangeName() {
		return ChangeName;
	}
	
	public JMenuItem getAddFile() {
		return AddFile;
	}
	
	public JMenuItem getDeleteFile() {
		return DeleteFile;
	}
	
	public JMenuItem getEditFileExpl() {
		return EditFileExpl;
	}
	
	public JMenu getJMFind() {
		return JMFind;
	}
	
	public JMenuItem getSearchFile() {
		return SearchFile;
	}
	
	public JMenu getJMSetting() {
		return JMSetting;
	}
	
	public JMenuItem getSetting() {
		return Setting;
	}
	
	public JMenu getViewFileList() {
		return ViewFileList;
	}
	
	public JMenuItem getViewDetails() {
		return ViewDetails;
	}
	
	public JMenuItem getViewSimply() {
		return ViewSimply;
	}
	
	public JMenuItem getViewIcon() {
		return ViewIcon;
	}
	
	public JMenuItem getViewBIcon() {
		return ViewBIcon;
	}
	
	public JMenu getTranslate() {
		return Translate;
	}
	
	public JMenu getJMHelp() {
		return JMHelp;
	}
	
	public JMenuItem getInfo() {
		return Info;
	}

	//-------------------------------------------------------------------//
	
	public JMenuItem getTranslateKor(){
		return Trans_Kor;
	}
	
	public JMenuItem getTranslateEng(){
		return Trans_Eng;
	}
	//-------------------------------------------------------------------//
}