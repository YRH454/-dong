<%@ page pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>选题结果</title>
<style>
:root{--b:#1565C0;--bl:#1E88E5;--bbg:#E3F2FD;--w:#fff;--d:#333;--g:#888}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f4f6f9;color:var(--d);font-size:14px;padding:8px 10px}
h3{font-size:16px;color:var(--b);margin-bottom:8px}
.stat-bar{display:flex;gap:8px;margin-bottom:10px;flex-wrap:wrap;align-items:center}
.stat-bar .count{background:var(--bbg);color:var(--b);padding:4px 12px;border-radius:14px;font-size:13px;font-weight:600}
.tip-text{font-size:12px;color:var(--g);margin-bottom:10px;line-height:1.5}
.table-wrap{-webkit-overflow-scrolling:touch;overflow-x:auto;margin:0 -4px;padding:0 4px}
table.dt{width:100%;border-collapse:collapse;min-width:900px;font-size:13px;background:var(--w);border-radius:8px;overflow:hidden;box-shadow:0 1px 4px rgba(0,0,0,.04)}
table.dt th{background:var(--b);color:var(--w);padding:8px 6px;font-weight:600;font-size:11px;white-space:nowrap}
table.dt td{padding:8px 6px;text-align:center;border-bottom:1px solid #f0f0f0;font-size:13px}
table.dt tr:active{background:var(--bbg)}
.action-link{color:var(--bl);font-weight:500;text-decoration:none;cursor:pointer;margin:0 3px}
.action-link.danger{color:#C62828}

/* Popup overlay */
.popup-mask{display:none;position:fixed;inset:0;background:rgba(0,0,0,.4);z-index:200;justify-content:center;align-items:center}
.popup-mask.on{display:flex}
.popup-box{background:var(--w);border-radius:14px;box-shadow:0 8px 32px rgba(0,0,0,.15);width:94%;max-width:480px;max-height:90vh;overflow-y:auto}
.popup-hd{background:var(--b);color:var(--w);padding:14px 18px;font-weight:700;font-size:16px;display:flex;justify-content:space-between;align-items:center;border-radius:14px 14px 0 0}
.popup-hd .close-btn{width:32px;height:32px;border:none;background:rgba(255,255,255,.2);color:var(--w);border-radius:50%;font-size:18px;cursor:pointer}
.popup-bd{padding:14px}
.popup-bd table{width:100%}
.popup-bd td{padding:6px 4px;font-size:14px}
.popup-bd td:first-child{text-align:right;color:var(--g);width:60px;font-size:13px}
.popup-bd input[type=text],.popup-bd textarea{width:100%;padding:8px 10px;border:1.5px solid #e0e0e0;border-radius:8px;font-size:14px;font-family:inherit;outline:none;transition:border .15s}
.popup-bd input:focus,.popup-bd textarea:focus{border-color:var(--bl)}
.popup-bd textarea{height:60px;resize:vertical}
.popup-bd .btn-row{display:flex;gap:8px;justify-content:flex-end;margin-top:10px}
.popup-bd .btn-row button{height:36px;padding:0 18px;border:none;border-radius:18px;font-size:13px;font-weight:600;cursor:pointer}
.popup-bd .btn-save{background:var(--bl);color:var(--w)}
.popup-bd .btn-reset{background:#eee;color:var(--d)}
.popup-bd .btn-close{background:#eee;color:var(--d)}
@media(min-width:768px){body{padding:12px 16px}table.dt{min-width:auto}}
</style>
<script src="/byxt/js/ajax.js"></script>
<script>
function update(id,tm,bz,xh,sxm,zy,bj){
  document.getElementById("gx").disabled=false;
  document.getElementById("gx").onclick=gengxin;
  document.getElementById("tmid").value=id;
  document.getElementById("mc").value=tm;
  document.getElementById("bz").value=bz;
  document.getElementById("xh").value=xh;
  document.getElementById("sxm").value=sxm;
  document.getElementById("zy").value=zy;
  document.getElementById("bj").value=bj;
  document.getElementById("popup").classList.add('on');
}
function jiance(){
  var xh=document.getElementById("xh").value;
  sendRequest("/byxt/teacher/jcxh","xh="+xh,'POST',showresult1);
}
function showresult1(){
  if(httpRequest.readyState==4&&httpRequest.status==200){
    var info=httpRequest.responseText.split(",");
    if(info[0]=='0'){alert("无此学号")}
    else{document.getElementById("sxm").value=info[1];document.getElementById("zy").value=info[2];document.getElementById("bj").value=info[3];}
  }
}
function gengxin(){
  var xh=document.getElementById("xh").value, sxm=document.getElementById("sxm").value;
  var zy=document.getElementById("zy").value, bj=document.getElementById("bj").value, tmid=document.getElementById("tmid").value;
  if(xh==''&&sxm==''&&zy==''&&bj==''){document.getElementById("updatetmForm").submit()}
  else{sendRequest("/byxt/teacher/tmupdate","xh="+xh+"&sxm="+sxm+"&zy="+zy+"&bj="+bj+"&tmid="+tmid,'POST',showresult2)}
}
function showresult2(){
  if(httpRequest.readyState==4&&httpRequest.status==200){
    var info=httpRequest.responseText;
    if(info=='2'){document.getElementById("updatetmForm").submit()}
    else if(info=='0'){alert('学号、姓名、专业、班级不匹配')}
    else{alert('该学号已经有选题记录')}
  }
}
function guanbi(){document.getElementById("popup").classList.remove('on')}
</script>
</head>
<body>
<%
request.setCharacterEncoding("UTF-8");
TmDao tmDao=new TmDao();
String op=request.getParameter("op");
if("update".equals(op)){
  Tm tm=new Tm();
  tm.setId(Integer.parseInt(request.getParameter("tmid")));
  tm.setTm(request.getParameter("tm")); tm.setBz(request.getParameter("bz"));
  tm.setXh(request.getParameter("xh")); tm.setSxm(request.getParameter("sxm"));
  tm.setZy(request.getParameter("zy")); tm.setBj(request.getParameter("bj"));
  tmDao.update(tm);
}else if("del".equals(op)){
  tmDao.delete(Integer.parseInt(request.getParameter("id")));
}
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
String adminid=currentUser.getAdminid();
String c1=" where gh='"+currentUser.getId()+"' and adminid='"+adminid+"'";
List<TmEx> tmExList=tmDao.query(c1,adminid);
String c2=" where gh='"+currentUser.getId()+"' and xh!='' and adminid='"+adminid+"'";
int xtrs=tmDao.getRecordCount(c2);
pageContext.setAttribute("tmExList",tmExList);
pageContext.setAttribute("xtrs",xtrs);
%>
<div class="stat-bar"><h3>📋 选题结果</h3><span class="count">已选 ${xtrs} 人</span></div>
<div class="tip-text">联系好学生后，点击「修改」→ 输入学号 → 点「检测学号」→ 核对信息 → 点「更新」</div>
<div class="table-wrap">
<table class="dt">
<thead><tr><th>工号</th><th>教师</th><th>题目</th><th>备注</th><th>学号</th><th>学生</th><th>专业</th><th>班级</th><th>操作</th></tr></thead>
<tbody>
<c:forEach items="${tmExList}" var="tmEx">
<tr>
  <td>${tmEx.gh}</td><td>${tmEx.txm}</td><td>${tmEx.tm}</td><td>${tmEx.bz}</td>
  <td>${tmEx.xh}</td><td>${tmEx.sxm}</td><td>${tmEx.zy}</td><td>${tmEx.bj}</td>
  <td>
    <a class="action-link" href="javascript:update(${tmEx.id},'${tmEx.tm}','${tmEx.bz}','${tmEx.xh}','${tmEx.sxm}','${tmEx.zy}','${tmEx.bj}');">修改</a>
    <a class="action-link danger" href='xtjg2.jsp?op=del&id=${tmEx.id}' onclick="return confirm('确认删除？')">删除</a>
  </td>
</tr>
</c:forEach>
</tbody></table>
</div>

<!-- Popup -->
<div class="popup-mask" id="popup">
<div class="popup-box">
  <div class="popup-hd">修改题目信息 <button class="close-btn" onclick="guanbi()">&times;</button></div>
  <div class="popup-bd">
    <form method="post" id="updatetmForm" action="xtjg2.jsp?op=update">
    <table>
    <tr><td>题目</td><td><input type="text" name="tm" id="mc"></td></tr>
    <tr><td>备注</td><td><textarea name="bz" id="bz"></textarea></td></tr>
    <tr><td>学号</td><td style="display:flex;gap:8px"><input type="text" name="xh" id="xh" style="flex:1"><button type="button" onclick="jiance()" style="height:34px;padding:0 12px;border:none;border-radius:8px;background:var(--bl);color:var(--w);font-size:12px;white-space:nowrap">检测</button></td></tr>
    <tr><td>姓名</td><td><input type="text" name="sxm" id="sxm"></td></tr>
    <tr><td>专业</td><td><input type="text" name="zy" id="zy"></td></tr>
    <tr><td>班级</td><td><input type="text" name="bj" id="bj"></td></tr>
    </table>
    <input type="hidden" name="tmid" id="tmid"><input type="hidden" name="op" value="update">
    <div class="btn-row">
      <button type="button" class="btn-close" onclick="guanbi()">关闭</button>
      <button type="reset" class="btn-reset">重置</button>
      <button type="button" class="btn-save" id="gx" disabled>更新</button>
    </div>
    </form>
  </div>
</div>
</div>
</body>
</html>
