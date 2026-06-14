<%@ page import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mobile-app.css" />
<style>
.page-layout{display:flex;gap:16px;flex-direction:column}
#left{flex:1;min-width:0}
#right{width:100%;background:var(--gray-50);border-radius:var(--radius);padding:16px}
#right h2{font-size:1rem;margin-bottom:12px}
#right form td{padding:5px 6px;font-size:.875rem}
#right form td:first-child{text-align:right;width:30%;color:var(--gray-500);font-weight:500}
#right form input[type=text],#right form input[type=file]{width:100%;box-sizing:border-box}
.dt th,.dt td{border:1px solid #ddd;text-align:center;padding:6px 8px}
.dt th{background:#E3F2FD;color:#0D47A1;font-weight:600;font-size:.8125rem;white-space:nowrap}
.dt td{font-size:.8125rem}
.dt tr:active{background:var(--gray-50)}
.act-link{display:inline-block;padding:2px 8px;border-radius:4px;font-size:.8125rem;margin:0 1px;color:#1565C0;white-space:nowrap}
@media(min-width:900px){.page-layout{flex-direction:row}#right{width:300px;flex-shrink:0}}
</style>
<script>
function update1(id,xh,xm,zy,bj,email,phone,qq){
  var f=document.getElementById("updateform");
  document.getElementById("btupdate").disabled=false;
  f.studentid.value=id;f.xh.value=xh;f.xm.value=xm;f.zy.value=zy;
  f.email.value=email;f.phone.value=phone;f.qq.value=qq;f.bj.value=bj;f.xh.readOnly=true;
}
function update2(){document.getElementById("updateform").action="/byxt/admin/studentmanage?action=update"}
function add(){document.getElementById("updateform").action="/byxt/admin/studentmanage?action=add"}
function jump1(p){var f=document.getElementById("query");f.pageNo.value=p;f.submit()}
</script>
</head>
<body style="background:var(--gray-50)">
<div class="content-page">
<div class="page-layout">

<div id="left">
  <div class="flex-between mb-2">
    <h2 style="margin:0">学生信息</h2>
    <form action="/byxt/admin/studentmanage" method="post" id="query" class="form-inline">
      <input type="text" name="keyWord" value="${param.keyWord}" placeholder="学号/姓名/专业/班级" style="width:160px;height:38px;border:1.5px solid #ddd;border-radius:8px;padding:0 10px;font-size:.875rem"/>
      <input type="hidden" name="pageNo" value="1"/>
      <input type="hidden" name="action" value="query"/>
      <input type="submit" value="查询" style="height:38px;padding:0 14px;background:#1565C0;color:#fff;border:none;border-radius:8px;font-size:.875rem;cursor:pointer"/>
    </form>
  </div>

  <c:if test="${recordCount==0}">
    <div class="empty-state"><div class="icon">📭</div><p>暂无学生记录</p></div>
  </c:if>

  <c:if test="${recordCount>0}">
  <div class="table-wrap">
    <table class="dt">
    <tr><th>学号</th><th>姓名</th><th>专业</th><th>班级</th><th>邮箱</th><th>电话</th><th>QQ</th><th>修改</th><th>删除</th><th>重置</th></tr>
    <c:forEach items="${studentList}" var="student">
    <tr>
      <td>${student.xh}</td><td>${student.xm}</td><td>${student.zy}</td><td>${student.bj}</td>
      <td>${student.email}</td><td>${student.phone}</td><td>${student.qq}</td>
      <td><a class="act-link" href="javascript:update1('${student.id}','${student.xh}','${student.xm}','${student.zy}','${student.bj}','${student.email}','${student.phone}','${student.qq}')">修改</a></td>
      <td><a class="act-link" href="/byxt/admin/studentmanage?action=delete&id=${student.id}" onclick="return confirm('确实要删除吗？')" style="color:#C62828">删除</a></td>
      <td><a class="act-link" href="/byxt/admin/studentmanage?action=resetpwd&xh=${student.xh}" onclick="return confirm('确实要重置密码吗？')">重置</a></td>
    </tr>
    </c:forEach>
    </table>
  </div>

  <div class="pager">
    共${recordCount}条 第${pageNo}/${pageCount}页 &nbsp;
    <c:if test="${pageNo>1}"><a href="javascript:jump1('1')">首页</a><a href="javascript:jump1('${pageNo-1}')">上页</a></c:if>
    <c:if test="${pageNo==1}"><span class="disabled">首页 上页</span></c:if>
    <span class="current">${pageNo}</span>
    <c:if test="${pageNo<pageCount}"><a href="javascript:jump1('${pageNo+1}')">下页</a><a href="javascript:jump1('${pageCount}')">末页</a></c:if>
    <c:if test="${pageNo==pageCount}"><span class="disabled">下页 末页</span></c:if>
  </div>
  <div style="text-align:center;margin-top:8px"><a href="/byxt/admin/studentmanage?action=qk" onclick="return confirm('确实要清空记录吗？')" style="color:#C62828;font-size:.8125rem">清空记录</a></div>
  </c:if>
</div>

<div id="right">
  <h2>➕ 添加/修改学生</h2>
  <form action="" method="post" id="updateform">
    <table style="width:100%">
      <tr><td>学号</td><td><input type="text" name="xh" id="xh" class="form-input" style="height:36px;font-size:.875rem"/></td></tr>
      <tr><td>姓名</td><td><input type="text" name="xm" class="form-input" style="height:36px;font-size:.875rem"/></td></tr>
      <tr><td>专业</td><td><input type="text" name="zy" class="form-input" style="height:36px;font-size:.875rem"/></td></tr>
      <tr><td>班级</td><td><input type="text" name="bj" class="form-input" style="height:36px;font-size:.875rem"/></td></tr>
      <tr><td>邮箱</td><td><input type="text" name="email" class="form-input" style="height:36px;font-size:.875rem"/></td></tr>
      <tr><td>电话</td><td><input type="text" name="phone" class="form-input" style="height:36px;font-size:.875rem"/></td></tr>
      <tr><td>QQ</td><td><input type="text" name="qq" class="form-input" style="height:36px;font-size:.875rem"/></td></tr>
      <tr><td colspan="2" style="text-align:center;padding-top:10px">
        <input type="submit" value="添加" onclick="add()" class="btn btn-primary btn-sm"/>
        <input type="submit" value="修改" id="btupdate" onclick="update2()" disabled class="btn btn-outline btn-sm"/>
        <input type="reset" value="重置" onclick="document.getElementById('xh').readOnly=false" class="btn btn-sm" style="background:#eee"/>
      </td></tr>
    </table>
    <input type="hidden" value="" id="studentid" name="studentid"/>
  </form>

  <hr style="margin:16px 0;border-color:#eee">

  <h2>📥 Excel导入学生</h2>
  <p style="font-size:.75rem;color:var(--gray-500)">仅支持03版Excel，首行为标题（学号,姓名,专业,班级,email,电话,qq）</p>
  <a href="/byxt/admin/student.xls" style="font-size:.8125rem">📎 模板下载</a>
  <form method="post" action="/byxt/admin/studentmanage?action=daoru" enctype="multipart/form-data" style="margin-top:8px">
    <div class="form-inline"><span style="font-size:.8125rem">工作表序号:</span><input type="text" size="3" name="sheetno" value="1" style="width:50px;height:36px;border:1.5px solid #ddd;border-radius:8px;padding:0 8px;font-size:.875rem"/></div>
    <div class="form-inline mt-1"><input type="file" name="file1" style="flex:1;font-size:.8125rem"/><input type="submit" value="导入" class="btn btn-primary btn-sm"/></div>
  </form>
</div>

</div>
</div>
</body>
</html>
