package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import filestructure.LZFile;
import filestructure.MjFile;
import service.CompressionService;
import service.CompressionServiceImpl;
import service.FileProcessingService;
import service.FileProcessingServiceImpl;
import view.MainFrame;
import view.MainPanel;
import view.OpnZipFilePanel;

//MainPanel의 이벤트 초기화를 담당하는 Controller 클래스이다
public class MainPanelController {

	//자기 자신의 이벤트 처리를 위한 자기 자신의 panel과 panel들간의 화면 전환을 위한 다른 panel들 또한 가지고 있는 상태이다
	// 또한 압축, 압축해제, 파일 처리등의 서비스를 호출하기위해 service Class들 또한 멤버로 가지고 있다.
	private MainPanel mainPanel;
	private OpnZipFilePanel zipFilePanel;
	private CompressionService compressionService;
	private FileProcessingService fileProcessingService;

	
	public MainPanelController(MainFrame mainFrame) {
		this.mainPanel = mainFrame.getMainPanel();
		this.zipFilePanel = mainFrame.getOpnZipFilePanel();
		compressionService = new CompressionServiceImpl();
		fileProcessingService = new FileProcessingServiceImpl();
		eventinit();
	}
	
	private void eventinit() {
		
		//압축 파일 열기버튼에 대한 이벤트 처리이다
		mainPanel.getbuttonOpenFile().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int response = chooser.showOpenDialog(null); // 파일 열기 성공하면 0 실패하면 1 반환
				
				//파일을 열면 파일 정보를 적절하게 파싱하여 zipFilePanel로 보낸다 오픈한 파일이 mj파일일 경우에만 실행시키며 그렇지 않은 경우에는 안내메세지를 내보낸다
				if(response == JFileChooser.APPROVE_OPTION) {
					File file = new File(chooser.getSelectedFile().getAbsolutePath());
					
					String fileName = file.getName();
					String ext = fileName.substring(fileName.lastIndexOf(".")+1);
					
					if(ext.equals("mj")) {
						mainPanel.setVisible(false);					
						MjFile mjFile = fileProcessingService.getMjFileData(file);
						
						zipFilePanel.getRoot().setUserObject(file.getName()); // TreeView 파일 이름 세팅
						String fileSize = Integer.toString(mjFile.getHeader().getFileSize());
						int compressedFileSize = mjFile.getHeader().getCompressedFileSize();
						
						String headings[] = new String[] {"파일명", "원본 크기", "압축 크기", "파일 위치"};
						String data[][] = new String[][] {
							{file.getName(), fileSize, Integer.toString(compressedFileSize), file.getAbsolutePath()}
						};
						
						DefaultTableModel dtm = new DefaultTableModel(data, headings); // FileView 데이터 세팅
						zipFilePanel.getTable().setModel(dtm);
						zipFilePanel.setVisible(true);
					}else if(ext.equals("lz77")) {
						mainPanel.setVisible(false);
						LZFile lzFile = fileProcessingService.getLZFileData(file);
						
						zipFilePanel.getRoot().setUserObject(file.getName()); // TreeView 파일 이름 세팅
						String fileSize = Integer.toString(lzFile.getOriginFileSize());

						int compressedFileSize = (int) file.length();
						
						String headings[] = new String[] {"파일명", "원본 크기", "압축 크기", "파일 위치"};
						String data[][] = new String[][] {
							{file.getName(), fileSize, Integer.toString(compressedFileSize), file.getAbsolutePath()}
						};
						
						DefaultTableModel dtm = new DefaultTableModel(data, headings); // FileView 데이터 세팅
						zipFilePanel.getTable().setModel(dtm);
						zipFilePanel.setVisible(true);
						
					}else {
						JOptionPane.showMessageDialog(null, "확장자가 mj나 lz77인 파일을 선택해 주세요");
					}
					
				}
			}
		});
		
		//새로 압축하기 버튼에 대한 이벤트 처리이다
		mainPanel.getbuttonNewCompress().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				int response = chooser.showOpenDialog(null); // 파일 열기 성공하면 0 실패하면 1 반환
				
				//파일을 여는데 성공하면 파일 정보를 적절하게 파싱하여 zipFilePanel로 전송한다
				if(response == JFileChooser.APPROVE_OPTION) {
					File file = new File(chooser.getSelectedFile().getAbsolutePath());
					mainPanel.setVisible(false);
					
					zipFilePanel.getRoot().setUserObject(file.getName()); // TreeView 파일 이름 세팅
					String fileSize = Long.toString(file.length());
					
					String headings[] = new String[] {"파일명", "원본 크기", "파일 위치"};
					String data[][] = new String[][] {
						{file.getName(), fileSize, file.getAbsolutePath()}
					};
					
					DefaultTableModel dtm = new DefaultTableModel(data, headings); // FileView 데이터 세팅
					zipFilePanel.getTable().setModel(dtm);
					zipFilePanel.setVisible(true);
				}	
			
			}
		});
				
	}
}
