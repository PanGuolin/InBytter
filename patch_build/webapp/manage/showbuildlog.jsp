<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.io.File, java.lang.*,
com.byttersoft.patchbuild.*,
com.byttersoft.patchbuild.beans.*, 
com.byttersoft.patchbuild.permission.*,
com.byttersoft.patchbuild.cache.*" %>
<%@ include file="/header.jsp"%>
<%
	String[] waitLogs = BuildQueue.listWaitLog();
	String[] buildLogs = BuildQueue.listBuildLog();
%>

<table id="tb_package">
   	<tr class="head">
       	<td align="left" style="width:100%"><div help_id="log_help">等待构建列表</div>
       	<div id="log_help">点击条目下载详细日志</div>
   	<div style="width:100%; height:300px; overflow:auto;border:#69C solid 1px;">
   	<ul class="list">
   	<%
   		for (int i=waitLogs.length -1; i>=0; i--) {
   			String log = waitLogs[i];
   			int index = log.indexOf(']');
   			if (index != -1) {
   				log = "<a href='" + request.getContextPath() + "/manage/getFile.do?type=log&filename=" +
   				log.substring(1, index) + "'>" + log + "</a>";
   			}
   			out.println("<li>" + log + "</li>");
   		}
   	%>
   	</ul>
   	</div>
   	</td>
   </tr>
   <tr class="head">
       <td><div help_id="log_help2">构建历史信息</div>
       <div id="log_help2">点击条目下载详细日志</div>
       <div style="width:100%; height:300px; overflow:auto; border:#69C solid 1px;">
       <ul class="list">
       <%
       	for (int i=buildLogs.length -1; i>=0; i--) {
       		String log = buildLogs[i];
   			int index = log.indexOf(']');
   			if (index != -1) {
   				log = "<a href='" + request.getContextPath() + "/manage/getFile.do?type=log&filename=" +
   				log.substring(1, index) + "'>" + log + "</a>";
   			}
   			out.println("<li>" + log + "</li>");
       	}
   	%>
   		</ul>
   		</div>  
       </td>
   </tr>
</table>
<script>
setCurrentPage("showbuildlog");
</script>