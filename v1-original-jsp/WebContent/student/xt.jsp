<%@ page  pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html> 
<head>
<meta http-equiv="refresh" content="20" />
  <script src="/byxt/js/ajax.js"></script>
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
 <script language="javascript">
  function shuaxin(){
  window.location.reload();
  }
  function xztm(tmid){
   if(window.confirm("确实要选择该题目吗")){
     var url="/byxt/student/xztm";
     sendRequest(url,"tmid="+tmid,'POST',showresult);	
 }
   }
 function showresult(){
       if (httpRequest.readyState == 4) { 
			if (httpRequest.status == 200) {
				var info=httpRequest.responseText;
				if(info=='0'){
				   alert("该题目已经被选了，请选择别的题目");
				}
				else{
				   alert("选题成功，请点击左侧选题结果菜单查看详细信息");
				   
				}
				window.location.reload();
			}
		}
		
}
 </script>
 
</head>

<body onload="document.getElementById('ct').innerHTML=new Date().toLocaleString()">
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
ZtDao ztDao=new ZtDao();
if(ztDao.query(currentUser.getAdminid())==0){
 out.print("现在不能选题");
 return;
}
TmDao tmDao=new TmDao();
String condition1=" where xh='"+currentUser.getId()+"' and adminid='"+adminid+"'";
int xtgs=tmDao.getRecordCount(condition1);
if(xtgs!=0){
out.print("你已经选过题目了，不能再选了。<br>如要重选，请先在选题结果中删除选题记录。");
 return;
}
String condition2=" where xh='' and adminid='"+adminid+"'";
List<Tm> tmList=tmDao.querytm(condition2);
int kxgs=tmDao.getRecordCount(condition2);
pageContext.setAttribute("tmList",tmList);
pageContext.setAttribute("kxgs",kxgs);
 %>
 友情提示：题目列表每20秒刷新一次，为提高选题成功率，建议选题前<a href="javascript:shuaxin()">手动刷新列表</a>
 <h3>题目列表(<span id="ct"></span>)</h3>
 目前题目数量：${kxgs} <br>
   <table>
   <tr>
   <th>教师姓名</th><th>题目</th><th>备注</th><th>选择</th> 
   </tr>
  <c:forEach items="${tmList}" var="tm">
  <tr>
   <td>${tm.txm}</td><td>${tm.tm}</td><td>${tm.bz}</td>  
   <td><input type="button" value="选择" onclick="xztm('${tm.id}')"></td>   
  </tr>
 </c:forEach> 
 </table>
 
</body>
</html>
