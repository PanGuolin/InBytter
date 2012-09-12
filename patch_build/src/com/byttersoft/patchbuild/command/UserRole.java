package com.byttersoft.patchbuild.command;

/**
 * 用户角色枚举
 * @author pangl
 *
 */
public enum UserRole {
	/**
	 * 系统管理员角色
	 */
	admin,
	
	/**
	 * 测试员角色
	 */
	tester,
	
	/**
	 * 发布员角色
	 */
	deployer;

	@Override
	public String toString() {
		if (this == admin)
			return "系统管理员";
		if (this == tester)
			return "测试人员";
		if (this == deployer)
			return "发布人员";
		return super.toString();
	}
	
}
