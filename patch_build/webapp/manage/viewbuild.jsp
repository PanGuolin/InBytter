<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.byttersoft.patchbuild.service.*,
com.byttersoft.patchbuild.utils.*" %>

<%@ include file="/header.jsp"%>
<%
	String fileName = request.getParameter("fileName");
	String branch = UserUtil.getBranch(request);
	BuildFile info = BuildPackService.getBuildPackInfo(branch, fileName);
	pageContext.setAttribute("info", info);
	pageContext.setAttribute("conf", info.getConfig());
%>
<br/><br/>
<div class="information">
<span>名称: ${conf.id}</span>
<span>分支: ${info.branchName}</span>
<br/>
 
<span>构建时间: ${info.buildTime}</span>
<span>构建用户: ${conf.developers}</span>
<br/>

<span>包含模块：${conf.modules}</span>
<span>是否包含SQL:<c:if test="${info.includeSqls}">是</c:if></span>
<br/>
<span>VP单列表: ${conf.vps}</span>
<br/>

<span>文件列表:</span>
<textarea style="width:95%; scroller:auto;" rows="10" disabled="disabled">
	<c:forEach var="file" items="${conf.allFiles}">${file}
	</c:forEach>
</textarea>
<span>构建说明:</span>
<textarea style="width:95%; scroller:auto;" rows="5" disabled="disabled">${conf.comment }</textarea>
<span>依赖包列表：${info.depends }</span>
<br/>

<span>测试用户: ${conf.testers }</span>
<span>测试通过时间：${info.testPassTime }</span>
<br/>
<span>变更历史</span>
<textarea rows="5" style="width:95%" disabled="disabled">
<c:forEach var="log" items="${info.changeLogs }">${log }
</c:forEach>
</textarea>
</div>


<%@ include file="/footer.jsp"%>