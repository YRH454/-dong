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
import vo.Student;

public class StudentDao {
public Student findStudentByIdAdminid(String xh,String adminid) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Student student=null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from student where xh=? and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, xh);
			ps.setString(2,adminid);
			rs = ps.executeQuery();
			if (rs.next()) {
				student=new Student();
				student.setId(rs.getInt("id"));
				student.setXh(rs.getString("xh"));
				student.setXm(rs.getString("xm"));
				student.setPwd(rs.getString("pwd"));
				student.setZy(rs.getString("zy"));
				student.setBj(rs.getString("bj"));
				student.setEmail(rs.getString("email"));
				student.setPhone(rs.getString("phone"));
				student.setQq(rs.getString("qq"));
				student.setAdminid(rs.getString("adminid"));
			    
			}
		} finally {
			JdbcUtil.free(rs, ps, conn);
		}
		return student;
		
	}
	public void modipwd(String xh,String studentpwd,String adminid) throws Exception{
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update student set pwd=? where xh=? and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, studentpwd);
			ps.setString(2, xh);
			ps.setString(3,adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
		
	}
	public void add(Student student) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "insert into student (xh,xm,pwd,zy,bj,email,phone,qq,adminid)  values (?,?,?,?,?,?,?,?,?) ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, student.getXh());
			ps.setString(2,student.getXm());
			ps.setString(3,student.getPwd());
			ps.setString(4, student.getZy());
			ps.setString(5, student.getBj());
			ps.setString(6, student.getEmail());
			ps.setString(7, student.getPhone());
			ps.setString(8, student.getQq());
			ps.setString(9,student.getAdminid());
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}

	public void update(Student student) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			//System.out.println(student.getXh());
			String sql = "update student set xm=?,zy=?,bj=?,email=?,phone=?,qq=? where xh=?  and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, student.getXm());
			ps.setString(2, student.getZy());
			ps.setString(3,student.getBj());
			ps.setString(4,student.getEmail());
			ps.setString(5, student.getPhone());
			ps.setString(6, student.getQq());
			ps.setString(7, student.getXh());
			ps.setString(8, student.getAdminid());
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void delete(int studentid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "delete from student where id=? ";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, studentid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void resetpwd(String xh,String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update student set pwd=? where xh=? and adminid=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, Encrypt.MD5(xh));
			ps.setString(2, xh);
			ps.setString(3,adminid);
			ps.executeUpdate();
		} finally {
			JdbcUtil.free(null, ps, conn);
		}
	}
	public void qingkong(String adminid) throws Exception {
		Connection conn = null;
		PreparedStatement ps1 = null;
	//	PreparedStatement ps2 = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql1 = "delete from student  where adminid=?";
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1,adminid);
		    ps1.executeUpdate();
		    
		 //   String sql2 = "alter table student auto_increment=1  ";
		//	ps2 = conn.prepareStatement(sql2);
		  //  ps2.executeUpdate();
		    
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
		String sql = "insert into student (xh,xm,pwd,zy,bj,email,phone,qq,adminid)  values (?,?,?,?,?,?,?,?,?) ";
		ps=conn.prepareStatement(sql);
		workbook = Workbook.getWorkbook(excelPath);
		sheet = workbook.getSheet(sheetNo-1);	
		int r = sheet.getRows();	
		//int c=sheet.getColumns();
		//System.out.println(r+","+c);
		for (int i = 1; i < r; i++) {
			
			  for(int j=1;j<=9;j++){
				  if(j==1||j==2){
					  ps.setString(j, sheet.getCell(j-1,i).getContents().trim());
				  }
				  if(j==3){
				  ps.setString(3, Encrypt.MD5(sheet.getCell(0,i).getContents().trim()));
				  }
				  if(j>=4&&j<=8){
					  ps.setString(j, sheet.getCell(j-2,i).getContents().trim());
				  }
				  else if(j==9){
					  ps.setString(j, adminid);
				  }
			  }
				 
		   ps.addBatch();
			  
	   }
		
		  try {
			  ps.executeBatch();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
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
			String sql = "select xh,xm,zy,bj,email,phone,qq from student "+condition;
			ps=conn.prepareStatement(sql);
		    rs=ps.executeQuery();
			 wwb=Workbook.createWorkbook(excelPath);
			 ws=wwb.createSheet("sheet1",0);
			 String[] titles={"学号","姓名","专业","班级","邮箱","电话","QQ"};
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
				String sql = "select count(*) from student "+condition;
				pst = conn.prepareStatement(sql);
				rs=pst.executeQuery();
				rs.next();
				recordcount=rs.getInt(1);
				
			}finally {
				JdbcUtil.free(rs, pst, conn);
			}
			return recordcount;
		 
	}
	public List<Student> query(String condition,int pageNo,int pageSize) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> studentList=new ArrayList<Student>();
		int startrecno=(pageNo-1)*pageSize;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select * from student "+condition+" order by zy,bj  limit ?,? ";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, startrecno);
			ps.setInt(2, pageSize);
			rs=ps.executeQuery();
			while(rs.next()){
			   Student student=new Student();
			   student.setId(rs.getInt("id"));
				student.setXh(rs.getString("xh"));
				student.setXm(rs.getString("xm"));
				student.setPwd(rs.getString("pwd"));
				student.setZy(rs.getString("zy"));
				student.setBj(rs.getString("bj"));
				student.setEmail(rs.getString("email"));
				student.setPhone(rs.getString("phone"));
				student.setQq(rs.getString("qq"));
				
			   studentList.add(student);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return studentList;
	}
	public List<Student> wxt(String adminid) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Student> studentList=new ArrayList<Student>();
		
		try {
			conn = JdbcUtil.getConnection();
			String sql = "select student.xh,student.xm,student.zy,student.bj,student.email,student.phone,student.qq from student left join tm on student.xh=tm.xh and student.adminid=tm.adminid where student.adminid=? and tm.xh is null ";
			ps=conn.prepareStatement(sql);	
			ps.setString(1,adminid);
			rs=ps.executeQuery();
			while(rs.next()){
			   Student student=new Student();
			   
				student.setXh(rs.getString(1));
				student.setXm(rs.getString(2));
				
				student.setZy(rs.getString(3));
				student.setBj(rs.getString(4));
				student.setEmail(rs.getString(5));
				student.setPhone(rs.getString(6));
				student.setQq(rs.getString(7));
				
			   studentList.add(student);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return studentList;
	}

}
