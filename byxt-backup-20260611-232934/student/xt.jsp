<%@ page pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="refresh" content="30" />
<title>选择题目</title>
<style>
:root{--g:#2E7D32;--gl:#4CAF50;--gbg:#E8F5E9;--w:#fff;--d:#333;--b:#e0e0e0;--r:14px}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f5f7f5;color:var(--d);font-size:15px;line-height:1.6;padding:0}
.tip{background:linear-gradient(135deg,#FFF8E1,#FFECB3);color:#F57F17;padding:12px 16px;font-size:13px;border-left:4px solid #FFC107;margin-bottom:12px;border-radius:0 10px 10px 0;display:flex;align-items:center;gap:6px;flex-wrap:wrap}
.tip a{color:#E65100;font-weight:600}
.header-bar{display:flex;justify-content:space-between;align-items:center;padding:0 0 12px;flex-wrap:wrap;gap:8px}
.header-bar h2{font-size:20px;color:var(--g);display:flex;align-items:center;gap:8px}
.badge{background:var(--gbg);color:var(--g);padding:4px 14px;border-radius:20px;font-size:13px;font-weight:600}
.table-wrap{-webkit-overflow-scrolling:touch;overflow-x:auto;margin:0 -4px;padding:0 4px}
table{width:100%;border-collapse:collapse;min-width:500px;font-size:14px;background:var(--w);border-radius:var(--r);overflow:hidden;box-shadow:0 1px 6px rgba(0,0,0,.06)}
thead th{background:var(--g);color:var(--w);padding:12px 10px;font-weight:600;font-size:13px;white-space:nowrap;position:sticky;top:0}
tbody td{padding:11px 10px;text-align:center;border-bottom:1px solid #f0f0f0;font-size:14px}
tbody tr:last-child td{border-bottom:none}
tbody tr:active{background:var(--gbg)}
.btn-sm{display:inline-block;height:36px;padding:0 16px;border:none;border-radius:20px;font-size:13px;font-weight:600;cursor:pointer;background:var(--gl);color:var(--w);box-shadow:0 2px 6px rgba(76,175,80,.25);transition:all .15s;-webkit-tap-highlight-color:transparent}
.btn-sm:active{transform:scale(.95);background:var(--g)}
.alert-info{background:#E3F2FD;color:#1565C0;padding:24px;text-align:center;border-radius:var(--r);font-size:15px}
.empty-hint{padding:40px 20px;text-align:center;color:#999;font-size:15px}
.empty-hint .icon{font-size:48px;display:block;margin-bottom:12px}
@media(min-width:768px){body{padding:8px 16px}.table-wrap{margin:0}table{min-width:auto}}
</style>
<script src="/byxt/js/ajax.js"></script>
<script>
function shuaxin(){window.location.reload()}
function xztm(tmid){
  if(window.confirm("确实要选择该题目吗？")){
    sendRequest("/byxt/student/xztm","tmid="+tmid,'POST',showresult);
  }
}
function showresult(){
  if(httpRequest.readyState==4&&httpRequest.status==200){
    var info=httpRequest.responseText;
    if(info=='0'){alert("该题目已经被选了，请选择别的题目")}
    else{alert("选题成功，请点击选题结果查看详细信息")}
    window.location.reload();
  }
}
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
  <div class="tip">列表每30秒自动刷新 &nbsp;<a href="javascript:shuaxin()">手动刷新</a></div>
  <div class="header-bar">
    <h2>可选题目</h2>
    <span class="badge">剩余 ${kxgs} 个</span>
  </div>
  <c:if test="${kxgs==0}"><div class="empty-hint"><span class="icon">📭</span>暂无可选题目，请等待教师出题</div></c:if>
  <c:if test="${kxgs>0}">
  <div class="table-wrap">
  <table>
  <thead><tr><th>教师</th><th>题目</th><th>备注</th><th>操作</th></tr></thead>
  <tbody>
  <c:forEach items="${tmList}" var="tm">
  <tr><td>${tm.txm}</td><td>${tm.tm}</td><td>${tm.bz}</td><td><button class="btn-sm" onclick="xztm('${tm.id}')">选择</button></td></tr>
  </c:forEach>
  </tbody></table>
  </div>
  </c:if>
</div>
</body>
</html>
