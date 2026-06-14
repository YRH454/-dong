package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.Encrypt;
import util.JdbcUtil;
import vo.Student;
import vo.User;

public class UserDao {
	public User findUserById(String id) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user=null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from user where userid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				user=new User();
				user.setUserid(rs.getString(1));
				user.setUserpwd(rs.getString(2));
				user.setDp(rs.getString(3));
				user.setLastlogin(rs.getString(4));
			    user.setXuhao(rs.getInt("xuhao"));
			}
		} finally {
			JdbcUtil.free(rs, ps, conn);
		}
		return user;
		
	}
public User findUserByDp(String dp) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user=null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from user where dp=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, dp);
			rs = ps.executeQuery();
			if (rs.next()) {
				user=new User();
				user.setUserid(rs.getString(1));
				user.setUserpwd(rs.getString(2));
				user.setDp(rs.getString(3));
				user.setLastlogin(rs.getString(4));
				   user.setXuhao(rs.getInt("xuhao"));
			    
			}
		} finally {
			JdbcUtil.free(rs, ps, conn);
		}
		return user;
		
	}
	public User findUserByIdDp(String id,String dp) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user=null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from user where userid=? or dp=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, dp);
			rs = ps.executeQuery();
			if (rs.next()) {
				user=new User();
				user.setUserid(rs.getString(1));
				user.setUserpwd(rs.getString(2));
				user.setDp(rs.getString(3));
				user.setLastlogin(rs.getString(4));
				   user.setXuhao(rs.getInt("xuhao"));
			}
		} finally {
			JdbcUtil.free(rs, ps, conn);
		}
		return user;
		
	}
	public List<User> query() throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<User> userList=new ArrayList<User>();
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from user order by xuhao";
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
			   User user=new User();
			 user.setUserid(rs.getString(1));
			 user.setDp(rs.getString(3));
			 user.setLastlogin(rs.getString(4));
			 user.setXuhao(rs.getInt("xuhao"));
			  userList.add(user);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return userList;
	}
	
	public void modipwd(String userid,String userpwd) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update user set userpwd=? where userid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userpwd);
			ps.setString(2, userid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
		
	}
public void modidp(String userid,String dp) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update user set dp=? where userid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, dp);
			ps.setString(2, userid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
		
}
public void modidpxuhao(String userid,String dp,int xuhao) throws Exception{
	
	Connection conn = null;
	PreparedStatement ps = null;
	try {
		conn = JdbcUtil.getConnection();
		String sql = "update user set dp=?,xuhao=? where userid=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, dp);
		ps.setInt(2, xuhao);
		ps.setString(3, userid);
		ps.executeUpdate();
	} finally {
		JdbcUtil.free(null, ps, conn);
	}
	
}
public void modilastlogin(String userid,String lastlogin) throws Exception{
	
	Connection conn = null;
	PreparedStatement ps = null;
	try {
		conn = JdbcUtil.getConnection();
		String sql = "update user set lastlogin=? where userid=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, lastlogin);
		ps.setString(2, userid);
		ps.executeUpdate();
	} finally {
		JdbcUtil.free(null, ps, conn);
	}
	
}
public void add(String userid,String dp) throws Exception{
	
	Connection conn = null;
	PreparedStatement ps = null;
	try {
		conn = JdbcUtil.getConnection();
		String sql = "insert into user (userid,userpwd,dp) values(?,?,?)";
		ps = conn.prepareStatement(sql);
		ps.setString(1, userid);
		ps.setString(2, Encrypt.MD5(userid));
		ps.setString(3,dp);
		ps.executeUpdate();
	} finally {
		JdbcUtil.free(null, ps, conn);
	}
	
}
public void delete(String userid) throws Exception{
	
	Connection conn = null;
	PreparedStatement ps = null;
	try {
		conn = JdbcUtil.getConnection();
		String sql = "delete from user where userid=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, userid);
		
		ps.executeUpdate();
	} finally {
		JdbcUtil.free(null, ps, conn);
	}
	
}
}
