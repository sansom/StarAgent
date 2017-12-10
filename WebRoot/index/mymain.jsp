<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>服务器列表</title>
		<%@ include file="../include/head.inc"%>
		
		<!-- <link type="text/css" href="css/bootstrap.min.css" rel="stylesheet"> -->
		<link type="text/css" href="css/main.css" rel="stylesheet">
		
		<script type="text/javascript">
			//定义全局变量
			var totalHeight = 600;		//初始化云平台的总高度
			var tgtotalHeight = 600;	//初始化托管总高度
			
			var selectedHeight = 400;	//选中单个服务器时高度
			var selectedYyHeight = 300;	//选中单个应用时高度
			
			var ptlx_yun = <%= SystemConstants.PTLX_YUN%>;	//云平台类型
			var ptlx_tg = <%= SystemConstants.PTLX_TG%>;	//托管类型
			
			//************云平台服务器所需参数************************
			var fwq_num = ${fn:length(fwqAll)};	//初始化服务器数量
			var yy_num = ${fn:length(yyAll)};	//初始化应用数量
			
			var currentFwqAll = new Array();	//当前所有的服务器信息（用于更新时候对比），初始化时候push进去，方便存储   只存储id
			var currentYyAll = new Array();		//当前所有的应用信息（用于更新时候对比），初始化时候push进去，方便存储   只存储id
			var currentconnectInfo = <%= request.getAttribute("connectInfo")%>;	//当前所有的连线信息（服务器悬浮时候应用高亮要用到）
			
			//************托管服务器所需参数************************
			var tg_fwq_num = ${fn:length(tgfwqAll)};	//初始化服务器数量
			var tg_yy_num = ${fn:length(tgyyAll)};	//初始化应用数量
			
			var currentTgFwqAll = new Array();	//当前所有的服务器信息（用于更新时候对比），初始化时候push进去，方便存储之存储id
			var currentTgYyAll = new Array();		//当前所有的应用信息（用于更新时候对比），初始化时候push进去，方便存储之存储id
			var currentTgconnectInfo = <%= request.getAttribute("tgconnectInfo")%>;	//当前所有的连线信息（服务器悬浮时候应用高亮要用到）
			
			
			//智能调节总高度
			function adjustTotalHeight(currentTotalHeight, num, ptlx) {
				var everyHeight = (currentTotalHeight-(num+1)*20)/num;	//当前的每一个的高度
				//如果当前高度大于300并且3个以上的服务器的时候才去减少
				if (everyHeight > 300 && num>3) {
					var decCount = currentTotalHeight - (200 * Math.ceil(num/2)) + 20*(Math.ceil(num/2)+1);
					if (ptlx == ptlx_yun) {
						totalHeight -= decCount;
					}else {
						tgtotalHeight -= decCount;
					}
				}
				//如果当前高度小于170,就去增加
				if (everyHeight < 170) {
					var addCount = (200 * Math.ceil(num/2)) + 20*(Math.ceil(num/2)+1) - currentTotalHeight;
					if (ptlx == ptlx_yun) {
						totalHeight += addCount;
					}else {
						tgtotalHeight += addCount;
					}
				}
			}
			
			//第一次启动时候重载下总高度
			adjustTotalHeight(totalHeight, fwq_num, ptlx_yun);
			adjustTotalHeight(totalHeight, tg_fwq_num, ptlx_tg);
			
			//窗口大小改变事件
			$(window).resize(function () {          //当浏览器大小变化时
				setFloatByWidth();
			   /*  alert($(window).width());          //浏览器时下窗口可视区域高度
			    alert($(document).width());        //浏览器时下窗口文档的高度
			    alert($(document.body).width());   //浏览器时下窗口文档body的高度
			    alert($(document.body).outerWidth(true)); //浏览器时下窗口文档body的总高度 包括border padding margin */
			});
			
			function setFloatByWidth() {
				var wid = $(window).width();
				if (wid <= 815) {
					$(".main_middle").each(function(){
						$(this).css("float","none");
					});
				}else {
					$(".main_middle").each(function(){
						$(this).css("float","left");
					});
				}
			}
			
			//调节服务器高度
			function autoFwqHeight(ptlx) {
			
				var temp_fwq_num = fwq_num;
				var temp_currentFwqAll = currentFwqAll;
				var temp_totalHeight = totalHeight;
				if (ptlx == ptlx_tg) {
					temp_fwq_num = tg_fwq_num;
					temp_currentFwqAll = currentTgFwqAll;
					temp_totalHeight = tgtotalHeight;
				}
			
				//使用数组id的方式替换遍历dom的操作
				$(temp_currentFwqAll).each(function(){
					if (temp_fwq_num<2) {
						$("#fwq"+this).height(300);	//fwqitem
						$("#fwqYyInfo"+this).height(300);	//fwqYyInfo
						$("#fwqMore"+this).height(300);	//fwqMore
					}else {
						var fwqHeight = (totalHeight - (Math.ceil(temp_fwq_num/2)+1)*20) / Math.ceil(temp_fwq_num/2); 
						$("#fwq"+this).height(fwqHeight);	//fwqitem
						$("#fwqYyInfo"+this).height(fwqHeight);	//fwqYyInfo
						$("#fwqMore"+this).height(fwqHeight);	//fwqMore
					}
				});
			}
			//调节应用高度
			function autoYyHeight(ptlx) {
			
				var temp_yy_num = yy_num;
				var temp_currentYyAll = currentYyAll;
				var temp_totalHeight = totalHeight;
				if (ptlx == ptlx_tg) {
					temp_yy_num = tg_yy_num;
					temp_currentYyAll = currentTgYyAll;
					temp_totalHeight = tgtotalHeight;
				}
			
				//使用数组id的方式替换遍历dom的操作
				$(temp_currentYyAll).each(function(){
					if (temp_yy_num<2) {
						$("#yy"+this).height(300);
						$("#yyMore"+this).height(300);
					}else {
						var yyHeight = (totalHeight - (temp_yy_num+1)*20) / temp_yy_num;
						$("#yy"+this).height(yyHeight);
						$("#yyMore"+this).height(yyHeight);
					}
				});
			}
			
			//初始化服务器布局
			function resetFwqLayout(ptlx){
				
				var temp_currentFwqAll = currentFwqAll;
				if (ptlx == ptlx_tg) {
					temp_currentFwqAll = currentTgFwqAll;
				}
				
				$(temp_currentFwqAll).each(function(){
					$("#fwq"+this).show();
					$("#fwqYyInfo"+this).hide();
					$("#fwqMore"+this).hide();
				});
			}
			//初始化应用布局
			function resetYyLayout(ptlx){
				
				var temp_currentYyAll = currentYyAll;
				if (ptlx == ptlx_tg) {
					temp_currentYyAll = currentTgYyAll;
				}
				
				$(temp_currentYyAll).each(function(){
					$("#yy"+this).show();
					$("#yyMore"+this).hide();
				});
			}
			
			$(function(){
				autoFwqHeight(ptlx_yun);
				autoFwqHeight(ptlx_tg);
				/* autoYyHeight(ptlx_yun);
				autoYyHeight(ptlx_tg); */
				
				//绑定查看日志的遮罩层
		    	if($(".logShowClass").length>0){
			    	$(".logShowClass").each(function(){
						$(this).alertBox({ id:"#logDiv",borderWidth: "0px", level: "1000"});
					});
		    	}
		    	var screenHeight = $(document).height();
				$("#logDiv").height(screenHeight * 0.6);
			});
			
			//鼠标悬停在服务器上面的事件
			function showFwqLinerInfo(divObj) {
				/* var fwqContentTpye = $(divObj).parent().attr("id");		//fwqContent或者tgfwqContent
				var ptlx = ptlx_yun;
				var temp_currentconnectInfo = currentconnectInfo;
				if (fwqContentTpye == "tgfwqContent") {
					ptlx = ptlx_tg;
					temp_currentconnectInfo = currentTgconnectInfo;
				}
				
				var fwqid = divObj.id.substring(8);	//fwqOuter139
				//将该服务器对应的应用高亮显示
				for (var key in temp_currentconnectInfo) {
					if (key.indexOf(fwqid) != -1) {
						//包含有该服务器信息，将对应的该应用的div高亮
						var keyInfo = key.split("-");	//fromid,toid
						var yyid = keyInfo[0];	//fromid
						$("#yy"+yyid).addClass("heighLight");
					}
				};
				//判断服务器是否异常
				if ($("#fwq"+fwqid).attr("title") == "异常") {
					return;
				} */
			}
			//鼠标从服务器上面释放事件
			function hideFwqLinerInfo(divObj) {
			
				/* var fwqContentTpye = $(divObj).parent().attr("id");		//fwqContent或者tgfwqContent
				var ptlx = ptlx_yun;
				var temp_currentconnectInfo = currentconnectInfo;
				if (fwqContentTpye == "tgfwqContent") {
					ptlx = ptlx_tg;
					temp_currentconnectInfo = currentTgconnectInfo;
				}
			
				var fwqid = divObj.id.substring(8);	//fwq139
				//将该服务器对应的应用以及相应的连线高亮显示
				for (var key in temp_currentconnectInfo) {
					if (key.indexOf(fwqid) != -1) {
						//包含有该服务器信息，将对应的该应用的div高亮
						var keyInfo = key.split("-");	//fromid,toid
						var yyid = keyInfo[0];	//fromid
						$("#yy"+yyid).removeClass("heighLight");
					}
				}; */
			}
			//鼠标悬停在服务器应用上面的事件
			function showFwqYyLinerInfo(yyid,fwqid) {
				//$("#yy"+yyid).addClass("heighLight_yy");
			}
			//鼠标从服务器应用上面释放事件
			function hideFwqYyLinerInfo(yyid,fwqid) {
				//$("#yy"+yyid).removeClass("heighLight_yy");
			}
			//调节服务器应用高度
			function adjustFwqYyHeight(fwqid, ptlx) {
			
				var temp_fwq_num = fwq_num;
				var temp_currentFwqAll = currentFwqAll;
				var temp_totalHeight = totalHeight;
				if (ptlx == ptlx_tg) {
					temp_fwq_num = tg_fwq_num;
					temp_currentFwqAll = currentTgFwqAll;
					temp_totalHeight = tgtotalHeight;
				}
			
				var otherHeight = (totalHeight -selectedHeight-(Math.ceil(temp_fwq_num/2)+1)*20) / (Math.ceil(temp_fwq_num/2)-1);
				$(temp_currentFwqAll).each(function(){
					if ($("#fwq"+this).height() < selectedHeight) {
						if (this.indexOf(fwqid) != -1) {
							$("#fwq"+this).height(selectedHeight);
							$("#fwqYyInfo"+this).height(selectedHeight);
						}else {
							/* $("#fwq"+this).height(otherHeight);
							$("#fwqYyInfo"+this).height(otherHeight); */
						}
					}
				});
			}
			//调节服务器详细信息的高度
			function adjustFwqMoreHeight(fwqid, ptlx) {
				
				var temp_fwq_num = fwq_num;
				var temp_currentFwqAll = currentFwqAll;
				var temp_totalHeight = totalHeight;
				if (ptlx == ptlx_tg) {
					temp_fwq_num = tg_fwq_num;
					temp_currentFwqAll = currentTgFwqAll;
					temp_totalHeight = tgtotalHeight;
				}
				
				var otherHeight = (totalHeight -selectedHeight-(Math.ceil(temp_fwq_num/2)+1)*20) / (Math.ceil(temp_fwq_num/2)-1);
				$(temp_currentFwqAll).each(function(){
					if (this.indexOf(fwqid) != -1) {
						$("#fwq"+this).height(selectedHeight);
						$("#fwqMore"+this).height(selectedHeight);
					}else {
						/* $("#fwq"+this).height(otherHeight);
						$("#fwqMore"+this).height(otherHeight); */
					}
				});
			}
			//点击查看应用信息
			function showYyInfo(fwqid, ptlx) {
				if ($("#fwq"+fwqid).attr("title") == "异常") {
					return;
				}
				adjustFwqYyHeight(fwqid, ptlx);
				//显示应用信息
				$("#fwq"+fwqid).hide();
				$("#fwqYyInfo"+fwqid).show();
			}
			//点击查看更多信息
			function showMoreInfo(fwqid, ptlx) {
				if ($("#fwq"+fwqid).attr("title") == "异常") {
					return;
				}
				adjustFwqMoreHeight(fwqid, ptlx);
				//显示应用信息
				$("#fwq"+fwqid).hide();
				$("#fwqMore"+fwqid).show();
			}
			//从应用界面返回服务器信息界面
			function toFwqLayOut(fwqid, ptlx) {
			
				var temp_currentFwqAll = currentFwqAll;
				if (ptlx == ptlx_tg) {
					temp_currentFwqAll = currentTgFwqAll;
				}
			
				$("#fwqYyInfo"+fwqid).hide();
				$("#fwq"+fwqid).show();
				autoFwqHeight(ptlx);
				$(temp_currentFwqAll).each(function(){
					if($("#fwqYyInfo"+this).css("display") == "block") {
						adjustFwqYyHeight(this, ptlx);
					}
				});
			}
			//从服务器详细信息界面返回服务器信息界面
			function toFwqLayOutFromMore(fwqid, ptlx) {
			
				var temp_currentFwqAll = currentFwqAll;
				if (ptlx == ptlx_tg) {
					temp_currentFwqAll = currentTgFwqAll;
				}
			
				$("#fwqMore"+fwqid).hide();
				$("#fwq"+fwqid).show();
				autoFwqHeight();
				$(temp_currentFwqAll).each(function(){
					if($("#fwqMore"+this).css("display") == "block") {
						adjustFwqYyHeight(this, ptlx);
					}
				});
			}
			
			//查看服务器日志列表
			function showFwqLog(fwqip, dk) {
				var html;
				// 先清空之前的内容
				
				if ($("#tableId")) {
					$("#tableId").remove();
				}
				
				/* var thisTable = document.getElementById("tableId");
				if (thisTable != null) {
					thisTable.parentNode.removeChild(thisTable);	
				} */
				var url = "../fwqgl/fwqgetTomcatLogs.action";
				//根据服务器或者托管服务器设置不同的请求路径
				/* if (logServerType == "yun") {
					//云平台的
					url = "../fwqgl/fwqgetLogs.action";
				} else {
					url = "../fwqgl/tgFwqgetLogs.action";
				} */
				//异步发出请求，获得日志文件列表
				$.ajax({
					type: "POST",
					async:true,
			   		url: url,
			   		dataType:"json",
			   		data:"fwqip="+fwqip,
					success: function(data){
						// 将值拼成HTML显示到页面中
						if (data != null && data.length>0) {
							if ($("#tableId")) {
								$("#tableId").remove();
							}
							//循环之前的HTML代码
							var html_table = "<table id='tableId' cellspacing='10'><tr><td class='log_div_td1'>文件名</td><td class='log_div_td2'>文件大小</td><td colspan='2'>操作</td></tr><tr><td colspan='4'><hr/></td></tr>";
							//循环生成的HTML代码
							var html_tr = "";
							for(var i=0;i<data.length;i++){
								var logName = data[i][0];	// 日志文件的名字
								html_tr = html_tr.concat('<tr><td class="log_div_td1">'+data[i][0]+'</td><td class="log_div_td2">'+data[i][1]+'</td><td class="log_div_td3"><a href="javascript:void(0)" onclick="showLog(&quot;'+logName+'&quot;,&quot;'+fwqip+'&quot;,&quot;'+dk+'&quot;)">查看</a></td><td class="log_div_td3"><a id="downLogId'+i+'" href="javascript:void(0)" onclick="downLog(&quot;'+logName+'&quot;,&quot;'+fwqip+'&quot;,&quot;'+i+'&quot;,&quot;'+dk+'&quot;)">下载</a></td></tr>');
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
			function downLog(logName,fwqip,index,dk){
				// 为了解决如果下载的日志文件不存在的问题，采用异步的方式
				$.ajax({
					url:"http://"+fwqip+":"+dk+"/fwzxutils/fileDeal.do?methodName=isTomcatLogExist&logName="+logName,
					type:"get",
					dataType:"jsonp",	//浏览器的跨域请求问题，使用jsonp解决
					success:function(data){
						if (data.isFileExist == "error") {
							alert("日志不存在,可能已被删除");
						}else if (data.isFileExist == "yes") {
							location.href = "http://"+fwqip+":"+dk+"/fwzxutils/fileDeal.do?methodName=downTomcatLog&logName="+logName;
						}
					}
				});
			}
			
			//****************************服务器应用操作**************************
			//查看应用日志
			function showFwqYyLog(fwqip, dk, warname) {
				var appname = warname.substring(0,warname.length-4);
				var url = "../yygl/yyisYyLogExist.action";
				/* if (serverType=="yun") {
					url = "../yygl/yyisYyLogExist.action";
				}else {
					url = "../yygl/tgyyisYyLogExist.action";
				} */
				//发送异步请求获取文件是否存在
				$.ajax({
					type: "POST",
			   		url: url,
			   		data:"appname="+appname+"&ip="+fwqip,
			   		dataType:"json",
					success: function(resultBean){
						if(resultBean.statusCode == "0000"){
							//说明存在，直接访问
							var u = "http://"+fwqip+":"+dk+"/fwzxutils/fileDeal.do?methodName=showAppLog&yyLogPath="+encodeURI(resultBean.arg1);
							window.open(u);
		  				}else{
		  					alert(resultBean.msg);
		  					return false;
		  				}
					}
				});
			}
			
			//访问服务器应用
			function viewFwqYy(fwqip, dk, warname) {
				var appname = warname.substring(0,warname.length-4);
				var url = "http://"+fwqip+":"+dk+"/"+appname;
				window.open(url);
			}
			
			//************************动态更新监控信息*****************************
			//初始化创建定时器，5秒后开始定时刷新
			function init() {
			
				setFloatByWidth();
			
				setTimeout(function(){
					//updateMonitorInfo();
					setInterval(function(){
						updateMonitorAllInfo();
					},30*1000);
				},5*1000);
			}
			
			//手动更新首页监控界面信息
			function updateMonitorAllInfo() {
				updateMonitorInfo('<%=SystemConstants.PTLX_YUN%>');
				updateMonitorInfo('<%=SystemConstants.PTLX_TG%>');
			}
			
			//更新监控信息
			function updateMonitorInfo(ptlx) {
				$.ajax({
					type: "POST",
			   		url: "../index/indexgetRefreshInfo.action",
			   		data:"ptlx="+ptlx,
			   		dataType:"json",
					success: function(data){
					
						//如果为空，就不更新
						if (data == null || data == "") {
							return;
						}
					
						var yyAll = data["yyAll"];
						var fwqAll = data["fwqAll"];
						var connectInfo = data["connectInfo"];
						//在更新之后，根据新的num，调节总高度
						
						if (fwqAll && fwqAll != null && fwqAll != "") {
							if (ptlx == ptlx_yun) {
								adjustTotalHeight(totalHeight, fwqAll.length, ptlx_yun);
							}
							if (ptlx == ptlx_tg) {
								adjustTotalHeight(tgtotalHeight, fwqAll.length, ptlx_tg);
							}
							
							updateFwqInfoByPtlx(fwqAll,yyAll,connectInfo,ptlx);
							//执行完成之后，更新高度信息
							autoFwqHeight(ptlx);
							//autoYyHeight(ptlx);
							//初始化服务器界面
							resetFwqLayout(ptlx);
							//resetYyLayout(ptlx);
						}
					}
				});
			}
			
			//抽取出来的更新服务器信息的方法
			function updateFwqInfoByPtlx(fwqAll,yyAll,connectInfo,ptlx) {
				
				//操作之前，将新信息进行备份，之后要用
				var tempFwqAll = fwqAll.slice();
				var tempYyAll = yyAll.slice();
			
				//遍历服务器，将要添加的、修改的、删除的存到数组中
				var fwqToAddArr = new Array();
				var fwqToUpdateArr = new Array();
				var fwqToRemoveArr = new Array();
				//遍历应用，将要添加的、修改的、删除的存到数组中
				var yyToAddArr = new Array();
				var yyToUpdateArr = new Array();
				var yyToRemoveArr = new Array();
				
				//遍历服务器，将要修改的和要删除的进行整理
				if (ptlx == ptlx_tg) {
					for(var i in currentTgFwqAll) {
						var currentFwqId = currentTgFwqAll[i];
						var flag = true;
						for(var k in fwqAll) {
							if (fwqAll[k]["id"].indexOf(currentFwqId) != -1) {
								//新的中包含有当前的该服务器,放入修改数组中
								fwqToUpdateArr.push(fwqAll[k]);
								delete(fwqAll[k]);
								flag = false;
								break;
							}
						}
						if (flag) {
							//新的中不包含当前该服务器，将当前的这个放入删除的数组中
							fwqToRemoveArr.push(currentFwqId);
						}
					}
				}else {
					for(var i in currentFwqAll) {
						var currentFwqId = currentFwqAll[i];
						var flag = true;
						for(var k in fwqAll) {
							if (fwqAll[k]["id"].indexOf(currentFwqId) != -1) {
								//新的中包含有当前的该服务器,放入修改数组中
								fwqToUpdateArr.push(fwqAll[k]);
								delete(fwqAll[k]);
								flag = false;
								break;
							}
						}
						if (flag) {
							//新的中不包含当前该服务器，将当前的这个放入删除的数组中
							fwqToRemoveArr.push(currentFwqId);
						}
					}
				}
				//现在新的数组中剩下的就是要添加的
				for(var n in fwqAll) {
					fwqToAddArr.push(fwqAll[n]);
				}
				//将对应的数组中的数据做相应的处理
				if (fwqToAddArr && fwqToAddArr.length>0) {
					$(fwqToAddArr).each(function(){
						asyncAddFwq(this, ptlx);
					});
				}
				if (fwqToUpdateArr && fwqToUpdateArr.length>0) {
					$(fwqToUpdateArr).each(function(){
						asyncUpdateFwq(this, ptlx);
					});
				}
				if (fwqToRemoveArr && fwqToRemoveArr.length>0) {
					$(fwqToRemoveArr).each(function(){
						asyncRemoveFwq(this, ptlx);
					});
				}
				
				//执行完成之后，需要更新当前的数组信息
				if (ptlx == ptlx_tg) {
					currentTgFwqAll.length = 0;
					for(var i in tempFwqAll) {
						currentTgFwqAll.push(tempFwqAll[i]["id"]);
					}
					//这里需要将map进行复制，暂时就在这里直接遍历赋值了
					for(var key in currentTgconnectInfo) {
						delete(currentTgconnectInfo[key]);
					}
					for(var key in connectInfo) {
						//currentconnectInfo.put(key,connectInfo[key]);
						currentTgconnectInfo[key] = connectInfo[key];
					}
				}else {
					currentFwqAll.length = 0;
					for(var i in tempFwqAll) {
						currentFwqAll.push(tempFwqAll[i]["id"]);
					}
					//这里需要将map进行复制，暂时就在这里直接遍历赋值了
					for(var key in currentconnectInfo) {
						delete(currentconnectInfo[key]);
					}
					for(var key in connectInfo) {
						//currentconnectInfo.put(key,connectInfo[key]);
						currentconnectInfo[key] = connectInfo[key];
					}
				}
			}
			
			//抽取出来的创建应用的拼接代码
			function createFwqYyInfoHtml(fwqInfo) {
				var fwqip = fwqInfo["ptlx"] == '01' ? fwqInfo["fwqip"] : fwqInfo["fwqip_ww"];
				var fwqYyInfoDivHtml = '<table align="center"><tr><td style="text-align: center;height: 20px;" colspan="2"><h3 style="line-height: 20px;">（<span class="fwqfwqname'+fwqInfo["id"]+'">'+fwqInfo["fwqname"]+'</span>）应用信息'
								+ '<span style="float: right;margin-right: 25px;" ><a href="javascript:void(0)" onclick="toFwqLayOut('+fwqInfo["id"]+','+fwqInfo["ptlx"]+')">返回</a></span>'
								+'</h3></td></tr>'
								+ '<tr><td colspan="2" style="height: 20px;"><hr/></td></tr>';
				if (fwqInfo["yyInfo"]) {
					for(var i in fwqInfo["yyInfo"]) {
						fwqYyInfoDivHtml += '<tr><td  onmouseover="showFwqYyLinerInfo(\''+fwqInfo["yyInfo"][i]["id"]+'\',\''+fwqInfo["id"]+'\')" ' 
								+ 'onmouseout="hideFwqYyLinerInfo(\''+fwqInfo["yyInfo"][i]["id"]+'\',\''+fwqInfo["id"]+'\')">'
								+ '<a href="javascript:void(0)" onclick="showFwqYyInfo(&quot;'+fwqInfo["yyInfo"][i]["id"]+'&quot;,&quot;'+fwqInfo["yyInfo"][i]["ptlx"]+'&quot;)">'+fwqInfo["yyInfo"][i]["yyname"]+'</a>'
								+ '</td><td><span>';
						if (!fwqInfo["yyInfo"][i]["yyyxzt"]) {
							fwqYyInfoDivHtml += '<font color="#F8D080">检测中</font>';
						}else if (fwqInfo["yyInfo"][i]["yyyxzt"] == 0) {
							fwqYyInfoDivHtml += '<font color="green">正常</font>';
						}else {
							fwqYyInfoDivHtml += '<font color="red">异常</font>';
						}
						fwqYyInfoDivHtml += '</span>&nbsp\;&nbsp\;&nbsp\;<a href="javascript:void(0)" '
										 + 'onclick="showFwqYyLog(&quot;'+fwqip+'&quot;,&quot;'+fwqInfo["dk"]+'&quot;,&quot;'+fwqInfo["yyInfo"][i]["warname"]+'&quot;)"'
										 + '>日志</a>&nbsp\;&nbsp\;&nbsp\;<a href="javascript:void(0)" '
										 + 'onclick="viewFwqYy(&quot;'+fwqip+'&quot;,&quot;'+fwqInfo["dk"]+'&quot;,&quot;'+fwqInfo["yyInfo"][i]["warname"]+'&quot;)"'
										 + '>访问</a>'
									   + '</td></tr>';
					}
				}
				fwqYyInfoDivHtml += '</table>';
				return fwqYyInfoDivHtml;
			}
			
			//异步添加一个服务器监控信息
			function asyncAddFwq(fwqInfo, ptlx) {
			
				var status = fwqInfo["fwqstatus_dicvalue"];
				var fwqip = fwqInfo["fwqip"];
				var fwqip_ww = fwqInfo["fwqip_ww"];
				var dk = fwqInfo["dk"];
				var fwqipInfo = ptlx == '01' ? fwqip : fwqip_ww;
			
				var fwqOuterDiv=$('<div id="fwqOuter'+fwqInfo["id"]+'" class="fwqOuteritem" ' 
								+ 'onmouseover="showFwqLinerInfo(this)" onmouseout="hideFwqLinerInfo(this)"></div>');//创建一个外面的div
				//创建fwqId的div
				var fwqIdDivHtml = '<div id="fwq'+fwqInfo["id"]+'" class="fwqitem" title="'+status+'">'
								+ '<table class="mb-table show-td" border="0" cellpadding="0" cellspacing="0">'
								+ '<tr><td colspan="4" style="height: 20px;">'+fwqipInfo+' / <span class="fwqfwqname'+fwqInfo["id"]+'">'+fwqInfo["fwqname"]+'</span>'
								+ '&nbsp\;&nbsp\;&nbsp\;&nbsp\;【<a href="javascript:void(0)" id="logShow'+fwqInfo["id"]+'" class="logShowClass" onclick="showFwqLog(&quot;'+fwqip_ww+'&quot;,&quot;'+dk+'&quot;)">日志</a>】'
								+ '</td></tr><tr><td colspan="2" style="height: 20px;">运行情况</td><td colspan="2" style="height: 20px;">服务器信息</td></tr>'
								+ '<tr><td rowspan="3" colspan="2">';
				if (fwqInfo["msgInfoList"]) {
					fwqIdDivHtml += '<table  id="fwqyxqk'+fwqInfo["id"]+'">';
					for(var i in fwqInfo["msgInfoList"]) {
						fwqIdDivHtml += '<tr><td style="height: 20px;line-height: 20px;">';
						if (fwqInfo["msgInfoList"][i]["level"] == "error") {
							fwqIdDivHtml += '<font color="red">'+fwqInfo["msgInfoList"][i]["msg"]+'</font>';
						}
						if (fwqInfo["msgInfoList"][i]["level"] == "info") {
							fwqIdDivHtml += '<font color="#FFA815">'+fwqInfo["msgInfoList"][i]["msg"]+'</font>';
						}
						if (fwqInfo["msgInfoList"][i]["level"] == "success") {
							fwqIdDivHtml += '<font color="green">'+fwqInfo["msgInfoList"][i]["msg"]+'</font>';
						}
						fwqIdDivHtml += '</td></tr>';
					}
					fwqIdDivHtml += '</table>';
				}
				
				fwqIdDivHtml += '</td><td>cpu / 内存</td><td><span>';
				//cpu
				var cont = fwqInfo["cpuused"];
				var percent = cont.split("%")[0];
				if (percent > 80) {
					cont = '<font color="red">'+cont+'</font>';
				}
				fwqIdDivHtml += cont;
				
				//内存
				fwqIdDivHtml += '</span> / <span>';
				var cont = fwqInfo["memeryused"];
				var percent = cont.split("%")[0];
				if (percent > 80) {
					cont = '<font color="red">'+cont+'</font>';
				}
				fwqIdDivHtml += cont;
				
				fwqIdDivHtml += '</span></td></tr>'
							 + '<tr><td>端口号</td><td>'+fwqInfo["dk"]+'</td></tr>'
							 + '<tr><td><a href="javascript:void(0)" onclick="showYyInfo(&quot;'+fwqInfo["id"]+'&quot;,&quot;'+fwqInfo["ptlx"]+'&quot;)">应用信息</a></td>'
							 + '<td><a href="javascript:void(0)" onclick="showMoreInfo(&quot;'+fwqInfo["id"]+'&quot;,&quot;'+fwqInfo["ptlx"]+'&quot;)">详细信息</a></td></tr></table></div>'
				
				var fwqItemDiv = $(fwqIdDivHtml);	//服务器item的div
				//将该div放进外面大的div中
				$(fwqOuterDiv).append(fwqItemDiv);
				
				//创建fwqYyInfoId的div
				var fwqYyInfoDivHtml = '<div id="fwqYyInfo'+fwqInfo["id"]+'" class="fwqYyInfoitem" onmouseover="adjustFwqYyHeight('+fwqInfo["id"]+','+fwqInfo["ptlx"]+')" title="'+status+'" style="display: none;">';
				fwqYyInfoDivHtml += createFwqYyInfoHtml(fwqInfo)+'</div>';
				var fwqYyInfoItemDiv = $(fwqYyInfoDivHtml);
				//将该div放进外面大的div中
				$(fwqOuterDiv).append(fwqYyInfoItemDiv);
				
				
				//创建详细信息的div
				var fwqMoreDivHtml = '<div id="fwqMore'+fwqInfo["id"]+'" class="fwqMoreitem" onmouseover="adjustFwqMoreHeight('+fwqInfo["id"]+','+fwqInfo["ptlx"]+')" title="'+status+'" style="display: none;">'
								+ '<table align="center"><tr><td style="text-align: center;height: 20px;" colspan="2"><h3 style="line-height: 20px;">（<span class="fwqfwqname'+fwqInfo["id"]+'">'+fwqInfo["fwqname"]+'</span>）服务器信息'
								+ '<span style="float: right;margin-right: 25px;" ><a href="javascript:void(0)" onclick="toFwqLayOutFromMore('+fwqInfo["id"]+','+fwqInfo["ptlx"]+')">返回</a></span>'
								+'</h3></td></tr>'
								+ '<tr><td colspan="2" style="height: 20px;"><hr/></td></tr>'
								+ '<tr><td>操作系统</td><td><span id="fwqczxt$'+fwqInfo["id"]+'">'+fwqInfo["fwqczxt_dicvalue"]+'</span></td></tr>';
								+ '<tr><td>所在机房</td><td><span id="fwqszjf$'+fwqInfo["id"]+'">'+fwqInfo["szjf"]+'</span></td></tr>';
								+ '<tr><td>对外地址</td><td><span id="fwqfwqym$'+fwqInfo["id"]+'">'+fwqInfo["fwqfwqym"]+'</span></td></tr>';
								+ '<tr><td>线程数量</td><td><span id="fwqthreadcount$'+fwqInfo["id"]+'">'+fwqInfo["threadcount"]+'</span></td></tr>';
								+ '<tr><td>JVM可用堆内存</td><td><span id="fwqjvmmemory$'+fwqInfo["id"]+'">'+fwqInfo["jvmmemory"]+'</span></td></tr>';
								+ '<tr><td>JVM运行线程数</td><td><span id="fwqjvmthreadcount$'+fwqInfo["id"]+'">'+fwqInfo["jvmthreadcount"]+'</span></td></tr>';
								+ '<tr><td>信息更新时间</td><td><span id="fwqupdatetime$'+fwqInfo["id"]+'">'+fwqInfo["updatetime"]+'</span></td></tr>'
								+ '</table></div>';
				
				var fwqMoreItemDiv = $(fwqMoreDivHtml);
				//将该div放进外面大的div中
				$(fwqOuterDiv).append(fwqMoreItemDiv);
				
				//将大的div放进容器中
				if (ptlx == ptlx_tg) {
					$("#tgfwqContent").append(fwqOuterDiv);
					tg_fwq_num ++;	//将服务器数量+1
				}else {
					$("#fwqContent").append(fwqOuterDiv);
					fwq_num ++;	//将服务器数量+1
				}
				//将服务器的日志信息绑定弹出层
				$("#logShow"+fwqInfo["id"]).alertBox({ id:"#logDiv",borderWidth: "0px", level: "1000"});
			}
			//异步移除一个服务器监控信息
			function asyncRemoveFwq(fwqid, ptlx) {
				//直接将outer大的移除掉即可
				$("#fwqOuter"+fwqid).remove();
				//将数量-1
				if (ptlx == ptlx_tg) {
					tg_fwq_num --;	//将服务器数量+1
				}else {
					fwq_num --;
				}
			}
			
			//异步修改一个服务器监控信息
			function asyncUpdateFwq(fwqInfo) {
				//需要修改的地方为三个
				//1、预警信息，生成并替换即可
				var fwqid = fwqInfo["id"];
				var yxqkHtml = '';
				
				if (fwqInfo["msgInfoList"]) {
					for(var i in fwqInfo["msgInfoList"]) {
						yxqkHtml += '<tr><td style="height: 20px;line-height: 20px;">';
						if (fwqInfo["msgInfoList"][i]["level"] == "error") {
							yxqkHtml += '<font color="red">'+fwqInfo["msgInfoList"][i]["msg"]+'</font>';
						}
						if (fwqInfo["msgInfoList"][i]["level"] == "info") {
							yxqkHtml += '<font color="#FFA815">'+fwqInfo["msgInfoList"][i]["msg"]+'</font>';
						}
						if (fwqInfo["msgInfoList"][i]["level"] == "success") {
							yxqkHtml += '<font color="green">'+fwqInfo["msgInfoList"][i]["msg"]+'</font>';
						}
					}
					yxqkHtml += '</td></tr>';
				}
				
				$("#fwqyxqk"+fwqid).html(yxqkHtml);
				//2、应用信息，生成并替换
				//创建fwqYyInfoId的html
				var fwqYyInfoDivHtml = createFwqYyInfoHtml(fwqInfo);
				$("#fwqYyInfo"+fwqid).html(fwqYyInfoDivHtml);
				
				//3、服务器详细信息，直接修改
				//fwqname修改（fwqname用到的地方多，就使用class作为标识了）
				$(".fwqfwqname"+fwqid).each(function(){
					$(this).html(fwqInfo["fwqname"]);
				});
				//cpu/内存/端口
				
				var contCpu = fwqInfo["cpuused"];
				var percent = contCpu.split("%")[0];
				if (percent > 80) {
					contCpu = '<font color="red">'+contCpu+'</font>';
				}
				var contMem = fwqInfo["memeryused"];
				var percent = contMem.split("%")[0];
				if (percent > 80) {
					contMem = '<font color="red">'+contMem+'</font>';
				}
				$("#fwqcpuused"+fwqid).html(contCpu);
				$("#fwqmemeryused"+fwqid).html(contMem);
				$("#fwqdk"+fwqid).html(fwqInfo["dk"]);
				//操作系统/所在机房/对外地址
				$("#fwqczxt"+fwqid).html(fwqInfo["czxt_dicvalue"]);
				$("#fwqszjf"+fwqid).html(fwqInfo["szjf"]);
				$("#fwqfwqym"+fwqid).html(fwqInfo["fwqym"]);
				//线程数量/JVM可用堆内存/JVM运行线程数/信息更新时间
				$("#fwqthreadcount"+fwqid).html(fwqInfo["threadcount"]);
				$("#fwqjvmmemory"+fwqid).html(fwqInfo["jvmmemory"]);
				$("#fwqjvmthreadcount"+fwqid).html(fwqInfo["jvmthreadcount"]);
				$("#fwqupdatetime"+fwqid).html(fwqInfo["updatetime"]);
				
				//对div上面的title的修改（用来判断服务器的异常状态）
				$("#fwq"+fwqid).attr("title",fwqInfo["fwqstatus_dicvalue"]);
				$("#fwqYyInfo"+fwqid).attr("title",fwqInfo["fwqstatus_dicvalue"]);
				$("#fwqMore"+fwqid).attr("title",fwqInfo["fwqstatus_dicvalue"]);
			}
		</script>
	</head>
	<body style="" onload="init()">
		<div class="bmain-round" style="border: 1px solid #111;">
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx" style="width: 100%">
						<b>当前位置：</b> &gt
						<span>总线监控</span>
						<span style="float: right; padding-right: 50px"> <a href="javascript:void(0)" onclick="updateMonitorAllInfo()">更新</a></span>
					</div>
				</div>
				<div class="main_content">
					<!-- 云平台服务器监控 -->
					<div class="main_left">
						<center><font size="4" color="black">云平台服务器监控</font></center>
						<div id="yunServerPlumb">
							<!-- 应用信息 -->
							<div id="yyContent" class="yyContent">
								<c:if test="${not empty yyAll}">
									<c:forEach items="${yyAll}" var="yyInfo">
										<script>
											//将初始化应用信息保存到当前的应用数组中
											currentYyAll.push('${yyInfo.id}');
										</script>
										<div id="yyOuter${yyInfo.id}">
											<a href="javascript:void(0)" style="color: black;" onclick="showYyMore('${yyInfo.id}','${yyInfo.ptlx}')">
												<div id="yy${yyInfo.id}" class="yyitem  btn-primary btn-lg btn-block">
													<table>
														<tr>
															<td>${yyInfo.yyname} / ${yyInfo.yyversion}</td>
														</tr>
													</table>
												</div>
											</a>
											<a href="javascript:void(0)" onmouseover="adjustYyMoreHeight('${yyInfo.id}','${yyInfo.ptlx}')" style="color: black;" onclick="showYyItem('${yyInfo.id}','${yyInfo.ptlx}')">
												<div id="yyMore${yyInfo.id}" class="yyMoreitem" style="display: none;">
													<table id="yyyxqk${yyInfo.id}">
														<c:if test="${not empty yyInfo.fwqInfoList}">
															<tr>
																<td style="height: 20px;" colspan="2">${yyInfo.yyname}</td>
															</tr>
															<tr>
																<td style="height: 2px;" colspan="2"><hr/></td>
															</tr>
															<c:forEach items="${yyInfo.fwqInfoList}" var="fwqInfo">
																<tr>
																	<td>${fwqInfo.fwqname}</td>
																	<td>
																		<span>
																			<c:if test="${empty fwqInfo.yyyxzt}"><font color="#F8D080">检测中</font></c:if>
																			<c:if test="${fwqInfo.yyyxzt==0}"><font color="green">正常</font></c:if>
																			<c:if test="${fwqInfo.yyyxzt==1}"><font color="red">异常</font></c:if>
																		</span>
																	</td>
																</tr>
															</c:forEach>
														</c:if>
													</table>
												</div>
											</a>
										</div>
									</c:forEach>
								</c:if>
							</div>
							<!-- 服务器信息 -->
							<div id="fwqContent" class="fwqContent">
								<c:if test="${not empty fwqAll}">
									<c:forEach items="${fwqAll}" var="fwqInfo" varStatus="i">
										<script>
											//将初始化服务器信息保存到当前的服务器数组中
											currentFwqAll.push('${fwqInfo.id}');
										</script>
										<div id="fwqOuter${fwqInfo.id}" class="fwqOuteritem" onmouseover="showFwqLinerInfo(this)" 
											onmouseout="hideFwqLinerInfo(this)">
											<div id="fwq${fwqInfo.id}" class="fwqitem" title="${fwqInfo.fwqstatus_dicvalue}">
												<table class="mb-table show-td" border="0" cellpadding="0" cellspacing="0">
													<tr><td colspan="4" style="height: 20px;">${fwqInfo.fwqip} / <span class="fwqfwqname${fwqInfo.id}">${fwqInfo.fwqname}</span> 
														&nbsp;&nbsp;&nbsp;&nbsp;【<a href="javascript:void(0)" id="logShow${fwqInfo.id}" class="logShowClass" onclick="showFwqLog('${fwqInfo.fwqip_ww}','${fwqInfo.dk}')">日志</a>】	
													</td></tr>
													<tr>
														<td colspan="2" style="height: 20px;">运行情况</td>
														<td colspan="2" style="height: 20px;">服务器信息</td>
													</tr>
													<tr>
														<td rowspan="3" colspan="2">
															<table id="fwqyxqk${fwqInfo.id}">
																<c:if test="${not empty fwqInfo.msgInfoList}">
																	<c:forEach var="msgInfo" items="${fwqInfo.msgInfoList}">
																		<tr>
																			<td style="height: 20px;line-height: 20px;">
																				<c:if test="${msgInfo.level == 'error'}">
																					<font color="red">${msgInfo.msg}</font>
																				</c:if>
																				<c:if test="${msgInfo.level == 'info'}">
																					<font color="#FFA815">${msgInfo.msg}</font>
																				</c:if>
																				<c:if test="${msgInfo.level == 'success'}">
																					<font color="green">${msgInfo.msg}</font>
																				</c:if>
																			</td>
																		</tr>
																	</c:forEach>
																</c:if>
															</table>
														</td>
														<td>cpu / 内存</td>
														<td><span id="fwqcpuused${fwqInfo.id}">
																<script type="text/javascript">
																	var cont = '${fwqInfo.cpuused}';
																	var percent = '${fwqInfo.cpuused}'.split("%")[0];
																	if (percent > 80) {
																		cont = '<font color="red">'+cont+'</font>';
																	}else {
																		
																	}
																	document.write(cont);
																</script>
															</span> / 
															<span id="fwqmemeryused${fwqInfo.id}">
																<script type="text/javascript">
																	var cont = '${fwqInfo.memeryused}';
																	var percent = '${fwqInfo.memeryused}'.split("%")[0];
																	if (percent > 80) {
																		cont = '<font color="red">'+cont+'</font>';
																	}else {
																		
																	}
																	document.write(cont);
																</script>
																<%-- ${fwqInfo.memeryused} --%>
															</span></td>
														<%-- <td><span id="fwqcpuused${fwqInfo.id}">${fwqInfo.cpuused}</span> / <span id="fwqmemeryused${fwqInfo.id}">${fwqInfo.memeryused}</span></td> --%>
													</tr>
													<tr>
														<td>端口号</td>
														<td><span id="fwqdk">${fwqInfo.dk}</span></td>
													</tr>
													<tr>
														<td><a href="javascript:void(0)" onclick="showYyInfo('${fwqInfo.id}','${fwqInfo.ptlx}')">应用信息</a></td>
														<td><a href="javascript:void(0)" onclick="showMoreInfo('${fwqInfo.id}','${fwqInfo.ptlx}')">详细信息</a></td>
													</tr>
												</table>
											</div>
											<div id="fwqYyInfo${fwqInfo.id}" class="fwqYyInfoitem" onmouseover="adjustFwqYyHeight('${fwqInfo.id}','${fwqInfo.ptlx}')" title="${fwqInfo.fwqstatus_dicvalue}" style="display: none;">
												<table align="center">
													<tr>
														<td style="text-align: center;height: 20px;" colspan="2">
															<h3 style="line-height: 20px;"><span class="fwqfwqname">（${fwqInfo.fwqname}）</span>应用信息
																<span style="float: right;margin-right: 25px;" ><a href="javascript:void(0)" onclick="toFwqLayOut('${fwqInfo.id}','${fwqInfo.ptlx}')">返回</a></span>
															</h3>
														</td>
													</tr>
													<tr>
														<td colspan="2" style="height: 2px;"><hr/></td>
													</tr>
													<c:if test="${not empty fwqInfo.yyInfo}">
														<c:forEach var="yyInfo" items="${fwqInfo.yyInfo}">
															<tr>
																<td  onmouseover="showFwqYyLinerInfo('${yyInfo.id}','${fwqInfo.id}')" onmouseout="hideFwqYyLinerInfo('${yyInfo.id}','${fwqInfo.id}')">
																	<a href="javascript:void(0)" onclick="showFwqYyInfo('${yyInfo.id}','${yyInfo.ptlx}')">${yyInfo.yyname}</a>
																</td>
																<td>
																	<span>
																		<c:if test="${empty yyInfo.yyyxzt}"><font color="#F8D080">检测中</font></c:if>
																		<c:if test="${yyInfo.yyyxzt==0}"><font color="green">正常</font></c:if>
																		<c:if test="${yyInfo.yyyxzt==1}"><font color="red">异常</font></c:if>
																	</span>
																	&nbsp;&nbsp;&nbsp;
																	<a href="javascript:void(0)" onclick="showFwqYyLog('${fwqInfo.fwqip_ww}','${fwqInfo.dk}','${yyInfo.warname}')">日志</a>&nbsp;&nbsp;&nbsp;
																	<a href="javascript:void(0)" onclick="viewFwqYy('${fwqInfo.fwqip}','${fwqInfo.dk}','${yyInfo.warname}')">访问</a>
																</td>
															</tr>
														</c:forEach>
													</c:if>
												</table>
											</div>
											<div id="fwqMore${fwqInfo.id}" class="fwqMoreitem" onmouseover="adjustFwqMoreHeight('${fwqInfo.id}','${fwqInfo.ptlx}')" title="${fwqInfo.fwqstatus_dicvalue}" style="display: none;">
												<table align="center">
													<tr>
														<td style="text-align: center;height: 20px;" colspan="2">
															<h3 style="line-height: 20px;"><span class="fwqname${fwqInfo.id}">（${fwqInfo.fwqname}）</span>服务器信息
																<span style="float: right;margin-right: 25px;" ><a href="javascript:void(0)" onclick="toFwqLayOutFromMore('${fwqInfo.id}','${fwqInfo.ptlx}')">返回</a></span>
															</h3>
														</td>
													</tr>
													<tr>
														<td colspan="2" style="height: 2px;"><hr/></td>
													</tr>
													<tr>
														<td>操作系统</td>
														<td><span id="fwqczxt${fwqInfo.id}">${fwqInfo.fwqczxt_dicvalue}</span></td>
													</tr>
													<tr>
														<td>所在机房</td>
														<td><span id="fwqszjf${fwqInfo.id}">${fwqInfo.szjf}</span></td>
													</tr>
													<tr>
														<td>对外地址</td>
														<td><span id="fwqfwqym${fwqInfo.id}">${fwqInfo.fwqym}</span></td>
													</tr>
													<tr>
														<td>线程数量</td>
														<td><span id="fwqthreadcount${fwqInfo.id}">${fwqInfo.threadcount}</span></td>
													</tr>
													<tr>
														<td>JVM可用堆内存</td>
														<td><span id="fwqjvmmemory${fwqInfo.id}">${fwqInfo.jvmmemory}</span></td>
													</tr>
													<tr>
														<td>JVM运行线程数</td>
														<td><span id="fwqjvmthreadcount${fwqInfo.id}">${fwqInfo.jvmthreadcount}</span></td>
													</tr>
													<tr>
														<td>信息更新时间</td>
														<td><span id="fwqupdatetime${fwqInfo.id}">${fwqInfo.updatetime}</span></td>
													</tr>
												</table>
											</div>
										</div>
									</c:forEach>
								</c:if>
							</div>
						</div>
					</div>
					<!-- 托管服务器监控 -->
					<div class="main_middle">
						<center><font size="4" color="black">托管服务器监控</font></center>
						<div id="tgServerPlumb">
							<!-- 服务器信息 -->
							<div id="tgfwqContent" class="fwqContent">
								<c:if test="${not empty tgfwqAll}">
									<c:forEach items="${tgfwqAll}" var="fwqInfo" varStatus="i">
										<script>
											//将初始化服务器信息保存到当前的服务器数组中
											currentTgFwqAll.push('${fwqInfo.id}');
										</script>
										<div id="fwqOuter${fwqInfo.id}" class="fwqOuteritem" onmouseover="showFwqLinerInfo(this)" 
											onmouseout="hideFwqLinerInfo(this)">
											<div id="fwq${fwqInfo.id}" class="fwqitem" title="${fwqInfo.fwqstatus_dicvalue}">
												<table class="mb-table show-td" border="0" cellpadding="0" cellspacing="0">
													<tr><td colspan="4" style="height: 20px;">${fwqInfo.fwqip_ww} / <span class="fwqfwqname${fwqInfo.id}">${fwqInfo.fwqname}</span> 
														&nbsp;&nbsp;&nbsp;&nbsp;【<a href="javascript:void(0)" id="logShow${fwqInfo.id}" class="logShowClass" onclick="showFwqLog('${fwqInfo.fwqip_ww}','${fwqInfo.dk}')">日志</a>】	
													</td></tr>
													<tr>
														<td colspan="2" style="height: 20px;">运行情况</td>
														<td colspan="2" style="height: 20px;">服务器信息</td>
													</tr>
													<tr>
														<td rowspan="3" colspan="2">
															<table id="fwqyxqk${fwqInfo.id}">
																<c:if test="${not empty fwqInfo.msgInfoList}">
																	<c:forEach var="msgInfo" items="${fwqInfo.msgInfoList}">
																		<tr>
																			<td style="height: 20px;line-height: 20px;">
																				<c:if test="${msgInfo.level == 'error'}">
																					<font color="red">${msgInfo.msg}</font>
																				</c:if>
																				<c:if test="${msgInfo.level == 'info'}">
																					<font color="#FFA815">${msgInfo.msg}</font>
																				</c:if>
																				<c:if test="${msgInfo.level == 'success'}">
																					<font color="green">${msgInfo.msg}</font>
																				</c:if>
																			</td>
																		</tr>
																	</c:forEach>
																</c:if>
															</table>
														</td>
														<td>cpu / 内存</td>
														<td><span id="fwqcpuused${fwqInfo.id}">
																<script type="text/javascript">
																	var cont = '${fwqInfo.cpuused}';
																	var percent = '${fwqInfo.cpuused}'.split("%")[0];
																	if (percent > 80) {
																		cont = '<font color="red">'+cont+'</font>';
																	}else {
																		
																	}
																	document.write(cont);
																</script>
															</span> / 
															<span id="fwqmemeryused${fwqInfo.id}">
																<script type="text/javascript">
																	var cont = '${fwqInfo.memeryused}';
																	var percent = '${fwqInfo.memeryused}'.split("%")[0];
																	if (percent > 80) {
																		cont = '<font color="red">'+cont+'</font>';
																	}else {
																		
																	}
																	document.write(cont);
																</script>
																<%-- ${fwqInfo.memeryused} --%>
															</span></td>
													</tr>
													<tr>
														<td>端口号</td>
														<td><span id="fwqdk">${fwqInfo.dk}</span></td>
													</tr>
													<tr>
														<td><a href="javascript:void(0)" onclick="showYyInfo('${fwqInfo.id}','${fwqInfo.ptlx}')">应用信息</a></td>
														<td><a href="javascript:void(0)" onclick="showMoreInfo('${fwqInfo.id}','${fwqInfo.ptlx}')">详细信息</a></td>
													</tr>
												</table>
											</div>
											<div id="fwqYyInfo${fwqInfo.id}" class="fwqYyInfoitem" onmouseover="adjustFwqYyHeight('${fwqInfo.id}','${fwqInfo.ptlx}')" title="${fwqInfo.fwqstatus_dicvalue}" style="display: none;">
												<table align="center">
													<tr>
														<td style="text-align: center;height: 20px;" colspan="2">
															<h3 style="line-height: 20px;"><span class="fwqfwqname">（${fwqInfo.fwqname}）</span>应用信息
																<span style="float: right;margin-right: 25px;" ><a href="javascript:void(0)" onclick="toFwqLayOut('${fwqInfo.id}','${fwqInfo.ptlx}')">返回</a></span>
															</h3>
														</td>
													</tr>
													<tr>
														<td colspan="2" style="height: 2px;"><hr/></td>
													</tr>
													<c:if test="${not empty fwqInfo.yyInfo}">
														<c:forEach var="yyInfo" items="${fwqInfo.yyInfo}">
															<tr>
																<td  onmouseover="showFwqYyLinerInfo('${yyInfo.id}','${fwqInfo.id}')" onmouseout="hideFwqYyLinerInfo('${yyInfo.id}','${fwqInfo.id}')">
																	<a href="javascript:void(0)" onclick="showFwqYyInfo('${yyInfo.id}','${yyInfo.ptlx}')">${yyInfo.yyname}</a>
																</td>
																<td>
																	<span>
																		<c:if test="${empty yyInfo.yyyxzt}"><font color="#F8D080">检测中</font></c:if>
																		<c:if test="${yyInfo.yyyxzt==0}"><font color="green">正常</font></c:if>
																		<c:if test="${yyInfo.yyyxzt==1}"><font color="red">异常</font></c:if>
																	</span>
																	&nbsp;&nbsp;&nbsp;
																	<a href="javascript:void(0)" onclick="showFwqYyLog('${fwqInfo.fwqip_ww}','${fwqInfo.dk}','${yyInfo.warname}')">日志</a>&nbsp;&nbsp;&nbsp;
																	<a href="javascript:void(0)" onclick="viewFwqYy('${fwqInfo.fwqip_ww}','${fwqInfo.dk}','${yyInfo.warname}')">访问</a>
																</td>
															</tr>
														</c:forEach>
													</c:if>
												</table>
											</div>
											<div id="fwqMore${fwqInfo.id}" class="fwqMoreitem" onmouseover="adjustFwqMoreHeight('${fwqInfo.id}','${fwqInfo.ptlx}')" title="${fwqInfo.fwqstatus_dicvalue}" style="display: none;">
												<table align="center">
													<tr>
														<td style="text-align: center;height: 20px;" colspan="2">
															<h3 style="line-height: 20px;"><span class="fwqname${fwqInfo.id}">（${fwqInfo.fwqname}）</span>服务器信息
																<span style="float: right;margin-right: 25px;" ><a href="javascript:void(0)" onclick="toFwqLayOutFromMore('${fwqInfo.id}','${fwqInfo.ptlx}')">返回</a></span>
															</h3>
														</td>
													</tr>
													<tr>
														<td colspan="2" style="height: 2px;"><hr/></td>
													</tr>
													<tr>
														<td>操作系统</td>
														<td><span id="fwqczxt${fwqInfo.id}">${fwqInfo.fwqczxt_dicvalue}</span></td>
													</tr>
													<tr>
														<td>所在机房</td>
														<td><span id="fwqszjf${fwqInfo.id}">${fwqInfo.szjf}</span></td>
													</tr>
													<tr>
														<td>对外地址</td>
														<td><span id="fwqfwqym${fwqInfo.id}">${fwqInfo.fwqym}</span></td>
													</tr>
													<tr>
														<td>线程数量</td>
														<td><span id="fwqthreadcount${fwqInfo.id}">${fwqInfo.threadcount}</span></td>
													</tr>
													<tr>
														<td>JVM可用堆内存</td>
														<td><span id="fwqjvmmemory${fwqInfo.id}">${fwqInfo.jvmmemory}</span></td>
													</tr>
													<tr>
														<td>JVM运行线程数</td>
														<td><span id="fwqjvmthreadcount${fwqInfo.id}">${fwqInfo.jvmthreadcount}</span></td>
													</tr>
													<tr>
														<td>信息更新时间</td>
														<td><span id="fwqupdatetime${fwqInfo.id}">${fwqInfo.updatetime}</span></td>
													</tr>
												</table>
											</div>
										</div>
									</c:forEach>
								</c:if>
							</div>
							<!-- 应用信息 -->
							<div id="tgyyContent" class="yyContent">
								<c:if test="${not empty tgyyAll}">
									<c:forEach items="${tgyyAll}" var="yyInfo">
										<script>
											//将初始化应用信息保存到当前的应用数组中
											currentTgYyAll.push('${yyInfo.id}');
										</script>
										<div id="yyOuter${yyInfo.id}">
											<a href="javascript:void(0)" style="color: black;" onclick="showYyMore('${yyInfo.id}','${yyInfo.ptlx}')">
												<div id="yy${yyInfo.id}" class="yyitem  btn-primary btn-lg btn-block">
													<table>
														<tr>
															<td>${yyInfo.yyname} / ${yyInfo.yyversion}</td>
														</tr>
													</table>
												</div>
											</a>
											<a href="javascript:void(0)" onmouseover="adjustYyMoreHeight('${yyInfo.id}','${yyInfo.ptlx}')" style="color: black;" onclick="showYyItem('${yyInfo.id}','${yyInfo.ptlx}')">
												<div id="yyMore${yyInfo.id}" class="yyMoreitem" style="display: none;">
													<table id="yyyxqk${yyInfo.id}">
														<c:if test="${not empty yyInfo.fwqInfoList}">
															<tr>
																<td style="height: 20px;" colspan="2">${yyInfo.yyname}</td>
															</tr>
															<tr>
																<td style="height: 2px;" colspan="2"><hr/></td>
															</tr>
															<c:forEach items="${yyInfo.fwqInfoList}" var="fwqInfo">
																<tr>
																	<td>${fwqInfo.fwqname}</td>
																	<td>
																		<span>
																			<c:if test="${empty fwqInfo.yyyxzt}"><font color="#F8D080">检测中</font></c:if>
																			<c:if test="${fwqInfo.yyyxzt==0}"><font color="green">正常</font></c:if>
																			<c:if test="${fwqInfo.yyyxzt==1}"><font color="red">异常</font></c:if>
																		</span>
																	</td>
																</tr>
															</c:forEach>
														</c:if>
													</table>
												</div>
											</a>
										</div>
									</c:forEach>
								</c:if>
							</div>
						</div>
					</div>
					<!-- Nginx监控  -->
					<div class="main_right">
					</div>
					<!-- 清除浮动使用 -->
					<div style="clear: both;"></div>
				</div>
			</div>
		</div>
		<!-- 点击运行日志弹出层 -->
	     <div id="logDiv" class="log_div box-shadow" style="display: none">
	     	<div class="close closeLog"><a style="color: white" href="javascript:void(0)">X</a></div>
	     </div>
	</body>
</html>
