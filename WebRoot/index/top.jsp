<%@page import="com.core.jadlsoft.struts.action.UserUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.core.jadlsoft.model.xtgl.UserSessionBean" />
<%
UserSessionBean userSessionBean = (UserSessionBean) session.getAttribute(UserUtils.USER_SESSION);
String username = userSessionBean.getUserName();

String yhlxcn = "";
yhlxcn = "欢迎您："+username;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<%@include file="../include/meta.inc"%>
		<TITLE>民爆行业生产经营动态监控信息系统</TITLE>
		<link href="../css/index_new.css" rel="stylesheet" type="text/css" />
		<link href="../css/load_new.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="../common/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../common/queryUtils.js"></script>	
		<script type="text/javascript" src="../common/common.js"></script>
		<script type="text/javascript" src="../common/tableManager.js"></script>
		<script type="text/javascript" src="../common/ajaxUtil.js"></script>
		<script type="text/javascript" src="../common/validate.js"></script>

		<SCRIPT language=JavaScript>
		
		function naved(obj){
			for(var i=0; i<document.getElementById("nav").getElementsByTagName("li").length; i++){
				if( document.getElementById("nav").getElementsByTagName("li")[i].className=="nav_on1"){
					document.getElementById("nav").getElementsByTagName("li")[i].className="nav_on2";
				}
				obj.className="nav_on1";				
		    }
		}
		
		function exit() {
			exitLocalSystem();
		}
		//从本地系统退出
		function exitLocalSystem(){
			if(confirm("您确定要退出系统吗？")) {
				top.location.href="../dologinloginout.action";
			}
		}
	
		function changeSecondColomn(idx, obj){
			parent.left.document.location = "menulist.jsp?code=" + idx;
		}
		
		/*获取制定xml标签的值*/
		function getValue(xmldoc , tagname){
			var tagObj = xmldoc.getElementsByTagName(tagname)[0];
			if(tagObj != undefined && tagObj != null && tagObj.childNodes.length != 0 && tagObj.childNodes[0].nodeValue != undefined && tagObj.childNodes[0].nodeValue != null){
				return tagObj.childNodes[0].nodeValue;
			}
			return "";
		}
		
	</SCRIPT>
	<style type="text/css">
		body{background:#386698 url(../images/new/images/topbg.jpg) repeat-x;overflow-x:hidden;}
		.downloadDiv {
			width: 600px;
			margin-left:100px;
			text-align: center;
		}
		.downloadDiv tr td {
			color:#FFFF00;
			width: 120px;
		}
		.downloadDiv tr td a {
			text-decoration: none;
			color: #2A00E1;
		}
		.downloadDiv tr td a:HOVER {
			color: red;
		}
	</style>
	</head>
	<body >
	<div class="topimg"> 
      <h1 class=""></h1>
      <!-- <div class="downloadDiv">
      	<table class="downloadTable" cellspacing="10">
      		<tr>
      			<td></td>
      			<td>jdk1.6</td>
      			<td>tomcat6</td>
      			<td>SSO客户端</td>
      		</tr>
      		<tr>
      			<td>Windows</td>
      			<td><a href="#">jdk_x64</a>/<a href="#">jdk_x86</a></td>
      			<td rowspan="2"><a href="#">Tomcat6（通用）</a></td>
      			<td rowspan="2"><a href="#">SSOClient.zip</a></td>
      		</tr>
      		<tr>
      			<td>Linux</td>
      			<td><a href="#">jdk_x64</a>/<a href="#">jdk_x86</a></td>
      		</tr>
      		<tr></tr>
      	</table>
      </div> -->
      <div class="topxx">
      
      
      </div>
      <div class="topxx2">
          <dl class="fl dlxx">
              <dd title="<%=username %>"> <%=username.length() > 9 ? username.substring(0,9) + "..." : username%></dd>  
              
          </dl>
          <div class="fl helptop">
             
             <a href="#" class="tuic" onclick="exit();">退出</a>
          </div>
      </div><!--topxx2 end-->
   </div><!--top img end--> 
   <div class="j10"></div> 
	<iframe id="portalSystemIframe" src="" style="display:none"></iframe>  
</body>
</html>
