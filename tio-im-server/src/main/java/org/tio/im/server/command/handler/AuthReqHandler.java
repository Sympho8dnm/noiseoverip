package org.tio.im.server.command.handler;

import org.apache.log4j.Logger;
import org.tio.core.ChannelContext;
import org.tio.im.common.Const;
import org.tio.im.common.ImPacket;
import org.tio.im.common.packets.AuthReqBody;
import org.tio.im.common.packets.AuthRespBody;
import org.tio.im.common.packets.Command;
import org.tio.im.server.command.CmdHandler;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年9月13日 下午1:39:35
 */
public class AuthReqHandler extends CmdHandler
{
	
	private Logger logger = Logger.getLogger(AuthReqHandler.class);
	
	@Override
	public Object handler(ImPacket packet, ChannelContext channelContext) throws Exception
	{
		if (packet.getBody() == null) {
			throw new Exception("body is null");
		}
		AuthReqBody authReqBody = JSONObject.parseObject(packet.getBody(), AuthReqBody.class);
		String token = authReqBody.getToken() == null ? "" : authReqBody.getToken();
		String data = token +  Const.authkey;
		logger.info(data);
		ImPacket imRespPacket = new ImPacket();
		AuthRespBody authRespBody = new AuthRespBody();
		imRespPacket.setCommand(Command.COMMAND_AUTH_RESP);
		imRespPacket.setBody(JSONObject.toJSONBytes(authRespBody));
		return authRespBody;
	}



	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		
	}

	@Override
	public Command command() {
		return Command.COMMAND_AUTH_REQ;
	}
}
