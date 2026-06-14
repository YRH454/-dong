package servlet.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.Encrypt;
import util.PropertiesUtil;
import vo.CurrentUser;
import vo.Student;

import com.oreilly.servlet.MultipartRequest;

import dao.StudentDao;
@WebServlet("/admin/studentmanage")
public class StudentManage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	StudentDao studentDao=new StudentDao();
	public StudentManage() {
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
		HttpSession session=request.getSession();
		CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
		String adminid=currentUser.getAdminid();
		//System.out.println(action);
		if("query".equals(action)){
			this.query(request,response,adminid);
		}
		else if("add".equals(action)){
			this.add(request,response,adminid);
		}
		else if("update".equals(action)){
			this.update(request,response,adminid);
		}
		else if("delete".equals(action)){
			this.delete(request,response);
		}
		else if("resetpwd".equals(action)){
			this.resetpwd(request,response,adminid);
		}
		else if("daoru".equals(action)){
			this.daoru(request,response,adminid);
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
				condition+=" and (xh like '%"+keyWord+"%' or xm like '%"+keyWord+"%' or zy like '%"+keyWord+"%' or bj like '%"+keyWord+"%')";
			}
		 int pageSize=Integer.parseInt(PropertiesUtil.getValue("pr.properties", "studentpagesize"));;
			int pageNo=1;
			try {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			} catch (Exception e) {
				
			}
		 try {
			int recordCount=studentDao.getRecordCount(condition);
			if(recordCount>0){
				 List<Student> studentlist=studentDao.query(condition,pageNo,pageSize);						
					int t1=recordCount%pageSize;;
					int t2=recordCount/pageSize;
					int pageCount=(t1==0?t2:t2+1);
					request.setAttribute("pageNo", pageNo);
					request.setAttribute("pageCount", pageCount);
					request.setAttribute("studentList", studentlist);
			}
			request.setAttribute("recordCount", recordCount);
			
			request.getRequestDispatcher("/admin/studentAdmin.jsp").forward(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void add(HttpServletRequest request, HttpServletResponse response,String adminid) {
		// TODO Auto-generated method stub
		
		try {
			
			Student student = new Student();
		    student.setXh(request.getParameter("xh"));
		    student.setXm(request.getParameter("xm"));
		    student.setPwd(Encrypt.MD5(request.getParameter("xh")));
		    student.setZy(request.getParameter("zy"));
		    student.setEmail(request.getParameter("email"));
		    student.setPhone(request.getParameter("phone"));
		    student.setQq(request.getParameter("qq"));
		    student.setBj(request.getParameter("bj"));
		    student.setAdminid(adminid);
			studentDao.add(student);
		
		response.sendRedirect("/byxt/admin/studentmanage?action=query");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	private void update(HttpServletRequest request, HttpServletResponse response,String adminid) {
		// TODO Auto-generated method stub
		
		try {
			
			Student student = new Student();
			student.setId(Integer.parseInt(request.getParameter("studentid")));
		    student.setXh(request.getParameter("xh"));
		    student.setXm(request.getParameter("xm"));
		   
		    student.setZy(request.getParameter("zy"));
		    student.setEmail(request.getParameter("email"));
		    student.setPhone(request.getParameter("phone"));
		    student.setQq(request.getParameter("qq"));
		    student.setBj(request.getParameter("bj"));
		    student.setAdminid(adminid);
			studentDao.update(student);
		
			response.sendRedirect("/byxt/admin/studentmanage?action=query");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		  try {
				studentDao.delete(Integer.parseInt(request.getParameter("id")));
				response.sendRedirect("/byxt/admin/studentmanage?action=query");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private void resetpwd(HttpServletRequest request, HttpServletResponse response,String adminid) throws ServletException, IOException{
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		  try {
			studentDao.resetpwd(request.getParameter("xh"),adminid);
			out.print("密码重置成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void qk(HttpServletRequest request, HttpServletResponse response,String adminid) {
		// TODO Auto-generated method stub
		  try {
				studentDao.qingkong(adminid);
				response.sendRedirect("/byxt/admin/studentmanage?action=query");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private void daoru(HttpServletRequest request, HttpServletResponse response,String adminid) throws ServletException, IOException{
		// TODO Auto-generated method stub
		
		String saveDirectory =this.getServletContext().getRealPath("/file");
		int maxPostSize =5  * 1024 * 1024 ;  //总上传大小限制：3M
		//FileRenamePolicy policy =(FileRenamePolicy)new DefaultFileRenamePolicy(); 
		MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,"utf-8"); 
		File excelFile=multi.getFile("file1");
		if(excelFile!=null){
			try {
				studentDao.importFromExcel(excelFile,Integer.parseInt(multi.getParameter("sheetno")),adminid);
				excelFile.delete();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.sendRedirect("/byxt/admin/studentmanage?action=query");
	
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
