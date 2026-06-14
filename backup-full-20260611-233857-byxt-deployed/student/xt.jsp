<%@ page pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>选择题目</title>
<style>
:root{--g:#2E7D32;--gl:#4CAF50;--gbg:#E8F5E9;--w:#fff;--d:#333;--b:#e0e0e0;--r:14px}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f5f7f5;color:var(--d);font-size:15px;line-height:1.6;padding:0}
.tip{background:linear-gradient(135deg,#FFF8E1,#FFECB3);color:#F57F17;padding:10px 14px;font-size:12px;border-left:4px solid #FFC107;margin-bottom:10px;border-radius:0 8px 8px 0;display:flex;align-items:center;gap:6px;flex-wrap:wrap}
.tip a{color:#E65100;font-weight:600}
.header-bar{display:flex;justify-content:space-between;align-items:center;padding:0 0 8px;flex-wrap:wrap;gap:8px}
.header-bar h2{font-size:20px;color:var(--g)}
.badge{background:var(--gbg);color:var(--g);padding:4px 14px;border-radius:20px;font-size:13px;font-weight:600}
.search-box{display:flex;gap:6px;margin-bottom:10px}
.search-box input{flex:1;height:40px;padding:0 12px;border:1.5px solid #ddd;border-radius:20px;font-size:14px;outline:none;font-family:inherit;background:var(--w);transition:border .15s}
.search-box input:focus{border-color:var(--gl)}
.search-box button{height:40px;padding:0 16px;border:none;border-radius:20px;background:var(--gl);color:var(--w);font-size:13px;font-weight:600;cursor:pointer;-webkit-tap-highlight-color:transparent}
.table-wrap{-webkit-overflow-scrolling:touch;overflow-x:auto;margin:0 -4px;padding:0 4px}
table{width:100%;border-collapse:collapse;min-width:500px;font-size:14px;background:var(--w);border-radius:var(--r);overflow:hidden;box-shadow:0 1px 6px rgba(0,0,0,.06)}
thead th{background:var(--g);color:var(--w);padding:10px 8px;font-weight:600;font-size:13px;white-space:nowrap}
tbody td{padding:10px 8px;text-align:center;border-bottom:1px solid #f0f0f0;font-size:14px}
tbody tr:last-child td{border-bottom:none}
tbody tr:active{background:var(--gbg)}
.btn-sm{display:inline-block;height:34px;padding:0 16px;border:none;border-radius:17px;font-size:13px;font-weight:600;cursor:pointer;background:var(--gl);color:var(--w);box-shadow:0 2px 6px rgba(76,175,80,.25);transition:all .15s;-webkit-tap-highlight-color:transparent}
.btn-sm:active{transform:scale(.95);background:var(--g)}
.alert-info{background:#E3F2FD;color:#1565C0;padding:24px;text-align:center;border-radius:var(--r);font-size:15px}
.empty-hint{padding:40px 20px;text-align:center;color:#999;font-size:15px}
.empty-hint .icon{font-size:48px;display:block;margin-bottom:12px}

/* Pagination */
.pager{display:flex;gap:6px;justify-content:center;align-items:center;margin-top:14px;flex-wrap:wrap}
.pager button{min-width:36px;height:36px;padding:0 8px;border:1.5px solid #ddd;border-radius:18px;background:var(--w);color:var(--d);font-size:13px;cursor:pointer;transition:all .15s;-webkit-tap-highlight-color:transparent}
.pager button.active{background:var(--g);color:var(--w);border-color:var(--g);font-weight:600}
.pager button:disabled{opacity:.3;pointer-events:none}
.pager .info{font-size:12px;color:#888;padding:0 8px}
@media(min-width:768px){body{padding:8px 16px}.table-wrap{margin:0}table{min-width:auto}}
</style>
<script src="/byxt/js/ajax.js"></script>
<script src="/byxt/js/ui.js"></script>
<script>
var PAGE_SIZE=10, currentPage=1, allData=[], filteredData=[];

function shuaxin(){window.location.reload()}
function xztm(tmid){
  confirmModal("确实要选择该题目吗？", "确认选题", function(){
    sendRequest("/byxt/student/xztm","tmid="+tmid,'POST',showresult);
  });
}
function showresult(){
  if(httpRequest.readyState==4&&httpRequest.status==200){
    var info=httpRequest.responseText;
    if(info=='0'){toast("该题目已经被选了，请选择别的题目","warn")}
    else{toast("选题成功！请查看选题结果","success");setTimeout(function(){window.location.reload()},1500)}
  }
}

// Search & Pagination
function initTable(){
  var rows=document.querySelectorAll('#dataTable tbody tr');
  allData=[];rows.forEach(function(r){allData.push(r.cloneNode(true))});
  filteredData=allData.slice();
  doSearch();
}
function doSearch(){
  var kw=document.getElementById('searchInput').value.toLowerCase().trim();
  filteredData=allData.filter(function(row){
    if(!kw) return true;
    var text=(row.textContent||'').toLowerCase();
    return text.indexOf(kw)!==-1;
  });
  currentPage=1;
  renderPage();
}
function renderPage(){
  var tbody=document.querySelector('#dataTable tbody');
  tbody.innerHTML='';
  var start=(currentPage-1)*PAGE_SIZE;
  var pageData=filteredData.slice(start,start+PAGE_SIZE);
  if(pageData.length===0){
    tbody.innerHTML='<tr><td colspan="4" style="padding:30px;color:#999">没有匹配的题目</td></tr>';
  }else{
    pageData.forEach(function(row){tbody.appendChild(row)});
  }
  var totalPages=Math.ceil(filteredData.length/PAGE_SIZE);
  document.getElementById('pageInfo').textContent=(filteredData.length>0?(start+1)+'-'+Math.min(start+PAGE_SIZE,filteredData.length)+' / '+filteredData.length:'0 / 0');
  document.getElementById('prevBtn').disabled=currentPage<=1;
  document.getElementById('nextBtn').disabled=currentPage>=totalPages;
  // Render page numbers
  var pn=document.getElementById('pageNumbers');pn.innerHTML='';
  for(var i=1;i<=totalPages;i++){
    var btn=document.createElement('button');
    btn.textContent=i;if(i===currentPage) btn.className='active';
    btn.onclick=(function(p){return function(){currentPage=p;renderPage()}})(i);
    pn.appendChild(btn);
  }
}
function prevPage(){if(currentPage>1){currentPage--;renderPage()}}
function nextPage(){var tp=Math.ceil(filteredData.length/PAGE_SIZE);if(currentPage<tp){currentPage++;renderPage()}}
window.onload=function(){initTable();document.getElementById('ct').innerHTML=new Date().toLocaleString()};
</script>
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
ZtDao ztDao=new ZtDao();
if(ztDao.query(currentUser.getAdminid())==0){
  out.print("<div class='alert-info'>现在不是选题时间，请等待管理员开放选题</div>"); return;
}
TmDao tmDao=new TmDao();
String c1=" where xh='"+currentUser.getId()+"' and adminid='"+adminid+"'";
if(tmDao.getRecordCount(c1)!=0){
  out.print("<div class='alert-info'>你已经选过题目了，不能重复选择<br><small>如需重选，请先到选题结果中删除现有记录</small></div>"); return;
}
String c2=" where xh='' and adminid='"+adminid+"'";
List<Tm> tmList=tmDao.querytm(c2);
int kxgs=tmDao.getRecordCount(c2);
pageContext.setAttribute("tmList",tmList);
pageContext.setAttribute("kxgs",kxgs);
%>
<div style="padding:12px 14px">
  <div class="tip">每30秒自动刷新 &nbsp;<a href="javascript:shuaxin()">手动刷新</a> &nbsp;|&nbsp; 每页10条</div>
  <div class="header-bar">
    <h2>可选题目</h2>
    <span class="badge">剩余 ${kxgs} 个</span>
  </div>
  <c:if test="${kxgs==0}"><div class="empty-hint"><span class="icon">📭</span>暂无可选题目，请等待教师出题</div></c:if>
  <c:if test="${kxgs>0}">
  <div class="search-box">
    <input type="text" id="searchInput" placeholder="搜索教师/题目..." oninput="doSearch()">
    <button onclick="doSearch()">搜索</button>
  </div>
  <div class="table-wrap">
  <table id="dataTable">
  <thead><tr><th>教师</th><th>题目</th><th>备注</th><th>操作</th></tr></thead>
  <tbody>
  <c:forEach items="${tmList}" var="tm">
  <tr data-id="${tm.id}"><td>${tm.txm}</td><td>${tm.tm}</td><td>${empty tm.bz?'-':tm.bz}</td><td><button class="btn-sm" onclick="xztm('${tm.id}')">选择</button></td></tr>
  </c:forEach>
  </tbody></table>
  </div>
  <div class="pager">
    <button id="prevBtn" onclick="prevPage()">上一页</button>
    <span id="pageNumbers"></span>
    <button id="nextBtn" onclick="nextPage()">下一页</button>
    <span class="info" id="pageInfo"></span>
  </div>
  </c:if>
</div>
</body>
</html>
