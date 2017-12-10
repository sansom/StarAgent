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
	<title>服务列表</title>
	<%@ include file="../include/head.inc"%>
	<script type="text/javascript">
		
	    
		//服务删除
		function fwsc(id){
			if(confirm("服务删除之后将不可再访问，您确定要删除服务吗？")){
				window.location = "fwremove.action?fwBean.id="+id;
			}
		}
		
		//停止服务
		function fwtz(id){
			$.ajax({
	    		  type: "POST",
		   		  url: "fwtzFw.action",
		   		  dataType:"text",
		   		  async:true,
		   		  data:"fwBean.id="+id,
				  dataType:"text",
				  success: function(msg){
		  				if(msg == "success"){
		  					alert("服务停止成功！");
		  					window.location = "fwlist.action";
		  				}else{
		  					alert("服务停止失败！");
		  					
		  					return false;
		  				}
				  }
			});
		}
		//服务发布
		function fwfb(id){
			$.ajax({
	    		  type: "POST",
		   		  url: "fwqdFw.action",
		   		  dataType:"text",
		   		  async:true,
		   		  data:"fwBean.id="+id,
				  dataType:"text",
				  success: function(msg){
		  				if(msg == "success"){
		  					alert("服务发布成功！");
		  					window.location = "fwlist.action";
		  				}else{
		  					alert("服务发布失败！");
		  				
		  					return false;
		  				}
				  }
			});
		}
	
		//服务修改
		function fwxg(id){
			window.location = "fwget.action?id="+id;
		}
		
		//服务依赖关系编辑
		function fwgxedit(id){
			window.location = "fwgxedit.action?id="+id;
		}
		
		//服务授权
		function fwsq(id){
			window.location = "fwfwsq.action?id="+id;
		}
		
		//查询列表
    	function search(){
    	var fields=new Array("fwid**","fwname**","fwfl","yyid");
    	getparamter(fields, "queryparamter", "queryparamtername");
    	document.forms[0].submit();
		return true;
    }
    	//选择所属应用
    function selectSsyy(){
   		var conditions = "";
   		openSeachWindow("ssyy",conditions,"yyid,yyname","yyid,yyname","","");
    }
	</script>
</head>
<body >
<div class="bmain-round" style="border:1px solid #111;">
		<div class="main-rbox">
	<div class="boxtop rbox-pos">
	  <div class="htx"><b>当前位置：</b> &gt <span>服务注册</span></div> 
	</div><!--boxtop pos end--> 
     <table class="mb-tm"  border="0" cellspacing="0" cellpadding="0"> 
     	<tr>
     		<td>
     			<div class="min400"> 
               		<div class="boxb indboxt">
               			<h3><span class="qylie">服务列表</span></h3>
               			<div class="boxb_main boxb_main100">
							
										<div class="liebtop" id="searchId">
											<form action="fwlist.action" method="post">
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
		                                        
												<td class="tabletdr" width="10%">所属应用：</td>
												<td width="26%" class="tableleft">
													<input type="hidden" name="yyid"  id="yyid" /> 	
													<input type="text" name="yyname" readonly="readonly" id="yyname"  class="input1_226_bg"  onclick="selectSsyy();" />
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
               					<colgroup style="width: 7%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 8%"/>
								<colgroup style="width: 15%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 7%"/>
								<colgroup style="width: 5%"/>
								<colgroup style="width: 10%"/>
                				<tr class="mb-ta-head">
	                           		<th >序号</th>
	                           		<th >服务ID</th>
				                    <th >服务名称</th>
				                    <th >服务分类</th>
				                    <th >所属应用</th>
				                    <th >路由类型</th>
				                    <th >路径</th>
				                    <th >排序</th>
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
										<td style="text-align: left;">
											${item.uri}
										</td>
										<td >
											<c:if test="${item.yyname == 'nofound'}">
											<font color="red">未找到应用</font>
											</c:if>
											<c:if test="${item.yyname != 'nofound'}">
											${item.yyname}
											</c:if>
										</td>
										<td >
											${item.sort}
											&nbsp;
											<!-- 
											${item.price}元/
											<jadlbean:write name="item" property="priceunit" actualkey="unit" /> -->
										</td>
										
										<td >
											${item.fwzt_dicvalue}
										</td>
										<td  align="center">
											
											
											<c:if test="${item.fwzt == 0}">
												<a href="#" onclick="fwtz('${item.id}')">停止</a>
											</c:if>
											<c:if test="${item.fwzt == 1}">
												<a href="#" onclick="fwfb('${item.id}')">发布</a>
												
											</c:if>
											<a href="#" onclick="fwxg('${item.id}')">修改</a>
											<a href="#" onclick="fwsc('${item.id}')">删除</a>
											<br/>
											<a href="#" onclick="fwgxedit('${item.id}')">依赖关系</a>
											<a href="#" onclick="fwsq('${item.id}')">定制路由</a>
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
					<div id="printId" align="center">
						<input type="button" class="tianjiaanniu" value="添加服务"
							onclick="window.location='fwedit.action'"/>
				
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
