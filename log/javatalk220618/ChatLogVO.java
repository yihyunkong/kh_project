package log.javatalk220618;

public class ChatLogVO {
	private int roomnum = 0;
	private String nickname = null;
	private String chat_msg = null;
	private String chat_date = null;
	private String chat_time = null;
	
	public int getRoomnum() { 
		return roomnum;
	}
	
	public void setRoomnum(int roomnum) {
		this.roomnum = roomnum;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getChat_msg() {
		return chat_msg;
	}
	
	public void setChat_msg(String chat_msg) {
		this.chat_msg = chat_msg;
	}
	
	public String getChat_date() {
		return chat_date;
	}
	
	public void setChat_date(String chat_date) {
		this.chat_date = chat_date;
	}
	
	public String getChat_time() {
		return chat_time;
	}
	
	public void setChat_time(String chat_time) {
		this.chat_time = chat_time;
	}
}
