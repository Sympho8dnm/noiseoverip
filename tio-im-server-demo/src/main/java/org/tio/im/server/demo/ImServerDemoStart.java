/**
 * 
 */
package org.tio.im.server.demo;

import org.tio.im.common.ImConfig;
import org.tio.im.common.packets.Command;
import org.tio.im.server.ImServerStarter;
import org.tio.im.server.command.CommandManager;
import org.tio.im.server.command.handler.HandshakeReqHandler;
import org.tio.im.server.command.handler.LoginReqHandler;
import org.tio.im.server.demo.command.DemoWsHandshakeProcessor;
import org.tio.im.server.demo.init.HttpServerInit;
import org.tio.im.server.demo.listener.ImDemoAioListener;
import org.tio.im.server.demo.service.LoginServiceProcessor;

import com.jfinal.kit.PropKit;

/**
 * 
 * @author WChao
 *
 */
public class ImServerDemoStart {
	
	public static void main(String[] args)throws Exception{
		PropKit.use("app.properties");
		int port = PropKit.getInt("port");//启动端口
		ImConfig imConfig = new ImConfig(null, port);
		HttpServerInit.init(imConfig);
		//ImgMnService.start();//启动爬虫爬取模拟在线人头像;
		ImServerStarter imServerStarter = new ImServerStarter(imConfig,new ImDemoAioListener());
		/*****************start 以下处理器根据业务需要自行添加与扩展，每个Command都可以添加扩展,此处为demo中处理**********************************/
		HandshakeReqHandler handshakeReqHandler = CommandManager.getCommand(Command.COMMAND_HANDSHAKE_REQ,HandshakeReqHandler.class);
		handshakeReqHandler.addProcCmdHandler(new DemoWsHandshakeProcessor());//添加自定义握手处理器;
		LoginReqHandler loginReqHandler = CommandManager.getCommand(Command.COMMAND_LOGIN_REQ,LoginReqHandler.class);
		loginReqHandler.addProcCmdHandler(new LoginServiceProcessor());//添加登录业务处理器;
		/*****************end *******************************************************************************************/
		imServerStarter.start();
	}
}
