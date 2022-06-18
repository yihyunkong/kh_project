package log.javatalk220618;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chat.javatalk20220615.DBConnectionMgr;
//import log.javatalk220618.ChatLogVO;

	// 대화 내용 저장 테이블
	public class ChatLogDAO_02 {
	// 선언부 
	//////////////////////// DB연동 ////////////////////////
	Connection 			con 		= null;
	PreparedStatement 	pstmt 		= null;
	ResultSet 			rs 			= null;
	
	//////////////////////// DB연동 ////////////////////////
	
	// constructor
	public ChatLogDAO_02() {
		System.out.println(saveMsg(1, "토마토", "hi"));
	}
	
	// 대회 내용 저장 메소드
	public boolean saveMsg(int roomnum, String nickname, String chat_msg) {
//		ChatLogVO chVO = new ChatLogVO();
		
		boolean isOk = true;
		
		StringBuilder sql = new StringBuilder();
		sql.append("insert into chatlog						");
		sql.append("values(?, ?, ?, sysdate, systimestamp)	");
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, roomnum);
			pstmt.setString(2, nickname);
			pstmt.setString(3, chat_msg);
			pstmt.executeUpdate();
			
			System.out.println("대화내용 저장 성공");
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("대화내용 저장 실패");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("대화내용 저장 실패");
		} finally {
			DBConnectionMgr.freeConnection(pstmt, con);
		}
		return isOk;
	}

	// 메인 메소드
	public static void main(String[] args) {
		new ChatLogDAO_02();
	} 

}
