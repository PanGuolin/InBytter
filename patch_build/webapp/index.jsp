<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.byttersoft.patchbuild.service.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%
pageContext.setAttribute("names", BuildReposManager.listNames());
%>
<!doctype html>
<html>
<head>
	<title>拜特构建管理系统</title>
	<style>
        body{ text-align:center; font-size: 14px; margin:10px 0;}
        #main{ margin:0 auto; width:1000px; text-align:center;}
        #tb_login{ border-collapse:collapse; text-align:left; margin:0 auto; margin-top:200px;}
        #tb_login h1{ margin:5px; padding:0; font-size:16px; font-weight:bold;}
        #tb_login th{ min-width:80px;} 
        #tb_login th, #tb_login td{ border:#69C solid 1px; padding:5px;}
		#tb_login th {min-width:100px; background:#369; color:#FFF;}
        #tb_login a{ color:#369;}
        #tb_login a:hover{ color:#F33;}
        #tb_login #btn_submit{ padding:30px;}
	</style>
</head>
<body>
<div class="outer">
	<div class="blank"></div>
    <div style="">
    	<form action="./login.do" method="POST"> 
        <table id="tb_login">
        	<tr>
            	<th colspan="3">
                	<h1>请登录</h1><span style="font-weight:normal;">${error_msg}</span>
                </th>
            </tr>
            <tr>  
                <td style="text-align:right">用户名：</td>  
                <td><input type="text" name="username" tabindex="1" style="width:120px;"/></td>
                <td rowspan="3"><span style="text-align:right">
                    <input type="submit" id="btn_submit" value="登录" tabindex="4"/>
                </span></td>  
            </tr>  
            <tr>  
                <td style="text-align:right">密码：</td>  
                <td><input type="password" name="password" tabindex="2" style="width:120px;"/></td>
            </tr>  
            <tr>  
                <td style="text-align:right" tabindex="3">代码分支：</td>  
                <td>
                    <select style="width:120px;" name="branch">
                    	<c:forEach var="name" items="${names}">
                    	<option value="${name}">${name}</option>
                    	</c:forEach>
                    	<option value="systemadmin">系统管理</option>
	               </select>
                </td>
            </tr>  
        </table> 
        </form> 
    </div>
</div>
</body>
</html>