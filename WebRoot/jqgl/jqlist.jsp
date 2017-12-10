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
	<title>集群列表</title>
	<%@ include file="../include/include.jsp" %>
	<script type="text/javascript">
	
		//内容item模板
		var content_item_templete = '<div class="result-show-content-item">' +
					'<span class="result-show-content-title">&item-title&</span>' +
					'<span class="result-show-content-status" id="&item-status-id&">&item-status&</span>' +
					'</div>';
  		function jqEdit(jqid) {
  			var url = "jqedit.action";
  			if (jqid) {
				url = "jqedit.action?jqid="+jqid;
			}
			window.location.href = url;
  		}
  		
  		/*
  		* 撤销集群
  		*/
  		function jqcx(id, name) {
  			showBindLayer("resultShow","","300px");
  		
	  		$("#show-jq").html(name);
			$("#result-show-jqid").val(id);
  		}
  		
  		//真正撤销集群
  		function realcxjq() {
  			$("#result-show-content").html("");
  			//执行删除的操作
  			$("#result-show-content").append(
				$(
					content_item_templete
						.replace("&item-title&", "删除操作")
						.replace("&item-status-id&", "jq-delete-init-status")
						.replace("&item-status&", "执行中...")
				)
			);
			$.ajax({
				type : "post",
				url : "jqremove.action",
				data : "jqid="+$("#result-show-jqid").val(),
				dataType : "json",
				success : function(data){
					//设置取消按钮为刷新页面，隐藏确定按钮
					$("#result-show-okBtn").hide();
					$("#result-show-backBtn").show();
					$("#result-show-backBtn").bind("click",function(){
						location.reload(true);
					});
					$("#result-show-content").html("");
					//拿到最后一个，带的是要更新的Nginx的id信息
					var nidInfo = data[data.length-1];
					var nids = "";
					if (nidInfo.statusCode == '0000') {
						nids = nidInfo.msg;
					}
					for(var i = 0;i<data.length;i++) {
						if (i == data.length-2) {
							//倒数第二个，是最终的结果
							if (data[i].statusCode == '0000') {
								//如果成功，说明删除的操作是成功完成的，这里去更新Nginx配置（采用异步的方式，因为即使不更新也不影响用户的访问）
								asyncUpdateNginx(nids);
							}else {
								//失败，说明操作失败，暂不处理
								
							}
						}
						if (i == data.length-1) {
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
					}
				}
			});
  		}
  		
  		//删除集群之后，异步更新Nginx配置
  		function asyncUpdateNginx(nids) {
  			$("#result-show-content").append(
				$(
					content_item_templete
						.replace("&item-title&", "更新Nginx配置信息：")
						.replace("&item-status-id&", "item-id-update-nginx")
						.replace("&item-status&", "执行中...")
				)
			);
  			$.ajax({
  				type : "post",
  				url : "jqasyncUpdateNginxConf.action",
  				data : "nids=" + nids,
  				dataType : "json",
  				success : function(data) {
  					if (data.statusCode == '0000') {
	  					$("#item-id-update-nginx").html(data.msg);
  						$("#item-id-update-nginx").css("color", "green");
					}else {
						$("#item-id-update-nginx").html(data.msg+"【请检测问题后手动更新】");
						$("#item-id-update-nginx").css("color", "red");
					}
  				}
  			});
  		}
  		
	</script>
</head>
<body >

<section id="contentMain">
	<h2 class="infoTitle">集群管理</h2>
	<a type="button" class="addSomeBtn addBtnFora" style="margin-bottom: 20px;cursor: pointer;" onclick="jqEdit('')">接入集群</a>
	<div>
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
          		<colgroup style="width: 1%" />
          		<colgroup style="width: 15%" />
				<colgroup style="width: 20%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 35%" />
				<colgroup style="width: 20%" />
			<tbody>
				<tr class="title">
					<th></th>
					<th>集群名称</th>
					<th>访问域名</th>
					<th>监听端口</th>
					<th>代理服务器</th>
					<th>操作</th>
				</tr>
				<c:if test="${empty list}">
					<tr><td colspan="6" align="center" style="text-align: center;" class="noData">暂时没有服务器信息</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr>
							<td></td>
							<td>${item.jqname}</td>
							<td>${item.fwym}</td>
							<td>${item.fwdk}</td>
							<td>
								<c:if test="${empty item.fwqinfo}"></c:if>
								<c:if test="${not empty item.fwqinfo}">
									<table class="api_table" border="0" cellpadding="0" cellspacing="0">
										<colgroup style="width: 85%" />
										<colgroup style="width: 15%" />
										<c:forEach var="fwq" items="${fn:split(item.fwqinfo,',')}">
											<tr>
												<c:if test="${fn:split(fwq,'@')[3] == 0}">
													<td><font color="green">${fn:split(fwq,'@')[0]}
														【
															${fn:split(fwq,'@')[1]}:${fn:split(fwq,'@')[2]}
														】
														</font>
													</td>
													<td>
														<font color="green">正常</font>
													</td>
												</c:if>
												<c:if test="${fn:split(fwq,'@')[3] != 0}">
													<td><font color="red">${fn:split(fwq,'@')[0]}
														【
															${fn:split(fwq,'@')[1]}:${fn:split(fwq,'@')[2]}
														】
														</font>
													</td>
													<td>
														<font color="red">异常</font>
													</td>
												</c:if>
											</tr>
										</c:forEach>
									</table>
								</c:if>
							</td>
							<td>
								<a href="javascript:void(0)" 
										onclick="jqEdit('${item.id}')" >编辑</a>
								<a href="javascript:void(0)" 
										onclick="jqcx('${item.id}','${item.jqname}')" >撤销</a>
							</td>
							<input type=hidden name="jqid" value="${item.id}" />
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
<div id="resultShow" class="resultShow">
	<input id="result-show-jqid" type="hidden" value="" />
	<div class="result-show-title">
		集群【<font><span id="show-jq"></span></font>】执行<font color="#A1D0F6">删除</font>操作
	</div>
	<div class="result-show-tip">
		<p>确定要撤销集群？</p>
	</div>
	
	<div id="result-show-content" class="result-show-content">
	</div>
	<div class="result-show-controller">
		<input id="result-show-okBtn" class="defaultBtn" onclick="realcxjq()" type="button" value="确定" />
		<input id="result-show-backBtn" class="defaultBtn backBtn" style="display: none;" onclick="" type="button" value="返回" />
	</div>
</div>
</body>
</html>