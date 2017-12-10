var currentFwqAll = new Array();	//当前所有的服务器信息（用于更新时候对比），初始化时候push进去，方便存储   只存储id

$(function(){
	//绑定查看日志的遮罩层
	if($(".logShowClass").length>0){
    	$(".logShowClass").each(function(){
			$(this).alertBox({ id:"#logDiv",borderWidth: "0px", level: "1000"});
		});
	}
	
	var wid = client().width;
	var hei = client().height;
	
	if (hei <= 400) {
		hei = 400;
	}
	
	$("#logDiv").height(hei*0.6);
	$("#logDiv").width(wid*0.6);
	
//	var screenHeight = $(window).height();
//	$("#logDiv").height(screenHeight * 0.6);
	//执行定时监控
	init();
});

//兼容获取可视区域的宽高
function client(){
	 if(window.innerHeight !== undefined){
		 return {
       "width": window.innerWidth,
           "height": window.innerHeight
        }
    }else if(document.compatMode === "CSS1Compat"){
            return {
           	 "width": document.documentElement.clientWidth,
           	 "height": document.documentElement.clientHeight
            }
   }else{
     return {
		"width": document.body.clientWidth,
		"height": document.body.clientHeight
     }
   }
}

//点击查看应用信息
function showYyInfo(fwqid, ptlx) {
	if ($("#fwq"+fwqid).attr("title") == "异常") {
		return;
	}
	//显示应用信息
	$("#fwqOuter"+fwqid).css("height","200px");
	$("#fwq"+fwqid).hide();
	$("#fwqYyInfo"+fwqid).show();
}
//点击查看更多信息
function showMoreInfo(fwqid, ptlx) {
	if ($("#fwq"+fwqid).attr("title") == "异常") {
		return;
	}
	//显示应用信息
	$("#fwqOuter"+fwqid).css("height","200px");
	$("#fwq"+fwqid).hide();
	$("#fwqMore"+fwqid).show();
}
//从应用界面返回服务器信息界面
function toFwqLayOut(fwqid, ptlx) {
	$("#fwqOuter"+fwqid).css("height","160px");
	$("#fwqYyInfo"+fwqid).hide();
	$("#fwq"+fwqid).show();
}
//从服务器详细信息界面返回服务器信息界面
function toFwqLayOutFromMore(fwqid, ptlx) {
	$("#fwqOuter"+fwqid).css("height","160px");
	$("#fwqMore"+fwqid).hide();
	$("#fwq"+fwqid).show();
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
	//var url = "http://"+fwqip+":"+dk+"/fwzxutils/fileDeal.do?methodName=showTomcatLog&logName="+logName;
	//window.open(url);
	window.open("../fwqgl/fwqtoshowLogOnline.action?logname="+logName+"&fwqip="+fwqip);
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

	setTimeout(function(){
		setInterval(function(){
			updateMonitorAllInfo();
		},60*1000);
	},5*1000);
}

//更新首页监控界面信息
function updateMonitorAllInfo() {
	updateMonitorInfo(currentPtlx);
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
			
			var errInfo = data["errInfo"];
			if (errInfo && errInfo != null && errInfo != "") {
				var yunInfo = errInfo["errInfo01"];
				var tgInfo = errInfo["errInfo02"];
				
				var yunErrNum = 0;
				var yunInfoNum = 0;
				var tgErrNum = 0;
				var tgInfoNum = 0;
				
				if (yunInfo) {
					yunErrNum = yunInfo["errNum"];
					yunInfoNum = yunInfo["infoNum"];
				}
				if (tgInfo) {
					tgErrNum = tgInfo["errNum"];
					tgInfoNum = tgInfo["infoNum"];
				}
				
				$('#yunErrNum', window.parent.document).html(yunErrNum);
				$('#yunInfoNum', window.parent.document).html(yunInfoNum);
				$('#tgErrNum', window.parent.document).html(tgErrNum);
				$('#tgInfoNum', window.parent.document).html(tgInfoNum);
				
			}
			
			var fwqAll = data["fwqAll"];
			if (fwqAll && fwqAll != null && fwqAll != "") {
				updateFwqInfoByPtlx(fwqAll,ptlx);
			}
		}
	});
}

//抽取出来的更新服务器信息的方法
function updateFwqInfoByPtlx(fwqAll,ptlx) {
	
	//操作之前，将新信息进行备份，之后要用
	var tempFwqAll = fwqAll.slice();

	//遍历服务器，将要添加的、修改的、删除的存到数组中
	var fwqToAddArr = new Array();
	var fwqToUpdateArr = new Array();
	var fwqToRemoveArr = new Array();
	
	//遍历服务器，将要修改的和要删除的进行整理
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
	currentFwqAll.length = 0;
	for(var i in tempFwqAll) {
		currentFwqAll.push(tempFwqAll[i]["id"]);
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
			fwqYyInfoDivHtml += '<tr><td> ' 
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
					+ '></div>');//创建一个外面的div
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
	var fwqYyInfoDivHtml = '<div id="fwqYyInfo'+fwqInfo["id"]+'" class="fwqYyInfoitem" title="'+status+'" style="display: none;">';
	fwqYyInfoDivHtml += createFwqYyInfoHtml(fwqInfo)+'</div>';
	var fwqYyInfoItemDiv = $(fwqYyInfoDivHtml);
	//将该div放进外面大的div中
	$(fwqOuterDiv).append(fwqYyInfoItemDiv);
	
	//创建详细信息的div
	var fwqMoreDivHtml = '<div id="fwqMore'+fwqInfo["id"]+'" class="fwqMoreitem" title="'+status+'" style="display: none;">'
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
	$("#fwqContent").append(fwqOuterDiv);
	//将服务器的日志信息绑定弹出层
	$("#logShow"+fwqInfo["id"]).alertBox({ id:"#logDiv",borderWidth: "0px", level: "1000"});
}
//异步移除一个服务器监控信息
function asyncRemoveFwq(fwqid, ptlx) {
	//直接将outer大的移除掉即可
	$("#fwqOuter"+fwqid).remove();
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