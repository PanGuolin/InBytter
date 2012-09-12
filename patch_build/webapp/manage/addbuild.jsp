<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.byttersoft.patchbuild.service.*,
com.byttersoft.patchbuild.utils.*" %>
<%@ include file="/header.jsp"%>
<%
	String projectList = (String)session.getAttribute("projects");
	String[] projects = new String[0];
	if (projectList != null) 
		projects = projectList.split(";");
	pageContext.setAttribute("projects", projects);
	pageContext.setAttribute("repos", BuildReposManager.getByName(UserUtil.getBranch(request)));
	String msg = (String)session.getAttribute("message");
	session.removeAttribute("message");
	if (msg != null) pageContext.setAttribute("message", msg);
%>
    <div class="main">
    	<form action="${contextPath}/manage/confirmbuild.jsp" method="post" id="addForm">
            <table style="border:0; width:100%; text-align:left; margin-bottom:10px;">
            	<tr>
	            	<td class="tl">*VP单列表: </td>
	            	<td class="tv tips_bottom"><div  help_id="vps_help"><input type="text" 
	            		name="keyword" id="keyword" value="${keyword}" size="100" /></div>
	            		<div id="vps_help">多个VP单之间用分号";"连接，构建包会以第一个VP单名称进行命名</div>
	            	</td>
	            	<td class="tv" style="text-align:right;"><button type="button" onclick="submitForm()" rowspan="2">下一步</button></td>
            	</tr>
            </table>
            <table id="tb_package">
                <tr class="head">
                    <td align="left" style="width:47%">
                    	<span help_id="all_proj_help" style="font-weight:bold;">所有工程列表：</span>
                    	<div id="all_proj_help">当前分支的所有工程列表，双击条目移到已选择搜索工程列表当中</div>
                    </td>
                    <td align="left" style="width:47%">
                    	<span help_id="sel_proj_help" style="font-weight:bold;">已选择搜索工程：</span>
                    	<div id="sel_proj_help">目标代码所在的工程，系统将仅会在这些工程中查找相应的文件，
                    	如果不指定任何工程则将执行比较费时的全范围查找，即在所有工程中查找。
                    	注意不需要选择SQL工程，因为SQL文件会根据VP单列表自动添加进来</div>
                    </td>
                </tr>
                <tr>
                    <td align="left" valign="top" class="tips_right">
                    	<select id="allProjects" size="25" multiple="multiple" style="width:100%; border:#69C solid 1px;" ondblclick="move2Right(this, 'allProjects', 'selectedProjects')">
                    	<c:forEach var="proj" items="${repos.projects}">
                      		<option value="${proj}"  ondblclick="return move2Right(this, 'allProjects', 'selectedProjects')">${proj}</option>
                  	  	</c:forEach>
                        </select>
                    </td> 
                    <td align="left" valign="top">
                    	<select id="selectedProjects" name="selectedProjects" size="25" multiple="multiple" style="width:100%" ondblclick="move2Left(this, 'allProjects', 'selectedProjects')">
                    	<c:forEach var="proj" items="${projects}">
                      		<option value="${proj}"  ondblclick="return move2Left(this, 'allProjects', 'selectedProjects')">${proj}</option>
                  	  	</c:forEach>
                    	</select>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="projects" id="projects"/>
        </form>
    </div>
    <script>
    	setCurrentPage("addbuild");
    	<c:if test="${message != null}">alert("${message}")</c:if>
    	function submitForm() {
    		var keyword = document.getElementById("keyword");
    		if (keyword.value == "") {
    			alert("必须输入VP单信息！");
    			return;
    		}
    		var project = document.getElementById("selectedProjects");
    		var options = project.options;
    		//if ($("[name='onlysql']").attr("checked")) {
    		//} else {
	    		if (options.length == 0) {
	    			if (!confirm("没有选中工程将需要更多时间搜索，是否继续？")) {
	    				return;
	    			}
	    		}
    		//}
    		
    		var value = "";
    		
    		for (var i=0; i<options.length; i++) {
    			value += options[i].value + ";";
    		}
    		document.getElementById("projects").value = value;
    		document.getElementById("addForm").submit();
    	}
    </script>
<body>
</html>