package com.byttersoft.ant;

import java.io.File;
import java.io.FileFilter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * 根据文件修改时间列出文件，注意并不处理子目录文件
 * @author pangl
 *
 */
public class ListFilByMD extends Task{
	
	/**
	 * 文件后缀
	 */
	private String extend;
	
	/**
	 * 目录
	 */
	private File dir;
	
	private String property;

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public void execute() throws BuildException {
		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (pathname.isDirectory())
					return false;
				if (!pathname.getName().endsWith(extend))
					return false;
				return true;
			}
		});
		if (files == null || files.length == 0)
			return;
		for (int i=0; i<files.length - 1; i++) {
			for (int j=i; j<files.length; j++) {
				if (files[i].lastModified() > files[j].lastModified()) {
					File t = files[j];
					files[j] = files[i];
					files[i] = t;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (File f : files) {
			sb.append(f.getName() + ";");
		}
		getProject().setProperty(property, sb.toString());
	}
}
