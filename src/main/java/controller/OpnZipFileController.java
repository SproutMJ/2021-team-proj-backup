package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
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
import view.ProgressBarPopup;

//OpnZilFilePanel의 컨트롤러이다
public class OpnZipFileController {

	//자기 자신의 이벤트 처리를 위한 자기 자신의 panel과 panel들간의 화면 전환을 위한 다른 panel들 또한 가지고 있는 상태이다
	// 또한 압축, 압축해제, 파일 처리등의 서비스를 호출하기위해 service Class들 또한 멤버로 가지고 있다.
	private OpnZipFilePanel zipFilePanel;
	private MainPanel mainPanel;
	private ProgressBarPopup popup;
	private CompressionService compressionService;
	private FileProcessingService fileProcessingService;
	
	//
	public OpnZipFileController(MainFrame mainFrame) {
		this.zipFilePanel = mainFrame.getOpnZipFilePanel();
		this.mainPanel = mainFrame.getMainPanel();
		this.popup = mainFrame.getProgressBarPopup();
		this.compressionService = new CompressionServiceImpl();
		this.fileProcessingService = new FileProcessingServiceImpl();
		eventInit();
	}
	
	//컴포넌트의 이벤트 처리를 담당하는 함수
	private void eventInit() {
		
		//압축 해제 버튼
		zipFilePanel.getBtnDecompress().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//JTable의 Column 개수를 판단하여 압축파일 화면인지 압축해제 파일 화면인지 검사한다
				int columnCount = zipFilePanel.getTable().getColumnCount();
				if(columnCount != 4) {
					JOptionPane.showMessageDialog(null, "해당 파일은 압축 파일이 아닙니다.");
					return;
				}
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int response = fileChooser.showSaveDialog(null);
				
				//파일 선택이 된 경우 파일경로등을 인자로 보내 압축해제를 실행한다. 화면 갱신을 위해 SwingWorker를 사용하여 멀티스레드로 처리한다
				if(response == JFileChooser.APPROVE_OPTION) {
					String decompressedfilePath = fileChooser.getSelectedFile().getAbsolutePath();
					String compressedFilePath = (String)zipFilePanel.getTable().getModel().getValueAt(0, 3);
					File compressedFile = new File(compressedFilePath);
					
					final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

						@Override
						protected Void doInBackground() throws Exception {
							
							popup.setVisible(true);
							compressionService.deCompressFile(compressedFile, decompressedfilePath, popup.getProgressBar());
							popup.setVisible(false);
							JOptionPane.showMessageDialog(null, "압축해제가 완료되었습니다.");
							return null;
						}
					};
					worker.execute();
				}
			}
			
		});
		
		//LZ77 압축 해제 버튼
		zipFilePanel.getLZ77DeCompress().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//JTable의 Column 개수를 판단하여 압축파일 화면인지 압축해제 파일 화면인지 검사한다
				int columnCount = zipFilePanel.getTable().getColumnCount();
				if(columnCount != 4) {
					JOptionPane.showMessageDialog(null, "해당 파일은 압축 파일이 아닙니다.");
					return;
				}
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int response = fileChooser.showSaveDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					String decompressedfilePath = fileChooser.getSelectedFile().getAbsolutePath();
					String compressedFilePath = (String)zipFilePanel.getTable().getModel().getValueAt(0, 3);
					File compressedFile = new File(compressedFilePath);
					
					final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

						@Override
						protected Void doInBackground() throws Exception {
							
							popup.setVisible(true);
							compressionService.deCompressLZFile(compressedFile, decompressedfilePath, popup.getProgressBar());
							popup.setVisible(false);
							JOptionPane.showMessageDialog(null, "압축해제가 완료되었습니다.");
							return null;
						}
					};
					worker.execute();
				}
				
			}
			
		});
		
		//새로 압축 버튼
		zipFilePanel.getBtnNewCompress().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int response = fileChooser.showSaveDialog(null);
				
				//파일 선택이 된 경우 파일 경로등을 인자로 보내 압축을 실행한다 화면 갱신을 위해 Swing Worker를 통해 멀티스레딩으로 압축 해제를 처리한다
				if(response == JFileChooser.APPROVE_OPTION) {
					
					File saveFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
					String originFilePath = (String)zipFilePanel.getTable().getModel().getValueAt(0, 2);
					File originFile = new File(originFilePath);
					
					
					final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

						@Override
						protected Void doInBackground() throws Exception {
							
							popup.setVisible(true);
							compressionService.compressFile(saveFile, originFile, popup.getProgressBar());
							popup.setVisible(false);
							JOptionPane.showMessageDialog(null, "압축이 완료되었습니다.");
							
							return null;
						}
						
					};
					worker.execute();
					
				}
				
			}
			
		});
		
		//LZ77 압축 버튼
		zipFilePanel.getBtnLZ77Compress().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				int response = fileChooser.showSaveDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					
					File saveFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
					String originFilePath = (String)zipFilePanel.getTable().getModel().getValueAt(0, 2);
					File originFile = new File(originFilePath);
					
					final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

						@Override
						protected Void doInBackground() throws Exception {
							
							popup.setVisible(true);
							compressionService.lz77Compress(saveFile, originFile, popup.getProgressBar());
							popup.setVisible(false);
							JOptionPane.showMessageDialog(null, "압축이 완료되었습니다.");
							
							return null;
						}
						
					};
					worker.execute();
				}
				
			}
			
		});
		
		//파일 열기 버튼
		zipFilePanel.getBtnFileOpn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int response = chooser.showOpenDialog(null);
				
				//파일을 선택한다
				if(response == JFileChooser.APPROVE_OPTION) {
					File file = new File(chooser.getSelectedFile().getAbsolutePath());
					String fileName = file.getName();
					String extension = fileName.substring(fileName.lastIndexOf(".")+1);
					
					//파일 확장자가 mj인 경우에만 압축을 실행한다
					if(extension.equals("mj")) {
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
					}else if(extension.equals("lz77")) {
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
						
					}else { // 그렇지 않으면 안내메세지를 전송한다
						JOptionPane.showMessageDialog(null, "확장자가 mj나 lz77인 파일을 선택해 주세요");
					}
					
				}
			}
			
		});
	}
}
