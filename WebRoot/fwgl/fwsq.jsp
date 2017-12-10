<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>定制路由</title>

		<%@ include file="../include/head.inc"%>
		<script type="text/javascript">
  	var win_search;		//弹出窗口
  	//校验域填写是否完整
  	function checkInput(){
  		$("#insert").attr("disabled", true);
		var err = checkForm(document.fwsq);
		if(!err){
			$("#insert").attr("disabled", false);
			return false;
		}
		var b = true;
		$("input[name=ip]").each(function(){
			if(!checkIP($(this).val())){
				 b = false;
				return false;
			}
		});
		
		if(!b){
			return false;
		}
		document.forms[0].action = "fwsqupdate.action";
		document.forms[0].submit();
  	}
  	
  	

	function checkIP(ip){ 
		var exp=/([0-9]{1,3}\.{1}){3}[0-9]{1,3}/ 
		if(exp.test(ip))     
   		{     
       		if( RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)   
       		return true;     
  		}     
		alert("IP地址不合法!"); 
		return false;
	} 
    //删除行
 	function del(tableId,obj){
		var table = document.getElementById(tableId);
			var onrow = obj.parentNode.parentNode.rowIndex;
			table.deleteRow(onrow);
 	}
 	
 	//添加授权
 	function addSq(ip,lx){
 		if($("#fwyltab tr").length>=12){
 			alert("最多只能添加10个IP!");
 			return false;
 		}

 		var onTab = document.getElementById("fwyltab");
		var new_tr = onTab.insertRow();
		var new_td_1 = new_tr.insertCell();
		var new_td_2 = new_tr.insertCell();
		var new_td_3 = new_tr.insertCell();
		var selectDefault1 = "";
		var selectDefault2 = "";
		var selectDefault3 = "";
		if(lx == ''){
		 	selectDefault1 = "selected=\"selected\"";
		}else if(lx == '0'){
			selectDefault2  = "selected=\"selected\"";
		}else if(lx == '1'){
			selectDefault3  = "selected=\"selected\"";
		}
		new_td_1.innerHTML = " <input type=\"text\" alt=\"notnull;length<=17;\" title=\"ip地址\"  name=\"ip\" id=\"ip\" value=\""+ip+"\" />";
		new_td_2.innerHTML = "<select style=\"width:200px\" alt=\"notnull;\" title=\"类型\" name=\"sqfw\" id=\"sqfw\" >"+
			                   "<option value=\"\" "+selectDefault1+"></option>"+
			                   "<option value=\"0\" "+selectDefault2+">允许</option>"+
			                   "<option value=\"1\" "+selectDefault3+">禁止</option>"+
			                  "</select>";
		new_td_3.innerHTML = "<a href=\"javascript:void(0)\" onclick=\"del('fwyltab',this)\">删除</a>";
 	}
 	

  	//初始化
  	function init(){
  		  <s:if test="#request.fwsqList != null">
			<s:iterator id="item" value="#request.fwsqList" status="idx">
				
					addSq('${item.ip}','${item.sqfw}');
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
						<span>定制路由</span>
					</div>
				</div>
				<!--boxtop pos end-->
				<form method="post" name="fwsq" id="fwsq" action="../zdjh">
				<table class="mb-tm" style="height: auto" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							
								<div class="">
									<div class="j10"></div>
									<div class="boxb indboxt">
										<h3>
											<span class="qylie">定制路由</span>
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
		                                      <colgroup width="30%"> </colgroup>
		                                       <colgroup width="30%"> </colgroup>
		                                        <colgroup width="10%"> </colgroup>
		                                      <tr>
		                                          <td colspan="3" >
		                                         * 注：默认为允许所有IP* &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                                         	定制路由
		                                          	<input type="button" onclick="addSq('','')"  value="授权路由"/>
		                                          
		                                          </td>
		                                      </tr>
		                                     <tr>
		                                      
			                                     <td>
			                                     路由IP地址
			                                     </td>
			                                     <td>
			                                     	类型
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
						
						</td>
					</tr>
				</table>
				</form>
				<!--mbox end-->
			</div>
		</div>
	</body>
</html>

