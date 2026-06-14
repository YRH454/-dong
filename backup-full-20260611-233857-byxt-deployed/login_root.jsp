<%@ page  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>超级管理员登录-毕业设计选题系统</title>
<style>
*{margin:0;padding:0;box-sizing:border-box}
:root{--red:#c62828;--dark:#333;--white:#fff;--border:#ddd;--bg:#faf5f5;--card-bg:#fff;--shadow:0 6px 30px rgba(0,0,0,.12);--radius:14px}

body{font-family:-apple-system,BlinkMacSystemFont,"Segoe UI","PingFang SC","Microsoft YaHei",sans-serif;background:linear-gradient(135deg,#ffebee,#ef9a9a,#e57373);min-height:100vh;display:flex;flex-direction:column;align-items:center}

.header{width:100%;padding:18px 20px 12px;text-align:center}
.header span{font-size:clamp(22px,5vw,32px);font-weight:700;color:var(--dark);letter-spacing:2px;position:relative}
.header span:after{content:'';display:block;width:40px;height:3px;background:var(--red);margin:8px auto 0;border-radius:2px}

.card{width:92%;max-width:420px;background:var(--card-bg);border-radius:var(--radius);box-shadow:var(--shadow);margin:10px 0 30px;overflow:hidden}
.card-title{background:linear-gradient(135deg,#c62828,#ef5350);color:var(--white);text-align:center;padding:18px;font-size:clamp(18px,4vw,22px);font-weight:600;letter-spacing:3px}
.card-body{padding:24px 20px 16px}

.form-group{margin-bottom:18px}
.form-group input[type=text],
.form-group input[type=password]{width:100%;height:48px;border:1.5px solid var(--border);border-radius:10px;padding:0 14px;font-size:16px;transition:border .2s;outline:none;background:#fafafa}
.form-group input:focus{border-color:var(--red);background:var(--white);box-shadow:0 0 0 3px rgba(198,40,40,.1)}

.badge{display:inline-block;background:#fff3e0;color:#e65100;padding:6px 16px;border-radius:20px;font-size:13px;font-weight:600;margin-bottom:18px;text-align:center;width:100%}

.btn-submit{width:100%;height:50px;background:linear-gradient(135deg,#c62828,#ef5350);color:var(--white);border:none;border-radius:25px;font-size:18px;font-weight:600;letter-spacing:4px;cursor:pointer;transition:all .2s;-webkit-tap-highlight-color:transparent;box-shadow:0 4px 14px rgba(198,40,40,.3)}
.btn-submit:active{transform:scale(.97)}

.msg-error{color:#e53935;text-align:center;margin-top:8px;font-size:14px;min-height:22px}

@media(min-width:768px){.card{width:420px}}
</style>
<script src="${pageContext.request.contextPath}/js/jquery1.js"></script>
<script>
$(function(){
 $("#dl").click(function(){
   if($("#userid").val().trim()==''||$("#userpwd").val().trim()==''){
     alert("用户名或密码不能为空");
   }
   else{
     $("#dlform").submit();
   }
 });
});
function qingkong(){
  $("#msg").html("");
}
</script>
</head>
<body>

<div class="header">
  <span>🔐 超级管理员</span>
</div>

<div class="card">
  <div class="card-title">系统登录</div>
  <div class="card-body">
    <form action="/byxt/login" method="post" id="dlform">

      <div class="badge">⚠ 超级管理员入口 — 仅限系统维护使用</div>

      <div class="form-group">
        <input type="text" name="userid" id="userid" onclick="qingkong()" placeholder="请输入用户名" />
      </div>
      <div class="form-group">
        <input type="password" name="userpwd" id="userpwd" onclick="qingkong()" placeholder="请输入密码" />
      </div>

      <input type="hidden" name="sf" value="root" />
      <input type="button" value="登　录" class="btn-submit" id="dl"/>
    </form>
    <% String msg=(String)request.getAttribute("msg"); %>
<div id="msg" class="msg-error"><%=msg!=null?msg:""%></div>
  </div>
</div>

</body>
</html>
