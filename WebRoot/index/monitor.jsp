<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>监控预览</title>
<%@ include file="../include/include.jsp" %>
<style type="text/css">

	.main-item {
		margin-bottom: 20px;
	}

	.layui-elem-quote {
	    padding: 0 8px;
	    line-height: 28px;
	    border-left: 2px solid #88B7E0;
	}
	.fnitem {
		width: 140px;
	}
	.layui-form-label {
		width: 120px;
	}
	.layui-input-block {
	    margin-left: 150px;
	    min-height: 36px;
    }
    .overview-item {
    	border: 1px solid #eee;
    	text-align: center;
    }
    .overview-item-title {
    	background: #F9F9F9;
    	line-height: 35px;
    }
    .overview-item-content {
    	padding: 5px 0;
    }
    .overview-item-content-count {
    	font-size: 20px;
    	font-family: PingFangSC-Light monospace;
    	line-height: 38px;
    }
    
    .a-link:HOVER {
    	text-decoration: underline;
    }
    
    .servershow-item {
    	padding-left: 0;
    	border: 1px solid #eee;
    }
    .servershow-item-title {
    	background: #f9f9f9;
	    height: 36px;
	    line-height: 36px;
	    padding-left: 10px;
    }
    
    .servershow-item-content {
    	
    }
    
    .servershow-item-content-left {
    	padding-top: 10px;
    }
    
    .line-vertical {
    	float: left;
	    height: 110px;
	    border: 1px dashed #f0f0f5;
    }
    
    .line-horizon {
    	float: left;
    	width: 100%;
	    margin-bottom: 14px;
	    border: 1px dashed #f0f0f5;
    }
    
    .servershow-item-content-left .item {
    	line-height: 40px;
    	font-size: 12px;
    }
    
    .servershow-item-content-left .item span {
    	font-weight: bold;
    }
    
    .servershow-item-content-right {
    	height: 100%;
    	padding-top: 10px;
    }
    
   /*  .servershow-item-content-right ul {
    	display: flex;
    	flex-direction: column;
    	justify-content: center;
    	align-items: center;
    	align-content: center;
    } */
    
    .servershow-item-content-right li {
    	text-align:left;
    	line-height: 25px;
    	padding-left: 16px;
    	color: red;
    }
    
    .servershow-item-content-right li a {
    	color: #5881AF;
    }
    
    .servershow-item-action {
    	border-top: 1px dashed #f0f0f5;
    	line-height: 36px;
    }
    
    .servershow-item-action div:hover {
    	/* background: #f9f9f9; */
    	background: #eee;
    	cursor: pointer;
    }
    
