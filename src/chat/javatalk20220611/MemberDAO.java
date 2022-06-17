package chat.javatalk20220611;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chat.javatalk20220604.DBConnectionMgr;

public class MemberDAO {
	// 선언부 
	/////////////////////////// DB 연동 ///////////////////////////
	
	Connection 			con 	= null;
	PreparedStatement 	pstmt 	= null;
	ResultSet 			rs 		= null;
	//////////////////////////////////////////////////////////////
	
	// 생성자
	public MemberDAO() {
//		System.out.println(signUp("apple17892", "123", "사과2"));
//		System.out.println(idCheck("apple12"));
		System.out.println(signIn("apple17892", "123"));
	}

	/**********************************************************
	 * 회원가입 구현
	 * @param  MemberVO mbVO	- 사용자가 입력한 id, pw, name
	 * @return int 		result	- 1: 회원가입 성공, -1: 회원가입 실패
	 * 
	 * INSERT INTO MEMBER(ID, PW, NAME) VALUES(?, ?, ?)
	 **********************************************************/
	// 회원가입 메소드
	public int signUp(String id, String pw, String name) {
		System.out.println("회원가입 메소드 호출 성공");
		
		int result = 0;
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO MEMBER(ID, PW, NAME) ");
		sql.append("		    VALUES(?, ?, ?)      ");
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.executeUpdate();
			
			System.out.println("회원가입 성공");
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("회원가입 실패");
			result = -1;
		} finally {
			DBConnectionMgr.freeConnection(pstmt, con);
		}
		return result;
	}
	
	/**********************************************************
	 * 아이디 중복 검사 구현
	 * @param  String id		- 사용자가 입력한 id, pw, name
	 * @return MemberVO mbVO	
	 * 
	 * SELECT ID FROM MEMBER WHERE ID = ?
	 **********************************************************/
	// 아이디 중복 검사 메소드
	public MemberVO idCheck(String id) {
		System.out.println("아이디 중복 검사 메소드 호출 성공");
		
		MemberVO mbVO = new MemberVO();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID     ");
		sql.append("  FROM MEMBER ");
		sql.append(" WHERE ID = ? ");
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println(id + "은(는) 중복된 아이디 입니다. 다른 아이디를 입력하세요.");
			} else {
				System.out.println(id + "은(는) 사용 가능한 아이디 입니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		}
		return mbVO;
	}
	
	/**********************************************************
	 * 로그인 구현
	 * @param  String id, pw 	- 사용자가 입력한 id, pw
	 * @return int	  result	- 1: 로그인 성공, -1: 로그인 실패
	 * 
	 * SELECT ID FROM MEMBER WHERE ID = ?
	 **********************************************************/
	// 로그인 메소드
	public int signIn(String id, String pw) {
		System.out.println("로그인 메소드 호출 성공");
		
		int result = 0;
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT *											");
		sql.append("  FROM (			 								");
		sql.append("		SELECT 									    ");
		sql.append("		       CASE WHEN ID = ? THEN				");
		sql.append("		            CASE WHEN PW = ? THEN '1' 	    ");
		sql.append("		            ELSE '-1'					    ");
		sql.append("		            END								");
		sql.append("		       ELSE '-1'							");
		sql.append("		        END LOGIN							");
		sql.append("		  FROM MEMBER 								");
		sql.append("		ORDER BY LOGIN DESC							");
		sql.append("	   )											");
		sql.append(" WHERE ROWNUM = 1									");
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(pw.equals(rs.getString("pw"))) {
					result = 1;
					System.out.println("로그인 성공");
				} else {
					result = -1;
					System.out.println("아이디와 비밀번호가 일치하지 않습니다.");
				}
			} else {
				System.out.println("등록되지 않은 회원입니다.");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {			
		 	e.printStackTrace();
			System.out.println("로그인 실패");
			result = -1;
		} finally {
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		}
		return result;
	}

	// 메인 메소드
	public static void main(String[] args) {
		new MemberDAO();
	}
}
