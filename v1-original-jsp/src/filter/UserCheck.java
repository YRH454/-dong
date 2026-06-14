package filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vo.CurrentUser;
@WebFilter(
	    urlPatterns = {
	    	"/root/*",
	        "/admin/*", 
	        "/teacher/*", 
	        "/student/*"
	       
	    }
)
public class UserCheck  implements Filter{

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		
		HttpServletRequest request=(HttpServletRequest)arg0;
		HttpServletResponse response=(HttpServletResponse)arg1;
		String url=request.getServletPath();
		
		HttpSession session=request.getSession();
		CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
		response.setContentType("text/html;charset=UTF-8");
	
		
		if(currentUser==null){		
			PrintWriter out=response.getWriter();
			if(url.startsWith("/root"))
				 out.print("您还没有登录或会话已经过期,请<a href='/byxt/login_root.jsp' target='_top'>登录</a>");
			else
				 out.print("您还没有登录或会话已经过期,请<a href='/byxt/login.jsp' target='_top'>登录</a>");
		}
		else{
			if(url.startsWith(currentUser.getUrlprefix())){
				arg2.doFilter(request, response);
			}
			else{
				PrintWriter out=response.getWriter();				
				if(url.startsWith("/root"))
				 out.print("您还没有登录或会话已经过期,请<a href='/byxt/login_root.jsp' target='_top'>登录</a>");
				else
					 out.print("您还没有登录或会话已经过期,请<a href='/byxt/login.jsp' target='_top'>登录</a>");
			}
			
			
		}
		
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
