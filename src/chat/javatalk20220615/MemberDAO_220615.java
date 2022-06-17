package chat.javatalk20220615;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chat.javatalk20220604.DBConnectionMgr;

public class MemberDAO_220615 {
	// 선언부 
	/////////////////////////// DB 연동 ///////////////////////////
	Connection 			con 	= null;
	PreparedStatement 	pstmt 	= null;
	ResultSet 			rs 		= null;
	//////////////////////////////////////////////////////////////
	
	// 생성자  
	public MemberDAO_220615() {
//		System.out.println(idCheck("tomato"));
//		System.out.println(signUp("berry", "123", "딸기"));
//		System.out.println(signIn("tomato", "123"));
//		System.out.println(deleteMember("banana", "123"));
		
	}

	/**********************************************************
	 * 회원가입 구현
	 * @param  MemberVO mbVO	- 사용자가 입력한 id, pw, nickname
	 * @return int 		result	- 1: 회원가입 성공, -1: 회원가입 실패
	 * 
	 * INSERT INTO MEMBER(ID, PW, NAME) VALUES(?, ?, ?)
	 **********************************************************/
	// 회원가입 메소드
	public int signUp(String id, String pw, String nickname) {
		System.out.println("회원가입 메소드 호출 성공");
		
		int result = 0;
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO MEMBER(ID, PW, NICKNAME) 	");
		sql.append("		    VALUES(?, ?, ?)      		");
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			
			int i = 1;
			
			pstmt.setString(i++, id);
			pstmt.setString(i++, pw);
			pstmt.setString(i++, nickname);
			
			result = pstmt.executeUpdate(); // insert 성공시 oracle 내에 반환되는 값을 보여준다.
			System.out.println("회원가입 성공");
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
	 * @param  String id		- 사용자가 입력한 id
	 * @return int result	    - 1: 중복된 아이디 , -1: 사용 가능
	 * 
	 * SELECT ID FROM MEMBER WHERE ID = ?
	 **********************************************************/
	// 아이디 중복 검사 메소드
	public int idCheck(String id) {
		System.out.println("아이디 중복 검사 메소드 호출 성공");
		
		int result = 0;
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT *                                    ");
		sql.append("  FROM (                                    ");
		sql.append("        SELECT                              ");
		sql.append("               CASE WHEN ID = ? THEN '1'	");
		sql.append("               ELSE '-1'                    ");
		sql.append("                END IDCHECK                 ");
		sql.append("          FROM MEMBER                       ");
		sql.append("        ORDER BY IDCHECK DESC               ");
		sql.append("       )                                    ");
		sql.append(" WHERE ROWNUM = 1                           ");

		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("IDCHECK").equals("1")) {
					result = 1;
					System.out.println(id + "은(는) 중복된 아이디 입니다. 다른 아이디를 입력하세요.");
				} else if(rs.getString("IDCHECK").equals("-1")){
					result = -1;
					System.out.println(id + "은(는) 사용 가능한 아이디 입니다.");
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("아이디 중복 체크 실패");
		} finally {
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		}
		return result;
	}
	
	/**********************************************************
	 * 로그인 구현
	 * @param  String id, pw 	- 사용자가 입력한 id, pw
	 * @return int	  result	- 1: 로그인 성공, 
	 * 							  0: 아이디 존재, 비밀번호 불일치, 
	 * 							 -1: 아이디 미존재
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
		sql.append("		            ELSE '0'					    ");
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
				if(rs.getString("login").equals("1")) {
					result = 1;
					System.out.println("로그인 성공");
				} else if(rs.getString("login").equals("0")){
					result = 0;
					System.out.println("아이디와 비밀번호가 일치하지 않습니다.");
				} else if(rs.getString("login").equals("-1")) {
					result = -1;
					System.out.println("등록되지 않은 회원입니다.");
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("로그인 실패");
		} catch (Exception e) {			
		 	e.printStackTrace();
			System.out.println("로그인 실패");
		} finally {
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		}
		return result;
	}
	
	/**********************************************************
	 * 회원정보 수정 메소드 구현
	 * @param  MemberVo mvo 	
	 * @return int	  result
	 * 
	 * UPDATE MEMBER SET NICKNAME = ?, PW = ? WHRER ID = ?
	 **********************************************************/
	// 회원정보 수정 메소드
	public int editMember(MemberVO mvo) {
		System.out.println("회원정보 수정 메소드 호출 성공");
		
		int result = 0;
		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE MEMBER                    		");
		sql.append("			  SET NICKNAME = ?, PW = ?	");
		sql.append("			WHERE ID = ?              	");
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			
//			con.setAutoCommit(false);
			
			int i = 1;
			
			pstmt.setString(i++, mvo.getNickname());
			pstmt.setString(i++, mvo.getPw());
			pstmt.setString(i++, mvo.getId());
			
			pstmt.executeUpdate();
//			con.commit();
			
			result = pstmt.executeUpdate();
			System.out.println("회원정보 수정 성공");
		} catch (SQLException se) { 
			se.printStackTrace();
			result = -1;
			System.out.println("회원정보 수정 실패");
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
			System.out.println("회원정보 수정 실패");
		}
		return result;
	}
	
	/**********************************************************
	 * 회원정보 탈퇴 메소드 구현
	 * @param  String id, pw		 	
	 * @return int	  result
	 * 
	 * UPDATE MEMBER SET NICKNAME = ?, PW = ? WHRER ID = ?
	 **********************************************************/
	// 회원 탈퇴 메소드
	public int deleteMember(String id, String pw) {
		System.out.println("회원탈퇴 메소드 호출 성공");
	      
	    String dbpw = "";  // DB상의 비밀번호를 담을 변수
	      
	    int result = 0;
	      
	    //비밀번호 조회
	    StringBuilder sql1 = new StringBuilder();
	    sql1.append("SELECT PW FROM MEMBER       ");
	    sql1.append(" WHERE ID = ?                ");
	      
	    //회원 삭제
	    StringBuilder sql2 = new StringBuilder();
	    sql2.append("DELETE FROM MEMBER          ");
	    sql2.append(" 	 WHERE ID = ?            ");

	    con = DBConnectionMgr.getConnection();
	    
	    try {
	   	  	con.setAutoCommit(false);
	   	  	pstmt = con.prepareStatement(sql1.toString());
	   	  	pstmt.setString(1, id);
	   	  	rs = pstmt.executeQuery();
	       
	   	  	if(rs.next()) {
	   	  		dbpw = rs.getString("PW");
	   	  		
	   	  		if(dbpw.equals(pw)) { //입력된 비밀번호와 DB의 비번 비교
	   	  			//같을경우 회원 삭제 진행
	   	  			pstmt = con.prepareStatement(sql2.toString());
	   	  			pstmt.setString(1, id);
	   	  			result = pstmt.executeUpdate();
	   	  			con.commit();
	   	  			System.out.println("회원 탈퇴 성공");
	   	  		} else if(!dbpw.equals(pw)){ //비밀번호 다를 경우
	   	  			result = 0;
	   	  			System.out.println("회원 탈퇴 실패. 비밀번호가 틀렸습니다.");
	   	  		}
	   	  	}
	    } catch (SQLException se) {
	       se.printStackTrace();
	       result = -1;
	       System.out.println("회원 탈퇴 실패");
	    } catch (Exception e) {
	       e.printStackTrace();
	       System.out.println("회원 탈퇴 실패");
	    } finally {
	       DBConnectionMgr.freeConnection(pstmt, con);
	    }
	    return result;
	}
	
	// 메인 메소드
	public static void main(String[] args) {
		new MemberDAO_220615();
	}
}
