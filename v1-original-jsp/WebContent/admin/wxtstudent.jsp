<%@ page language="java" import="java.util.*,dao.*,vo.*" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
StudentDao studentDao=new StudentDao();
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
String adminid=currentUser.getAdminid();
List<Student> wxtstudent=studentDao.wxt(adminid); 
pageContext.setAttribute("wxtstudent",wxtstudent);
 %>
<html>
  <head>
  <style>    
  th,td{
  border:2px solid gray;
  text-align:center;
  padding:3px 5px;
  }
  table{ 
  margin:0 auto;
  border-collapse:collapse; 
  }  
  body{
   padding:10px;
   text-align:center;
  }    
   </style>
  </head>
  
  <body>
  <h2>未选题学生记录（<%=wxtstudent.size()%>条）</h2>
    <table>
   <tr><th>学号</th><th>姓名</th><th>专业</th><th>班级</th><th>邮箱</th><th>电话</th><th>QQ</th></tr>
  <c:forEach items="${wxtstudent}" var="student">
  <tr>
   <td>${student.xh}</td>
   <td>${student.xm}</td>
   <td>${student.zy}</td>
   <td>${student.bj}</td>
   <td>${student.email}</td>
   <td>${student.phone}</td>
   <td>${student.qq}</td>      
  </tr>
 </c:forEach>
 
 </table>
  </body>
</html>
