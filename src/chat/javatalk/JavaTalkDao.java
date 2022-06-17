package chat.javatalk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import chat.server.example.ChatMsgVO;

public class JavaTalkDao { // DAO(Data Access Object) : db에 접근하기 위한 객체
	// 전역변수 선언
	Connection 			con = JavaTalkJDBC.getConnection();  // 오라클 서버와 연결하기
	PreparedStatement 	ps;   // 오라클 서버에 작성한 select문을 전달하고 오라클 서버에 처리 요청하기
	ResultSet 			rs;   // 조회 결과를 자바 코드로 가져올 때 필요함 - 오라클 서버의 커서를 조작하기
	List<ChatMsgVO> 	list; // 채팅 메세지 백업
	
	// 회원가입 메소드 (name, id, pw)
	void Join(String name, String id, String pw) { // 회원가입 시 입력해야하는 값
		String sql = "INSERT INTO JAVATALK(ID, PW) VALUES(?, ?)"; // 회원가입 시 insert문
		//con = JavaTalkJDBC.getConnection(); // JDBC(JavaTalkJDBC)의 getConnection 메소드를 오라클 서버와 연결하는 Connection에 담기
		try {
			ps = con.prepareStatement(sql); // Connection con에 sql문(insert)을 파라미터로 넣기
			ps.setString(1, name);
			ps.setString(2, id);
			ps.setString(3, pw);
			JavaTalkJDBC.closeConnection(rs, ps, con);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	// id 중복검사 메소드
	/*************************************************
	 * @param id : 사용자가 입력한 아이디
	 * @return 1: id 존재, 0: 아이디 사용가능, -1: default
	 *************************************************/
	public int IdCheck(String id) { // param : 사용자가 입력한 아이디
		int result = -1; // return 값이 default
		
		String sql = "SELECT ID FROM JAVATALK WHERE ID = ?"; // 중복된 아이디가 존재한다면 
		
		try {
			ps = con.prepareStatement(sql.toString()); // ? 자리에 들어갈 아이디 설정하기
			ps.setString(2, id);
			rs = ps.executeQuery(); // select문 처리. insert, update, delete문 처리는 excuteUpdate()
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	// 로그인 메소드
	void Login() {
		
	}
	
	// 대화내용 테이블에 저장 메소드 
	void ChatSave() {
		
	}
	
	// 대화내용 백업 메소드
	void ChatBackUp() {
		
	}
	
	public static void main(String[] args) {
		JavaTalkDao jt = new JavaTalkDao();
	}

}
