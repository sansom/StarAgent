<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<link type="text/css" href="../css/mystyle.css" rel="stylesheet">

<script type="text/javascript" src="../yygl/js/yy.js"></script>

<script type="text/javascript">
		var Ctl_Deploy = "<%=SystemConstants.CONTROL_DEPLOY%>";	//部署
		var Ctl_Start = "<%=SystemConstants.CONTROL_START%>";	//启动
		var Ctl_Stop = "<%=SystemConstants.CONTROL_STOP%>";		//停止	
		var Ctl_Remove = "<%=SystemConstants.CONTROL_REMOVE%>";	//删除
		
		var Rls_Sfb = "<%=SystemConstants.RELEASE_SFB%>";		//试发布
		var Rls_Back = "<%=SystemConstants.RELEASE_BACK%>";		//版本回退 
		var Rls_Zsfb = "<%=SystemConstants.RELEASE_ZSFB%>";		//版本发布
		
		var Ptlx_Run = "<%=SystemConstants.PTLX_YUN%>";
		//在页面加载时候绑定弹出层的事件
		$().ready(function(){
			//绑定选择服务器弹出层
			if ($(".selectFwqClass").length>0) {
				$(".selectFwqClass").each(function(){
					$(".selectFwqClass").alertBox({ id:"#selectFwqDiv",borderWidth: "0px", level: "1000"});
				});
			}
			
			if ($("#appHandlerBtn").length>0) {
				$("#appHandlerBtn").alertBox({ id:"#appHandlerDiv",borderWidth: "0px", level: "1000"});
			}
			
		
		});
		
		//选择服务器的全选按钮
  		function selectAllCkb(obj) {
  			$(".ckb_fwq[id='"+obj.value+"']").each(function(){
  				$(this).attr("checked", obj.checked);
  			});
  		}
		
		//显示所有可用的服务器列表
		function selectKyFwq(yyid){
			var ptlx = $("#ptlx").val();
			$.ajax({
    		  type: "POST",
	   		  url: "yygetKyFwqByYyid.action",
	   		  data:{"yyid":yyid,"ptlx":ptlx},
	   		  dataType:"json",
			  success: function(jq){
			  	//给要提交的表单中的隐藏域设置应用id值
			  	$("#yyidfwq").val(yyid);
			  	//拼接服务器信息
			  	var html_content = "";
			  
			  	for(var i=0;i<jq.length;i++){

			  		html_content +=	"<tr>"+
		 							"<td width=\"35%\" style=\"border-top: 1px solid #1B5372;text-align: left;\">"+
		 								"<input   onclick=\"selectAllCkb(this)\" id=\""+jq[i].jqid+"\"  type=\"checkbox\" value=\""+jq[i].jqid+"\"/>&nbsp;&nbsp;"+
		 								jq[i].jqname +
		 							"</td>"+
		 							"<td style=\"border-top: 1px solid #1B5372\">"+
		 							"	<table>";
		 							var fwqlist = jq[i].fwqlist.split(",");
		 							for(var j=0;j < fwqlist.length;j++){
		 								var fwqBean = fwqlist[j].split("@");
		 								html_content += "<tr>"+
							  							"<td style=\"text-align: left;\">"+
							  							"<input id=\""+jq[i].jqid+"\" class=\"ckb_fwq\" name=\"fwqids\" type=\"checkbox\" value=\""+jq[i].jqid+"@"+fwqBean[0]+"\"/>&nbsp;&nbsp;"+
							  							fwqBean[1]+
							  							"</td>"+
							  						"</tr>";
		 							}
		 			html_content +=	"	</table>"+	
			  						"</td>"+
			  						"</tr>";
			  	
			  	}
		
			  	
			  	$("#fwqItemtable").html(html_content);
			  	
			  }
			});
		}
		
		//增加应用运行服务器
		function addYyyxfwq(){
			if($(".ckb_fwq:checked").length <= 0){
				alert("请选择集群");
				return false;
			}
			
			document.forms["selectFwqForm"].action = "yyaddYyyxfwq.action?yyBean.ptlx="+$("#ptlx").val();
			document.forms["selectFwqForm"].submit();
		}
		
		//获取有应用操作的服务器集合
		function getYyFwqsArr(yyid,fwqip){
			var fwqs = new Array();
			
			if(fwqip != null && fwqip != ""){
				//服务器IP不为空，把IP放到数组返回
				fwqs[0] = fwqip;
			}else {
				//服务器IP为空，获取应用下所有服务器
				var obj = $("input[name^='fwq_fwqip_"+yyid+"']");
				if(obj.length > 0){
					obj.each(function(i){  
						fwqs[i] = this.value;
	                }); 
				}
			}
			return fwqs;
		}
		
		/*************************************** 操作结果显示 ***************************************/
		
		//操作类型 title：标题   waiting：等待信息     result：操作结果   finish:完成操作
		var czlx_title = "title";
		var czlx_wait = "wait";
		var czlx_result = "result";
		var czlx_finish = "finish";
		
		//初始化div
		function initHandlerDiv(){
			$("#appHandler_warname").html("");
			$("#appHandler_dowhat").html("");
			$("#appHandlerTabs tbody").empty();
			
		}
		
		//显示App操作信息   
		function showMsg(czlx,yyid,fwqip,dowhat,dowhat_dicvalue,info){
			if(czlx == null || czlx == ""){
				return false;
			}
			if(czlx == czlx_title){
					
				showTitle(yyid,dowhat_dicvalue);
			}else if(czlx == czlx_wait){
				showWait(yyid,fwqip);
			}else if(czlx == czlx_result){
				showResult(fwqip,info);
			}else if(czlx == czlx_finish){
				showFinish();
			}

		}
		
		//显示App操作信息 __显示标题
		function showTitle(yyid,dowhat_dicvalue){
			//1.初始化层
			initHandlerDiv();
			
			//2.显示层
			if($("#appHandlerDiv").is(":hidden")){
				
				$("#appHandlerBtn").click();
			}
			
			//3.输出标题
			var appname = $("#yy_warname_"+yyid).html();
			$("#appHandler_warname").html(appname);
			$("#appHandler_dowhat").html(dowhat_dicvalue);
			
		}
		
		//显示App操作信息 __等待信息
		function showWait(yyid,fwqip){
			
			var tr = "";
			if(fwqip != ""){
				var fwqname = document.getElementById("fwq_fwqname_"+yyid+"_"+fwqip).value; 
				tr = "<tr>"+
						"<td style=\"text-align: right;\">"+
							"在服务器&nbsp;[&nbsp;"+fwqname+"&nbsp;]&nbsp;执行结果："+
						"</td>"+
						"<td style=\"text-align: left;\" id=\"appHandler_"+fwqip+"\">"+
							"【<font color=\"#FFB90F\" >请稍等，正在处理中...</font>】"+
						"</td>"+
					"</tr>";
			
			}else{
				tr = "<tr>"+
						
						"<td style=\"text-align: center;\" colspan=\"2\" id=\"appHandler_"+yyid+"\">"+
							"<font color=\"#FFB90F\" >请稍等，正在处理中...</font>"+
						"</td>"+
					"</tr>";
			}
			 
			$("#appHandlerTabs").append(tr);
			
		}
		
		
		//显示App操作信息 __处理结果
		function showResult(fwqip,info){
			document.getElementById("appHandler_"+fwqip).innerHTML = info;
			
		}
		
		//显示App操作信息 __完成操作
		function showFinish(yyid,dowhat){
			//$("#appHandler_Finish").show();
			
		}
		
		
		/*************************************** 应用操作 ***************************************/
		
		//应用运行控制器[兼容批量操作]   部署(deploy)  启动(start)  停止(stop)   删除(remove)
		function control(yyid,fwqip,dowhat,dowhat_dicvalue,isValidate){
			
			//1.检查
			if(dowhat == null || dowhat == ""){
				alert("无操作标识符！");
				return false;
			}
			var fwqs = this.getYyFwqsArr(yyid,fwqip);
			if(fwqs.length <= 0){
				alert("无可用服务器！");
				return false;
			}

			//2.循环操作
			var info = "";
			
			showMsg(czlx_title,yyid,"",dowhat,dowhat_dicvalue,info);
			for(var i = 0; i < fwqs.length; i++){
				
				var fwq = fwqs[i];
				//2.1校验
				showMsg(czlx_wait,yyid,fwq,dowhat,dowhat_dicvalue,info);
				
				if(isValidate == 'true'){
					info = validate_ctl(yyid,fwq,dowhat);
				}

				if(info != ""){
					showMsg(czlx_result,yyid,fwq,dowhat,dowhat_dicvalue,"【<font color=\"red\">"+info+"</font>】");
					continue;
				}
				//2.2执行操作
				info = todo(yyid,fwq,dowhat,dowhat_dicvalue)

			}
			showMsg(czlx_finish,yyid,fwq,dowhat,dowhat_dicvalue,info);

		}

		//执行操作（应用）
		function todo(yyid,fwqip,dowhat,dowhat_dicvalue){
			var re = "";
			//获取应用运行记录主键
			var yxjlid = document.getElementById("fwq_yxjlid_"+yyid+"_"+fwqip).value;
			
			//设置socket通讯IP  云应用使用内网IP通讯，其他应用使用外网IP通讯
			var socket_Ip = fwqip;
			if($("#ptlx").length <=0 || $("#ptlx").val() != Ptlx_Run){
				socket_Ip = document.getElementById("fwq_fwqwwip_"+yyid+"_"+fwqip).value;
			}
			
			$.ajax({
	    		  type: "POST",
		   		  url: "yycontroltodo.action",
		   		  dataType:"text",
		   		  async:true,
		   		  data:"yxjlid=" + yxjlid + "&yyid=" + yyid + "&fwqip=" + socket_Ip + "&dowhat=" + dowhat,
				  dataType:"json",
				  success: function(resultBean){
		  				if(resultBean.statusCode == "<%=SystemConstants.STATUSCODE_OK%>"){
		  					showMsg(czlx_result,yyid,fwqip,dowhat,dowhat_dicvalue,"【<font color=\"green\">操作成功！"+resultBean.arg1+"</font>】");	  				
		  				}else{
		  					showMsg(czlx_result,yyid,fwqip,dowhat,dowhat_dicvalue,"【<font color=\"red\">"+resultBean.msg+"</font>】");
		  				}
		  				
				  }
			});
			return re;
		}
		
		//完成操作
		function finishHandler(){
			var obj = $("td[id^='appHandler_']");
			var isFinish = true;
			if(obj.length > 0){
				obj.each(function(i){ 
					var tval = $(this).html();
					if(tval.indexOf("正在处理中") != -1){
						isFinish = false;
						return false;
					}
                }); 
			}
			if(!isFinish){
				alert("请等待程序处理完成！");
				return false;
			}
			
			formSubmit("");

		}
		
		//校验应用操作
		function validate_ctl(yyid,fwqip,dowhats){
			if(dowhats == Ctl_Deploy){
				return validate_deploy(yyid,fwqip);
			}if(dowhats == Ctl_Start){
				return validate_start(yyid,fwqip);
			}if(dowhats == Ctl_Stop){
				return validate_stop(yyid,fwqip);
			}if(dowhats == Ctl_Remove){
				return validate_remove(yyid,fwqip);
			}else{
				return "无效的操作符！\n";
			}
			return "";
		}
		
		//校验(部署)
		function validate_deploy(yyid,fwqip){
			//1.服务器是否在线
			if(!check_fwqIsOnline(yyid,fwqip)){
				return "服务器不可用！";
			}
			return "";
		}
		
		//校验(启动)
		function validate_start(yyid,fwqip){
			//1.服务器是否在线
			if(!check_fwqIsOnline(yyid,fwqip)){
				return "服务器不可用！";
				
			}
			//2.应用是否已启动
			if(check_YyIsOnline(yyid,fwqip)){
				return "应用已启动！";

			}
			return "";
		}
		
		//校验(停止)
		function validate_stop(yyid,fwqip){
			//1.服务器是否在线
			if(!check_fwqIsOnline(yyid,fwqip)){
				return "服务器不可用！";
			}
			//2.应用是否已停止
			if(check_YyIsOffline(yyid,fwqip)){
				return "应用未启动！";
			}
			return "";
		}
		
		
		//校验(删除)
		function validate_remove(yyid,fwqip){
			return "";
		}

		//验证_服务器是否在线
		function check_fwqIsOnline(yyid,fwqip){
			
			var fwqzt = document.getElementById("fwq_fwqstatus_"+yyid+"_"+fwqip).value;
			
			if(fwqzt != "<%=SystemConstants.FWQSTATUS_ZC%>"){
				return false;
			}
			return true;
		}
		
		//验证_应用是否启动
		function check_YyIsOnline(yyid,fwqip){
			var fwqzt = document.getElementById("fwq_yyzt_"+yyid+"_"+fwqip).value;
				
			if(fwqzt != "<%=SystemConstants.YYZT_YQD%>"){
				return false;
			}
			return true;
		}
		
		//验证_应用是否停止
		function check_YyIsOffline(yyid,fwqip){
			
			var fwqzt = document.getElementById("fwq_yyzt_"+yyid+"_"+fwqip).value;
			if(fwqzt == "<%=SystemConstants.YYZT_YQD%>"){
				return false;
			}
			return true;
		}

		//访问应用
		function openApp(fwqip,fwqip_ww,dk,yyid,warname){
			//1.服务器是否在线
			if(!check_fwqIsOnline(yyid,fwqip)){
				alert("服务器【"+fwqip+"】不可用！");
				return false;
			}
			
			var appname = warname.substring(0,warname.length-4);
			var url = "http://"+fwqip_ww+":"+dk+"/"+appname;
			window.open(url);
		}
		
		
		//查看应用日志
		function openAppLog(fwqip,fwqip_ww,dk,yyid,warname){
			//1.服务器是否在线
			if(!check_fwqIsOnline(yyid,fwqip)){
				alert("服务器【"+fwqip+"】不可用！");
				return false;
			}
			var appname = warname.substring(0,warname.length-4);
			//发送异步请求获取文件是否存在
			$.ajax({
				type: "POST",
		   		url: "yyisYyLogExist.action",
		   		data:"appname="+appname+"&ip="+fwqip,
		   		dataType:"json",
				success: function(resultBean){
					if(resultBean.statusCode == "<%=SystemConstants.STATUSCODE_OK%>"){
						//说明存在，直接访问
						var url = "http://"+fwqip_ww+":"+dk+"/fwzxutils/fileDeal.do?methodName=showAppLog&yyLogPath="+encodeURI(resultBean.arg1);
						window.open(url);
	  				}else{
	  					alert(resultBean.msg);
	  					return false;
	  				}
				}
			});
		}
		
		//修改应用
		function yyEdit(id){
			formSubmit("yyedit.action?yyBean.id="+id);
		}
		//删除应用
		function yyRemove(id){
			if(isHasFw(id)){
				return false;
			}
			
			//是否运行中
			var isYxz = false;
			var obj = $("input[name^='fwq_yyzt_"+id+"']");
			if(obj.length > 0){
				obj.each(function(i){  
					if(this.value == "<%=SystemConstants.YYZT_YQD%>"){
						isYxz = true;
						return false;
					}
                }); 
			}
			
			if(isYxz){
				alert("该应用正在运行中，请停止所有服务器再删除！");
				return false;
			}
			
			if(confirm("您确认要删除该应用吗？")){
				formSubmit("yyremove.action?yyBean.id="+id);
			}

		}
		
		//检查应用是否
		function isHasFw(yyid){
			var boo = false;
			$.ajax({
	    		  type: "POST",
		   		  url: "yyisHasFw.action",
		   		  dataType:"text",
		   		  async:false,
		   		  data:"yyid="+yyid,
				  dataType:"text",
				  success: function(msg){
		  				if(msg == "fwfailed"){
		  					alert("该应用下存在有效服务，请先删除服务再删除应用！");
		  					boo =  true;
		  				}else if(msg == "sjjkfailed"){
		  					alert("该应用下存在有效数据接口，请先删除接口再删除应用！");
		  					boo =  true;
		  				}
				  }
			});
			return boo;
		}
	  	
		
		//表单提交
	    function formSubmit(action){
	     	var fields =new Array("ptlx");
			getparamter(fields, "queryparamter", "queryparamtername");    
			if(action != null && action != ""){
				document.forms["yyForm"].action = action; 
				document.forms["yyForm"].submit();
			}else{
				window.location = document.forms["yyForm"].action;
			}
	    }
	    
	    /*************************************** 试发布开始 ***************************************/
		//版本试发布
	    function versionRelease(yyid,dowhat,dowhat_dicvalue){
	    
	    	//验证
			if(!validate_release(yyid,dowhat)){
				return false;
			}
			
			//显示操作信息头
			showMsg(czlx_title,yyid,"",dowhat,dowhat_dicvalue,"");
			//显示等待信息
			showMsg(czlx_wait,yyid,"",dowhat,dowhat_dicvalue,"");
			
			$.ajax({
				  type: "POST",
		 		  url: "yycontrol!release.action",
		 		  dataType:"text",
		 		  async:true,
		 		  data:"&yyid=" + yyid + "&dowhat=" + dowhat + "&isAutoRun=<%=SystemConstants.isAutoRun_false%>",
				  dataType:"json",
				  success: function(resultBean){
						if(resultBean.statusCode == "<%=SystemConstants.STATUSCODE_OK%>"){
							showMsg(czlx_result,"",yyid,dowhat,dowhat_dicvalue,"<font color=\"green\">"+resultBean.msg+"</font>");	  				
						}else{
							showMsg(czlx_result,"",yyid,dowhat,dowhat_dicvalue,"<font color=\"red\">"+resultBean.msg+"</font>");
						}
				  }
			});
		}
		
		//试发布校验
		function validate_release(yyid,dowhats){
			if(dowhats == Rls_Sfb){
				return validate_sfb(yyid);
			}if(dowhats == Rls_Back){
				return validate_back(yyid);
			}if(dowhats == Rls_Zsfb){
				return validate_zsfb(yyid);
			}else{
				alert("无效的操作符！");
				return false;
			}
			return false;
		}
		
		//版本试发布验证
		function validate_sfb(yyid){
			var count = 0;
			var obj = $("input[name^='fwq_fwqstatus_"+yyid+"']");
			if(obj.length > 0){
				obj.each(function(i){  
					if(this.value == "<%=SystemConstants.FWQSTATUS_ZC%>"){
						count++;
					}
                }); 
			}
			
			if(count < 1){
				alert("无可用服务器！");
				return false;
			}
			
			var sfbversion = $("#fwq_sfbversion_"+yyid).val();
			if(!confirm("您确认执行版本【"+sfbversion+"】试发布吗？")){
				return false;
			}
			return true;
			
		}
		
		//版本回退验证
		function validate_back(yyid){
			var b = false;
			var obj = $("input[name^='fwq_fbzt_"+yyid+"']");
			if(obj.length > 0){
				obj.each(function(i){  
					if(this.value == "<%=SystemConstants.FBZT_SFB%>"){
						b = true;
						return false;
					}
                }); 
			}
			
			if(!b){
				alert("该应用无试发布服务器，无需回退！");
				return false;
			}
			
			var sfbversion = $("#fwq_sfbversion_"+yyid).val();
			var version = $("#fwq_version_"+yyid).val();
			if(!confirm("您确认要将试发布版本【"+sfbversion+"】恢复到线上版本【"+version+"】吗？")){
				return false;
			}
			return true;
			
		}
		
		//版本正式发布验证
		function validate_zsfb(yyid){

			var obj = $("#fwq_sfbversion_"+yyid).val();
			if(obj == null || obj == ""){
				alert("该应用无试发布版本！");
				return false;
			}
			if(!confirm("您确认要发布【"+obj+"】版本吗？")){
				return false;
			}
			return true;
			
		}
		 /*************************************** 试发布结束 ***************************************/
