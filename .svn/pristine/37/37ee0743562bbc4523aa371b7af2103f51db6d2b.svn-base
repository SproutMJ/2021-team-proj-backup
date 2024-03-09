package view;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class OpnZipFilePanel extends JPanel {

	//파일 열기 버튼
	private JButton btnFileOpn;
	//압축 해제 버튼
	private JButton btnDecompress;
	//새로 압축 버튼
	private JButton btnNewCompress;
	
	// 다중파일 압축을 위해 만들어놓은 버튼들이며 현재 사용되지 않음
	private JButton btnAddFile;
	private JButton btnDeleteFile;
	
	//버튼들만 모아놓는 패널을 따로 설정
	private JPanel btnPanel;
	//디렉토리 파일들만 모아놓는 컨테이너를 따로 설정
	private JScrollPane directoryPane;
	//Tree구조 디렉터리를 만들기 위한 객체
	private JTree tree;
	//Tree구조에서 사용되는 모델 데이터
	private DefaultTreeModel model;
	//Tree구조에서 사용되는 노드
	private DefaultMutableTreeNode root;
	//Table로 파일 데이터들을 모아놓기 위한 컨테이너
	private JScrollPane scrollPane;
	//파일 정보를 표시하기 위한 테이블
	private JTable table;
	//테이블의 제목
	private String[] headings;
	//테이블의 데이터
	private String[][] data;
	//테이블에 세팅되는 값들
	private DefaultTableModel defaultTableModel;

	// View Class에서 생성자는 단순히 멤버 클래스들을 초기화하고 화면을 꾸미는 역할만 맡는다.
	// 이벤트 초기화는 컨트롤러에서 담당한다.
	public OpnZipFilePanel() {

		setVisible(false);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		setBounds(0, 0, 800, 600);
		setBackground(Color.GRAY);
		setLayout(null);
		setLayout(null);

		// BtnPanel just for design
		btnPanel = new JPanel();
		btnPanel.setBounds(0, 26, 800, 88);
		btnPanel.setBackground(Color.WHITE);
		add(btnPanel);
		btnPanel.setLayout(null);

		btnFileOpn = new JButton("");
		btnFileOpn.setBounds(0, 0, 73, 88);
		btnPanel.add(btnFileOpn);
		btnFileOpn.setIcon(new ImageIcon(getClass().getResource("/Btn_FileOpen.png")));

		btnDecompress = new JButton("");
		btnDecompress.setBounds(73, 0, 73, 88);
		btnPanel.add(btnDecompress);
		btnDecompress.setIcon(new ImageIcon(getClass().getResource("/Btn_FileSolve.png")));

		btnNewCompress = new JButton("");
		btnNewCompress.setBounds(144, 0, 73, 88);
		btnPanel.add(btnNewCompress);
		btnNewCompress.setIcon(new ImageIcon(getClass().getResource("/Btn_NewComp.png")));

		//btnAddFile = new JButton("");
		//btnAddFile.setBounds(219, 0, 73, 88);
		//btnPanel.add(btnAddFile);
		//btnAddFile.setIcon(new ImageIcon(getClass().getResource("/Btn_AddFile.png")));

		//btnDeleteFile = new JButton("");
		//btnDeleteFile.setBounds(293, 0, 73, 88);
		//btnPanel.add(btnDeleteFile);
		//btnDeleteFile.setIcon(new ImageIcon(getClass().getResource("/Btn_DeleteFile.png")));

		root = new DefaultMutableTreeNode("test");
		root.setUserObject("test2");
		// DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("실제 파일 이름");

		// root.add(child1);

		tree = new JTree(root);
		model = new DefaultTreeModel(root);
		tree.setModel(model);
		tree.setVisibleRowCount(10);

		// File's Explorer
		directoryPane = new JScrollPane(tree);
		directoryPane.setBounds(0, 117, 187, 483);
		directoryPane.setBackground(Color.WHITE);
		add(directoryPane);

		headings = new String[] { "파일명", "압축 크기", "원본 크기" };
		data = new String[][] { { "myFile.exe", "300byte", "557byte" } };

		defaultTableModel = new DefaultTableModel(data, headings);
		table = new JTable(defaultTableModel);

		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(199, 117, 601, 483);
		scrollPane.getViewport().setBackground(Color.WHITE);
		add(scrollPane);

	}

	public JButton getBtnFileOpn() {
		return btnFileOpn;
	}

	public void setBtnFileOpn(JButton btnFileOpn) {
		this.btnFileOpn = btnFileOpn;
	}

	public JButton getBtnDecompress() {
		return btnDecompress;
	}

	public void setBtnDecompress(JButton btnDecompress) {
		this.btnDecompress = btnDecompress;
	}

	public JButton getBtnNewCompress() {
		return btnNewCompress;
	}

	public void setBtnNewCompress(JButton btnNewCompress) {
		this.btnNewCompress = btnNewCompress;
	}

	public JButton getBtnAddFile() {
		return btnAddFile;
	}

	public void setBtnAddFile(JButton btnAddFile) {
		this.btnAddFile = btnAddFile;
	}

	public JButton getBtnDeleteFile() {
		return btnDeleteFile;
	}

	public void setBtnDeleteFile(JButton btnDeleteFile) {
		this.btnDeleteFile = btnDeleteFile;
	}

	public JPanel getBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(JPanel btnPanel) {
		this.btnPanel = btnPanel;
	}

	public JScrollPane getDirectoryPane() {
		return directoryPane;
	}

	public void setDirectoryPane(JScrollPane directoryPane) {
		this.directoryPane = directoryPane;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public DefaultTreeModel getModel() {
		return model;
	}

	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}

	public DefaultMutableTreeNode getRoot() {
		return root;
	}

	public void setRoot(DefaultMutableTreeNode root) {
		this.root = root;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public String[] getHeadings() {
		return headings;
	}

	public void setHeadings(String[] headings) {
		this.headings = headings;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public DefaultTableModel getDefaultTableModel() {
		return defaultTableModel;
	}

	public void setDefaultTableModel(DefaultTableModel defaultTableModel) {
		this.defaultTableModel = defaultTableModel;
	}

}