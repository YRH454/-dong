package com.byxt.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.byxt.util.JdbcUtil;
import com.byxt.model.Student;
import com.byxt.model.Teacher;
import com.byxt.model.Tm;
import com.byxt.model.TmEx;

public class TmDao {
	//--------------------------------------------绠＄悊鍛樼鏂规硶
	public void delete(int tmid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "delete from tm where id=? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, tmid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public int getRecordCount(String condition) throws Exception{
		 
		 Connection conn = null;
		 PreparedStatement pst = null;
		 ResultSet rs = null;
		int recordcount=0;
		try {
				conn = JdbcUtil.getConnection();
				String sql = "select count(*) from tm "+condition;
				//System.out.println(sql);
				pst = conn.prepareStatement(sql);
				rs=pst.executeQuery();
				rs.next();
				recordcount=rs.getInt(1);
				
			}finally {
				JdbcUtil.free(rs, pst, conn);
			}
			return recordcount;
		 
	}
	public List<Tm> query(String condition,int pageNo,int pageSize) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Tm> tmList=new ArrayList<Tm>();
		int startrecno=(pageNo-1)*pageSize;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from tm "+condition+" order by xh  limit ?,? ";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, startrecno);
			ps.setInt(2, pageSize);
			rs=ps.executeQuery();
			while(rs.next()){
			   Tm tm=new Tm();
			   tm.setId(rs.getInt("id"));
				tm.setGh(rs.getString("gh"));
				tm.setTxm(rs.getString("txm"));
				tm.setTm(rs.getString("tm"));
				tm.setBz(rs.getString("bz"));
				tm.setXh(rs.getString("xh"));
				tm.setSxm(rs.getString("sxm"));
				tm.setZy(rs.getString("zy"));
				tm.setBj(rs.getString("bj"));
				tm.setAdminid(rs.getString("adminid"));
			   tmList.add(tm);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return tmList;
	}
	public void qingkong(String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement ps1 = null;
		//PreparedStatement ps2 = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql1 = "delete from tm where adminid=? ";
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, adminid);
		    ps1.executeUpdate();
		    
