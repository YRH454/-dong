package util;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class JdbcUtil {
	private static DataSource dataSource = null;
	static {
		Properties props = new Properties();
		InputStream inStream = JdbcUtil.class.getClassLoader()
				.getResourceAsStream("dbcpconfig.properties");
		try {
			props.load(inStream);
			dataSource = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			throw new ExceptionInInitializerError();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public static void free(ResultSet rs, Statement stmt, Connection conn)
			throws SQLException {
		try {
			if (rs != null)
				rs.close();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} finally {
				if (conn != null)
					conn.close();
			}
		}
	}

	/**
	 * ��ɵ��ô洢��̵�sql��� <br>
	 * ������generateSql<BR>
	 * @param procedureName
	 *            �洢�����
	 * @param num
	 *            �������
	 * @return String ��ɵ�sql���<BR>
	 */
	public static String generateSql(String procedureName, int num) {
		StringBuffer sb = new StringBuffer();
		sb.append("{call " + procedureName + "(");
		for (int i = 0; i < num; i++) {
			if (i == 0) {
				sb.append("?");
			} else {
				sb.append(",?");
			}
		}
		sb.append(")}");
		return sb.toString();
	}
	public static void main(String[] args) throws Exception{
		JdbcUtil.getConnection();
	}


}
