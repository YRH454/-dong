<%@ page  pageEncoding="UTF-8" import="java.util.*,dao.UserDao,vo.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>选题系统--用户登录</title>
<meta name="title" content="选题系统，毕业设计选题系统"/>
<meta name="keywords" content="选题系统，毕业设计选题系统"/>
<style>
*{margin:0;padding:0;box-sizing:border-box}
:root{--green:#4CAF50;--dark:#333;--white:#fff;--border:#ddd;--bg:#f0f4f0;--card-bg:#fff;--shadow:0 6px 30px rgba(0,0,0,.12);--radius:14px}

body{font-family:-apple-system,BlinkMacSystemFont,"Segoe UI","PingFang SC","Microsoft YaHei",sans-serif;background:linear-gradient(135deg,#e8f5e9,#c8e6c9,#a5d6a7);min-height:100vh;display:flex;flex-direction:column;align-items:center}

/* header */
.header{width:100%;padding:18px 20px 12px;text-align:center}
.header span{font-size:clamp(22px,5vw,32px);font-weight:700;color:var(--dark);letter-spacing:2px;position:relative}
.header span:after{content:'';display:block;width:40px;height:3px;background:var(--green);margin:8px auto 0;border-radius:2px}

/* login card */
.card{width:92%;max-width:420px;background:var(--card-bg);border-radius:var(--radius);box-shadow:var(--shadow);margin:10px 0 30px;overflow:hidden}
.card-title{background:linear-gradient(135deg,#43a047,#66bb6a);color:var(--white);text-align:center;padding:18px;font-size:clamp(18px,4vw,22px);font-weight:600;letter-spacing:3px}
.card-body{padding:24px 20px 16px}

/* form */
.form-group{margin-bottom:18px}
.form-group input[type=text],
.form-group input[type=password]{width:100%;height:48px;border:1.5px solid var(--border);border-radius:10px;padding:0 14px;font-size:16px;transition:border .2s;outline:none;background:#fafafa}
.form-group input:focus{border-color:var(--green);background:var(--white);box-shadow:0 0 0 3px rgba(76,175,80,.12)}

.radio-group{display:flex;gap:10px;flex-wrap:wrap;margin-bottom:18px;justify-content:center}
.radio-group label{display:flex;align-items:center;gap:6px;padding:8px 16px;border:1.5px solid var(--border);border-radius:24px;font-size:15px;cursor:pointer;transition:all .2s;background:#fafafa;-webkit-tap-highlight-color:transparent}
.radio-group label:has(input:checked){border-color:var(--green);background:#e8f5e9;color:#2e7d32;font-weight:600}
.radio-group input[type=radio]{accent-color:var(--green);width:16px;height:16px}

.select-group{margin-bottom:20px;display:flex;align-items:center;gap:8px;justify-content:center;flex-wrap:wrap}
.select-group span{font-size:15px;color:#666}
.select-group select{height:42px;border:1.5px solid var(--border);border-radius:10px;padding:0 12px;font-size:15px;outline:none;background:#fafafa;min-width:140px}
.select-group select:focus{border-color:var(--green)}

.btn-submit{width:100%;height:50px;background:linear-gradient(135deg,#43a047,#66bb6a);color:var(--white);border:none;border-radius:25px;font-size:18px;font-weight:600;letter-spacing:4px;cursor:pointer;transition:all .2s;-webkit-tap-highlight-color:transparent;box-shadow:0 4px 14px rgba(76,175,80,.3)}
.btn-submit:active{transform:scale(.97);box-shadow:0 2px 8px rgba(76,175,80,.3)}

.msg-error{color:#e53935;text-align:center;margin-top:8px;font-size:14px;min-height:22px}

/* footer */
.footer{text-align:center;font-size:12px;color:#999;padding:0 0 20px}

@media(min-width:768px){
  .card{width:420px}
}

/* fix for count2 counter images */
.footer img{vertical-align:middle}
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

<%
UserDao userDao=new UserDao();
List<User> userList=userDao.query();
pageContext.setAttribute("userList",userList);
%>

<div class="header">
  <span>📚 选题系统</span>
</div>

<div class="card">
  <div class="card-title">系统登录</div>
  <div class="card-body">
    <form action="/byxt/login" method="post" id="dlform">

      <div class="form-group">
        <input type="text" name="userid" id="userid" onclick="qingkong()" placeholder="请输入用户名" />
      </div>
      <div class="form-group">
        <input type="password" name="userpwd" id="userpwd" onclick="qingkong()" placeholder="请输入密码" />
      </div>

      <div class="radio-group">
        <label><input type="radio" name="sf" value="student" checked />🎓 学生</label>
        <label><input type="radio" name="sf" value="teacher" />🎯 教师</label>
        <label><input type="radio" name="sf" value="admin" />⚙️ 管理员</label>
      </div>

      <div class="select-group">
        <span>部门:</span>
        <select name="dp">
          <c:forEach items="${userList}" var="user">
            <option value="${user.userid},${user.dp}">${user.dp}</option>
          </c:forEach>
        </select>
      </div>

      <input type="button" value="登　录" class="btn-submit" id="dl"/>
    </form>
    <% String msg=(String)request.getAttribute("msg"); %>
<div id="msg" class="msg-error"><%=msg!=null?msg:""%></div>
  </div>
</div>

<div class="footer"><%@include file="count2.jsp"%></div>

</body>
</html>
