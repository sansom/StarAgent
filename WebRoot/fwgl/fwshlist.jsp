<%@page import="com.core.jadlsoft.struts.action.UserUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@page import="com.core.jadlsoft.model.xtgl.UserSessionBean"%>
<%
	UserSessionBean userSessionBean = (UserSessionBean)session.getAttribute(UserUtils.USER_SESSION);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>服务列表</title>
	<%@ include file="../include/head.inc"%>
	<script type="text/javascript">
		
	    
		//服务注销
		function fwsc(id){
			if(confirm("您确定要注销服务吗？")){
				window.location = "fwwbfwremove.action?wbfwBean.id="+id;
			}
		}
		

		//服务审核
		function fwsh(id){
			window.location = "fwwbfwget.action?wbfwBean.id="+id;
		}
	
	</script>
</head>
<body >
<div class="bmain-round" style="border:1px solid #111;">
		<div class="main-rbox">
	<div class="boxtop rbox-pos">
	  <div class="htx"><b>当前位置：</b> &gt <span>外部服务</span></div> 
	</div><!--boxtop pos end--> 
     <table class="mb-tm"  border="0" cellspacing="0" cellpadding="0"> 
     	<tr>
     		<td>
     			<div class="min400"> 
               		<div class="boxb indboxt">
               			<h3><span class="qylie">外部服务列表</span></h3>
               			<div class="boxb_main boxb_main100">
               				<table class="mb-table show-td" border="0" cellspacing="0" cellpadding="0">
               					<colgroup style="width: 5%"/>
               					<colgroup style="width: 7%"/>
               					<colgroup style="width: 7%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 15%"/>
								<colgroup style="width: 5%"/>
								<colgroup style="width: 7%"/>
								<colgroup style="width: 7%"/>
								<colgroup style="width: 10%"/>
                				<tr class="mb-ta-head">
	                           		<th >序号</th>
	                           		<th >申请人</th>
	                           		<th >服务ID</th>
				                    <th >服务名称</th>
				                    <th >路由类型</th>
				                    <th >路径</th>
				                    <th >服务价格</th>
				                    <th >状态</th>
				                    <th >操作</th>
					            </tr>
					            <s:if test="#request.list != null">
								<s:iterator id="item" value="#request.list" status="idx">
									<tr class="mb-ta-head">
										<td>
											${idx.index+1}
										</td>
										<td >
											${item.sqrxm}
										</td>
										<td >
											${item.fwid}
										</td>
										<td >
											${item.fwname}
										</td>
										<td >
											${item.lylx_dicvalue}
										</td>
										<td >
											${item.uri}
										</td>
										
										<td >
											${item.price}元/
											<jadlbean:write name="item" property="priceunit" actualkey="unit" />
										</td>
										<td >
											<c:if test="${item.fwsqzt == '03'}">
													待审核
											</c:if>
											<c:if test="${item.fwsqzt == '01'}">
													有效
											</c:if>
										</td>
										
										<td  align="center">
											<c:if test="${item.fwsqzt == '03'}">
												<a href="#" onclick="fwsh('${item.id}')">审核</a>
												
											</c:if>
											<a href="#" onclick="fwsc('${item.id}')">注销</a>
										</td>
										<input type=hidden name="fwid" value="${item.id}"/>
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
