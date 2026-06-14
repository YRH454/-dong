<%@ page pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>选题结果</title>
<style>
:root{--g:#2E7D32;--gl:#4CAF50;--gbg:#E8F5E9;--w:#fff;--d:#333;--red:#C62828}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f5f7f5;color:var(--d);font-size:15px;line-height:1.6;padding:12px 14px}
h2{font-size:20px;color:var(--g);margin-bottom:14px}
.card{background:var(--w);border-radius:14px;overflow:hidden;box-shadow:0 1px 6px rgba(0,0,0,.06);margin-bottom:14px}
.card .row{display:flex;padding:12px 16px;border-bottom:1px solid #f0f0f0;align-items:center}
.card .row:last-child{border-bottom:none}
.card .label{width:90px;flex-shrink:0;color:#888;font-size:13px}
.card .value{flex:1;font-size:15px;font-weight:500}
.card .row.act{justify-content:center;padding:14px}
.btn-del{display:inline-block;height:40px;padding:0 24px;border:none;border-radius:20px;font-size:14px;font-weight:600;cursor:pointer;background:var(--red);color:var(--w);text-decoration:none;line-height:40px;-webkit-tap-highlight-color:transparent;transition:all .15s}
.btn-del:active{transform:scale(.95)}
.empty-hint{padding:40px 20px;text-align:center;color:#999;font-size:15px}
.empty-hint .icon{font-size:48px;display:block;margin-bottom:12px}
@media(min-width:768px){body{max-width:700px;margin:0 auto;padding:16px 20px}}
</style>
<script src="/byxt/js/ajax.js"></script>
<script src="/byxt/js/ui.js"></script>
<script>function delRec(url){confirmModal("确认删除选题记录？删除后不可恢复","删除确认",function(){location.href=url})}</script>
</head>
<body>
<%
request.setCharacterEncoding("UTF-8");
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
StudentDao studentDao=new StudentDao();
Student student=studentDao.findStudentByIdAdminid(currentUser.getId(),currentUser.getAdminid());
if(student.getXm()==null||student.getXm().equals("")||student.getPhone()==null||student.getPhone().equals("")||student.getQq()==null||student.getQq().equals("")){
  response.sendRedirect("modizl.jsp"); return;
}
String adminid=currentUser.getAdminid();
TmDao tmDao=new TmDao();
String delsno=request.getParameter("xh");
if(delsno!=null){tmDao.qcjl(delsno,adminid);}
String c1=" where xh='"+currentUser.getId()+"' and adminid='"+adminid+"'";
List<TmEx> tmExList=tmDao.query(c1,adminid);
pageContext.setAttribute("tmExList",tmExList);
ZtDao ztDao=new ZtDao();
pageContext.setAttribute("adid",ztDao.query(currentUser.getAdminid())+"");
%>
<h2>📋 选题结果</h2>
<c:if test="${empty tmExList}">
<div class="empty-hint"><span class="icon">📭</span>目前没有选题记录</div>
</c:if>
<c:if test="${!empty tmExList}">
<c:forEach items="${tmExList}" var="tmEx">
<div class="card">
  <div class="row"><span class="label">学号</span><span class="value">${tmEx.xh}</span></div>
  <div class="row"><span class="label">姓名</span><span class="value">${tmEx.sxm}</span></div>
  <div class="row"><span class="label">专业班级</span><span class="value">${tmEx.zy} / ${tmEx.bj}</span></div>
  <div class="row"><span class="label">指导教师</span><span class="value">${tmEx.txm}（${tmEx.tzhicheng}）</span></div>
  <div class="row"><span class="label">题目</span><span class="value">${tmEx.tm}</span></div>
  <div class="row"><span class="label">备注</span><span class="value">${tmEx.bz}</span></div>
  <div class="row"><span class="label">联系方式</span><span class="value">${tmEx.temail} / ${tmEx.tphone} / QQ:${tmEx.tqq}</span></div>
  <div class="row"><span class="label">办公地点</span><span class="value">${tmEx.tbgdd}</span></div>
  <c:if test="${adid!='0'}">
  <div class="row act"><a href="javascript:void(0)" class="btn-del" onclick="delRec('xtjg.jsp?xh=${tmEx.xh}')">删除记录</a></div>
  </c:if>
</div>
</c:forEach>
</c:if>
</body>
</html>
