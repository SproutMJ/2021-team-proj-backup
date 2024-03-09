package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import filestructure.MjFile;
import service.CompressionService;
import service.CompressionServiceImpl;
import service.FileProcessingService;
import service.FileProcessingServiceImpl;
import service.ProcessService;
import service.ProcessServiceImpl;
import view.MainFrame;
import view.MainPanel;
import view.OpnZipFilePanel;

//MainFrame의 이벤트 초기화를 담당하는 Controller 클래스이다
public class MainFrameController {

	
	//자기 자신의 이벤트 처리를 위한 자기 자신의 panel과 panel들간의 화면 전환을 위한 다른 panel들 또한 가지고 있는 상태이다
	// 또한 압축, 압축해제, 파일 처리등의 서비스를 호출하기위해 service Class들 또한 멤버로 가지고 있다.
	private MainFrame mainFrame;
	private MainPanel mainPanel;
	private OpnZipFilePanel opnZipFilePanel;
	private CompressionService compressService;
	private FileProcessingService fileProcessingService;
	private ProcessService processService;
	
	public MainFrameController(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		opnZipFilePanel = mainFrame.getOpnZipFilePanel();
		mainPanel = mainFrame.getMainPanel();
		this.compressService = new CompressionServiceImpl();
		this.fileProcessingService = new FileProcessingServiceImpl();
		this.processService = new ProcessServiceImpl();
		eventInit();
	}
	
	
	public void eventInit() {
		
		// 압축 파일 열기 메뉴 MainPanelController의 압축파일열기 기능과 동일하다
		mainFrame.getOpnCompFile().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int response = chooser.showOpenDialog(null); // 파일 열기 성공하면 0 실패하면 1 반환
				
				if(response == JFileChooser.APPROVE_OPTION) {
					File file = new File(chooser.getSelectedFile().getAbsolutePath());
					
					String fileName = file.getName();
					String ext = fileName.substring(fileName.lastIndexOf(".")+1);
					
					if(ext.equals("mj")) {
						MjFile mjFile = fileProcessingService.getMjFileData(file);
						
						opnZipFilePanel.getRoot().setUserObject(file.getName()); // TreeView 파일 이름 세팅
						String fileSize = Integer.toString(mjFile.getHeader().getFileSize());
						int compressedFileSize = mjFile.getHeader().getCompressedFileSize();
						
						String headings[] = new String[] {"파일명", "원본 크기", "압축 크기", "파일 위치"};
						String data[][] = new String[][] {
							{file.getName(), fileSize, Integer.toString(compressedFileSize), file.getAbsolutePath()}
						};
						
						DefaultTableModel dtm = new DefaultTableModel(data, headings); // FileView 데이터 세팅
						opnZipFilePanel.getTable().setModel(dtm);
						opnZipFilePanel.setVisible(true);
						mainPanel.setVisible(false);
					}else {
						JOptionPane.showMessageDialog(null, "확장자명이 mj인 파일만 열 수 있습니다.");
					}
					
				}
			}
			
		});
		
		// 파일 닫기 메뉴 단순히 opnZipFilePanel을 닫아주고 mainPanel을 열어준다
		mainFrame.getFileClose().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.setVisible(true);
				opnZipFilePanel.setVisible(false);
			}
		});
		
		// 파일 삭제 메뉴 opnZipfilePanel이 열려있는 상태에서만 작동한다 해당 파일 정보를 fileProcessService를 받아 삭제한다
		mainFrame.getFileDelete().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(opnZipFilePanel.isVisible()) {
					
					JTable table = opnZipFilePanel.getTable();
					int columnCount = table.getColumnCount();
					
					String filePath;
					
					if(columnCount == 4) {
						filePath = (String) opnZipFilePanel.getTable().getValueAt(0, 3);
					}else {
						filePath = (String) opnZipFilePanel.getTable().getValueAt(0, 2);
					}
					

					File file = new File(filePath);
					fileProcessingService.deleteFile(file);
					mainPanel.setVisible(true);
					opnZipFilePanel.setVisible(false);
					
					JOptionPane.showMessageDialog(null, "파일 삭제를 완료했습니다");
				}
			}
		});
		
		// 새 창 띄우기 메뉴에 대한 이벤트 처리이다. processService의 서비스를 받아 새로운 프로세스를 하나 생성한다
		mainFrame.getOpenNewWindow().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processService.openNewWindow();
			}
			
		});
	}
}
