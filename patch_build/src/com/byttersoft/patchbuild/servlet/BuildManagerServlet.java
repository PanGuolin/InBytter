package com.byttersoft.patchbuild.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.byttersoft.patchbuild.beans.BuildPackInfo;
import com.byttersoft.patchbuild.service.BuildPackService;
import com.byttersoft.patchbuild.utils.UserUtil;

/**
 * 构建包管理
 * @author pangl
 *
 */
public class BuildManagerServlet extends HttpServlet{

	private static final Logger logger = Logger.getLogger(BuildManagerServlet.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		String branch = UserUtil.getBranch(req);
		String fileName = req.getParameter("fileName");
		String user = UserUtil.getUserName(req);
		
		logger.info("[user]: " + UserUtil.getUserName(req) 
				+ ", [branch]: " + branch 
				+ ", [action]:" + action 
				+ ", [file]: " + fileName);
		if (fileName.indexOf('/') != -1 || fileName.indexOf('\\') != -1)
			throw new ServletException("文件名不合法：" + fileName);
		if (!checkPermission(req)) {
			resp.sendRedirect(req.getContextPath() + "/manage/listbuild.jsp");
			return;
		}
		
		BuildPackInfo info = BuildPackService.getBuildPackInfo(branch, fileName);
		long ts = info.addChangeLog(UserUtil.getUserName(req), action);
		HttpSession session = req.getSession();
		String desc = "分支[" + branch + "] 的构建包[" + fileName + "]";
		try {
			//取消构建包
			if ("cancel".equals(action)) {
				BuildPackService.cancel(info);
			}
			//发布构建包
			else if ("deploy".equals(action)) {
				BuildPackService.deployPack(info);
			}
			//开始测试
			else if ("test".equals(action)) {
				BuildPackService.startTest(info, user);
			}
			//测试通过
			else if ("pass".equals(action)) {
				BuildPackService.testPasss(info, user);
			}
			else if ("canceltest".equals(action)) {
				BuildPackService.cancelTest(info);
			} 
			session.setAttribute("message", "执行成功\\n" + desc);
			logger.info("[成功]:" + action);
		} catch (Exception ex) {
			ex.printStackTrace();
			session.setAttribute("message", "执行失败\\n" + desc);
			logger.info("[失败]: " + action);
			info.rollbackChange(ts);
		}
		info.storeConfig();
		resp.sendRedirect(req.getContextPath() + "/manage/listbuild.jsp");
	}

	private boolean checkPermission(HttpServletRequest req) {
		if (UserUtil.isAmdin(req))
			return true;
		String action = req.getParameter("action");
		String branch = UserUtil.getBranch(req);
		String fileName = req.getParameter("fileName");
		BuildPackInfo info = BuildPackService.getBuildPackInfo(branch, fileName);
		String userName = UserUtil.getUserName(req);
		
		if (action.equals("cancel") || action.equals("test") || action.equals("pass") || action.equals("canceltest")) {
			if (!UserUtil.isTester(req)) {
				req.getSession().setAttribute("message", "当前用户不是测试人员，无法执行操作:" + action);
				logger.info("[失败]: 不是测试人员,无法执行操作：" + action);
				return false;
			}
			String test = info.getTester();
			if ("".equals(test))
				test = null;
			if (action.equals("cancel")) {
				if (test != null) {
					req.getSession().setAttribute("message", "请先取消测试" + fileName);
					return false;
				}
			} else if (action.equals("test")) {
				if (test != null) {
					req.getSession().setAttribute("message", "构建包已经开始测试！"+ fileName);
					return false;
				}
			} else if (action.equals("pass")) {
			} else if (action.equals("canceltest")) {
				if (test == null) {
					req.getSession().setAttribute("message", "构建包没有测试用户！" + fileName);
					return false;
				}else if (!test.equals(userName)) {
					req.getSession().setAttribute("message", "无法取消别人正在测试的构建包！" + fileName);
					return false;
				}
			} 
			
		} else if (action.equals("deploy")) {
			if (!UserUtil.isDeployer(req)) {
				req.getSession().setAttribute("message", "当前用户不是发布人员，无法执行操作:" + action);
				logger.info("[失败]: 不是发布用户,无法执行操作：" + action);
				return false;
			}
		}
		return true;
	}
	
}
