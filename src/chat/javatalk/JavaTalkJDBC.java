package chat.javatalk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//DB 연동을 전담하는 클래스
public class JavaTalkJDBC { 
	public static Connection con 		= null; // DB와 연결하는 인터페이스
	PreparedStatement ps	= null; // SQL문 실행하는 인터페이스 (PreparedStatement는 Statement에 비해 속도가 빠르다.)
	ResultSet rs			= null; // 조회된 결과 데이터를 갖는 인터페이스 (커서)
	
	// 오라클 계정 연결에 필요한 정보 
	private static final String url  	= "jdbc:oracle:thin:@localhost:1521/ORCL";
	private static final String id	 	= "JAVATALK";
	private static final String pw		= "yy0415";
	
	// Connection 시작 메소드 (DB와 연결)
	public static Connection getConnection() { // 인스턴스 호출 없이 쓰기 위해 static
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // JDBC 드라이버 로딩
			con = DriverManager.getConnection(url, id, pw); // connection 객체 생성하기 및 접속하는 작업
		} catch (Exception e) { // 모든 Exception 확인 가능
			e.printStackTrace(); // Exception이 발생하면 console에 내용을 나타낸다.
		}
		return con;
	}
	
	// Connection 종료 메소드 (Connection 사용 후, DB와 연결 종료)
	public static void closeConnection(ResultSet rs, PreparedStatement ps, Connection con) { // select문에서 사용한 객체. select문에서 커서를 사용한다.
		try {
			if(rs != null) { // connection이 실행되고 있다면,
				rs.close(); // connection 닫아주기
			}
			if(ps != null) {
				ps.close();
			}
			if(con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection(PreparedStatement ps, Connection con) { // insert, delete, update문에서 사용한 객체 
		try {
			if(ps != null) {
				ps.close();
			}
			if(con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
