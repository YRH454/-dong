package servlet.teacher;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vo.CurrentUser;
import vo.Teacher;
import dao.TeacherDao;
import dao.TmDao;
@WebServlet("/teacher/modizl")
public class Modizl extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Modizl() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
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
		HttpSession session=request.getSession();
		CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
		String adminid=currentUser.getAdminid();
		String gh=request.getParameter("gh");
		String xm=request.getParameter("xm");
		Teacher teacher=new Teacher();
		teacher.setGh(gh);
		teacher.setXm(xm);
		teacher.setZhicheng(request.getParameter("zhicheng"));
		teacher.setEmail(request.getParameter("email"));
		teacher.setPhone(request.getParameter("phone"));
		teacher.setQq(request.getParameter("qq"));
		teacher.setBgdd(request.getParameter("bgdd"));
		teacher.setAdminid(adminid);
		TeacherDao teacherDao=new TeacherDao();		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		try {
			teacherDao.update_t(teacher);
			TmDao tmDao=new TmDao();
			tmDao.update3(gh, xm, adminid);
			out.print("资料修改成功");
			
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
