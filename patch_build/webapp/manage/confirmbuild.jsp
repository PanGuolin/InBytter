<%@page language="java" pageEncoding="UTF-8"%>

<%@page import="com.byttersoft.patchbuild.service.*,
com.byttersoft.patchbuild.utils.*" %>
<%@ include file="/header.jsp"%>
<%
String projects = request.getParameter("projects");
String keyword = request.getParameter("keyword");
session.setAttribute("keyword", keyword);
session.setAttribute("projects", projects);
	
if (keyword.indexOf('<') != -1 || keyword.indexOf('>') != -1 || keyword.indexOf(':') != -1) {
	session.setAttribute("message", "VP单列表不能包含非法字符:<,>,:");
	response.sendRedirect(request.getContextPath() + "/manage/addbuild.jsp");
	return;
}

ChangedSVNFiles changedFiles = SVNUtil.listChangedFiles(UserUtil.getBranch(request),
		projects.split(";"), keyword.split(";"));
pageContext.setAttribute("changedFiles", changedFiles);
%>
    <div class="main">
    	<form action="${pageContext.request.contextPath}/manage/buildPack.do" id="confForm" method="POST">
            <table id="tb_package">
                <tr>
                    <td style="width:500px;" class="tv tips_bottom" >
                    	<input type="text" id="file_name" style="width:100%"/>
                    	<span class="tip">将包含在构建包中的文件列表，注意不要将SQL文件加入该列表。SQL文件会根据VP单列表自动加到最终构建包当中</span>
                    </td>
                    <td class="tv">
                    	<button type="button" id="btn_add_file" onclick="add()">增加文件</button>
                    	<button type="button" id="btn_del_file" disabled="disabled" onclick="remove()">移除文件</button>
                    	<button type="button" onclick="window.location='<%=request.getContextPath()%>/manage/addbuild.jsp';">上一步</button>
                    	<button type="button" onclick="doNext()">提交构建</button>
                    	
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                    <select size="30" id="file_list" name="file_list" multiple="multiple" style="width:100%" onchange="changeSelected(this)">
                      <c:forEach var="path" items="${changedFiles.paths}">
                      	<option style="line-height:20px; font-size:14px; border:1px solid black;">${path}</option>
                  	  </c:forEach>
                    </select>
                    </td>
                 </tr>
                 <tr>
                    <td>构建说明<br/>
                    	<textarea rows="10" cols="80" style="width:100%; border:#69C solid 1px;" name="comment">${changedFiles.comments}</textarea>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="build_files" id="build_files"/>
        </form>
    </div>
    
    
<script type="text/javascript">
	function changeSelected(select) {
		value = select.options[select.selectedIndex].value;
		document.getElementById("file_name").value = value;
		document.getElementById("btn_del_file").disabled = false;
	}
	
	function remove() {
		var select = document.getElementById("file_list");
		select.remove(select.selectedIndex);
	}
	
	function add() {
		var value = document.getElementById("file_name").value;
		if (value == "") {
			alert("请指定SVN文件全路径");
			return;
		}
		var select = document.getElementById("file_list");
		var options = select.options;
		for (var i=0; i<options.length; i++) {
			if (options[i].value == value) {
				alert("指定的文件路径已存在！");
				return;
			}
		}
		var newOpt1 = new Option(value, value);
		select.options[options.length] = newOpt1;
		document.getElementById("file_name").value = "";
	}
	
	function doNext() {
		var select = document.getElementById("file_list");
		var options = select.options;
		var value = "";
		for (var i=0; i<options.length; i++) {
			value += options[i].text + ";"
		}
		document.getElementById("build_files").value = value;
		document.getElementById("confForm").submit();
	}
</script>
