package servlet.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import dao.TeacherDao;
import vo.CurrentUser;
@WebServlet("/admin/teacher/daoru")
public class TeacherDaoru extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TeacherDaoru() {
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

		TeacherDao teacherDao=new TeacherDao();
		String saveDirectory =this.getServletContext().getRealPath("/file");
		HttpSession session=request.getSession();
		CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
		String adminid=currentUser.getAdminid();
		int maxPostSize =5  * 1024 * 1024 ;  //总上传大小限制：3M
		//FileRenamePolicy policy =(FileRenamePolicy)new DefaultFileRenamePolicy(); 
		MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,"utf-8"); 
		File excelFile=multi.getFile("file1");
		if(excelFile!=null){
			try {
				teacherDao.importFromExcel(excelFile,Integer.parseInt(multi.getParameter("sheetno")),adminid);
				excelFile.delete();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.sendRedirect("/byxt/admin/teacher/query");
	
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
