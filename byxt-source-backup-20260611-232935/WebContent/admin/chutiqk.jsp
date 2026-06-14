<%@ page     pageEncoding="UTF-8" import="util.JdbcUtil,java.sql.*,vo.*,java.util.*"%> 
<!DOCTYPE html>
<html>
<head>
 <style type="text/css">
table,td,th{
border:2px solid gray;
border-collapse : collapse ;
text-align : center ;
padding : 10px ;
font-weight : bold ;
}
table{
margin:0 auto;
}
body{
text-align : center ;
font-size:20px;
}

</style>
</head>
<body>
<%
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
String adminid=currentUser.getAdminid();
String condition=" where adminid='"+adminid+"'";

Connection con=JdbcUtil.getConnection();

//学生数
String sql1="select   count(*) from student"+condition;
PreparedStatement pst1=con.prepareStatement(sql1);
ResultSet rs1=pst1.executeQuery();
rs1.next();
int studentCnt=rs1.getInt(1);
rs1.close();pst1.close();
out.print("学生人数："+studentCnt+", ");

//出题上限总数
String sql2="select  sum(shangxian) from teacher "+condition;
PreparedStatement pst2=con.prepareStatement(sql2);
ResultSet rs2=pst2.executeQuery();
rs2.next();
int shangxianCnt=rs2.getInt(1);
rs2.close();pst2.close();
out.print("出题上限总数："+shangxianCnt+", ");

//已出题总数
String sql3="select   count(*) from tm"+condition;
PreparedStatement pst3=con.prepareStatement(sql3);
ResultSet rs3=pst3.executeQuery();
rs3.next();
int chutiCnt=rs3.getInt(1);
rs3.close();pst3.close();
out.print("已出题："+chutiCnt+"<br><br>");

//每个老师出题数
String sql4="select   gh,count(*) from tm"+condition +" group by gh";
PreparedStatement pst4=con.prepareStatement(sql4);
ResultSet rs4=pst4.executeQuery();
HashMap<String,Integer> hm1=new HashMap<String,Integer>();

while(rs4.next()){
	String gh=rs4.getString(1);
	int xuanti=rs4.getInt(2);
	
	hm1.put(gh,xuanti);
}
rs4.close();pst4.close();

//每个老师选题数
String sql5="select   gh,count(*) from tm"+condition+" and xh!='' " +" group by gh";
PreparedStatement pst5=con.prepareStatement(sql5);
ResultSet rs5=pst5.executeQuery();
HashMap<String,Integer> hm2=new HashMap<String,Integer>();

while(rs5.next()){
	String gh=rs5.getString(1);
	int xuanti=rs5.getInt(2);
	
	hm2.put(gh,xuanti);
}
rs5.close();pst5.close();

%>
<table>
<tr><th>序号</th><th>工号</th><th>姓名</th><th>出题上限</th><th>实际出题</th><th>选题数</th></tr>
<%
String sql6="select   gh,xm,shangxian from teacher "+condition;
PreparedStatement pst6=con.prepareStatement(sql6);
ResultSet rs6=pst6.executeQuery();
int xuhao=0;
while(rs6.next()){
	
	String gh=rs6.getString(1);
	String txm=rs6.getString(2);
	int shangxian=rs6.getInt(3);
	int chuti=hm1.getOrDefault(gh, 0);
	int xuanti=hm2.getOrDefault(gh, 0);
	xuhao++;
	if(chuti<shangxian){
		out.print("<tr style='color:red'><td>"+xuhao+"</td><td>"+gh+"</td><td>"+txm+"</td><td>"+shangxian+"</td><td>"+chuti+"</td><td>"+xuanti+"</td></tr>");
	}
	else{
		out.print("<tr><td>"+xuhao+"</td><td>"+gh+"</td><td>"+txm+"</td><td>"+shangxian+"</td><td>"+chuti+"</td><td>"+xuanti+"</td></tr>");
	}
	
}

rs6.close();pst6.close();
con.close();
%>
</table>
</body>
</html>