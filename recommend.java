import java.util.*;

import javax.swing.*;
import java.awt.*;

public class recommend {
	//사용자와 노래 관련 배열만들기 
	// 사용자 노래 유사도 배열만들기 	
	// 나중에 static 다 지우기 .
	// 노래는 총 500곡 
	class Genre{ //사용자가 많이 듣는 노래장르를 찾아내기 위한 클래스이다. 
		String genre=null;
		int cnt=0;
		void setGenre(String genre) { 
			this.genre=genre;
		}
	}
	class Vector{
		double num1,num2;
	}
	class Sim{ //유사도 클래스이다. 
		// 유사도가높은 순서대로 정렬을 해야되서 sim이 자체 user_prcode를 가질 수있도록 arrayslit로 만들었다. 
		double similarity;
		int user_prcode;
		void set(double similarity, int user_prcode) {
			this.similarity=similarity;
			this.user_prcode=user_prcode;
		}
	}
	private ArrayList <Genre> genre=new ArrayList<Genre>();
	private ArrayList<MyData> uBs =new ArrayList <MyData>(); // user *song 
	private ArrayList<Sim> similarity =  new ArrayList <Sim>();  //유사도 cos값 넣기 
	private ArrayList<Integer> recmusic_prcode=new ArrayList<Integer>(); //추천노래 music_prcode
	private ArrayList<Integer> muisc_prcode=new ArrayList<Integer>(); //사용자 플레이리스트에 있는 music_prcode
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
	recommend(int user_prcode,JTextArea recom){ //생성자.user DAO, 와 textarea를 받는다.  
		uBs.add(new MyData());
		
		dao=new modelTestDAO();	
		this.user_prcode=user_prcode;
		this.recom=recom;
		numUser= dao.getUserRowSize(); //user숫자 //user삭제는 되지 않기때문에 0번부터 시작한다고 가정. 
		System.out.println(numUser);
		setArraylist();		
		
		//두함수 변형 하자. 
		recomend=getrecommend(user_prcode); //-> 배열로 순서대로 받음. 		
		recmusic_prcode.addAll(findmusic(user_prcode,recomend));
		int size=recmusic_prcode.size();
		if(recmusic_prcode.size()<11) { //추천 노래가 총 10개가 되지 않을때 실행된다. 
			int i1=0, i2=0;
			int i=0;
			findtop(); // 사용자가 많이듣는 장를 찾아내는 함수이다. 1위와 2위를 찾아낸다. 
			
			while(i1<Math.ceil((10-size)/2)) { //10곡추천중 남은 추천곡이 홀수일떄 1순위는 올림하여 2순위보다 하나 더 많게 추천된다. 
				if(i>dao.getMusic_prcode_genre(stop1).size()-1) break;
				if(serch(recmusic_prcode,dao.getMusic_prcode_genre(stop1).get(i))) {
					recmusic_prcode.add(dao.getMusic_prcode_genre(stop1).get(i)); //순위가 높은 곡 부터 추천된다. 
					i1++;
				}
				i++;
			}
			i=0;
			while(i2<Math.floor((10-size)/2)) { //10곡추천중 남은 추천곡이 홀수일떄 1순위는 올림하여 2순위보다 하나 더 많게 추천된다. 
				if(i>dao.getMusic_prcode_genre(stop2).size()-1) break;				
				if(serch(recmusic_prcode,dao.getMusic_prcode_genre(stop2).get(i))) {
					recmusic_prcode.add(dao.getMusic_prcode_genre(stop2).get(i)); //순위가 높은 곡 부터 추천된다. 
					i2++;
				}
				i++;
			}
		}
		
		refreshrecomm(recmusic_prcode);
	}
	public void findtop() { //사용자의 플레이리스트중 가장 높은 장르 2개 선택 
		muisc_prcode=dao.getuserPlaylist(user_prcode);
		for(int j=0;j<muisc_prcode.size();j++) {
			for(int i=0;i<genre.size();i++) {
				if(genre.get(i).genre.equals(dao.getMusic_prcode(muisc_prcode.get(j)).getgenre())) {
					genre.get(i).cnt++;
					cnt1=false;
				}
			}
			if(cnt1) {  //동일한 장르가 없으면 arraylist 추가해서 장르를 추가해준다. 
				genre.add(new Genre());
				genre.get(genre.size()-1).cnt=1;
				genre.get(genre.size()-1).genre=dao.getMusic_prcode(muisc_prcode.get(j)).getgenre();
			}
			cnt1=true;
		}
		for(int i=0;i<genre.size();i++) { //많이 듣는 장르 2개를 찾아서 넣어준다. 
			if(genre.get(i).cnt>top1) {top1=genre.get(i).cnt; stop1=genre.get(i).genre;}
			else if(genre.get(i).cnt>top2 && genre.get(i).cnt<top1) {top2=genre.get(i).cnt; stop2=genre.get(i).genre;}
		}
	}
	public void refreshrecomm(ArrayList<Integer> music_prcode) {
		recom.setText(""); // 텍스트 상자 초기
		
		// 현재 상태가 데이터 조회 후 상태 인지, 아니면 새로운 데이터를 입력하기 위한 상태인지 설정하는 변수
		recom.append("추천 리스트 출력창\n" + "MusicNum           장르     \t가수\t노래제목\t\n");
		//recom.append("전체 리스트에 등록된 노래가 없습니다. !!\n노래를 등록해 주세요 !!");  // getAll()에 값이 없을 때 발생
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
	ArrayList<Integer> findmusic(int user_prcode, ArrayList<Sim> recomend) { //유사도를 이용하여 추천받르 노래를  정한다. 				
		ArrayList<Integer> recommend_musicNum= new ArrayList<Integer>();
		
		for(int i=2;i<numUser;i++) { // 가장 유사도가 높은 사용자 순으로 추천한다.  
			for(int j=1;j<=50;j++) { //총 50곡의 노래이다. 
				if(uBs.get(recomend.get(i).user_prcode).dataList.get(j)==(double)1 
						&& uBs.get(user_prcode).dataList.get(j)==(double)0 && serch(recommend_musicNum,j)) {
					//추천받을 사용자의 노래가 0이면 재생목록에 없고 추천해줄 사용자의 배열이 1이면 serch 함수 (recommend에 이미 포함되어있는지 확인하는 함수)를 통해서 모두 true값이면 add한다. 
					recommend_musicNum.add(j);
				}
			}
			if(recommend_musicNum.size()>11) break;
		}
		System.out.println(recommend_musicNum.size());
		return recommend_musicNum;
	}
	boolean serch( ArrayList<Integer> recommend_musicNum,int num) {
		// recommend에 포함되어있는지 확인하는 함수. 
		boolean serch=true;
		for(int i=0;i<recommend_musicNum.size();i++) {//recommend에 포함되어있을경우 
			if(recommend_musicNum.get(i)==num)
				serch=false;
		}
		for(int i=0;i<dao.getuserPlaylist(user_prcode).size();i++) { //사용자의 플레이르스트에 이미 추가되어있을경우 
			if(dao.getuserPlaylist(user_prcode).get(i)==num)
				serch=false;
		}
		return serch;
	}

	public ArrayList<Sim> getrecommend(int user_prcode) { 
		//유사도 배열을 이욯하여 유사도가 높은 순으로 정렬하는 함수이다. 
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
		//uBs와 sim행렬의 값을 넣어주는 함수이다. 
		numUser= dao.getUserRowSize(); //user숫자 //user삭제는 되지 않기때문에 0번부터 시작한다고 가정. //0으로 초기화 
		for(int i=1;i<=numUser;i++) { 
			uBs.add(new MyData());
			uBs.get(i).dataList.add(0, 0.0);
			for(int j=1;j<=50;j++) 
				uBs.get(i).dataList.add(j, 0.0); //[사용자][노래]
		}
		
		
		for(int i=1;i<=numUser; i++) // 사용자 * 노래 배열에 사용자 플레이르트에 있는 노래면 1을 넣어준다. 
			for(int j=1;j<=dao.getuserPlaylist(i).size();j++) {
				uBs.get(i).dataList.set(dao.getuserPlaylist(i).get(j-1), 1.0);
			}
		
		temp.similarity=0.0; temp.user_prcode=0;
		similarity.add(temp);
		for(int i=1;i<=numUser;i++) { // 각 사용자 들별로 유사도를 계산하여 값을 넣어준다. 
			temp=new Sim();
			temp.similarity=getCosTheta(user_prcode,i); temp.user_prcode=i;
			similarity.add(temp);
			System.out.println(similarity.get(i).user_prcode+"  "+similarity.get(i).similarity);
		}System.out.println();
		
	}
	
	double getCosTheta(int num1,int num2) { // 유사도 계산을 위해 코사인 내적을 구하는 함수이다. 
		double cos=0;
		double vectorsize=vectorSize(num1,num2);
		if(vectorsize==0) cos=0;
		else cos=vectorProduct(num1,num2)/vectorsize;
		
		return cos;
	}
	double vectorProduct(int num1, int num2) // 백터 의 곱
	{
		double vectorsize=0;
		for(int i=1;i<=numUser; i++) {
			vectorsize+=uBs.get(num1).dataList.get(i)*uBs.get(num2).dataList.get(i);
		}
		return vectorsize;
	}
	double vectorSize(int num1,int num2) //벡터 사이즈구하기 
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