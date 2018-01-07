import java.util.*;

import javax.swing.*;
import java.awt.*;

public class recommend {
	//����ڿ� �뷡 ���� �迭����� 
	// ����� �뷡 ���絵 �迭����� 	
	// ���߿� static �� ����� .
	// �뷡�� �� 500�� 
	class Genre{ //����ڰ� ���� ��� �뷡�帣�� ã�Ƴ��� ���� Ŭ�����̴�. 
		String genre=null;
		int cnt=0;
		void setGenre(String genre) { 
			this.genre=genre;
		}
	}
	class Vector{
		double num1,num2;
	}
	class Sim{ //���絵 Ŭ�����̴�. 
		// ���絵������ ������� ������ �ؾߵǼ� sim�� ��ü user_prcode�� ���� ���ֵ��� arrayslit�� �������. 
		double similarity;
		int user_prcode;
		void set(double similarity, int user_prcode) {
			this.similarity=similarity;
			this.user_prcode=user_prcode;
		}
	}
	private ArrayList <Genre> genre=new ArrayList<Genre>();
	private ArrayList<MyData> uBs =new ArrayList <MyData>(); // user *song 
	private ArrayList<Sim> similarity =  new ArrayList <Sim>();  //���絵 cos�� �ֱ� 
	private ArrayList<Integer> recmusic_prcode=new ArrayList<Integer>(); //��õ�뷡 music_prcode
	private ArrayList<Integer> muisc_prcode=new ArrayList<Integer>(); //����� �÷��̸���Ʈ�� �ִ� music_prcode
	private ArrayList<Sim> recomend=new ArrayList<Sim>();
	
	
	private Sim temp=new Sim();
	private int numUser;
	private modelTestDAO dao;
	private modelTestMusic music;
	private int user_prcode;
	private JTextArea recom;
	boolean cnt1=true;
	int top1=0, top2=0;
	String stop1=null, stop2=null;
	recommend(int user_prcode,JTextArea recom){ //������.user DAO, �� textarea�� �޴´�.  
		uBs.add(new MyData());
		
		dao=new modelTestDAO();	
		this.user_prcode=user_prcode;
		this.recom=recom;
		numUser= dao.getUserRowSize(); //user���� //user������ ���� �ʱ⶧���� 0������ �����Ѵٰ� ����. 
		System.out.println(numUser);
		setArraylist();		
		
		//���Լ� ���� ����. 
		recomend=getrecommend(user_prcode); //-> �迭�� ������� ����. 		
		recmusic_prcode.addAll(findmusic(user_prcode,recomend));
		int size=recmusic_prcode.size();
		if(recmusic_prcode.size()<11) { //��õ �뷡�� �� 10���� ���� ������ ����ȴ�. 
			int i1=0, i2=0;
			int i=0;
			findtop(); // ����ڰ� ���̵�� �带 ã�Ƴ��� �Լ��̴�. 1���� 2���� ã�Ƴ���. 
			
			while(i1<Math.ceil((10-size)/2)) { //10����õ�� ���� ��õ���� Ȧ���ϋ� 1������ �ø��Ͽ� 2�������� �ϳ� �� ���� ��õ�ȴ�. 
				if(i>dao.getMusic_prcode_genre(stop1).size()-1) break;
				if(serch(recmusic_prcode,dao.getMusic_prcode_genre(stop1).get(i))) {
					recmusic_prcode.add(dao.getMusic_prcode_genre(stop1).get(i)); //������ ���� �� ���� ��õ�ȴ�. 
					i1++;
				}
				i++;
			}
			i=0;
			while(i2<Math.floor((10-size)/2)) { //10����õ�� ���� ��õ���� Ȧ���ϋ� 1������ �ø��Ͽ� 2�������� �ϳ� �� ���� ��õ�ȴ�. 
				if(i>dao.getMusic_prcode_genre(stop2).size()-1) break;				
				if(serch(recmusic_prcode,dao.getMusic_prcode_genre(stop2).get(i))) {
					recmusic_prcode.add(dao.getMusic_prcode_genre(stop2).get(i)); //������ ���� �� ���� ��õ�ȴ�. 
					i2++;
				}
				i++;
			}
		}
		
		refreshrecomm(recmusic_prcode);
	}
	public void findtop() { //������� �÷��̸���Ʈ�� ���� ���� �帣 2�� ���� 
		muisc_prcode=dao.getuserPlaylist(user_prcode);
		for(int j=0;j<muisc_prcode.size();j++) {
			for(int i=0;i<genre.size();i++) {
				if(genre.get(i).genre.equals(dao.getMusic_prcode(muisc_prcode.get(j)).getgenre())) {
					genre.get(i).cnt++;
					cnt1=false;
				}
			}
			if(cnt1) {  //������ �帣�� ������ arraylist �߰��ؼ� �帣�� �߰����ش�. 
				genre.add(new Genre());
				genre.get(genre.size()-1).cnt=1;
				genre.get(genre.size()-1).genre=dao.getMusic_prcode(muisc_prcode.get(j)).getgenre();
			}
			cnt1=true;
		}
		for(int i=0;i<genre.size();i++) { //���� ��� �帣 2���� ã�Ƽ� �־��ش�. 
			if(genre.get(i).cnt>top1) {top1=genre.get(i).cnt; stop1=genre.get(i).genre;}
			else if(genre.get(i).cnt>top2 && genre.get(i).cnt<top1) {top2=genre.get(i).cnt; stop2=genre.get(i).genre;}
		}
	}
	public void refreshrecomm(ArrayList<Integer> music_prcode) {
		recom.setText(""); // �ؽ�Ʈ ���� �ʱ�
		
		// ���� ���°� ������ ��ȸ �� ���� ����, �ƴϸ� ���ο� �����͸� �Է��ϱ� ���� �������� �����ϴ� ����
		recom.append("��õ ����Ʈ ���â\n" + "MusicNum           �帣     \t����\t�뷡����\t\n");
		//recom.append("��ü ����Ʈ�� ��ϵ� �뷡�� �����ϴ�. !!\n�뷡�� ����� �ּ��� !!");  // getAll()�� ���� ���� �� �߻�
		for(int i=0;i<music_prcode.size();i++) {
			music=new modelTestMusic();
			music=dao.getPlayMusic(music_prcode.get(i));
			music.setmusic_prcode(music_prcode.get(i));
			if(music.getmusic_prcode()>0) {				
				StringBuffer sb=new StringBuffer();
				sb.append(music.getmusic_prcode() +"\t");
				sb.append(music.getgenre() + "\t");
				sb.append(music.getsinger() + "\t");
				sb.append(music.getsong() + "\t\n");
				recom.append(sb.toString());
			}
		}
		
	}
	ArrayList<Integer> findmusic(int user_prcode, ArrayList<Sim> recomend) { //���絵�� �̿��Ͽ� ��õ�޸� �뷡��  ���Ѵ�. 				
		ArrayList<Integer> recommend_musicNum= new ArrayList<Integer>();
		
		for(int i=2;i<numUser;i++) { // ���� ���絵�� ���� ����� ������ ��õ�Ѵ�.  
			for(int j=1;j<=50;j++) { //�� 50���� �뷡�̴�. 
				if(uBs.get(recomend.get(i).user_prcode).dataList.get(j)==(double)1 
						&& uBs.get(user_prcode).dataList.get(j)==(double)0 && serch(recommend_musicNum,j)) {
					//��õ���� ������� �뷡�� 0�̸� �����Ͽ� ���� ��õ���� ������� �迭�� 1�̸� serch �Լ� (recommend�� �̹� ���ԵǾ��ִ��� Ȯ���ϴ� �Լ�)�� ���ؼ� ��� true���̸� add�Ѵ�. 
					recommend_musicNum.add(j);
				}
			}
			if(recommend_musicNum.size()>11) break;
		}
		System.out.println(recommend_musicNum.size());
		return recommend_musicNum;
	}
	boolean serch( ArrayList<Integer> recommend_musicNum,int num) {
		// recommend�� ���ԵǾ��ִ��� Ȯ���ϴ� �Լ�. 
		boolean serch=true;
		for(int i=0;i<recommend_musicNum.size();i++) {//recommend�� ���ԵǾ�������� 
			if(recommend_musicNum.get(i)==num)
				serch=false;
		}
		for(int i=0;i<dao.getuserPlaylist(user_prcode).size();i++) { //������� �÷��̸���Ʈ�� �̹� �߰��Ǿ�������� 
			if(dao.getuserPlaylist(user_prcode).get(i)==num)
				serch=false;
		}
		return serch;
	}

