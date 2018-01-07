


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javazoom.jl.player.Player;

//import com.mysql.jdbc.PreparedStatement;

//Model 
// 상품정보 데이터베이스 처리 클래스 구현
// 클래스 선언부
// DB 접속을 위한 정보와 콤보박스 데이터용 	벡터가 선언
class user{
	int music_prcode;
	int userplaylist_prcode;
	void setmusic_prcode(int music_prcode) {this.music_prcode=music_prcode;}
	void setuserplaylist_prcode(int userplaylist_prcde) {this.userplaylist_prcode=userplaylist_prcode;}
}
public class modelTestDAO{
	
	String jdbcDriver ="com.mysql.jdbc.Driver";
	String jdbcUrl="jdbc:mysql://localhost/javadb";
	Connection conn;

	PreparedStatement pstmt; PreparedStatement pstmt2; PreparedStatement pstmt3;
	ResultSet rs;

	Vector<String> listitems =null; 
	Vector<String> playitems =null; 
	Vector<String> recomitems =null; // 콤보 박스 아이템 관리를 위한 벡터
	String sql; 
	
	public modelTestDAO() {};
	
	public ArrayList<modelTestMusic> getplay_music(modelTestMusic usercode){ // 플레이 노래 목록을 가져오는 메소드
		connectDB();
		sql="select * from userplaylist where user_prcode= ?";
		
		ArrayList<modelTestMusic> datas = new ArrayList<modelTestMusic>();

		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, usercode.getuser_prcode()); 
			rs=pstmt.executeQuery();

			while(rs.next()) {
				modelTestMusic m=new modelTestMusic();
				m.setmusic_prcode(rs.getInt("music_prcode"));
				datas.add(m);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
		return datas;
	}
	
	public ArrayList<modelTestMusic> getall_music(){ // 전체 노래 목록을 가져오는 메소드
		connectDB();
		sql="select * from allplaylist";
		
		ArrayList<modelTestMusic> datas = new ArrayList<modelTestMusic>();

		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			rs=pstmt.executeQuery();

			while(rs.next()) {
				modelTestMusic m=new modelTestMusic();
				m.setmusic_prcode(rs.getInt("music_prcode"));
				m.setgenre(rs.getString("genre"));
				m.setsinger(rs.getString("singer"));
				m.setsong(rs.getString("song"));
				datas.add(m);

			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
		return datas;
	}

	public boolean newListMusic(modelTestMusic music) { // 전체 리스트에 노래 추가할 때 (ok)
		connectDB();
		String sql="insert into allplaylist(genre, singer, song) VALUES(?, ?, ?)";
		
		int result =0;
		
		try {
			// 3. Statement 생성
			pstmt=(PreparedStatement) conn.prepareStatement(sql);

			pstmt.setString(1, music.getgenre());
			pstmt.setString(2, music.getsinger());
			pstmt.setString(3, music.getsong());
			
			// 4. SQL 문 전송
			result=pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
	}


	public modelTestMusic getMusic_prcode(int music_prcode) { // music_procde 에 해당하는 music class 를 가져온다. 
		//getProduct()
		connectDB();
		sql="select * from allplaylist where music_prcode = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, music_prcode); 
			rs=pstmt.executeQuery();

			rs.next();
			p=new modelTestMusic();
			p.setmusic_prcode(rs.getInt("music_prcode"));
			p.setgenre(rs.getString("genre"));
			p.setsinger(rs.getString("singer"));
			p.setsong(rs.getString("song"));
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return p;
	}
	public modelTestMusic getMusic_song(String song) { // music_procde 에 해당하는 music class 를 가져온다. 
		//getProduct()
		connectDB();
		sql="select * from allplaylist where song = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, song); 
			rs=pstmt.executeQuery();

			rs.next();
			p=new modelTestMusic();
			p.setmusic_prcode(rs.getInt("music_prcode"));
			p.setgenre(rs.getString("genre"));
			p.setsinger(rs.getString("singer"));
			p.setsong(rs.getString("song"));
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return p;
	}
	public modelTestMusic getMusic_singer(String singer) { // music_procde 에 해당하는 music class 를 가져온다. 
		//getProduct()
		connectDB();
		sql="select * from allplaylist where singer = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, singer); 
			rs=pstmt.executeQuery();

			rs.next();
			p=new modelTestMusic();
			p.setmusic_prcode(rs.getInt("music_prcode"));
			p.setgenre(rs.getString("genre"));
			p.setsinger(rs.getString("singer"));
			p.setsong(rs.getString("song"));
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return p;
	}
	public modelTestMusic getMusic_genre(String genre) { // music_procde 에 해당하는 music class 를 가져온다. 
		//getProduct()
		connectDB();
		sql="select * from allplaylist where genre = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, genre); 
			rs=pstmt.executeQuery();

			rs.next();
			p=new modelTestMusic();
			p.setmusic_prcode(rs.getInt("music_prcode"));
			p.setgenre(rs.getString("genre"));
			p.setsinger(rs.getString("singer"));
			p.setsong(rs.getString("song"));
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return p;
	}
	

	public boolean delPlayMusic(int music_prcode) { 	//전체 플레이리스트삭제  delete
		//delProduct()
		connectDB();
		sql="delete from allplaylist where music_prcode = ?";
		int result=0;

		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, music_prcode);
			result=pstmt.executeUpdate();
			rs.close();
		}catch(Exception e) {}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
	}

