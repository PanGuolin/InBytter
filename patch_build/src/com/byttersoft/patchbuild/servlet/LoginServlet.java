package com.byttersoft.patchbuild.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.byttersoft.patchbuild.utils.SVNUtil;
import com.byttersoft.patchbuild.utils.UserUtil;

/**
 * 登录处理
 * @author pangl
 *
 */
public class LoginServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String deployers = config.getInitParameter("deployers");
		UserUtil.setDeployers(deployers);
		
		String testers = config.getInitParameter("testers");
		UserUtil.setTesters(testers);
		
		String offLine = config.getInitParameter("offline");
		SVNUtil.setOffLineMode(Boolean.parseBoolean(offLine));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		String branch = req.getParameter("branch");
		
		HttpSession session = req.getSession();
		if (userName == null || userName.length() == 0) {
			req.setAttribute("error_msg", "请输入用户名密码");
			session.getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
			return;
		}
		
		if (userName != null)
			userName = userName.trim();
		if (password != null)
			password = password.trim();
		if (branch != null)
			branch = branch.trim();
		
		boolean flag = UserUtil.loginUser(req, userName, password, branch);
		if (!flag) {
			req.setAttribute("error_msg", "用户名或密码无法在分支[" + branch + "]上验证通过！");
			session.getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
			return;
		}
		
		if (UserUtil.isDeployer(req) || UserUtil.isTester(req)) {
			resp.sendRedirect("./manage/listbuild.jsp");
			//session.getServletContext().getRequestDispatcher("/manage/listbuild.jsp").forward(req, resp);
		} else {
			resp.sendRedirect("./manage/addbuild.jsp");
			//session.getServletContext().getRequestDispatcher("/manage/addbuild.jsp").forward(req, resp);
		}
		return;
	}

}
