package chat.javatalk20220608;

public class MemberVO { // read only !
	private String mem_id;
	private String mem_pw;
	private String mem_nick;
	
	// 회원가입 및 회원조회
	public MemberVO(String mem_id, String mem_pw, String mem_nick) {
		this.mem_id = mem_id;
		this.mem_pw = mem_pw;
		this.mem_nick = mem_nick;
	}
	 
	// 아이디 중복검사
	public MemberVO(String mem_id) {
		this.mem_id = mem_id;
	}
	
	// 로그인
	public MemberVO(String mem_id, String mem_pw) {
		this.mem_id = mem_id;
		this.mem_pw = mem_pw;
	}

	public String getMem_id() {
		return mem_id;
	}
	
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	
	public String getMem_pw() {
		return mem_pw;
	}
	
	public void setMem_pw(String mem_pw) {
		this.mem_pw = mem_pw;
	}
	
	public String getMem_nick() {
		return mem_nick;
	}
	
	public void setMem_nick(String mem_nick) {
		this.mem_nick = mem_nick;
	}
	
	
	
}
