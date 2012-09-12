package com.byttersoft.patchbuild.command;

import com.byttersoft.patchbuild.beans.BuildFile;
import com.byttersoft.patchbuild.filter.ICommandFilter;
import com.byttersoft.patchbuild.utils.UserUtil;

/**
 * 命令对象抽象类
 * @author pangl
 *
 */
public abstract class BuildCommand implements Cloneable{
	
	private static final ICommandFilter[] EMPTY_FILTER = new ICommandFilter[0];
	
	/**
	 * 命令上下文信息
	 */
	protected CommandContext context = null;
	
	/**
	 * 实例ID号
	 */
	protected String commandId = null;
	
	/**
	 * 旧有配置内容，用于回滚操作
	 */
	protected String oldXML = null;
	
	protected ICommandFilter[] filters = EMPTY_FILTER;
	
	protected BuildCommand(){
		
	}
	
	void setFilters(ICommandFilter[] filters) {
		if (filters != null)
			this.filters = filters;
	}
	
	/**
	 * 设置上下文环境
	 * @param request
	 */
	void setContext(CommandContext context) {
		this.context = context;
		
	}
	
	/**
	 * 执行命令
	 * @throws Exception
	 */
	public final void execute() throws Exception {
		
		try {
			doBeforeExecute();
			for (ICommandFilter filter : filters) {
				if (!filter.beforeExecute(this))
					break;
			}
			doExcute();
		} catch (Exception ex) {
			for (ICommandFilter filter : filters) {
				if (!filter.onError(this, ex)) {
					break;
				}
			}
			doAfterError(ex);
			
		}
		doAfterExecute();
		for (ICommandFilter filter : filters) {
			if (!filter.afterExecute(this)) {
				break;
			}
		}
	}
	
	/**
	 * 合法性检查， 同时设置buildFile对象
	 * @throws Exception 如果不合法则抛出异常
	 */
	private void valid() throws Exception{
		String fileName = context.getFileName();
		if (fileName.indexOf('/') != -1 || fileName.indexOf('\\') != -1) {
			throw new Exception("文件名不合法：" + fileName);
		}
		
		UserRole role = getExecuteRole();
		if (role != null && !UserUtil.checkRole(context.getBranch(), context.getUser(), role)) {
			throw new Exception("action " + getName() +  " need role: " + role + " to execute.");
		}
		doValid();
	}

	
	/**
	 * 检查合法性
	 * @throws Exception 如果不合法则抛出异常
	 */
	protected void doValid() throws Exception {
		
	}
				
	
	/**
	 * 执行具体任务
	 * @throws Exception 如果执行失败则抛出异常
	 */
	protected abstract void doExcute() throws Exception;
	
	/**
	 * 执行命令之前的操作<br/>
	 * 子类重载必须调用super.doBeforeExecute();
	 * @throws Exception 如果无法继续执行则抛出异常
	 */
	protected void doBeforeExecute() throws Exception {
		valid();
		this.oldXML = context.getBuildFile().getConfig().toXML();
	}
	
	/**
	 * 成功执行命令后的操作
	 * 子类重载必须调用 super.doAfterExecute();
	 */
	protected void doAfterExecute() {
		
		BuildFile buildFile = context.getBuildFile();
		if (!oldXML.equals(buildFile.getConfig().toXML())) {
			buildFile.storeConfig();
		}
		buildFile.addChangeLog(context.getUser(), getName());
	}
	
	/**
	 * 执行失败时执行的操作
	 * @param ex
	 * @throws Exception 如果异常无法忽略则应当继续抛出
	 */
	protected void doAfterError(Exception ex) throws Exception{
		//回滚操作
		if (oldXML != null) {
			context.getBuildFile().updateConfig(oldXML);
		}
		throw ex;
	}
	
	public abstract String getName();
	
	/**
	 * 返回有权限执行的角色,每个命令仅允许指定 一个角色，如果想指定现有的多个角色请创建一个新的角色包含这些角色。
	 * @return 
	 */
	protected abstract UserRole getExecuteRole();

	@Override
	protected BuildCommand clone() {
		BuildCommand command = null;
		try {
			command = (BuildCommand) super.clone();
		} catch (CloneNotSupportedException e) {
			//这里不应当抛出导异常，所以不处理
			e.printStackTrace();
		}
		//因为ICommandFilter对象是无状态的，所以这里使用浅拷贝
		return command;
	}

	public CommandContext getContext() {
		return context;
	}
	
}
