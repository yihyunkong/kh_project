package chat.javatalk20220604;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MemberDAO {
	Connection 			con;
	PreparedStatement 	pstmt; // 작성한 쿼리문을 전달한다.
	ResultSet 			rs;
	//List<MemberVO>		list;
	
	public MemberDAO() {
		System.out.println(Login("tomato", "123"));
	}
	
	// 회원가입 메소드
	private int SignIn(String mem_id, String mem_pw, String mem_nick) {
		
		// 회원가입 시 필요한 insert 쿼리문
		String sql = "INSERT INTO MEMBER(mem_id, mem_pw, mem_nick) VALUES(?, ?, ?)";
		
		con = DBConnectionMgr.getConnection(); // DBConnectionMgr을 통해서 오라클 서버와 연결하기
		
		try {
			pstmt = con.prepareStatement(sql); // sql(쿼리)문을 연결하여 전달함
			pstmt.setString(1, mem_id);
			pstmt.setString(2, mem_pw);
			pstmt.setString(3, mem_nick);
			pstmt.executeUpdate();
			//DBConnectionMgr.freeConnection(pstmt, con); // connection 해제
		} catch (Exception e) {
			e.getStackTrace();
		}
		System.out.println("회원가입 메소드 호출 성공");
		return 1;
	}
	
	// ID 중복검사 메소드 : 아이디 중복 시 -1
	private int IDCheck(String mem_id) {
		int result = 0;
		
		// ID 중복검사 시 필요한 select 쿼리문
		String sql = "SELECT mem_id FROM MEMBER WHERE mem_id = ?";
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			
			// 중복된 아이디가 있는지 확인하기
			if(rs.next()) { // 만약 아이디가 있다면 return result = -1
				if(mem_id.equals(rs.getString("mem_id"))) {
					System.out.println("아이디가 존재합니다. 다른 아이디를 입력해주세요.");
					result = -1;
				}
			} else {
				System.out.println("사용가능한 아이디 입니다.");
				result = 1;
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		System.out.println("아이디 중복검사 메소드 호출 성공");
		return result;
	}
	// 로그인 메소드 : 로그인 성공 시 111, 로그인 실패 시 -111, 아이디나 비밀번호가 틀렸을 시 000
	private int Login(String mem_id, String mem_pw) {
		int result = 0;
		
		// ID와 PW 확인 시 필요한 
		String sql = "SELECT mem_id, mem_pw FROM MEMBER WHERE mem_id = ? AND mem_pw = ?";
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			pstmt.setString(2, mem_pw);
			rs = pstmt.executeQuery();
			
			/*
			 * if 아이디가 있다면
			 * 		if 아이디랑 비밀번호가 일치하다면
			 * 			 >> 로그인 성공
			 * 		else 일치하지 않다면
			 * 			 >> 비밀번호가 틀렸습니다.
			 * else 아이디가 없다면
			 * 		>> 등록되지 않은 회원입니다.
			 */
			
			if(rs.next()) { // 아이디가 있다면 
				if(rs.getString("mem_pw").equals(mem_pw)) { // 입력한 비밀번호가 맞다면
					System.out.println("로그인 성공");
					result = 111;
				} else { // 아니라면
					System.out.println("비밀번호를 확인해주세요.");
					result = 000;
				}
			} else { // 아이디가 없다면
				System.out.println("등록되지 않은 회원입니다.");
				result = -111;
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		System.out.println("로그인 메소드 호출 성공");
		return result;
	}
	
	// 메인메소드
	public static void main(String[] args) {
		new MemberDAO();
	}

}
