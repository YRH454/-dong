<%@ page  pageEncoding="UTF-8" import="dao.TmDao,dao.TeacherDao,vo.Tm,vo.CurrentUser,vo.Teacher"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>
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
function chuti(){
  var tm=document.getElementById("tm").value;
  if(tm==''){
    alert("题目名称不能为空");
  }
  else{
   document.getElementById("form1").submit();
  }
}
</script>
  </head> 
  
  <body>
  <%
  request.setCharacterEncoding("UTF-8");
 
   CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
   String gh=currentUser.getId();
   String adminid=currentUser.getAdminid();
   TeacherDao teacherDao=new TeacherDao();
   Teacher teacher=teacherDao.findTeacherByIdAdminid(gh,adminid);
   int shangxian=teacher.getShangxian();
 TmDao tmDao=new TmDao();
   
   int yxsl=tmDao.getRecordCount(" where gh='"+gh+"' and adminid='"+adminid+"'");
   if(yxsl>=shangxian){
	   out.print("题目数量已达上限");
	   return;
   }
   
   String tm=request.getParameter("tm");
   if(tm!=null){
  
   Tm tm1=new Tm();  
   int n=Integer.parseInt(request.getParameter("tmgs"));
   tm1.setGh(currentUser.getId());
   tm1.setTxm(currentUser.getMc());
   //tm1.setTm(request.getParameter("tm"));
   String tmm=request.getParameter("tm");
   tm1.setBz(request.getParameter("bz"));
   for(int i=0;i<n;i++){
	   //tm1.setTm(tmm+(i+1));
	   tm1.setTm(tmm);
     tmDao.add(tm1,adminid);
   }
   pageContext.setAttribute("msg","添加成功，请到选题结果中查看已经添加的题目");
   response.setHeader("refresh","2;url=plchuti.jsp");
   
  }
  
   %>
  <h3>教师出题(每个题目只能对应一个学生，题目个数上限：<%=shangxian%>)</h3>
    <form id="form1" action="" method="post">
  题目数量：
  <select name="tmgs">
   
   <%
    for(int i=shangxian-yxsl;i>=1;i--){
    	out.print("<option value="+i+">&nbsp;"+i+"&nbsp;</option>");
    }
   %>
  </select>
  <br>
    题目名称：<input type="text" size="80" name="tm" id="tm" onclick="document.getElementById('msg').innerHTML=''"><br>
    题目备注：<textarea name="bz" rows="10" cols="80"></textarea><br>
     <input type="button" value="确定" onclick="chuti()"/>
    </form>
    <div id="msg" style="color:red">${msg}</div>
  </body>
</html>
