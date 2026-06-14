<%@ page pageEncoding="UTF-8" import="util.JdbcUtil,java.sql.*,vo.*,java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>出题情况</title>
<style>
:root{--o:#E65100;--ol:#FB8C00;--obg:#FFF3E0;--w:#fff;--d:#333}
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;background:#fdf7f3;color:var(--d);font-size:14px;padding:8px 10px}
.stat-row{display:grid;grid-template-columns:repeat(auto-fit,minmax(100px,1fr));gap:8px;margin-bottom:12px}
.stat-card{background:var(--w);border-radius:10px;padding:12px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.04);border-top:3px solid var(--ol)}
.stat-card .num{font-size:22px;font-weight:700;color:var(--o)}
.stat-card .lbl{font-size:11px;color:#888;margin-top:2px}
.table-wrap{-webkit-overflow-scrolling:touch;overflow-x:auto;margin:0 -4px;padding:0 4px}
table{width:100%;border-collapse:collapse;min-width:550px;font-size:13px;background:var(--w);border-radius:8px;overflow:hidden;box-shadow:0 1px 4px rgba(0,0,0,.04)}
thead th{background:var(--o);color:var(--w);padding:10px 8px;font-weight:600;font-size:12px;white-space:nowrap}
tbody td{padding:9px 8px;text-align:center;border-bottom:1px solid #f5f0ed;font-size:13px}
tbody tr:active{background:var(--obg)}
tr.warn{color:var(--o);font-weight:600}
tr.warn td{background:#FFF8F0}
@media(min-width:768px){body{padding:12px 16px}table{min-width:auto}}
</style>
</head>
<body>
<%
CurrentUser currentUser=(CurrentUser)session.getAttribute("currentUser");
String adminid=currentUser.getAdminid();
String condition=" where adminid='"+adminid+"'";
Connection con=JdbcUtil.getConnection();

String sql1="select count(*) from student"+condition;
PreparedStatement pst1=con.prepareStatement(sql1);
ResultSet rs1=pst1.executeQuery(); rs1.next();
int studentCnt=rs1.getInt(1); rs1.close();pst1.close();

String sql2="select sum(shangxian) from teacher "+condition;
PreparedStatement pst2=con.prepareStatement(sql2);
ResultSet rs2=pst2.executeQuery(); rs2.next();
int shangxianCnt=rs2.getInt(1); rs2.close();pst2.close();

String sql3="select count(*) from tm"+condition;
PreparedStatement pst3=con.prepareStatement(sql3);
ResultSet rs3=pst3.executeQuery(); rs3.next();
int chutiCnt=rs3.getInt(1); rs3.close();pst3.close();

String sql4="select gh,count(*) from tm"+condition+" group by gh";
PreparedStatement pst4=con.prepareStatement(sql4);
ResultSet rs4=pst4.executeQuery();
HashMap<String,Integer> hm1=new HashMap<>();
while(rs4.next()){hm1.put(rs4.getString(1),rs4.getInt(2));}
rs4.close();pst4.close();

String sql5="select gh,count(*) from tm"+condition+" and xh!='' group by gh";
PreparedStatement pst5=con.prepareStatement(sql5);
ResultSet rs5=pst5.executeQuery();
HashMap<String,Integer> hm2=new HashMap<>();
while(rs5.next()){hm2.put(rs5.getString(1),rs5.getInt(2));}
rs5.close();pst5.close();
%>
<div class="stat-row">
  <div class="stat-card"><div class="num"><%=studentCnt%></div><div class="lbl">学生人数</div></div>
  <div class="stat-card"><div class="num"><%=shangxianCnt%></div><div class="lbl">出题上限总数</div></div>
  <div class="stat-card"><div class="num"><%=chutiCnt%></div><div class="lbl">已出题目</div></div>
</div>
<div class="table-wrap">
<table>
<thead><tr><th>序号</th><th>工号</th><th>姓名</th><th>出题上限</th><th>实际出题</th><th>选题数</th></tr></thead>
<tbody>
<%
String sql6="select gh,xm,shangxian from teacher "+condition;
PreparedStatement pst6=con.prepareStatement(sql6);
ResultSet rs6=pst6.executeQuery();
int xuhao=0;
while(rs6.next()){
  String gh=rs6.getString(1), txm=rs6.getString(2);
  int shangxian=rs6.getInt(3);
  int chuti=hm1.getOrDefault(gh,0), xuanti=hm2.getOrDefault(gh,0);
  xuhao++;
  String cls=chuti<shangxian?" class='warn'":"";
  out.print("<tr"+cls+"><td>"+xuhao+"</td><td>"+gh+"</td><td>"+txm+"</td><td>"+shangxian+"</td><td>"+chuti+"</td><td>"+xuanti+"</td></tr>");
}
rs6.close();pst6.close();con.close();
%>
</tbody></table>
</div>
</body>
</html>
