package com.byttersoft.patchbuild.permission;

import javax.servlet.http.HttpServletRequest;

import com.byttersoft.patchbuild.utils.UserUtil;

/**
 * 系统权限
 * @author pangl
 *
 */
public class Permission {

	/**
	 * 系统功能
	 */
	private final SystemAction action;
	/**
	 * 可接受的用户权限
	 */
	private final UserRole[] acceptRoles;
	/**
	 * 数据权限表达式
	 */
	private final String dataPermExpr;
	public Permission(SystemAction action, String dataPermExpr, UserRole ...acceptRoles) {
		super();
		this.action = action;
		dataPermExpr = dataPermExpr.trim();
		if (dataPermExpr.length() == 0)
			dataPermExpr = null;
		this.dataPermExpr = dataPermExpr;
		this.acceptRoles = acceptRoles;
	}
	

	/**
	 * 检查权限
	 * @param action
	 * @param role
	 * @param data
	 * @return 如果成功则返回0。以下情况会返回成功<br/>
	 * <ol>
	 * <li>action 不匹配,返回成功表示本权限不进行限制</li>
	 * <li>action匹配同时role和data也匹配</li>
	 * </ol>
	 * 返回-1,表示角色不匹配
	 */
	public int checkPermission(SystemAction action, HttpServletRequest req, Object data) {
		if (!this.action.equals(action))
			return 0;
		boolean accept = false;
		for (UserRole uRole : acceptRoles) {
			switch(uRole) {
				case admin:
					accept = UserUtil.isAmdin(req);
					break;
				case tester:
					accept = UserUtil.isTester(req);
					break;
				case developer:
					accept = UserUtil.getUserName(req) != null;
					break;
				case deployer:
					accept = UserUtil.isDeployer(req);
					break;
			}
			if (accept)
				break;
		}
		if (!accept)
			return -1;
		if (this.dataPermExpr == null)
			return 0;
		//执行脚本
		return checkPermExpr(req, data);
	}
	
	private int checkPermExpr( HttpServletRequest req, Object data) {
		return 0;
	}


	public SystemAction getAction() {
		return action;
	}


	public UserRole[] getAcceptRoles() {
		return acceptRoles;
	}


	public String getDataPermExpr() {
		return dataPermExpr;
	}
}
