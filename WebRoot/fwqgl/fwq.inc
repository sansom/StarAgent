<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="../include/include.jsp" %>
<%@ include file="../include/log.inc" %>

<script type="text/javascript">
	
	var selFwqname;
	
	/*
	* 模板信息
	*/
	//内容item模板
	var content_item_templete = '<div class="result-show-content-item">' +
					'<span class="result-show-content-title">&item-title&：</span>' +
					'<span class="result-show-content-status" id="&item-status-id&">&item-status&</span>' +
					'</div>';
	
	//撤销服务器前操作
	function fwqcx(id, name, fwqip, fwqip_ww) {
	
		showBindLayer("resultShow", "", "300px");
	
		var fip = fwqip;
		var ptlx = $("#ptlx").val();
		if (ptlx == '<%= SystemConstants.PTLX_TG %>') {
			fip = fwqip_ww;
		}
		$("#show-fwq").html(name);
		$("#result-show-fwqid").val(id);
		$("#result-show-fwqip").val(fip);
	}
	
	//真正撤销操作
	function realcxfwq() {
		if ($("#result-show-okBtn").hasClass("close")) {
			//如果包含有这个元素说明已经不用再执行同样的操作了
			location.reload(true);
			return;
		}
		$("#result-show-content").html("");
		//1、校验Nginx
		//2、执行删除的操作
		$("#result-show-content").append(
			$(
				content_item_templete
					.replace("&item-title&", "nginx更新环境检测")
					.replace("&item-status-id&", "nginx-update-status")
					.replace("&item-status&", "检测中...")
			)
		);
		var fwqid = $("#result-show-fwqid").val();
		var fwqip = $("#result-show-fwqip").val();
		$.ajax({
			type : "post",
			url : "../nginx/nginxcheckForUpdate.action",
			data : "fwqid="+fwqid,
			dataType : "json",
			success : function(data) {
				$("#result-show-backBtn").bind("click", function(){
					var ptlx = $("#ptlx").val();
					tolist(ptlx);
				});
				if (data.statusCode == '0000') {
					//检验通过
					$("#nginx-update-status").css("color", "green");
					$("#nginx-update-status").html(data.msg);
					//开始执行删除的操作
					$.ajax({
						type : "post",
						url : "fwqremove.action",
						data : "fwqBean.id=" + fwqid,
						dataType : "json",
						success : function(data) {
							var warnames;
							for(var i = 0;i<data.length;i++) {
								if (i == data.length-2) {
									//存储的是要停止的应用的war名称
									warnames = data[i].msg;
									continue;
								}
								$("#result-show-content").append(
									$(
										content_item_templete
											.replace("&item-title&", data[i].arg1)
											.replace("&item-status-id&", "item-status-id"+i)
											.replace("&item-status&", data[i].msg)
									)
								);
								if (data[i].statusCode == '0000') {
									$("#item-status-id"+i).css("color", "green");
								}else {
									$("#item-status-id"+i).css("color", "red");
								}
								if (i == data.length-1) {
									//最后一个，是最终的结果
									if (data[i].statusCode == '0000') {
										//操作成功就取消确定按钮的事件
										var obj = $("#result-show-okBtn");
										$("#result-show-okBtn").addClass("close");
									}
								}
							}
							//全部执行完成之后，异步停止服务器上面的应用
							$.ajax({
								type : "post",
								url : "fwqstopYyOnFwq.action",
								data : "fwqip=" + fwqip + "&warnames=" + warnames,
								success : function(data) {
									console.log(data);
								},
								error : function(a,b) {
									console.log(a);
									console.log(b);
								}
							});
						},
						error : function(a,b){
							alert(b);
						}
					});
				}else {
					//检验不通过
					$("#nginx-update-status").css("color", "red");
					$("#nginx-update-status").html(data.msg);
					$("#result-show-content").append(
						$(
							content_item_templete
								.replace("&item-title&", "操作结果")
								.replace("&item-status-id&", "item-status-id")
								.replace("&item-status&", "<font color='red'>失败！</font>")
						)
					);
				}
			}
		});
		
	}

	//跳转到list界面
	function tolist(ptlx) {
		if (ptlx == '<%= SystemConstants.PTLX_TG %>') {
			window.location.href = "../fwqgl/tgfwqlist.action";
		}else {
			window.location.href = "../fwqgl/fwqlist.action";
		}
	}
	
	//服务器编辑
	function fwqEdit(id){
		formSubmit("fwqedit.action?fwqBean.id="+id);
	}
	
	//表单提交
    function formSubmit(action){
     	var fields =new Array("ptlx");
		getparamter(fields, "queryparamter", "queryparamtername");    
		if(action != null && action != ""){
			document.forms["fwqForm"].action = action; 
		}
      	document.forms["fwqForm"].submit();
    }
  
