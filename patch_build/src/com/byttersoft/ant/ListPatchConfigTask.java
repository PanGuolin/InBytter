package com.byttersoft.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * 根据时间顺序列出目录中所有配置文件并放到变量当中
 * @author pangl
 *
 */
public class ListPatchConfigTask extends Task{

	/**
	 * 监控的目录 
	 */
	private File monitorDir = null;
	
	/**
	 * 保存的变量名称
	 */
	private String varName = "_patchConfig.file.list";
	
	/**
	 * 是否强制存在
	 */
	private boolean forceExists = false;

	@Override
	public void execute() throws BuildException {
		File[] files = monitorDir.listFiles();
		if (files.length == 0) {
			if (forceExists)
				throw new BuildException("指定目录为空！" + monitorDir.getAbsolutePath());
		}
		for (int i=0; i<files.length-1; i++) {
			for (int j=i; j<files.length; j++) {
				if (files[i].lastModified() > files[j].lastModified()) {
					File f = files[i];
					files[i] = files[j];
					files[j] = f;
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (File f : files) {
			sb.append(f.getAbsolutePath() + ";");
		}
		getProject().setProperty(varName, sb.toString());
	}

	public void setMonitorDir(File monitorDir) {
		this.monitorDir = monitorDir;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public void setForceExists(boolean forceExists) {
		this.forceExists = forceExists;
	}
	
	
}
