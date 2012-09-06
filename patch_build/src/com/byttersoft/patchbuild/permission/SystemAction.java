package com.byttersoft.patchbuild.permission;


/**
 * 用户对构建包可以执行的操作类型
 * @author pangl
 *
 */
public enum SystemAction {
	
	//取消构建包
	cancel,
	//发布
	deploy,
	//开始测试
	test,
	//测试通过
	pass,
	//取消测试
	canceltest
	
}
