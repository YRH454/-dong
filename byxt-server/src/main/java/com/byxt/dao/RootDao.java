package com.byxt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.byxt.util.JdbcUtil;
import com.byxt.model.Root;
import com.byxt.model.User;

public class RootDao {
	public Root findUserById(String id) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Root root=null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from root where rootid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				root=new Root();
				root.setRootid(rs.getString(1));
				root.setRootpwd(rs.getString(2));
			    
			}
		} finally {
			JdbcUtil.free(rs, ps, conn);
		}
		return root;
		
	}
	public void modipwd(String rootid,String rootpwd) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update root set rootpwd=? where rootid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, rootpwd);
			ps.setString(2, rootid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
		
	}
}
