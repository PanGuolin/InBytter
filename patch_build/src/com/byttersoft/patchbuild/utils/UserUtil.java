package com.byttersoft.patchbuild.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.byttersoft.patchbuild.beans.RepositoryInfo;
import com.byttersoft.patchbuild.service.BuildReposManager;

/**
 * 用户管理工具类
 * @author pangl
 *
 */
public abstract class UserUtil {
	

	/**
	 * 设置测试人员用户列表
	 * @param list 多个用户名之间用分号连接
	 */
	public static void setTesters(String list) {
	}

	/**
	 * 设置发布用户列表
	 * @param deployers 多个用户名之间用分号连接
	 */
	public static void setDeployers(String list) {
	}
	
	/**
	 * 返回当用用户名称
	 * @param request
	 * @return 如果没有登录则返回null
	 */
	public static String getUserName(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userName = (String)session.getAttribute(KEY_SESSION_USERNAME);
		if (userName == null)
			return null;
		return userName.trim();
	}
	
	/**
	 * 返回登录的分支名称
	 * @param request
	 * @return 如果没有登录则返回null
	 */
	public static String getBranch(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String breanch = (String)session.getAttribute(KEY_SESSION_BRANCH);
		if (breanch == null)
			return null;
		return breanch.trim();
	}
	
	/**
	 * 返回当前登录用户的密码
	 * @param request
	 * @return 如果没有登录则返回null
	 */
	public static String getPassword(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String password = (String)session.getAttribute(KEY_SESSION_PASSWORD);
		if (password == null)
			return null;
		return password.trim();
	}
	
	/**
	 * 判断当前用户是否是发布人员
	 * @param request
	 * @return 如果当前没有登录用户或用户不是发布人员则返回false
	 */
	public static boolean isDeployer(HttpServletRequest request) {
		String userName = getUserName(request);
		if (userName == null)
			return false;
		return BuildReposManager.getByName(getBranch(request)).isDeployUser(userName);
	}
	
	public static boolean isDeployer(String userName, String branch) {
		if (userName == null)
			return false;
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		if (repos == null)
			return false;
		return repos.isDeployUser(userName);
	}
	
	/**
	 * 判断当前用户是否测试人员
	 * @param request
	 * @return 如果当前没有登录用户或用户不是发布人员则返回false
	 */
	public static boolean isTester(HttpServletRequest request) {
		return isTester(getUserName(request), getBranch(request));
	}
	
	public static boolean isTester(String userName, String branch) {
		if (userName == null)
			return false;
		return BuildReposManager.getByName(branch).isTestUser(userName);
	}
	
	public static boolean isAmdin(HttpServletRequest request) {
		return false;
	}
	
	public static final String KEY_SESSION_USERNAME = "session_username";
	
	public static final String KEY_SESSION_PASSWORD = "session_password";
	
	public static final String KEY_SESSION_BRANCH = "session_branch";
	
	/**
	 * 登录用户到系统中
	 * @param request
	 * @param userName
	 * @param password
	 * @param branch
	 */
	public static boolean loginUser(HttpServletRequest request, String userName, String password, String branch) {
		RepositoryInfo repos = BuildReposManager.getByName(branch);
		if (repos == null)
			return false;
		if (repos.isTestUser(userName) ||
				repos.isDeployUser(userName) ||
				SVNUtil.checkLogin(branch, userName, password)) {
			HttpSession session = request.getSession();
			session.setAttribute(KEY_SESSION_BRANCH, branch);
			session.setAttribute(KEY_SESSION_PASSWORD, password);
			session.setAttribute(KEY_SESSION_USERNAME, userName);
			return true;
		} else {
			return false;
		}
	}
}
