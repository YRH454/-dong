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
var qq=document.getElementById("qq").value; 
if(xm==''||phone==''||qq==''){
  alert("姓名、电话、QQ都不能为空");
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
  StudentDao studentDao=new StudentDao(); 
  Student student=studentDao.findStudentByIdAdminid(currentUser.getId(),currentUser.getAdminid()); 
  pageContext.setAttribute("student",student); 
   %>
  <h3 style="color:red">为方便师生联系，请先完善个人资料,否则无法选题（电话，QQ不能为空)</h3>
    <form id="form1" action="/byxt/student/modizl" method="post">
    学号：<input type="text" name="xh" id="xh" value="${student.xh}" readonly><br>
    姓名：<input type="text" name="xm" id="xm" value="${student.xm}"><br>
    专业：<input type="text" name="zy" id="zy" value="${student.zy}"><br>
    班级：<input type="text" name="bj" id="bj" value="${student.bj}"><br>
    邮箱：<input type="text" name="email" id="email" value="${student.email}"><br>
    电话：<input type="text" name="phone" id="phone" value="${student.phone}"><br>
    QQ：<input type="text" name="qq" id="qq" value="${student.qq}"><br>
    
     <input type="button" value="确定" onclick="modi()"/>
    </form>
  </body>
</html>
