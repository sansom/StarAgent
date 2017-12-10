<%@page import="com.core.jadlsoft.struts.action.UserUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@page import="com.core.jadlsoft.model.xtgl.UserSessionBean"%>
<%
	UserSessionBean userSessionBean = (UserSessionBean)session.getAttribute(UserUtils.USER_SESSION);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>服务队管理</title>
	<%@ include file="../include/head.inc"%>
	<script type="text/javascript">

		//服务队列删除
		function fwdlsc(id){
			if(confirm("您确定要删除服务队列吗？")){
				window.location = "fwdlremove.action?fwddBean.id="+id;
			}
		}
	
	
		//服务队列修改
		function fwdlxg(id){
			window.location = "fwdlget.action?fwddBean.id="+id;
		}
	
		
		//查询列表
    	function search(){
    	var fields=new Array("fwid**","fwname**","fwfl","lylx");
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
	  <div class="htx"><b>当前位置：</b> &gt <span>服务队列管理</span></div> 
	</div><!--boxtop pos end--> 
     <table class="mb-tm"  border="0" cellspacing="0" cellpadding="0"> 
     	<tr>
     		<td>
     			<div class="min400"> 
               		<div class="boxb indboxt">
               			<h3><span class="qylie">服务队列列表</span></h3>
               			<div class="boxb_main boxb_main100">
							
										<div class="liebtop" id="searchId">
											<form action="fwdllist.action" method="post">
												<input type="hidden" name="queryparamter" />
												<input type="hidden" name="queryparamtername" />
											
												<table class="table_300" >
												<tr>
													<td class="tabletdr" width="10%">服务ID：</td>
                             						<td width="26%" class="tableleft">
                             					
													<input type="text" name="fwid" id="fwid"  class="input1_226" />
														
		                                        </td>
		                                        
												<td class="tabletdr" width="10%">服务名称：</td>
												<td width="26%" class="tableleft">
													<input type="text" name="fwname" id="fwname"  class="input1_226" />
		                                        </td>
												<td width="26%" class="tableleft" >
		                                 			<input type="button" value="查询" class="tjcx_new_1" onclick="search();" />
	                                    			<input type="reset" value="重置" class="chongzhi_new_1"/>
	                                    			
		                                 		</td>
												</tr>
												<tr>
													<td class="tabletdr" width="10%">服务分类：</td>
                             						<td width="26%" class="tableleft">
														<select name="fwfl" id="fwfl" title="服务分类"  style="width: 227px" >
															<option value=""></option>
															<jadlhtml:optionsCollection label="mc" name="dic"
																property="t_dm_fwfl" value="dm" />
														</select>
														
		                                        </td>
		                                        
												<td class="tabletdr" width="10%">路由类型：</td>
												<td width="26%" class="tableleft">
													<select name="lylx" id="lylx" title="路由类型"  style="width: 227px" >
															<option value=""></option>
															<jadlhtml:optionsCollection label="mc" name="dic"
																property="t_dm_lylx" value="dm" />
														</select>
		                                        </td>
												<td width="26%" class="tableleft" >
		                                 			
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
								<colgroup style="width: 10%"/>

                				<tr class="mb-ta-head">
	                           		<th >序号</th>
	                           		<th >服务ID</th>
				                    <th >服务名称</th>
				                    <th >服务分类</th>
				                    <th >路由类型</th>
				                    <th >队列序号</th>
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
											${item.fwfl_dicvalue}
										</td>
										<td >
											${item.lylx_dicvalue}
										</td>
											<td >
											${item.dddj}
										</td>
										<td  align="center">
											<a href="#" onclick="fwdlxg('${item.id}')">修改</a>
											<a href="#" onclick="fwdlsc('${item.id}')">删除</a>
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
					<div id="printId" align="center">
						<input type="button" class="tianjiaanniu" value="添加服务队列"
							onclick="window.location='fwdledit.action'"/>
				
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
