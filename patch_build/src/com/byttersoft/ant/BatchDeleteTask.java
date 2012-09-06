package com.byttersoft.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;

/**
 * 删除一个目录下的多个文件
 * @author pangl
 *
 */
public class BatchDeleteTask extends Task {

	/**
	 * 要删除的文件目录
	 */
	private File dir;
	
	/**
	 * 以分号分隔的文件列表，支持通配符
	 */
	private String includes;

	@Override
	public void execute() throws BuildException {
		log("删除文件列表:" + includes);
		String[] fs = includes.split(";");
		
		for (String file : fs) {
			Delete delete = new Delete();
			delete.setTaskName("MultiDelete");
			delete.setProject(this.getProject());
			delete.setVerbose(true);
			FileSet fileSet = new FileSet();
			fileSet.setProject(this.getProject());
			fileSet.setDir(dir);
			fileSet.setIncludes(file);
			delete.add(fileSet);
			delete.execute();
		}
		log("删除文件完成");
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public void setIncludes(String includes) {
		this.includes = includes;
	}
	
	public static void main(String[] args) {
		BatchDeleteTask task = new BatchDeleteTask();
		task.setProject(new Project());
		task.setDir(new File("E:\\workspaces\\20120515\\bytter_build\\WEB-INF\\lib"));
		task.setIncludes("bankacc-api*.jar;sort-api*.jar;vouchermng-api*.jar;project-api*.jar;BytterSSH*.jar;innerAccount-api*.jar;budget-api*.jar;allocation-api*.jar;finacial-api*.jar;");
		task.execute();
	}
}
