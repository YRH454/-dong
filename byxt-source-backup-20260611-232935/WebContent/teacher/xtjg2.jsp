<%@ page  pageEncoding="UTF-8" import="dao.*,vo.*,java.util.*"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
  <script src="/byxt/js/ajax.js"></script>
<style>
  body{
      text-align:center;
 font-family: 微软雅黑;
 font-size:16px;

  }
  .dt th,.dt td{
  border:2px solid gray;
  text-align:center;
  padding:3px 10px;
  }
  table.dt{
  border-collapse:collapse; 
  margin:0 auto;
  }
  table{
   margin:0 auto;
  }
  #updatetmForm{
  padding:20px;
  display:none;
  position:fixed;
  top:50%;
  left:30%;
  width:600px;
  height:250px;
  z-index:1000;
  margin-left:-200px;
  margin-top:-200px;
   background: #88f;
  }
 </style>
 <script language="javascript"> 
  function update(id,tm,bz,xh,sxm,zy,bj){
   var updatetm= document.getElementById("updatetmForm");
   document.getElementById("gx").disabled=false;
document.getElementById("gx").onclick=gengxin;
  updatetm.tmid.value=id;
  updatetm.tm.value=tm;
   updatetm.bz.value=bz;
    updatetm.xh.value=xh;
     updatetm.sxm.value=sxm;
      updatetm.zy.value=zy;
       updatetm.bj.value=bj;
       updatetm.style.display='block';
 
 
 }
 function jiance(){
 var xh=document.getElementById("xh").value;
 var url="/byxt/teacher/jcxh";
 sendRequest(url,"xh="+xh,'POST',showresult1);	
 }
 function showresult1(){
       if (httpRequest.readyState == 4) { 
			if (httpRequest.status == 200) {
				var info=httpRequest.responseText.split(",");
				if(info[0]=='0'){
				  alert("无此学号");
				}				
				else{
				 document.getElementById("sxm").value=info[1];
				  document.getElementById("zy").value=info[2];
				   document.getElementById("bj").value=info[3];
				}
			}
		}
		
}
 function gengxin(){
 var xh=document.getElementById("xh").value;
 var sxm=document.getElementById("sxm").value;
 var zy=document.getElementById("zy").value;
 var bj=document.getElementById("bj").value; 
 var tmid=document.getElementById("tmid").value;
 if(xh==''&& sxm=='' && zy=='' && bj==''){
   document.getElementById("updatetmForm").submit();
 }
 else{
  
  var url="/byxt/teacher/tmupdate";
  sendRequest(url,"xh="+xh+"&sxm="+sxm+"&zy="+zy+"&bj="+bj+"&tmid="+tmid,'POST',showresult2);	
 }
}
 function showresult2(){
       if (httpRequest.readyState == 4) { 
			if (httpRequest.status == 200) {
				var info=httpRequest.responseText;
				if(info=='2'){
				   document.getElementById("updatetmForm").submit();
				}
				else if(info=='0'){
				 alert('学号、姓名、专业、班级不匹配');
				}
				else{
				  alert('该学号已经有选题记录');
				}
			}
		}
		
}
 function guanbi(){
	 document.getElementById("updatetmForm").style.display="none";
 }
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
tm.setTm(request.getParameter("tm"));
tm.setBz(request.getParameter("bz"));
tm.setXh(request.getParameter("xh"));
tm.setSxm(request.getParameter("sxm"));
tm.setZy(request.getParameter("zy"));
tm.setBj(request.getParameter("bj"));
tmDao.update(tm);   
}
else if("del".equals(op)){
tmDao.delete(Integer.parseInt(request.getParameter("id")));
}
 CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
 String adminid=currentUser.getAdminid();
 String condition1=" where gh='"+currentUser.getId()+"' and adminid='"+adminid+"'";
List<TmEx> tmExList=tmDao.query(condition1,adminid);
String condition2=" where gh='"+currentUser.getId()+"' and xh!=''  and adminid='"+adminid+"'";
int xtrs=tmDao.getRecordCount(condition2);
pageContext.setAttribute("tmExList",tmExList);
pageContext.setAttribute("xtrs",xtrs);
 %>
 <h2>选题结果</h2> 
 <h3>（联系好的学生，在对应题目上点击修改链接-在弹出的修改表单中输入学号-点击检测学号-核对后点击更新按钮.)</h3>

 目前选题人数：${xtrs} <br>
   <table class="dt">
   <tr>
   <th>工号</th><th>教师姓名</th><th>题目</th><th>备注</th><th>学生学号</th>
   <th>姓名</th><th>专业</th><th>班级</th><th>email</th><th>phone</th><th>QQ</th>
    <th>修改</th><th>删除</th>
   </tr>
  <c:forEach items="${tmExList}" var="tmEx">
  <tr>
   <td>${tmEx.gh}</td><td>${tmEx.txm}</td><td>${tmEx.tm}</td><td>${tmEx.bz}</td>
   <td>${tmEx.xh}</td><td>${tmEx.sxm}</td><td>${tmEx.zy}</td>
  <td>${tmEx.bj}</td><td>${tmEx.semail}</td><td>${tmEx.sphone}</td><td>${tmEx.sqq}</td>
   <td><a href="javascript:update(${tmEx.id},'${tmEx.tm}','${tmEx.bz}','${tmEx.xh}','${tmEx.sxm}','${tmEx.zy}','${tmEx.bj}');">修改</a></td>
   <td><a href='xtjg2.jsp?op=del&id=${tmEx.id}' onclick="return confirm('确实要删除该 记录吗？')">删除</a></td>
  </tr>
 </c:forEach>
 
 </table>
 <br> 
    <form  method="post" id="updatetmForm" action="xtjg2.jsp?action=update">
    
   <table>
   
    <tr><td>题目</td><td><input type="text" name="tm" id="mc" size="70"/></td></tr>
     <tr><td>备注</td><td><textarea name="bz" id="bz" rows="4" cols="50"></textarea></td></tr>
     <tr><td>学号</td><td><input type="text" name="xh" id="xh" /><input type="button" onclick="jiance()" value="检测学号"></td></tr>     
     <tr><td>姓名</td><td><input type="text" name="sxm" id="sxm" /></td></tr>    
     <tr><td>专业</td><td><input type="text" name="zy" id="zy"  /></td></tr>    
     <tr><td>班级</td><td><input type="text" name="bj" id="bj"  /></td></tr>       
     <tr>
     <td><input type="button" id="gx" value="更新" disabled style="width:60px;height:28px" /></td>
     <td>&nbsp;&nbsp;<input type="reset" value="重置" style="width:60px;height:28px"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="关闭" onclick="guanbi()" style="width:60px;height:28px"/></td></tr>
   
    </table>
     <input type="hidden" name="tmid" id="tmid"  />
      <input type="hidden" name="op" value="update"/>
    </form>
    
</body>
</html>
