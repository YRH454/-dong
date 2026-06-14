<%@ page pageEncoding="UTF-8" import="dao.UserDao,vo.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>修改部门</title>
<style>
:root{--o:#E65100;--ol:#FB8C00;--w:#fff;--d:#333}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#fdf7f3;color:var(--d);font-size:15px;padding:20px 14px}
h3{font-size:20px;color:var(--o);margin-bottom:18px}
.card{background:var(--w);border-radius:13px;padding:20px 16px;box-shadow:0 1px 6px rgba(0,0,0,.04)}
.fg{margin-bottom:14px}
.fg label{display:block;font-size:13px;color:#888;margin-bottom:4px}
.fg input{width:100%;height:46px;padding:0 14px;border:1.5px solid #e0e0e0;border-radius:10px;font-size:16px;outline:none;font-family:inherit;transition:border .15s;background:#fafafa}
.fg input:focus{border-color:var(--ol);background:var(--w);box-shadow:0 0 0 3px rgba(251,140,0,.08)}
.btn{width:100%;height:48px;border:none;border-radius:24px;background:var(--ol);color:var(--w);font-size:17px;font-weight:600;cursor:pointer;-webkit-tap-highlight-color:transparent;box-shadow:0 4px 12px rgba(251,140,0,.2);transition:all .15s}
.btn:active{transform:scale(.97);background:var(--o)}
@media(min-width:600px){body{max-width:440px;margin:0 auto;padding:24px 0}}
</style>
<script>
function modidp(){
  var dp=document.getElementById("dp").value;
  if(dp==''){alert("部门名称不能为空");return}
  if(confirm("确认修改？")) document.getElementById("form1").submit();
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
  if(user==null||(user!=null&&user.getUserid().equals(userid))){
    userDao.modidp(userid,dp);
    CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
    currentUser.setDp(dp);
    response.sendRedirect("/byxt/admin/index.jsp");
  }else{
    response.sendRedirect("/byxt/admin/index.jsp?msg=failure");
  }
}
%>
<h3>修改部门名称</h3>
<div class="card">
<form id="form1" method="post" target="_top">
  <div class="fg"><label>当前部门</label><input type="text" name="dp" id="dp" value="${sessionScope.currentUser.dp}"></div>
  <input type="hidden" name="userid" value="${sessionScope.currentUser.id}">
  <input type="button" value="保存" class="btn" onclick="modidp()">
</form>
</div>
</body>
</html>
