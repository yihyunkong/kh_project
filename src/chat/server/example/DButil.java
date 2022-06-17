package chat.server.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * DButil클래스는 connection을 매번하는게 번거롭기 때문에
 * 미리 이 코드들을 유틸클래스로 만들어놓고 가져다 쓰기 위해서 즉 편의성을 위해
 * 미리 만들어 놓는다.
 * 리턴으로 Connection 객체를 반환 받아와야한다.
 */
// DB연동 전담하는 클래스 
public class DButil {
	private static Connection con = null;
	private static final String url = "jdbc:oracle:thin:@localhost:1521/ORCL";
	private static final String user = "JAVATALK";
	private static final String pwd = "yy0415";
	private static final String driver = "oracle.jdbc.driver.OracleDriver";

	public static Connection getOracleConnection() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("연결에 실패하였습니다.");
		}
		return con;
	}

	// Connection 종료 메서드
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// PreparedStatement, Connection 종료 메서드
	public static void close(Connection con, PreparedStatement pst) {
		if (con != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			close(con);
		}
	}

	// Connection, PreparedStatement , ResultSet 닫기
	public static void close(Connection con, PreparedStatement pst, ResultSet rs) {
		if (con != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}
			close(con, pst);
		}

	}

	public static void close(Connection con, Statement st, ResultSet rs) {
		if(con != null) {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			close(con);
		}
	}
}
