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
.search-box{display:flex;gap:6px;margin-bottom:10px}
.search-box input{flex:1;height:40px;padding:0 12px;border:1.5px solid #ddd;border-radius:20px;font-size:14px;outline:none;font-family:inherit;background:var(--w)}
.search-box input:focus{border-color:var(--g)}
.pager{display:flex;gap:6px;justify-content:center;align-items:center;margin-top:14px;flex-wrap:wrap}
.pager button{min-width:36px;height:36px;padding:0 8px;border:1.5px solid #ddd;border-radius:18px;background:var(--w);color:var(--d);font-size:13px;cursor:pointer;-webkit-tap-highlight-color:transparent}
.pager button.active{background:var(--g);color:var(--w);border-color:var(--g);font-weight:600}
.pager button:disabled{opacity:.3;pointer-events:none}
.pager .info{font-size:12px;color:#888;padding:0 8px}
@media(min-width:768px){body{padding:16px 20px}.table-wrap{margin:0}}
</style>
<script>
var PS=10,cp=1,ad=[],fd=[];
function initT(){
  var rows=document.querySelectorAll('#dt tbody tr');ad=[];rows.forEach(function(r){ad.push(r.cloneNode(true))});fd=ad.slice();doS();
}
function doS(){
  var kw=document.getElementById('sk').value.toLowerCase().trim();
  fd=ad.filter(function(r){if(!kw)return true;return(r.textContent||'').toLowerCase().indexOf(kw)!==-1});cp=1;renderP();
}
function renderP(){
  var tb=document.querySelector('#dt tbody');tb.innerHTML='';var s=(cp-1)*PS,dd=fd.slice(s,s+PS);
  if(!dd.length){tb.innerHTML='<tr><td colspan="3" style="padding:30px;color:#999">无匹配</td></tr>'}else{dd.forEach(function(r){tb.appendChild(r)})}
  var tp=Math.ceil(fd.length/PS);
  document.getElementById('pi').textContent=(fd.length>0?(s+1)+'-'+Math.min(s+PS,fd.length)+' / '+fd.length:'0 / 0');
  document.getElementById('pb').disabled=cp<=1;document.getElementById('nb').disabled=cp>=tp;
  var pn=document.getElementById('pn');pn.innerHTML='';
  for(var i=1;i<=tp;i++){var b=document.createElement('button');b.textContent=i;if(i===cp)b.className='active';b.onclick=(function(p){return function(){cp=p;renderP()}})(i);pn.appendChild(b)}
}
function pp(){if(cp>1){cp--;renderP()}}
function np(){var tp=Math.ceil(fd.length/PS);if(cp<tp){cp++;renderP()}}
window.onload=initT;
</script>
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
<div style="margin-bottom:6px">题目数量：<span class="badge">${kxgs}</span> ｜ 每页10条</div>
<div class="search-box"><input type="text" id="sk" placeholder="搜索教师/题目..." oninput="doS()"></div>
<div class="table-wrap">
<table id="dt">
<thead><tr><th>教师</th><th>题目</th><th>备注</th></tr></thead>
<tbody>
<c:forEach items="${tmList}" var="tm">
<tr><td>${tm.txm}</td><td>${tm.tm}</td><td>${empty tm.bz?'-':tm.bz}</td></tr>
</c:forEach>
</tbody></table>
</div>
<div class="pager">
  <button id="pb" onclick="pp()">上一页</button><span id="pn"></span><button id="nb" onclick="np()">下一页</button>
  <span class="info" id="pi"></span>
</div>
</c:if>
</body>
</html>
