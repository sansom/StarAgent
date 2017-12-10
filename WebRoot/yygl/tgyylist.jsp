<%@page import="com.core.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>应用列表</title>
	<%@ include file="../include/head.inc"%>
	
	
	<script type="text/javascript">
		
	</script>
</head>
<body >
<%@ include file="yy.inc"%>
<form action="../yygl/tgyylist.action" id="yyForm" name="yyForm" method="post">
	<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
	<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />
	<input type="hidden" id="ptlx" name="yyBean.ptlx" title="平台类型" value="<%=SystemConstants.PTLX_TG %>" />
</form>
</body>
</html>
