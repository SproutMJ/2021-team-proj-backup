package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainPanel extends JPanel {
	
	// 압축 해제를 위해 파일을 열때 사용되는 버튼
	private JButton buttonOpenFile;
	// 모든 파일을 압축하기 위해 사용되는 버튼
	private JButton buttonNewCompress;
	// 밑의 두 버튼은 버튼이나 라벨처럼 사용된다
	private JButton btnComLan;
	private JButton btnSolLan;

	// View Class에서 생성자는 단순히 멤버 클래스들을 초기화하고 화면을 꾸미는 역할만 맡는다.
	// 이벤트 초기화는 컨트롤러에서 담당한다.
	public MainPanel() {

			setVisible(true);
			setBorder(new EmptyBorder(5, 5, 5, 5));
			setLayout(null);
			
			setBounds(0, 0, 800, 600);
			setBackground(Color.BLACK);
			setLayout(null);
			
			buttonOpenFile = new JButton("");
			buttonOpenFile.setBackground(Color.BLACK);
			buttonOpenFile.setIcon(new ImageIcon(getClass().getResource("/buttonCompressed.jpg")));
			buttonOpenFile.setBounds(180, 220, 158, 120);
			add(buttonOpenFile);
			
			buttonNewCompress = new JButton("");
			buttonNewCompress.setBackground(Color.BLACK);
			buttonNewCompress.setIcon(new ImageIcon(getClass().getResource("/buttonSolve.jpg")));
			buttonNewCompress.setBounds(440, 220, 158, 119);
			add(buttonNewCompress);
			
			btnComLan = new JButton("압축 파일 열기");
			btnComLan.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			btnComLan.setForeground(Color.WHITE);
			btnComLan.setBackground(Color.BLACK);
			btnComLan.setBounds(200, 340, 116, 25);
			btnComLan.setBorderPainted(false);
			add(btnComLan);
			
			btnSolLan = new JButton("새로 압축하기");
			btnSolLan.setForeground(Color.WHITE);
			btnSolLan.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			btnSolLan.setBackground(Color.BLACK);
			btnSolLan.setBounds(462, 340, 116, 25);
			btnSolLan.setBorderPainted(false);
			add(btnSolLan);
			
		}
    public JButton getbuttonOpenFile() {
        return this.buttonOpenFile;
    }
    
    public JButton getbuttonNewCompress() {
        return this.buttonNewCompress;
    }
    
    public JButton getbtnComLan() {
        return this.btnComLan;
    }
    
    public JButton getbtnSolLan() {
    	return this.btnSolLan;
    }
    
   
}