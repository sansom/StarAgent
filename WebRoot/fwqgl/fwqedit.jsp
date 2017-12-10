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
		<title>服务器编辑</title>
		<%@ include file="../include/include.jsp" %>
		<script type="text/javascript">
		
			var isTg = '${fwqBean.ptlx}' == '01' ? false : true;
		
		  	//保存
		  	function save(){
		  		
		  		$("#insert").attr("disabled", true);
		  		//1.校验表单
				var err = checkForm(document.forms["fwqFormId"]);
				if(!err){
					$("#insert").attr("disabled", false);
					return false;
				}
				var fwqid = $("#fwqid").val();
				
				//2.校验IP地址正确性（新增）
				if((fwqid == null || fwqid == "") && !checkIP()){
					$("#insert").attr("disabled", false);
					return false;
				}
				
				//3、校验联系电话合法性
				if (!checkLxdh()) {
					$("#insert").attr("disabled", false);
					return false;
				}
				
				//采用这种方式可以显示服务器检测中的状态，否则出不来效果
				if(fwqid == null || fwqid == ""){
					// 检测服务器并且提交表单
					ajax_checkfwqzt();
				}else{
					document.getElementById("fwqFormId").action="fwqupdate.action";
					document.getElementById("fwqFormId").submit();
				}
			}
			
			//检测服务器状态
			function ajax_checkfwqzt(){
			
				var load_index = layer.load();
				//showFwqDiv("in");
				var fwqip = $("#fwqip").val();
				if (isTg) {
					fwqip = $("#fwqip_ww").val();
				}
				
				var re = false;
				$.ajax({
			   		type: "POST",
			   		url: "fwqcheckfwq.action",
			   		dataType:"json",
			   		data:"fwqip="+fwqip,
					success: function(data){
						layer.close(load_index);
						if(data.statusCode == "0000"){
							initFwqData(data.msg);
							document.forms["fwqFormId"].action = "fwqsave.action";
							document.forms["fwqFormId"].submit();
						}else{
							//校验失败
							$("#insert").attr("disabled", false);
							layer.alert(data.msg);
						}
					}
				 });
			}
			
			//校验联系电话
			function checkLxdh() {
				var dylxrdh = $("#dylxrdh").val();
				var delxrdh = $("#delxrdh").val();
				if (dylxrdh != null && dylxrdh != "") {
					//可以不输入，但是输入就要校验
					var myreg = /^1(3|4|5|7|8)\d{9}$/;
					if (!myreg.test(dylxrdh)) {
						//不正确
						alert("第一联系人电话格式不正确！");
						$("#dylxrdh").focus();
						return false;
					}
				}
				if (delxrdh != null && delxrdh != "") {
					//可以不输入，但是输入就要校验
					var myreg = /^1(3|4|5|7|8)\d{9}$/;
					if (!myreg.test(delxrdh)) {
						//不正确
						alert("第二联系人电话格式不正确！");
						$("#delxrdh").focus();
						return false;
					}
				}
				return true;
			}
			
			//校验IP地址
			function checkIP(){
				var exp=/([0-9]{1,3}\.{1}){3}[0-9]{1,3}/;
				var fwqip = $("#fwqip").val();
				var fwqip_ww = $("#fwqip_ww").val();
				
				if (isTg) {
					//托管服务器，内网可以为空，但如果输入就必须校验正则
					if (fwqip_ww == null || fwqip_ww == "") {
						alert("外网不能为空！");
						return false;
					}else {
						if(exp.test(fwqip_ww)){     
				       		if(!(RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)) {
				       			alert("外网ip非法！");
				       			return false;
				       		}
				  		}else {
							alert("外网ip非法！");
							return false;
						}
					}
					
					if (fwqip != null && fwqip != "") {
						if(exp.test(fwqip)){     
				       		if(!(RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)) {
				       			alert("内网ip非法！");
				       			return false;
				       		}
				  		}
					}
					return true;
				}else {
					//云平台服务器，外网可以为空，但如果输入就必须校验正则
					if (fwqip == null || fwqip == "") {
						alert("内网不能为空！");
						return false;
					}else {
						if(exp.test(fwqip)){     
				       		if(!(RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)) {
				       			alert("内网ip非法！");
				       			return false;
				       		}
				  		}else {
							alert("内网ip非法！");
							return false;
						}
					}
					
					if (fwqip_ww != null && fwqip_ww != "") {
						if(exp.test(fwqip_ww)){     
				       		if(!(RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)) {
				       			alert("外网ip非法！");
				       			return false;
				       		}
				  		}
					}
					return true;
				}
				/* var exp=/([0-9]{1,3}\.{1}){3}[0-9]{1,3}/;
				if(exp.test(fwqip)){     
		       		if( RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)   
		       		return true;     
		  		}
				alert("IP地址不合法!"); 
				return false; */
			} 
			
			//初始化服务器动态信息
			function initFwqData(fwqInfo){
				$("#fwqczxt").val(fwqInfo.fwqczxt);
				$("#backupsrc").val(fwqInfo.backupsrc);
				$("#cpuused").val(fwqInfo.cpuused);
				$("#memeryused").val(fwqInfo.memeryused);
				$("#threadcount").val(fwqInfo.threadcount);
				$("#jvmmemory").val(fwqInfo.jvmmemory);
				$("#jvmthreadcount").val(fwqInfo.jvmthreadcount);
				$("#jvmloadedclasscount").val(fwqInfo.jvmloadedclasscount);
				$("#fwqstatus").val("<%=SystemConstants.FWQSTATUS_ZC%>");
				$("#updatetime").val("<%=DateUtils.getCurrentDataTime()%>");         
			}
			
			//初始化
			function init(){
				<c:if test="${fwqBean != null && fwqBean.fwqip != null}">
					$("#dynamicData").show();
					<c:if test="${fwqBean.fwqczxt!= ''}">
						$("#text_fwqczxt").val("<jadlbean:write name='fwqBean' property='fwqczxt'/>");
					</c:if>
				
					<c:if test="${fwqBean.fwqstatus != ''}">
					
						$("#text_fwqstatus").val("<jadlbean:write name='fwqBean' property='fwqstatus' />");
					</c:if>
				</c:if>
				
				//集群信息
				/* <c:if test="${not empty jqFwqJqList}">
					$("#ssjq").val("${jqFwqJqList[0].id}");		//当前就只允许有一个集群信息
				</c:if> */
			
			}
  		</script>
	</head>
