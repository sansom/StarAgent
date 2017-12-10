<%@page import="com.core.jadlsoft.utils.DateUtils"%>
<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@page import="com.core.jadlsoft.utils.MBConstant"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>集群编辑</title>
		<style type="text/css">
			input {
				border: none;
			}
		</style>
		<%@ include file="../include/include.jsp"%>
		<script type="text/javascript">
			//内容item模板
			var content_item_templete = '<div class="result-show-content-item">' +
					'<span class="result-show-content-title">&item-title&</span>' +
					'<span class="result-show-content-status" id="&item-status-id&">&item-status&</span>' +
					'</div>';
		
			//定义成员变量，存储当前选中的ptlx为云平台还是托管平台
			var currentPtlx = "";
			var beforeData = ""; 	//初始时候的数据信息
		
			$(function(){
				//初始化设置服务器id集合的隐藏域的值
				//$("#fwqids").val('${fwqids}');
				//初始化选择的服务器平台类型
				var lx = $("#currPtlx").val();
				if (lx && lx != null && lx != "") {
					currentPtlx = lx;
				}
				beforeData = $("#jqFormId").serialize();
			});
		
		  	//保存
		  	function save(){
		  		
		  		$("#insert").attr("disabled", true);
		  		//1.校验表单
				var err = checkForm(document.forms["jqFormId"]);
				if(!err){
					//location.reload(true);
					$("#insert").attr("disabled", false);
					return false;
				}
				var jqid = $("#jqid").val();
				
				var nowData = $("#jqFormId").serialize();
				if (nowData == beforeData) {
					//数据一样
					layer.alert("没有变化！");
					//location.reload(true);
					$("#insert").attr("disabled", false);
					return false;
				}
			
				//2.保存
				if(jqid == null || jqid == ""){
					//新加操作
					$("#show-title-jq").html($("#jqname").val());
					$("#show-title-do").html("添加");
					$("#result-show-content").append(
						$(
							content_item_templete
								.replace("&item-title&", "添加操作")
								.replace("&item-status-id&", "jq-save-status")
								.replace("&item-status&", "执行中...")
						)
					);
					$.ajax({
						type : "post",
						url : "jqsave.action",
						dataType : "json",
						data : $("#jqFormId").serialize(),
						success : function(data) {
							$("#insert").attr("disabled", false);
							if (data.statusCode == "0000") {
								window.location.href = "jqlist.action";
							}else {
								$("#jq-save-status").html('<font color="red">'+data.msg+'</font>');
								return false;
							}
						}
					});
				}else{
					ajaxUpdate();	//异步更新，涉及到更新Nginx配置
				}
			}
			
			//异步提交表单
			function ajaxUpdate() {
			
				showBindLayer("resultShow","","300px");
			
				//更新操作
				$("#show-title-jq").html($("#jqname").val());
				$("#show-title-do").html("更新");
				$("#result-show-content").append(
					$(
						content_item_templete
							.replace("&item-title&", "nginx更新环境检测：")
							.replace("&item-status-id&", "nginx-update-status")
							.replace("&item-status&", "检测中...")
					)
				);
				var jqid = $("#jqid").val();
				$.ajax({
					type : "post",
					url : "../nginx/nginxcheckForUpdate.action",
					data : "jqid="+jqid,
					dataType : "json",
					success : function(data) {
						$("#result-show-backBtn").bind("click", function(){
							window.location.href = "jqlist.action";
						});
						if (data.statusCode == '0000') {
							//检验通过
							$("#nginx-update-status").css("color", "green");
							$("#nginx-update-status").html(data.msg);
							//开始执行更新的操作
							$.ajax({
								type : "post",
								url : "jqupdate.action",
								dataType : "json",
								data : $("#jqFormId").serialize(),
								success : function(data) {
									for(var i = 0;i<data.length;i++) {
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
											//最后一个，判断是否为询问的
											if (data[i].arg2 == '0') {
												//是询问的
												$("#item-status-id"+i).css("color", "#FFA84F");
												var url = data[i].arg3;
												$("#result-show-backBtn").val("取消");
												$("#result-show-okBtn").show();
												$("#result-show-okBtn").bind("click", function() {
													window.location.href = url;
												});
											}
										}
									}
								}
							});
						}else {
							//检验不通过
							$("#nginx-update-status").css("color", "red");
							$("#nginx-update-status").html(data.msg);
							$("#result-show-content").append(
								$(
									content_item_templete
										.replace("&item-title&", "操作结果：")
										.replace("&item-status-id&", "item-status-id")
										.replace("&item-status&", "<font color='red'>失败！</font>")
								)
							);
						}
					}
				});
			}
			
			//选择服务器
			function selFwqCbx(obj,fwqid,ptlx) {
				var checked = $(obj).is(':checked');
				if (checked) {
					//选中状态
					if (currentPtlx != null && currentPtlx != "") {
						//当前有平台类型，就必须选择同平台类型的
						if (ptlx != currentPtlx) {
							layer.alert("只能选择同一平台类型的服务器！");
							$("#fwqckb"+fwqid).prop("checked", false);
							return;
						}
					}else {
						//当前没有选中的，选择后设置当前的平台类型
						currentPtlx = ptlx;
					}
				}else {
					//取消选中，如果当前只有一个处于选中状态，就去掉当前的平台类型
					var size = $(".yunfwqckb:checked").size() + $(".tgfwqckb:checked").size();
					if (size == 0) {
						//取消当前的平台类型
						currentPtlx = "";
					}
				}
			}
			
			//选择服务器的全选按钮点击事件
			function selectAllFwq(obj, ptlx) {
				var checked = $(obj).is(':checked');
				
				if (checked) {
					if (currentPtlx != null && currentPtlx != "" && currentPtlx != ptlx) {
						layer.alert("只能选择同一平台类型的服务器！");
						$(obj).prop("checked", false);
						return;
					}
					currentPtlx = ptlx;
					if (ptlx == '01') {
						$(".yunfwqckb").each(function(){
							$(this).prop("checked", true);
						});
					}
					if (ptlx == '02') {
						$(".tgfwqckb").each(function(){
							$(this).prop("checked", true);
						});
					}
				}else {
					//取消，清空当前平台类型
					currentPtlx = "";
					if (ptlx == '01') {
						$(".yunfwqckb").each(function(){
							$(this).prop("checked", false);
						});
					}
					if (ptlx == '02') {
						$(".tgfwqckb").each(function(){
							$(this).prop("checked", false);
						});
					}
				}
				
			}
			
  		</script>
	</head>
