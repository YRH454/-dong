<%@ page pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>查看题目</title>
<style>
:root{--g:#2E7D32;--gbg:#E8F5E9;--w:#fff;--d:#333;--b:#e0e0e0;--r:14px}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f5f7f5;color:var(--d);font-size:15px;line-height:1.6;padding:12px 14px}
h3{font-size:18px;color:var(--g);margin-bottom:12px;display:flex;align-items:center;gap:8px}
.badge{background:var(--gbg);color:var(--g);padding:4px 14px;border-radius:20px;font-size:13px;font-weight:600}
.table-wrap{-webkit-overflow-scrolling:touch;overflow-x:auto;margin:0 -4px;padding:0 4px}
table{width:100%;border-collapse:collapse;min-width:480px;font-size:14px;background:var(--w);border-radius:var(--r);overflow:hidden;box-shadow:0 1px 6px rgba(0,0,0,.06)}
thead th{background:var(--g);color:var(--w);padding:12px 10px;font-weight:600;font-size:13px;white-space:nowrap}
tbody td{padding:11px 10px;text-align:center;border-bottom:1px solid #f0f0f0;font-size:14px}
tbody tr:active{background:var(--gbg)}
.alert-info{background:#FFF8E1;color:#E65100;padding:14px;text-align:center;border-radius:var(--r);font-size:14px;border:1px solid #FFECB3;margin-bottom:12px}
.empty-hint{padding:40px 20px;text-align:center;color:#999;font-size:15px}
@media(min-width:768px){body{padding:16px 20px}.table-wrap{margin:0}}
</style>
</head>
<body>
<%
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
StudentDao studentDao=new StudentDao();
Student student=studentDao.findStudentByIdAdminid(currentUser.getId(),currentUser.getAdminid());
if(student.getXm()==null||student.getXm().equals("")||student.getPhone()==null||student.getPhone().equals("")||student.getQq()==null||student.getQq().equals("")){
  response.sendRedirect("modizl.jsp"); return;
}
String adminid=currentUser.getAdminid();
TmDao tmDao=new TmDao();
String c2=" where xh='' and adminid='"+adminid+"'";
List<Tm> tmList=tmDao.querytm(c2);
int kxgs=tmDao.getRecordCount(c2);
pageContext.setAttribute("tmList",tmList);
pageContext.setAttribute("kxgs",kxgs);
%>
<h3>👁 题目浏览</h3>
<div class="alert-info">当前仅可查看，不能选题（等待管理员开放选题状态）</div>
<c:if test="${kxgs==0}"><div class="empty-hint">暂无可选题目</div></c:if>
<c:if test="${kxgs>0}">
<div style="margin-bottom:8px">题目数量：<span class="badge">${kxgs}</span></div>
<div class="table-wrap">
<table>
<thead><tr><th>教师</th><th>题目</th><th>备注</th></tr></thead>
<tbody>
<c:forEach items="${tmList}" var="tm">
<tr><td>${tm.txm}</td><td>${tm.tm}</td><td>${tm.bz}</td></tr>
</c:forEach>
</tbody></table>
</div>
</c:if>
</body>
</html>
