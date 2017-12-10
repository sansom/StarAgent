<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@page import="org.apache.taglibs.standard.tag.common.core.ForEachSupport"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>故障推送人员</title>
	<%@ include file="../include/include.jsp" %>
	<script type="text/javascript">
	
		//编辑推送人员
		function tsryEdit(ryid) {
			if (ryid && ryid != null && ryid != "") {
				//编辑
				window.location.href = "tsryedit.action?tsRyBean.id="+ryid;
			}else {
				//添加
				window.location.href = "tsryedit.action";
			}
		}
		
		//删除推送人员
		function tsryDelete(ryid) {
			confirmGo("确定删除该人员信息？", "tsrydelete.action?tsRyBean.id="+ryid);
		}
	</script>
</head>
<body >
	<section id="contentMain">
		<h2 class="infoTitle">故障推送人员列表</h2>
		<div>
			<table class="listTable" border="0" cellpadding="0" cellspacing="0">
	          		<colgroup style="width: 5%" />
					<colgroup style="width: 10%" />
					<colgroup style="width: 10%" />
					<colgroup style="width: 10%" />
					<colgroup style="width: 10%" />
					<colgroup style="width: 15%" />
				<tbody>
					<tr class="title">
						<th>序号</th>
						<th>用户名</th>
						<th>短信推送</th>
						<th>邮箱推送</th>
						<th>微信推送</th>
						<th>操作</th>
					</tr>
					<c:if test="${empty list}">
						<tr><td colspan="6" align="center" style="text-align: center;" class="noData">暂时没有人员信息</td></tr>
					</c:if>
					<c:if test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>${status.index + 1}</td>
								<td>${item.username}</td>
								<td>
									<c:if test="${item.dxts=='0'}">
										<img src="../images/ok0.png" alt="" width="18px;" height="18px;" />
									</c:if>
									<c:if test="${item.dxts=='1'}">
										<img src="../images/err0.png" alt="" width="20px;" height="20px;" />
									</c:if>
								</td>
								<td>
									<c:if test="${item.yxts=='0'}">
										<img src="../images/ok0.png" alt="" width="18px;" height="18px;" />
									</c:if>
									<c:if test="${item.yxts=='1'}">
										<img src="../images/err0.png" alt="" width="20px;" height="20px;" />
									</c:if>
								</td>
								<td>
									<c:if test="${item.wxts=='0'}">
										<img src="../images/ok0.png" alt="" width="18px;" height="18px;" />
									</c:if>
									<c:if test="${item.wxts=='1'}">
										<img src="../images/err0.png" alt="" width="20px;" height="20px;" />
									</c:if>
								</td>
								<td>
									<a href="javascript:void(0)" 
											onclick="tsryEdit('${item.id}')" >编辑</a>
									<a href="javascript:void(0)" 
											onclick="tsryDelete('${item.id}')" >撤销</a>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
		<div class="pagelist">
			<%@ include file="../include/page.inc"%>
		</div>
	</section>
</body>
</html>