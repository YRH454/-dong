<%@ page  pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<html>
<head>
  <script src="/byxt/js/ajax.js"></script>
<style>
  body{
 text-align:center;
 font-family: 微软雅黑;
 font-size:16px;
margin-top:10px;
  }  
  table,th,td{
border:1px solid red;
padding:10px;
text-align:center;
}
  table{
  margin:0 auto;
  border-collapse:collapse;
  }
  
 </style>
 <script language="javascript">
 
 </script> 
</head> 
<body>
<%
request.setCharacterEncoding("UTF-8");
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");

StudentDao studentDao=new StudentDao(); 
Student student=studentDao.findStudentByIdAdminid(currentUser.getId(),currentUser.getAdminid()); 
if(student.getXm().equals("")||student.getPhone().equals("")||student.getQq().equals("")){
	response.sendRedirect("modizl.jsp");
	return;
}

String adminid=currentUser.getAdminid();
TmDao tmDao=new TmDao();
String delsno=request.getParameter("xh");
if(delsno!=null){
tmDao.qcjl(delsno,adminid);
}


 String condition1=" where xh='"+currentUser.getId()+"' and adminid='"+adminid+"'";
List<TmEx> tmExList=tmDao.query(condition1,adminid);
pageContext.setAttribute("tmExList",tmExList);

ZtDao ztDao=new ZtDao();
pageContext.setAttribute("adid", ztDao.query(currentUser.getAdminid())+"");
//if(ztDao.query(currentUser.getAdminid())==0){
// out.print("现在不能选题");
// return;
//}
 %>
 <h2>选题结果</h2>  
 <c:if test="${empty tmExList}">
 目前没有选题记录
 </c:if>
 <c:if test="${!empty tmExList}">
   <table>
 <c:forEach items="${tmExList}" var="tmEx">

     <tr><td>学号</td><td>${tmEx.xh}</td></tr>
     <tr><td>姓名</td><td>${tmEx.sxm}</td></tr>
     <tr><td>专业</td><td>${tmEx.zy}</td></tr>
     <tr><td>班级</td><td>${tmEx.bj}</td></tr>
     <tr><td>教师姓名</td><td>${tmEx.txm}</td></tr>
     <tr><td>教师职称</td><td>${tmEx.tzhicheng}</td></tr>
     <tr><td>题目</td><td>${tmEx.tm}</td></tr>
     <tr><td>备注</td><td>${tmEx.bz}</td></tr>
     <tr><td>教师email</td><td>${tmEx.temail}</td></tr>
     <tr><td>教师电话</td><td>${tmEx.tphone}</td></tr>
     <tr><td>教师QQ</td><td>${tmEx.tqq}</td></tr>
      <tr><td>教师办公地点</td><td>${tmEx.tbgdd}</td></tr>
      <c:if test="${adid!=0}">
      <tr><td colspan="2">　　　<a href="xtjg.jsp?xh=${tmEx.xh}">删除选题记录</a></td></tr>
      </c:if>
 </c:forEach>
    
    </table>
   
 </c:if>
</body>
</html>
