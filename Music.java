import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JTextArea;

import javazoom.jl.player.Player;


public class Music extends Thread{ // ���� ����� ������ ���·� ����
	private Player player; // javazoom���� �ܺ� ���̺귯���� build path�� �߰����Ѽ� ����� �� �ִ� jiPlayer ��ü
	private File file; // �ش� ������ �о 
	private FileInputStream fis; // inputStream�� �ۼ��Ѵ���
	private BufferedInputStream bis; // �������·� �о�� �����Ŵ
	private ArrayList<modelTestMusic> playlists; // �Ķ���ͷ� ������ playlist�� �����Ű�� ����
	private int i; // ���ϸ���Ʈ�� ��� ����
	
	public Music(ArrayList<modelTestMusic> playlists) {
		this.playlists=new ArrayList<modelTestMusic>();
		this.playlists=playlists; // �ش� playlist�� �Ķ���ͷ� ������ playlist�� set
		i=0; // ù ������ 0 ���� �ʱ�ȭ
		try {
			if(this.playlists.size()!=0) { // �÷��̸���Ʈ�� ����� 0���� �������� �ʰ� ������ ��.
			start(); // run
			}
		}catch(Exception e) {System.out.println(e.getMessage()); return;}
	}
	
	public void close() {
		this.stop(); // �뷡 ���� ��Ŵ
		this.interrupt(); 
	}
	
	@Override
	public void run() { // ���� ������ �����ų run �޼ҵ�
		try {
			while(true) {
				// player.play(); // ji(javajoom �ܺ� ���̺귯��) ��ü�� ���� �÷��̾� ����.		
				for(i=0;i<this.playlists.size();i++) { // ������ ��� playlists ��� �����Ű�� ���ؼ� arraylist �ݺ��� ���
					file=new File("src/music/"+playlists.get(i).getmusic_prcode()+".mp3");
					fis=new FileInputStream(file); // �� ���� ����� ����
					bis=new BufferedInputStream(fis); // �� ���� ����� ����
					player=new Player(bis); // �� ���� ����� ����
					player.play(); // ji(javajoom �ܺ� ���̺귯��) ��ü�� ���� �÷��̾� ����.		
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
