<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@	page import="java.util.*,java.text.*,java.math.*"%>
<%@ taglib prefix="jadllogic" uri="jadllogic.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
<head>
<title>首页监控</title>
<%@ include file="../include/include.jsp" %>
</head>
<body onload="">
	<section id="contentMain">
		Loading...
	</section>
</body>
</html>
