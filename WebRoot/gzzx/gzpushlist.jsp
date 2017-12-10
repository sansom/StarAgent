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
	<title>推送详情</title>
	<%@ include file="../include/meta.inc" %>
</head>
<body >
	<section id="contentMain" style="padding: 10px;margin: 0px;background:white;">
		<h2 class="infoTitle">推送详情</h2>
		<div>
			<table class="listTable" border="0" cellpadding="0" cellspacing="0">
	          		<colgroup style="width: 5%" />
					<colgroup style="width: 10%" />
					<colgroup style="width: 15%" />
					<colgroup style="width: 15%" />
					<colgroup style="width: 10%" />
				<tbody>
					<tr class="title">
						<th>序号</th>
						<th>推送类型</th>
						<th>收件人昵称</th>
						<th>收件人信息</th>
						<th>推送时间</th>
						<th>推送状态</th>
					</tr>
					<c:if test="${empty list}">
						<tr><td colspan="6" align="center" style="text-align: center;" class="noData">暂时没有推送信息</td></tr>
					</c:if>
					<c:if test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>${status.index + 1}</td>
								<td>${item.tslx_dicvalue}</td>
								<td>${item.tousername}</td>
								<td>${item.touser}</td>
								<td>${empty item.sendtime ? "---" : item.sendtime}</td>
								<td>
									<c:if test="${item.tszt == '01'}">
										<font color="#FE9900">${item.tszt_dicvalue}</font>
									</c:if>
									<c:if test="${item.tszt == '03'}">	
										<font color="red">${item.tszt_dicvalue}</font>
										（已重发【<font color="red">${item.cfcs}</font>】次）
									</c:if>
									<c:if test="${item.tszt == '04'}">
										<font color="green">${item.tszt_dicvalue}</font>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
		<!-- 操作按钮 -->
        <div class="clearfix" style="margin-top: 20px;">
            <div class="labelRight">
                <input type="button" class="defaultBtn backBtn" onclick="self.location=document.referrer;" value="返回" />
            </div>
        </div>
		<div class="pagelist">
			<%@ include file="../include/page.inc"%>
		</div>
	</section>
</body>
</html>