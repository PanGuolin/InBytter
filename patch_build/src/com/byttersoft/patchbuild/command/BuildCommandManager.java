package com.byttersoft.patchbuild.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.byttersoft.patchbuild.filter.ICommandFilter;

/**
 * 构建命令管理类
 * @author pangl
 *
 */
public class BuildCommandManager {
	
	public static final String FILE_CONFIG = "/com/byttersoft/patchbuild/command/action_command.properties";
	
	/**
	 * 配置文件中命令的KEY
	 */
	public static final String CONFIG_KEY_ACTIONS = "actions";
	
	/**
	 * 配置文件中命令的连接符
	 */
	public static final String ACTIONS_SEPARATOR = ",";
	
	/**
	 * 配置文件中过滤器配置的连接符
	 */
	public static final String FILTER_SEPARATOR = ";";
	
	/**
	 * 配置文件中命令对应的过滤器列表
	 */
	public static final String FILTER_SUFFIX = ".filter";
	
	/**
	 * 命令原型管理
	 */
	static Map<String, BuildCommand> CommandRegister = new HashMap<String, BuildCommand>();
	
	static {
		init();
	}
	
	/**
	 * 初始化时注册原型信息
	 */
	static void init() {
		Properties prop = new Properties();
		try {
			prop.load(BuildCommand.class.getResourceAsStream(FILE_CONFIG));
			String actions = prop.getProperty(CONFIG_KEY_ACTIONS);
			if (actions != null) {
				String[] actionList = actions.split(ACTIONS_SEPARATOR);
				//组装原型对象
				for (String action : actionList) {
					String commandCls = prop.getProperty(action);
					BuildCommand command = (BuildCommand)Class.forName(commandCls).newInstance();
					
					String filters = prop.getProperty(action + FILTER_SUFFIX);
					if (filters != null) {
						String[] filterList = filters.split(FILTER_SEPARATOR);
						List<ICommandFilter> oFilterList = new ArrayList<ICommandFilter>();
						for (String filter : filterList) {
							ICommandFilter filterObj = (ICommandFilter)Class.forName(filter).newInstance();
							oFilterList.add(filterObj);
						}
						command.setFilters((ICommandFilter[])oFilterList.toArray(new ICommandFilter[oFilterList.size()]));
					}
					CommandRegister.put(action, command);
				}
			}
		} catch (Exception ex) {
			//这里不能抛出异常也无法进行恢复，所以仅作打印处理
			ex.printStackTrace();
		}
	}
	
	/**
	 * 获取action对应的命令类,使用prototype模式
	 * @param request
	 * @return
	 * @throws CloneNotSupportedException 
	 * @throws Exception
	 */
	public static BuildCommand getCommand(CommandContext context) {
		
		BuildCommand command = CommandRegister.get(context.getAction());
		command = (BuildCommand) command.clone();
		command.setContext(context);
		return command;
	}
}
