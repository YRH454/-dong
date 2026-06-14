<%@ page  pageEncoding="UTF-8" import="vo.*,dao.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
function modi(){
var xm=document.getElementById("xm").value;
var phone=document.getElementById("phone").value; 
if(xm==''||phone==''){
  alert("姓名或电话不能为空");
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
  CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser"); 
  TeacherDao teacherDao=new TeacherDao(); 
  String adminid=currentUser.getAdminid();
  Teacher teacher=teacherDao.findTeacherByIdAdminid(currentUser.getId(),adminid); 
  pageContext.setAttribute("teacher",teacher); 
   %>
  <h3>修改个人资料</h3>
    <form id="form1" action="/byxt/teacher/modizl" method="post">
    工号：<input type="text" name="gh" id="gh" value="${teacher.gh}" readonly><br>
    姓名：<input type="text" name="xm" id="xm" value="${teacher.xm}"><br>
    职称：<input type="text" name="zhicheng" id="zhicheng" value="${teacher.zhicheng}"><br>
    邮箱：<input type="text" name="email" id="email" value="${teacher.email}"><br>
    电话：<input type="text" name="phone" id="phone" value="${teacher.phone}"><br>
    QQ：<input type="text" name="qq" id="qq" value="${teacher.qq}"><br>
    办公地点：<input type="text" name="bgdd" id="bgdd" value="${teacher.bgdd}"><br>
     <input type="button" value="确定" onclick="modi()"/>
    </form>
  </body>
</html>
