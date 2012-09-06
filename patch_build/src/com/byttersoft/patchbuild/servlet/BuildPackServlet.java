package com.byttersoft.patchbuild.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.byttersoft.patchbuild.BuildQueue;
import com.byttersoft.patchbuild.beans.BuildPackConfig;
import com.byttersoft.patchbuild.utils.UserUtil;

/**
 * 构建包
 * @author MickeyMic
 *
 */
public class BuildPackServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String build_files = req.getParameter("build_files");
		String comment = req.getParameter("comment");
		if (comment != null) {
			comment = new String(comment.getBytes("ISO8859_1"), "UTF-8");
			comment = comment.trim().replaceAll(">", "&gt;");
			comment = comment.replaceAll("<", "&lt;");
		}
		
		BuildPackConfig config = new BuildPackConfig();
		config.setVersion(UserUtil.getBranch(req));
		config.setDevelopers(UserUtil.getUserName(req));
		config.setCustomer("all");
		config.setComment(comment);
		
		String vps = (String)session.getAttribute("keyword");
		config.setVps(vps);
		config.setId(vps.split(";")[0]);
		
		String[] files = build_files.split(";");
		for (String file : files) {
			if (!config.addFile(file)) {
				System.err.println("文件路径不合法:" + file);
			}
		}
		
		try {
			BuildQueue.addBuild(config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e);
		}
		resp.sendRedirect(req.getContextPath() + "/manage/showbuildlog.jsp");
	}
}
