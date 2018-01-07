
public class modelTestMusic {
	
	
	private int user_prcode;
	private String user_name;
	private String pw ;
	// user table
	
	private int recommend_prcode;
	// music list window (ok)
	
	private int userplaylist_prcode;
	// recommend music window
	
	private int music_prcode;
	private String genre;
	private String singer;
	private String song;
	// play list window
	
	
	// get/set ¸Þ¼Òµå
	public int getuser_prcode() { return user_prcode;}
	public void setuser_prcode(int user_prcode) {this.user_prcode=user_prcode;}	
	public String getuser_name() {return this.user_name;}
	public void setuser_name(String user_name) {this.user_name=user_name;}
	public String getpw() {return pw;}
	public void setpw(String pw) {this.pw=pw;}
	
	
	public int getrecommend_prcode() {return recommend_prcode;}
	public void setrecommend_prcode(int recommend_prcode) {this.recommend_prcode=recommend_prcode;}
	
	public int getuserplaylist_prcode() {return userplaylist_prcode;}
	public void setuserplaylist_prcode(int userplaylist_prcode) {this.userplaylist_prcode=userplaylist_prcode;}
	
	public int getmusic_prcode() {return music_prcode;}
	public void setmusic_prcode(int music_prcode) {this.music_prcode=music_prcode;}
	public String getgenre() {return genre;}
	public void setgenre(String genre) {this.genre=genre;}
	public String getsinger() {return singer;}
	public void setsinger(String singer) {this.singer=singer;}
	public String getsong() {return song;}
	public void setsong(String song) {this.song=song;}
	
	public void set(modelTestMusic music) {
		music_prcode=music.getmusic_prcode();
		genre=music.getgenre();
		singer=music.getsinger();
		song=music.getsong();
	}
	
	
} 