import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import javax.swing.*;



public class Main_UI extends JFrame{
	
	private JScrollPane list; 
	protected JTextArea allList, playList, recommendList;
	protected JLabel status, code1, la_name, type_song, song_name, singer , id_name, tx_id;
	protected JButton logout, save, search, delete, play, recom, stop;
	protected JTextField textmusic_genre, textmusic_singerx, textmusic_prcode, textmusic_song;
	
	private modelTestDAO testP; 
	private ArrayList<modelTestMusic> alldatas;
	private ArrayList<modelTestMusic> user_playdatas; // ��ü ����Ʈ�� �÷��� ����Ʈ ��ü�� ����־ ���� ���� 
	protected JScrollPane ply_list ,rcmd_list; // �÷��̸���Ʈ ��ũ�� , ��õ ����Ʈ ��ũ��
	protected MyWinExit exit=new MyWinExit();
	boolean editMode; 
	
	modelTestMusic user;
	ArrayList <Integer> user_musicnum;
	
	Main_UI(modelTestMusic user) {
		this.user=user;
		user_musicnum=new ArrayList<Integer>();
		setTitle("������õ ���α׷�");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testP=new modelTestDAO();
		alldatas=new ArrayList<modelTestMusic>();
		user_playdatas=new ArrayList<modelTestMusic>();
		main_MyPanel c = new main_MyPanel();
		add(c);
		
		
		refreshData();
		setuserPlaylistText();
		
		setSize(900,600);
		setVisible(true);
		
	}
	class main_MyPanel extends JPanel{
		private ImageIcon icon_main = new ImageIcon("src/images/���ι��.jpg");
		private Image img_main = icon_main.getImage();
		main_MyPanel(){
			setResizable(false);
			setLayout(null);
			testP=new modelTestDAO();
			alldatas=new ArrayList<modelTestMusic>();
			user_playdatas=new ArrayList<modelTestMusic>();
			
		
			status=new JLabel();
			status.setBounds(20, 140, 400, 20);
			
			add(status);
			logout=new JButton("�α׾ƿ�");
			logout.setSize(100,20);
			logout.setLocation(300, 20);
			add(logout);
			// ����2 ����//
			allList=new JTextArea("�뷡 ���â\n" + "MusicNum            �帣             ����              �뷡����\n");
			list = new JScrollPane(allList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			list.setLocation(10,200);
			list.setSize(400,330);
			add(list);
			
			save = new JButton("���");
			save.setLocation(30,170);
			save.setSize(60,20);
			add(save);
			
			search = new JButton("��ȸ");
			search.setLocation(120,170);
			search.setSize(60,20);
			add(search);
			
			delete = new JButton("����");
			delete.setLocation(210,170);
			delete.setSize(60,20);
			add(delete);
			
			recom = new JButton("��õ");
			recom.setLocation(300,170);
			recom.setSize(60,20);
			add(recom);
			
			//����2 ��
			
			// ����3 ����//
			playList = new JTextArea("�÷��� ����Ʈ ���â\n " + "MusicNum            �帣             ����              �뷡����\n");
			ply_list = new JScrollPane(playList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			ply_list.setLocation(420,20);
			ply_list.setSize(450,200);
			add(ply_list);
			
			play = new JButton("���");
			play.setLocation(780,225);
			play.setSize(80,20);
			add(play);
			
			stop=new JButton("����");
			stop.setLocation(660,225);
			stop.setSize(80,20);
			add(stop);
			
			//����3 ��
			// ����4 ����//
			recommendList=new JTextArea("��õ ����Ʈ ���â\n" + "MusicNum           �帣     \t����\t�뷡����\t\n");
			rcmd_list = new JScrollPane(recommendList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			rcmd_list.setLocation(420,250);
			rcmd_list.setSize(450,280);
			add(rcmd_list);
			
			//����4 ��
			
			
			la_name = new JLabel("musicnum");
			la_name.setLocation(20,50);
			la_name.setSize(100,20);
			add(la_name);
			
			textmusic_prcode = new JTextField("");
			textmusic_prcode.setLocation(85, 50);
			textmusic_prcode.setSize(200,20);
			add(textmusic_prcode);
			
			id_name = new JLabel("ID");
			id_name.setLocation(20,20);
			id_name.setSize(100,20);
			add(id_name);
			
			tx_id = new JLabel(testP.getUser(user.getuser_name()).getuser_name()+" ���� ���� ������.");	// ������ �̰� �ø��ѷ����� �����آa
			tx_id.setLocation(85, 20);
			tx_id.setSize(200,20);
			add(tx_id);
			
			
			song_name = new JLabel("�뷡 ����");
			song_name.setLocation(20,80);
			song_name.setSize(100,20);
			add(song_name);
			
			textmusic_song = new JTextField("");
			textmusic_song.setLocation(85, 80);
			textmusic_song.setSize(200,20);
			add(textmusic_song);
			
			type_song = new JLabel("�뷡 �帣");
			type_song.setLocation(20,110);
			type_song.setSize(100,20);
			add(type_song);
		
			textmusic_genre = new JTextField("");
			textmusic_genre.setLocation(85, 110);
			textmusic_genre.setSize(200,20);
			add(textmusic_genre);
			
			singer = new JLabel("����");
			singer.setLocation(20,140);
			singer.setSize(100,20);
			add(singer);
		
			textmusic_singerx = new JTextField("");
			textmusic_singerx.setLocation(85, 140);
			textmusic_singerx.setSize(200,20);
			add(textmusic_singerx);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img_main, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
	public void setuserPlaylistText() {
		playList.setText("");
		playList.append("�÷��� ����Ʈâ\n"+ "MusicNum           �帣     \t����\t�뷡����\t\n");
		user_musicnum.clear();
		user_musicnum.add(0,0);
		user_musicnum.addAll(testP.getuserPlaylist(user.getuser_prcode())); //user�� 
		System.out.println((user.getuser_prcode()));
	
		for(int i=1;i<user_musicnum.size();i++) {
			StringBuffer sb2=new StringBuffer();
			sb2.append(user_musicnum.get(i) +"\t");
			sb2.append(testP.getMusic_prcode(user_musicnum.get(i)).getgenre() + "\t");
			sb2.append(testP.getMusic_prcode(user_musicnum.get(i)).getsinger() + "\t");
			sb2.append(testP.getMusic_prcode(user_musicnum.get(i)).getsong() + "\t\n");
			playList.append(sb2.toString());
		}
	}
	public void refreshData() { // �޺��ڽ� ������ �� ��ü ��� �����͸� �׶��׶� �ֽ� �����ͷ� ������Ʈ�ϴ� �޼ҵ�
		allList.setText(""); // �ؽ�Ʈ ���� �ʱ�
		editMode=false;
		// ���� ���°� ������ ��ȸ �� ���� ����, �ƴϸ� ���ο� �����͸� �Է��ϱ� ���� �������� �����ϴ� ����
		allList.append("�뷡 ���â\n" + "MusicNum           �帣     \t����\t�뷡����\t\n");
		alldatas=testP.getall_music();
	
		if(alldatas != null) { // Arraylist�� ��ü �����͸� ���Ŀ� ���� ���
			for(modelTestMusic p:alldatas) {
				StringBuffer sb=new StringBuffer();
				sb.append(p.getmusic_prcode() +"\t");
				sb.append(p.getgenre() + "\t");
				sb.append(p.getsinger() + "\t");
				sb.append(p.getsong() + "\t\n");
				allList.append(sb.toString());
			} 	
		}
		else {
			allList.append("��ü ����Ʈ�� ��ϵ� �뷡�� �����ϴ�. !!\n�뷡�� ����� �ּ��� !!"); // getAll()�� ���� ���� �� �߻�
		} // ��ü ����Ʈ �߰���Ű��
	
		//�̺κ��� �Լ��� ���� �Էµɶ����� �ٽ� ȣ��ǰ� �ؾ��Ѵ�. 
		
	}
	public void clearField() {  // �ؽ�Ʈ���ڸ� ����ִ� ����
		textmusic_prcode.setText("");
		textmusic_song.setText("");
		textmusic_genre.setText("");
		textmusic_singerx.setText("");
	}
	public void addButtonActionLsitener(ActionListener e) {
		 logout.addActionListener(e);
		 save.addActionListener(e);
		 search.addActionListener(e);
		 delete.addActionListener(e);
		 play.addActionListener(e);
		 recom.addActionListener(e);
		 stop.addActionListener(e);
	}
	public class MyWinExit extends WindowAdapter{
		public void windowClosing() {
			setVisible(false);
		}
	}
}
