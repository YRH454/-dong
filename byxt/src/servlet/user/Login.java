package servlet.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.RootDao;
import dao.StudentDao;
import dao.TeacherDao;
import dao.UserDao;
import util.Encrypt;
import vo.CurrentUser;
import vo.Root;
import vo.Student;
import vo.Teacher;
import vo.User;
@WebServlet("/login")
public class Login extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Login() {
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
		String userid=request.getParameter("userid");
		String userpwd=request.getParameter("userpwd");
		String sf=request.getParameter("sf");	
		
		String dpy=request.getParameter("dp");
		String dp="",dpname="";
		if(dpy!=null){
			String[] dpys=dpy.split(",");
			dp=dpys[0];
			dpname=dpys.length>1?dpys[1]:"";
		}
		
		HttpSession session=request.getSession();
		CurrentUser currentUser=new CurrentUser();
		if("student".equals(sf)){
			StudentDao studentDao=new StudentDao();
			try {
				Student student=studentDao.findStudentByIdAdminid(userid,dp);			
				if(student==null){
					request.setAttribute("msg","用户名不存在");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
					
				}
				else if(student.getPwd().equals(Encrypt.MD5(userpwd))){
					currentUser.setId(student.getXh());
					currentUser.setMc(student.getXm());
					currentUser.setRole("学生");
					currentUser.setUrlprefix("/student");
					currentUser.setAdminid(dp);
					currentUser.setDp(dpname);
					session.setAttribute("currentUser",currentUser);
					response.sendRedirect("/byxt/student/index.jsp");
				}
				else{
					request.setAttribute("msg","密码不正确");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("teacher".equals(sf)){
			TeacherDao teacherDao=new TeacherDao();
			try {
				Teacher teacher=teacherDao.findTeacherByIdAdminid(userid,dp);			
				if(teacher==null){
					request.setAttribute("msg","用户名不存在");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
					
				}
				else if(teacher.getPwd().equals(Encrypt.MD5(userpwd))){
					currentUser.setId(teacher.getGh());
					currentUser.setMc(teacher.getXm());
					currentUser.setRole("教师");
					currentUser.setUrlprefix("/teacher");
					currentUser.setAdminid(dp);
					currentUser.setDp(dpname);
					session.setAttribute("currentUser",currentUser);
					response.sendRedirect("/byxt/teacher/index.jsp");
				}
				else{
					request.setAttribute("msg","密码不正确");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if("admin".equals(sf)){
			UserDao userDao=new UserDao();
			try {
				User user=userDao.findUserById(userid);			
				if(user==null){
					request.setAttribute("msg","用户名不存在");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
					
				}
				else if(user.getUserpwd().equals(Encrypt.MD5(userpwd))){
					if(user.getUserid().equals(dp)){
						currentUser.setId(user.getUserid());
						currentUser.setMc("");
						currentUser.setRole("管理员");
						
						currentUser.setUrlprefix("/admin");
						currentUser.setAdminid(user.getUserid());
						currentUser.setDp(user.getDp());
						session.setAttribute("currentUser",currentUser);
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String lastlogin=sdf.format(new Date());						
						userDao.modilastlogin(userid, lastlogin);
						response.sendRedirect("/byxt/admin/index.jsp");
					}
					else{
						request.setAttribute("msg","部门不正确");
						request.getRequestDispatcher("/login.jsp").forward(request,response);
					}
					
				}
				else{
					request.setAttribute("msg","密码不正确");
					request.getRequestDispatcher("/login.jsp").forward(request,response);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else if("root".equals(sf)){
			RootDao rootDao=new RootDao();
			try {
				Root root=rootDao.findUserById(userid);			
				if(root==null){
					request.setAttribute("msg","用户名不存在");
					request.getRequestDispatcher("/login_root.jsp").forward(request,response);
					
				}
				else if(root.getRootpwd().equals(Encrypt.MD5(userpwd))){
					currentUser.setId(root.getRootid());
					currentUser.setMc("");
					currentUser.setRole("root");
					currentUser.setUrlprefix("/root");
					session.setAttribute("currentUser",currentUser);
					response.sendRedirect("/byxt/root/index.jsp");
				}
				else{
					request.setAttribute("msg","密码不正确");
					request.getRequestDispatcher("/login_root.jsp").forward(request,response);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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
