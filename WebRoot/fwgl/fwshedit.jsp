<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>服务编辑</title>

		<%@ include file="../include/head.inc"%>
		<script type="text/javascript">
  	var win_search;		//弹出窗口
  	//校验域填写是否完整
  	function checkInput(fwzt){
  		$("#insert").attr("disabled", true);
		var err = checkForm(document.forms[0]);
		if(!err){
			$("#insert").attr("disabled", false);
			return false;
		}
		var spsm = $("#spsm").val();
		if(fwzt == '04' && spsm == ''){
			alert("审批说明不能为空！");
			return false;
		}
		
		$("#fwzt").val(fwzt);
		if(confirm("您确定要进行审核操作吗？")){
			document.forms[0].action = "fwwbfwsh.action";
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
  	

  	
  	//初始化
  	function init(){
		$("#priceunit").val("${wbfwBean.priceunit}");											
  	}
  	</script>
	</head>
	<body onload="init();">

		<div class="bmain-round">
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx">
						<b>当前位置：</b> &gt
						<span>服务审核</span>
					</div>
				</div>
				<!--boxtop pos end-->
				<table class="mb-tm" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<s:form method="post" action="../zdjh">
								<div class="min400">
									<div class="j10"></div>
									<div class="boxb indboxt">
										<h3>
											<span class="qylie">服务审核</span>
										</h3>
										<div class="boxb_main boxb_main100">
											<div class="liebtop">
												<table class="mb-table show-td" style="height: auto" border="0" cellspacing="0" cellpadding="0">
												<colgroup width="35%" ></colgroup>
												<colgroup width="50%"></colgroup>
												<tr>
												
												<td style="text-align:right;">
												<input name="wbfwBean.id" type="hidden" id="id" value="${wbfwBean.id}" />
														<input name="wbfwBean.fwzt" type="hidden" id="fwzt" value="" />
												服务ID：
												</td>
												<td style="text-align:left;padding-left: 10px">
												${wbfwBean.fwid}
												</td>
												</tr>
												<tr>
												<td style="text-align:right;">
												服务名称：
												</td>
												<td style="text-align:left;padding-left: 10px">
												${wbfwBean.fwname}
												</td>
												</tr>
												<tr>
												<tr>
												<td style="text-align:right;">
												
												路由类型：
												</td>
												<td style="text-align:left;padding-left: 10px">
												
												<jadlbean:write name="wbfwBean" property="lylx" />
											
												</td>
												</tr>
												
												<td style="text-align:right;">
												
												路径：
												</td>
												<td style="text-align:left;padding-left: 10px">
												${wbfwBean.uri}
												</td>
												</tr>
												<tr>
												
												<td style="text-align:right;">
												
												价格：
												</td>
												<td style="text-align:left;padding-left: 10px">
												${wbfwBean.price}/<jadlbean:write name="wbfwBean" property="priceunit" actualkey="unit" />
												</td>
												</tr>
												<tr>
												
												<td style="text-align:right;">
												
												服务输入：
												</td>
												<td style="text-align:left;padding-left: 10px">
												${wbfwBean.fwsr}
												</td>
												</tr>
												
												<tr>
												<tr>
												
												<td style="text-align:right;">
												
												服务输出：
												</td>
												<td style="text-align:left;padding-left: 10px">
												${wbfwBean.fwsc}
												</td>
												</tr>
												
												<tr>
												<tr>
												
												<td style="text-align:right;">
												
												服务描述：
												</td>
												<td style="text-align:left;padding-left: 10px">
												${wbfwBean.fwms}
												</td>
												</tr>
												
												<tr>
												
												<td style="text-align:right;">
												
												审批意见：
												</td>
												<td style="text-align:left;padding-left: 10px">
												<s:textarea cols="37" rows="3" cssClass="jqTextarea" title="审批意见" id="spsm"  name="wbfwBean.spsm" alt="length<=500"></s:textarea>
												</td>
												</tr>
												</table>
												
													
											</div>
											<!--top end-->
										</div>
									</div>
									<!--box end-->
									<br />
									<br />
									<div align="center">
										<input type="button" style="width: 70px;height: 29px" name="insert" value="通过"
											class="" onclick="checkInput('01');" />
										<input type="button" style="width: 70px;height: 29px" name="insert" value="不通过"
											class="" onclick="checkInput('04');" />
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

