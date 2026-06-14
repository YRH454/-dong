package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import util.Encrypt;
import util.JdbcUtil;
import vo.Teacher;

public class TeacherDao {
public Teacher findTeacherByIdAdminid(String gh,String adminid) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Teacher teacher=null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from teacher where gh=? and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, gh);ps.setString(2,adminid);
			rs = ps.executeQuery();
			if (rs.next()) {
				teacher=new Teacher();
				teacher.setId(rs.getInt("id"));
				teacher.setGh(rs.getString("gh"));
				teacher.setXm(rs.getString("xm"));
				teacher.setPwd(rs.getString("pwd"));
				teacher.setZhicheng(rs.getString("zhicheng"));
				teacher.setEmail(rs.getString("email"));
				teacher.setPhone(rs.getString("phone"));
				teacher.setQq(rs.getString("qq"));
				teacher.setBgdd(rs.getString("bgdd"));
				teacher.setShangxian(rs.getInt("shangxian"));
				teacher.setAdminid(rs.getString("adminid"));
				
			    
			}
		} finally {
			JdbcUtil.free(rs, ps, conn);
		}
		return teacher;
		
	}
	public void modipwd(String gh,String teacherpwd,String adminid) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update teacher set pwd=? where gh=? and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, teacherpwd);
			ps.setString(2, gh);
			ps.setString(3,adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
		
	}
	public void add(Teacher teacher) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "insert into teacher (gh,xm,pwd,zhicheng,email,phone,qq,bgdd,shangxian,adminid)  values (?,?,?,?,?,?,?,?,?,?) ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, teacher.getGh());
			ps.setString(2,teacher.getXm());
			ps.setString(3,teacher.getPwd());
			ps.setString(4, teacher.getZhicheng());
			ps.setString(5, teacher.getEmail());
			ps.setString(6, teacher.getPhone());
			ps.setString(7, teacher.getQq());
			ps.setString(8, teacher.getBgdd());
			ps.setInt(9, teacher.getShangxian());
			ps.setString(10, teacher.getAdminid());
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}

	public void update_a(Teacher teacher) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update teacher set xm=?,zhicheng=?,email=?,phone=?,qq=?,bgdd=?,shangxian=? where gh=?  and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, teacher.getXm());
			ps.setString(2, teacher.getZhicheng());
			ps.setString(3,teacher.getEmail());
			ps.setString(4,teacher.getPhone());
			ps.setString(5, teacher.getQq());
			ps.setString(6, teacher.getBgdd());
			ps.setInt(7,teacher.getShangxian());
			ps.setString(8, teacher.getGh());
			ps.setString(9,teacher.getAdminid());
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void update_t(Teacher teacher) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update teacher set xm=?,zhicheng=?,email=?,phone=?,qq=?,bgdd=? where gh=?  and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, teacher.getXm());
			ps.setString(2, teacher.getZhicheng());
			ps.setString(3,teacher.getEmail());
			ps.setString(4,teacher.getPhone());
			ps.setString(5, teacher.getQq());
			ps.setString(6, teacher.getBgdd());

			ps.setString(7, teacher.getGh());
			ps.setString(8, teacher.getAdminid());
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void delete(int teacherid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "delete from teacher where id=? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, teacherid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void resetpwd(String gh,String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update teacher set pwd=? where gh=? and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, Encrypt.MD5(gh));
			ps.setString(2, gh);
			ps.setString(3,adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void qingkong(String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement ps1 = null;
		//PreparedStatement ps2 = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql1 = "delete from teacher  where adminid=?";
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1,adminid);
		    ps1.executeUpdate();
		    
		    //String sql2 = "alter table teacher auto_increment=1  ";
			//ps2 = conn.prepareStatement(sql2);
		   // ps2.executeUpdate();
		    
		} finally {
			JdbcUtil.free(null, ps1, null);
			//JdbcUtil.free(null, ps2, conn);
		}
    }
	public void importFromExcel(File excelPath,int sheetNo,String adminid) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		Workbook workbook = null;
		Sheet sheet=null;
		
		try {
		conn = JdbcUtil.getConnection();
		String sql = "insert into teacher (gh,xm,pwd,zhicheng,email,phone,qq,bgdd,shangxian,adminid)  values (?,?,?,?,?,?,?,?,?,?) ";
		ps=conn.prepareStatement(sql);
		workbook = Workbook.getWorkbook(excelPath);
		sheet = workbook.getSheet(sheetNo-1);	
		int r = sheet.getRows();	
		//int c=sheet.getColumns();
		//System.out.println(r+","+c);
		for (int i = 1; i < r; i++) {
			
			  for(int j=1;j<=10;j++){
				  if(j==1||j==2){
					  ps.setString(j, sheet.getCell(j-1,i).getContents().trim());
				  }
				  if(j==3){
				  ps.setString(3, Encrypt.MD5(sheet.getCell(0,i).getContents().trim()));
				  }
				  if(j>=4&&j<=9){
					  ps.setString(j, sheet.getCell(j-2,i).getContents().trim());
				  }
				  else if(j==10){
					  ps.setString(10, adminid);
				  }
			  }
				 
		   ps.addBatch();
			  
	   }
		
		  try {
			  ps.executeBatch();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	
	}finally {

		if(workbook!=null)  workbook.close();
		JdbcUtil.free(null, ps, conn);
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
			String sql = "select gh,xm,zhicheng,email,phone,qq,bgdd from teacher "+condition;
			ps=conn.prepareStatement(sql);
		    rs=ps.executeQuery();
			 wwb=Workbook.createWorkbook(excelPath);
			 ws=wwb.createSheet("sheet1",0);
			 String[] titles={"工号","姓名","职称","邮箱","电话","QQ","办公地点"};
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
	

	public int getRecordCount(String condition) throws Exception{
		 
		 Connection conn = null;
		 PreparedStatement pst = null;
		 ResultSet rs = null;
		int recordcount=0;
		try {
				conn = JdbcUtil.getConnection();
				String sql = "select count(*) from teacher "+condition;
				pst = conn.prepareStatement(sql);
				rs=pst.executeQuery();
				rs.next();
				recordcount=rs.getInt(1);
				
			}finally {
				JdbcUtil.free(rs, pst, conn);
			}
			return recordcount;
		 
	}
	public List<Teacher> query(String condition,int pageNo,int pageSize) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Teacher> teacherList=new ArrayList<Teacher>();
		int startrecno=(pageNo-1)*pageSize;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from teacher "+condition+" order by gh  limit ?,? ";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, startrecno);
			ps.setInt(2, pageSize);
			rs=ps.executeQuery();
			while(rs.next()){
			   Teacher teacher=new Teacher();
			   teacher.setId(rs.getInt("id"));
				teacher.setGh(rs.getString("gh"));
				teacher.setXm(rs.getString("xm"));
				teacher.setPwd(rs.getString("pwd"));
				teacher.setZhicheng(rs.getString("zhicheng"));
				teacher.setEmail(rs.getString("email"));
				teacher.setPhone(rs.getString("phone"));
				teacher.setQq(rs.getString("qq"));
				teacher.setBgdd(rs.getString("bgdd"));
				teacher.setShangxian(rs.getInt("shangxian"));
			   teacherList.add(teacher);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return teacherList;
	}
	

}
