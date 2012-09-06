<%@page language="java" pageEncoding="UTF-8"%>
		</div>
	</div>
<%
	String msg = (String)session.getAttribute("message");
	if (msg != null) {
		session.removeAttribute("message");
		out.println("<script>alert('" + msg + "');</script>");
	}
%>
	
	</body>
</html>
<script type="text/css">

</script>