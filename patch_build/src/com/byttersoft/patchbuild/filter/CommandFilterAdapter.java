package com.byttersoft.patchbuild.filter;

/**
 * ICommandFilter 的默认实现
 */
import com.byttersoft.patchbuild.command.BuildCommand;

public class CommandFilterAdapter implements ICommandFilter {

	public boolean beforeExecute(BuildCommand command) throws Exception {
		return true;
	}

	public boolean onError(BuildCommand command, Throwable t) {
		return true;
	}

	public boolean afterExecute(BuildCommand command) {
		return true;
	}

}