		   // String sql2 = "alter table tm auto_increment=1  ";
			//ps2 = conn.prepareStatement(sql2);
		   // ps2.executeUpdate();
		    
		} finally {
			JdbcUtil.free(null, ps1, null);
			//JdbcUtil.free(null, ps2, conn);
		}
    }
	public void exportToExcel(File excelPath,String condition) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		WritableWorkbook wwb=null;
		WritableSheet ws=null;
		//excelPath.delete();
		try {
			conn=JdbcUtil.getConnection();
			String sql = "select gh,txm,tm,bz,xh,sxm,zy,bj from tm  "+condition+" order by gh ";
			ps=conn.prepareStatement(sql);
		    rs=ps.executeQuery();
			 wwb=Workbook.createWorkbook(excelPath);
			 ws=wwb.createSheet("sheet1",0);
			 String[] titles={"鏁欏笀宸ュ彿","鏁欏笀濮撳悕","棰樼洰","棰樼洰澶囨敞","瀛︾敓瀛﹀彿","瀛︾敓濮撳悕","涓撲笟","鐝骇"};
			 int columnCount=titles.length;
			 for(int i=0;i<columnCount;i++){
				
				 ws.addCell(new Label(i,0,titles[i]));
			 }
			
			int count=1;
			 while(rs.next()){
				 for(int j=0;j<columnCount;j++){
			   			
			   	  		ws.addCell(new Label(j,count,rs.getString(j+1)));
			    }
				count++; 
			 }
			wwb.write();
			
			
		} finally {

			if(wwb!=null) wwb.close();
			JdbcUtil.free(rs, ps, conn);
		}
	}
	
	public void exportToExcel2(File excelPath,String condition) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		WritableWorkbook wwb=null;
		WritableSheet ws=null;
		//excelPath.delete();
		try {
			conn=JdbcUtil.getConnection();
			String sql = "select gh,txm,tm,bz,tm.xh,sxm,tm.zy,tm.bj,student.email,student.phone,student.qq from tm,student  "+condition+" and student.xh=tm.xh order by gh ";
			ps=conn.prepareStatement(sql);
		    rs=ps.executeQuery();
			 wwb=Workbook.createWorkbook(excelPath);
			 ws=wwb.createSheet("sheet1",0);
			 String[] titles={"鏁欏笀宸ュ彿","鏁欏笀濮撳悕","棰樼洰","棰樼洰澶囨敞","瀛︾敓瀛﹀彿","瀛︾敓濮撳悕","涓撲笟","鐝骇","email","phone","qq"};
			 int columnCount=titles.length;
			 for(int i=0;i<columnCount;i++){
				
				 ws.addCell(new Label(i,0,titles[i]));
			 }
			
			int count=1;
			 while(rs.next()){
				 for(int j=0;j<columnCount;j++){
			   			
			   	  		ws.addCell(new Label(j,count,rs.getString(j+1)));
			    }
				count++; 
			 }
			wwb.write();
			
			
		} finally {

			if(wwb!=null) wwb.close();
			JdbcUtil.free(rs, ps, conn);
		}
	}
	
	//--------------------------------------------鏁欏笀绔柟娉?
	public void add(Tm tm,String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "insert into tm (gh,txm,tm,bz,xh,sxm,zy,bj,adminid)  values (?,?,?,?,'','','','',?) ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, tm.getGh());
			ps.setString(2,tm.getTxm());
			ps.setString(3,tm.getTm());
			ps.setString(4,tm.getBz());		
			ps.setString(5, adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void update(Tm tm) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update tm set tm=?,bz=?,xh=?,sxm=?,zy=?,bj=? where id=?  ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, tm.getTm());
			ps.setString(2, tm.getBz());
			ps.setString(3,tm.getXh());
			ps.setString(4,tm.getSxm());
			ps.setString(5, tm.getZy());
			ps.setString(6, tm.getBj());
			ps.setInt(7,tm.getId());
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void update2(String xh,String xm,String zy,String bj,String adminid) throws Exception {//瀛︾敓淇敼璧勬枡鍚庯紝棰樼洰闇€瑕佸啀鏇存柊涓?
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update tm set sxm=?,zy=?,bj=? where xh=? and adminid=?  ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, xm);
			ps.setString(2, zy);
			ps.setString(3,bj);
			ps.setString(4,xh);
			ps.setString(5, adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void update3(String gh,String txm,String adminid) throws Exception {//鏁欏笀淇敼璧勬枡鍚庯紝棰樼洰闇€瑕佸啀鏇存柊涓?
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update tm set txm=? where gh=? and adminid=?  ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, txm);
			ps.setString(2, gh);
			ps.setString(3,adminid);
			
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public List<TmEx> query(String condition,String adminid) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TmEx> tmList=new ArrayList<TmEx>();
		
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from tm "+condition+" order by xh desc ";
			ps=conn.prepareStatement(sql);		
			rs=ps.executeQuery();
			while(rs.next()){
			   TmEx tmEx=new TmEx();
			   tmEx.setId(rs.getInt("id"));
				tmEx.setGh(rs.getString("gh"));
				tmEx.setTxm(rs.getString("txm"));
				tmEx.setTm(rs.getString("tm"));
				tmEx.setBz(rs.getString("bz"));
				tmEx.setXh(rs.getString("xh"));
				tmEx.setSxm(rs.getString("sxm"));
				tmEx.setZy(rs.getString("zy"));
				tmEx.setBj(rs.getString("bj"));
				tmEx.setAdminid(rs.getString("adminid"));
				if(!"".equals(rs.getString("xh"))){
					StudentDao studentDao=new StudentDao();
					TeacherDao teacherDao=new TeacherDao();
					Student st=studentDao.findStudentByIdAdminid(rs.getString("xh"),adminid);
					Teacher tc=teacherDao.findTeacherByIdAdminid(rs.getString("gh"),adminid);
					tmEx.setSemail(st.getEmail());
					tmEx.setSphone(st.getPhone());
					tmEx.setSqq(st.getQq());
					tmEx.setTzhicheng(tc.getZhicheng());
					tmEx.setTbgdd(tc.getBgdd());
					tmEx.setTemail(tc.getEmail());
					tmEx.setTphone(tc.getPhone());
					tmEx.setTqq(tc.getQq());
					
				}
				
			   tmList.add(tmEx);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return tmList;
	}
	//瀛︾敓绔柟娉?
	public List<Tm> querytm(String condition) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Tm> tmList=new ArrayList<Tm>();		
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from tm "+condition+" order by gh ";
			ps=conn.prepareStatement(sql);			
			rs=ps.executeQuery();
			while(rs.next()){
			   Tm tm=new Tm();
			   tm.setId(rs.getInt("id"));
				tm.setGh(rs.getString("gh"));
				tm.setTxm(rs.getString("txm"));
				tm.setTm(rs.getString("tm"));
				tm.setBz(rs.getString("bz"));
				tm.setXh(rs.getString("xh"));
				tm.setSxm(rs.getString("sxm"));
				tm.setZy(rs.getString("zy"));
				tm.setBj(rs.getString("bj"));			
				tm.setAdminid(rs.getString("adminid"));
			    tmList.add(tm);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return tmList;
	}
	public void qcjl(String sno,String adminid) throws Exception {//娓呴櫎棰樼洰璁板綍
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update tm set xh='',sxm='',zy='',bj='' where xh=? and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sno);
			ps.setString(2,adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public static synchronized int xt(int tmid,String xh,String xm,String zy,String bj) throws Exception{
		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;			
		try {
			conn = JdbcUtil.getConnection();
			String sql1 = "select * from tm where xh='' and id="+tmid;
			ps1=conn.prepareStatement(sql1);			
			rs=ps1.executeQuery();
			if(rs.next()){
				String sql2 = "update tm set xh=?,sxm=?,zy=?,bj=? where id=?";
				ps2=conn.prepareStatement(sql2);
				ps2.setString(1, xh);
				ps2.setString(2, xm);
				ps2.setString(3, zy);
				ps2.setString(4, bj);
				ps2.setInt(5, tmid);
				ps2.executeUpdate();
				return 1;
			}
			else{
				return 0;
			}
		}finally {
			JdbcUtil.free(rs, ps1, null);
			JdbcUtil.free(null, ps2, conn);
			}
		
	}
	
	
}