</script>

<div class="bmain-round" style="border:1px solid #111;">
		<div class="main-rbox">
	<div class="boxtop rbox-pos">
	  <div class="htx"><b>当前位置：</b> &gt <span>应用管理</span></div> 
	</div><!--boxtop pos end--> 
     <table class="mb-tm"  border="0" cellspacing="0" cellpadding="0"> 
     	<tr>
     		<td>
     			<div class="min400"> 
               		<div class="boxb indboxt">
               			<h3>
               				<span class="qylie" style="width:50%">应用列表</span>
               				
               			</h3>
               			
               			<div class="boxb_main boxb_main100" >
               				<!-- 定义状态常量 -->
							<c:set value="<%=SystemConstants.FWQSTATUS_ZC %>" var="fwqstatus_zc" ></c:set>
							<c:set value="<%=SystemConstants.YYZT_WQD %>" var="yyzt_wqd" ></c:set>
							<c:set value="<%=SystemConstants.YYZT_YQD %>" var="yyzt_yqd" ></c:set>
							<c:set value="<%=SystemConstants.YYYYZT_ZC %>" var="yyyxzt_zc" ></c:set>
							<c:set value="<%=SystemConstants.YYYYZT_YC %>" var="yyyxzt_yc" ></c:set>
							
							<c:set value="<%=SystemConstants.CONTROL_DEPLOY%>" var="control_deploy" ></c:set>
							<c:set value="<%=SystemConstants.CONTROL_START %>" var="control_start" ></c:set>
							<c:set value="<%=SystemConstants.CONTROL_STOP %>" var="control_stop" ></c:set>
							<c:set value="<%=SystemConstants.CONTROL_REMOVE %>" var="control_remove" ></c:set>
							
							<c:set value="<%=SystemConstants.RELEASE_SFB %>" var="release_sfb" ></c:set>
							<c:set value="<%=SystemConstants.RELEASE_BACK %>" var="release_back" ></c:set>
							<c:set value="<%=SystemConstants.RELEASE_ZSFB %>" var="release_zsfb" ></c:set>
							
							<c:set value="<%=SystemConstants.FBZT_SFB %>" var="fbzt_sfb" ></c:set>
							<c:set value="<%=SystemConstants.FBZT_FB %>" var="fbzt_fb" ></c:set>
						
							
               				<table class="mb-table show-td"  border="0" cellspacing="0" cellpadding="0">
               					
								<colgroup style="width: 7%"/>
								<colgroup style="width: 5%"/>
								<colgroup style="width: 8%"/>
								<colgroup style="width: 5%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 30%"/>
							
								<colgroup style="width: 10%"/>
								<colgroup style="width: 5%"/>
                				<tr class="mb-ta-head">
				                    <th >应用名称</th>
				                    <th >war包</th>
				                    <th >正式版本&nbsp;&&nbsp;试发布版本</th>
				                    <th >环境选择</th>
				                    <th >版本发布控制</th>
				                    <th >应用控制台</th>
				                    <th >一键操作</th>
				                   
				                    <th >操作</th>
					            </tr>
					            <s:if test="#request.list != null">
								<s:iterator id="item" value="#request.list" status="idx">
									<tr class="mb-ta-head" >
										
										<td id="yy_warname_${item.id}">
											${item.yyname}
										</td>
									
										<td >
											${item.warname}
											
										</td>
										<td >
										<c:choose>
												<c:when test="${empty item.yyversion}">
												--
												</c:when>
												<c:otherwise>
													<font style="font-weight: bold;">${item.yyversion}</font>
												</c:otherwise>
											</c:choose>
											&nbsp;&&nbsp;
											<c:choose>
												<c:when test="${empty item.yyversion_sfb}">
												--
												</c:when>
												<c:otherwise>
													<font color="#FF9900" style="font-weight: bold;">${item.yyversion_sfb}</font>
												</c:otherwise>
											</c:choose>
											<input type="hidden" id="fwq_version_${item.id}" name="fwq_version_${item.id}" value="${item.yyversion}" />
											<input type="hidden" id="fwq_sfbversion_${item.id}" name="fwq_sfbversion_${item.id}" value="${item.yyversion_sfb}" />
											
										</td>
										<td >
											<a class="selectFwqClass" href="javascript:void(0)" onclick="selectKyFwq('${item.id}')">选择集群</a>
										</td>
											
										<td >
											【<a href="javascript:void(0)" onclick="versionRelease('${item.id}','${release_sfb }','试发布')">试发布</a>&nbsp;&nbsp;↑
											<a href="javascript:void(0)" onclick="versionRelease('${item.id}','${release_back }','环境恢复')" title="还原到线上版本[${item.yyversion}]">回退</a>&nbsp;&nbsp;↑
											<a href="javascript:void(0)" onclick="versionRelease('${item.id}','${release_zsfb }','正式发布')">正式发布</a>】
										</td>
										<td style="border-bottom: 0px solid #707070;">
										
											
											<c:if test="${not empty item.fwqyylist}">
												<!-- fwqArr：服务器应用数组 -->
												<c:set value="${fn:split(item.fwqyylist,',') }" var="fwqArr"></c:set>
												<table style="width: 100%;" >
													<colgroup width="10%"></colgroup>
													
													<colgroup width="28%"></colgroup>
													<colgroup width="10%"></colgroup>
													<colgroup width="20%"></colgroup>	
													<colgroup width="20%"></colgroup>
													<colgroup width="15%"></colgroup>
													
													<c:forEach var="fwqArr" items="${fwqArr}">
													<!-- fwq：服务器对象 【fwqname,fwqstatus,fwqip,dk,yyyxzt,yyzt,yyyxjlid,yyyxztinfo,fwqip_ww,jqname,version,fbzt】-->
													<!-- fwq：服务器对象 【服务器名称[0],服务器状态[1],服务器IP[2],端口[3],应用运行状态[4],应用状态[5],应用运行ID[6],应用运行描述[7],服务器外网IP[8],集群名称[9],应用版本号[10],发布状态[11]】-->
													<c:set value="${fn:split(fwqArr,'@') }" var="fwqbean"></c:set>
														<tr>
																
															<c:choose>
																<c:when test="${fwqbean[1] == fwqstatus_zc}">
																	
																 	<td>
																 	
																 		<c:choose>
																 			<c:when test="${fwqbean[11] == fbzt_sfb}">
																 				试发布
																 			</c:when>
																 			<c:when test="${fwqbean[11] == fbzt_fb}">
																 				正式
																 			</c:when>
																 			<c:otherwise>
																 				${fwqbean[11]}
																 			</c:otherwise>
																 		</c:choose>
																 		
																 	</td>
																	<td  style="text-align: left;padding-left: 15px">
																		<c:if test="${fwqbean[9] != '' && fwqbean[9] != '-'}">
																			<font title="集群">【${fwqbean[9]}】</font>
																		</c:if>
																		<font  title="服务器正常">
																			${fwqbean[0]}
																		</font>
																	</td>
																	<td>
																		<c:choose>
																	 		<c:when test="${not empty item.yyversion && item.yyversion != fwqbean[10]}">
																	 			<font color="#FFC85A">${fwqbean[10]}</font>
																	 		</c:when>
																	 		<c:otherwise>
																	 			<font color="green">${fwqbean[10]}</font>
																 			</c:otherwise>
																 		</c:choose>
																 	</td>
																	<td >
																		
																		<c:choose>
																		
																			<c:when test="${fwqbean[5] == yyzt_yqd}">
																				<c:choose>
																					<c:when test="${fwqbean[4] == yyyxzt_zc}">
																						 <font style="color: green">应用正常</font>
																					</c:when>
																					<c:when test="${fwqbean[4] == yyyxzt_yc}">
																						 <font style="color: red" >应用异常<br/>${fwqbean[7]}</font>
																					</c:when>
																					<c:otherwise>
																						 <font style="color: #FFB90F">检测中</font>
																					</c:otherwise>
																				</c:choose>
																				
																			</c:when>
																			<c:otherwise>
																				 <font>未启动</font>
																			</c:otherwise>
																		</c:choose>
																		<!-------------------------------- 隐藏域存储应用运行状态信息 -------------------------------->
																		<!-- 服务器名称 -->
																		<input type="hidden" id="fwq_fwqname_${item.id}_${fwqbean[2]}" name="fwq_fwqname_${item.id}_${fwqbean[2]}" value="${fwqbean[0]}" />
																		<!-- 服务器状态   0:正常 1:异常 -->
																		<input type="hidden" id="fwq_fwqstatus_${item.id}_${fwqbean[2]}" name="fwq_fwqstatus_${item.id}_${fwqbean[2]}" value="${fwqbean[1]}" />
																		<!-- 应用运行状态   0:正常 1:异常 -->
																		<input type="hidden" id="fwq_yyyxzt_${item.id}_${fwqbean[2]}" name="fwq_yyyxzt_${item.id}_${fwqbean[2]}" value="${fwqbean[4]}" />
																		<!-- 应用状态   0:已启动 1:未启动 -->
																		<input type="hidden" id="fwq_yyzt_${item.id}_${fwqbean[2]}" name="fwq_yyzt_${item.id}_${fwqbean[2]}" value="${fwqbean[5]}" />
																		
																		<!-- 应用记录主键 -->
																		<input type="hidden" id="fwq_yxjlid_${item.id}_${fwqbean[2]}" name="fwq_yxjlid_${item.id}_${fwqbean[2]}" value="${fwqbean[6]}" />
																		<!-- 应用运行服务器IP-->
																		<input type="hidden" id="fwq_fwqip_${item.id}_${fwqbean[2]}" name="fwq_fwqip_${item.id}_${fwqbean[2]}" value="${fwqbean[2]}" />
																		
																		<!-- 应用发布状态-->
																		<input type="hidden" id="fwq_fbzt_${item.id}_${fwqbean[2]}" name="fwq_fbzt_${item.id}_${fwqbean[2]}" value="${fwqbean[11]}" />
																	</td>
																	
																	<td>
																
																		<!-- <a href="javascript:void(0)" onclick="control('${item.id}','${fwqbean[2]}','${control_deploy}','部署','true')">部署</a>&nbsp;&nbsp;&nbsp; -->
																		<a href="javascript:void(0)" onclick="control('${item.id}','${fwqbean[2]}','${control_start}','启动','true')">启动</a>&nbsp;&nbsp;&nbsp;
																		<a href="javascript:void(0)" onclick="control('${item.id}','${fwqbean[2]}','${control_stop}','停止','true')">停止</a>&nbsp;&nbsp;&nbsp;
																	<a href="javascript:void(0)" onclick="if(confirm('您确定要删除该服务器上的应用吗？'))control('${item.id}','${fwqbean[2]}','${control_remove}','删除','true');">删除</a>
																	</td>
																	<td>
																		<a href="javascript:void(0)" onclick="openApp('${fwqbean[2]}','${fwqbean[8]}','${fwqbean[3]}','${item.id}','${item.warname}')">访问</a>&nbsp;
																		<a href="javascript:void(0)" onclick="openAppLog('${fwqbean[2]}','${fwqbean[8]}','${fwqbean[3]}','${item.id}','${item.warname}')">日志</a>&nbsp;
							
																	</td>
																</c:when>
																<c:otherwise>
																	<td>
																	
																 		<c:choose>
																 			<c:when test="${fwqbean[11] == fbzt_sfb}">
																 				试发布
																 			</c:when>
																 			<c:when test="${fwqbean[11] == fbzt_fb}">
																 				正式
																 			</c:when>
																 			<c:otherwise>
																 				${fwqbean[11]}
																 			</c:otherwise>
																 		</c:choose>
																 		
																 	</td>
																	<td style="text-align: left;padding-left: 15px">
																		<c:if test="${fwqbean[9] != ''  && fwqbean[9] != '-'}">
																			<font title="集群">【${fwqbean[9]}】</font>
																		</c:if>
																		<font  title="服务器不可用">
																	 		${fwqbean[0]}
																		</font>
																	</td>
																	<td></td>
																 	<td>
																 		<font style="color: red">服务器不可用</font>
																 	</td>
																 	<td colspan="2">
																 		
																 	</td>
																</c:otherwise>
															</c:choose>
														</tr>
													</c:forEach>
												</table>
											</c:if>
										
										</td>
										<td  align="center" >
											
											【<!-- <a href="javascript:void(0)" onclick="control('${item.id}','','${control_deploy}','部署','true')">部署</a>&nbsp;&nbsp;↑ -->
											<a href="javascript:void(0)" onclick="control('${item.id}','','${control_start}','启动','true')">启动</a>&nbsp;&nbsp;↑
											<a href="javascript:void(0)" onclick="control('${item.id}','','${control_stop}','停止','true')">停止</a>&nbsp;&nbsp;↑
											<a href="javascript:void(0)" onclick="if(confirm('您确定要删除所有服务器吗？'))control('${item.id}','','${control_remove}','删除','true')">删除</a>
											】
											
										</td>
											
										<td  align="center" >
											
											
											<a href="javascript:void(0)" onclick="yyEdit('${item.id}')">编辑</a>&nbsp;&nbsp;
											<a href="javascript:void(0)" onclick="yyRemove('${item.id}')">注销</a>
										</td>
									</tr>
									
								</s:iterator>
							</s:if>
               				</table>
               		 		<div class="j10"></div>
               				 <%@ include file="../include/page.inc"%>
               				<div class="j10"></div>
               			</div> 
               		</div>
               		<div class="j10"></div>
					<div id="printId" align="center">
						<input type="button" class="tianjiaanniu" value="添加应用"
							onclick="yyEdit('')"/>
				
					</div>
					<div class="j10"></div>
     			</div>
     		</td>
     	</tr>
     </table> 
     </div>
   </div>
   
   <!-- 选择服务器弹出层 -->
   <div id="selectFwqDiv" class="select_main" style="display: none;width: 400px;text-align: center;border:  #cfcfcf 10px solid" >
   		<h3 class="hea">
   		
   		&nbsp;
   		【一个应用只能选择一个集群】
   		</h3>
   		<form id="selectFwqForm" name="selectFwqForm" action="yyaddYyyxfwq.action" method="post">
   			<input id="yyidfwq" type="hidden" name="yyidfwq" value="" />
 	   		<table id="fwqItemtable" style="width: 100%;text-align: center;padding-left: 50px;border: 0px" >
	   		<tr style="display: none">
			  <td width="35%" style="border-top: 1px solid #1B5372">
			  
			  <input  class="ckb_fwq" name="jqids" type="checkbox" value="jq[i].jqid"/>&nbsp;&nbsp;
			  		集群1 >>>
			  </td>
			  <td style="border-top: 1px solid #1B5372">
			  <table>
			  		<tr>
			  		<td style="text-align: left;">
			  			<input id="ckb" class="ckb_fwq" name="fwqids" type="checkbox" value="jq[i].fwqlist[i].fwqid"/>&nbsp;&nbsp;
			  		服务器111111
			  		</td>
			  		</tr>
			  		<tr>
			  		<td style="text-align: left;">
			  			<input id="ckb" class="ckb_fwq" name="fwqids" type="checkbox" value="fwqlist[i].id"/>&nbsp;&nbsp;
			  		服务器2 1111111111111111
			  		</td>
			  		</tr>
			  		</table>
			  </td>
			</tr>
	   		</table>
 	  		<div class="select_fwq_item" style="padding-top:15px;" >
 	  			<a href="javascript:void(0)" class="monibutton" onclick="addYyyxfwq()">确定</a>&nbsp;
 	  			<a href="javascript:void(0)" class="monibutton close">取消</a>
 	  		</div>
 	  		
  		</form>
  	</div>
  	
	<!-- 应用操作结果显示层 -->
	<input type="button" style="display: none" id="appHandlerBtn" />
    <div id="appHandlerDiv" class="select_main" style="display: none;width: 500px;border: 10px solid #CFCFCF;" >
		<div style="padding-top: 15px;border-bottom:1px solid #75BDF2 ;width: 100%;text-align: center;height: 30px;font-size: 12px;">【<font id="appHandler_warname"></font>】执行 &nbsp;<font id="appHandler_dowhat" style="color: #FF9900;font-weight: bold;"></font>&nbsp;操作</div>
   		<table id="appHandlerTabs" style="width: 100%;line-height: 35px;height: 35px;padding-top: 5px;padding-bottom: 5px" >
   			<colgroup width="50%"></colgroup>
   			<colgroup width="40%"></colgroup>
  			<tr>
  				<td style="text-align: right;">
  					在服务器&nbsp;[192.168.20.21]&nbsp;操作结果：
  				</td>
  				<td style="text-align: left;">
  					【操作成功!】
  				</td>
  			</tr>
  			<tr>
  				<td style="text-align: center;" colspan="2">
  					试发布服务器已准备好，是否发布应用？
  					&nbsp;&nbsp;&nbsp;&nbsp;
  					<a  href="javascript:void(0)"  style="color:#009933;background-color:#DCDCDC;padding:5px;font-weight: bold;"  onclick="finishHandler(this)"> 是,立即发布   </a>
  					&nbsp;&nbsp;&nbsp;&nbsp;
  					<a  href="javascript:void(0)" style="color:#FF9900;background-color:#DCDCDC;padding: 5px;font-weight: bold;"  onclick="finishHandler(this)"> 不,稍后处理   </a>
  				</td>
  			</tr>
  		</table>
  		<table width="100%" style="text-align: center;border-top:1px solid #75BDF2;line-height: 40px;height:40px;" >
  			<tr>
  				<td >
  				<a id="appHandler_Finish" href="javascript:void(0)" class="monibutton" onclick="finishHandler(this)">确&nbsp;认</a>&nbsp;
  				</td>
  			</tr>
  		</table>

  	</div>
  