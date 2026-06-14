<%@ page import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mobile-app.css" />
<style>
.dt th,.dt td{border:1px solid #ddd;text-align:center;padding:6px 8px}
.dt th{background:#FFF3E0;color:#E65100;font-weight:600;font-size:.8125rem;white-space:nowrap}
.dt td{font-size:.8125rem}
.dt tr:active{background:var(--gray-50)}
.act-link{display:inline-block;padding:2px 8px;border-radius:4px;font-size:.8125rem;color:#C62828;white-space:nowrap}
</style>
<script>
function jump1(p){var f=document.getElementById("query");f.pageNo.value=p;f.submit()}
</script>
</head>
<body style="background:var(--gray-50)">
<div class="content-page">

<div class="flex-between mb-2">
  <h2 style="margin:0">题目信息</h2>
  <form action="/byxt/admin/tmmanage" method="post" id="query" class="form-inline">
    <input type="text" name="keyWord" value="${param.keyWord}" placeholder="教师/题目/专业/班级" style="width:160px;height:38px;border:1.5px solid #ddd;border-radius:8px;padding:0 10px;font-size:.875rem"/>
    <input type="hidden" name="pageNo" value="1"/>
    <input type="hidden" name="action" value="query"/>
    <input type="submit" value="查询" style="height:38px;padding:0 14px;background:#E65100;color:#fff;border:none;border-radius:8px;font-size:.875rem;cursor:pointer"/>
  </form>
</div>

<c:if test="${recordCount==0}">
  <div class="empty-state"><div class="icon">📭</div><p>暂无题目记录</p></div>
</c:if>

<c:if test="${recordCount>0}">
<div class="table-wrap">
  <table class="dt">
  <tr><th>教师工号</th><th>教师姓名</th><th>题目</th><th>备注</th><th>学号</th><th>姓名</th><th>专业</th><th>班级</th><th>删除</th></tr>
  <c:forEach items="${tmList}" var="tm">
  <tr>
    <td>${tm.gh}</td><td>${tm.txm}</td><td>${tm.tm}</td><td>${tm.bz}</td>
    <td>${tm.xh}</td><td>${tm.sxm}</td><td>${tm.zy}</td><td>${tm.bj}</td>
    <td><a class="act-link" href="/byxt/admin/tmmanage?action=delete&id=${tm.id}" onclick="return confirm('确实要删除吗？')">删除</a></td>
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
<div class="text-center mt-1">
  <a href="/byxt/admin/tmmanage?action=qk" onclick="return confirm('确实要清空记录吗？')" style="color:#C62828;font-size:.8125rem">清空记录</a> &nbsp;
  <a href="/byxt/admin/tmmanage?action=daochu" style="font-size:.8125rem;color:#1565C0">📥 导出题目表</a>
</div>
</c:if>

</div>
</body>
</html>
