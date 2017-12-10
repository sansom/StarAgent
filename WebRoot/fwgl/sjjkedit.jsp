<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>接口编辑</title>

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
		var jkid = $("#jkid").val();
		var uri = $("#uri").val();
		var lylx = $('#lylx option:selected').val();
		var jklx = $('#jklx option:selected').val();
		if(!isZm(jkid)){
			alert("接口ID只能由字母组成！");
			return false;
		}
		if(jkid.length > 19 || jkid.length < 6){
			alert("接口ID长度为6~19个字符！");
			return false;
		}
		if(lylx == '01'){
			if($("#yyid").val().length == 0){
				alert("请选择所属应用！");
				return false;
			}
		}
		if(!uri.startWith("h")){
			alert("路径不符！");
			return false;
		}
		if(jklx == '1'){
			if($("#ffname").val().length == 0){
				alert("方法名称不能为空！");
				return false;
			}
		}

		if($("#id").val() != null && $("#id").val() !=""){
			document.forms[0].action = "sjjkupdate.action";
			document.forms[0].submit();
		}else{
			document.forms[0].action = "sjjksave.action";
			document.forms[0].submit();
		}
		
  	}
  	
  	//校验是否是字母
  	function isZm(str){
  		 var Regx = /^[A-Za-z]*$/;
          if (Regx.test(str)) {
              return true;
          }
		return false;
  	}
  	

  	//选择所属应用
    function selectSsyy(){
   		var conditions = "";
   		openSeachWindow("ssyy",conditions,"yyid,yyname","yyid,yyname","","");
    }
    
    //删除行
 	function del(tableId,obj){
		var table = document.getElementById(tableId);
			var onrow = obj.parentNode.parentNode.rowIndex;
			table.deleteRow(onrow);
 	}
 	
 	//增加入参
 	function addRc(param,paramname,paramlx){
		var onTab = document.getElementById("jkrctab");
		var new_tr = onTab.insertRow();
		var new_td_1 = new_tr.insertCell();
		var new_td_2 = new_tr.insertCell();
		var new_td_3 = new_tr.insertCell();
		var new_td_4 = new_tr.insertCell();
		new_td_1.innerHTML = "paramIn<input type=\"hidden\" name=\"paramlx\" value=\""+paramlx+"\" />";
		new_td_2.innerHTML = "参数:<input type=\"text\" class=\"jqInp wdateInp\" value=\""+param+"\" name=\"param\" title=\"参数\" alt=\"notnull;length<=20;\" style=\"width: 150px\"/>";
		new_td_3.innerHTML = "中文名:<input type=\"text\" class=\"jqInp wdateInp\"  value=\""+paramname+"\" name=\"paramname\" title=\"中文名\" alt=\"notnull;length<=20;\" style=\"width: 150px\"/>";
		new_td_4.innerHTML = "<a href=\"javascript:void(0)\" onclick=\"del('jkrctab',this)\">删除</a>";
	
 	}
 	//增加出参
 	function addCc(param,paramname,paramlx){
 		var onTab = document.getElementById("jkcctab");
		var new_tr = onTab.insertRow();
		
		var new_td_1 = new_tr.insertCell();
		var new_td_2 = new_tr.insertCell();
		var new_td_3 = new_tr.insertCell();
		var new_td_4 = new_tr.insertCell();
		new_td_1.innerHTML = "paramOut<input type=\"hidden\" name=\"paramlx\" value=\""+paramlx+"\" />";
		new_td_2.innerHTML = "参数:<input type=\"text\" class=\"jqInp wdateInp\" value=\""+param+"\" name=\"param\" title=\"参数\" alt=\"notnull;length<=20;\" style=\"width: 150px\"/>";
		new_td_3.innerHTML = "中文名:<input type=\"text\" class=\"jqInp wdateInp\" value=\""+paramname+"\" name=\"paramname\" title=\"中文名\" alt=\"notnull;length<=20;\" style=\"width: 150px\"/>";
		new_td_4.innerHTML = "<a href=\"javascript:void(0)\" onclick=\"del('jkcctab',this)\">删除</a>";
	
 	}
  	
  	//初始化
  	function init(){
  		  <s:if test="#request.paramlist != null">
			<s:iterator id="item" value="#request.paramlist" status="idx">
					<c:if test="${item.paramlx == 1}">
						addCc('${item.param}','${item.paramname}','${item.paramlx}');
					</c:if>
					<c:if test="${item.paramlx == 0}">
						addRc('${item.param}','${item.paramname}','${item.paramlx}');
					</c:if>
			</s:iterator>
		 </s:if>
  	}
	//变换路由类型
  	function changeLylx(){
		var lylx = $('#lylx option:selected').val();
		if(lylx == '01'){
			$("#zxly").show();	
			$("#uriSm").show();
		}else if(lylx=='02'){
			$("#zxly").hide();	
			$("#uriSm").hide();
		}
  	 }
 	 //校验接口ID是否已存在
 	 function checkJkid(){
 		var jkid = $("#jkid").val();
 		$.ajax({
  		  type: "POST",
	   		  url: "sjjkcheckJkid.action",
	   		  dataType:"text",
	   		  async:true,
	   		  data:"sjjkBean.jkid="+jkid,
			  dataType:"text",
			  success: function(msg){
	  				if(msg == "success"){
	  				}else{
	  					alert("接口ID已存在！");
	  					$("#jkid").val("");
	  					return false;
	  				}
			  }
		});
 	 }
  	</script>
	</head>
	<body onload="init();">

		<div >
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx">
						<b>当前位置：</b> &gt
						<span>接口编辑</span>
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
											<span class="qylie">接口编辑</span>
										</h3>
										<div class="boxb_main boxb_main100">
											<div class="liebtop">
												<ul>
													<li>
														<label>
															接口ID：
														</label>
														<input name="sjjkBean.id" type="hidden" id="id" value="${sjjkBean.id}" />
														<c:if test="${sjjkBean.id == null || sjjkBean.id == ''}">
														<input type="text" name="sjjkBean.jkid" id="jkid" 
															value="${sjjkBean.jkid}" class="jqInp wdateInp" size="30"
															style="width: 250px" title="接口ID"
															alt="notnull;length<=30;" maxlength="30" size="30" onchange="checkJkid()"/>
														</c:if>
														<c:if test="${sjjkBean.id != null && sjjkBean.id != ''}">
														<input type="text" name="sjjkBean.jkid" id="jkid" readonly="readonly" style="width: 250px;background:#f4f4f4" 
															value="${sjjkBean.jkid}" class="jqInp wdateInp" size="30"
															style="width: 250px" title="接口ID"
															alt="notnull;length<=30;" maxlength="30" size="30" />
														</c:if>
														
															&nbsp; <font color="green">*&nbsp;接口ID为6-19个字母组成，添加成功后不可更改。*</font>
													<div class="j10"></div>
													</li>
													<li>
														<label>
															接口名称：
														</label>
														
														<input type="text" name="sjjkBean.jkname" id="jkname"
															value="${sjjkBean.jkname}" class="jqInp wdateInp"
															style="width: 250px" title="接口名称"
															alt="notnull;length<=50;" maxlength="50" size="30" />
														
													<div class="j10"></div> 
													</li>
													<li>
														<label>
															路由类型：
														</label>
														<select name="sjjkBean.lylx" id="lylx" class="jqInp wdateInp" style="width: 265px" title="接口类型" onchange="changeLylx()">
															<option value="01"  <c:if test="${'01' == sjjkBean.lylx}">selected</c:if>>中心路由</option>
															<option value="02"  <c:if test="${'02' == sjjkBean.lylx}">selected</c:if>>自由路由</option>
															</select>
													<div class="j10"></div>
													</li>
													<li>
														<label>
															服务分类：
														</label>
														
														<select name="sjjkBean.fwfl" id="fwfl" title="服务分类"  alt="notnull;" style="width: 265px">
															<option value=""></option>
															<option value="01" <c:if test="${'01' == sjjkBean.fwfl}">selected</c:if>>爆破作业管理服务</option>
															<option value="02" <c:if test="${'02' == sjjkBean.fwfl}">selected</c:if>>库房管理服务</option>
															<option value="03" <c:if test="${'03' == sjjkBean.fwfl}">selected</c:if>>运输车辆管理服务</option>
															<option value="04" <c:if test="${'04' == sjjkBean.fwfl}">selected</c:if>>人员信息服务</option>
															<option value="05" <c:if test="${'05' == sjjkBean.fwfl}">selected</c:if>>数据分析服务</option>
															<option value="06" <c:if test="${'06' == sjjkBean.fwfl}">selected</c:if>>人员管理服务</option>
															<option value="07" <c:if test="${'07' == sjjkBean.fwfl}">selected</c:if>>爆破作业监理服务</option>
															<option value="08" <c:if test="${'08' == sjjkBean.fwfl}">selected</c:if>>出入库服务</option>
															<option value="99" <c:if test="${'99' == sjjkBean.fwfl}">selected</c:if>>其他</option>
														</select>
													<div class="j10"></div>	
													</li>
													<div id="zxly" name="zxly" <c:if test="${'02' == sjjkBean.lylx}">style="display:none"</c:if>>
														<li>
															<label>
																所属应用：
															</label>
															<input type="hidden" name="sjjkBean.yyid" value="${sjjkBean.yyid}" id="yyid" /> 
	                                        				<input type="text" class="input1_250_bg" readonly="readonly" title="所属应用"  style="width: 250px" value="${sjjkBean.yyname}"  name="sjjkBean.yyname" id="yyname" onclick="selectSsyy();"/>&nbsp;
														<div class="j10"></div>
														</li>
													</div>
														<li>
															<label>
																接口路径：
															</label>
															<input type="text" name="sjjkBean.uri" id="uri" value="${sjjkBean.uri}"
																class=" " size="30" style="width: 250px" 
																title="路径" alt="notnull;length<=100;" maxlength="100"
																size="30" />
																&nbsp; <font color="green" id="uriSm">*&nbsp;路径组成http://[ip]:[port]/webname。*</font>
														<div class="j10"></div>
														</li>
													<li>
														<label>
															接口类型：
														</label>
														<select name="sjjkBean.jklx" id="jklx" class="jqInp wdateInp" style="width: 265px" title="接口类型">
															<option value="0"  <c:if test="${'0' == sjjkBean.jklx}">selected</c:if>>Http</option>
															<option value="1"  <c:if test="${'1' == sjjkBean.jklx}">selected</c:if>>Webservice</option>
															</select>
													<div class="j10"></div>
													</li>
													<li>
														<label>
															方法名称：
														</label>
														<input type="text" name="sjjkBean.ffname" id="ffname" value="${sjjkBean.ffname}"
															class=" " size="30" style="width: 250px" 
															title="方法名称" alt="length<=100;" maxlength="100"
															size="30" />
															&nbsp; <font color="green">*&nbsp;Webservice接口填写此项,只填写方法名称。*</font>
													<div class="j10"></div>
													</li>
													<li>
														<label>
															接口说明：
														</label>
														<s:textarea cols="50" rows="4" cssClass="jqTextarea" title="服务描述" id="jksm"  name="sjjkBean.jksm" alt="length<=500"></s:textarea>
													<div class="j10"></div>
													</li>
													</ul>
											</div>
											<!--top end-->
										</div>
									</div>
									<div class="" style="height: auto">
										
									
											<table class="mb-table show-td" style="height: auto" border="0" cellspacing="0" cellpadding="0">  
		                                      <tr>
		                                          <td >
		                                          <h3  ><span style="text-align: center;width: 100%">接口参数设置<input type="button" onclick="addRc('','','0')"  value="添加入参"><input type="button" onclick="addCc('','','1');"  value="添加出参"></span></h3>
		                                        	</td>
		                                          
		                                      </tr>
		                                      <tr>
		                                      <td >
		                                       <table class="mb-table show-td" id="jkrctab" style="height: auto" border="0" cellspacing="0" cellpadding="0">  
		                                          <colgroup width="15%"/>
		                                          <colgroup width="30%"/>
		                                          <colgroup width="30%"/>
		                                          <colgroup width="20%"/>
		                                           
					                                          
		                                          </table>
		                                          
		                                          
		                                          <table class="mb-table show-td" id="jkcctab" style="height: auto" border="0" cellspacing="0" cellpadding="0">  
		                                         <colgroup width="15%"/>
		                                          <colgroup width="30%"/>
		                                          <colgroup width="30%"/>
		                                          <colgroup width="20%"/>
		                                                    
		                                          </table>
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

