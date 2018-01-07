import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JTextArea;

import javazoom.jl.player.Player;


public class Music extends Thread{ // 음악 재생은 쓰레드 형태로 구현
	private Player player; // javazoom에서 외부 라이브러리를 build path에 추가시켜서 사용할 수 있는 jiPlayer 객체
	private File file; // 해당 파일을 읽어서 
	private FileInputStream fis; // inputStream에 작성한다음
	private BufferedInputStream bis; // 버퍼형태로 읽어내서 재생시킴
	private ArrayList<modelTestMusic> playlists; // 파라미터로 가져온 playlist를 적용시키기 위함
	private int i; // 파일리스트의 목록 순서
	
	public Music(ArrayList<modelTestMusic> playlists) {
		this.playlists=new ArrayList<modelTestMusic>();
		this.playlists=playlists; // 해당 playlist를 파라미터로 가져온 playlist로 set
		i=0; // 첫 순서는 0 으로 초기화
		try {
			if(this.playlists.size()!=0) { // 플레이리스트에 목록이 0개면 실행하지 않고 무시할 것.
			start(); // run
			}
		}catch(Exception e) {System.out.println(e.getMessage()); return;}
	}
	
	public void close() {
		this.stop(); // 노래 중지 시킴
		this.interrupt(); 
	}
	
	@Override
	public void run() { // 실제 음악을 재생시킬 run 메소드
		try {
			while(true) {
				// player.play(); // ji(javajoom 외부 라이브러리) 객체로 음악 플레이어 실행.		
				for(i=0;i<this.playlists.size();i++) { // 가져온 모든 playlists 모두 재생시키기 위해서 arraylist 반복문 사용
					file=new File("src/music/"+playlists.get(i).getmusic_prcode()+".mp3");
					fis=new FileInputStream(file); // 위 변수 설명과 같음
					bis=new BufferedInputStream(fis); // 위 변수 설명과 같음
					player=new Player(bis); // 위 변수 설명과 같음
					player.play(); // ji(javajoom 외부 라이브러리) 객체로 음악 플레이어 실행.		
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
