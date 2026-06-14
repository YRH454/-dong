package com.byxt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class JdbcUtil {

    private static DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource ds) {
        JdbcUtil.dataSource = ds;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void free(ResultSet rs, Statement stmt, Connection conn) throws SQLException {
        try { if (rs != null) rs.close(); } finally {
            try { if (stmt != null) stmt.close(); } finally {
                if (conn != null) conn.close();
            }
        }
    }
}
