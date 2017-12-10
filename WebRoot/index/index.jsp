<%@ page language="java" contentType="text/html; charset=utf-8"%>

<html>
	<head>
		<title>运维监控中心</title>
	</head>
	<FRAMESET id="frame1" border="0" frameSpacing="0" rows="96,*,20"
		frameBorder="NO" cols="*" >
		<frame name="topFrame" scrolling="no" noResize src="top.jsp">
		<FRAMESET id="frame2" border="0" frameSpacing="0" rows="*" 
			frameBorder="NO" cols="240,*">
			<frame name="left" scrolling="auto" marginwidth="0" marginheight="0"
				src="menulist.jsp">
				<FRAME name="main" frameBorder="0" noResize scrolling="auto"
				src="${pageContext.request.contextPath}/index/indexgetMain.action">
				<!-- <FRAME name="main" frameBorder="0" noResize scrolling="auto"
				src="mymain.jsp"> -->
		</FRAMESET>
		<frame name="bottom" scrolling="no" noResize src="bottom.jsp">
	</FRAMESET>
	<noframes>
		<body leftmargin="2" topmargin="0" >
			<p>
				你的浏览器版本过低！！！本系统要求IE6及以上版本才能使用本系统。
			</p>
		</body>
	</noframes>
</html>
