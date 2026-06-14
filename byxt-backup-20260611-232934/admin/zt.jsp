<%@ page pageEncoding="UTF-8" import="dao.ZtDao,vo.CurrentUser"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>选题状态</title>
<style>
:root{--o:#E65100;--ol:#FB8C00;--w:#fff;--d:#333;--g:#888}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#fdf7f3;color:var(--d);font-size:15px;padding:20px 14px}
h3{font-size:20px;color:var(--o);margin-bottom:16px}
.card{background:var(--w);border-radius:13px;padding:24px 20px;box-shadow:0 1px 6px rgba(0,0,0,.04);text-align:center}
.status-badge{display:inline-block;padding:10px 28px;border-radius:24px;font-size:16px;font-weight:700;margin-bottom:16px}
.status-on{background:#E8F5E9;color:#2E7D32}
.status-off{background:#FFEBEE;color:#C62828}
.desc{font-size:14px;color:var(--g);margin-bottom:16px}
.form-inline{display:flex;gap:8px;justify-content:center;flex-wrap:wrap;align-items:center}
.form-inline input[type=text]{width:100px;height:44px;text-align:center;border:1.5px solid #e0e0e0;border-radius:10px;font-size:20px;font-weight:700;outline:none;font-family:inherit}
.form-inline input:focus{border-color:var(--ol)}
.btn{height:44px;padding:0 24px;border:none;border-radius:22px;background:var(--ol);color:var(--w);font-size:16px;font-weight:600;cursor:pointer;-webkit-tap-highlight-color:transparent;transition:all .15s}
.btn:active{transform:scale(.97);background:var(--o)}
</style>
</head>
<body>
<%
ZtDao ztDao=new ZtDao();
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
String adminid=currentUser.getAdminid();
if(request.getParameter("zt")!=null){
  ztDao.update(Integer.parseInt(request.getParameter("zt")),adminid);
}
int zt=ztDao.query(adminid);
%>
<h3>选题状态控制</h3>
<div class="card">
  <div class="status-badge <%=zt==1?"status-on":"status-off"%>">
    <%=zt==1?"开放选题中":"选题已关闭"%>
  </div>
  <div class="desc">0 = 学生不能选题（仅查看） &nbsp;|&nbsp; 1 = 学生可以选题</div>
  <form action="" method="post">
    <div class="form-inline">
      <span>当前值：</span>
      <input type="text" value="<%=zt%>" name="zt">
      <input type="submit" value="修改" class="btn">
    </div>
  </form>
</div>
</body>
</html>
