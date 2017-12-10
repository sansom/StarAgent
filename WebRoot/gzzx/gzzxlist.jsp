<%@page import="com.core.jadlwork.cache.gzzx.GzzxCache"%>
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
	<title>故障中心列表</title>
	<%@ include file="../include/include.jsp"%>
	<script type="text/javascript">
		
		$(function(){
			//初始化设置当前的过滤时长
			$("#gzFilMinute").val('<%= GzzxCache.gzFilterMinute %>');
		});
	
		function reloadFrame(frameId) {
			$("#"+frameId).attr('src', $("#"+frameId).attr('src'));
		}
		//设置过滤时长
		function updateGzFilMinute(value){
			$.ajax({
				type : "post",
				url : "gzzxupdateGzFilMinute.action",
				data : "minute=" + value,
				success : function(data) {
					if (data == "success") {
						layer.alert("修改成功！");
					}else {
						layer.alert("修改失败！");
					}
				},
				error : function(a,b) {
					layer.alert("修改失败！");
					console.log(b);
				}
			});
		}
	</script>
</head>
<body>
	<section id="contentMain">
		<div class="clearfix" style="height: auto;width: 400px;padding-bottom: 10px;">
            <label class="labelLeft" style="width: 35%;line-height: 30px;">故障重复过滤时长：</label>
            <div class="labelRight">
            	<select id="gzFilMinute" onchange="updateGzFilMinute(this.value)" class="selectStyle">
					<option value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
				</select> 分钟
            </div>
        </div>
		<div class="contentDiv mainContent">
			<div class="layui-tab layui-tab-card" style="margin-top: 0px;">
				<ul class="layui-tab-title">
				    <li onclick="reloadFrame('gzzxFrameFWQ')" class="layui-this">服务器异常故障列表</li>
				    <li onclick="reloadFrame('gzzxFrameYY')">应用异常故障列表</li>
				    <li>应用运行异常故障列表</li>
				</ul>
				<div class="layui-tab-content" style="height: 700px;">
				    <div class="layui-tab-item layui-show" style="height: 100%;">
						<iframe frameborder="0" id="gzzxFrameFWQ" style="width: 100%;height: 100%;border: none;" src="../gzzx/fwqgzzxlist.action"></iframe>
				    </div>
				    <div class="layui-tab-item" style="height: 100%;">
				    	<iframe frameborder="0" id="gzzxFrameYY" style="width: 100%;height: 100%;border: none;" src="../gzzx/yygzzxlist.action"></iframe>
				    </div>
				    <div class="layui-tab-item">
				    	开发中，敬请期待...
					</div>
				</div>
			</div>
		</div>
</body>
<script type="text/javascript">
	layui.use('element', function(){
		var element = layui.element;
	});
</script>
</html>