	public ArrayList<Sim> getrecommend(int user_prcode) { 
		//���絵 �迭�� �̟G�Ͽ� ���絵�� ���� ������ �����ϴ� �Լ��̴�. 
		for(int i=1;i<similarity.size();i++) {
			for(int j=i+1;j<similarity.size(); j++) {
				if(similarity.get(i).similarity<similarity.get(j).similarity) {
					temp=new Sim();
					temp.set(similarity.get(j).similarity, similarity.get(j).user_prcode);
					similarity.set(j, similarity.get(i));
					similarity.set(i, temp);			
				}
			}
		}
		for(int i=1;i<similarity.size();i++)
			System.out.println(similarity.get(i).user_prcode+"  "+similarity.get(i).similarity);
		System.out.println();
		
		return similarity;
	}
	void setArraylist() {
		//uBs�� sim����� ���� �־��ִ� �Լ��̴�. 
		numUser= dao.getUserRowSize(); //user���� //user������ ���� �ʱ⶧���� 0������ �����Ѵٰ� ����. //0���� �ʱ�ȭ 
		for(int i=1;i<=numUser;i++) { 
			uBs.add(new MyData());
			uBs.get(i).dataList.add(0, 0.0);
			for(int j=1;j<=50;j++) 
				uBs.get(i).dataList.add(j, 0.0); //[�����][�뷡]
		}
		
		
		for(int i=1;i<=numUser; i++) // ����� * �뷡 �迭�� ����� �÷��̸�Ʈ�� �ִ� �뷡�� 1�� �־��ش�. 
			for(int j=1;j<=dao.getuserPlaylist(i).size();j++) {
				uBs.get(i).dataList.set(dao.getuserPlaylist(i).get(j-1), 1.0);
			}
		
		temp.similarity=0.0; temp.user_prcode=0;
		similarity.add(temp);
		for(int i=1;i<=numUser;i++) { // �� ����� �麰�� ���絵�� ����Ͽ� ���� �־��ش�. 
			temp=new Sim();
			temp.similarity=getCosTheta(user_prcode,i); temp.user_prcode=i;
			similarity.add(temp);
			System.out.println(similarity.get(i).user_prcode+"  "+similarity.get(i).similarity);
		}System.out.println();
		
	}
	
