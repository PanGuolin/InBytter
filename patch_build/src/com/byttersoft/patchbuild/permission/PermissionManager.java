package com.byttersoft.patchbuild.permission;

import java.util.Hashtable;

/**
 * 权限管理控制类
 * @author pangl
 *
 */
public class PermissionManager {
	
	private static Hashtable<SystemAction, Permission> permissions = new Hashtable<SystemAction, Permission>();
	
	static {
		add(new Permission(SystemAction.cancel, "([data].tester==null OR [data].tester == [USER.NAME]) AND [data].passTs==-1", UserRole.tester));
		add(new Permission(SystemAction.canceltest, "[data].tester == [USER.NAME]", UserRole.tester));
		add(new Permission(SystemAction.deploy, null, UserRole.deployer));
		add(new Permission(SystemAction.pass, "[data].tester == null OR [data].tester == [USER.NAME]", UserRole.tester));
		add(new Permission(SystemAction.test, "[data].tester == null", UserRole.tester));
	}
	
	private static void add(Permission perm) {
		permissions.put(perm.getAction(), perm);
	}
}