<body>
	<c:set value="<%= SystemConstants.PTLX_YUN %>" var="ptlx_yun" />
	<c:set value="<%= SystemConstants.PTLX_TG %>" var="ptlx_tg" />
	<section id="contentMain">
		<h2 class="infoTitle">集群编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="jqFormId"
              		method="post" id="jqFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input name="jqJbxxBean.id" type="hidden" id="jqid" value="${jqJbxxBean.id}" />
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>集群名称：</label>
			            <div class="labelRight">
			            	<input type="text" name="jqJbxxBean.jqname" id="jqname"
								value="${jqJbxxBean.jqname}" class="inputText"
								size="30" title="集群名称"
								alt="notnull;length<=50;" maxlength="50" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>访问域名：</label>
			            <div class="labelRight">
			            	<input type="text" name="jqJbxxBean.fwym" id="fwym"
								value="${jqJbxxBean.fwym}" class="inputText"
								size="30" title="访问域名"
								alt="notnull;length<=30;" maxlength="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>访问端口：</label>
			            <div class="labelRight">
			            	<input type="text" name="jqJbxxBean.fwdk" id="fwdk"
								value="${jqJbxxBean.fwdk}" class="inputText"
								size="30" title="访问端口"
								alt="notnull;length<=6;" maxlength="6" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">接入服务器：</label>
			            <div class="labelRight">
			            	<c:choose>
								<c:when test="${empty fwqList && empty kyfwqList}">
									<!-- 没有可用 -->
									没有可用服务器
								</c:when>
								<c:otherwise>
									<div style="width:350px;padding-top: 5px;padding-bottom: 5px;padding-left: 6px;display: inline-block;">
									<input id="yunselectAll" style="line-height: 24px;width: 25px;margin-left: 0px;" type="checkbox" onclick="selectAllFwq(this,'01')"/>
									<label for="yunselectAll" style="width: 100px;text-align: left;line-height: 24px;height: 24px;margin-top: -3px;">云平台</label>
									<table class="api_table" border="0" cellspacing="0" cellpadding="0">
											<colgroup style="width: 10%" />
											<colgroup style="width: 90%" />
											<c:if test="${not empty fwqList}">
												<c:forEach items="${fwqList}" var="fwq">
													
													<c:if test="${fwq.ptlx==ptlx_yun}">
														<!-- 设置当前的平台类型 -->
														<input type="hidden" id="currPtlx" value="<%= SystemConstants.PTLX_YUN %>" />
														<tr class="mb-ta-head">
															<td style="padding-left: 22px;"><input class="yunfwqckb" id="fwqckb${fwq.id}" onclick="selFwqCbx(this,'${fwq.id}','${fwq.ptlx}')" checked="checked" name="fwqids" value="${fwq.id}" class="fwqckb" style="width: 20px;" type="checkbox"/></td>
															<td>
																<label for="fwqckb${fwq.id}">
																	${fwq.fwqname}【
																		${fwq.fwqip}
																	: ${fwq.dk}】
																</label>
															</td>
														</tr>
													</c:if>
												</c:forEach>
											</c:if>
											<c:if test="${not empty kyfwqList}">
												<c:forEach items="${kyfwqList}" var="kyfwq">
													<c:if test="${kyfwq.ptlx == ptlx_yun}">
														<tr class="mb-ta-head">
															<td style="padding-left: 22px;"><input class="yunfwqckb" id="fwqckb${kyfwq.id}" onclick="selFwqCbx(this,'${kyfwq.id}','${kyfwq.ptlx}')" name="fwqids" value="${kyfwq.id}" class="fwqckb" style="width: 20px;" type="checkbox"/></td>
															<td>
																<label for="fwqckb${kyfwq.id}">
																	${kyfwq.fwqname}【
																		${kyfwq.fwqip}
																		<%-- <c:if test="${kyfwq.ptlx == '01'}">${kyfwq.fwqip}</c:if>
																		<c:if test="${kyfwq.ptlx == '02'}">${kyfwq.fwqip_ww}</c:if> --%>
																	: ${kyfwq.dk}】
																</label>
															</td>
													</tr>
													</c:if>
												</c:forEach>
											</c:if>
									</table>
									
									<br/><br/>
									
									<input id="tgselectAll" style="line-height: 24px;width: 25px;margin-left: 0px;" type="checkbox" onclick="selectAllFwq(this,'02')"/>
									<label for="tgselectAll" style="width: 100px;text-align: left;line-height: 24px;height: 24px;margin-top: -3px;">托管</label>
									<table class="api_table" border="0" cellspacing="0"
											cellpadding="0">
											<colgroup style="width: 10%" />
											<colgroup style="width: 90%" />
											<c:if test="${not empty fwqList}">
												<c:forEach items="${fwqList}" var="fwq">
													<c:if test="${fwq.ptlx == ptlx_tg}">
														<!-- 设置当前的平台类型 -->
														<input type="hidden" id="currPtlx" value="${fwq.ptlx}" />
														<tr class="mb-ta-head">
															<td style="padding-left: 22px;"><input class="tgfwqckb" id="fwqckb${fwq.id}" onclick="selFwqCbx(this,'${fwq.id}','${fwq.ptlx}')" checked="checked" name="fwqids" value="${fwq.id}" class="fwqckb" style="width: 20px;" type="checkbox"/></td>
															<td>
																<label for="fwqckb${fwq.id}">
																	${fwq.fwqname}【
																		${fwq.fwqip_ww}
																	: ${fwq.dk}】
																</label>
															</td>
														</tr>
													</c:if>
												</c:forEach>
											</c:if>
											<c:if test="${not empty kyfwqList}">
												<c:forEach items="${kyfwqList}" var="kyfwq">
													<c:if test="${kyfwq.ptlx==ptlx_tg}">
														<tr class="mb-ta-head">
															<td style="padding-left: 22px;"><input class="tgfwqckb" id="fwqckb${kyfwq.id}" onclick="selFwqCbx(this,'${kyfwq.id}','${kyfwq.ptlx}')" name="fwqids" value="${kyfwq.id}" class="fwqckb" style="width: 20px;" type="checkbox"/></td>
															<td>
																<label for="fwqckb${kyfwq.id}">
																	${kyfwq.fwqname}【
																		${kyfwq.fwqip_ww}
																	: ${kyfwq.dk}】
																</label>
															</td>
														</tr>
													</c:if>
												</c:forEach>
											</c:if>
									</table>
								</div>
								</c:otherwise>
							</c:choose>
			            </div>
			        </li>
			        
			        <!-- 操作按钮 -->
			        <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight">
			                <input type="button" class="defaultBtn" onclick="save()" value="保存" />
			                <input type="button" class="defaultBtn backBtn" onclick="history.back(-1)" value="返回" />
			            </div>
			        </li>
			    </ul>
			</form>
		</div>    
	</section>

	<!-- 操作结果显示层 -->
	<div id="resultShow" class="resultShow">
		<input id="result-show-fwqid" type="hidden" value="" />
		<div class="result-show-title">
			集群【<font><span id="show-title-jq"></span></font>】执行<font color="#A1D0F6"><span id="show-title-do">删除</span></font>操作
		</div>
		<div id="result-show-content" class="result-show-content">
		</div>
		<div class="result-show-controller">
			<input id="result-show-okBtn" class="defaultBtn" type="button" value="确定" style="display: none;" />
			<input id="result-show-backBtn" class="close defaultBtn" type="button" value="返回" />
		</div>
	</div>
</body>
</html>

