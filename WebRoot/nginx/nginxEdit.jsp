<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Nginx服务器编辑</title>
		<%@ include file="../include/include.jsp"%>
		<script type="text/javascript">
		
			$(function(){
				//初始化加载该nginx所管理的集群信息
				var nid = $("#nginxid").val();
				if (nid != null && nid != "") {
					<c:forEach items="${jqNginxList}" var="jqItem" varStatus="status">
						$("#jqxx"+${jqItem.id}).attr("checked",true);
					</c:forEach>
				}
			});
		
		  	//校验域填写是否完整
		  	function save(){
		  		$("#insert").attr("disabled", true);
				var err = checkForm(document.getElementById("nginxFormId"));
				if(!err){
					$("#insert").attr("disabled", false);
					return false;
				}
				
				//校验模板文件（可以不上传， 但是上传的话对后缀有要求）
				var filename = $("#myFile").val();
				if (filename == null || filename == "") {
					
				} else {
					//校验上传文件的后缀
		     		var suffix = filename.substring(filename.lastIndexOf("."));
		     		if (suffix != ".conf") {
		     			layer.alert("文件格式不正确，上传失败！请上传.conf后缀的文件！");
						$("#insert").attr("disabled", false);
						return false;
					}
				}
				
				var nginxid = $("#nginxid").val();
				if(nginxid == null || nginxid == ""){
					//新增服务器
					var fwqip = $("#fwqip").val();
					if(!checkIP(fwqip)){
						$("#insert").attr("disabled", false);
						return false;
					}
					// 检测服务器并且提交表单
					ajax_checkfwq(fwqip);
				}else{
					//修改服务器
					document.getElementById("nginxFormId").action="nginxupdate.action";
					document.getElementById("nginxFormId").submit();
				}
			}
			
			//检测中
			function checkIn(){
				$("#waitdiv").show();
				$("#savediv").hide();
			}
			
			//检测完成
			function checkOut(){
				$("#waitdiv").hide();
				$("#savediv").show();
			}
			
			// 检测服务器并且提交表单
			function ajax_checkfwq(fwqip){
				var load_index = layer.load();
				$.ajax({
			   		type: "POST",
			   		url: "nginxcheckfwq.action",
			   		dataType:"json",
			   		data:"fwqip="+fwqip,
					success: function(data){
						layer.close(load_index);
						if(data.statusCode == "0000"){
							// 说明校验成功,提交保存服务器的表单
							//将服务器操作系统值赋值给隐藏域
							$("#fwqczxt").val(data.arg1);
							document.getElementById("nginxFormId").action="nginxsave.action";
							document.getElementById("nginxFormId").submit();
						}else{
							// 校验失败
							layer.alert(data.msg);
						}
					},
					error: function(){
						layer.close(load_index);
						layer.alert("保存失败！");
					}
				 });
			}
			
			function checkIP(ip){ 
				var exp=/([0-9]{1,3}\.{1}){3}[0-9]{1,3}/;
				if(exp.test(ip)){     
		       		if( RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)   
		       		return true;     
		  		}     
				layer.alert("IP地址不合法!"); 
				return false;
			} 
  		</script>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">Nginx服务器编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="nginxFormId" enctype="multipart/form-data"
              		method="post" id="nginxFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input name="nginxBean.id" type="hidden" id="nginxid" value="${nginxBean.id}" />
				<input name="nginxBean.fwqczxt" type="hidden" id="fwqczxt" />
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>服务器名称：</label>
			            <div class="labelRight">
			            	<input type="text" name="nginxBean.fwqname" id="fwqname"
								value="${nginxBean.fwqname}" class="inputText"
								size="30" title="服务器名称"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>nginx所在目录：</label>
			            <div class="labelRight">
			            	<input type="text" name="nginxBean.nginxsrc" id="nginxsrc" value="${nginxBean.nginxsrc}"
								class="inputText" size="30"
								title="nginx所在目录" alt="notnull;length<=50;" maxlength="50"
								size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>所在服务器ip：</label>
			            <div class="labelRight">
			            	<c:if test="${empty nginxBean.id}">
								<input type="text" name="nginxBean.fwqip" id="fwqip" value="${nginxBean.fwqip}"
									class="inputText" size="30"
									title="服务器ip" alt="notnull;length<=30;" maxlength="30"
									size="30" />
							</c:if>
							<c:if test="${not empty nginxBean.id}">
								<input type="text" disabled="disabled" name="nginxBean.fwqip" id="fwqip" value="${nginxBean.fwqip}"
									class="inputText readonly" size="30"
									title="服务器ip" alt="notnull;length<=30;" maxlength="30"
									size="30" />
							</c:if>
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>Nginx模板文件：</label>
			            <div class="labelRight">
			            	<input type="file" name="myFile" class="" id="myFile" 
			            		style="width: 265px;padding: 0;margin: 0;height: 22px;border: none;" title="Nginx模板"  size="30" />
			            </div>
			        </li>
			        
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">集群管理：</label>
			            <div class="labelRight">
			            	<c:if test="${empty jqList}">暂无可用集群</c:if>
							<c:if test="${not empty jqList}">
								<table class="api_table" border="0" cellspacing="0" cellpadding="0" style="width: 260px;">
									<colgroup style="width: 10%" />
									<colgroup style="width: 90%" />
									<c:forEach var="jq" items="${jqList}">
										<tr>
											<td><input id="jqxx${jq.id}" type="checkbox" name="jqxx" value="${jq.id}" /></td>
											<td><label for="jqxx${jq.id}">${jq.jqname}</label></td>
										</tr>
									</c:forEach>
								</table>
							</c:if>
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
</body>
</html>

