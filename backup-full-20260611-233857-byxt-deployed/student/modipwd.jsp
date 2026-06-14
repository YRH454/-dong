<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>修改密码</title>
<style>
:root{--g:#2E7D32;--gl:#4CAF50;--w:#fff;--d:#333}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#f5f7f5;color:var(--d);font-size:15px;padding:20px 14px}
h3{font-size:20px;color:var(--g);margin-bottom:18px}
.form-card{background:var(--w);border-radius:13px;padding:20px 16px;box-shadow:0 1px 6px rgba(0,0,0,.04)}
.fg{margin-bottom:14px}
.fg label{display:block;font-size:13px;color:#888;margin-bottom:4px}
.fg input{width:100%;height:46px;padding:0 14px;border:1.5px solid #e0e0e0;border-radius:10px;font-size:16px;outline:none;font-family:inherit;transition:border .15s}
.fg input:focus{border-color:var(--gl);box-shadow:0 0 0 3px rgba(76,175,80,.08)}
.btn{width:100%;height:48px;border:none;border-radius:24px;background:var(--gl);color:var(--w);font-size:17px;font-weight:600;cursor:pointer;-webkit-tap-highlight-color:transparent;box-shadow:0 4px 12px rgba(76,175,80,.2);transition:all .15s}
.btn:active{transform:scale(.97);background:var(--g)}
@media(min-width:600px){body{max-width:440px;margin:0 auto;padding:24px 0}}
</style>
<script>
function modi(){
  var p1=document.getElementById("pwd1").value, p2=document.getElementById("pwd2").value;
  if(p1==''||p2==''){alert("新密码不能为空");return}
  if(p1!=p2){alert("两次密码不一致");return}
  if(confirm("确认修改密码？")) document.getElementById("form1").submit();
}
</script>
</head>
<body>
<h3>🔑 修改密码</h3>
<div class="form-card">
<form id="form1" action="/byxt/student/modipwd" method="post">
  <div class="fg"><label>新密码</label><input type="password" name="pwd1" id="pwd1" placeholder="请输入新密码"></div>
  <div class="fg"><label>确认密码</label><input type="password" name="pwd2" id="pwd2" placeholder="请再次输入"></div>
  <input type="button" value="确认修改" class="btn" onclick="modi()">
</form>
</div>
</body>
</html>
