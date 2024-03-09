package launcher;

import java.awt.EventQueue;

import javax.swing.JFrame;

import config.DependencyManager;
import view.MainFrame;

public class Main {

	//main함수는 이 클래스에서 실행한다. 화면 그리기가 중간에 blocking되지 않도록 invokeLater를 통해 가상머신에게 명령을 내려 화면을 그려내게 명령한다
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DependencyManager.managerStart();
            }
        });
    }
}