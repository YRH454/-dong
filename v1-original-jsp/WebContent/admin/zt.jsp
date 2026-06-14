<%@ page  pageEncoding="UTF-8" import="dao.ZtDao,vo.CurrentUser"%>  

<html>
  <head>
  <style>
  body{
  text-align:center;
  margin-top:5%;
 font-family: 微软雅黑;
 font-size:18px;
 line-height:2;
  }
 </style>
 

  </head>
  
  <body> 
 
  <%
   ZtDao ztDao=new ZtDao();
	CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
	String adminid=currentUser.getAdminid();
  if(request.getParameter("zt")!=null){
    ztDao.update(Integer.parseInt(request.getParameter("zt")),adminid);
  }
 
  int zt=ztDao.query(adminid);  
   %>
    说明：状态值0表示学生还不能选题，状态值1表示可以选题。 目前状态值为<%=zt%>。
   <form action="" method="post">
     <input type="text" value="<%=zt%>" name="zt"/>
     <input type="submit" value="修改">
   </form>
  </body>
</html>
