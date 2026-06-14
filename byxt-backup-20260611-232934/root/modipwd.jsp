<%@ page  pageEncoding="UTF-8"%>

<html>
  <head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <style>
  body{
  text-align:center;
  margin-top:5%;
 font-family: 微软雅黑;
 font-size:18px;
 line-height:2;
  }
 </style>
 
<script>
function modi(){
var pwd1=document.getElementById("pwd1").value;
var pwd2=document.getElementById("pwd2").value;
if(pwd1==''||pwd2==''){
  alert("新密码不能为空");
}
else if(pwd1!=pwd2){
   alert("两次密码不一致");
}
else{
 if(confirm("确实要修改密码吗")){
   document.getElementById("form1").submit();
 }
}
 
}
</script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mobile-app.css" />
</head>
  
  <body><div class="content-page">
  <h3>修改密码</h3>
    <form id="form1" action="/byxt/root/modipwd" method="post">
    输入新密码：<input type="password" name="pwd1" id="pwd1"><br>
    确认新密码：<input type="password" name="pwd2" id="pwd2"><br>
     <input type="button" value="确定" onclick="modi()"/>
    </form>
  </div></body>
</html>
