package chat.javatalk20220608;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chat.javatalk20220604.DBConnectionMgr;
import chat.javatalk20220604.MemberVO;

public class MemberDAO10 {
	Connection 			con;	// 연결
	PreparedStatement 	pstmt; 	// 작성한 쿼리문을 전달한다.
	ResultSet 			rs; 	// 커서
	
	// 생성자
	public MemberDAO10() {
		System.out.println(Delete("khs"));
	}
	
	// 회원가입 메소드 >> 클라이언트가 입력하는 곳 >> VO에서 값을 읽을 필요가 없다. (리턴값 int)
	public int SignIn(String mem_id, String mem_pw, String mem_nick) { // insert
		int result = 0; // 리턴값 result 초기화
		
		String sql = "INSERT INTO MEMBER(mem_id, mem_pw, mem_nick) VALUES(?, ?, ?)"; // 회원가입 시 필요한 insert 쿼리문
		
		con = DBConnectionMgr.getConnection(); // 오라클 서버에 연결하기 
		
		try {
			// SQL문을 전달하는 객체
			pstmt = con.prepareStatement(sql);
			// 데이터 설정
			pstmt.setString(1, mem_id);
			pstmt.setString(2, mem_pw);
			pstmt.setString(3, mem_nick);
			// SQL문 실행하기
			pstmt.executeUpdate();
			// 연결 해제 
			DBConnectionMgr.freeConnection(pstmt, con); // 연결해제 할 때는 반대 순서로 닫아주기
			System.out.println("회원가입 성공");
			result = 1; // 회원가입 성공 시 return값 1 반환
		} catch (SQLException se) { // 동일한 아이디가 table에 존재하면 실행
			se.getStackTrace(); 
			System.out.println(se.getMessage());
			System.out.println("동일한 아이디가 존재합니다.");
			result = -1; // 회원가입 실패 시 return값 -1 반횐
		} catch (Exception e) { // 다른 문제로 회원가입 실패 시 실행
			e.getStackTrace();
			System.out.println(e.getMessage());
			System.out.println("회원가입 실패");
			result = -1; // 회원가입 실패 시 return값 -1 반환
		}
		return result; // 회원가입 성공 시 1
	}
	
