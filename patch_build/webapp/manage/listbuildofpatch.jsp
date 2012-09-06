<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.byttersoft.patchbuild.service.*,
com.byttersoft.patchbuild.utils.*"%>
<%@ include file="/header.jsp"%>
<%
	String patch = request.getParameter("patch");
	pageContext.setAttribute("infos", PatchService
			.listBuildPackInfoOfPatch(UserUtil.getBranch(request), patch));
	pageContext.setAttribute("patch", patch);
%>
	<table border="1px" cellspacing="0px" cellpadding="0px" id="tb_package">
		<tr>
			<th>构建包</th>
			<th>提交人</th>
			<th>测试员</th>
			<th>发布时间</th>
			<th>VP列表</th>
			<th>包含SQL</th>
		</tr>
		<c:forEach var="info" items="${infos}">
		<tr FILENAME="${patch}/${info.fileName}" name="popMenuSource">
			<td>${info.fileName}</td>
			<td>${info.submiter}</td>
			<td>${info.tester}</td>
			<td>${info.deployTime}</td>
			<td>${info.vps}</td>
			<td><c:if test="${info.includeSqls}">是</c:if></td>
		</tr>
		</c:forEach>
</table>
<div id="menuDiv">
<ul>
	<li><a href="${contextPath}/manage/getFile.do?fileofpatch=%FILENAME%">下载历史构建包</a></li>
</ul>
</div>
<form action="${contextPath}/manage/buildManage.do" id="manageForm" method="POST">
	<input type="hidden" name="action" id="actionField"/>
	<input type="hidden" name="branch" id="branchField"/>
	<input type="hidden" name="fileName" id="fileField"/>
</form>
<script style="text/javascript">
	setMessage("查看补丁${patch}包含的构建包");
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