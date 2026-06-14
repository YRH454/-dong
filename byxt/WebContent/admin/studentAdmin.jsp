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
    #left{
    float:left;
    width:75%;
    }
    #right{
    float:left;
     width:25%;
    }
    </style>
    <script language="javascript">
  
 function update1(id,xh,xm,zy,bj,email,phone,qq){

   var updateForm= document.getElementById("updateform");
  document.getElementById("btupdate").disabled=false;
  updateForm.studentid.value=id;
  updateForm.xh.value=xh;
  updateForm.xm.value=xm;
  updateForm.zy.value=zy; 
  updateForm.email.value=email;
  updateForm.phone.value=phone;
  updateForm.qq.value=qq;
  updateForm.bj.value=bj;
  updateForm.xh.readOnly=true;
 }
 function update2(){
var updateForm=document.getElementById("updateform");
updateForm.action="/byxt/admin/studentmanage?action=update";
  
 }
  function add(){
var updateForm=document.getElementById("updateform");
updateForm.action="/byxt/admin/studentmanage?action=add";
  
 }
 
  function jump1(pageno){
      var queryForm=document.getElementById("query");
      queryForm.pageNo.value=pageno;
      queryForm.submit();
    }
  </script>
	</head>
	<body>
<div id="left">
<h2>学生信息</h2>
 <form action="/byxt/admin/studentmanage" method="post" id="query">
   <input type="text" name="keyWord" value="${param.keyWord}" size="30" placeholder="请输入学号或姓名或专业或班级"/>
  <input type="hidden" name="pageNo" value="1"/>
  <input type="hidden" name="action" value="query"/>
 <input type="submit" value="查询"/>
 </form>
 <c:if test="${recordCount==0}">
    <br/><br/>无记录
  </c:if>
   <c:if test="${recordCount>0}">
   <table class="dt">
   <tr><th>学号</th><th>姓名</th><th>专业</th><th>班级</th><th>邮箱</th><th>电话</th><th>QQ</th><th>修改</th><th>删除</th><th>重置密码</th></tr>
  <c:forEach items="${studentList}" var="student">
  <tr>
   <td>${student.xh}</td>
   <td>${student.xm}</td>
   <td>${student.zy}</td>
   <td>${student.bj}</td>
   <td>${student.email}</td>
   <td>${student.phone}</td>
   <td>${student.qq}</td>    
   <td><a href="javascript:update1('${student.id}','${student.xh}','${student.xm}','${student.zy}','${student.bj}','${student.email}','${student.phone}','${student.qq}');">修改</a></td>
   <td><a href='/byxt/admin/studentmanage?action=delete&id=${student.id}' onclick="return confirm('确实要删除该记录吗？')">删除</a></td>
   <td><a href='/byxt/admin/studentmanage?action=resetpwd&xh=${student.xh}' onclick="return confirm('确实要重置密码吗？')">重置密码</a></td>
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
<div><a href="/byxt/admin/studentmanage?action=qk" onclick="return confirm('确实要清空记录吗？')">清空记录</a></div>
</c:if>

</div>
<div id="right">
<h2>添加/修改学生信息</h2>
<form action="" method="post" id="updateform" >
    <table>
     <tr><td>学号</td><td><input type="text" name="xh" id="xh"/></td></tr>
    <tr><td>姓名</td><td><input type="text" name="xm" /></td></tr>
     <tr><td>专业</td><td><input type="text" name="zy" /></td></tr>
      <tr><td>班级</td><td><input type="text" name="bj" /></td></tr>
     <tr><td>邮箱</td><td><input type="text" name="email" /></td></tr>
     <tr><td>电话</td><td><input type="text" name="phone" /></td></tr>
     <tr><td>QQ</td><td><input type="text" name="qq" /></td></tr>
     
     <tr><td>　　<input type="submit" value="添加" onclick="add()"/></td>
     <td>　　<input type="submit" value="修改" id="btupdate" onclick="update2()" disabled/>　<input type="reset" value="重置" onclick="document.getElementById('xh').readOnly=false"/></td></tr>
    
    </table>
    <input type="hidden" value="" id="studentid" name="studentid"/>
    </form>   
    <hr> 
    <h2>从Excel导入学生信息</h2>
    1、Excel必须为03版<br>
    2、Ecxel第一行为标题行，对应信息（学号，姓名，专业，班级，email，电话，qq）
     <br> <a href="/byxt/admin/student.xls">学生信息模板下载</a>
     <form method="post" action="/byxt/admin/studentmanage?action=daoru"  enctype="multipart/form-data">
     工作表序号（从1开始） <input type="text" size="5" name="sheetno" value="1"/><br>
     <input type="file" name="file1"/>          
     <input type="submit" value="导入"/>
     </form>
</div>
	</body>
</html>
