<%@ page pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>选题系统-学生端</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mobile-app.css" />
<style>
.app-sidebar .side-item:active,.app-sidebar .side-item:hover{background:#E8F5E9;border-left-color:#4CAF50;color:#2E7D32}
</style>
<script>
function toggleSidebar(){
  document.getElementById('sidebar').classList.toggle('open');
  document.getElementById('overlay').classList.toggle('show');
}
function closeSidebar(){
  document.getElementById('sidebar').classList.remove('open');
  document.getElementById('overlay').classList.remove('show');
}
function openurl(url){
  document.getElementById('rightFrame').src=url;
  if(window.innerWidth<769) closeSidebar();
}
</script>
</head>
<body style="display:flex;flex-direction:column;height:100vh;overflow:hidden">

<%
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
String adminid=currentUser.getAdminid();
ZtDao ztDao=new ZtDao();
int zt=ztDao.query(currentUser.getAdminid());
pageContext.setAttribute("zt",zt);
%>

<!-- Top Bar -->
<div class="topbar">
  <div class="menu-btn mobile-only" onclick="toggleSidebar()">&#9776;</div>
  <div class="brand">&#127891; 选题系统-学生端</div>
  <div class="user-tag"><%=currentUser.getId()%> <%=currentUser.getMc()%><br><%=currentUser.getDp()%></div>
</div>

<!-- App Body -->
<div class="app-body">
  <!-- Sidebar -->
  <div class="app-sidebar" id="sidebar">
    <div class="side-menu">
      <c:if test="${zt==1}">
      <div class="side-item" onclick="openurl('/byxt/student/xt.jsp')"><span class="si-icon">&#128221;</span>选择题目</div>
      </c:if>
      <c:if test="${zt==0}">
      <div class="side-item" onclick="openurl('/byxt/student/kt.jsp')"><span class="si-icon">&#128065;</span>查看题目</div>
      </c:if>
      <div class="side-item" onclick="openurl('/byxt/student/xtjg.jsp')"><span class="si-icon">&#128203;</span>选题结果</div>
      <div class="side-item" onclick="openurl('/byxt/student/modizl.jsp')"><span class="si-icon">&#128100;</span>个人信息</div>
      <div class="side-item" onclick="openurl('/byxt/student/modipwd.jsp')"><span class="si-icon">&#128273;</span>修改密码</div>
      <a href="/byxt/logout"><div class="side-item danger"><span class="si-icon">&#128682;</span>退出系统</div></a>
    </div>
  </div>

  <!-- Overlay -->
  <div class="overlay" id="overlay" onclick="closeSidebar()"></div>

  <!-- Content -->
  <div class="app-content">
    <iframe id="rightFrame" name="rightFrame" style="width:100%;height:100%;border:none"
            src="/byxt/student/xt.jsp"></iframe>
  </div>
</div>

</body>
</html>