</style>
</head>
<body onload="" style="background:white;min-width: 300px;">
	<section id="contentMain">
		<div>
			<!-- 控制区域 -->
			<div class="main-item">
				<blockquote class="layui-elem-quote"><h5>阈值控制</h5></blockquote>
				<form class="layui-form" action="" >
					<div class="layui-form-item layui-inline" style="margin-bottom: 0">
						<label class="layui-form-label">CPU报警控制阀</label>
						<div class="layui-input-block fnitem">
							<select name="city" lay-verify="required">
								<option value="1">60%</option>
								<option value="2">70%</option>
								<option value="3">80%</option>
								<option value="4">90%</option>
							</select>
						</div>
					</div>
					
					<div class="layui-form-item layui-inline" style="margin-bottom: 0">
						<label class="layui-form-label">内存报警控制阀</label>
						<div class="layui-input-block fnitem">
							<select name="city" lay-verify="required">
								<option value="1">60%</option>
								<option value="2">70%</option>
								<option value="3">80%</option>
								<option value="4">90%</option>
							</select>
						</div>
					</div>
				</form>
			</div>
			
			<!-- 服务器概况 -->
			<div class="main-item">
				<blockquote class="layui-elem-quote"><h5>服务器概况</h5></blockquote>
				<div>
					<div class="layui-row layui-col-space15">
						<div class="layui-col-md6">
							<div class="overview-item">
								<div class="layui-row">
									<h5 class="layui-col-xs12 overview-item-title">
										云平台服务器
									</h5>
								</div>
								<div class="layui-row overview-item-content">
									<div class="layui-col-xs3">
										<div>总数</div>
										<div class="overview-item-content-count" style="color: #09C">
											3
										</div>
									</div>
									<div class="layui-col-xs3">
										<div>正常运行</div>
										<div class="overview-item-content-count" style="color: #090">
											2
										</div>
									</div>
									<div class="layui-col-xs3">
										<a href="javascript:void(0)" class="a-link">
											<div style="color: #0066CC">异常</div>
											<div class="overview-item-content-count" 
												style="color: #F00">1</div>
										</a>
									</div>
									<div class="layui-col-xs3">
										<a href="javascript:void(0)" class="a-link">
											<div style="color: #0066CC">警告</div>
											<div class="overview-item-content-count" 
												style="color: #F00">1</div>
										</a>
									</div>
								</div>
							</div>
						</div>
						<div class="layui-col-md6">
							<div class="overview-item">
								<div class="layui-row">
									<h5 class="layui-col-xs12 overview-item-title">
										托管服务器
									</h5>
								</div>
								<div class="layui-row overview-item-content">
									<div class="layui-col-xs3">
										<div>总数</div>
										<div class="overview-item-content-count" style="color: #09C">
											3
										</div>
									</div>
									<div class="layui-col-xs3">
										<div>正常运行</div>
										<div class="overview-item-content-count" style="color: #090">
											2
										</div>
									</div>
									<div class="layui-col-xs3">
										<a href="javascript:void(0)" class="a-link">
											<div style="color: #0066CC">异常</div>
											<div class="overview-item-content-count" 
												style="color: #F00">1</div>
										</a>
									</div>
									<div class="layui-col-xs3">
										<a href="javascript:void(0)" class="a-link">
											<div style="color: #0066CC">警告</div>
											<div class="overview-item-content-count" 
												style="color: #F00">1</div>
										</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 服务器展示 -->
			<div class="main-item">
				<blockquote class="layui-elem-quote"><h5>服务器展示<span style="margin-left: 10px;font-weight: bolder;">【云平台】</span></h5></blockquote>
				<div>
					<div class="layui-row layui-col-space15">
						<%
							for(int i=0;i<10;i++) {
						 %>
						<div class="layui-col-sm6 layui-col-lg4 layui-col-xs12">
							<div class="overview-item">
								<div class="servershow-item-title">
									服务器名称<%= i+1 %>
								</div>
								<div class="servershow-item-content layui-row">
									<div class="servershow-item-content-left layui-col-sm4 layui-col-xs12">
										<div class="item">
											CPU：<span style="color: green">40%</span>
										</div>	
										<div class="item">
											内存： <span style="color: red">90%</span>
										</div>	
									</div>
									
									<div style="display: none;" class="line-vertical layui-show-sm-inline-block"></div>
									<div class="line-horizon layui-hide-sm"></div>
									
									<div class="servershow-item-content-right layui-col-sm7 layui-col-xs12">
										<ul>
											<c:set var="count" value="5"></c:set>
											<c:if test="${count==1}">
												<li style="color: green">一切正常</li>
											</c:if>
											<c:if test="${count>3}">
												<li>短信发送异常</li>
												<li>短信发送异常</li>
												<li>短信发送异常</li>
												<li><a href="javascript:void(0)">更多>></a></li>
											</c:if>
											<c:if test="${count<=3 && count>1}">
												<li>短信发送异常</li>
												<li>短信发送异常</li>
												<li>短信发送异常</li>
											</c:if>
										</ul>
									</div>
								</div>

								<div class="servershow-item-action layui-row">
									<div class="layui-col-xs6">
										查看详情
									</div>
									<div class="layui-col-xs6">
										应用信息
									</div>
								</div>
							</div>
						</div>
						<%
							}
						 %>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
<script type="text/javascript">
	layui.use('form', function(){
		var form = layui.form;
	});
</script>
</html>
