<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>民爆行业安全培训信息管理系统_V1.02.00</title>
<script type="text/javascript" src="common/jquery.js"></script>
<script type="text/javascript" src="common/menucookie.js"></script>
<style >
.xtba {
	margin:0 auto;
	width:100%;
	text-align:center;
	position:fixed;
	bottom:60px;
	color: #FFFFFF;
}
.xtba a {
	color: #FFFFFF;
	text-decoration: none;
}
</style>
<script type="text/javascript">
	function init(){
		//document.location = "dologin.action";
		//定位光标
		focus();
		//获取cookie用户名
		getRememberInfo();
		//var height = window.screen.availHeight;
	}
	
	function focus(){
		document.getElementById("logid").focus();
	}
	
	function checkPwd(){
		delCookie();	//清除cookie中的菜单id
		doSubmit();		
		return;
		checkWin();
		$("#imageField").attr("disabled", true);
		var logid = document.getElementById("logid").value;
		var password = document.getElementById("password").value;
		if((  logid == null||logid == "") && (  password == null||password == "")){
			alert("请输入用户名、密码!");
			document.getElementById("logid").focus();
			$("#imageField").attr("disabled", false);
			return;
		}
		if( logid == null||logid == "" ){
		  	alert("用户名不能为空！");
			
			document.getElementById("logid").focus();
			$("#imageField").attr("disabled", false);
		   	return;
		}
		if( password == null ||password == "" ){
			alert("密码不能为空！");
			
			document.getElementById("password").focus();
			$("#imageField").attr("disabled", false);
			return;
		}
		
		if(!checkUserAndPwd(logid,password)){
			alert("用户名或密码错误");
	     	$("#imageField").attr("disabled", false);
	     	return;
		}
		
		doSubmit();		 	
	}
	function checkUserAndPwd(logid,password){
		var boo = false;
		$.ajax({
	   		type: "POST",
	   		url: "dologin!checkUserAndPwd.action",
	   		dataType:"text",
	   		async:false,
	   		data:"password="+password+"&userid="+encodeURI(encodeURI(logid)),
			success: function(msg){
				if(msg == 'success'){
	   		 		boo = true;
	     		}else{
	     			boo = false;	 	
	     		} 
			}
		 });
		 return boo;
	}
	
	function doSubmit(){
		if(document.getElementById("rmbUsr").checked){  
			setCookie("usrName_",document.getElementById("logid").value,24,"/");
        }
        if(document.getElementById("rmbPwd").checked){  
			setCookie("password_",document.getElementById("password").value,24,"/");
        }
        LoginForm.submit();
	}
	
	//新建cookie。  
	//hours为空字符串时,cookie的生存期至浏览器会话结束。hours为数字0时,建立的是一个失效的cookie,这个cookie会覆盖已经建立过的同名、同path的cookie（如果这个cookie存在）。  
    function setCookie(name,value,hours,path){  
	    var name = escape(name);  
	    var value = escape(value);  
	    var expires = new Date();  
	    expires.setTime(expires.getTime() + hours*3600000);  
	    path = path == "" ? "" : ";path=" + path;  
	    _expires = (typeof hours) == "string" ? "" : ";expires=" + expires.toUTCString();  
	    document.cookie = name + "=" + value + _expires + path;  
	}  
	
	//获取cookie信息  
  	function getRememberInfo(){  
	    var userName="";   
	    var password = "";
	    userName=getCookieValue("usrName_");  
	   	password = getCookieValue("password_");
	   	if(userName != null && userName !=""){
	   		document.getElementById("logid").value=userName; 
	   		document.getElementById("rmbUsr").checked=true; 
	   		
	   	}
	   	if(password != null && password !=""){
	   		document.getElementById("password").value=password; 
	   		document.getElementById("rmbPwd").checked=true; 
	   		
	   	}
	   
	  
	}
	
	function getCookieValue(name){  
	    var name = escape(name);  
	    //读cookie属性，这将返回文档的所有cookie  
	    var allcookies = document.cookie;  
	    //查找名为name的cookie的开始位置  
	    name += "=";  
	    var pos = allcookies.indexOf(name);  
	    //如果找到了具有该名字的cookie，那么提取并使用它的值  
	    if (pos != -1){                                     //如果pos值为-1则说明搜索"version="失败  
	    	var start = pos + name.length;                  //cookie值开始的位置  
	        var end = allcookies.indexOf(";",start);        //从cookie值开始的位置起搜索第一个";"的位置,即cookie值结尾的位置  
	        if (end == -1) end = allcookies.length;       	//如果end值为-1说明cookie列表里只有一个cookie  
	       	var value = allcookies.substring(start,end);  	//提取cookie的值  
	       	return unescape(value);                         //对它解码  
	   	}  
	    else{
	    	return "";   //搜索失败，返回空字符串   
	    }                                   
	} 
	//删除cookie  
	function deleteCookie(name,path){  
	    var name = escape(name);  
	    var expires = new Date(0);  
	    path = path == "" ? "" : ";path=" + path;  
	    document.cookie = name + "="+ ";expires=" + expires.toUTCString() + path;  
	}
	//网页内按下回车触发
	document.onkeydown = hotkey;
	function hotkey(){
		var esc = window.event.keyCode;
		if(esc==13){
			document.getElementById("imageField").click();                
            return false;
		}
	}
	//检查浏览器版本
 	function checkWin(){
   	    // 增加对客户端IE版本的判断，如果非IE6或以上版本，不让登录系统*/
    	var win_ie = navigator.appVersion;
    	if(win_ie.indexOf("MSIE") == -1) {
    		//alert("请使用IE浏览器运行本系统！");
    		return;
    	}else if(parseInt(navigator.appVersion.split("MSIE")[1]) < 6){
    		alert("您的IE浏览器版本太低，请使用IE6或以上版本运行本系统！");
    		return;
    	}
   	}
  
	if (top.location != self.location){       
	top.location=self.location;       
	}  
