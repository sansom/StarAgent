<%@page import="com.core.jadlsoft.struts.action.UserUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@page import="com.core.jadlsoft.model.xtgl.UserSessionBean"%>
<%
	UserSessionBean userSessionBean = (UserSessionBean)session.getAttribute(UserUtils.USER_SESSION);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>数据接口列表</title>
	<%@ include file="../include/head.inc"%>
	<script type="text/javascript">
		
		//接口删除
		function jksc(id){
			if(confirm("接口删除之后将不可以再访问，您确定要删除接口吗？")){
				window.location = "sjjkremove.action?sjjkBean.id="+id;
			}
		}
		
	
		//接口修改
		function jkxg(id){
			window.location = "sjjkget.action?id="+id;
		}
		
		//停止数据接口
		function jktz(id){
			$.ajax({
	    		  type: "POST",
		   		  url: "sjjktzSjjk.action",
		   		  dataType:"text",
		   		  async:true,
		   		  data:"sjjkBean.id="+id,
				  dataType:"text",
				  success: function(msg){
		  				if(msg == "success"){
		  					alert("接口停止成功！");
		  					window.location = "sjjklist.action";
		  				}else{
		  					alert("接口停止失败！");
		  					
		  					return false;
		  				}
				  }
			});
		}
		//接口发布
		function jkfb(id){
			$.ajax({
	    		  type: "POST",
		   		  url: "sjjkfbSjjk.action",
		   		  dataType:"text",
		   		  async:true,
		   		  data:"sjjkBean.id="+id,
				  dataType:"text",
				  success: function(msg){
		  				if(msg == "success"){
		  					alert("接口发布成功！");
		  					window.location = "sjjklist.action";
		  				}else{
		  					alert("接口发布失败，请检查网络！");
		  				 
		  					return false;
		  				}
				  }
			});
		}
	</script>
</head>
<body >
<div class="bmain-round" style="border:1px solid #111;">
		<div class="main-rbox">
	<div class="boxtop rbox-pos">
	  <div class="htx"><b>当前位置：</b> &gt <span>数据接口列表</span></div> 
	</div><!--boxtop pos end--> 
     <table class="mb-tm"  border="0" cellspacing="0" cellpadding="0"> 
     	<tr>
     		<td>
     			<div class="min400"> 
               		<div class="boxb indboxt">
               			<h3><span class="qylie">数据接口列表</span></h3>
               			<div class="boxb_main boxb_main100">
               				<table class="mb-table show-td" border="0" cellspacing="0" cellpadding="0">
               					<colgroup style="width: 5%"/>
               					<colgroup style="width: 7%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 15%"/>
								<colgroup style="width: 10%"/>
								<colgroup style="width: 5%"/>
								<colgroup style="width: 5%"/>
								<colgroup style="width: 15%"/>
                				<tr class="mb-ta-head">
	                           		<th >序号</th>
	                           		<th >接口ID</th>
				                    <th >接口名称</th>
				                    <th >路径</th>
				                    <th >所属应用</th>
				                    <th >接口类型</th>
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
											${item.jkid}
										</td>
										<td >
											${item.jkname}
										</td>
										<td >
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
											${item.jklx_dicvalue}
										</td>
										<td >
											${item.jkzt_dicvalue}
										</td>
										<td  align="center">
											<c:if test="${item.jkzt == 0}">
												<a href="#" onclick="jktz('${item.id}')">停止</a>
											</c:if>
											<c:if test="${item.jkzt == 1}">
												<a href="#" onclick="jkfb('${item.id}')">发布</a>
												<a href="#" onclick="jkxg('${item.id}')">修改</a>
												<a href="#" onclick="jksc('${item.id}')">删除</a>
											</c:if>
											
											<a href="#" onclick="jkck('${item.id}')">购买信息查看</a>
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
						<input type="button" class="tianjiaanniu" value="添加服务"
							onclick="window.location='sjjkedit.action'"/>
				
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
