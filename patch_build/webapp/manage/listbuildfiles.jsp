<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.byttersoft.patchbuild.service.*,
com.byttersoft.patchbuild.utils.*" %>
<%@ include file="/header.jsp"%>
<%
	String pattern = request.getParameter("pattern");
if (pattern != null && pattern.trim().length() > 0) {
	pageContext.setAttribute("pattern", pattern);
	pageContext.setAttribute("infos", BuildFileService.findBPIByFile(UserUtil.getBranch(request), pattern));
}
%>
	<div>
		<form action="${contextPath}/manage/listbuildfiles.jsp">
			<table>
				<tr>
					<td style="width:200px; text-align:right;"><span help_id="help_find">输入待查找的文件路径：</span>
					<span id="help_find">查找某个文件存在于哪些构建包当中，以解决构建冲突问题</span></td>
					<td><input type="text" style="width:100%" name="pattern" value="${pattern}"/></td>
					<td width="100px"><input type="submit" value="查询"/></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="scrollTable">
	<table class="thead">
	 	<col width="30px"/><col width="200px"/><col width="100px"/>
	 	<col width="120px"/><col width="100px"/><col width="50px"/>
	 	<col width="50px"/><col/>
        <tbody>
		<tr>
			<th>&nbsp;</th>
			<th>构建包</th>
			<th>提交人</th>
			<th>构建时间</th>
			<th>测试员</th>
			<th>SQL</th>
			<th>通过</th>
			<th>依赖</th>
		</tr>
		</tbody>
	</table>
	<div>
        <table class="tbody">
	        <col width="30px"/><col width="200px"/><col width="100px"/>
		 	<col width="120px"/><col width="100px"/><col width="50px"/>
		 	<col width="50px"/><col/>
		 	<tbody>
			<c:forEach var="info" items="${infos}" varStatus="status" >
			<tr FILENAME="${info.fileName}" BRANCH="${info.branchName}" CONFID="${info.config.id}" name="popMenuSource">
				<td>${status.count}</td>
				<td>${info.fileName}</td>
				<td>${info.submiter}</td>
				<td>${info.buildTime}</td>
				<td><c:if test="${info.tester != null}">${info.tester}</c:if></td>
				<td><c:if test="${info.includeSqls}">有</c:if></td>
				<td><c:if test="${info.config.passTS != -1}">是</c:if></td>
				<td>${info.depends}</td>
			</tr>	
			</c:forEach>
			</tbody>
		</table>
	</div>
	</div>
	
<div id="menuDiv">
<ul>
	<li><a href="${contextPath}/manage/getFile.do?filename=%FILENAME%">下载</a>
		<span class="tips">下载构建包内容以便测试</span></li>
	<li><a href="${contextPath}/manage/viewbuild.jsp?fileName=%FILENAME%">查看详细信息</a>
		<span class="tips">查看构建包的详细信息，包括构建说明及历史等</span></li>
	<li onclick="submitForm('cancel', '%CONFID%')">取消构建包
		<span class="tips">将构建包删除，以使开发人员可以重新提交构建</span></li>
	<li onclick="submitForm('test', '%CONFID%')">开始测试
		<span class="tips">标识当前测试人员已经开始对构建包进行测试，其它人无法进行删除等操作</span></li>
	<li onclick="submitForm('canceltest', '%CONFID%')">取消测试
		<span class="tips">测试人员标识放弃对构建包的测试，其它人可以进行测试或取消构建包等</span></li>
	<li onclick="submitForm('pass', '%CONFID%')">测试通过
		<span class="tips">测试人员声明构建包已经被测试，并且没有问题，提醒发布人员该构建包可以发布（除非有依赖）</span></li>
	<li onclick="submitForm('deploy', '%CONFID%')">发布
		<span class="tips">发布人员发布构建包到当天的补丁中</span></li>
</ul>
</div>
<form action="${contextPath}/manage/buildManage.do" id="manageForm" method="POST">
	<input type="hidden" name="action" id="actionField"/>
	<input type="hidden" name="fileName" id="fileField"/>
</form>
<script style="text/javascript">
	//setPgeTitle("构建包列表: ${session_branch}");
	setCurrentPage("btn_listbuildfiles");
	function submitForm(action, file) {
		if (confirm("确定执行操作： " + action + " [${session_branch}-" + file + "] ?")) {
			document.getElementById("actionField").value = action;
			document.getElementById("fileField").value = file;
			document.getElementById("manageForm").submit();
		}
	}
	
	//addLoadEvent(new function() {devideTable("tb_package", "scrollerDiv");});
</script>
<%@ include file="/footer.jsp"%>