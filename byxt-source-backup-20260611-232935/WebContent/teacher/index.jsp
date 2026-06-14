<%@ page  pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>选题系统-教师端</title>
<link href="${pageContext.request.contextPath}/css/adminStyle.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery1.js"></script>
<script type="text/javascript">
	
	function openurl(url) {
		var rframe = document.getElementById("rightFrame");
		rframe.src = url;
	}
</script>
<style>
body {
	margin: 0;
	font-family: 微软雅黑;
	background-image: url(/byxt/images/.jpg);
	background-repeat: no-repea;
	background-size: cover;
	background-attachment: fixed;
	background-color: #ffffff
	
}

.top1 {
	position: absolute;
	top: 0px;
	width: 100%;
	height: 20px;
	text-align: center;
	color: #FFFFFF;
	font-size: 17px;
	font-height: 20px;
	font-family: 楷体;
	background-color: #888888
}

.title {
float:left;
    margin:-32px 20px;
	font-size: 40px;
	color: #FFFFFF;
	font-height: 55px;
	font-family: 隶书;
}

.top2 {
	position: absolute;
	top: 10px;
	width: 100%;
	height: 77px;
	text-align: center;
	color: #ccffff;
	background-color: #8EC172
}

.left {
	position: absolute;
	left: 0px;
	top: 87px;
	width: 200px;
	height: 90%;
	border-right: 1px solid #9370DB;
	color: #000000;
	font-size: 20px;
	text-align: center;
	background-color: f8f8f8
}

.right {
	position: absolute;
	left: 200px;
	top:97px;
	width: 85.2%;
	height: 85%;
	border-top: 0px solid #484860;
	font-size: 14px;
	text-align: center;
}

.end {
	position: absolute;
	bottom: 0px;
	width: 100%;
	height: 30px;
	text-align: center;
	color: #556B2F;
	font-size: 17px;
	font-height: 20px;
	font-family: 楷体;
	background-color: #C0C0C0
}

.div1 {
	text-align: center;
	width: 200px;
	padding-top: 10px;
}

.div2 {
	height: 40px;
	line-height: 40px;
	cursor: pointer;
	font-size: 18px;
	position: relative;
	border-bottom: #ccc 0px dotted;
}

.spgl {
	position: absolute;
	height: 20px;
	width: 20px;
	left: 40px;
	top: 10px;
	background: url(/byxt/images/1.png);
}

.yhgl {
	position: absolute;
	height: 20px;
	width: 20px;
	left: 40px;
	top: 10px;
	background: url(/byxt/images/4.png);
}

.gggl {
	position: absolute;
	height: 20px;
	width: 20px;
	left: 40px;
	top: 10px;
	background: url(/byxt/images/4.png);
}

.zlgl {
	position: absolute;
	height: 20px;
	width: 20px;
	left: 40px;
	top: 10px;
	background: url(/byxt/images/4.png);
}

.pjgl {
	position: absolute;
	height: 20px;
	width: 20px;
	left: 40px;
	top: 10px;
	background: url(/byxt/images/4.png);
}

.tcht {
	position: absolute;
	height: 20px;
	width: 20px;
	left: 40px;
	top: 10px;
	background: url(/byxt/images/2.png);
}

.div3 {
	display: none;
	cursor: pointer;
	font-size: 15px;
}

.div3 ul {
	margin: 0;
	padding: 0;
}

.div3 li {
	height: 30px;
	line-height: 30px;
	list-style: none;
	border-bottom: #ccc 1px dotted;
	text-align: center;
}

.a {
	text-decoration: none;
	color: #000000;
	font-size: 15px;
}

.a1 {
	text-decoration: none;
	color: #000000;
	font-size: 18px;
}
</style>
</head>
<body>

	
	<div class="top2">
	
		<div class="title" >
			<h3>选题系统-教师端</h3>
		</div>
		<div class="fr top-link">
			<span style="color:white">工号：${currentUser.id} 　姓名：${currentUser.mc}　　　选项：${currentUser.dp}　　　　　　</span> 
		</div>
	</div>

	<div class="left">
		<div class="div1">
			
         			
			<div class="div2" onclick="openurl('/byxt/teacher/chuti2.jsp');">
				<div class="spgl"></div>
				教师出题
			</div>	
			<div class="div2" onclick="openurl('/byxt/teacher/plchuti.jsp');">
				<div class="spgl"></div>
				批量出题
			</div>	
				<div class="div2" onclick="openurl('/byxt/teacher/xtjg2.jsp');">
				<div class="spgl"></div>
				选题结果
			</div>	
			
			
			<div class="div2" onclick="openurl('/byxt/teacher/modizl.jsp');">
				<div class="spgl"></div>
				 个人信息
			</div>	
			
			
			<div class="div2" onclick="openurl('/byxt/teacher/modipwd.jsp');">
					<div class="tcht"></div>
					修改密码
			</div>
			<a href="/byxt/logout">
			<div class="div2">
				<div class="tcht"></div>
					退出系统
				
			</div>
			</a>
		</div>
	</div>

	<div class="right">
		<iframe id="rightFrame" name="rightFrame" width="100%" height="100%"
			scrolling="auto" marginheight="0" marginwidth="0" align="center"
			style="border: 0px solid #CCC; margin: 0; padding: 0;" src="/byxt/teacher/modizl.jsp"></iframe>
	</div>







</body>
</html>
