package com.byttersoft.patchbuild.command;

import com.byttersoft.patchbuild.beans.BuildFile;

/**
 * 取消测试命令
 * @author pangl
 *
 */
public class CancelTestBuildCommand extends BuildCommand {
	private static final String COMMAND_NAME = "取消测试";

	@Override
	protected void doExcute() throws Exception {
		BuildFile buildFile = context.getBuildFile();
		buildFile.getConfig().setTesters(null);
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
				throw new Exception("当前构建包【" + context.getFileName() + "】被正在由用户" + currentTester + "测试，无法取消测试");
			}
		}
		super.doValid();
	}
	
}
