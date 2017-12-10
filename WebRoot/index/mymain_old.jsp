<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>服务器列表</title>
		<%@ include file="../include/head.inc"%>
		<style type="text/css">
			a {
				text-decoration: none;
				color: white;
			}
			.log_div {
	    		margin: 0 auto;
	    		margin-top:5px;
	    		padding:10px;
	    		position:relative;
	    		width: 860px;
	    		overflow-x: auto;
	    		border-radius: 5px;
	    		background-color: #F8F8F8;
	    	}
	    	.box-shadow{  
			    filter: progid:DXImageTransform.Microsoft.Shadow(color='#969696',Direction=120, Strength=10);/*for ie6,7,8*/  
			    -moz-box-shadow:5px 5px 15px #969696;/*firefox*/  
			    -webkit-box-shadow:5px 5px 15px #969696;/*webkit*/  
			    box-shadow:5px 5px 15px #969696;/*opera或ie9*/  
			}
			.closeLog {
				position:absolute;
				top:0px;
				right:1px;
				text-align:center;
				line-height:30px;
				background-color: #2FA2F4;
				color:#F8F8F8;
				width: 40px;
				height: 30px;
			}
			.closeLog a:HOVER {
				color:red;
			}
			.log_div table {
				margin: 0 auto;
				margin-top:20px;
				text-align: center;
				font-size: 18px;
			}
			.log_div table td a {
				color: #0000CC;
			}
			.log_div_td1 {
				margin: 0 auto;
				width: 50%;
			}
			.log_div_td2 {
				margin: 0 auto;
				width: 20%;
			}
			.log_div_td3 {
				margin: 0 auto;
				width: 15%;
				color: #4285F4;
			}
			.main_content {
				width: 100%;
				background: #001B3E;
				padding: 10px;
			}
			.main_left {
				width: 50%;
				float: left;
				margin: 0 auto;
				text-align: center;
			}
			.main_left_fwq {
				margin-top:20px;
				padding:10px;
				float:left;
				width: 350px;
			}
			/* .tb_fwq {
				width: 100%;
				border:solid #add9c0; 
				border-width:1px 0px 0px 1px;
				background-color: #113965;
			}
			.tb_fwq td {
				border:solid #add9c0; 
				border-width:0px 1px 1px 0px; 
				font-size:10px;
				color:#FFFF00;
				line-height: 20px;
			} */
			.tb_fwq {
				width: 100%;
				background-color: #113965;
			}
			.tb_fwq td {
				font-size:10px;
				color:#FFFF00;
				line-height: 20px;
			}
			.main_middle {
				width: 25%;
				float: left;
				text-align: center;
			}
			.main_right {
				width: 25%;
				float: left;
			}
		</style>
		<script type="text/javascript">
		    $(function(){
		    	var screenHeight = $(document).height();
				$("#logDiv").height(screenHeight * 0.8);
		    });
			//获取日志列表
			function fwqLogs(fwqip, dk, logServerType){
				var url = "";
				//根据服务器或者托管服务器设置不同的请求路径
				if (logServerType == "yun") {
					//云平台的
					url = "../fwqgl/fwqgetLogs.action";
				} else {
					url = "../fwqgl/tgFwqgetLogs.action";
				}
				//异步发出请求，获得日志文件列表
				$.ajax({
					type: "POST",
					async:true,
			   		url: url,
			   		dataType:"json",
			   		data:"fwqip="+fwqip,
					success: function(data){
						var html;
						// 先清空之前的内容
						var thisTable = document.getElementById("tableId");
						if (thisTable != null) {
							thisTable.parentNode.removeChild(thisTable);	
						}
						// 将值拼成HTML显示到页面中
						if (data != null && data.length>0) {
							//循环之前的HTML代码
							var html_table = "<table id='tableId' cellspacing='10'><tr><td class='log_div_td1'>文件名</td><td class='log_div_td2'>文件大小</td><td colspan='2'>操作</td></tr><tr><td colspan='4'><hr/></td></tr>";
							//循环生成的HTML代码
							var html_tr = "";
							for(var i=0;i<data.length;i++){
								var logName = data[i][0];	// 日志文件的名字
								html_tr = html_tr.concat('<tr><td class="log_div_td1">'+data[i][0]+'</td><td class="log_div_td2">'+data[i][1]+'</td><td class="log_div_td3"><a href="javascript:void(0)" onclick="showLog(&quot;'+logName+'&quot;,&quot;'+fwqip+'&quot;,&quot;'+dk+'&quot;)">查看</a></td><td class="log_div_td3"><a id="downLogId'+i+'" href="javascript:void(0)" onclick="downLog(&quot;'+logName+'&quot;,&quot;'+fwqip+'&quot;,&quot;'+i+'&quot;)">下载</a></td></tr>');
							}
							html = html_table+html_tr+"</table>";
						}else {
							// 说明没有日志
							html = "<table id='tableId'><tr><td>未找到相关日志，可能服务器未开启或者服务器的日志设置没有打开</td></tr></table>";
						}
						$("#logDiv").append(html);
					}
				});
			}
			
			//查看日志
			function showLog(logName,fwqip,dk){
				// 根据服务器ip和日志的名称
				// 打开一个新的窗口，在新窗口中直接显示日志的内容
				var url = "http://"+fwqip+":"+dk+"/fwzxutils/fileDeal.do?methodName=showTomcatLog&logName="+logName;
				window.open(url);
			}
			
			//下载日志
			function downLog(logName,fwqip,index){
				// 为了解决如果下载的日志文件不存在的问题，采用异步的方式
				$.ajax({
					url:"http://"+fwqip+":8080/fwzxutils/fileDeal.do?methodName=isTomcatLogExist&logName="+logName,
					type:"get",
					dataType:"jsonp",	//浏览器的跨域请求问题，使用jsonp解决
					success:function(data){
						if (data.isFileExist == "error") {
							alert("日志不存在,可能已被删除");
						}else if (data.isFileExist == "yes") {
							location.href = "http://"+fwqip+":8080/fwzxutils/fileDeal.do?methodName=downTomcatLog&logName="+logName;
						}
					}
				});
			}
			
			//访问应用
			function viewApp(ip,dk,appname){
				var url = "http://"+ip+":"+dk+"/"+appname;
				window.open(url);
			}
			
			//应用日志信息
			function appLog(ip,dk,appname,serverType){		//serverType，判断是云平台的服务器或者是托管的服务器
				var url = "";
				if (serverType=="yun") {
					url = "../yygl/yyisYyLogExist.action";
				}else {
					url = "../yygl/tgyyisYyLogExist.action";
				}
				//发送异步请求获取文件是否存在
				$.ajax({
					type: "POST",
			   		url: url,
			   		data:"appname="+appname+"&ip="+ip,
			   		dataType:"json",
					success: function(resultBean){
						if(resultBean.statusCode == "0000"){
							//说明存在，直接访问
							var u = "http://"+ip+":"+dk+"/fwzxutils/fileDeal.do?methodName=showAppLog&yyLogPath="+resultBean.arg1;
							window.open(u);
		  				}else{
		  					alert(resultBean.msg);
		  					return false;
		  				}
					}
				});
			}
			
			//停止应用
			function tzApp(ip,appname,serverType){
				var url = "";
				if (serverType=="yun") {
					url = "../yygl/yytzFwqApp.action";
				}else {
					url = "../yygl/tgyytzFwqApp.action";
				}
				$.ajax({
					type: "POST",
			   		url: url,
			   		data:"appname="+appname+"&ip="+ip,
			   		dataType:"json",
					success: function(resultBean){
						if(resultBean.statusCode == "0000"){
		  					alert("应用停止成功！");
		  					window.location.reload();
		  				}else{
		  					alert(resultBean.msg);
		  					return false;
		  				}
					}
				});
			}
			
			//定时刷新
			function refreshCache(){
				getServerCache();		//刷新云平台服务器信息
				getTgServerCache();		//刷新托管服务器信息
			}
			
			//定时执行的异步刷新云平台服务器的数据
			function getServerCache(){
				$.ajax({
			   		type: "POST",
			   		url: "../cache/cachegetServerCache.action",
			   		dataType:"json",
					success: function(data){
						//删除之前内容
						$(".appTr").remove();
						for(var key in data) {
							//设置基本信息	
							//设置连接情况
							var fwqStatus = document.getElementById("fwqStatus"+key);
							if (data[key].fwqStatus == 0) {
								fwqStatus.innerHTML = '<font color="green">正常</font>'; 
							}else if (data[key].fwqStatus == 1) {
								fwqStatus.innerHTML = '<font color="red">异常</font>'; 
							}
							//设置CPU
							document.getElementById("cpuUsed"+key).innerHTML = data[key].cpuUsed;
							//设置内存使用率
							document.getElementById("memeryUsed"+key).innerHTML = data[key].memeryUsed;
							/* //设置线程数
							document.getElementById("threadCount"+key).innerHTML = data[key].threadCount;
							//设置JVM堆内存
							document.getElementById("jvmMemory"+key).innerHTML = data[key].jvmMemory;
							//设置JVM启动线程数
							document.getElementById("jvmThreadCount"+key).innerHTML = data[key].jvmThreadCount;
							//设置JVM加载类的个数
							document.getElementById("jvmLoadedClassCount"+key).innerHTML = data[key].jvmLoadedClassCount; */
							//设置应用信息
							var showtable = document.getElementById('showtable'+key);	//得到table
							for(var appkey in data[key].appInfo){
							    var apptr = document.createElement('tr');     //创建tr
							    $(apptr).addClass("appTr");
							    var apptd;
							    var appnameListStr = '<%= request.getAttribute("appnameList")%>';
							    var appnameList = appnameListStr.split(",");
							    var flag = false;
							    for(var i=0;i<appnameList.length;i++){
							    	if(appnameList[i].indexOf(appkey) != -1){
							    		flag = true;
							    		break;
							    	}
							    }
							    if (flag) {
									apptd = '<td>'+appkey+'</td><td>['
										  + '<a href="javascript:void(0)" onclick="viewApp(&quot;'+key+'&quot;,&quot;'+data[key].dk+'&quot;,&quot;'+appkey+'&quot;)">访问</a>'
										  + '、<a href="javascript:void(0)" onclick="appLog(&quot;'+key+'&quot;,&quot;'+data[key].dk+'&quot;,&quot;'+appkey+'&quot;)">日志</a>'
										  + ']</td>';
								} else {
									apptd = '<td>'+appkey+'&nbsp;<font color="red" size="1">非体系内</font></td><td>无操作</td>';
								}
							    $(apptr).append($(apptd));	//设置tr里面的内容
							    //apptr.innerHTML=apptd; //设置tr里面的内容
							    showtable.appendChild(apptr);
							}
						}
					}
				 });
			}
			
			//定时执行的异步刷新托管缓存数据
			function getTgServerCache(){
				$.ajax({
			   		type: "POST",
			   		url: "../cache/cachegetTgServerCache.action",
			   		dataType:"json",
					success: function(data){
						//删除之前内容
						$(".tgAppTr").remove();
						for(var key in data) {
							//设置基本信息	
							//设置连接情况
							var fwqStatus = document.getElementById("fwqStatus"+key);
							if (data[key].fwqStatus == 0) {
								fwqStatus.innerHTML = '<font color="green">正常</font>'; 
							}else if (data[key].fwqStatus == 1) {
								fwqStatus.innerHTML = '<font color="red">异常</font>'; 
							}
							//设置CPU
							document.getElementById("cpuUsed"+key).innerHTML = data[key].cpuUsed;
							//设置内存使用率
							document.getElementById("memeryUsed"+key).innerHTML = data[key].memeryUsed;
							/* //设置线程数
							document.getElementById("threadCount"+key).innerHTML = data[key].threadCount;
							//设置JVM堆内存
							document.getElementById("jvmMemory"+key).innerHTML = data[key].jvmMemory;
							//设置JVM启动线程数
							document.getElementById("jvmThreadCount"+key).innerHTML = data[key].jvmThreadCount;
							//设置JVM加载类的个数
							document.getElementById("jvmLoadedClassCount"+key).innerHTML = data[key].jvmLoadedClassCount; */
							//设置应用信息
							var showtable = document.getElementById('showtable'+key);	//得到table
							for(var appkey in data[key].appInfo){
							    var apptr = document.createElement('tr');     //创建tr
							    $(apptr).addClass("tgAppTr");
							    var apptd;
							    var appnameListStr = '<%= request.getAttribute("tgAppnameList")%>';
							    var appnameList = appnameListStr.split(",");
							    var flag = false;
							    for(var i=0;i<appnameList.length;i++){
							    	if(appnameList[i].indexOf(appkey) != -1){
							    		flag = true;
							    		break;
							    	}
							    }
							    if (flag) {
									apptd = '<td>'+appkey+'</td><td>['
										  + '<a href="javascript:void(0)" onclick="viewApp(&quot;'+key+'&quot;,&quot;'+appkey+'&quot;)">访问</a>'
										  + '、<a href="javascript:void(0)" onclick="appLog(&quot;'+key+'&quot;,&quot;'+appkey+'&quot;)">日志</a>'
										  + '、<a href="javascript:void(0)" onclick="tzApp(&quot;'+key+'&quot;,&quot;'+appkey+'&quot;)">停止</a>'
										  + ']</td>';
								} else {
									apptd = '<td>'+appkey+'&nbsp;<font color="red" size="1">非体系内</font></td><td>无操作</td>';
								}
							    $(apptr).append($(apptd));	//设置tr里面的内容
							    //apptr.innerHTML=apptd; //设置tr里面的内容
							    showtable.appendChild(apptr);
							}
						}
					}
				 });
			}
			
		    //界面初始化
		    function init(){
		    	//绑定查看日志的遮罩层
		    	if($(".logShowClass").length>0){
			    	$(".logShowClass").each(function(){
						$(this).alertBox({ id:"#logDiv",borderWidth: "0px", level: "1000"});
					});
		    	}
				//设置5s刷新数据
				setInterval(refreshCache,5000);
		    }
		    
		</script>
	</head>
	<body onload="init();">
		<div class="bmain-round" style="border: 1px solid #111;">
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx" style="width: 100%">
						<b>当前位置：</b> &gt
						<span>总线监控</span>
						<span style="float: right; padding-right: 50px"> </span>
					</div>
				</div>
				<!--boxtop pos end-->
				<div class="main_content">
					<!-- 云平台服务器监控 -->
					<div class="main_left">
						<center><font size="4" color="#F2F2F2">云平台服务器监控</font></center>
						<c:if test="${empty serverCache}">
							<div style="margin: 0 auto; text-align: center; font-size: 16px; color: red">暂无可用服务器</div>
						</c:if>
						<c:if test="${not empty serverCache}">
							<c:forEach items="${serverCache}" var="server">
								<div class="main_left_fwq">
									<table id="showtable${server.key}" class="tb_fwq" border="0">
										<tbody>
											<tr><td colspan="2">
												【${server.value.szjf}】  ${server.value.fwqname}&nbsp;
													<span id="fwqStatus${server.key}">
														<c:if test="${server.value.fwqStatus == 0}"><font color="green">正常</font></c:if>
														<c:if test="${server.value.fwqStatus == 1}"><font color="red">异常</font></c:if>
													</span>
												<%-- ${server.key}<c:if test="${not empty server.value.fwqname}">/${server.value.fwqname}</c:if> --%>
											</td></tr>
											<%-- <tr>
												<td>连接情况</td>
												<td id="fwqStatus${server.key}">
													<c:if test="${server.value.fwqStatus == 0}"><font color="green">正常</font></c:if>
													<c:if test="${server.value.fwqStatus == 1}"><font color="red">异常</font></c:if>
												</td>
											</tr> --%>
											<tr>
												<td>内存/cpu</td>
												<td style="color: #B31ACF">
													<span id="memeryUsed${server.key}">${server.value.memeryUsed}</span> <font color="white">/</font> 
													<span id="cpuUsed${server.key}">${server.value.cpuUsed}</span>
												</td>
												<%-- <td id="cpuUsed${server.key}" style="color: #B31ACF">${server.value.cpuUsed}</td> --%>
											</tr>
											<%-- <tr>
												<td>内存占用率</td>
												<td id="memeryUsed${server.key}" style="color: #B31ACF">${server.value.memeryUsed}</td>
											</tr>
											<tr>
												<td>启动线程数</td>
												<td id="threadCount${server.key}" style="color: #B31ACF">${server.value.threadCount}</td>
											</tr>
											<tr>
												<td>JVM可用堆内存大小</td>
												<td id="jvmMemory${server.key}" style="color: #B31ACF">${server.value.jvmMemory}</td>
											</tr>
											<tr>
												<td>JVM运行线程数</td>
												<td id="jvmThreadCount${server.key}" style="color: #B31ACF">${server.value.jvmThreadCount}</td>
											</tr>
											<tr>
												<td>JVM加载的类的个数</td>
												<td id="jvmLoadedClassCount${server.key}" style="color: #B31ACF">${server.value.jvmLoadedClassCount}</td>
											</tr> --%>
											<tr>
												<td>日志</td>
												<td>[<a href="javascript:void(0)" id="logId" class="logShowClass" onclick="fwqLogs('${server.key}','${server.value.dk}','yun')">运行日志</a>&nbsp;&nbsp;]</td>
											</tr>
											<tr><td colspan="2">应用</td></tr>
											<c:forEach items="${server.value.appInfo}" var="app">
												<tr class="appTr">
													<c:if test="${not fn:contains(appnameList,app.value.appname)}">
														<td>
															${app.key}
															<font size="1" color="red">非体系内</font>
														</td>
														<td>无操作</td>
													</c:if>
													<c:if test="${fn:contains(appnameList,app.value.appname)}">
														<td>
															${app.key}
														</td>
														<td>[<a href="javascript:void(0)" onclick="viewApp('${server.key}','${server.value.dk}','${app.key}','yun')">访问</a>、
															<a href="javascript:void(0)" onclick="appLog('${server.key}','${server.value.dk}','${app.key}','yun')">日志</a>]</td>
															<%-- <a href="javascript:void(0)" onclick="tzApp('${server.key}','${app.key}','yun')">停止</a>]</td> --%>
													</c:if>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<!-- 托管服务器监控 -->
					<div class="main_middle">
						<center><font size="4" color="#F2F2F2">托管服务器监控</font></center>
						<c:if test="${empty tgServerCache}">
							<div style="margin: 0 auto; text-align: center; font-size: 16px; color: red">暂无可用服务器</div>
						</c:if>
						<c:if test="${not empty tgServerCache}">
							<c:forEach items="${tgServerCache}" var="server">
								<div class="main_left_fwq">
									<table id="showtable${server.key}" class="tb_fwq" border="0">
										<tbody>
											<tr><td colspan="2">
												【${server.value.szjf}】  ${server.value.fwqname}&nbsp;
													<span id="fwqStatus${server.key}">
														<c:if test="${server.value.fwqStatus == 0}"><font color="green">正常</font></c:if>
														<c:if test="${server.value.fwqStatus == 1}"><font color="red">异常</font></c:if>
													</span>
											</td></tr>
											<%-- <tr>
												<td>连接情况</td>
												<td id="fwqStatus${server.key}">
													<c:if test="${server.value.fwqStatus == 0}"><font color="green">正常</font></c:if>
													<c:if test="${server.value.fwqStatus == 1}"><font color="red">异常</font></c:if>
												</td>
											</tr> --%>
											<tr>
												<td>内存/cpu</td>
												<td style="color: #B31ACF">
													<span id="memeryUsed${server.key}">${server.value.memeryUsed}</span> <font color="white">/</font> 
													<span id="cpuUsed${server.key}">${server.value.cpuUsed}</span>
												</td>
											</tr>
											<%-- <tr>
												<td>启动线程数</td>
												<td id="threadCount${server.key}" style="color: #B31ACF">${server.value.threadCount}</td>
											</tr>
											<tr>
												<td>JVM可用堆内存大小</td>
												<td id="jvmMemory${server.key}" style="color: #B31ACF">${server.value.jvmMemory}</td>
											</tr>
											<tr>
												<td>JVM运行线程数</td>
												<td id="jvmThreadCount${server.key}" style="color: #B31ACF">${server.value.jvmThreadCount}</td>
											</tr>
											<tr>
												<td>JVM加载的类的个数</td>
												<td id="jvmLoadedClassCount${server.key}" style="color: #B31ACF">${server.value.jvmLoadedClassCount}</td>
											</tr> --%>
											<tr>
												<td>日志</td>
												<td>[<a href="javascript:void(0)" id="logId" class="logShowClass" onclick="fwqLogs('${server.key}','tg')">运行日志</a>&nbsp;&nbsp;]</td>
											</tr>
											<tr><td colspan="2">应用</td></tr>
											<c:forEach items="${server.value.appInfo}" var="app">
												<tr class="tgAppTr">
													<c:if test="${not fn:contains(appnameList,app.value.appname)}">
														<td>
															${app.key}
															<font size="1" color="red">非体系内</font>
														</td>
														<td>无操作</td>
													</c:if>
													<c:if test="${fn:contains(tgAppnameList,app.value.appname)}">
														<td>
															${app.key}
														</td>
														<td>[<a href="javascript:void(0)" onclick="viewApp('${server.key}','${app.key}','tg')">访问</a>、
															<a href="javascript:void(0)" onclick="appLog('${server.key}','${app.key}','tg')">日志</a>]</td>
													</c:if>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:forEach>
						</c:if>
					</div>
					<!-- Nginx监控  -->
					<div class="main_right">
						Nginx监控 
					</div>
					<!-- 清除浮动使用 -->
					<div style="clear: both;"></div>
				</div>
			</div>
		</div>
		<!-- 点击运行日志弹出层 -->
	     <div id="logDiv" class="log_div box-shadow" style="display: none">
	     	<div class="close closeLog"><a href="javascript:void(0)">X</a></div>
	     </div>
	</body>
</html>
