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
	//--------------------------------------------缂傚倸鍊搁崐鐑芥嚄閼搁潧鍨旈悗闈涙啞椤洟鏌￠崶銉ョ仼缂佺姵鐗曢埞鎴︽偐閸欏鎮欓悗鐟版啞缁诲啯绌辨繝鍥舵晬婵犙呭亾鐎氭稒绻涚€电甯堕柛濠傛健瀵鏁撻悩鑼紲濠电姴鐏氶崝鏍礊閸垻纾?
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
			 String[] titles={"TeacherID","TeacherName","Topic","Remark","StudentID","StudentName","Major","Class"};
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
			 String[] titles={"TeacherID","TeacherName","Topic","Remark","StudentID","StudentName","Major","Class","email","phone","qq"};
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
	
	//--------------------------------------------闂傚倸鍊峰ù鍥ь浖閵娾晜鍊块柨鏇炲€归崑锟犳煏婢诡垰鎳愰悾鍫曟⒑闁偛鑻晶鎵磼鏉堛劌娴€殿噮鍣ｉ獮瀣攽閸粎鏁鹃梻鍌欑閹碱偊鎯夋總绋跨獥閹兼番鍔岄悡?
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
	public void update2(String xh,String xm,String zy,String bj,String adminid) throws Exception {//闂傚倷娴囬褏鈧稈鏅犲畷妯肩磼濡湱绠氶梺鎼炲労閸撴瑩寮告笟鈧弻鐔煎箚瑜滈崵鐔兼煃閻熸壆校闁靛洤瀚板顕€宕堕‖顔芥崌閺岋繝宕遍埡浣哥睄闂佽鍠氶、濠囁囬弻銉﹀€垫慨妯煎帶瀵喚鈧鍠楅幃鍌炲极閹剧粯鍋愰柤鑲╃礋閺囥垺鐓欓柛蹇撳悑閸庢鏌涢妸銉э紞缂侇喖鐗婂鍕箾閵忥紕娲撮柟顔ㄥ洤閱囨い鎰╁€曟慨鐑樼節閻㈤潧浠╃€殿喗鎹囧畷锟犲礃閸欏倹绋掔粭鐔煎焵椤掑嫬鏄ラ柍褜鍓氶妵鍕箳閹存績鍋撹ぐ鎺戠柈闁告繂濞婅ぐ鎺撴櫜闁搞儱澧庨崝鎼佹⒑閸濆嫭鍣洪柣鐔叉櫊瀵鏁撻悩鑼€為悷婊冪箻楠炴鎮╃紒妯煎幐闁诲繒鍋為弸濠氬绩閼姐倗纾?
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
	public void update3(String gh,String txm,String adminid) throws Exception {//闂傚倸鍊峰ù鍥ь浖閵娾晜鍊块柨鏇炲€归崑锟犳煏婢诡垰鎳愰悾鍫曟⒑闁偛鑻晶顕€鎳ｉ幇鐗堢厱闁靛鍔嶇涵楣冩煛鐎ｎ剙鏋涢柡灞稿墲閹峰懘鎸婃径澶嬬潖闂備礁婀遍～瀣炊瑜忛ˇ鏉款渻閵堝棗绗傞柤瀹犲煐缁傚秷銇愰幒鎾嫼闂佸憡绋戦敃銊︾珶濮椻偓閺屾盯骞嬮悩铏彧濠碘€冲级閸旀洟鍩為幋锕€鐐婂瀣捣閻撴垶绻濈喊妯活潑闁搞劋鍗冲畷銉р偓锝庡墰閻滅粯淇婇妶鍛櫤闁抽攱甯￠弻娑㈠Ψ椤栨粌鍩屾繛瀛樼矒缁犳牠寮婚敐澶嬪亜闁告稑锕﹂敍娑氱磽娴ｆ彃浜炬繛鎾村焹閸嬫挾鈧娲﹂崹鍫曞春閳ь剚銇勯幒鎴濐仾闁绘挻鐩弻娑㈠Ψ閹存繃鍣洪柟鎻掋偢濮婃椽宕崟顔碱伃闂佹寧娲忛崕宕囧垝?
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
					tmEx.setStatus(rs.getInt("status"));
					tmEx.setChoice(rs.getInt("choice"));
					tmEx.setReason(rs.getString("reason"));
					tmEx.setFile_path(rs.getString("file_path"));
				if(!"".equals(rs.getString("xh")) && adminid != null && !adminid.isEmpty()){
					StudentDao studentDao=new StudentDao();
					TeacherDao teacherDao=new TeacherDao();
					Student st=studentDao.findStudentByIdAdminid(rs.getString("xh"),adminid);
					Teacher tc=teacherDao.findTeacherByIdAdminid(rs.getString("gh"),adminid);
					if(st!=null){ tmEx.setSemail(st.getEmail()); tmEx.setSphone(st.getPhone()); tmEx.setSqq(st.getQq()); }
					if(tc!=null){ tmEx.setTzhicheng(tc.getZhicheng()); tmEx.setTbgdd(tc.getBgdd()); tmEx.setTemail(tc.getEmail()); tmEx.setTphone(tc.getPhone()); tmEx.setTqq(tc.getQq()); }
					
				}
				
			   tmList.add(tmEx);
			}
		}finally {JdbcUtil.free(rs, ps, conn);}
		return tmList;
	}
	//闂傚倷娴囬褏鈧稈鏅犲畷妯肩磼濡湱绠氶梺鎼炲労閸撴瑩寮告笟鈧弻鐔煎箚瑜嶉。鎶芥煕濡や胶顣查柕鍥у瀵粙濡歌婵洭姊虹紒妯诲鞍闁荤噦濡囬幑?
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
	public void qcjl(String sno,String adminid) throws Exception {//婵犵數濮烽弫鎼佸磻閻愬搫绠伴柟闂寸缁犵姵淇婇婵勨偓鈧柡瀣Ч楠炴牠骞栭鐔虹獧缂備焦鍞荤徊鐐┍婵犲浂鏁嶆慨姗嗗墯閸婎垳绱撴担鐟板妞ゃ劌锕獮鍐喆閸曨剙顎撻柣鐔哥懃鐎氼噣宕欓敍鍕＝?
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
	
	

	public static synchronized int xtWithStatus(int tmid,String xh,String xm,String zy,String bj,String reason,int choice) throws Exception{
		Connection conn = null;
		PreparedStatement ps1 = null, ps2 = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtil.getConnection();
			String sql1 = "select * from tm where (xh='' or status=3) and id="+tmid;
			ps1=conn.prepareStatement(sql1);
			rs=ps1.executeQuery();
			if(rs.next()){
				String sql2 = "update tm set xh=?,sxm=?,zy=?,bj=?,status=1,reason=?,choice=? where id=?";
				ps2=conn.prepareStatement(sql2);
				ps2.setString(1, xh); ps2.setString(2, xm); ps2.setString(3, zy);
				ps2.setString(4, bj); ps2.setString(5, reason); ps2.setInt(6, choice);
				ps2.setInt(7, tmid);
				ps2.executeUpdate();
				return 1;
			} else { return 0; }
		}finally { JdbcUtil.free(rs, ps1, null); JdbcUtil.free(null, ps2, conn); }
	}

	public void confirmStudent(int tmid) throws Exception {
		Connection conn = null; PreparedStatement ps = null;
		try { conn = JdbcUtil.getConnection(); ps = conn.prepareStatement("update tm set status=2 where id=?"); ps.setInt(1, tmid); ps.executeUpdate(); }
		finally { JdbcUtil.free(null, ps, conn); }
	}

	public void rejectStudent(int tmid) throws Exception {
		Connection conn = null; PreparedStatement ps = null;
		try { conn = JdbcUtil.getConnection(); ps = conn.prepareStatement("update tm set status=3 where id=?"); ps.setInt(1, tmid); ps.executeUpdate(); }
		finally { JdbcUtil.free(null, ps, conn); }
	}
}