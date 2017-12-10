<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>关系编辑</title>

		<%@ include file="../include/head.inc"%>
		<script type="text/javascript">
  	var win_search;		//弹出窗口
  	//校验域填写是否完整
  	function checkInput(){
  		$("#insert").attr("disabled", true);
		var err = checkForm(document.forms[0]);
		if(!err){
			$("#insert").attr("disabled", false);
			return false;
		}
		
		if($("#id").val() != null && $("#id").val() !=""){
			document.forms[0].action = "fwgxupdate.action";
			document.forms[0].submit();
		}else{
			if($("input[name=prefwid]").length == 0){
				alert("请选择依赖服务！");
				return false;
			}
			document.forms[0].action = "fwgxsave.action";
			document.forms[0].submit();
		}
		
  	}
  	
  	

    //删除行
 	function del(tableId,obj){
		var table = document.getElementById(tableId);
			var onrow = obj.parentNode.parentNode.rowIndex;
			table.deleteRow(onrow);
 	}
 	
 	//添加依赖关系
 	function addYlgx(){
 		var fwylid = $("#fwylselect").val();
 		if(fwylid == null || fwylid == ""){
 			alert("请选择依赖服务！");
 			return false;
 		}
 		
 		if(isCf(fwylid)){
 			alert("依赖服务已存在！");
 			return false;
 		}
		var fwlxtext = $("#fwylselect").find("option:selected").text(); 
		addTab(fwylid,fwlxtext);
 	}
 	
 	function addTab(fwylid,fwlxtext){
 		var onTab = document.getElementById("fwyltab");
		var new_tr = onTab.insertRow();
		var new_td_1 = new_tr.insertCell();
		var new_td_2 = new_tr.insertCell();
		var new_td_3 = new_tr.insertCell();
		new_td_1.innerHTML = fwylid+"<input type=\"hidden\" name=\"prefwid\" value=\""+fwylid+"\" />";
		new_td_2.innerHTML = fwlxtext+"<input type=\"hidden\" name=\"prefwname\" value=\""+fwlxtext+"\" />";
		new_td_3.innerHTML = "<a href=\"javascript:void(0)\" onclick=\"del('fwyltab',this)\">删除</a>";
 	}
 	
  	
  	//检查是否重复
  	function isCf(ylid){
  		var b = false;
  		$("input[name=prefwid]").each(function(){
			if($(this).val() == ylid){
				b= true;
			}
		});
		return b;
  	}
  	
  	
  	
  	//初始化
  	function init(){
  		  <s:if test="#request.fwgxlist != null">
			<s:iterator id="item" value="#request.fwgxlist" status="idx">
					addTab('${item.prefwid}','${item.prefwname}');
			</s:iterator>
		 </s:if>
  	}
  	</script>
	</head>
	<body onload="init();">

		<div >
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx">
						<b>当前位置：</b> &gt
						<span>服务依赖编辑</span>
					</div>
				</div>
				<!--boxtop pos end-->
				<table class="mb-tm" style="height: auto" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<s:form method="post" action="../zdjh">
								<div class="">
									<div class="j10"></div>
									<div class="boxb indboxt">
										<h3>
											<span class="qylie">服务依赖关系编辑</span>
										</h3>
										<div class="boxb_main boxb_main100">
											<div class="liebtop">
												<ul>
													<li style="text-align: center;">
															
															服务ID：
															<input name="fwBean.id" type="hidden" id="id" value="${fwBean.id}" />
														<input name="fwBean.fwid" type="hidden" id="fwid" value="${fwBean.fwid}" />
														<input name="fwBean.fwname" type="hidden" id="fwname" value="${fwBean.fwname}" />
														${fwBean.fwid}
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														服务名称：${fwBean.fwname}
													</li>
													
													<div class="j10"></div>
											</div>
											<!--top end-->
										</div>
									</div>
									<div class="" style="height: auto">
								
											<table class="mb-table show-td" id="fwyltab"  style="height: auto" border="0" cellspacing="0" cellpadding="0">  
		                                      <tr>
		                                          <td colspan="3" >
		                                          
		                                          	服务依赖设置
		                                          		<s:select name="fwylselect" emptyOption="1"  cssStyle="width:200px" list="#request.fwList" listKey="fwid"  id="fwylselect" listValue="fwname" >
														</s:select>
		                                          	<input type="button" onclick="addYlgx()"  value="添加"/>
		                                          
		                                      </tr>
		                                     <tr>
			                                     <td>
			                                     服务ID
			                                     </td>
			                                     <td>
			                                     服务名称
			                                     </td>
			                                      <td>
			                                     操作
			                                     </td>
		                                     </tr>
		                                    
                                     	</table>

									</div>
									<!--box end-->
									<br />
									<br />
									<div align="center">
										<input type="button" name="insert" value="保存"
											class="tianjiaanniu" onclick="checkInput();" />
										<input type="button" value="返回" class="fanhui"
											onclick="history.back();" />
									</div>
								</div>
							</s:form>
						</td>
					</tr>
				</table>
				<!--mbox end-->
			</div>
		</div>
	</body>
</html>

