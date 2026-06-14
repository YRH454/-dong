package servlet.student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vo.CurrentUser;
import vo.Student;
import dao.StudentDao;
import dao.TmDao;
@WebServlet("/student/modizl")
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
		Student student=new Student();
		String xh=request.getParameter("xh");
		String xm=request.getParameter("xm");
		String zy=request.getParameter("zy");
		String bj=request.getParameter("bj");
		student.setXh(xh);
		student.setXm(xm);
		student.setZy(zy);
		student.setEmail(request.getParameter("email"));
		student.setPhone(request.getParameter("phone"));
		student.setQq(request.getParameter("qq"));
		student.setBj(bj);
		student.setAdminid(adminid);
		StudentDao studentDao=new StudentDao();	
		TmDao tmDao=new TmDao();
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		try {
			studentDao.update(student);
			tmDao.update2(xh, xm, zy, bj, adminid);
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
