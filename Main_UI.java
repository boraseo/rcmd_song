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
	private ArrayList<modelTestMusic> user_playdatas; // 전체 리스트와 플레이 리스트 객체를 담고있어서 따로 생성 
	protected JScrollPane ply_list ,rcmd_list; // 플레이리스트 스크롤 , 추천 리스트 스크롤
	protected MyWinExit exit=new MyWinExit();
	boolean editMode; 
	
	modelTestMusic user;
	ArrayList <Integer> user_musicnum;
	
	Main_UI(modelTestMusic user) {
		this.user=user;
		user_musicnum=new ArrayList<Integer>();
		setTitle("음악추천 프로그램");
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
		private ImageIcon icon_main = new ImageIcon("src/images/메인배경.jpg");
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
			logout=new JButton("로그아웃");
			logout.setSize(100,20);
			logout.setLocation(300, 20);
			add(logout);
			// 구역2 시작//
			allList=new JTextArea("노래 결과창\n" + "MusicNum            장르             가수              노래제목\n");
			list = new JScrollPane(allList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			list.setLocation(10,200);
			list.setSize(400,330);
			add(list);
			
			save = new JButton("등록");
			save.setLocation(30,170);
			save.setSize(60,20);
			add(save);
			
			search = new JButton("조회");
			search.setLocation(120,170);
			search.setSize(60,20);
			add(search);
			
			delete = new JButton("삭제");
			delete.setLocation(210,170);
			delete.setSize(60,20);
			add(delete);
			
			recom = new JButton("추천");
			recom.setLocation(300,170);
			recom.setSize(60,20);
			add(recom);
			
			//구역2 끝
			
			// 구역3 시작//
			playList = new JTextArea("플레이 리스트 출력창\n " + "MusicNum            장르             가수              노래제목\n");
			ply_list = new JScrollPane(playList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			ply_list.setLocation(420,20);
			ply_list.setSize(450,200);
			add(ply_list);
			
			play = new JButton("재생");
			play.setLocation(780,225);
			play.setSize(80,20);
			add(play);
			
			stop=new JButton("중지");
			stop.setLocation(660,225);
			stop.setSize(80,20);
			add(stop);
			
			//구역3 끝
			// 구역4 시작//
			recommendList=new JTextArea("추천 리스트 출력창\n" + "MusicNum           장르     \t가수\t노래제목\t\n");
			rcmd_list = new JScrollPane(recommendList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			rcmd_list.setLocation(420,250);
			rcmd_list.setSize(450,280);
			add(rcmd_list);
			
			//구역4 끝
			
			
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
			
			tx_id = new JLabel(testP.getUser(user.getuser_name()).getuser_name()+" 님의 음악 페이지.");	// 혜연아 이거 컬르롤러에서 연결해줭
			tx_id.setLocation(85, 20);
			tx_id.setSize(200,20);
			add(tx_id);
			
			
			song_name = new JLabel("노래 제목");
			song_name.setLocation(20,80);
			song_name.setSize(100,20);
			add(song_name);
			
			textmusic_song = new JTextField("");
			textmusic_song.setLocation(85, 80);
			textmusic_song.setSize(200,20);
			add(textmusic_song);
			
			type_song = new JLabel("노래 장르");
			type_song.setLocation(20,110);
			type_song.setSize(100,20);
			add(type_song);
		
			textmusic_genre = new JTextField("");
			textmusic_genre.setLocation(85, 110);
			textmusic_genre.setSize(200,20);
			add(textmusic_genre);
			
			singer = new JLabel("가수");
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
		playList.append("플레이 리스트창\n"+ "MusicNum           장르     \t가수\t노래제목\t\n");
		user_musicnum.clear();
		user_musicnum.add(0,0);
		user_musicnum.addAll(testP.getuserPlaylist(user.getuser_prcode())); //user의 
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
	public void refreshData() { // 콤보박스 데이터 및 전체 목록 데이터를 그때그때 최신 데이터로 업데이트하는 메소드
		allList.setText(""); // 텍스트 상자 초기
		editMode=false;
		// 현재 상태가 데이터 조회 후 상태 인지, 아니면 새로운 데이터를 입력하기 위한 상태인지 설정하는 변수
		allList.append("노래 결과창\n" + "MusicNum           장르     \t가수\t노래제목\t\n");
		alldatas=testP.getall_music();
	
		if(alldatas != null) { // Arraylist의 전체 데이터를 형식에 맞춰 출력
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
			allList.append("전체 리스트에 등록된 노래가 없습니다. !!\n노래를 등록해 주세요 !!"); // getAll()에 값이 없을 때 발생
		} // 전체 리스트 추가시키기
	
		//이부분을 함수로 만들어서 입력될때마다 다시 호출되게 해야한다. 
		
	}
	public void clearField() {  // 텍스트상자를 비워주는 역할
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
