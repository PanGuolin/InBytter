<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.io.File, java.lang.*,
com.byttersoft.patchbuild.*,
com.byttersoft.patchbuild.beans.*, 
com.byttersoft.patchbuild.permission.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%
	pageContext.setAttribute("contextPath", request.getContextPath());
%>
<!doctype html>
<html>
<head>
	<title>拜特构建管理系统</title>
	<link rel="stylesheet" href="${contextPath}/css/common.css" type="text/css" />
	<script type="text/javascript" src="${contextPath}/js/jquery.js"></script>
	<script type="text/javascript" src="${contextPath}/js/pub.js"></script>
</head>
<body>
<div class="outer">
	<div class="main">
		<div class="title">欢迎${session_username}, 当前分支:${session_branch}<br/>
		<span id="message_span"></span>
		</div> 
		<div class="toolbar" id="toolbar">
			<a href="${contextPath}/manage/listpatch.jsp" id="btn_listpatch">补丁包列表</a>
			<a href="${contextPath}/manage/listbuild.jsp" id="btn_listbuild">构建包列表</a>
			<a href="${contextPath}/manage/showbuildlog.jsp" id="btn_showbuildlog">构建日志</a>
			<a href="${contextPath}/manage/addbuild.jsp" id="btn_addbuild">增加构建</a>
			<a href="${contextPath}/manage/listbuildfiles.jsp" id="btn_listbuildfiles">文件查询</a>
			<a href="${contextPath}/index.jsp" id="btn_index">切换用户</a>
		</div>
	</div>
	<div class="blank"></div>
	<div class="content">