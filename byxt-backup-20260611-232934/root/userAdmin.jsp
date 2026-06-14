<%@ page import="java.util.*,dao.*,vo.User,util.Encrypt" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">	
   
    <style>
    
  .dt th,.dt td{
  border:2px solid gray;
  text-align:center;
  padding:3px 5px;
  }
   table{
 
  margin:0 auto;
  }
  table.dt{
  border-collapse:collapse; 
  margin:0 auto;
  }
    body{
      padding:10px;
      text-align:center;
    }
  
    </style>
    <script src="${pageContext.request.contextPath}/js/jquery1.js"></script>
    <script>
  
 
 function update2(userid,dp,xuhao){
document.getElementById("userid2").value=userid;
document.getElementById("dp2").value=dp;
document.getElementById("xuhao").value=xuhao;
 }
  function checkadd(){
	  if($("#userid1").val().trim()==''||$("#dp1").val().trim()==''){
		     alert("用户名或部门名称不能为空");
		   }
		   else{
		     $("#addForm").submit();
		   }
  }
  function checkupdate(){
	  if($("#dp2").val().trim()==''||$("#xuhao").val().trim()==''){
		     alert("部门名称或序号不能为空");
		   }
		   else{
		     $("#updateForm").submit();
		   }
  }
 
  
  </script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mobile-app.css" />
</head>
	<body><div class="content-page">

<h2>管理员信息</h2>
<%
request.setCharacterEncoding("UTF-8");
UserDao userDao=new UserDao();
String action=request.getParameter("action");

if(action!=null){
	if("add".equals(action)){
		String userid=request.getParameter("userid1");
		String dp=request.getParameter("dp1");
		User user=userDao.findUserByIdDp(userid,dp); 
		if(user==null){
			userDao.add(userid, dp);
			ZtDao ztDao=new ZtDao();
			ztDao.add(userid);
		}
		else{
			out.print("<script>alert('userid或部门已经存在');</script>");
		}
	}
	else if("update".equals(action)){
		String userid=request.getParameter("userid2");
		String dp=request.getParameter("dp2");
		int xuhao=Integer.parseInt(request.getParameter("xuhao"));
		User user=userDao.findUserByDp(dp);
		if(user!=null&&!user.getUserid().equals(userid)){
			out.print("<script>alert('部门已经存在');</script>");
		}
		else{
			userDao.modidpxuhao(userid, dp,xuhao);
		}
		
	}
	else if("del".equals(action)){
		String userid=request.getParameter("userid");
		userDao.delete(userid);
		ZtDao ztDao=new ZtDao();
		ztDao.delelte(userid);
		TeacherDao teacherDao=new TeacherDao();
		StudentDao studentDao=new StudentDao();
		TmDao tmDao=new TmDao();
		teacherDao.qingkong(userid);
		studentDao.qingkong(userid);
		tmDao.qingkong(userid);
		
	}
	else if("reset".equals(action)){
		String userid=request.getParameter("userid");
		userDao.modipwd(userid,Encrypt.MD5(userid));
		out.print("<script>alert('"+userid+"重置成功');</script>");
	}
}

%>
<%


List<User> userList=userDao.query();
pageContext.setAttribute("userList",userList);

%>
   <div class="table-wrap"><table class="dt">
   <tr><th>userId</th><th>dp</th><th>最后登录时间</th><th>序号</th><th>修改dp</th><th>删除</th><th>重置密码</th></tr>
  <c:forEach items="${userList}" var="user">
  <tr>
   <td>${user.userid}</td>
   <td>${user.dp}</td>
    <td>${user.lastlogin}</td>
     <td>${user.xuhao}</td>
   <td><a href="javascript:update2('${user.userid}','${user.dp}',${user.xuhao})">修改</a></td>
   <td><a href='/byxt/root/userAdmin.jsp?action=del&userid=${user.userid}' onclick="return confirm('确实要删除该记录吗？')">删除</a></td>
   <td><a href='/byxt/root/userAdmin.jsp?action=reset&userid=${user.userid}' onclick="return confirm('确实要重置密码吗？')">重置密码</a></td>
  </tr>
 </c:forEach>
 
 </table><br>
 <form method="post" id="addForm" action="userAdmin.jsp?action=add">
 userId:<input id="userid1" name="userid1">
 部门名称：<input type="text" class="form-input" name="dp1" id="dp1"/>
  
 <input type="button" value="添加" onclick="checkadd()"/>
 </form>
 
 <form method="post" id="updateForm" action="userAdmin.jsp?action=update">
 <span id="lb">修改部门名称</span>
 <input type="text" class="form-input" name="dp2" id="dp2"/><br>
  <span id="lb">修改序号</span>
 <input type="text" class="form-input" name="xuhao" id="xuhao"/>
 <input type="hidden" name="userid2" id="userid2" value=""/>
 
 <input type="button" value="修改" onclick="checkupdate()"/>
 </form>


</div></body>
</html>
