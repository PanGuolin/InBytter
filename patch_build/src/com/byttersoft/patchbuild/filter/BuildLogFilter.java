package com.byttersoft.patchbuild.filter;

import org.apache.log4j.Logger;

import com.byttersoft.patchbuild.command.BuildCommand;
import com.byttersoft.patchbuild.command.CommandContext;

/**
 * 构建日志过滤器，用于在构建前后输出日志
 * @author pangl
 *
 */
public class BuildLogFilter extends CommandFilterAdapter {
	
	protected static final Logger logger = Logger.getLogger(BuildCommand.class);

	@Override
	public boolean beforeExecute(BuildCommand command) throws Exception {
		CommandContext context = command.getContext();
		logger.info(context.getUser() + " execute " + context.getAction() +
				" on " + context.getFileName() + "(" + context.getBranch() + ")" + 
				" from " + context.getRemoteAddr() + " [" + context.getContextId() + "]");
		return super.beforeExecute(command);
	}

	@Override
	public boolean onError(BuildCommand command, Throwable t) {
		CommandContext context = command.getContext();
		logger.error("execute fail on action: " + context.getAction() + 
				" by " + context.getUser() + "[" + context.getContextId() + "]", t);
		return super.onError(command, t );
	}

	@Override
	public boolean afterExecute(BuildCommand command) {
		CommandContext context = command.getContext();
		logger.info("execute success on action: " + context.getAction() 
				+ " by " + context.getUser() + "[" + context.getContextId() + "]");
		return super.afterExecute(command);
	}
	
	

}
