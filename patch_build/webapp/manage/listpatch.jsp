<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.byttersoft.patchbuild.service.*,
com.byttersoft.patchbuild.utils.*" %>
<%
	pageContext.setAttribute("infos", PatchService.listPachInfo(UserUtil.getBranch(request)));
%>
<%@ include file="/header.jsp"%>
	<table border="1px" cellspacing="0px" cellpadding="0px" id="tb_package">
		<tr>
			<th>原始补丁</th>
			<th>长度</th>
			<th>最后修改时间</th>
			<th>包含构建包个数</th>
		</tr>
	<c:forEach var="info" items="${infos}" varStatus="status">
		<c:if test="${status.count < 30}">
		<tr INFONAME="${info.name}" name="popMenuSource">
			<td>${info.name}</td>
			<td>${info.size}</td>
			<td>${info.lastModify}</td>
			<td>${info.includePBSize}</td>
		</tr>
		</c:if>
	</c:forEach>
</table>
<div id="menuDiv">
<ul>
	<li><a href="${contextPath}/manage/getFile.do?patchname=%INFONAME%">下载原始补丁</a></li>
	<li><a href="${contextPath}/manage/getFile.do?enpatchname=%INFONAME%">下载加密补丁</a></li>
	<li><a href="${contextPath}/manage/listbuildofpatch.jsp?patch=%INFONAME%">查看构建包列表</a></li>
</ul>
</div>
<form action="${contextPath}/manage/buildManage.do" id="manageForm" method="POST">
	<input type="hidden" name="action" id="actionField"/>
	<input type="hidden" name="branch" id="branchField"/>
	<input type="hidden" name="fileName" id="fileField"/>
</form>
<script style="text/javascript">
	setCurrentPage("listpatch");
	function submitForm(action, branch, file) {
		if (confirm("确定执行操作： " + action + "  [" + branch + "-" + file + "] ?")) {
			document.getElementById("actionField").value = action;
			document.getElementById("branchField").value = branch;
			document.getElementById("fileField").value = file;
			document.getElementById("manageForm").submit();
		}
	}
</script>
<%@ include file="/footer.jsp"%>