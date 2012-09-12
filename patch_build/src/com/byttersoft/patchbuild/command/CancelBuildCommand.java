package com.byttersoft.patchbuild.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.byttersoft.patchbuild.beans.BuildConfig;
import com.byttersoft.patchbuild.beans.BuildFile;
import com.byttersoft.patchbuild.beans.RepositoryInfo;
import com.byttersoft.patchbuild.cache.BPICache;
import com.byttersoft.patchbuild.service.BuildFileService;
import com.byttersoft.patchbuild.service.BuildReposManager;

/**
 * 取消构建包
 * @author pangl
 *
 */
public class CancelBuildCommand extends BuildCommand{
	
	private static final String COMMAND_NAME = "取消构建";

	@Override
	protected void doExcute() throws Exception {
		BuildFile buildFile = context.getBuildFile();
		RepositoryInfo repos = BuildReposManager.getByName(buildFile.getBranchName());
		BuildConfig conf = buildFile.getConfig();
		File xmlFile = new File(repos.getWorkspace(), "deleted/" + 
				conf.getId() + "_" + System.currentTimeMillis() + ".xml");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile));
			writer.write(conf.toXML());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		buildFile.getFile().delete();
		BuildFileService.removeDepend(buildFile);
		BPICache.remove(buildFile);
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
