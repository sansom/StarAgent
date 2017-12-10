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
	<title>故障中心列表</title>
	<%@ include file="../include/meta.inc" %>
</head>
<body >
	<c:set var="gzlx" value="<%=SystemConstants.GZZX_GZLX_FWQ %>"></c:set>
	<%@ include file="gzzx.inc"%>
	
	<form action="../gzzx/fwqgzzxlist.action" id="gzzxForm" name="gzzxForm" method="post">
		<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
		<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />
		<input type="hidden" id="gzlx" name="gzzxJbxxBean.gzlx" title="故障类型" value="<%=SystemConstants.GZZX_GZLX_FWQ %>" />
	</form>
</body>
</html>