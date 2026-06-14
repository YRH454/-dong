<%@ page pageEncoding="UTF-8" import="vo.*,dao.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>个人信息</title>
<style>
:root{--g:#2E7D32;--gl:#4CAF50;--w:#fff;--d:#333;--r:13px}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f5f7f5;color:var(--d);font-size:15px;line-height:1.6;padding:12px 14px}
.warn{background:#FFF3E0;color:#E65100;padding:14px 16px;border-radius:10px;font-size:13px;margin-bottom:16px;border-left:4px solid #FF9800}
.form-card{background:var(--w);border-radius:var(--r);padding:20px 16px;box-shadow:0 1px 6px rgba(0,0,0,.04)}
.fg{margin-bottom:14px}
.fg label{display:block;font-size:13px;color:#888;margin-bottom:4px}
.fg input{width:100%;height:46px;padding:0 14px;border:1.5px solid #e0e0e0;border-radius:10px;font-size:16px;outline:none;font-family:inherit;transition:border .15s;background:#fafafa}
.fg input:focus{border-color:var(--gl);background:var(--w);box-shadow:0 0 0 3px rgba(76,175,80,.08)}
.fg input[readonly]{background:#f5f5f5;color:#999}
.btn{width:100%;height:48px;border:none;border-radius:24px;background:var(--gl);color:var(--w);font-size:17px;font-weight:600;cursor:pointer;-webkit-tap-highlight-color:transparent;box-shadow:0 4px 12px rgba(76,175,80,.2);transition:all .15s}
.btn:active{transform:scale(.97);background:var(--g)}
@media(min-width:600px){body{max-width:480px;margin:0 auto;padding:16px 0}}
</style>
<script>
function modi(){
  var xm=document.getElementById("xm").value, phone=document.getElementById("phone").value, qq=document.getElementById("qq").value;
  if(xm==''||phone==''||qq==''){alert("姓名、电话、QQ都不能为空");return}
  if(confirm("确认保存？")) document.getElementById("form1").submit();
}
</script>
</head>
<body>
<%
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
StudentDao studentDao=new StudentDao();
Student student=studentDao.findStudentByIdAdminid(currentUser.getId(),currentUser.getAdminid());
pageContext.setAttribute("student",student);
%>
<div class="warn">请完善个人资料，电话和QQ不能为空，否则无法选题</div>
<div class="form-card">
<form id="form1" action="/byxt/student/modizl" method="post">
  <div class="fg"><label>学号</label><input type="text" name="xh" id="xh" value="${student.xh}" readonly></div>
  <div class="fg"><label>姓名</label><input type="text" name="xm" id="xm" value="${student.xm}"></div>
  <div class="fg"><label>专业</label><input type="text" name="zy" id="zy" value="${student.zy}"></div>
  <div class="fg"><label>班级</label><input type="text" name="bj" id="bj" value="${student.bj}"></div>
  <div class="fg"><label>邮箱</label><input type="text" name="email" id="email" value="${student.email}"></div>
  <div class="fg"><label>电话 *</label><input type="text" name="phone" id="phone" value="${student.phone}" placeholder="请输入手机号"></div>
  <div class="fg"><label>QQ *</label><input type="text" name="qq" id="qq" value="${student.qq}" placeholder="请输入QQ号"></div>
  <input type="button" value="保存" class="btn" onclick="modi()">
</form>
</div>
</body>
</html>
