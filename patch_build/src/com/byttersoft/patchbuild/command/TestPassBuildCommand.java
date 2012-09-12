package com.byttersoft.patchbuild.command;

import com.byttersoft.patchbuild.beans.BuildFile;


/**
 * 测试通过
 * @author pangl
 *
 */
public class TestPassBuildCommand extends BuildCommand {
	private static final String COMMAND_NAME = "测试通过";

	@Override
	protected void doExcute() throws Exception {
		BuildFile buildFile = context.getBuildFile();
		buildFile.getConfig().setTesters(context.getUser());
		buildFile.getConfig().setPassTS(System.currentTimeMillis());
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
				throw new Exception("当前构建包【" + context.getFileName() + "】正由用户" + currentTester + "测试！");
			}
		}
		super.doValid();
	}
}