</script> 
<style type="text/css">
	.loginBody {
		margin: 0 auto;
		background-color: #4D93D8;
	}
	.loginBox {
		margin: 0 auto;
		width:400px;
		background-color: #F0F0F0;
		text-align: center;
		border-radius:5px;
	}
	.login-table {
		margin: 0 auto;
		margin-top: 270px;
		padding: 20px;
	}
	.login-table-tr {
		height: 40px;
		line-height: 40px;
	}
</style>
</head>

<body class="loginBody" onload="init();">
<div class="loginBox">
   <form action="dologin.action" id="LoginForm" method="post" >
  		<table class="login-table">
  			<tr class="login-table-tr">
  				<td>用户名：</td>
  				<td><input type="text" class="w200" id="logid" name="userBean.userid" /></td>
  			</tr>
  			<tr class="login-table-tr">
  				<td>密&#12288码：</td>
  				<td><input type="password" class="w200" id="password" name="userBean.password" /></td>
  			</tr>
  			<tr class="login-table-tr">
  				<td colspan="2">
  					<input name="rmbUsr" id="rmbUsr" type="checkbox" /><label for="rmbUsr">记住用户名</label>
  					<input name="rmbPwd" id="rmbPwd" type="checkbox" /><label for="rmbPwd">记住密码</label>
  				</td>
  			</tr>
  			<c:if test="${not empty errMsg}">
  				<tr class="login-table-tr">
	 				<td colspan="2"><span><font color="red">${errMsg}</font></span></td>
	 			</tr>
  			</c:if>
  			<tr>
  				<td colspan="2">
		            <input type="button" id="imageField" onclick="checkPwd();" value="登录" />
		            <input type="reset"  value="重置" />
  				</td>
  			</tr>
  		</table>	
        <%-- <dl>
            <dt>用户名：</dt>
            <dd><input type="text" class="w200" id="logid" name="userBean.userid" /></dd>
        </dl>
        <dl>
            <dt>密&#12288码：</dt>
            <dd><input type="password" class="w200" id="password" name="userBean.password" /></dd>
        </dl>
        <dl class="line" style="padding:0px; padding-bottom:15px;">
            <dd><label><input name="rmbUsr" id="rmbUsr" type="checkbox"   class="position" /> 记住用户名</label></dd>
        </dl>
        <span><font color="red">${errMsg}</font></span>
        <br /><br />
        <div class="btn">
            <input type="button" id="imageField" onclick="checkPwd();" value="登录" />
            <input type="reset"  value="重置" />
        </div> --%>
       
   	</form>
        <!-- <a href="newimages/IESETING.doc" target="_blank">系统配置说明</a>  -->
        <!-- <div class="footer">为了您能获得最佳的显示效果，请使用1024*768以上的分辨率</div> -->
        
        <!-- <div class="xtba" style="text-align: center;color: #FFFFFF" >版权所有&copy;中华人民共和国工业和信息化部&nbsp;&nbsp;<a href="http://www.miibeian.gov.cn" style="" target="_blank"><font color="#FFFFFF">京ICP备13033489号-7</font></a></div> -->
</div>
	<center>
	<div class="xtba">
		版权所有&copy;中华人民共和国工业和信息化部&nbsp;&nbsp;
		<a href="http://www.miibeian.gov.cn" target="_blank">
			京ICP备13033489号-7
		</a>
	</div>
	</center>
</body>
</html>
