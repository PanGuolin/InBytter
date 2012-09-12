package com.byttersoft.patchbuild.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.byttersoft.patchbuild.command.BuildCommand;
import com.byttersoft.patchbuild.command.BuildCommandManager;
import com.byttersoft.patchbuild.command.CommandContext;

/**
 * 构建包管理
 * @author pangl
 *
 */
public class BuildManagerServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		CommandContext context = new CommandContext();
		context.init(req);
		BuildCommand command = BuildCommandManager.getCommand(context);
		HttpSession session = req.getSession();
		try {
			command.execute();
			session.setAttribute("message", "执行成功\\n" + command.getName() + context.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", "执行失败\\n" + e.getMessage());
		}
		resp.sendRedirect(req.getContextPath() + "/manage/listbuild.jsp");
	}
	
}
