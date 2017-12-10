<%@page import="com.core.jadlwork.model.nginx.NginxBean"%>
<%@page import="com.core.jadlwork.model.yygl.YyBean"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>nginx配置列表</title>
	<%@ include file="../include/include.jsp"%>
	<script type="text/javascript" src="../common/jquery.form.min.js" ></script>
	<script type="text/javascript">
		//点击上传模板事件
		function uploadTemplete(){
		
			showBindLayer("templeteUploadDiv", "600px", "260px", "模板上传");
		
			//清空之前的信息
			$("#uploadTempleteResultInfo").html("----");
			$("#fileInput").val("");
		}
	
		//上传模板的确定事件
     	function uploadTempleteSure() {
     		var errmsg = "";
     		var filename = $("#fileInput").val();
     		
     		//校验文件是否为空
			if (filename == null || filename == "") {
				errmsg = "请选择文件！";
			} else {
				//校验上传文件的后缀
	     		var suffix = filename.substring(filename.lastIndexOf("."));
	     		if (suffix != ".conf") {
					errmsg = "文件格式不正确，上传失败！请上传.conf后缀的文件！";
				}
			}
     		
			if (errmsg != "") {
				$("#uploadTempleteResultInfo").html("<font color='red'>"+errmsg+"</font>");
				return;
			}
     		//ajax异步上传文件
     		$("#uploadTempleteFormId").ajaxSubmit({
     			dataType: "json",
     			success: function(data){
     				if (data.statusCode == "0000") {
						//上传成功
						$("#uploadTempleteResultInfo").html("<font color='green'>上传成功!</font>");
					}else {
						$("#uploadTempleteResultInfo").html("<font color='red'>"+data.msg+"</font>");
					}
     			}
     		});
     	}
     	
     	//模板下载
     	function downloadTemplete(nid) {
     		var load_index = layer.load();
     		//下载文件之前先判断文件是否存在
     		if (nid == null || nid == "") {
     			layer.close(load_index);
				layer.alert("nginxid为空，请重试！");
				return;
			}
     		$.ajax({
     			url : "nginxisNginxConfTempleteExist.action",
     			type : "post",
     			async: false,
     			data : "nid="+nid,
     			success : function (data) {
     				layer.close(load_index);
     				if (data == "success") {
						//存在就直接下载
						window.location.href = "nginxdownloadNginxConfTemplete.action?nid="+nid;
					}else {
						layer.alert("模板文件不存在！");
					}
     			},
     			error: function(){
     				layer.close(load_index);
     			}
     		});
     	}
	
		//生成Nginx配置文件
		function createNginxConf(nid, isAlert){
			$("#genernateLocalConf").attr("disabled",true);
			var result = "";
			$.ajax({
				url:"nginxcreateNginxConf.action",
				data:"nid="+nid,
				dataType:"json",
				async:false,
				success: function(resultBean){
					if (resultBean.statusCode == "0000") {
						result = "success";
					}else {
						result = resultBean.msg;
					}
				}
			});
			if (isAlert) {
				if (result == "success") {
					confirmDo("生成成功！是否查看？", function(){
						window.open("nginxviewLocalNginxConf.action?nid="+nid);
					});
				}else {
					layer.alert(result);
				}
			}
			$("#genernateLocalConf").attr("disabled",false);
			return result;
		}
		
		//查看生成的本地配置文件
		function showNginxConf(nid) {
			$("#viewLocalConf").attr("disabled",true);
			//先生成配置文件，再去打开
			var resultInfo = createNginxConf(nid, false);
			$("#viewLocalConf").attr("disabled",false);
			if (resultInfo == "success") {
				//生成成功
				window.open("nginxviewLocalNginxConf.action?nid="+nid);
			}else {
				layer.alert("打开nginx配置文件失败！（生成过程中失败！）\r\n原因为：【"+resultInfo+"】");
			}
		}
		
		//替换Nginx配置文件
		function replaceNginxConf(nid, fwqip, nginxRootPath){
			var load_index = layer.load();
			$("#replaceOnlineConf").attr("disabled",true);
			var msg = createNginxConf(nid);	//先生成Nginx配置文件
			if (msg == "success") {
				$.ajax({
					url:"nginxreplaceNginxConf.action",
					data:"nid="+nid+"&fwqip="+fwqip+"&nginxRootPath="+nginxRootPath,
					dataType:"json",
					success: function(resultBean){
						layer.close(load_index);
						setTimeout(function(){
							$("#replaceOnlineConf").attr("disabled",false);
								if (resultBean.statusCode == "0000") {
									confirmDo("替换成功！是否查看当前线上配置？", function(){
										showRemoteNginxConf(nid, fwqip, nginxRootPath);
									});
								}else {
									layer.alert(resultBean.msg);
								}
						}, 10);
					}
				});
			}else {
				layer.close(load_index);
				setTimeout(function(){
					layer.alert("替换nginx配置文件失败！（生成过程中失败！）\r\n原因为：【"+msg+"】");
					$("#replaceOnlineConf").attr("disabled",false);
				},10);
			}
		}
		
		//查看线上配置
		function showRemoteNginxConf(nid, fwqip, nginxRootPath) {
			var load_index = layer.load();
			$("#viewOnlineConf").attr("disabled",true);
			//先下载线上配置
			$.ajax({
				type: "post",
				url : "nginxdownloadFwqNginxConfig.action",
				data: "fwqip="+fwqip+"&nginxRootPath="+nginxRootPath+"&nid="+nid,
				dataType:"text",
				success:function(data){
					layer.close(load_index);
					setTimeout(function(){
						$("#viewOnlineConf").attr("disabled",false);
						if (data == "success") {
							//成功，直接跳转到查看的界面
							window.open("nginxshowFwqNginxConfig.action?nid="+nid);
						}else {
							layer.alert("查看线上配置文件失败！（下载过程中失败！）\r\n原因为：【"+data+"】");
						}
					},10);
				},
				error : function(a,b) {
					layer.close(load_index);	
					console.log(b);
				}
			});
		}
		
		//试发布规则配置
		function sfbgzpz(nid) {
			window.location.href = "nginxsfbgzpz.action?nid="+nid;
		}
		
	</script>
