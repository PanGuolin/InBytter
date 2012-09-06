package com.byttersoft.patchbuild.beans;

/**
 * 变更铁SVN文件列表信息
 * @author pangl
 *
 */
public class ChangedSVNFiles {

	/**
	 * 变更的文件路径
	 */
	private String[] paths;
	
	/**
	 * 相关的变量注释信息
	 */
	private String comments;

	public ChangedSVNFiles(String[] paths, String comments) {
		super();
		this.paths = paths;
		this.comments = comments;
	}

	public String[] getPaths() {
		return paths;
	}

	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