	public boolean updateListMusic(modelTestMusic music) { // 전체 리스트 update (ok)
		//updateProduct()
		connectDB();
		sql="update allplaylist set genre= ?, singer= ?, song= ? where music_prcode = ?";
		int result=0;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, music.getgenre());
			pstmt.setString(2, music.getsinger());
			pstmt.setString(3, music.getsong());
			pstmt.setInt(4, music.getmusic_prcode());
			result=pstmt.executeUpdate();
		}catch(Exception e) {}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
	}
	
	public modelTestMusic getUser(String user_name) { 
		//pw받아와서 user을 찾아낸다. 
		connectDB();
		sql="select * from usertable where user_name = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, user_name); 
			rs=pstmt.executeQuery();

			rs.next();
			p=new modelTestMusic();
			p.setuser_prcode(rs.getInt("user_prcode"));
			p.setuser_name(rs.getString("user_name"));
			p.setpw(rs.getString("pw"));
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return p;
	}

	public boolean newUser(modelTestMusic user) { // 유저 추가 (ok)
		connectDB();
		String sql="insert into usertable(pw,user_name) VALUES(?,?)";
		int result=0;
		try {
			// 3. Statement 생성
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(1, user.getpw());
			pstmt.setString(2, user.getuser_name());
			// 4. SQL 문 전송
			result= pstmt.executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
		closeDB();
		if(result>0) {return true;}
		else {return false;}

	}
	public int getallplaylistRowSize() {
		//System.out.printf("ad");
		connectDB();
		int result=0;
		int count=0;
		String sql="select COUNT(*) As result From allplaylist";
		try {
			pstmt=conn.prepareStatement(sql);
			ResultSet r=pstmt.executeQuery(sql);
			r.next();
			count=r.getInt("result");
			r.close();
			System.out.println(count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();

		return count; 
	}
	public int getUserRowSize() {
		//System.out.printf("ad");
		connectDB();
		int result=0;
		int count=0;
		String sql="select COUNT(*) As result From usertable";
		try {
			pstmt=conn.prepareStatement(sql);
			ResultSet r=pstmt.executeQuery(sql);
			r.next();
			count=r.getInt("result");
			r.close();
			//System.out.println(count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();

		return count; 
	}
	public int getuserplaylistRowSize() {
		//System.out.printf("ad");
		connectDB();
		int result=0;
		int count=0;
		String sql="select COUNT(*) As result From userplaylist";
		try {
			pstmt=conn.prepareStatement(sql);
			ResultSet r=pstmt.executeQuery();
			r.next();
			count=r.getInt("result");
			r.close();
			System.out.println(count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeDB();

		return count; 
	}
	public int getuserPlaylistrowsize_same_withID(int ID) { // 아이디와 동일한 노래수 카운트 
		
		connectDB();
		int result=0;
		int count=0;
		String sql="select COUNT(*) As result From userplaylist where user_prcode = '"+ID+"'";
		try {				
			pstmt=conn.prepareStatement(sql);
			ResultSet r=pstmt.executeQuery();
			r.next();
			count=r.getInt("result");
			r.close();
			System.out.println(count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return count;	
	}
	public ArrayList<Integer> getuserPlaylist(int user_prcode){  // userplaylist에서 username과 동일한 음악넘버 추출  이걸 arraylist로 리턴해준다. 
		ArrayList<Integer> userplaylist = new ArrayList<Integer>();
		
		connectDB();
		String sql="select music_prcode From userplaylist where user_prcode = '"+user_prcode+"'";
		try {
			pstmt=conn.prepareStatement(sql);
			ResultSet r=pstmt.executeQuery();
			while(r.next()){
				userplaylist.add(r.getInt("music_prcode"));
			}	
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return userplaylist;
	}
	public ArrayList<Integer> getMusic_prcode_genre(String genre){
		ArrayList <Integer> num=new ArrayList<Integer>();
		connectDB();
		sql="select * from allplaylist where genre = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, genre); 
			rs=pstmt.executeQuery();

			while(rs.next()) {
				num.add(rs.getInt("music_prcode"));
			}
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return num;
	}
	public ArrayList<modelTestMusic> getuserPlaylist_playlistprcode(int user_prcode){  // userplaylist에서 username과 동일한 음악넘버 추출  이걸 arraylist로 리턴해준다. 
		ArrayList<modelTestMusic> userplaylist = new ArrayList<modelTestMusic>();
		modelTestMusic temp;
		connectDB();
		String sql="select * From userplaylist where user_prcode = '"+user_prcode+"'";
		try {
			pstmt=conn.prepareStatement(sql);
			ResultSet r=pstmt.executeQuery();
			while(r.next()){
				temp=new modelTestMusic();
				temp.setuserplaylist_prcode(r.getInt("userplaylist_prcode"));
				temp.setmusic_prcode(r.getInt("music_prcode"));
				temp.setuser_prcode(r.getInt("user_prcode"));
				userplaylist.add(temp);
			}	
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return userplaylist;
	}
	
	

	public boolean newUserPlaylist(modelTestMusic music) {
		connectDB();
		String sql="insert into userplaylist(user_prcode, music_prcode) VALUES(?, ?)";
		
		int result =0;
		
		try {
			// 3. Statement 생성
			pstmt=(PreparedStatement) conn.prepareStatement(sql);

			pstmt.setInt(1, music.getuser_prcode());
			pstmt.setInt(2, music.getmusic_prcode());
			
			// 4. SQL 문 전송
			result=pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
	}
	public boolean delUserPlaylist(modelTestMusic music) {
		connectDB();
		sql="delete from userplaylist where userplaylist_prcode = ?";
		int result=0;

		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, music.getuserplaylist_prcode());
			result=pstmt.executeUpdate();
			rs.close();
		}catch(Exception e) {}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
		
	}
	
	public modelTestMusic getPlayMusic(int music_prcode) { // select
		//getProduct()
		connectDB();
		sql="select * from allplaylist where music_prcode = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, music_prcode); 
			rs=pstmt.executeQuery();

			rs.next();
			p=new modelTestMusic();
			p.setmusic_prcode(rs.getInt("music_prcode"));
			p.setgenre(rs.getString("genre"));
			p.setsinger(rs.getString("singer"));
			p.setsong(rs.getString("song"));
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return p;
	}

	public ArrayList<modelTestMusic> recomgetAll(){ // recommend 목록을 가져오는 메소드 
		connectDB();
		sql="select * from recommend";

		// 전체 검색 데이터를 전달하는 ArrayList
		ArrayList<modelTestMusic> datas = new ArrayList<modelTestMusic>();

		//관리 번호 콤보박스 데이터를 위한 벡터 초기화
		recomitems = new Vector<String>();
		recomitems.add("전체");

		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			rs=pstmt.executeQuery();

			while(rs.next()) {
				modelTestMusic m=new modelTestMusic();
				m.setmusic_prcode(rs.getInt("recommusicnum"));
				m.setgenre(rs.getString("recomgenre"));
				m.setsinger(rs.getString("recomsinger"));
				m.setsong(rs.getString("recomsong"));
				datas.add(m);
				recomitems.add(rs.getString("recommusicnum")); // primary key만 vector로 따로 관리
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
		return datas;
	}

	public boolean newRecomMusic(modelTestMusic music) { // insert
		connectDB();
		String sql="insert into recommend(recomgenre, recomsinger, recomsong) VALUES(?, ?, ?)";

		int result =0;
		try {
			// 3. Statement 생성
			pstmt=(PreparedStatement) conn.prepareStatement(sql);

			pstmt.setString(1, music.getgenre());
			pstmt.setString(2, music.getsinger());
			pstmt.setString(3, music.getsong());

			// 4. SQL 문 전송
			result=pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
	}

	public modelTestMusic getRecomMusic(int recommusicnum) { // select
		//getProduct()
		connectDB();
		sql="select * from recommend where recommusicnum = ?";
		modelTestMusic p=null;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, recommusicnum); 
			rs=pstmt.executeQuery();

			rs.next();
			p=new modelTestMusic();
			p.setmusic_prcode(rs.getInt("recommusicnum"));
			p.setgenre(rs.getString("recomgenre"));
			p.setsinger(rs.getString("recomsinger"));
			p.setsong(rs.getString("recomsong"));
			rs.close();
		}catch(Exception e) {}
		closeDB();
		return p;
	}

	public boolean delRecomMusic(int recommusicnum) { 	// delete
		//delProduct()
		connectDB();
		sql="delete from recommend where recommusicnum = ?";
		int result=0;

		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, recommusicnum);
			result=pstmt.executeUpdate();
			rs.close();
		}catch(Exception e) {}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
	}

	public boolean updateRecomMusic(modelTestMusic music) { // update
		//updateProduct()
		connectDB();
		sql="update recommend set recomgenre= ?, recomsinger= ?, recomsong= ? where recommusicnum = ?";
		int result=0;
		try {
			pstmt=(PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, music.getgenre());
			pstmt.setString(2, music.getsinger());
			pstmt.setString(3, music.getsong());
			pstmt.setInt(4, music.getmusic_prcode());
			result=pstmt.executeUpdate();
		}catch(Exception e) {}
		closeDB();
		if(result>0) {return true;}
		else {return false;}
	}
	// recommend

	public void connectDB() {
		try {
			// 1. jdbc 드라이버 로드
			Class.forName(jdbcDriver);
			// 2. 데이터베이스 연결
			conn=DriverManager.getConnection(jdbcUrl,"root","1234");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void closeDB() {
		try {
			// 6. 연결 해제
			pstmt.close();
			//rs.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	} 

	// Model


} 