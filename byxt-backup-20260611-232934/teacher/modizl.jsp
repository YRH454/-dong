<%@ page pageEncoding="UTF-8" import="vo.*,dao.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>个人信息</title>
<style>
:root{--b:#1565C0;--bl:#1E88E5;--w:#fff;--d:#333;--r:13px}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f4f6f9;color:var(--d);font-size:15px;line-height:1.6;padding:12px 14px}
h3{font-size:20px;color:var(--b);margin-bottom:16px}
.fcard{background:var(--w);border-radius:var(--r);padding:20px 16px;box-shadow:0 1px 6px rgba(0,0,0,.04)}
.fg{margin-bottom:14px}
.fg label{display:block;font-size:13px;color:#888;margin-bottom:4px}
.fg input{width:100%;height:46px;padding:0 14px;border:1.5px solid #e0e0e0;border-radius:10px;font-size:16px;outline:none;font-family:inherit;transition:border .15s;background:#fafafa}
.fg input:focus{border-color:var(--bl);background:var(--w);box-shadow:0 0 0 3px rgba(30,136,229,.08)}
.fg input[readonly]{background:#f5f5f5;color:#999}
.btn{width:100%;height:48px;border:none;border-radius:24px;background:var(--bl);color:var(--w);font-size:17px;font-weight:600;cursor:pointer;-webkit-tap-highlight-color:transparent;box-shadow:0 4px 12px rgba(30,136,229,.2);transition:all .15s}
.btn:active{transform:scale(.97);background:var(--b)}
@media(min-width:600px){body{max-width:480px;margin:0 auto;padding:16px 0}}
</style>
<script>
function modi(){
  var xm=document.getElementById("xm").value, phone=document.getElementById("phone").value;
  if(xm==''||phone==''){alert("姓名和电话不能为空");return}
  if(confirm("确认保存？")) document.getElementById("form1").submit();
}
</script>
</head>
<body>
<%
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
TeacherDao teacherDao=new TeacherDao();
Teacher teacher=teacherDao.findTeacherByIdAdminid(currentUser.getId(),currentUser.getAdminid());
pageContext.setAttribute("teacher",teacher);
%>
<h3>👤 个人资料</h3>
<div class="fcard">
<form id="form1" action="/byxt/teacher/modizl" method="post">
  <div class="fg"><label>工号</label><input type="text" name="gh" id="gh" value="${teacher.gh}" readonly></div>
  <div class="fg"><label>姓名 *</label><input type="text" name="xm" id="xm" value="${teacher.xm}"></div>
  <div class="fg"><label>职称</label><input type="text" name="zhicheng" id="zhicheng" value="${teacher.zhicheng}"></div>
  <div class="fg"><label>邮箱</label><input type="text" name="email" id="email" value="${teacher.email}"></div>
  <div class="fg"><label>电话 *</label><input type="text" name="phone" id="phone" value="${teacher.phone}"></div>
  <div class="fg"><label>QQ</label><input type="text" name="qq" id="qq" value="${teacher.qq}"></div>
  <div class="fg"><label>办公地点</label><input type="text" name="bgdd" id="bgdd" value="${teacher.bgdd}"></div>
  <input type="button" value="保存" class="btn" onclick="modi()">
</form>
</div>
</body>
</html>
