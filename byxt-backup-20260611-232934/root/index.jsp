<%@ page  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>毕业设计选题系统-Root端</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mobile-app.css" />
<style>
.app-sidebar .side-item:active,.app-sidebar .side-item:hover{background:#FFEBEE;border-left-color:#C62828;color:#B71C1C}
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
<% vo.CurrentUser currentUser=(vo.CurrentUser)session.getAttribute("currentUser"); %>

<!-- Top Bar -->
<div class="topbar root">
  <div class="menu-btn mobile-only" onclick="toggleSidebar()">&#9776;</div>
  <div class="brand">&#128737; 选题系统-Root端</div>
  <div class="user-tag"><%=currentUser.getId()%></div>
</div>

<!-- App Body -->
<div class="app-body">
  <!-- Sidebar -->
  <div class="app-sidebar" id="sidebar">
    <div class="side-menu">
      <div class="side-item" onclick="openurl('/byxt/root/userAdmin.jsp')"><span class="si-icon">&#128083;</span>admin管理</div>
      <div class="side-item" onclick="openurl('/byxt/root/modipwd.jsp')"><span class="si-icon">&#128273;</span>修改密码</div>
      <a href="/byxt/logout"><div class="side-item danger"><span class="si-icon">&#128682;</span>退出系统</div></a>
    </div>
  </div>

  <!-- Overlay -->
  <div class="overlay" id="overlay" onclick="closeSidebar()"></div>

  <!-- Content -->
  <div class="app-content">
    <iframe id="rightFrame" name="rightFrame" style="width:100%;height:100%;border:none"
            src="/byxt/root/userAdmin.jsp"></iframe>
  </div>
</div>

</body>
</html>
