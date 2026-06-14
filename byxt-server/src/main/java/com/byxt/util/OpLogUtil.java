package com.byxt.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OpLogUtil {
    public static void log(String adminId, String content) {
        try (Connection c = JdbcUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement("INSERT INTO op_log(content, adminid, create_time) VALUES(?, ?, NOW())");
            ps.setString(1, content);
            ps.setString(2, adminId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception ignored) {}
    }
}
