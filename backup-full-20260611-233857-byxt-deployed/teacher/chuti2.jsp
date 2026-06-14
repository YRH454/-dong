<%@ page pageEncoding="UTF-8" import="dao.TmDao,dao.TeacherDao,vo.Tm,vo.CurrentUser,vo.Teacher"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>教师出题</title>
<style>
:root{--b:#1565C0;--bl:#1E88E5;--bbg:#E3F2FD;--w:#fff;--d:#333}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f4f6f9;color:var(--d);font-size:15px;padding:12px 14px}
.stat{display:flex;gap:10px;margin-bottom:16px;flex-wrap:wrap}
.stat .item{flex:1;min-width:100px;background:var(--w);border-radius:12px;padding:14px;text-align:center;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.stat .num{font-size:24px;font-weight:700;color:var(--bl)}
.stat .lbl{font-size:12px;color:#888;margin-top:2px}
.form-card{background:var(--w);border-radius:13px;padding:20px 16px;box-shadow:0 1px 6px rgba(0,0,0,.04)}
.fg{margin-bottom:14px}
.fg label{display:block;font-size:13px;color:#888;margin-bottom:4px}
.fg input,.fg textarea{width:100%;padding:0 14px;border:1.5px solid #e0e0e0;border-radius:10px;font-size:16px;outline:none;font-family:inherit;transition:border .15s}
.fg input{height:46px}
.fg textarea{height:100px;padding:12px 14px;resize:vertical}
.fg input:focus,.fg textarea:focus{border-color:var(--bl);box-shadow:0 0 0 3px rgba(30,136,229,.08)}
.btn{width:100%;height:48px;border:none;border-radius:24px;background:var(--bl);color:var(--w);font-size:17px;font-weight:600;cursor:pointer;-webkit-tap-highlight-color:transparent;box-shadow:0 4px 12px rgba(30,136,229,.2);transition:all .15s}
.btn:active{transform:scale(.97);background:var(--b)}
.msg{text-align:center;margin-top:12px;font-size:14px;color:#E65100}
.limit-warn{background:#FFF3E0;color:#E65100;padding:14px;border-radius:10px;text-align:center;font-size:14px}
@media(min-width:600px){body{max-width:520px;margin:0 auto;padding:16px 0}}
</style>
<script>
function chuti(){
  if(document.getElementById("tm").value==''){alert("请填写题目名称");return}
  document.getElementById("form1").submit();
}
</script>
</head>
<body>
<%
request.setCharacterEncoding("UTF-8");
String tm=request.getParameter("tm");
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
String gh=currentUser.getId(), adminid=currentUser.getAdminid();
TeacherDao teacherDao=new TeacherDao();
Teacher teacher=teacherDao.findTeacherByIdAdminid(gh,adminid);
int shangxian=teacher.getShangxian();
TmDao tmDao=new TmDao();
int yxsl=tmDao.getRecordCount(" where gh='"+gh+"' and adminid='"+adminid+"'");
if(yxsl>=shangxian){
  out.print("<div class='limit-warn'>题目数量已达上限（"+shangxian+"），无法继续出题</div>"); return;
}
if(tm!=null){
  Tm tm1=new Tm();
  tm1.setGh(gh); tm1.setTxm(currentUser.getMc());
  tm1.setTm(request.getParameter("tm")); tm1.setBz(request.getParameter("bz"));
  tmDao.add(tm1,adminid);
  pageContext.setAttribute("msg","添加成功，跳转中...");
  response.setHeader("refresh","2;url=chuti2.jsp");
}
%>
<div class="stat">
  <div class="item"><div class="num"><%=shangxian%></div><div class="lbl">题目上限</div></div>
  <div class="item"><div class="num"><%=yxsl%></div><div class="lbl">已出题目</div></div>
  <div class="item"><div class="num"><%=shangxian-yxsl%></div><div class="lbl">剩余可出</div></div>
</div>
<div class="form-card">
<form id="form1" action="" method="post">
  <div class="fg"><label>题目名称 *</label><input type="text" name="tm" id="tm" placeholder="请输入题目名称" onclick="document.getElementById('msg').innerHTML=''"></div>
  <div class="fg"><label>题目备注</label><textarea name="bz" placeholder="可选备注信息"></textarea></div>
  <input type="button" value="提交出题" class="btn" onclick="chuti()">
</form>
<% String msg=(String)request.getAttribute("msg"); if(msg!=null){ %><div id="msg" class="msg"><%=msg%></div><% } %>
</div>
</body>
</html>
