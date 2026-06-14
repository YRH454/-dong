<%@ page  pageEncoding="UTF-8" import="dao.UserDao,vo.*"%> 

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
 
<script>
function modidp(){
var dp=document.getElementById("dp").value;
if(dp==''){
  alert("部门名称不能为空");
}
else{
 if(confirm("确实要修改吗")){
   document.getElementById("form1").submit();
 }
}
 
}
</script>
  </head>
  
  <body>
   <%
   request.setCharacterEncoding("UTF-8");
    String dp=request.getParameter("dp");
   if(dp!=null){
	 UserDao userDao=new UserDao();
	 String userid=request.getParameter("userid");
	 User user=userDao.findUserByDp(dp);
	 if(user==null||user!=null&&user.getUserid().equals(userid)){
		 userDao.modidp(userid, dp);
		 CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
		 currentUser.setDp(dp);
		 response.sendRedirect("/byxt/admin/index.jsp");
	 }
	 else{
		 response.sendRedirect("/byxt/admin/index.jsp?msg=failure");
	 }
	 
   }
  
     %>
  <h3>修改选项名称</h3>
    <form id="form1"  method="post" target="_top">
    选项名称：<input type="text" name="dp" id="dp" value="${sessionScope.currentUser.dp}" size="25"><br>
    <input type="hidden" name="userid" value="${sessionScope.currentUser.id}">  
     <input type="button" value="确定" onclick="modidp()"/>
    </form>
   
  </body>
</html>
