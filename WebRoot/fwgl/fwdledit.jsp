<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>服务队列编辑</title>

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
			document.forms[0].action = "fwdlupdate.action";
			document.forms[0].submit();
		}else{
			document.forms[0].action = "fwdlsave.action";
			document.forms[0].submit();
		}
		
  	}
	//选择服务
    function selectFw(){
   		var conditions = "";
   		openSeachWindow("allfwlist",conditions,"fwid,fwname","fwid,fwname","","");
    }
  	</script>
	</head>
	<body >

		<div >
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx">
						<b>当前位置：</b> &gt
						<span>服务队列编辑</span>
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
											<span class="qylie">服务队列编辑</span>
										</h3>
										<div class="boxb_main boxb_main100">
											<div class="liebtop">
												<ul>
													<li style="text-align: center;">
															<label>
															服务：
														</label>
															
															<input type="hidden" name="fwddBean.fwid" value="${fwddBean.fwid}" id="fwid" /> 
                                        					<input type="text" class="input1_250_bg" readonly="readonly" title="服务" alt="notnull;length<=50;" style="width: 250px" value="${fwname}"  name="fwname" id="fwname" onclick="selectFw();"/>&nbsp;
															<input name="fwddBean.id" type="hidden" id="id" value="${fwddBean.id}" />
														
													</li><div class="j10"></div>
													<li style="text-align: center;">
															
																<label>
															队列序号：
														</label>
															<input type="text" name="fwddBean.dddj" id="dddj"
															value="${fwddBean.dddj}" onblur=" var g = /^[1-9]*[1-9][0-9]*$/;if(!g.test(this.value)){alert('队列序号只能输入正整数');this.value = '';}" class="jqInp wdateInp"
															style="width: 250px" title="队列序号"
															alt="notnull;length<=10;" maxlength="50" size="30" />
							
													</li>
													<div class="j10"></div>
											</div>
											<!--top end-->
										</div>
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

