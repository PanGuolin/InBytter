package com.byttersoft.patchbuild.filter;

import com.byttersoft.patchbuild.command.BuildCommand;

/**
 * 命令执行过滤器,系统中要求所有的过滤器都是无状态的
 * @author pangl
 *
 */
public interface ICommandFilter {
	
	/**
	 * 在BuildCommand.execute方法之前执行<br/>
	 * @param context
	 * @return 如果返回true表示后继的过滤器将被执行，否则放弃后继的过滤器，直接执行execute方法
	 * @throws Exception 如果抛出异常，表示应当放弃后继的过滤器及BuildCommand的execute方法直接返回
	 */
	public boolean beforeExecute(BuildCommand command) throws Exception;
	
	/**
	 * 在BuildCommand.execute方法执行异常时执行
	 * @param context
	 * @return 如果返回true表示后继的过滤器将被执行，否则放弃后继的过滤器
	 */
	public boolean onError(BuildCommand command, Throwable t);
	
	/**
	 * 在BuildCommand.execute方法执行之后执行
	 * @param context
	 * @return 如果返回true表示后继的过滤器将被执行，否则放弃后继的过滤器
	 */
	public boolean afterExecute(BuildCommand command);
	
}
