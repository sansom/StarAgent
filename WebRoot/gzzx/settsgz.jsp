<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@page import="org.apache.taglibs.standard.tag.common.core.ForEachSupport"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>推送规则配置</title>
	<%@ include file="../include/include.jsp"%>
	<link type="text/css" href="../index/css/main.css" rel="stylesheet" />
	<script type="text/javascript">
	
		//成员变量，存储当前点击弹出框的规则id
		var currentGzid = "";
	
		$(function(){
			$(".selectRyBt").each(function(){
			
				var gzid = this.id.substring(4);
				$(this).click(function(){
				
					showBindLayer("selectRyDiv", "", "", "人员配置");
				
					currentGzid = gzid;
					//初始化人员的选中状态
					var ryids = $("#ryidsHide"+gzid).val();
					var ryidsBefore = new Array();
					if (ryids != null && ryids != "") {
						ryidsBefore = ryids.split(",");
					}
					
					$(".selRyCkb").each(function(){
						var ryid = $(this).val();
						if (ryidsBefore.contains(ryid)) {
							//之前选中
							$(this).attr("checked", true);
						}else {
							$(this).attr("checked", false);
						}
					});
				});
			});
		});
		
		//是否启用规则
		function changeQyzt(gzid) {
			var qyzt = '0';
			var msg = "确认启用该规则？";
			if (!$("#gzckb"+gzid).is(':checked')) {
				//取消选中
				qyzt = '1';
				msg = "确认停用该规则？";
			}
			confirmDo(msg, function(){
				$.ajax({
					type : "post",
					url : "gzzxchangeGzQyzt.action",
					data : "gzid=" + gzid + "&qyzt=" + qyzt,
					dataType : "json",
					success : function(data) {
						layer.alert(data.msg);
					}
				});
			}, function(){
				//拒绝选中
				$("#gzckb"+gzid).attr("checked",!$("#gzckb"+gzid).is(':checked'));
			});
		}
		
		//取消人员选择
		function qxSelRy() {
			var gzid = currentGzid;
			//还原之前的选中状态	
			var ryids = $("#ryidsHide"+gzid).val();
			var ryidsBefore = new Array();
			if (ryids != null && ryids != "") {
				ryidsBefore = ryids.split(",");
			}
			$(".selRyCkb").each(function(){
				var ryid = $(this).val();
				if (ryidsBefore.contains(ryid)) {
					//之前选中
					$(this).attr("checked", true);
				}else {
					$(this).attr("checked", false);
				}
			});
			layer.closeAll('page');	//关闭所有页面层
		}
		
		//确认选择人员
		function qrSelRy() {
			var gzid = currentGzid;
			var ryids = "";
			var rynames = "";
			$(".selRyCkb").each(function(){
				if ($(this).is(':checked')) {
					var ryid = $(this).val();
					ryids += ryid;
					ryids += ",";
					
					rynames += $("#label"+ryid).html();
					rynames += ",";
				}
			});
			if (ryids != "") {
				ryids = ryids.substring(0, ryids.length-1);
			}
			
			if (rynames != "") {
				rynames = rynames.substring(0, rynames.length-1);
			}
			$.ajax({
				type : "post",
				url : "gzzxupdateTsgzRy.action?",
				data : "ryids=" + ryids + "&gzid=" + gzid,
				success : function(data) {
					if (data == "success") {
						$("#ryidsHide"+gzid).val(ryids);
						$("#ryids"+gzid).html(rynames);
					}
				}
			});
			layer.closeAll('page');	//关闭所有页面层
		}
	</script>
</head>
<body >
	<section id="contentMain">
		<h2 class="infoTitle">推送规则配置</h2>
        <div class="contentDiv mainContent">
        	<table class="listTable" border="0" cellspacing="0"
				cellpadding="0">
				<colgroup style="width: 20%" />
				<colgroup style="width: 20%" />
				<colgroup style="width: 20%" />
				<c:forEach items="${gzList}" var="gz">
					<tr class="">
						<td>
							<c:if test="${gz.qyzt=='0'}">
								<input id="gzckb${gz.dm}" onclick="changeQyzt('${gz.dm}')" type="checkbox" checked="checked" />
							</c:if>
							<c:if test="${gz.qyzt!='0'}">
								<input id="gzckb${gz.dm}" onclick="changeQyzt('${gz.dm}')" type="checkbox" />
							</c:if>
							<label for="gzckb${gz.dm}">${gz.mc}</label>
						</td>
						<td><input id="gzbt${gz.dm}" type="button" class="selectRyBt defaultBtn primaryBtn" value="人员配置" onclick="rypz()" /></td>
						<td id="ryids${gz.dm}">${gz.rynames}</td>
						<input id="ryidsHide${gz.dm}" type="hidden" value="${gz.ryids}" />
					</tr>
				</c:forEach>
			</table>
		</div>    
	</section>
	<!-- 点击运行人员配置弹出层 -->
     <div id="selectRyDiv" class="log_div " style="display: none;width: 350px;">
     	<c:if test="${empty ryList}">
     		暂时没有可选人员，请去添加人员信息！
     	</c:if>
     	<c:if test="${not empty ryList}">
     		<c:forEach items="${ryList}" var="ryItem">
     			<div style="float: left;display: inline-block;padding: 5px 20px;">
     				<input class="selRyCkb" value="${ryItem.id}" id="ckb${ryItem.id}" type="checkbox" />
     				<span><label id="label${ryItem.id}" for="ckb${ryItem.id}">${ryItem.username}</label></span>
     			</div>
     		</c:forEach>
     		<div style="clear: both;height: 20px;"></div>
     		<div style="display: block;margin: 0 auto;">
     			<input class="defaultBtn primaryBtn" onclick="qrSelRy()" type="button" value="确认" />
     			<input class="defaultBtn backBtn" onclick="qxSelRy()" type="button" value="取消" />
     		</div>
     	</c:if>
     </div>
</body>
</html>
