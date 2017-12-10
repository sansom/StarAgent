<%@page import="com.core.jadlsoft.struts.action.UserUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="com.core.jadlsoft.utils.DateUtils"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@page import="com.core.jadlsoft.model.xtgl.UserSessionBean"%>
<%
	UserSessionBean userSessionBean = (UserSessionBean)session.getAttribute(UserUtils.USER_SESSION);
	String qqsj_to = DateUtils.getCurrentData();
	String qqsj_from = qqsj_to.substring(0,5) + "01-01";

	String queryparamter = (String)request.getParameter("queryparamter");

	if(queryparamter != null && queryparamter.indexOf("qqsj_from") > 0 ){
		
		qqsj_from = queryparamter.substring(queryparamter.indexOf("qqsj_from") + 12,queryparamter.indexOf("qqsj_from") + 22);
	
	}
	if(queryparamter != null && queryparamter.indexOf("qqsj_to") > 0 ){
		
		qqsj_to = queryparamter.substring(queryparamter.indexOf("qqsj_to") + 10,queryparamter.indexOf("qqsj_to") + 20);
		
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>服务日志</title>
	<%@ include file="../include/head.inc"%>
	<script type="text/javascript">
	//选择服务
	function selectFw(){
		var conditions = "";
	   	openSeachWindow("fwlist",conditions,"fwid,fwname","fwid,fwname","","");
	}
	
	 //查询列表
    function search(){
    	var fields=new Array("fwid","qqly","qqsj_from","qqsj_to");
    	getparamter(fields, "queryparamter", "queryparamtername");
    	
    	document.forms[0].submit();
		return true;
    }
     function openFwlogmx(fwid,fwname){
    	var qqsj_from = document.getElementById("qqsj_from").value; 
    	var qqsj_to = document.getElementById("qqsj_to").value; 
		
    	var url = "fwloglist.action?queryparamter=%26%26fwid~=~" + fwid + "~qqsj_from~=~" + qqsj_from + "~qqsj_to~=~" + qqsj_to; 
		do_openMax(url,"服务日志明细","yes");
    }
	</script>
</head>
<body >
<div class="bmain-round" style="border:1px solid #111;">
		<div class="main-rbox">
	<div class="boxtop rbox-pos">
	  <div class="htx"><b>当前位置：</b> &gt <span>服务日志</span></div> 
	</div><!--boxtop pos end--> 
     <table class="mb-tm"  border="0" cellspacing="0" cellpadding="0"> 
     	<tr>
     		<td>
     			<div class="min400"> 
               		<div class="boxb indboxt">
               			<h3><span class="qylie">服务日志</span></h3>

									<div class="boxb_main boxb_main100">
							
										<div class="liebtop" id="searchId">
											<form action="fwlogtjlist.action" method="post">
												<input type="hidden" name="queryparamter" />
												<input type="hidden" name="queryparamtername" />
											
												<table class="table_300" >
												<tr>
													<td class="tabletdr" width="10%">请求服务：</td>
                             						<td width="26%" class="tableleft">
                             						<input type="hidden" id="fwid" />
													<input type="text" name="fwname" id="fwname" readonly="readonly"  class="input1_226_bg" onclick="selectFw();"/>
														
		                                        </td>
												<td class="tabletdr" width="10%">请求路由：</td>
												<td width="26%" class="tableleft">
													<select name="qqly" id="qqly" title="请求路由" style="width: 265px" >
															<option value=""></option>
															<jadlhtml:optionsCollection label="mc" name="dic"
																property="t_dm_lylx" value="dm" />
														</select>
		                                        </td>
												<td width="26%" class="tableleft" >
		                                 			<input type="button" value="查询" class="tjcx_new_1" onclick="search();" />
	                                    			<input type="reset" value="重置" class="chongzhi_new_1"/>
	                                    			
		                                 		</td>
												</tr>
												<tr>
													<td class="tabletdr" width="10%">服务时间：</td>
                             						<td width="26%" class="tableleft">
                             						<input title="服务起始时间" id="qqsj_from" class="input_shij100" name="qqsj_from" type="text" value="<%=qqsj_from %>" onfocus="WdatePicker();" />
			                                        &nbsp;至&nbsp; 
			                                     	<input title="服务截止时间" id="qqsj_to" class="input_shij100" name="qqsj_to" type="text" value="<%=qqsj_to %>" onfocus="WdatePicker();"/>
														
		                                        </td>
												<td class="tabletdr" width="10%"></td>
												<td width="26%" class="tableleft" colspan="2">
													
		                                        </td>
												
												</tr>
											</table>
											</form>
											
										</div>
										
										
										<div class="j10"></div>
               			<div class="boxb_main boxb_main100">
               				<table class="mb-table show-td" border="0" cellspacing="0" cellpadding="0">
               					<colgroup style="width: 5%"/>
               					<colgroup style="width: 10%"/>
								<colgroup style="width: 15%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 10%"/>
                				<tr class="mb-ta-head">
	                           		<th >序号</th>   
				                    <th >请求服务ID</th>
				                    <th >请求服务名称</th>
				                    <th >路由方式</th>
				                    <th >服务次数</th>
				                   	<th >操作</th>
					            </tr>
					            <s:if test="#request.list != null">
								<s:iterator id="item" value="#request.list" status="idx">
									<tr class="mb-ta-head">
										<td>
											${idx.index+1}
										</td>
										<td >
											${item.fwid}
										</td>
										<td >
											${item.fwname}
										</td>
										<td >
											<jadlbean:write name="item" property="qqly" actualkey="lylx" />
										
										</td>
										<td >
											${item.fwcs}
										</td>
										<td >
											<a href="javascript:void(0)" onclick="openFwlogmx('${item.fwid}','${item.fwname}');">详情</a>
										</td>
									</tr>
								</s:iterator>
							</s:if>
               				</table>
               				<div class="j10"></div>
               				 <%@ include file="../include/page.inc"%>
               				<div class="j10"></div>
               			</div> 
               		</div>
               		<div class="j10"></div>
					
     			</div>
     		</td>
     	</tr>
     </table> 
     </div>
     </div>
</body>
</html>