</script>

<c:set value="<%= SystemConstants.PTLX_YUN %>" var="ptlx_yun" />
<c:set value="<%= SystemConstants.PTLX_TG %>" var="ptlx_tg" />

<section id="contentMain">
	<h2 class="infoTitle">服务器列表</h2>
	<a type="button" class="addSomeBtn addBtnFora" style="margin-bottom: 20px;cursor: pointer;" onclick="fwqEdit('')">接入服务器</a>
	<div>
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
          		<colgroup style="width: 5%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 7%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 15%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
			<tbody>
				<tr class="title">
					<th>序号</th>
					<th>所在机房</th>
					<th>所属集群</th>
					<th>服务器名称</th>
					<th>IP地址</th>
					<th>Tomcat端口</th>
					<th>操作系统</th>
					<th>服务器状态</th>
					<th>联系人信息</th>
					<th>操作</th>
				</tr>
				<c:if test="${empty list}">
					<tr><td colspan="10" align="center" style="text-align: center;" class="noData">暂时没有服务器信息</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td>${item.szjf}</td>
							<td>
								<c:choose>
									<c:when test="${empty item.jqname}">
									</c:when>
									<c:when test="${fn:contains(item.jqname,',')}">
										<table class="api_table" border="0" cellpadding="0" cellspacing="0">
											<c:set value="${ fn:split(item.jqname, ',') }" var="names" />
											<c:forEach items="${names}" var="onename">
												<tr class="">
													<td>${onename}</td>
												</tr>
											</c:forEach>
										</table>
									</c:when>
									<c:otherwise>
										${item.jqname}
									</c:otherwise>
								</c:choose> 
							</td>
							<td>${item.fwqname}</td>
							<td>
								<c:if test="${item.ptlx == ptlx_yun}">
									${item.fwqip}
								</c:if>
								<c:if test="${item.ptlx == ptlx_tg}">
									${item.fwqip_ww}
								</c:if>
							</td>
							<td>${item.dk}</td>
							<td>${item.fwqczxt_dicvalue}</td>
							<td>
								<c:choose>
									<c:when test="${item.fwqstatus == '0'}">
										<font color="green">${item.fwqstatus_dicvalue} </font>
									</c:when>
									<c:otherwise>
										<font color="red" >${item.fwqstatus_dicvalue} 【${item.statusinfo}】</font>
									</c:otherwise>
								</c:choose> 
							</td>
							<td>
								<c:if test="${not empty item.dylxr}">
									<div style="padding: 8px;">
										${item.dylxr}：<c:if test="${not empty item.dylxrdh}">【${item.dylxrdh}】</c:if>
									</div>
								</c:if>
								<c:if test="${not empty item.delxr}">
									<div style="padding: 8px;">
										${item.delxr}：<c:if test="${not empty item.delxrdh}">【${item.delxrdh}】</c:if>
									</div>
								</c:if>
							</td>
							<td>
								<a href="javascript:void(0)" 
										onclick="tomcatLogs('${item.ptlx}','${item.fwqip_ww}','${item.fwqip}','${item.dk}')" >运行日志</a>
								<a href="javascript:void(0)" 
										onclick="fwqEdit('${item.id}')" >编辑</a>
								<a href="javascript:void(0)" 
										onclick="fwqcx('${item.id}','${item.fwqname}','${item.fwqip}','${item.fwqip_ww}')" >撤销</a>
							</td>
							<input type=hidden name="fwqid" value="${item.fwqid}" />
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="pagelist">
		<%@ include file="../include/page.inc"%>
	</div>
</section>
<!-- 操作结果显示层 -->
<div id="resultShow" class="resultShow" >
	<input id="result-show-fwqid" type="hidden" value="" />
	<input id="result-show-fwqip" type="hidden" value="" />
	<div class="result-show-title">
		服务器【<font><span id="show-fwq"></span></font>】执行<font color="#A1D0F6">删除</font>操作
	</div>
	<div class="result-show-tip">
		<p>撤销该服务器之后运行在该服务器上面的应用也将不能在该服务器上面继续访问，您确定要撤销吗？</p>
	</div>
	
	<div id="result-show-content" class="result-show-content">
	</div>
	<div class="result-show-controller">
		<input id="result-show-okBtn" class="defaultBtn" onclick="realcxfwq()" type="button" value="确定" />
	</div>
</div>

