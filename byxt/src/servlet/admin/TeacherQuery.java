package servlet.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.PropertiesUtil;
import vo.CurrentUser;
import vo.Teacher;
import dao.TeacherDao;
@WebServlet("/admin/teacher/query")
public class TeacherQuery extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TeacherQuery() {
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

		TeacherDao teacherDao=new TeacherDao();
		  request.setCharacterEncoding("UTF-8");
		  HttpSession session=request.getSession();
			CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
			String adminid=currentUser.getAdminid();
		  String condition=" where adminid='"+adminid+"'";
			//String fieldName=request.getParameter("fieldName");
			String keyWord=request.getParameter("keyWord");
			
			if(keyWord!=null && !"".equals(keyWord)){
				condition+=" and (gh like '%"+keyWord+"%' or xm like '%"+keyWord+"%')";
			}
		 int pageSize=Integer.parseInt(PropertiesUtil.getValue("pr.properties", "teacherpagesize"));;
			int pageNo=1;
			try {
				pageNo = Integer.parseInt(request.getParameter("pageNo"));
			} catch (Exception e) {
				
			}
		 try {
			int recordCount=teacherDao.getRecordCount(condition);
			if(recordCount>0){
				 List<Teacher> teacherlist=teacherDao.query(condition,pageNo,pageSize);						
					int t1=recordCount%pageSize;;
					int t2=recordCount/pageSize;
					int pageCount=(t1==0?t2:t2+1);
					request.setAttribute("pageNo", pageNo);
					request.setAttribute("pageCount", pageCount);
					request.setAttribute("teacherList", teacherlist);
			}
			request.setAttribute("recordCount", recordCount);
			
			request.getRequestDispatcher("/admin/teacherAdmin.jsp").forward(request,response);
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
