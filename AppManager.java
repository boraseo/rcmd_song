import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AppManager {
	
	private Login login_ui;
	private Main_UI main_ui;
	private modelTestMusic user;
	private modelTestDAO dao;
	
	private recommend recom;
	
	private boolean status; //  음악 재생가능한 상태 값(처음 클릭시 재생, 두번째 중지, 세번째 재생, 네번째 중지......)
	Music playMusic; // 음악 재생을 위한 자바 클래스
	private ArrayList<modelTestMusic> playlists;
	
	public AppManager(Login login) { //생성자 
		this.login_ui=login;
		user=new modelTestMusic();
		dao=new modelTestDAO();
		
		status=true; // 처음 앱을 실행시킬 때는 음악 재생가능한 상태 값을 true로 세팅(처음 클릭시 재생, 두번째 중지, 세번째 재생, 네번째 중지......)
	}
	public void appMain() {	// 로그창 컨트롤
		login_ui.addButtonActionLsitener( new ActionListener() { // UI의 버튼 이벤트
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				modelTestMusic temp;
				
				Object obj=e.getSource();	// 클릭된 버튼의 정보를 obj로 받아옴
				// 로그인 창에서 로그인하려는 사용자의 아이디와 비밀번호를 가져온다. set
				user.setpw(login_ui.getpw_tx().getText());
				user.setuser_name(login_ui.getLogin_tx().getText());
				// temp에 아이디 저장
				temp=dao.getUser(user.getuser_name());
				
				System.out.println(temp.getuser_name()+"   "+temp.getpw());
				System.out.println(user.getuser_name()+ "   "+user.getpw());
				
				if(obj==login_ui.login_bt) { //로그인 버튼 눌렸을 때 
					if (login_ui.getLogin_tx().getText().equals((String)"")	// 로그인 창이 빈칸일때 
							|| login_ui.getpw_tx().getText().equals((String)"")) { // 비밀번호 칸이 빈칸일때
						login_ui.getnoteLabel().setText("아이디 혹은 비밀번호를 다시 입력해주세요 "); // 경고메세지 띄움
						login_ui.setReconnect();  // 다시 초기 로그인창으로 전환
					}
					else if(temp.getpw()!=null &&
							temp.getpw().equals(user.getpw()) && 
							temp.getuser_name().equals(user.getuser_name())) { // 회원가입된 아이디를 입력했을때
						login_ui.getnoteLabel().setText("로그인 되었습니다."); // 정상적으로 로그인됬다는 메시지 띄움
						//입력된 아이디가 필요하면 user를 사용하면 된다. 
						login_ui.exit.windowClosing();  // 로그인이 성공적으로 되면 로그인창을 끈다.
						user.setuser_prcode(dao.getUser(user.getuser_name()).getuser_prcode());
						main_ui=new Main_UI(user);  // 로그인된 사용자의 메인창을 띄워준다.
						appMain2();
					}
					else {
						login_ui.getnoteLabel().setText("아이디 혹인 비번이 일치하지 않습니다."); // 아이디에따른 비밀번호가 일치하지 않을경우 경고창
						login_ui.setReconnect(); // 다시 초기 로그인창으로 전환
					}
				}
				else if(obj==login_ui.join_bt) {
					if(temp.getuser_name()!=null) {
						login_ui.getnoteLabel().setText("존재하는 아이디입니다."); // 현재 DB에 존재하는 아이디인경우 경고메세지
						login_ui.setReconnect();  // 다시 로그인 화며으로 돌아옴
						}
					else {
						dao.newUser(user);
						login_ui.getnoteLabel().setText("아이디가 생성되었습니다. 로그인 해주세요."); // 회원가입이 정상적으로 되었다는 메제지 띄움
						login_ui.setReconnect();
					}

				}
			}
		});
	}
	public void appMain2() {  // 음악 재생에 관련된 명령을 컨트롤하는 부분
		main_ui.addButtonActionLsitener(new ActionListener() { // 메인창의 버튼 이벤트
			public void actionPerformed(ActionEvent e) {
				Object obj=e.getSource();  // 클릭된 버튼 정보를 obj로 받는다.
				ArrayList <modelTestMusic> temp; 
				modelTestMusic temp1;
				if(obj==main_ui.logout) { // 로그아웃 버튼을 눌렀을 때 로그아웃 실행
					playMusic.close(); // 음악 종료
					status=true; // 음악 상태는 true로 전환(true일 때만 실행)
					 if(status==false){playMusic.close();}
					main_ui.exit.windowClosing();  // 로그아웃 한 사용자의 메인창을 닫는다.
					login_ui.exit.windowopen();  // 다시 로그인 창을 띄워준다.
					login_ui.setReconnect();
				}
				else if(obj==main_ui.save) { // 저장을 눌렀을 경우
					//내 플레이리스트에 저장.
					temp=new ArrayList<modelTestMusic>();
					temp1=new modelTestMusic();
					
					if(!main_ui.textmusic_prcode.getText().equals((String)""))  // 노래 prcode를 modelTestMusic에 저장
						temp.add(dao.getMusic_prcode(Integer.parseInt(main_ui.textmusic_prcode.getText())));		
					if(!main_ui.textmusic_genre.getText().equals((String)"")) // 노래 장르를 modelTestMusic에 저장
						temp.add(dao.getMusic_genre(main_ui.textmusic_genre.getText()));
					if(!main_ui.textmusic_singerx.getText().equals((String)"")) // 노래의 가수를 modelTestMusic에 저장
						temp.add(dao.getMusic_singer(main_ui.textmusic_singerx.getText()));
					if(!main_ui.textmusic_song.getText().equals((String)"")) // 노래 제목을 modelTestMusic에 저장
						temp.add(dao.getMusic_song(main_ui.textmusic_song.getText()));
					// modelTestMusic 카운트값 초기화
					int cnt=0; 
					int cnt1=0; 

					for(int i=0;i<temp.size();i++) { // modelTestMusic에 저장된 노래의 개수(크기)를 받는다.
						if(temp.get(0).getmusic_prcode()==temp.get(i).getmusic_prcode())
							System.out.println("i="+i+temp.get(0).getmusic_prcode()+"  "+ temp.get(i).getmusic_prcode());
							cnt++;
					}
					temp.get(0).setuser_prcode(user.getuser_prcode());
					for(int i=0;i<dao.getuserPlaylist(user.getuser_prcode()).size(); i++) { // dao를 통해 베이터 베이스에 노래가 저장되면 그 개수(크기)를 받는다.
						if(dao.getuserPlaylist(user.getuser_prcode()).get(i) ==temp.get(0).getmusic_prcode())
							cnt1++;
					}
					
					if(cnt<2 || temp.size()<4 || cnt1>0 )   main_ui.clearField();
					else {
						dao.newUserPlaylist(temp.get(0));
						main_ui.setuserPlaylistText();
					}
					
				}
				else if(obj==main_ui.search) {  // 노래를 search버튼을 눌렀을 경우
					temp1=new modelTestMusic();
					
					if(!main_ui.textmusic_prcode.getText().equals((String)"")) {
						temp1=dao.getMusic_prcode(Integer.parseInt(main_ui.textmusic_prcode.getText()));
					}
					else if(!main_ui.textmusic_genre.getText().equals((String)"")) {
						temp1=dao.getMusic_genre(main_ui.textmusic_genre.getText());
					}
					else if(!main_ui.textmusic_singerx.getText().equals((String)"")) {
						temp1=dao.getMusic_singer(main_ui.textmusic_singerx.getText());
					}
					else if(!main_ui.textmusic_song.getText().equals((String)"")) {
						temp1=dao.getMusic_song(main_ui.textmusic_song.getText());
					}
					main_ui.textmusic_prcode.setText(Integer.toString(temp1.getmusic_prcode()));
					main_ui.textmusic_genre.setText(temp1.getgenre());
					main_ui.textmusic_singerx.setText(temp1.getsinger());
					main_ui.textmusic_song.setText(temp1.getsong());
					
				}
				else if(obj==main_ui.delete) { //삭제 버튼을 눌렀을 경우
					//내 플레이리스트 항목 삭제. 
					temp=new ArrayList<modelTestMusic>();
					ArrayList<modelTestMusic> temp2=new ArrayList<modelTestMusic>();
					if(!main_ui.textmusic_prcode.getText().equals((String)"")) 
						temp.add(dao.getMusic_prcode(Integer.parseInt(main_ui.textmusic_prcode.getText())));
					if(!main_ui.textmusic_genre.getText().equals((String)"")) 
						temp.add(dao.getMusic_genre(main_ui.textmusic_genre.getText()));
					if(!main_ui.textmusic_singerx.getText().equals((String)"")) 
						temp.add(dao.getMusic_singer(main_ui.textmusic_singerx.getText()));
					if(!main_ui.textmusic_song.getText().equals((String)"")) 
						temp.add(dao.getMusic_song(main_ui.textmusic_song.getText()));
					// count 값초기화
					int cnt=0;
					int cnt1=0;
					// modelTestMusic의 노래 개수(크기)를 받는다.
					for(int i=0;i<temp.size();i++) {
						if(temp.get(0).getmusic_prcode()==temp.get(i).getmusic_prcode())
							System.out.println(temp.get(0).getmusic_prcode()+"  "+ temp.get(i).getmusic_prcode());
							cnt++;
					}
					//user의 prcode를 받아옴
					temp.get(0).setuser_prcode(user.getuser_prcode());
					System.out.println(user.getuser_prcode());
					temp2.addAll(dao.getuserPlaylist_playlistprcode(user.getuser_prcode()));
					// 유저 플레이 리스트의 노래 정보를 지운다.
					for(int i=0;i<temp2.size();i++) {
						System.out.println(temp2.get(i).getmusic_prcode()+"   "+ temp.get(0).getmusic_prcode());
						if(temp2.get(i).getmusic_prcode()==temp.get(0).getmusic_prcode())
							temp.get(0).setuserplaylist_prcode(temp2.get(i).getuserplaylist_prcode());
					}
					
					if(cnt<2 || temp.size()<4 || temp.get(0).getuserplaylist_prcode()==0)   main_ui.clearField(); 
					// 메인창의 플레이 리스트를 초기화된 플레이 리스트로 다시 보여준다.
					else {
						System.out.println(temp.get(0).getuserplaylist_prcode());
						dao.delUserPlaylist(temp.get(0));
						main_ui.setuserPlaylistText();
					}
				}
				else if(obj==main_ui.play) {
					if(status==true) { // 처음 클릭했을 때는 true이므로 음악을 재생함
					playlists=dao.getplay_music(dao.getUser(login_ui.login_tx.getText())); // 각각의 유저의 플레이리스트에 있는 musicnum(파일이름)을 다 모아서 arraylist로 가져옴.
					playMusic=new Music(playlists); // Music 클래스에 playlists와 전체 반복 재생 상태값 true를 생성자로 생성해서 Music 클래스를 구동시킴
					status=false; // 음악 상태는 false로 전환 다음에 클릭할 때는 종료할 거임(true일 때만 실행)
					System.out.println("play");}
				}
				else if(obj==main_ui.stop) {
					if(status==false) { // 음악 상태 false 일 때
						playMusic.close(); // 스레드 강제 종료
						status=true; // 음악 상태 false로 전환
						System.out.println("stop");
					}
				}
				else if(obj==main_ui.recom) {
					System.out.println("recom");
					recom=new recommend(user.getuser_prcode(),main_ui.recommendList); 
				}
				
			}
		});
	}
	
	
	public static void main(String[] args) {
		AppManager app=new AppManager(new Login());
		app.appMain();
	}

}
