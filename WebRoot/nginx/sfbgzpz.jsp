<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>试发布规则配置</title>
		<%@ include file="../include/include.jsp"%>
		<style type="text/css">
			a {
				text-decoration: none;
			}
			.tips {
				width: 700px;
				padding: 10px;
				background: #EAEAEA;
				margin: 0 auto;
				font-size: 12px;
				padding-left: 20px;
			}
			.tips p {
				line-height: 25px;
			}
			
			.showMain {
				width: 700px;
			}
			
			.gzitem {
				width: 660px;
				height: 35px;
				font-size: 14px;
				border: none;
				margin-top: 20px;
			}
			.gzitem input {
				width: 550px;
				height: 35px;
				line-height: 35px;
				font-size: 14px;
				border: none;
				border-bottom: 1px solid #ccc;
				text-align: center;
				color: #7E7C7C;
			}
			.gzitem a {
				width: 100px;
				height: 30px;
				line-height: 30px;
				font-size: 14px;
				color: #03C5FF;
				margin-left: 20px;
			}
			.resultShow {
				display: none;
				overflow-x: auto;
			}
	    </style>
	    
	    <script type="text/javascript">
	    	
	    	//内容item模板
			var content_item_templete = '<div class="result-show-content-item">' +
						'<span class="result-show-content-title">&item-title&</span>' +
						'<span class="result-show-content-status" id="&item-status-id&">&item-status&</span>' +
						'</div>';
	    	
	    	var initPzs = "";	//初始化配置信息（用于校验是否有改变，在页面加载时候给这个赋值）
	    	//添加规则输入框
	    	function addGzitem(aobj) {
	    		//最后一个框没有内容就不让添加
	    		var lastObj = $(".gzitem:last");
	    		var lastVal = $(".gzitem:last").children("input").val();
	    		if (lastVal == null || lastVal == "") {
					return;
				}
	    		
	    		var itemHtml = '<div class="gzitem">' +
	    						'<input type="text" name="regexes" value="" /><a onclick="removeGzItem(this)" href="javascript:void(0)">删除</a>' +
	    						'</div>';
	    		$("#gzShowDiv").append($(itemHtml));
	    	}
	    	
	    	//删除规则输入框
	    	function removeGzItem(aobj) {
	    		//根据后面是否有添加的a标签判断是否只清空信息
	    		var addObj = $(aobj).next();
	    		if (addObj && $(addObj).html()== "添加") {
					//只清空内容
					$(aobj).prev().val("");
					return;
				}
		    	$(aobj).parent().remove();
	    	}
	    	
	    	//保存试发布配置信息
	    	function saveSfbpz() {
	    	
	    		//如果没有改变，就不触发操作
				var pzs = "";
				$(".gzitem").each(function(){
					var val = $(this).children("input").val();
					if (val != null && val != "") {
						pzs = pzs + val + ",";
					}
				});
	    	
	    		if (pzs == initPzs) {
	    			$("#saveBtn").attr("disabled", false);
	    			layer.alert("没有变化");
	    			return;
	    		}
	    	
	    		showBindLayer("resultShow", "", "260px", "保存试发布配置");
	    	
	    		$("#result-show-content").html("");
	    		$("#saveBtn").attr("disabled", true);
				
				$("#result-show-content").append(
					$(
						content_item_templete
							.replace(/&item-title&/g, "nginx更新环境检测：")
							.replace(/&item-status-id&/g, "nginx-update-status")
							.replace(/&item-status&/g, "检测中...")
					)
				);
				
				$.ajax({
					type : "post",
					url : "../nginx/nginxcheckForUpdate.action",
					data : "nginxid="+$("#nginxid").val(),
					dataType : "json",
					success : function(data) {
						$("#result-show-backBtn").bind("click", function(){
							location.reload(true);
						});
						if (data.statusCode == '0000') {
							//检验通过
							$("#nginx-update-status").css("color", "green");
							$("#nginx-update-status").html(data.msg);
							//开始执行保存规则配置的操作
							$.ajax({
								type : "post",
								url : "nginxsaveSfbpz.action",
								data : $("#sfbpzForm").serialize(),
								dataType : "json",
								success : function(data) {
									for(var i = 0;i<data.length;i++) {
										$("#result-show-content").append(
											$(
												content_item_templete
													.replace(/&item-title&/g, data[i].arg1)
													.replace(/&item-status-id&/g, "item-status-id"+i)
													.replace(/&item-status&/g, data[i].msg)
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
						}else {
							//检测失败
							$("#nginx-update-status").css("color", "red");
							$("#nginx-update-status").html(data.msg);
							$("#result-show-content").append(
								$(
									content_item_templete
										.replace("&item-title&", "操作结果：")
										.replace("&item-status-id&", "")
										.replace("&item-status&", "<font color = 'red'>失败！</font>")
								)
							);
						}
					}
				});
	    	}
	    	
	    	//更新初始化的配置信息
	    	function updateInitPzs() {
	    		var pzs = "";
	    		$(".gzitem").each(function(){
	    			var val = $(this).children("input").val();
	    			if (val != null && val != "") {
						pzs = pzs + val + ",";
					}
	    		});
	    		initPzs = pzs;
	    	}
	    	
	    	//保存成功之后处理配置框的展示
	    	function dealPzitem() {
	    		/*
	    		*	判断最后一个输入框是否有内容，如果没有内容就不用处理
	    				如果有，就需要去将该“添加”的a标签删掉，并且重新创建一个带“添加”的输入框内容	
	    		*/
	    		var lastItem = $(".gzitem:last");	//div
	    		var val = lastItem.children("input").val();
	    		
	    		var addObj = lastItem.children("a:last");
	    		if ((val == null || val=="") && addObj && $(addObj).html()== "添加") {
					//这种情况下是不用处理的
					return;
				}
				
				//遍历所有框，将空的删除，将带添加的删除添加
				$(".gzitem").each(function(){
					var val = $(this).children("input").val();
					
					if (val == null || val == "") {
						//为空的删除
						$(this).remove();
						return true;
					}else {
						//判断是否带添加
						var addObj = $(this).children("a:last");
						if (addObj && $(addObj).html()== "添加") {
							//有添加，就把添加删了
							$(addObj).remove();
						}
					}
				});
				
				//完成之后，加上带a的框
	    		var itemHtml = '<div class="gzitem">' +
	    					  '<input type="text" name="regexes" value="" /><a onclick="removeGzItem(this)" ' +
	    					  ' href="javascript:void(0)">删除</a><a onclick="addGzitem(this)" href="javascript:void(0)">添加</a>' +
	    					  '</div>';
				$("#gzShowDiv").append($(itemHtml));
	    	}
	    	
	    </script>
	</head>
	<body>
		<section id="contentMain">
			<h2 class="infoTitle">【${nginxBean.fwqname}】试发布规则配置</h2>
			<div>
				<table class=""  border="0" cellspacing="0" cellpadding="0"> 
			     	<tr>
			     		<td>
			     			<div> 
			               		<div>
			               			<div class="tips">
			               				<p>
			               					<font color="red">*</font> 填写的是可以访问试发布环境的ip信息
			               				</p>
			               				<p>
			               					<font color="red">*</font> 采用正则表达式
			               				</p>
			               				<p>
			               					<font color="red">*</font> 多条任意一条匹配即可访问
			               				</p>
			               				<p>
			               					<font color="red">*</font> 使用的是外网地址（百度IP，出来的地址）
			               				</p>
			               			</div>
			               			<form id="sfbpzForm" action="nginxsaveSfbpz.action" method="post">
			               				<input id="nginxid" type="hidden" name="nginxid" value="${nginxBean.id}" />
				               			<div id="gzShowDiv" class="showMain">
				               				<c:if test="${not empty sfbpzList}">
				               					<c:forEach items="${sfbpzList}" var="pz">
				               						<script type="text/javascript">
				               							initPzs = initPzs + '${pz.regex}' + ',';
				               						</script>
				               						<div class="gzitem">
						               					<input type="text" name="regexes" value="${pz.regex}" /><a onclick="removeGzItem(this)" href="javascript:void(0)">删除</a>
						               				</div>
				               					</c:forEach>
				               				</c:if>
				               				<div class="gzitem">
				               					<input type="text" name="regexes" value="" /><a onclick="removeGzItem(this)" href="javascript:void(0)">删除</a><a onclick="addGzitem(this)" href="javascript:void(0)">添加</a>
				               				</div>
				               			</div>
			               			</form>
			               			
			               			<!-- 返回按钮 -->
								  	<div id="saveDiv" style="padding-bottom: 10px;margin-top: 30px;">
								  		<input type="button" id="saveBtn" class="defaultBtn" onclick="saveSfbpz()" value="确定" />
			                			<input type="button" class="defaultBtn backBtn" onclick="history.back(-1)" value="返回" />
									</div>
			               		</div>
			     			</div>
			     		</td>
			     	</tr>
			     </table>
			</div>
		</section>
		
		<!-- 操作结果显示层 -->
		<div id="resultShow" class="resultShow">
			<div class="result-show-title">
				nginx【<font><span id="show-nginx">${nginxBean.fwqname}</span></font>】执行<font color="#A1D0F6">试发布规则配置</font>操作
			</div>
			
			<div id="result-show-content" class="result-show-content">
			</div>
			
			<div class="result-show-controller">
				<input id="result-show-okBtn" class="defaultBtn" type="button" value="确定" style="display: none;" />
				<input id="result-show-backBtn" class="defaultBtn backBtn" onclick="layer.closeAll('page')" type="button" value="返回" />
			</div>
		</div>
	</body>
</html>

