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
	
	private boolean status; //  ���� ��������� ���� ��(ó�� Ŭ���� ���, �ι�° ����, ����° ���, �׹�° ����......)
	Music playMusic; // ���� ����� ���� �ڹ� Ŭ����
	private ArrayList<modelTestMusic> playlists;
	
	public AppManager(Login login) { //������ 
		this.login_ui=login;
		user=new modelTestMusic();
		dao=new modelTestDAO();
		
		status=true; // ó�� ���� �����ų ���� ���� ��������� ���� ���� true�� ����(ó�� Ŭ���� ���, �ι�° ����, ����° ���, �׹�° ����......)
	}
	public void appMain() {	// �α�â ��Ʈ��
		login_ui.addButtonActionLsitener( new ActionListener() { // UI�� ��ư �̺�Ʈ
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				modelTestMusic temp;
				
				Object obj=e.getSource();	// Ŭ���� ��ư�� ������ obj�� �޾ƿ�
				// �α��� â���� �α����Ϸ��� ������� ���̵�� ��й�ȣ�� �����´�. set
				user.setpw(login_ui.getpw_tx().getText());
				user.setuser_name(login_ui.getLogin_tx().getText());
				// temp�� ���̵� ����
				temp=dao.getUser(user.getuser_name());
				
				System.out.println(temp.getuser_name()+"   "+temp.getpw());
				System.out.println(user.getuser_name()+ "   "+user.getpw());
				
				if(obj==login_ui.login_bt) { //�α��� ��ư ������ �� 
					if (login_ui.getLogin_tx().getText().equals((String)"")	// �α��� â�� ��ĭ�϶� 
							|| login_ui.getpw_tx().getText().equals((String)"")) { // ��й�ȣ ĭ�� ��ĭ�϶�
						login_ui.getnoteLabel().setText("���̵� Ȥ�� ��й�ȣ�� �ٽ� �Է����ּ��� "); // ���޼��� ���
						login_ui.setReconnect();  // �ٽ� �ʱ� �α���â���� ��ȯ
					}
					else if(temp.getpw()!=null &&
							temp.getpw().equals(user.getpw()) && 
							temp.getuser_name().equals(user.getuser_name())) { // ȸ�����Ե� ���̵� �Է�������
						login_ui.getnoteLabel().setText("�α��� �Ǿ����ϴ�."); // ���������� �α��Ή�ٴ� �޽��� ���
						//�Էµ� ���̵� �ʿ��ϸ� user�� ����ϸ� �ȴ�. 
						login_ui.exit.windowClosing();  // �α����� ���������� �Ǹ� �α���â�� ����.
						user.setuser_prcode(dao.getUser(user.getuser_name()).getuser_prcode());
						main_ui=new Main_UI(user);  // �α��ε� ������� ����â�� ����ش�.
						appMain2();
					}
					else {
						login_ui.getnoteLabel().setText("���̵� Ȥ�� ����� ��ġ���� �ʽ��ϴ�."); // ���̵𿡵��� ��й�ȣ�� ��ġ���� ������� ���â
						login_ui.setReconnect(); // �ٽ� �ʱ� �α���â���� ��ȯ
					}
				}
				else if(obj==login_ui.join_bt) {
					if(temp.getuser_name()!=null) {
						login_ui.getnoteLabel().setText("�����ϴ� ���̵��Դϴ�."); // ���� DB�� �����ϴ� ���̵��ΰ�� ���޼���
						login_ui.setReconnect();  // �ٽ� �α��� ȭ������ ���ƿ�
						}
					else {
						dao.newUser(user);
						login_ui.getnoteLabel().setText("���̵� �����Ǿ����ϴ�. �α��� ���ּ���."); // ȸ�������� ���������� �Ǿ��ٴ� ������ ���
						login_ui.setReconnect();
					}

				}
			}
		});
	}
	public void appMain2() {  // ���� ����� ���õ� ����� ��Ʈ���ϴ� �κ�
		main_ui.addButtonActionLsitener(new ActionListener() { // ����â�� ��ư �̺�Ʈ
			public void actionPerformed(ActionEvent e) {
				Object obj=e.getSource();  // Ŭ���� ��ư ������ obj�� �޴´�.
				ArrayList <modelTestMusic> temp; 
				modelTestMusic temp1;
				if(obj==main_ui.logout) { // �α׾ƿ� ��ư�� ������ �� �α׾ƿ� ����
					playMusic.close(); // ���� ����
					status=true; // ���� ���´� true�� ��ȯ(true�� ���� ����)
					 if(status==false){playMusic.close();}
					main_ui.exit.windowClosing();  // �α׾ƿ� �� ������� ����â�� �ݴ´�.
					login_ui.exit.windowopen();  // �ٽ� �α��� â�� ����ش�.
					login_ui.setReconnect();
				}
				else if(obj==main_ui.save) { // ������ ������ ���
					//�� �÷��̸���Ʈ�� ����.
					temp=new ArrayList<modelTestMusic>();
					temp1=new modelTestMusic();
					
					if(!main_ui.textmusic_prcode.getText().equals((String)""))  // �뷡 prcode�� modelTestMusic�� ����
						temp.add(dao.getMusic_prcode(Integer.parseInt(main_ui.textmusic_prcode.getText())));		
					if(!main_ui.textmusic_genre.getText().equals((String)"")) // �뷡 �帣�� modelTestMusic�� ����
						temp.add(dao.getMusic_genre(main_ui.textmusic_genre.getText()));
					if(!main_ui.textmusic_singerx.getText().equals((String)"")) // �뷡�� ������ modelTestMusic�� ����
						temp.add(dao.getMusic_singer(main_ui.textmusic_singerx.getText()));
					if(!main_ui.textmusic_song.getText().equals((String)"")) // �뷡 ������ modelTestMusic�� ����
						temp.add(dao.getMusic_song(main_ui.textmusic_song.getText()));
					// modelTestMusic ī��Ʈ�� �ʱ�ȭ
					int cnt=0; 
					int cnt1=0; 

					for(int i=0;i<temp.size();i++) { // modelTestMusic�� ����� �뷡�� ����(ũ��)�� �޴´�.
						if(temp.get(0).getmusic_prcode()==temp.get(i).getmusic_prcode())
							System.out.println("i="+i+temp.get(0).getmusic_prcode()+"  "+ temp.get(i).getmusic_prcode());
							cnt++;
					}
					temp.get(0).setuser_prcode(user.getuser_prcode());
					for(int i=0;i<dao.getuserPlaylist(user.getuser_prcode()).size(); i++) { // dao�� ���� ������ ���̽��� �뷡�� ����Ǹ� �� ����(ũ��)�� �޴´�.
						if(dao.getuserPlaylist(user.getuser_prcode()).get(i) ==temp.get(0).getmusic_prcode())
							cnt1++;
					}
					
					if(cnt<2 || temp.size()<4 || cnt1>0 )   main_ui.clearField();
					else {
						dao.newUserPlaylist(temp.get(0));
						main_ui.setuserPlaylistText();
					}
					
				}
				else if(obj==main_ui.search) {  // �뷡�� search��ư�� ������ ���
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
				else if(obj==main_ui.delete) { //���� ��ư�� ������ ���
					//�� �÷��̸���Ʈ �׸� ����. 
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
					// count ���ʱ�ȭ
					int cnt=0;
					int cnt1=0;
					// modelTestMusic�� �뷡 ����(ũ��)�� �޴´�.
					for(int i=0;i<temp.size();i++) {
						if(temp.get(0).getmusic_prcode()==temp.get(i).getmusic_prcode())
							System.out.println(temp.get(0).getmusic_prcode()+"  "+ temp.get(i).getmusic_prcode());
							cnt++;
					}
					//user�� prcode�� �޾ƿ�
					temp.get(0).setuser_prcode(user.getuser_prcode());
					System.out.println(user.getuser_prcode());
					temp2.addAll(dao.getuserPlaylist_playlistprcode(user.getuser_prcode()));
					// ���� �÷��� ����Ʈ�� �뷡 ������ �����.
					for(int i=0;i<temp2.size();i++) {
						System.out.println(temp2.get(i).getmusic_prcode()+"   "+ temp.get(0).getmusic_prcode());
						if(temp2.get(i).getmusic_prcode()==temp.get(0).getmusic_prcode())
							temp.get(0).setuserplaylist_prcode(temp2.get(i).getuserplaylist_prcode());
					}
					
					if(cnt<2 || temp.size()<4 || temp.get(0).getuserplaylist_prcode()==0)   main_ui.clearField(); 
					// ����â�� �÷��� ����Ʈ�� �ʱ�ȭ�� �÷��� ����Ʈ�� �ٽ� �����ش�.
					else {
						System.out.println(temp.get(0).getuserplaylist_prcode());
						dao.delUserPlaylist(temp.get(0));
						main_ui.setuserPlaylistText();
					}
				}
				else if(obj==main_ui.play) {
					if(status==true) { // ó�� Ŭ������ ���� true�̹Ƿ� ������ �����
					playlists=dao.getplay_music(dao.getUser(login_ui.login_tx.getText())); // ������ ������ �÷��̸���Ʈ�� �ִ� musicnum(�����̸�)�� �� ��Ƽ� arraylist�� ������.
					playMusic=new Music(playlists); // Music Ŭ������ playlists�� ��ü �ݺ� ��� ���°� true�� �����ڷ� �����ؼ� Music Ŭ������ ������Ŵ
					status=false; // ���� ���´� false�� ��ȯ ������ Ŭ���� ���� ������ ����(true�� ���� ����)
					System.out.println("play");}
				}
				else if(obj==main_ui.stop) {
					if(status==false) { // ���� ���� false �� ��
						playMusic.close(); // ������ ���� ����
						status=true; // ���� ���� false�� ��ȯ
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
