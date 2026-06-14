<%@ page language="java" import="java.util.*,dao.*,vo.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
StudentDao studentDao=new StudentDao();
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
List<Student> wxtstudent=studentDao.wxt(currentUser.getAdminid());
pageContext.setAttribute("wxtstudent",wxtstudent);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>未选题学生</title>
<style>
:root{--o:#E65100;--ol:#FB8C00;--w:#fff;--d:#333}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#fdf7f3;color:var(--d);font-size:14px;padding:8px 10px}
h3{font-size:18px;color:var(--o);margin-bottom:10px}
.badge{background:#FFF3E0;color:var(--o);padding:3px 10px;border-radius:12px;font-size:12px;font-weight:600;margin-left:6px}
.table-wrap{-webkit-overflow-scrolling:touch;overflow-x:auto;margin:0 -4px;padding:0 4px}
table{width:100%;border-collapse:collapse;min-width:600px;font-size:13px;background:var(--w);border-radius:8px;overflow:hidden;box-shadow:0 1px 4px rgba(0,0,0,.04)}
thead th{background:var(--o);color:var(--w);padding:10px 8px;font-weight:600;font-size:12px;white-space:nowrap}
tbody td{padding:9px 8px;text-align:center;border-bottom:1px solid #f5f0ed;font-size:13px}
tbody tr:active{background:#FFF3E0}
.empty-hint{padding:30px;text-align:center;color:#999;font-size:14px}
@media(min-width:768px){body{padding:12px 16px}table{min-width:auto}}
</style>
</head>
<body>
<h3>未选题学生<span class="badge"><%=wxtstudent.size()%> 人</span></h3>
<c:if test="${empty wxtstudent}"><div class="empty-hint">所有学生均已选题 🎉</div></c:if>
<c:if test="${!empty wxtstudent}">
<div class="table-wrap">
<table>
<thead><tr><th>学号</th><th>姓名</th><th>专业</th><th>班级</th><th>邮箱</th><th>电话</th><th>QQ</th></tr></thead>
<tbody>
<c:forEach items="${wxtstudent}" var="s">
<tr><td>${s.xh}</td><td>${s.xm}</td><td>${s.zy}</td><td>${s.bj}</td><td>${s.email}</td><td>${s.phone}</td><td>${s.qq}</td></tr>
</c:forEach>
</tbody></table>
</div>
</c:if>
</body>
</html>
