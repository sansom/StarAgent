<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>应用编辑</title>

		<%@ include file="../include/head.inc"%>
		<script type="text/javascript" src="../common/jquery.form.min.js"></script>
		<script type="text/javascript">
		  	var win_search;		//弹出窗口
		  	//校验域填写是否完整
		  	function checkInput(){
		  		$("#insert").attr("disabled", true);
		  		
		  		//1.验证form包
				var err = checkForm(document.forms["yyForm"]);
				if(!err){
					$("#insert").attr("disabled", false);
					return false;
				}
				
				//2.验证war包
				var yyid = $("#yyid").val();
				var warname = $("#myFile").val();
				if((yyid == "" || yyid == null) && (warname == null || warname == "")){
					alert("请选择war包！");
					return false;
				}
				ajax_submit();
			}
			
			//保存中DIV层
			function showWaitDiv(boo){
				if(boo){
					$("#waitdiv").show();
					$("#savediv").hide();
				}else{
					$("#waitdiv").hide();
					$("#savediv").show();
				}
			}

			//设置日志文件地址
			function setLogSrc(obj){
				var filename = $(obj).val();
				if(filename != ""){
					var warName = filename.substring(filename.lastIndexOf("\\"), filename.lastIndexOf(".")).replace("\\","");
					$("#yyLogSrc").val("WEB-INF/"+warName+".log");
				}
			}
			
			//ajax提交
			function ajax_submit(){
		
				
				var yyid = $("#yyid").val();
				var url = "";
				if(yyid != null && yyid !=""){
					url = "yyupdate.action";
				}else{
					url = "yysave.action";
				}
	
				//显示等待层
				showWaitDiv(true);
				$("#yyForm").ajaxSubmit({
		       		type: "post", 
		            url: url,
		            enctype:"multipart/form-data",
		            async: true,
		            dataType: "json",
		            success: function(data) {
			            if (data.statusCode == '0000') {
			            	alert("保存成功");
			            	toListPage();
			          	}else{
			          		alert(data.msg);
			          	}
			            showWaitDiv(false);
		        	}
		      	});
			}
			
			
			
			//是否是正确的WAR包
			function isTrulyWAR(filename){
				var warname = $("#warname").val();
				if(filename != null && warname != null && filename != "" && warname != ""){
					filename = filename.substring(filename.lastIndexOf("\\"), filename.length).replace("\\","");
					
					if(warname != filename){
						alert("您选择的WAR包有误，该应用WAR包为【"+warname+"】，请重新选择！");
						return false;
					}
				}
				return true;
				
			}
			
			function toListPage (){
				var ptlx = $("#ptlx").val();
				if(ptlx == '<%=SystemConstants.PTLX_YUN %>'){
					window.location = "../yygl/yylist.action";
				}else if(ptlx == '<%=SystemConstants.PTLX_TG %>'){
					window.location = "../yygl/tgyylist.action";
				}
				
			}
  		</script>
	</head>
	<body>

		<div class="bmain-round">
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx">
						<b>当前位置：</b> &gt
						<span>应用编辑</span>
					</div>
				</div>
				<form action="" id="listForm" name="listForm" method="post">
					<input type="hidden" id="queryparamter" name="queryparamter" value="" />
					<input type="hidden" id="queryparamtername" name="queryparamtername" value="" />
					
				</form>
				<!--boxtop pos end-->
				<table class="mb-tm" border="0" cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<td>
							<form method="post" name="yyForm" id="yyForm" action="../zdjh" enctype="multipart/form-data">
								<input type="hidden" name="yyBean.ptlx" id="ptlx" value="${yyBean.ptlx}"/>
								<div class="min400">
									<div class="j10"></div>
									<div class="boxb indboxt">
										<h3>
											<span class="qylie">应用编辑</span>
										</h3>
										<div class="boxb_main boxb_main100">
											<input name="yyBean.id" type="hidden" id="yyid" value="${yyBean.id}" />
											<input name="yyBean.warname" type="hidden" id="warname" value="${yyBean.warname}" />
											<table class="boxb_main" cellpadding="10" cellspacing="10" style="margin: 0 auto; width: 100%">
												<colgroup width="40%"></colgroup>
												<colgroup width="50%"></colgroup>
												<tr>
													<td class="tb_left">应用名称：</td>
													<td class="tb_right">
														
														<input type="text" name="yyBean.yyname" id="yyname" class="jqInp wdateInp" size="30" style="width: 265px" title="应用名称" alt="notnull;length<=50;" maxlength="50" size="30" value="${yyBean.yyname}"  />
													</td>
												</tr>
												
												<tr>
													<td class="tb_left">线上版本号：</td>
													<td class="tb_right">
														<c:choose>
															<c:when test="${not empty yyBean.yyversion}">
																<font style="font-weight: bold;">${yyBean.yyversion}</font>
															</c:when>
															<c:otherwise>
																--
															</c:otherwise>
														</c:choose>
														
														
													</td>
												</tr>
												<tr>
													<td class="tb_left">试发布版本号：</td>
													<td class="tb_right">
														<c:choose>
															<c:when test="${not empty  yyBean.yyversion_sfb}">
																<font color="#FF9900" style="font-weight: bold;">${yyBean.yyversion_sfb}</font>
															</c:when>
															<c:otherwise>
																--
															</c:otherwise>
														</c:choose>
														
														
													</td>
												</tr>
												
												
												<tr>
												<td colspan="2" height="2px">
												<hr size="1" style="color: blue;width:100%">
												</td>
												</tr>
												
												
												<tr>
													<td class="tb_left">试发布WAR包：</td>
													<td class="tb_right" valign="top">
														<!-- 设置隐藏域编辑应用时候判断是否上传了war包 -->
													
														<input type="file" name="myFile" onblur="isTrulyWAR(this.value);setLogSrc(this);"  class="jqInp wdateInp" id="myFile" style="width: 265px;" title="war包"  size="30" />&nbsp;&nbsp;

													</td>
												</tr>
												<tr>
													<td class="tb_left">日志路径：</td>
													<td class="tb_right">
														<input type="text" name="yyBean.yyLogSrc" class="jqInp wdateInp" id="yyLogSrc" style="width: 265px;" title="日志"  size="30" value="${empty yyBean.yyLogSrc ? 'WEB-INF/war.log' : yyBean.yyLogSrc }" />
													</td>
												</tr>

											</table>
											
											<!--top end-->
										</div>
									</div>
									<!--box end-->
									<br />
									<br />
									<div  id="savediv" align="center">
										<input type="button" name="insert" value="保存"
											class="tianjiaanniu" onclick="checkInput();" />
										<input type="button" value="返回" class="fanhui"
											onclick="history.back()" />
									</div>
									<div id="waitdiv" style="display: none" align="center">
										<img class="checkwar" style="width: 13px;height: 13px;" src="../images/wait.gif" />
										正在保存应用信息,请稍等...
									</div>
								</div>
							</form>
						</td>
					</tr>
				</table>
				<!--mbox end-->
			</div>
		</div>
	</body>
</html>

