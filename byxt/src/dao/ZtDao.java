package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.JdbcUtil;
import vo.User;

public class ZtDao {
public int query(String adminid) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int zt=-1;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select zt from zt where adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,adminid);
			rs = ps.executeQuery();
			if (rs.next()) {
				zt=rs.getInt(1);			    
			}
			return zt;
			
		} finally {
			JdbcUtil.free(rs, ps, conn);
			
		}
		
	}
	public void update(int zt,String adminid) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update zt set zt=? where adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, zt);		
			ps.setString(2,adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
		
	}
public void add(String adminid) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "insert into zt values(0,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1,adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
		
	}
public void delelte(String adminid) throws Exception{
	
	Connection conn = null;
	PreparedStatement ps = null;
	try {
		conn = JdbcUtil.getConnection();
		String sql = "delete from zt where adminid=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1,adminid);
		ps.executeUpdate();
	} finally {
		JdbcUtil.free(null, ps, conn);
	}
	
}
}
