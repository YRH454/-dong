<%@ page import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>	
   
    <style>
    
  .dt th,.dt td{
  border:2px solid gray;
  text-align:center;
  padding:3px 5px;
  }
   table{
 
  margin:0 auto;
  }
  table.dt{
  border-collapse:collapse; 
  margin:0 auto;
  }
    body{
      padding:10px;
      text-align:center;
    }
   
    </style>
    <script language="javascript">   
  function jump1(pageno){
      var queryForm=document.getElementById("query");
      queryForm.pageNo.value=pageno;
      queryForm.submit();
    }
  </script>
	</head>
	<body>

<h2>题目信息</h2>
 <form action="/byxt/admin/tmmanage" method="post" id="query">
   <input type="text" name="keyWord" value="${param.keyWord}" size="30" placeholder="请输入教师姓名或题目或专业班级"/>
  <input type="hidden" name="pageNo" value="1"/>
  <input type="hidden" name="action" value="query"/>
 <input type="submit" value="查询"/>
 </form>
 <c:if test="${recordCount==0}">
    <br/><br/>无记录
  </c:if>
   <c:if test="${recordCount>0}">
   <table class="dt">
   <tr><th>教师工号</th><th>教师姓名</th><th>题目</th><th>题目备注</th><th>学生学号</th><th>姓名</th><th>专业</th><th>班级</th><th>删除</th></tr>
  <c:forEach items="${tmList}" var="tm">
  <tr>
   <td>${tm.gh}</td>
   <td>${tm.txm}</td>
   <td>${tm.tm}</td>
   <td>${tm.bz}</td>
   <td>${tm.xh}</td>
   <td>${tm.sxm}</td>
   <td>${tm.zy}</td>   
    <td>${tm.bj}</td>   
   <td><a href='/byxt/admin/tmmanage?action=delete&id=${tm.id}' onclick="return confirm('确实要删除该记录吗？')">删除</a></td>
   </tr>
 </c:forEach>
 
 </table>
 
   共有记录${recordCount}条， 第${pageNo}/${pageCount}页，
<c:if test="${pageNo>1}">
 <a href="javascript:jump1('1')">首页</a>
 <a href="javascript:jump1('${pageNo-1}')">上页</a>
</c:if>
<c:if test="${pageNo==1}">
  首页  上页
</c:if>

 <c:if test="${pageNo<pageCount}">
   <a href="javascript:jump1('${pageNo+1}')">下页</a>
   <a href="javascript:jump1('${pageCount}')">末页</a>
</c:if>
 <c:if test="${pageNo==pageCount}">
   下页   末页
</c:if>
<br>
<div><a href="/byxt/admin/tmmanage?action=qk" onclick="return confirm('确实要清空记录吗？')">清空记录</a></div>
<div><a href="/byxt/admin/tmmanage?action=daochu">导出题目表</a></div>
</c:if>


	</body>
</html>
