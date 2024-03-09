package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import AutoTranslate.Translate;
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

public class MainFrameController {

	private MainFrame mainFrame;
	private MainPanel mainPanel;
	private OpnZipFilePanel opnZipFilePanel;
	private CompressionService compressService;
	private FileProcessingService fileProcessingService;
	private ProcessService processService;
	private Translate m_Translate;
	
	public MainFrameController(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		opnZipFilePanel = mainFrame.getOpnZipFilePanel();
		mainPanel = mainFrame.getMainPanel();
		this.compressService = new CompressionServiceImpl();
		this.fileProcessingService = new FileProcessingServiceImpl();
		this.processService = new ProcessServiceImpl();
		
		this.m_Translate = new Translate();
		
		eventInit();
	}
	
	
	public void eventInit() {
		
		// 압축 파일 열기 메뉴
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
		
		// 파일 닫기 메뉴
		mainFrame.getFileClose().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.setVisible(true);
				opnZipFilePanel.setVisible(false);
			}
		});
		
		// 파일 삭제 메뉴
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
		
		// 새 창 띄우기 메뉴
		mainFrame.getOpenNewWindow().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processService.openNewWindow();
			}
			
		});
		
		
		mainFrame.getTranslateKor().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ChangeLang = 1;
				
				mainFrame.getFileMenu().setText(m_Translate.getTransSentence(mainFrame.getFileMenu().getText(), ChangeLang));
				mainFrame.getOpnCompFile().setText((m_Translate.getTransSentence(mainFrame.getOpnCompFile().getText(), ChangeLang)));
				mainFrame.getFileClose().setText((m_Translate.getTransSentence(mainFrame.getFileClose().getText(), ChangeLang)));
				mainFrame.getFileDelete().setText((m_Translate.getTransSentence(mainFrame.getFileDelete().getText(), ChangeLang)));
				mainFrame.getOpenNewWindow().setText((m_Translate.getTransSentence(mainFrame.getOpenNewWindow().getText(), ChangeLang)));
				
				mainFrame.getJMEdit().setText((m_Translate.getTransSentence(mainFrame.getJMEdit().getText(), ChangeLang)));
				mainFrame.getChangeName().setText((m_Translate.getTransSentence(mainFrame.getChangeName().getText(), ChangeLang)));
				mainFrame.getAddFile().setText((m_Translate.getTransSentence(mainFrame.getAddFile().getText(), ChangeLang)));
				mainFrame.getDeleteFile().setText((m_Translate.getTransSentence(mainFrame.getDeleteFile().getText(), ChangeLang)));
				mainFrame.getEditFileExpl().setText((m_Translate.getTransSentence(mainFrame.getEditFileExpl().getText(), ChangeLang)));
				
				mainFrame.getJMFind().setText((m_Translate.getTransSentence(mainFrame.getJMFind().getText(), ChangeLang)));
				mainFrame.getSearchFile().setText((m_Translate.getTransSentence(mainFrame.getSearchFile().getText(), ChangeLang)));
				
				mainFrame.getJMSetting().setText((m_Translate.getTransSentence(mainFrame.getJMSetting().getText(), ChangeLang)));
				mainFrame.getSetting().setText((m_Translate.getTransSentence(mainFrame.getSetting().getText(), ChangeLang)));
				
				mainFrame.getViewFileList().setText((m_Translate.getTransSentence(mainFrame.getViewFileList().getText(), ChangeLang)));
				mainFrame.getViewDetails().setText((m_Translate.getTransSentence(mainFrame.getViewDetails().getText(), ChangeLang)));
				mainFrame.getViewSimply().setText((m_Translate.getTransSentence(mainFrame.getViewSimply().getText(), ChangeLang)));
				mainFrame.getViewIcon().setText((m_Translate.getTransSentence(mainFrame.getViewIcon().getText(), ChangeLang)));
				mainFrame.getViewBIcon().setText((m_Translate.getTransSentence(mainFrame.getViewBIcon().getText(), ChangeLang)));
				
				mainFrame.getTranslate().setText((m_Translate.getTransSentence(mainFrame.getTranslate().getText(), ChangeLang)));
				
				mainFrame.getJMHelp().setText((m_Translate.getTransSentence(mainFrame.getJMHelp().getText(), ChangeLang)));
				mainFrame.getInfo().setText((m_Translate.getTransSentence(mainFrame.getInfo().getText(), ChangeLang)));	
			}	
		});
		
		mainFrame.getTranslateEng().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ChangeLang = 0;
				
				mainFrame.getFileMenu().setText(m_Translate.getTransSentence(mainFrame.getFileMenu().getText(), ChangeLang));
				mainFrame.getOpnCompFile().setText((m_Translate.getTransSentence(mainFrame.getOpnCompFile().getText(), ChangeLang)));
				mainFrame.getFileClose().setText((m_Translate.getTransSentence(mainFrame.getFileClose().getText(), ChangeLang)));
				mainFrame.getFileDelete().setText((m_Translate.getTransSentence(mainFrame.getFileDelete().getText(), ChangeLang)));
				mainFrame.getOpenNewWindow().setText((m_Translate.getTransSentence(mainFrame.getOpenNewWindow().getText(), ChangeLang)));
				
				mainFrame.getJMEdit().setText((m_Translate.getTransSentence(mainFrame.getJMEdit().getText(), ChangeLang)));
				mainFrame.getChangeName().setText((m_Translate.getTransSentence(mainFrame.getChangeName().getText(), ChangeLang)));
				mainFrame.getAddFile().setText((m_Translate.getTransSentence(mainFrame.getAddFile().getText(), ChangeLang)));
				mainFrame.getDeleteFile().setText((m_Translate.getTransSentence(mainFrame.getDeleteFile().getText(), ChangeLang)));
				mainFrame.getEditFileExpl().setText((m_Translate.getTransSentence(mainFrame.getEditFileExpl().getText(), ChangeLang)));
				
				mainFrame.getJMFind().setText((m_Translate.getTransSentence(mainFrame.getJMFind().getText(), ChangeLang)));
				mainFrame.getSearchFile().setText((m_Translate.getTransSentence(mainFrame.getSearchFile().getText(), ChangeLang)));
				
				mainFrame.getJMSetting().setText((m_Translate.getTransSentence(mainFrame.getJMSetting().getText(), ChangeLang)));
				mainFrame.getSetting().setText((m_Translate.getTransSentence(mainFrame.getSetting().getText(), ChangeLang)));
				
				mainFrame.getViewFileList().setText((m_Translate.getTransSentence(mainFrame.getViewFileList().getText(), ChangeLang)));
				mainFrame.getViewDetails().setText((m_Translate.getTransSentence(mainFrame.getViewDetails().getText(), ChangeLang)));
				mainFrame.getViewSimply().setText((m_Translate.getTransSentence(mainFrame.getViewSimply().getText(), ChangeLang)));
				mainFrame.getViewIcon().setText((m_Translate.getTransSentence(mainFrame.getViewIcon().getText(), ChangeLang)));
				mainFrame.getViewBIcon().setText((m_Translate.getTransSentence(mainFrame.getViewBIcon().getText(), ChangeLang)));
				
				mainFrame.getTranslate().setText((m_Translate.getTransSentence(mainFrame.getTranslate().getText(), ChangeLang)));
				
				mainFrame.getJMHelp().setText((m_Translate.getTransSentence(mainFrame.getJMHelp().getText(), ChangeLang)));
				mainFrame.getInfo().setText((m_Translate.getTransSentence(mainFrame.getInfo().getText(), ChangeLang)));	
				
				mainFrame.setVisible(false);
				mainFrame.setVisible(true);
			}	
		});
		
	}
}
