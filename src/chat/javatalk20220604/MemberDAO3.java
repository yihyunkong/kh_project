package chat.javatalk20220604;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.util.List;
//import chat.javatalk20220604.MemberVO;

public class MemberDAO3 { // 데이터 접근하기
	Connection 			con;
	PreparedStatement 	pstmt; // 작성한 쿼리문을 전달한다.
	ResultSet 			rs;
	//List<MemberVO>		list;
	
	public MemberDAO3() {
		//System.out.println(Login("tomato", "123"));
		//System.out.println(IDCheck("tomato"));
		System.out.println(SignIn("tomato", "123", "mato"));
	}
	
	// 회원가입 메소드
	private MemberVO SignIn(String mem_id, String mem_pw, String mem_nick) { // MemberVO를 리턴값으로 설정하여 파라미터 값도 MemberVO로 
		MemberVO mvo = new MemberVO(mem_id, mem_pw, mem_nick);
		
		// 회원가입 시 필요한 insert 쿼리문
		String sql = "INSERT INTO MEMBER(mem_id, mem_pw, mem_nick) VALUES(?, ?, ?)";
		
		// 연결
		con = DBConnectionMgr.getConnection(); // DBConnectionMgr을 통해서 오라클 서버와 연결하기
		
		// 전송
		try {
			pstmt = con.prepareStatement(sql); // sql(쿼리)문을 연결하여 전달함
			pstmt.setString(1, mvo.getMem_id());
			pstmt.setString(2, mvo.getMem_pw());
			pstmt.setString(3, mvo.getMem_nick());
			pstmt.executeUpdate();
			
			System.out.println("MemberDAO.SignIn().sql : " + sql);
			System.out.println("회원가입 성공");
			
			DBConnectionMgr.freeConnection(pstmt, con); // connection 해제
		} catch (Exception e) {
			System.out.println("오류 발생");
			e.getStackTrace();
		}
		return mvo;
	}
	
	// ID 중복검사 메소드 : 아이디 중복 시 -1
	private MemberVO IDCheck(String mem_id) {
		
		MemberVO mvo = new MemberVO(mem_id, mem_id, mem_id);
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
				}
			} else {
				System.out.println("사용가능한 아이디 입니다.");
			}
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		} catch (Exception e) {
			System.out.println("아이디 중복검사 실패");
			e.getStackTrace();
		}
		System.out.println("아이디 중복검사 메소드 호출 성공");
		return mvo;
	}
	
	// 로그인 메소드 : 로그인 성공 시 111, 로그인 실패 시 -111, 아이디나 비밀번호가 틀렸을 시 000
	private MemberVO Login(String mem_id, String mem_pw) {
		
		MemberVO mvo = new MemberVO(mem_id, mem_pw, mem_pw);
		
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
				} else { // 아니라면
					System.out.println("비밀번호를 확인해주세요.");
				}
			} else { // 아이디가 없다면
				System.out.println("등록되지 않은 회원입니다.");
			}
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		} catch (Exception e) {
			e.getStackTrace();
		}
		System.out.println("로그인 메소드 호출 성공");
		return mvo;
	}
	
	// 메인메소드
	public static void main(String[] args) {
		new MemberDAO3();
	}

}
