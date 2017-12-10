<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>用户编辑</title>

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
		var fwid = $("#fwid").val();
		var uri = $("#uri").val();
		if(!isZm(fwid)){
			alert("服务ID只能由字母组成！");
			return false;
		}
		if(fwid.length > 19 || fwid.length < 6){
			alert("服务ID长度为6~19个字符！");
			return false;
		}
		if(!uri.startWith("http://") && !uri.startWith("https://")){
			alert("路径不符！");
			return false;
		}

		if($("#id").val() != null && $("#id").val() !=""){
			document.forms[0].action = "fwupdate.action";
			document.forms[0].submit();
		}else{
			document.forms[0].action = "fwsave.action";
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
    
  	//选择数据接口
    function selectSjjk(){
   		var conditions = "";
   		openSeachWindow("sjjk",conditions,"sjjkid,sjjkname","sjjkid,sjjkname","","");
    }
  	
  	//初始化
  	function init(){
  		$("#fwfl").val("${fwBean.fwfl}");
  		//$("#lylx").val("01");
		$("#priceunit").val("${fwBean.priceunit}");											
  	}
  
  	</script>
	</head>
	<body onload="init();">

		<div class="bmain-round">
			<div class="main-rbox">
				<div class="boxtop rbox-pos">
					<div class="htx">
						<b>当前位置：</b> &gt
						<span>服务编辑</span>
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
											<span class="qylie">服务编辑</span>
										</h3>
										<div class="boxb_main boxb_main100">
											<div class="liebtop">
												<ul>
													<li>
														<label>
															服务ID：
														</label>
														<input name="fwBean.id" type="hidden" id="id" value="${fwBean.id}" />
														<c:if test="${fwBean.id == null || fwBean.id == ''}">
														<input type="text" name="fwBean.fwid" id="fwid" 
															value="${fwBean.fwid}" class="jqInp wdateInp" size="30"
															style="width: 250px" title="服务ID"
															alt="notnull;length<=30;" maxlength="30" size="30" />
														</c:if>
														<c:if test="${fwBean.id != null && fwBean.id != ''}">
														<input type="text" name="fwBean.fwid" id="fwid"  readonly="readonly"
															value="${fwBean.fwid}" class="jqInp wdateInp" size="30"
															style="width: 250px;background:#f4f4f4" title="服务ID"
															alt="notnull;length<=30;" maxlength="30" size="30" />
														</c:if>
														
															&nbsp; <font color="green">*&nbsp;服务ID为6-19个字母组成，添加成功后不可更改。*</font>
													</li>
													<div class="j10"></div>
													<li>
														<label>
															服务名称：
														</label>
														<input type="text" name="fwBean.fwname" id="fwname"
															value="${fwBean.fwname}" class="jqInp wdateInp"
															style="width: 250px" title="服务名称"
															alt="notnull;length<=50;" maxlength="50" size="30" />
														
													</li>
													<div class="j10"></div>
													<li>
														<label>
															服务排序：
														</label>
														<input type="text" class="jqInp wdateInp"  title="服务排序" alt="length<=3;" onkeypress="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value"
						onkeyup="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value;if(this.value=='undefined')this.value='';" 
						onblur="if(!this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))this.value=this.o_value;else{if(this.value.match(/^\.\d+$/))this.value=0+this.value;if(this.value.match(/^\.$/))this.value=0;this.o_value=this.value}"style="width: 250px" value="${fwBean.sort}"  name="fwBean.sort" id="sort"/>&nbsp;
														
													</li>
													
													<div class="j10"></div>
													<li>
														<label>
															服务分类：
														</label>
														
														<select name="fwBean.fwfl" id="fwfl" title="服务分类"  alt="notnull;" style="width: 265px" >
															<option value=""></option>
															<jadlhtml:optionsCollection label="mc" name="dic"
																property="t_dm_fwfl" value="dm" />
														</select>
														
													</li>
													<div class="j10"></div>
													
													<li>
														<label>
															所属应用：
														</label>
														<input type="hidden" name="fwBean.yyid" value="${fwBean.yyid}" id="yyid" /> 
                                        				<input type="text" class="input1_250_bg" readonly="readonly" title="所属应用" alt="notnull;length<=50;" style="width: 250px" value="${yyname}"  name="yyname" id="yyname" onclick="selectSsyy();"/>&nbsp;
													</li>
													<div class="j10"></div>
													<li>
														<label>
															路由类型：
														</label>
														<select name="fwBean.lylx" id="lylx" title="路由类型"  alt="notnull;" style="width: 265px" >
															<option value=""></option>
															<jadlhtml:optionsCollection label="mc" name="dic"
																property="t_dm_lylx" value="dm" />
														</select>
													</li><div class="j10"></div>
													
													<li>
														<label>
															路径：
														</label>
														
														<input type="text"  name="fwBean.uri" id="uri" value="${fwBean.uri}"
															class=" " size="30" style="width: 250px" 
															title="路径" alt="notnull;length<=100;" maxlength="100"
															size="30" />
															&nbsp; <font color="green">*&nbsp;路径组成[http|https]://[ip]/webname,其中[ip]由所属应用决定。*</font>
													</li><div class="j10"></div>
													
													<li>
														<label>
															价格：
														</label>
														
														<input type="text" name="fwBean.price" id="price" value="${fwBean.price}"
															class=" " size="30" style="width: 190px" 
															title="价格" alt="notnull;length<=10;" maxlength="10"
															size="30" onkeypress="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value"
						onkeyup="if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value;if(this.value=='undefined')this.value='';" 
						onblur="if(!this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))this.value=this.o_value;else{if(this.value.match(/^\.\d+$/))this.value=0+this.value;if(this.value.match(/^\.$/))this.value=0;this.o_value=this.value}" />
															元/
															<select name="fwBean.priceunit" id="priceunit" title="单位"  alt="notnull;" style="width: 45px;height:22px" >
															<jadlhtml:optionsCollection label="mc" name="dic"
																property="t_dm_unit" value="dm" />
														</select>
													</li>
													<div class="j10"></div>
													<li>
														<label>
															服务输入：
														</label>
													
                                        				<input type="text" class="jqInp wdateInp"  title="服务输入" alt="notnull;length<=100;" style="width: 250px" value="${fwBean.fwsr}"  name="fwBean.fwsr" id="fwsr"/>&nbsp;
													</li>
													<div class="j10"></div>
													
													<li>
														<label>
															服务输出：
														</label>
														<input type="text" class="jqInp wdateInp"  title="服务输出" alt="notnull;length<=100;" style="width: 250px" value="${fwBean.fwsc}"  name="fwBean.fwsc" id="fwsc"/>&nbsp;
													</li>
													<div class="j10"></div>
													<li>
														<label>
															服务描述：
														</label>
														
														<s:textarea cols="50" rows="4" cssClass="jqTextarea" title="服务描述" id="fwms"  name="fwBean.fwms" alt="notnull;length<=500"></s:textarea>
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

