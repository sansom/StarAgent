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
	String fwid = "";
	String queryparamter = (String)request.getParameter("queryparamter");

	String[] conds = queryparamter.substring(2).split("~");
	if(queryparamter != null){
		for(int i=0;i<conds.length/3;i++) {
			
			if(conds[3 * i].equals("qqsj_from")){
				qqsj_from = conds[3 * i+2];
			}else if (conds[3 * i].equals("qqsj_to") ){
				qqsj_to = conds[3 * i+2];
			}else if(conds[3 * i].equals("fwid") ){
			
				fwid = conds[3 * i+2];
			}
			
		}
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>服务日志明细</title>
	<%@ include file="../include/head.inc"%>
	<script type="text/javascript">
	//选择服务
	function selectFw(){
		var conditions = "";
	   	openSeachWindow("fwlist",conditions,"fwid,fwname","fwid,fwname","","");
	}
	
	 //查询列表
    function search(){
    	var fields=new Array("fwid","qqsj_from","qqsj_to","userid","clientip");
    	getparamter(fields, "queryparamter", "queryparamtername");
    	document.forms[0].submit();
		return true;
    }
    
      
   
	</script>
</head>
<body >
<div class="bmain-round" style="border:1px solid #111;">
		<div class="main-rbox">
	<div class="boxtop rbox-pos">
	  <div class="htx"><b>当前位置：</b> &gt <span>服务日志明细</span></div> 
	</div><!--boxtop pos end--> 
     <table class="mb-tm"  border="0" cellspacing="0" cellpadding="0"> 
     	<tr>
     		<td>
     			<div class="min400"> 
               		<div class="boxb indboxt">
               			<h3><span class="qylie">服务日志明细</span></h3>

									<div class="boxb_main boxb_main100">
							
										<div class="liebtop" id="searchId">
											<form action="fwloglist.action" method="post">
												<input type="hidden" name="queryparamter" />
												<input type="hidden" name="queryparamtername" />
											
												<table class="table_300" >
												<tr>
													<td class="tabletdr" width="10%">访问用户：</td>
                             						<td width="26%" class="tableleft">
                             						<input type="hidden" id="fwid" value="<%=fwid %>" />
													<input type="text" name="userid" id="userid" class="input1_226" />
														
		                                        </td>
												<td class="tabletdr" width="10%">用户IP：</td>
												<td width="26%" class="tableleft">
													<input type="text" name="clientip" id="clientip" class="input1_226" />
		                                        </td>
												<td width="26%" class="tableleft" >
		                                 			<input type="button" value="查询" class="tjcx_new_1" onclick="search();" />
	                                    			<input type="reset" value="重置" class="chongzhi_new_1"/>
	                                    			
		                                 		</td>
												</tr>
												<tr>
													<td class="tabletdr" width="10%">请求服务：</td>
                             						<td width="26%" class="tableleft">
                             						<input type="hidden" id="fwid" />
													<input type="text" name="fwname" id="fwname" readonly="readonly"  class="input1_226_bg" onclick="selectFw();"/>
														
		                                       	 </td>
											
													<td class="tabletdr" width="10%">服务时间：</td>
                             						<td width="26%" class="tableleft" colspan="2">
                             						<input title="服务起始时间" id="qqsj_from" class="input_shij100" name="qqsjfrom" type="text" value="<%=qqsj_from %>" onfocus="WdatePicker();" />
			                                        &nbsp;至&nbsp; 
			                                     	<input title="服务截止时间" id="qqsj_to" class="input_shij100" name="qqsjto" type="text" value="<%=qqsj_to %>" onfocus="WdatePicker();"/>
														
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
								
                				<tr class="mb-ta-head">
	                           		<th >序号</th>
	                           		<th >访问用户</th>
				                    <th >用户IP</th>
				                    <th >请求路由</th>
				                    <th >请求服务ID</th>
				                    <th >请求服务名称</th>
				                    <th >请求地址</th>
				                    <th >请求时间</th>
					            </tr>
					            <s:if test="#request.list != null">
								<s:iterator id="item" value="#request.list" status="idx">
									<tr class="mb-ta-head">
										<td>
											${idx.index+1}
										</td>
										
										<td >
											${item.userid}
										</td>
										<td >
											${item.clientip}
										</td>
										<td >
											<jadlbean:write name="item" property="qqly" actualkey="lylx" />
											
										</td>
										<td >
											${item.fwid}
										</td>
										<td >
											${item.fwname}
										</td>
											<td >
											${item.qqdz}
										</td>
										<td >
											${item.qqsj}
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
						<div id="printId">
							<script type="text/javascript">PE("titleId,searchId,printId");</script>
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
