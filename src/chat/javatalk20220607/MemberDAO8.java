package chat.javatalk20220607;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chat.javatalk20220604.DBConnectionMgr;
import chat.javatalk20220604.MemberVO;

public class MemberDAO8 {
	Connection 			con;	// 연결
	PreparedStatement 	pstmt; 	// 작성한 쿼리문을 전달한다.
	ResultSet 			rs; 	// 커서
	
	// constructor
	public MemberDAO8() {
		//System.out.println(SignIn("tomato4741", "456789", "to457"));
		System.out.println(Login("tomato", "7415"));
		//System.out.println(IDCheck("tomato"));
	}
	
/*****************************************************************************************************************************************/
	// 회원가입 메소드 >> 클라이언트가 입력하는 곳 >> VO에서 값을 읽을 필요가 없다.
	private int SignIn(String mem_id, String mem_pw, String mem_nick) { // insert
		System.out.println("회원가입 메소드 실행");
		
		// return 값 선언
		int result = 0;
		
		// 회원가입 시 필요한 insert 쿼리문
		String sql = "INSERT INTO MEMBER(mem_id, mem_pw, mem_nick) VALUES(?, ?, ?)";
		
		// 오라클 서버에 연결하기 
		con = DBConnectionMgr.getConnection();
		
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
			DBConnectionMgr.freeConnection(pstmt, con);
			System.out.println("회원가입 성공");
			result = 1;
		} catch (SQLException se) {
			se.getStackTrace();
			System.out.println(se.getMessage());
			System.out.println("동일한 아이디가 존재합니다.");
			result = -1; // 회원가입 실패 시 -1
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			System.out.println("회원가입 실패");
			result = -1; // 회원가입 실패 시 -1
		}
		return result; // 회원가입 성공 시 1
	}
	
/*****************************************************************************************************************************************/
	// ID 중복검사 메소드 - 아이디가 있니 or 없니? >> MemberVO에서 확인해보자
	private MemberVO IDCheck(String mem_id) {
		
		// memberVO에서 값을 읽어보기 위해 MemberVO 인스턴스화 하여 확인하기
		MemberVO mvo = null;
		
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
			
			// 중복된 아이디가 있는지 확인하기
			/*if(rs.next()) { // 만약 아이디가 있다면 
				if(mem_id.equals(rs.getString("mem_id"))) {
					System.out.println("아이디가 존재합니다. 다른 아이디를 입력해주세요.");
				}
			} else {
				System.out.println("사용가능한 아이디 입니다.");
			}*/
			
			// 중복된 아이디가 있는지 확인하기
			/*
			 * if 아이디가 있다면
			 * 		중복된 아이디 입니다.
			 * else 아이디가 없다면
			 * 		해당 아이디를 사용할 수 있습니다.
			 */
			
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
	
/******************************************************************************************************************************************/
	// 로그인 메소드 : 로그인 성공 시 111, 로그인 실패 시 -111, 아이디나 비밀번호가 틀렸을 시 000
	private MemberVO Login(String mem_id, String mem_pw) {
		
		// memberVO에서 값을 읽어보기 위해 MemberVO 인스턴스화 하여 확인하기
		MemberVO mvo = null;
	
		// ID와 PW 확인 시 필요한 select 쿼리문
		String sql = "SELECT * FROM MEMBER WHERE mem_id = ?";
		
		// 오라클 서버에 연결하기
		con = DBConnectionMgr.getConnection();
		
		try {
			// SQL문을 전달하는 객체
			pstmt = con.prepareStatement(sql);
			// 데이터 설정
			pstmt.setString(1, mem_id);
			// rs로 일치하는 정보가 있는지 확인 실행
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
			
			/*if(rs.next()) { // 아이디가 있다면 
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
			}*/
			
			/*if(rs != null) {
				if(rs.next()) {
					mvo = new MemberVO(mem_id, mem_pw); // 아이디와 비밀번호 읽기
					System.out.println("로그인 성공!");
				} else if(!(rs.getString("mem_pw").equals(mem_pw))) { // 아이디에 맞는 비밀번호가 다르다면
					System.out.println("비밀번호를 확인해주세요.");
				}
			} else {
				System.out.println("등록되지 않은 회원입니다.");
			}*/
			
			if(rs.next()) { // 아이디가 존재할 때
				if(rs.getString("mem_pw").equals(mem_pw)) { // 아이디에 맞는 비밀번호가 일치하다면
					mvo = new MemberVO(mem_id, mem_pw); // 아이디와 비밀번호 읽기
					System.out.println("로그인 성공!");
				} else if(!(rs.getString("mem_pw").equals(mem_pw))){ // 아이디는 존재하고 비밀번호가 일치하지 않다면
					System.out.println("비밀번호가 일치하지 않습니다.");
				}
			} else { // 등록된 아이디가 있고 비밀번호가 틀릴 때 나온다. 위에 중첩된 else문이 떠야하는데..
				System.out.println("등록되지 않은 회원입니다."); // 등록된 아이디가 없을 때
			}
			// 연결해제
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		} catch (SQLException se) { // 해당 catch문이 실행될 때 언제? 아이디와 비번이 일치하지 않을 때.. 존재하는 아이디에 틀린 비번을 입력하면 나온다. 존재하지 않은 아이디를 입력할 때도.. 그럼 else와 else if문은?
			se.getStackTrace();
			System.out.println(se.getMessage()); // 결과 집합을 모두 소모했습니다.
			System.out.println("로그인 실패");
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			System.out.println("로그인 실패");
		}
		return mvo;
	}
	
	// 메인메소드
	public static void main(String[] args) {
		new MemberDAO8();
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