<body onload="init()">

	<c:set value="<%= SystemConstants.PTLX_YUN %>" var="ptlx_yun" />
	<c:set value="<%= SystemConstants.PTLX_TG %>" var="ptlx_tg" />

	<section id="contentMain">
		<h2 class="infoTitle">服务器编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="fwqFormId"
              		method="post" id="fwqFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input type="hidden" name="fwqBean.ptlx" id="ptlx" value="${fwqBean.ptlx}"/>
              	<input type="hidden" name="fwqBean.id" id="fwqid" value="${fwqBean.id}"/>
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="itemTitle">基本信息编辑</li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>所在机房：</label>
			            <div class="labelRight">
			                <input type="text" name="fwqBean.szjf" id="szjf" class="inputText" 
			                	title="所在机房" alt="notnull;length<=50" maxlength="50" value="${fwqBean.szjf}">
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>服务器名称：</label>
			            <div class="labelRight">
			            	<input type="text" name="fwqBean.fwqname" id="fwqname"
								value="${fwqBean.fwqname}" class="inputText"
								size="30" title="服务器名称"
								alt="notnull;length<=50;" maxlength="50" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">
							<c:if test="${fwqBean.ptlx == ptlx_yun}">
			            		<span class="required">*</span>
			            	</c:if>
			            		服务器IP（内网）：
			            </label>
			            <div class="labelRight">
			            	<!-- 如果为云平台，使用内网， -->
							<c:if test="${fwqBean.ptlx == ptlx_yun}">
								<!-- 为添加界面 -->
								<c:if test="${fwqBean.id == null || fwqBean.id == ''}">
									<input type="text" name="fwqBean.fwqip" id="fwqip" value="${fwqBean.fwqip}"
										class="inputText" size="30" 
										title="服务器IP(内网)" alt="notnull;length<=16;" maxlength="16"
										size="30" />
								</c:if>
								<!-- 为编辑界面 -->
								<c:if test="${fwqBean.id != null && fwqBean.id != ''}">
									<input type="text" name="fwqBean.fwqip" readonly="readonly" id="fwqip" value="${fwqBean.fwqip}"
										class="inputText readonly" size="30" 
										title="服务器IP(内网)" alt="notnull;length<=16;" maxlength="16"
										size="30" />
								</c:if>
							</c:if>
							<!-- 如果为托管，使用外网， -->
							<c:if test="${fwqBean.ptlx != ptlx_yun}">
								<c:if test="${fwqBean.id == null || fwqBean.id == ''}">
									<input type="text" name="fwqBean.fwqip" id="fwqip" value="${fwqBean.fwqip}"
										class="inputText" size="30"
										title="服务器IP(内网)" maxlength="16"
										size="30" />
								</c:if>
							</c:if>
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">
							<c:if test="${fwqBean.ptlx == ptlx_tg}">
			            		<span class="required">*</span>
			            	</c:if>
			            		服务器IP（外网）：
			            </label>
			            <div class="labelRight">
			            	<!-- 如果为云平台，使用内网， -->
							<c:if test="${fwqBean.ptlx != ptlx_tg}">
								<!-- 为添加界面 -->
								<c:if test="${fwqBean.id == null || fwqBean.id == ''}">
									<input type="text" name="fwqBean.fwqip_ww"  id="fwqip_ww" value="${fwqBean.fwqip_ww}"
										class="inputText" size="30"
										title="服务器IP(外网)"  maxlength="16"
										size="30" />
								</c:if>
								<!-- 为编辑界面 -->
								<c:if test="${fwqBean.id != null && fwqBean.id != ''}">
									<input type="text" name="fwqBean.fwqip_ww" id="fwqip_ww" value="${fwqBean.fwqip_ww}"
										class="inputText" size="30" 
										title="服务器IP(外网)" maxlength="16"
										size="30" />
								</c:if>
							</c:if>
							<!-- 如果为托管，使用外网， -->
							<c:if test="${fwqBean.ptlx == ptlx_tg}">
								<!-- 为添加界面 -->
								<c:if test="${fwqBean.id == null || fwqBean.id == ''}">
									<input type="text" name="fwqBean.fwqip_ww"  id="fwqip_ww" value="${fwqBean.fwqip_ww}"
										class="inputText" size="30"
										title="服务器IP(外网)" alt="notnull;length<=16;"  maxlength="16"
										size="30" />
								</c:if>
								<!-- 为编辑界面 -->
								<c:if test="${fwqBean.id != null && fwqBean.id != ''}">
									<input type="text" name="fwqBean.fwqip_ww" readonly="readonly" 
										id="fwqip_ww" value="${fwqBean.fwqip_ww}"
										class="inputText readonly" size="30"
										title="服务器IP(外网)" alt="notnull;length<=16;" maxlength="16"
										size="30" />
								</c:if>
							</c:if>
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>Tomcat端口：</label>
			            <div class="labelRight">
			            	<input type="text" name="fwqBean.dk" id="dk" value="${fwqBean.dk}"
								class="inputText" size="30"
								title="Tomcat端口" alt="notnull;length<=6;" maxlength="6"
								size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">第一联系人：</label>
			            <div class="labelRight">
			            	<input type="text" name="fwqBean.dylxr" id="dylxr" value="${fwqBean.dylxr}"
								class="inputText" size="30" 
								title="第一联系人" alt="length<=20;" maxlength="20"
								size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">第一联系人电话：</label>
			            <div class="labelRight">
			            	<input type="text" name="fwqBean.dylxrdh" id="dylxrdh" value="${fwqBean.dylxrdh}"
								class="inputText" size="30" 
								title="第一联系人电话" alt="length<=15;" maxlength="15"
								size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">第二联系人：</label>
			            <div class="labelRight">
			            	<input type="text" name="fwqBean.delxr" id="delxr" value="${fwqBean.delxr}"
								class="inputText" size="30"
								title="第二联系人" alt="length<=20;" maxlength="20"
								size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">第二联系人电话</label>
			            <div class="labelRight">
			            	<input type="text" name="fwqBean.delxrdh" id="delxrdh" value="${fwqBean.delxrdh}"
								class="inputText" size="30"
								title="第二联系人电话" alt="length<=15;" maxlength="15"
								size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <div style="display: none;" id="dynamicData">
				        <li class="itemTitle">
				        	动态数据（<span style="font-size: 14px;">数据更新时间:&nbsp;<font style="color: green">${fwqBean.updatetime}</font>&nbsp;</span>）
							<input type="hidden" style="width: 250px;background: #e0e0e0;" name="fwqBean.updatetime" readonly="readonly" id="updatetime" value="${fwqBean.updatetime}" />
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">服务器操作系统：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_fwqczxt" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.fwqczxt" readonly="readonly" id="fwqczxt" value="${fwqBean.fwqczxt}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">服务器状态：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_fwqstatus" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.fwqstatus" readonly="readonly" id="fwqstatus" value="${fwqBean.fwqstatus}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">war包备份地址：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_backupsrc" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.backupsrc" readonly="readonly" id="backupsrc" value="${fwqBean.backupsrc}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">CPU使用率：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_cpuused" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.cpuused" readonly="readonly" id="cpuused" value="${fwqBean.cpuused}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">内存使用率：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_memeryused" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.memeryused" readonly="readonly" id="memeryused" value="${fwqBean.memeryused}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">线程数量：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_threadcount" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.threadcount" readonly="readonly" id="threadcount" value="${fwqBean.threadcount}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">JVM可用堆内存：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_jvmmemory" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.jvmmemory" readonly="readonly" id="jvmmemory" value="${fwqBean.jvmmemory}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">JVM运行线程数：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_jvmthreadcount" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.jvmthreadcount" readonly="readonly" id="jvmthreadcount" value="${fwqBean.jvmthreadcount}" />
				            </div>
				        </li>
				        
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">JVM加载的类数量：</label>
				            <div class="labelRight">
				            	<input type="text" readonly="readonly" 
				            		id="text_jvmloadedclasscount" value="" class="inputText readonly" />
				            	<input type="hidden" name="fwqBean.jvmloadedclasscount" readonly="readonly" id="jvmloadedclasscount" value="${fwqBean.jvmloadedclasscount}" />
				            </div>
				        </li>
			        </div>
			        
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

