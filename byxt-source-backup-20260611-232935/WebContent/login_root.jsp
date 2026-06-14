<%@ page  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/xgxt_login.css" />
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
}
);
function qingkong(){
  $("#msg").html("");
}
</script>
<title>用户登录-毕业设计选题系统</title>
</head>
<body>

<div id="header">
	<div class="header_title">
		<span class="title_con">毕业设计选题系统---root</span>
	</div>
</div>

<div id="content">
	<center>
		<div class="con">
		<div class="con_title">
			<span class="con_title_sp">系统登录</span>
		</div>
		<form action="/byxt/login" method="post" id="dlform">
		<div class="con_panel">
			<div class="con_input">
				<span>用户名：</span><input type="text" name="userid" id="userid" onclick="qingkong()"/>
			</div>
			<div class="con_input">
				<span>密&nbsp;&nbsp;&nbsp;&nbsp;码：</span><input type="password" name="userpwd" id="userpwd" onclick="qingkong()"/>
			</div>
			<input type="hidden" name="sf" value="root" />
			<input type="button" value="登    录" class="submit-btn" id="dl"/>
		</div>
		</form>
		<div id="msg" style="color:red">${msg}</div>
	</div>
	</center>
</div>



</body>
</html>
