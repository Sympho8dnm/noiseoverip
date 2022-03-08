package org.tio.im.server.command.handler;

import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.im.common.ImPacket;
import org.tio.im.common.packets.Command;
import org.tio.im.server.command.AbCmdHandler;
import org.tio.im.server.command.handler.processor.ProcessorIntf;
import org.tio.im.server.command.handler.processor.handshake.HandshakeProcessorIntf;

public class HandshakeReqHandler extends AbCmdHandler {
	
	@Override
	public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
		ProcessorIntf proCmdHandler = this.getProcessor(channelContext);
		if(proCmdHandler == null){
			Aio.remove(channelContext, "没有对应的握手协议处理器HandshakeProCmd...");
			return null;
		}
		HandshakeProcessorIntf handShakeProCmdHandler = (HandshakeProcessorIntf)proCmdHandler;
		ImPacket handShakePacket = handShakeProCmdHandler.handshake(packet, channelContext);
		if (handShakePacket == null) {
			Aio.remove(channelContext, "业务层不同意握手");
		}
		return handShakePacket;
	}

	@Override
	public Command command() {
		return Command.COMMAND_HANDSHAKE_REQ;
	}
}
