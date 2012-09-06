package com.byttersoft.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * 判断目录是否为空任务,如果为空则任务失败
 * @author pangl
 *
 */
public class FailDirEmptyTask extends Task{
	
	/**
	 * 检查的目录 
	 */
	private File dir;
	
	private String message = "directory is empty";

	public void setDir(File dir) {
		this.dir = dir;
	}


	@Override
	public void execute() throws BuildException {
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			throw new BuildException(message);
		}
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
