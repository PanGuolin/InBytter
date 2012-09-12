package com.byttersoft.patchbuild.command;

import com.byttersoft.patchbuild.beans.BuildFile;

/**
 * 开始测试构建包命令
 * @author pangl
 *
 */
public class StartTestBuildCommand extends BuildCommand {
	
	private static final String COMMAND_NAME = "开始测试";

	@Override
	protected void doExcute() throws Exception {
		BuildFile buildFile = context.getBuildFile();
		buildFile.getConfig().setTesters(context.getUser());
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	protected UserRole getExecuteRole() {
		return UserRole.tester;
	}

	@Override
	protected void doValid() throws Exception {
		BuildFile buildFile = context.getBuildFile();
		if (buildFile.hasTester()) {
			String currentTester = buildFile.getTester();
			if (!currentTester.equals(context.getUser())) {
				throw new Exception("用户" + currentTester + "已经对构建包【" + context.getFileName() + "】进行测试");
			}
		}
		super.doValid();
	}
}
