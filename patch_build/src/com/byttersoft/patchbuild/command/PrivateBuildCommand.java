package com.byttersoft.patchbuild.command;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.byttersoft.patchbuild.beans.BuildConfig;
import com.byttersoft.patchbuild.beans.BuildFile;
import com.byttersoft.patchbuild.beans.RepositoryInfo;
import com.byttersoft.patchbuild.service.BuildFileService;
import com.byttersoft.patchbuild.service.BuildReposManager;
import com.byttersoft.patchbuild.utils.AntTaskUtil;

/**
 * 获取私家包补丁
 * @author pangl
 *
 */
public class PrivateBuildCommand extends BuildCommand{
	
	private String COMMAND_NAME = "获取私家包";

	@Override
	protected void doExcute() throws Exception {
		BuildFile buildFile = context.getBuildFile();
		BuildConfig config = buildFile.getConfig();
		String[] depends = config.getDepends();
		if (depends != null) {
			List<String> dependList = new ArrayList<String>();
			dependList.addAll(Arrays.asList(depends));
			retriveDepends(buildFile, dependList);
			depends = (String[])dependList.toArray(new String[dependList.size()]);
		}
		RepositoryInfo repos = BuildReposManager.getByName(buildFile.getBranchName());
		File ws = repos.getWorkspace();
		File root = new File(ws, "privateBuild" + buildFile.getConfig().getId() + System.currentTimeMillis());
		root.mkdirs();
		FileWriter writer = new FileWriter(new File(root, "depends.txt"));
		if (depends != null) {
			for (int i=depends.length - 1; i >=0; i--) {
				BuildFile depFile = BuildFileService.getBuildPackInfo(buildFile.getBranchName(), depends[i]);
				AntTaskUtil.unzip(depFile.getFile(), root, null);
				writer.write(depends[i] + "\r\n");
			}
		}
		writer.flush();
		writer.close();
		AntTaskUtil.unzip(buildFile.getFile(), root, null);
		
		File targetFile = new File(repos.getPrivateDir(), buildFile.getFileName());
		AntTaskUtil.zip(root, targetFile, null);
		
		AntTaskUtil.deleteDir(root, null);
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	protected UserRole getExecuteRole() {
		return UserRole.tester;
	}
	
	/**
	 * 获取所有依赖的构建包对象，被依赖的构建包放在列表后面
	 * @param depends
	 * @param buildFile
	 */
	private void retriveDepends(BuildFile buildFile, List<String> dependList) {
		String[] depends = buildFile.getConfig().getDepends();
		if (depends != null) {
			for (String depend : depends) {
				BuildFile depFile = BuildFileService.getBuildPackInfo(buildFile.getBranchName(), depend);
				String id = depFile.getConfig().getId();
				//被依赖的包移到最后
				if (dependList.contains(id)) {
					dependList.remove(id);
				}
				dependList.add(id);
				retriveDepends(depFile, dependList);
			}
		}
	}

}
