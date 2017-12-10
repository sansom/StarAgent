<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@page import="org.apache.taglibs.standard.tag.common.core.ForEachSupport"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>云服务器列表</title>
</head>
<body >
<%@ include file="fwq.inc"%>
<form action="../fwqgl/fwqlist.action" id="fwqForm" name="fwqForm" method="post">
	<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
	<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />
	<input type="hidden" id="ptlx" name="fwqBean.ptlx" title="平台类型" value="<%=SystemConstants.PTLX_YUN %>" />
</form>
</body>
</html>
