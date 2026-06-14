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
import dao.StudentDao;
import dao.TmDao;
@WebServlet("/teacher/tmupdate")
public class TmUpdate extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public TmUpdate() {
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
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String xh=request.getParameter("xh");
		String sxm=request.getParameter("sxm");
		String zy=request.getParameter("zy");
		String bj=request.getParameter("bj");
		String tmid=request.getParameter("tmid");
		StudentDao studentDao=new StudentDao();
		TmDao tmDao=new TmDao();
		HttpSession session=request.getSession();
		CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
		String adminid=currentUser.getAdminid();
		String condition=" where xh='"+xh+"' and xm='"+sxm+"' and zy='"+zy+"' and bj='"+bj+"' and adminid='"+adminid+"'";
		try {
			
			int c=studentDao.getRecordCount(condition);
			if(c==0){
				out.print(0);
			}
			else{
				int xhs=tmDao.getRecordCount(" where xh='"+xh+"' and id!="+tmid+" and adminid='"+adminid+"'");
				if(xhs>0){
					out.print(1);
				}
				else{
					out.print(2);
				}
			}
			
			//System.out.println(c);
			
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
