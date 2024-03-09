package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

public class ProgressBarPopup extends JFrame {

	// 팝업은 Frame으로 만들었으며 화면을 표시하기위해 panel을 하나 선언
	private JPanel contentPane;
	// Progressbar 처리를 위한 progressbar 객체 선언
	private JProgressBar progressBar;
	
	// View Class에서 생성자는 단순히 멤버 클래스들을 초기화하고 화면을 꾸미는 역할만 맡는다.
	// 이벤트 초기화는 컨트롤러에서 담당한다.
	public ProgressBarPopup() {
		setVisible(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(33, 51, 354, 62);
		progressBar.setStringPainted(true);
		contentPane.add(progressBar);
	}
	
	public JProgressBar getProgressBar() {
		return this.progressBar;
	}
	
}
