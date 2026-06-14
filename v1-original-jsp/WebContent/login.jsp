<%@ page  pageEncoding="UTF-8" import="java.util.*,dao.UserDao,vo.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>选题系统--用户登录</title>
<meta name="title" content="选题系统，毕业设计选题系统"/>
<meta name="keywords" content="选题系统，毕业设计选题系统"/>
<link rel="stylesheet" href="css/xgxt_login.css" />
<script src="${pageContext.request.contextPath}/js/jquery1.js"></script>
<script>
$(function(){
 $("#dl").click(function(){
   if($("#userid").val().trim()==''||$("#userpwd").val().trim()==''){
     alert("用户名或密码不能为空");
   }
   else{
     $("#dlform").submit();
   }
 });
}
);
function qingkong(){
  $("#msg").html("");
}
</script>

</head>
<body>
<%
UserDao userDao=new UserDao();

List<User> userList=userDao.query();
pageContext.setAttribute("userList",userList);

%>
<div id="header">
	<div class="header_title">
		<span class="title_con">选题系统</span>
	</div>
</div>

<div id="content">
	<center>
		<div class="con">
		<div class="con_title">
			<span class="con_title_sp">系统登录</span>
		</div>
		<form action="/byxt/login" method="post" id="dlform">
		<div class="con_panel">
			<div class="con_input">
				<input type="text" name="userid" id="userid" onclick="qingkong()" placeholder="请输入用户名"  style="width:250px"/>
			</div>
			<div class="con_input">
				<input type="password" name="userpwd" id="userpwd" onclick="qingkong()" placeholder="请输入密码" style="width:250px"/>
			</div>
			<div class="con_select">
				<input type="radio" name="sf" value="student" checked />学生
				<input type="radio" name="sf" value="teacher" />教师
				<input type="radio" name="sf" value="admin" />管理员
				&nbsp;选项:	<select name="dp">
			<c:forEach items="${userList}" var="user">
			 <option value="${user.userid},${user.dp}">${user.dp}</option>
			 </c:forEach>
			</select>
			</div>
			
			<input type="button" value="登    录" class="submit-btn" id="dl"/>
		</div>
		</form>
		<div id="msg" style="color:red">${msg}</div>
	</div>
	</center>
</div>

<div style="text-align:center;margin-top:5px"><%@include file="count2.jsp"%></div>

</body>
</html>
