import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
public class Login extends JFrame{
	
	private JPanel lc;
	protected JLabel note;
	private JLabel login_la, pw_la;
	protected JTextField login_tx, pw_tx; 
	protected JButton login_bt, join_bt;
	protected MyWinExit exit =new MyWinExit();
	
	private String id ,pw;
	
	void setLogin_tx(JTextField text){ login_tx=text;}
	JTextField getLogin_tx() {return login_tx;}
	
	void setpw_tx(JTextField text ) {pw_tx=text;}
	JTextField getpw_tx(){return pw_tx;}
	
	void setnoteLabel(JLabel note) { this.note=note;}
	JLabel getnoteLabel() {return note;}
	
	
	public Login(){
		setTitle("로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		login_MyPanel lc=new login_MyPanel();
		add(lc);
	
	
		setSize(400,300);
		setVisible(true);	
		
	}
	class login_MyPanel extends JPanel{
		private ImageIcon icon = new ImageIcon("src/images/로그인화면.jpg");
		private Image img = icon.getImage();
		public login_MyPanel() {
			setLayout(null);
			note = new JLabel("[아이디와 비밀번호를 입력해주세요]");
			note.setSize(400, 130);
			note.setLocation(105,40);
			add(note);
			
			//defaultId=new modelTestMusic();
			
			// 로그인 라벨
			login_la = new JLabel("ID : ");
			login_la.setSize(30, 30);
			login_la.setLocation(70,120);
			add(login_la);
			
			//로그인 아이디 입력 창
			login_tx = new JTextField("");
			login_tx.setSize(200, 30);
			login_tx.setLocation(100,120);
			add(login_tx);
			
			
			pw_la = new JLabel("Password: ");
			pw_la.setSize(80, 30);
			pw_la.setLocation(30,160);
			add(pw_la);
			
			pw_tx=new JPasswordField("");
			pw_tx.setSize(200,30);
			pw_tx.setLocation(100, 160);
			add(pw_tx);
			
			// 로그인 버튼
			login_bt = new JButton("Login ");
			login_bt.setSize(100, 30);
			login_bt.setLocation(200,200);
			add(login_bt);
			
			join_bt=new JButton("Join ");
			join_bt.setSize(100,30);
			join_bt.setLocation(90,200);
			add(join_bt);
			
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
	
	public class MyWinExit extends WindowAdapter{
		public void windowClosing() {
			setVisible(false);
		}
		public void windowopen() {
			setVisible(true);
		}
	}
	public void addButtonActionLsitener(ActionListener listener) {
		login_bt.addActionListener(listener);
		join_bt.addActionListener(listener);
		
	}
	public void setReconnect() {
		login_tx.setText("");
		pw_tx.setText("");
	}
	
	
}