	double getCosTheta(int num1,int num2) { // ���絵 ����� ���� �ڻ��� ������ ���ϴ� �Լ��̴�. 
		double cos=0;
		double vectorsize=vectorSize(num1,num2);
		if(vectorsize==0) cos=0;
		else cos=vectorProduct(num1,num2)/vectorsize;
		
		return cos;
	}
	double vectorProduct(int num1, int num2) // ���� �� ��
	{
		double vectorsize=0;
		for(int i=1;i<=numUser; i++) {
			vectorsize+=uBs.get(num1).dataList.get(i)*uBs.get(num2).dataList.get(i);
		}
		return vectorsize;
	}
	double vectorSize(int num1,int num2) //���� ������ϱ� 
	{
		Vector vec =new Vector();
		vec.num1=0; vec.num2=0;		
		double vectorsize=0;
		
		for(int i=1;i<=numUser;i++) {
			vec.num1+=uBs.get(num1).dataList.get(i)*uBs.get(num1).dataList.get(i);
			vec.num2+=uBs.get(num2).dataList.get(i)*uBs.get(num2).dataList.get(i);		
		}
		vec.num1=Math.sqrt(vec.num1);
		vec.num2=Math.sqrt(vec.num2);
		vectorsize =vec.num1*vec.num2;
	
		return vectorsize;
	}

}