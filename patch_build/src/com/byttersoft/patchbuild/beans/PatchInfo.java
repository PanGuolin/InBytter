package com.byttersoft.patchbuild.beans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 补丁信息
 * @author pangl
 *
 */
public class PatchInfo implements Comparable<PatchInfo>{

	/**
	 * 补丁文件对象
	 */
	private File file;
	
	/**
	 * 补丁的加密版本
	 */
	private File encodedFile;
	
	/**
	 * 所属分支
	 */
	private String branch;
	
	/**
	 * 包含多少个当天已发布的构建包
	 */
	private int includePBSize = 0;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf('.'));
		this.encodedFile = new File(file.getParentFile(), name + ".patch");
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	public String getEncodedName() {
		if (encodedFile.exists())
			return encodedFile.getName();
		return "";
	}
	
	/**
	 * 是否需要重新加密打包
	 * @return
	 */
	public boolean needReEncode() {
		return !encodedFile.exists() || 
				(encodedFile.lastModified() < file.lastModified());
	}
	
	public String getName() {
		return file.getName();
	}

	public int compareTo(PatchInfo o) {
		int value = (int) (o.file.lastModified() - file.lastModified());
		if (value != 0)
			return value;
		return o.file.getName().compareTo(file.getName());
	}
	
	private static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String getLastModify() {
		Date date = new Date(file.lastModified());
		return formater.format(date);
	}
	
	
	public long getSize() {
		return file.length();
	}

	public int getIncludePBSize() {
		return includePBSize;
	}

	public void setIncludePBSize(int includePBSize) {
		this.includePBSize = includePBSize;
	}
	
}