</head>
<body>

	<%
 		String rootPath = "";
		NginxBean nginxBean = request.getAttribute("nginxBean")==null ? null : (NginxBean) request.getAttribute("nginxBean");
		if(nginxBean != null) {
			String srcRootPath = nginxBean.getNginxsrc();
			if(srcRootPath != null && !srcRootPath.equals("")) {
				rootPath = srcRootPath.replace("\\", "\\\\");
			}
		}
	 %>

	<section id="contentMain">
		<h2 class="infoTitle">配置信息</h2>
		<input type="button" class="defaultBtn" onclick="sfbgzpz('${nginxBean.id}')" value="试发布规则" />
		<input id="uploadTemplete" type="button" class="defaultBtn primaryBtn" onclick="uploadTemplete()" value="模板上传" />
		<input type="button" id="downloadTemplete" class="defaultBtn exportBtn" onclick="downloadTemplete(${nginxBean.id})" value="模板下载" />
		
		<div style="margin-top: 20px;">
			<c:if test="${empty jqyyfwqInfo}">
				<div>
					<blockquote class="layui-elem-quote" style="background: white;">暂无配置信息</blockquote>
				</div>
			</c:if>
			<c:if test="${not empty jqyyfwqInfo}">
            	<c:forEach items="${jqyyfwqInfo}" var="jqitem" varStatus="jqstatus">
            		<c:if test="${not empty jqitem.value['yyinfo']}">
            			<c:set value="${fn:length(jqitem.value['yyinfo'])}" var="yyNum" />
            			<c:set value="${fn:length(jqitem.value['syxyyinfo'])}" var="syxyyNum" />
	            		<table class="listTable" border="0" cellspacing="0" cellpadding="0" style="margin-bottom: 20px;">
							<colgroup style="width: 15%"/>
							<colgroup style="width: 10%"/>
							<colgroup style="width: 10%"/>
							<colgroup style="width: 15%"/>
							<colgroup style="width: 10%"/>
							<colgroup style="width: 25%"/>
							<c:if test="${jqstatus.index == 0}">
                				<tr class="title">
                					<th >集群名称</th>
                					<th >访问域名</th>
				                    <th >监听端口</th>
				                    <th >应用名称</th>
				                    <th >war包名称</th>
				                    <th >代理服务器信息</th>
					            </tr>
				            </c:if>
				            <c:forEach items="${jqitem.value['yyinfo']}" var="yyitem" varStatus="status">
				            	<tr>
				            		<c:if test="${status.index == 0}">
					            		<td rowspan="${yyNum}">${jqitem.value['jqname']}</td>
					            		<td rowspan="${yyNum}">${jqitem.value['fwym']}</td>
					            		<td rowspan="${yyNum}">${jqitem.value['fwdk']}</td>
				            		</c:if>
				            		<td>${yyitem.value['yyname']}</td>
				            		<td>${yyitem.value['warname']}</td>
				            		<td>
				            			<c:if test="${not empty yyitem.value['fwqinfo']}">
				            				<table align="center" style="width: 100%;">
				            					<c:forEach items="${yyitem.value['fwqinfo']}" var="fwqitem">
					            					<tr>
					            						<td>${fwqitem.value['fwqname']}【${fwqitem.value['fwqip']}:${fwqitem.value['dk']}】
					            							<c:if test="${fwqitem.value['fbzt'] == '1'}">
					            								【<font color="#FFA815">试发布</font>】
					            							</c:if>
					            						</td>
					            					</tr>
					            				</c:forEach>
				            				</table>
				            			</c:if>
				            		</td>
				            	</tr>
				            </c:forEach>
			            </table>
		            </c:if>
            	</c:forEach>
            </c:if>
		</div>
		<!-- 操作按钮 -->
        <div class="clearfix">
            <div class="labelRight">
                <input type="button" class="defaultBtn primaryBtn" id="genernateLocalConf" 
                	onclick="createNginxConf(${nginxBean.id},true)" value="生成本地" />
                <input type="button" class="defaultBtn primaryBtn" id="replaceOnlineConf" 
                	onclick="replaceNginxConf('${nginxBean.id}','${nginxBean.fwqip}','<%= rootPath %>')" value="替换线上" />
                <span style="display:inline-block;width:20px;"></span>
                <input type="button" class="defaultBtn exportBtn" id="viewLocalConf" 
                	onclick="showNginxConf(${nginxBean.id})" value="查看本地" />
                <input type="button" class="defaultBtn exportBtn" id="viewOnlineConf" 
                	onclick="showRemoteNginxConf('${nginxBean.id}','${nginxBean.fwqip}','<%= rootPath %>')" value="查看线上" />
                
                <input type="button" class="defaultBtn backBtn" onclick="history.back()" value="返回" />
            </div>
        </div>
	</section>
	<!-- 上传模板弹出层 -->
     <div id="templeteUploadDiv" style="display: none;background: white;position: relative;" >
     	<form id="uploadTempleteFormId" method="post" enctype="multipart/form-data" action="nginxuploadNginxConfTemplete.action">
     		<input type="hidden" name="nid" value="${nginxBean.id}" />
	     	<table class="listTable" style="margin-top: 15px;">
	     		<tr>
	     			<td>上传配置文件模板：<input id="fileInput" type="file" name="myFile" /></td>
	     		</tr>
	     		<tr>
	     			<td>上传结果：<span id="uploadTempleteResultInfo">----</span></td>
	     		</tr>
	     		<tr>
	     			<td>
	     				<input type="button" onclick="uploadTempleteSure()" class="defaultBtn primaryBtn" value="上传" />
	     			</td>
	     		</tr>
	     	</table>
     	</form>
  	</div>
</body>
</html>
