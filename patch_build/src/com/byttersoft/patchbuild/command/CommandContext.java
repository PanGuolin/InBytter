package com.byttersoft.patchbuild.command;

import javax.servlet.http.HttpServletRequest;

import com.byttersoft.patchbuild.beans.BuildFile;
import com.byttersoft.patchbuild.service.BuildFileService;
import com.byttersoft.patchbuild.utils.UserUtil;

/**
 * 命令上下文对象
 * @author pangl
 *
 */
public class CommandContext {
	
	public static final String KEY_PARAM_ACTION = "action";
	
	/**
	 * 当前用户名
	 */
	private String user = null;
	
	/**
	 * 构建包文件对象
	 */
	private BuildFile buildFile = null;
	
	/**
	 * 操作的文件名称
	 */
	private String fileName = null;
	
	/**
	 * 操作的分支名称 
	 */
	private String branch = null;
	
	/**
	 * 命令名称（ID）
	 */
	private String action = null;
	
	/**
	 * 客户端地址
	 */
	private String remoteAddr = null;
	
	/**
	 * 上下文唯一ID号
	 */
	private String contextId = null;
	
	/**
	 * 从Http请求中初始化上下文
	 * @param request
	 */
	public void init(HttpServletRequest request) {
		action = request.getParameter(KEY_PARAM_ACTION);
		user = UserUtil.getUserName(request);
		fileName = request.getParameter("fileName");
		branch = UserUtil.getBranch(request);
		remoteAddr = request.getRemoteAddr();
		
		buildFile = BuildFileService.getBuildPackInfo(branch, fileName);
		
		contextId = action + System.currentTimeMillis();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public BuildFile getBuildFile() {
		return buildFile;
	}

	public void setBuildFile(BuildFile buildFile) {
		this.buildFile = buildFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}
	
	
	

}
