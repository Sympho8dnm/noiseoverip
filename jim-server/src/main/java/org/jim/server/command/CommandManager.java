/**
 * 
 */
package org.jim.server.command;

import org.jim.common.packets.Command;
import org.jim.server.command.handler.processor.MultiProtocolCmdProcessor;
import org.jim.server.command.handler.processor.SingleProtocolCmdProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 版本: [1.0]
 * 功能说明: 命令执行管理器;
 * @author : WChao 创建时间: 2017年7月17日 下午2:23:41
 */
public class CommandManager{
	/**
	 * 通用cmd处理命令
	 */
	private static  Map<Integer, AbstractCmdHandler> handlerMap = new HashMap<>();
	private static Logger LOG = LoggerFactory.getLogger(CommandManager.class);

	private CommandManager(){};
	
	static{
		 try {
			List<CommandConfiguration> configurations = CommandConfigurationFactory.parseConfiguration();
			init(configurations);
		} catch (Exception e) {
			LOG.error(e.toString(),e);
		}
	}
	
	private static void init(List<CommandConfiguration> configurations) throws Exception{
		for(CommandConfiguration configuration : configurations){
			AbstractCmdHandler cmdHandler = ((Class<AbstractCmdHandler>)Class.forName(configuration.getCmdHandler())).newInstance();
			List<String> cmdProcessors = configuration.getCmdProcessors();
			if(!cmdProcessors.isEmpty()){
				for(String cmdProcessor : cmdProcessors){
					Object cmdProcessorObj = Class.forName(cmdProcessor).newInstance();
					if(cmdProcessorObj instanceof MultiProtocolCmdProcessor){
						cmdHandler.addMultiProtocolProcessor((MultiProtocolCmdProcessor)cmdProcessorObj);
					}else if(cmdProcessorObj instanceof SingleProtocolCmdProcessor){
						cmdHandler.setSingleProcessor((SingleProtocolCmdProcessor)cmdProcessorObj);
					}
				}
			}
			registerCommand(cmdHandler);
		}
	}

	public static AbstractCmdHandler registerCommand(AbstractCmdHandler imCommandHandler) throws Exception{
		if(imCommandHandler == null || imCommandHandler.command() == null) {
			return null;
		}
		int cmd_number = imCommandHandler.command().getNumber();
		if(Command.forNumber(cmd_number) == null) {
			throw new Exception("注册cmd处理器失败,不合法的cmd命令码:" + cmd_number + ",请在Command枚举类中添加!");
		}
		if(handlerMap.get(cmd_number) == null)
		{
			return handlerMap.put(cmd_number,imCommandHandler);
		}
		return null;
	}
	
	public static AbstractCmdHandler removeCommand(Command command){
		if(command == null) {
			return null;
		}
		int cmd_value = command.getNumber();
		if(handlerMap.get(cmd_value) != null)
		{
			return handlerMap.remove(cmd_value);
		}
		return null;
	}
	
	public static <T> T getCommand(Command command,Class<T> clazz){
		AbstractCmdHandler cmdHandler = getCommand(command);
		if(cmdHandler != null){
			return (T)cmdHandler;
		}
		return null;
	}
	
	public static AbstractCmdHandler getCommand(Command command){
		if(command == null) {
			return null;
		}
		return handlerMap.get(command.getNumber());
	}
}
