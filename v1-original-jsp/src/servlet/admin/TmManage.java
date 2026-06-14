package servlet.admin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.PropertiesUtil;
import vo.CurrentUser;
import vo.Tm;

import com.oreilly.servlet.ServletUtils;

import dao.TmDao;
@WebServlet("/admin/tmmanage")
public class TmManage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	TmDao tmDao=new TmDao();
	public TmManage() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String action=request.getParameter("action");
		//System.out.println(action);
		HttpSession session=request.getSession();
		CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
		String adminid=currentUser.getAdminid();
		if("query".equals(action)){
			this.query(request,response,adminid);
		}		
		else if("delete".equals(action)){
			this.delete(request,response);
		}
		
		else if("daochu".equals(action)){
			this.daochu(request,response,adminid);
		}
		else if("qk".equals(action)){
			this.qk(request,response,adminid);
		}
	}
	private void query(HttpServletRequest request, HttpServletResponse response,String adminid) {
		// TODO Auto-generated method stub
		  String condition=" where adminid='"+adminid+"'";
			//String fieldName=request.getParameter("fieldName");
			String keyWord=request.getParameter("keyWord");
			
			if(keyWord!=null && !"".equals(keyWord)){
				condition+=" and (txm like '%"+keyWord+"%' or tm like '%"+keyWord+"%' or zy like '%"+keyWord+"%' or bj like '%"+keyWord+"%')";
			}
		 int pageSize=Integer.parseInt(PropertiesUtil.getValue("pr.properties", "tmpagesize"));;
			int pageNo=1;
			try {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			} catch (Exception e) {
				
			}
		 try {
			int recordCount=tmDao.getRecordCount(condition);
			if(recordCount>0){
				 List<Tm> tmlist=tmDao.query(condition,pageNo,pageSize);						
					int t1=recordCount%pageSize;;
					int t2=recordCount/pageSize;
					int pageCount=(t1==0?t2:t2+1);
					request.setAttribute("pageNo", pageNo);
					request.setAttribute("pageCount", pageCount);
					request.setAttribute("tmList", tmlist);
			}
			request.setAttribute("recordCount", recordCount);
			
			request.getRequestDispatcher("/admin/tmAdmin.jsp").forward(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		  try {
				tmDao.delete(Integer.parseInt(request.getParameter("id")));
				response.sendRedirect("/byxt/admin/tmmanage?action=query");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void qk(HttpServletRequest request, HttpServletResponse response,String adminid) {
		// TODO Auto-generated method stub
		  try {
				tmDao.qingkong(adminid);
				response.sendRedirect("/byxt/admin/tmmanage?action=query");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private void daochu(HttpServletRequest request, HttpServletResponse response,String adminid) throws ServletException, IOException{
		// TODO Auto-generated method stub
		
		
			String saveDirectory =this.getServletContext().getRealPath("/file");
			
			String fileName="选题结果.xls";
			String condition=" where tm.adminid='"+adminid+"'";
			try {
				tmDao.exportToExcel2(new File(saveDirectory+"/"+fileName), condition);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				
				  String isofilename = new String(fileName.getBytes("gbk"),"iso-8859-1");
	              response.setContentType("application/octet-stream");
	             response.setHeader("Content-Disposition","attachment; filename=" + isofilename);
	             ServletOutputStream out = null;
	             out = response.getOutputStream();
	             ServletUtils.returnFile(saveDirectory+"/"+fileName,out);//下载文件
	             new File(saveDirectory+"/"+fileName).delete();
	             
			} catch (UnsupportedEncodingException ex) {//iso8559_1编码异常
	         ex.printStackTrace();
	        }catch(IOException e){//getOutputStream()异常。
	          e.printStackTrace();
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
