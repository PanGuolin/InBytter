package com.byttersoft.patchbuild.command;

import com.byttersoft.patchbuild.beans.BuildFile;
import com.byttersoft.patchbuild.service.BuildFileService;

/**
 * 发布构建包命令
 * @author pangl
 *
 */
public class DeployBuildCommand extends BuildCommand {
	
	private static final String COMMAND_NAME = "发布构建包";

	@Override
	protected void doExcute() throws Exception {
		BuildFile buildFile = context.getBuildFile();
		BuildFileService.deployPack(buildFile);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	protected UserRole getExecuteRole() {
		return UserRole.deployer;
	}

}
