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
  
 function update1(id,gh,xm,zhicheng,email,phone,qq,bgdd,shangxian){

   var updateForm= document.getElementById("updateform");
  document.getElementById("btupdate").disabled=false;
  updateForm.teacherid.value=id;
  updateForm.gh.value=gh;
  updateForm.gh.readOnly=true;
  updateForm.xm.value=xm;
  updateForm.zhicheng.value=zhicheng; 
  updateForm.email.value=email;
  updateForm.phone.value=phone;
  updateForm.qq.value=qq;
  updateForm.bgdd.value=bgdd; 
  updateForm.shangxian.value=shangxian;
 }
 function update2(){
var updateForm=document.getElementById("updateform");
updateForm.action="/byxt/admin/teacher/update";
  
 }
  function add(){
var updateForm=document.getElementById("updateform");
updateForm.action="/byxt/admin/teacher/add";
  
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
<h2>教师信息</h2>
 <form action="/byxt/admin/teacher/query" method="post" id="query">
   <input type="text" name="keyWord" value="${param.keyWord}" placeholder="请输入工号或姓名查询"/>
  <input type="hidden" name="pageNo" value="1"/>
 <input type="submit" value="查询"/>
 </form>
 <c:if test="${recordCount==0}">
    <br/><br/>无记录
  </c:if>
   <c:if test="${recordCount>0}">
   <table class="dt">
   <tr><th>工号</th><th>姓名</th><th>职称</th><th>邮箱</th><th>电话</th><th>QQ</th><th>办公地点</th><th>题目个数上限</th><th>修改</th><th>删除</th><th>重置密码</th></tr>
  <c:forEach items="${teacherList}" var="teacher">
  <tr>
   <td>${teacher.gh}</td>
   <td>${teacher.xm}</td>
   <td>${teacher.zhicheng}</td>
   <td>${teacher.email}</td>
   <td>${teacher.phone}</td>
   <td>${teacher.qq}</td>
    <td>${teacher.bgdd}</td>
    <td>${teacher.shangxian}</td>
   <td><a href="javascript:update1('${teacher.id}','${teacher.gh}','${teacher.xm}','${teacher.zhicheng}','${teacher.email}','${teacher.phone}','${teacher.qq}','${teacher.bgdd}',${teacher.shangxian});">修改</a></td>
   <td><a href='/byxt/admin/teacher/delete?id=${teacher.id}' onclick="return confirm('确实要删除该记录吗？')">删除</a></td>
    <td><a href='/byxt/admin/teacher/resetpwd?gh=${teacher.gh}' onclick="return confirm('确实要重置密码吗？')">重置密码</a></td>
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
<div><a href="/byxt/admin/teacher/qk" onclick="return confirm('确实要清空记录吗？')">清空记录</a></div>
</c:if>

</div>
<div id="right">
<h2>添加/修改教师信息</h2>
<form action="" method="post" id="updateform" >
    <table>
     <tr><td>工号</td><td><input type="text" name="gh" id="gh"/></td></tr>
    <tr><td>姓名</td><td><input type="text" name="xm" /></td></tr>
     <tr><td>职称</td><td><input type="text" name="zhicheng" /></td></tr>
     <tr><td>邮箱</td><td><input type="text" name="email" /></td></tr>
     <tr><td>电话</td><td><input type="text" name="phone" /></td></tr>
     <tr><td>QQ</td><td><input type="text" name="qq" /></td></tr>
      <tr><td>办公室</td><td><input type="text" name="bgdd" /></td></tr>
        <tr><td>题目个数上限</td><td><input type="text" name="shangxian" /></td></tr>
     <tr><td>　　<input type="submit" value="添加" onclick="add()"/></td>
     <td>　　<input type="submit" value="修改" id="btupdate" onclick="update2()" disabled/>　<input type="reset" value="重置" onclick="document.getElementById('gh').readOnly=false"/></td></tr>
    
    </table>
    <input type="hidden" value="" id="teacherid" name="teacherid"/>
    </form>   
    <hr> 
    <h2>从Excel导入教师信息</h2>
    1、Excel必须为03版<br>
    2、Ecxel第一行为标题行，对应信息（工号，姓名，职称，email，电话，qq，办公地点，题目个数上限）
   <br> <a href="/byxt/admin/teacher.xls">教师信息模板下载</a>
     <form method="post" action="/byxt/admin/teacher/daoru" enctype="multipart/form-data">
     工作表序号（从1开始） <input type="text" size="5" name="sheetno" value="1"/><br>
     <input type="file" name="file1"/>         
     <input type="submit" value="导入"/>
     </form>
</div>
	</body>
</html>
