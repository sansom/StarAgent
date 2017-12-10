<%@page import="org.apache.taglibs.standard.tag.common.core.ForEachSupport"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Nginx服务器列表</title>
	<%@ include file="../include/include.jsp"%>
	<style type="text/css">
		a {text-decoration: none;}
		.modal {
		    position: fixed;
		    top: 0;
		    right: 0;
		    bottom: 0;
		    left: 0;
		    overflow: hidden;
		    outline: 0;
		    -webkit-overflow-scrolling: touch;
		    background-color: rgb(0, 0, 0);  
		    filter: alpha(opacity=60);  
		    background-color: rgba(0, 0, 0, 0.6); 
		    z-index: 9999;
		}
		.modal img {
			width: 100px; 
			height: 100px;
			position: fixed;;
			top: 0px;
		    left: 0px;
		    right: 0px;
		    bottom: 0px;
		    margin: auto;
		}
	</style>
	<script type="text/javascript">
	
		$(function(){
			//定时更新Nginx状态
			setInterval(getNginxStatus,60*1000,60*1000);
		});
	
		//刷新Nginx状态
		function refresh() {
			getNginxStatus();
		}
	
		//更新Nginx启动状态
		function getNginxStatus() {
			//遍历所有的class为nginxStatus的span，将需要获取状态的信息递到后台
			var fwqInfos = "";
			$(".nginxStatus").each(function(){
				var nginxInfo = this.id.substring(7);	//截取获取id和服务器ip，形式为： id~fwqip 如： 1~192.168.20.124
				fwqInfos = fwqInfos + nginxInfo + ",";
			});
			//去除最后的逗号
			if (fwqInfos.length>0) {
				fwqInfos = fwqInfos.substring(0,fwqInfos.length-1);
			}
			$.ajax({
				type: "POST",
		   		url: "nginxgetNginxStatus.action",
		   		dataType:"text",
		   		data:"fwqInfos="+fwqInfos,
				success: function(data){
					//data的格式为  id~fwqip~status，就是在上面的那个后面跟了一个状态
					var infoArr = data.split(",");
					for ( var int = 0; int < infoArr.length; int++) {
						var id = "status~"+infoArr[int].substring(0,infoArr[int].length-2);
						var status = infoArr[int].substring(infoArr[int].length-1);
						//给对应的id设置状态信息
						var html;
						if (status == "1") {
							html = "<font color='red'>未启动</font>";
						}else if(status == "0") {
							html = "<font color='green'>启动</font>";
						}else {
							//服务器未开启
							html = "<font color='red'>服务器未开启</font>";
						}
						var obj = document.getElementById(id);
						obj.innerHTML = html;
					}
				}
			});
		}
	
		//配置Nginx
		function nginxConfig(nginxid) {
			window.location.href = "nginxconfig.action?nid="+nginxid;
		}
	
		//编辑nginx
		function nginxEdit(nginxid) {
			window.location.href = "nginxedit.action?nid="+nginxid;
		}
		//撤销nginx
		function nginxCx(nginxid) {
			confirmGo("确定撤销Nginx服务器？","nginxremove.action?nid="+nginxid);
		}
		
		//前往试发布配置界面
		function toSfbpz(nid) {
			window.location.href = "nginxsfbgzpz.action?nid="+nid;
		}
		
		//在线查看日志
		function viewLogOnline(nid) {
			window.open(encodeURI("nginxtoshowLogOnline.action?nid="+nid));
		}
		
		//下载当前日志
		function downloadLog(nid) {
			var load_index = layer.load();
			$.ajax({
				type: "post",
				url : "nginxdownloadLog.action",
				data: "nid="+nid,
				dataType:"text",
				success:function(data){
					layer.close(load_index);
					if (data == 'success') {
						//下载到本地成功
						window.location.href = "nginxdownloadLogFromLocal.action?nid="+nid;
					}else {
						layer.alert(data);
						return false;
					}
				},
				error : function(a,b) {
					console.log(b);
				}
			});
		}
		
	</script>
</head>
<body onload="getNginxStatus()">

	<section id="contentMain">
		<h2 class="infoTitle">集群管理</h2>
		<a type="button" class="addSomeBtn addBtnFora" style="margin-bottom: 20px;cursor: pointer;" onclick="window.location.href='nginxedit.action'">接入Nginx</a>
		<div>
			<table class="listTable" border="0" cellpadding="0" cellspacing="0">
	          		<colgroup style="width: 5%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 20%"/>
					<colgroup style="width: 15%"/>
					<colgroup style="width: 5%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
				<tbody>
					<tr class="title">
						<th >序号</th>
	                    <th >服务器名称</th>
	                    <th >线上更新状态</th>
	                    <th >试发布规则信息</th>
	                    <th >集群信息</th>
	                    <th >所在服务器ip</th>
	                    <th >运行状态</th>
	                    <th >日志</th>
	                    <th >操作</th>
					</tr>
					<c:if test="${empty list}">
						<tr><td colspan="9" align="center" style="text-align: center;" class="noData">暂时没有nginx信息</td></tr>
					</c:if>
					<c:if test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td >
									${item.fwqname}
								</td>
								<td>
									<c:if test="${item.gxzt != '1'}">
										<font color="green">更新成功</font>
									</c:if>
									<c:if test="${item.gxzt == '1'}">
										<font color="red">更新失败【${item.gxsbyy}】</font>
									</c:if>
								</td>
								<td>
									<c:if test="${not empty item.sfbgzinfo}">
										<table width="100%;">
											<c:forEach var="gz" items="${fn:split(item.sfbgzinfo,',')}">
												<tr>
													<td>
														<span style="cursor: pointer;" onclick="toSfbpz('${item.id}')">
															<font color="green">${gz}</font>
														</span>
													</td>
												</tr>
											</c:forEach>
										</table>
									</c:if>
									<c:if test="${empty item.sfbgzinfo}">
										<font color="red"><span style="cursor: pointer;" onclick="toSfbpz('${item.id}')">还没有配置试发布规则</span></font>
									</c:if>
								</td>
								<td>
									<c:if test="${not empty item.jqinfo}">
										<table width="100%;">
											<c:forEach var="jq" items="${fn:split(item.jqinfo,',')}">
												<tr>
													<td>
														${fn:split(jq,'@')[0]}
														【
															${fn:split(jq,'@')[1]}:${fn:split(jq,'@')[2]}
														】
													</td>
												</tr>
											</c:forEach>
										</table>
									</c:if>
								</td>
								<td>
									${item.fwqczxt_dicvalue}【${item.fwqip}】
								</td>
								<td>
									<span id="status~${item.id}~${item.fwqip}" class="nginxStatus">--</span>
								</td>
								<td>
									<a href="javascript:void(0)" 
											onclick="viewLogOnline('${item.id}')" >在线查看</a>
									<a href="javascript:void(0)" 
											onclick="downloadLog('${item.id}')" >下载当前</a>
								</td>
								<td>
									<a href="javascript:void(0)" 
											onclick="nginxConfig('${item.id}')" >查看配置</a>
									<a href="javascript:void(0)" 
											onclick="nginxEdit('${item.id}')" >编辑</a>
									<a href="javascript:void(0)" 
											onclick="nginxCx('${item.id}')" >撤销</a>
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
</body>
</html>
