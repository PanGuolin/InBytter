package com.byttersoft.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;

/**
 * 批量拷贝文件操作<br/>
 * 一次性将指定文件重命名为目标文件
 * @author pangl
 *
 */
public class BatchCopyTask extends Task{

	private String srcFiles;
	
	private String destFiles;
	
	private String delimiter = ";";
	
	private File srcRoot;
	
	private File destRoot;
	
	private String fromDir = null;

	@Override
	public void execute() throws BuildException {
		
		String[] sources = srcFiles.split(delimiter);
		String[] targets = null;
		if (destFiles != null) {
			targets = destFiles.split(delimiter);
			assert sources.length == targets.length;
		}
		for (int i=0; i<sources.length; i++) {
			File source = new File(srcRoot, sources[i]);
			if (!source.exists()) {
				super.log("File dosen't exists: " + source, Project.MSG_WARN);
				continue;
			}
			if (source.isDirectory()) {
				super.log("File is a directory: " + source, Project.MSG_WARN);
				continue;
			}
			File target;
			if (targets != null) {
				target = new File(destRoot, targets[i]);
			} else {
				String path = source.getAbsolutePath();
				path = path.replaceAll("\\\\", "/");
				if (fromDir == null)
					fromDir = srcRoot.getAbsolutePath();
				fromDir = fromDir.replaceAll("\\\\", "/");
				int index = path.indexOf(fromDir);
				path = path.substring(index + fromDir.length());
				target = new File(destRoot, path);
			}
			Copy copy = new Copy();
			copy.setProject(this.getProject());
			copy.setTaskName("batchCopy");
			copy.setFile(source);
			copy.setTofile(target);
			copy.setFailOnError(false);
			copy.setVerbose(true);
			copy.execute();
			//super.log("rename file " + source + " to " + target);
		}
	}

	/**
	 * 设置源文件列表，文件名称之间用 delimiter分隔
	 * @param srcFiles
	 * @see #setDelimiter(String)
	 */
	public void setSrcFiles(String srcFiles) {
		this.srcFiles = srcFiles;
	}

	/**
	 * 设置目标文件列表，文件名称之间用 delimiter分隔<br/>
	 * 目标文件列表的长度必须与源文件列表的长度相等
	 * @param targetFiles
	 * @see #setDelimiter(String)
	 */
	public void setDestFiles(String targetFiles) {
		this.destFiles = targetFiles;
	}

	/**
	 * 设置文件列表的分隔符，默认为";"
	 * @param delimiter
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public File getSrcRoot() {
		return srcRoot;
	}

	public void setSrcRoot(File srcRoot) {
		this.srcRoot = srcRoot;
	}

	public File getDestRoot() {
		return destRoot;
	}

	public void setDestRoot(File destRoot) {
		this.destRoot = destRoot;
	}

	public void setFromDir(String dir) {
		this.fromDir = dir;
	}
	
}