	// ID 중복검사 메소드 - 아이디가 있니 or 없니? >> MemberVO에서 확인해보자 >> return값을 MemberVO로 받아오자
	private MemberVO IDCheck(String mem_id) {
		MemberVO mvo = null; // 리턴값 초기화하기
		
		// ID 중복검사 시 필요한 select 쿼리문
		String sql = "SELECT mem_id FROM MEMBER WHERE mem_id = ?";
		
		// 오라클 서버에 연결하기
		con = DBConnectionMgr.getConnection();
		
		try {
			// SQL문을 전달하는 객체
			pstmt = con.prepareStatement(sql);
			// 데이터 설정
			pstmt.setString(1, mem_id);
			// rs로 일치하는 정보가 있는지 확인 실행
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 입력한 아이디가 존재한다면 
				mvo = new MemberVO(mem_id); // 입력한 아이디를 mvo에 넣기 (이 부분은 사실 모름.. 구글링함) 리턴값을 mvo로 받기 위해.. MemberVO에서 읽은 값과 비교?
				System.out.println(mem_id + "는 중복된 아이디 입니다. 다른 아이디를 입력해주세요."); // 입력한 아이디를 사용할 수 없음
			} else { // 입력한 아이디가 존재하지 않는다면 
				System.out.println(mem_id + "는 사용가능한 아이디 입니다.");
			}
			// 연결해제
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("아이디 조회 실패");
		}
		return mvo;
	}
	
	// 로그인 메소드
	public int Login(String mem_id, String mem_pw) {
		
		// 리턴 받을 변수 선언해주자
		int result = 0; // 아이디가 존재하지 않을 때 리턴값 출력		

		// sql 쿼리문을 받아줄거야
		StringBuilder sql = new StringBuilder();
		
		// sql에서 아이디와 비밀번호가 일치하는지 확인하는 쿼리문을 받아오자
		sql.append("select *											");
		sql.append("  from (											");
		sql.append("		select 									    ");
		sql.append("		       case when mem_id = ? then			");
		sql.append("		            case when mem_pw = ? then '1'   ");
		sql.append("		            else '-1'					    ");
		sql.append("		            end								");
		sql.append("		       else '0'								");
		sql.append("		       end login							");
		sql.append("		from member									");
		sql.append("		order by login desc							");
		sql.append("	   )											");
		sql.append("where rownum = 1									");
		
		// 오라클 서버에 연결하기
		con = DBConnectionMgr.getConnection();
		
		try {
			// SQL문을 전달하는 객체
			pstmt = con.prepareStatement(sql.toString());
			// 데이터 설정
			pstmt.setString(1, mem_id);
			pstmt.setString(2, mem_pw);
			// rs로 일치하는 정보가 있는지 확인 실행 >> select 처리
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("로그인 성공"); // 왜 로그인 성공 뜨지? 입력한 아이디가 없는데 ?
				result = 1;
			} 
			
			// 연결해제
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		} catch (SQLException se) { // 해당 catch문이 실행될 때 언제? 아이디와 비번이 일치하지 않을 때.. 존재하는 아이디에 틀린 비번을 입력하면 나온다. 존재하지 않은 아이디를 입력할 때도.. 그럼 else와 else if문은?
			se.getStackTrace();
			System.out.println(se.getMessage()); // 결과 집합을 모두 소모했습니다.
			System.out.println("아이디와 비밀번호가 일치하지 않습니다.");
			result = -1;
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			System.out.println("로그인 실패");
		}
		return result;
	}
	
	// 회원 정보 업데이트 하기 (아이디는 pk이기 때문에 수정 불가. 패스워드 및 닉네임 수정 가능하다.)
	public int Edit(String mem_pw, String mem_nick, String mem_id) {
		int result = 0; // 성공 및 실패 시 반환받을 값 초기화 
		
		String sql = "UPDATE member SET mem_pw = ?, mem_nick = ? WHERE mem_id = ?";
		
		con = DBConnectionMgr.getConnection();
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_pw);
			pstmt.setString(2, mem_nick);
			pstmt.setString(3, mem_id);
			pstmt.executeUpdate();
			DBConnectionMgr.freeConnection(pstmt, con); // 연결해제 할 때는 반대 순서로 닫아주기
			System.out.println("회원정보 수정 성공");
			result = 1; // 업데이트 성공 시 return값 1 반환
			
		} catch (SQLException se){
			se.getStackTrace();
			System.out.println(se.getMessage());
			System.out.println("회원정보 수정 실패");
			result = -1;
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			System.out.println("회원정보 수정 실패");
			result = -1;
		}
		return result;	
	}
	
	// 회원 정보 삭제하기
	public int Delete(String mem_id) {
		int result = 0;
		
		String sql = "DELETE FROM member WHERE mem_id  = ?";
		
		con = DBConnectionMgr.getConnection();
		
		try { // 삭제한 회원을 또 삭제한다고 하면 또 회원정보 삭제 성공 뜸ㅜㅜ
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			pstmt.executeUpdate();
			DBConnectionMgr.freeConnection(pstmt, con);
			System.out.println("회원정보 삭제 성공");
			result = 1;
		} catch (SQLException se) {
			se.getStackTrace();
			System.out.println(se.getMessage());
			System.out.println("회원정보 삭제 실패");
			result = -1;
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			System.out.println("회원정보 삭제 실패");
			result = -1;
		}
		return result;
	}
	
	// 메인메소드
	public static void main(String[] args) {
		new MemberDAO10();
	}

}

/*
 * MemberVO를 리턴값으로 받을 수 있게 수정하던 과정 중에 굳이 그럴 필요가 있나 고민을 해봄
 * 그리고 VO를 왜 쓰는가 고민도 해봤다.. DB에서 정보를 바로 읽어오면 되지 않나? 보안성 때문인가??
 * 1. 가입-로그인 과정에서 클라이언트가 db를 읽을 수 없음
 * 2. 클라이언트는 가입 과정 중에 아이디 중복검사에서만 중복된 아이디가 있는지 확인하는 것만 가능..
 * 3. 그럼 가입 로그인에서 리턴값을 MemberVO로 받을 필요가 없다.
 * 4. 근데 로그인 창에서는 입력하는 아이디가 존재하는지 그 아이디에 맞는 비밀번호가 존재하는지 확인을 해야 로그인 성공인데..
 *    그럼 MemberVO로 리턴 값을 받아볼 수 있겠다...!?
 */
