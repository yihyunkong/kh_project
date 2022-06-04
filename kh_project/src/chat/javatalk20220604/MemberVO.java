package chat.javatalk20220604;

public class MemberVO { // VO 클래스 : DAO 클래스에서 작업한 정보를 가져와서(get) 전달(set)한다. 
	private String mem_id;
	private String mem_pw;
	private String mem_nick;
	
	public MemberVO(String mem_id, String mem_pw, String mem_nick) {
		this.mem_id 	= mem_id;
		this.mem_pw 	= mem_pw;
		this.mem_nick 	= mem_nick;
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
