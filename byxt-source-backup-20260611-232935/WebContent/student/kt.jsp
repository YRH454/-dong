<%@ page  pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<style>
  body{
   
 text-align:center; 
 font-family: 微软雅黑;
 font-size:18px;

  }
  th,td{
  border:2px solid gray;
  text-align:center; 
  padding:3px 10px;
  }
  table{
  border-collapse:collapse; 
  margin:0 auto;
  }
 
 </style>
 
</head>

<body>
<%


CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");

StudentDao studentDao=new StudentDao(); 
Student student=studentDao.findStudentByIdAdminid(currentUser.getId(),currentUser.getAdminid()); 
if(student.getXm().equals("")||student.getPhone().equals("")||student.getQq().equals("")){
	response.sendRedirect("modizl.jsp");
	return;
}

String adminid=currentUser.getAdminid();
TmDao tmDao=new TmDao();
String condition2=" where xh='' and adminid='"+adminid+"'";
List<Tm> tmList=tmDao.querytm(condition2);
int kxgs=tmDao.getRecordCount(condition2);
pageContext.setAttribute("tmList",tmList);
pageContext.setAttribute("kxgs",kxgs);
 %>
  <h3> 目前不能选题，只能查看。题目数量：${kxgs}</h3>
 <br/>
   <table>
   <tr>
   <th>教师姓名</th><th>题目</th><th>备注</th> 
   </tr>
  <c:forEach items="${tmList}" var="tm">
  <tr>
   <td>${tm.txm}</td><td>${tm.tm}</td><td>${tm.bz}</td>  
  
  </tr>
 </c:forEach> 
 </table>
 
</body>
</html>
