/**
 * 
 */
package org.tio.im.server.ws;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpConst;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpRequestDecoder;
import org.tio.im.common.Protocol;
import org.tio.im.server.handler.AbServerHandler;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.common.Opcode;
import org.tio.websocket.common.WsRequestPacket;
import org.tio.websocket.common.WsResponsePacket;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.WsServerAioHandler;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.handler.IWsMsgHandler;

import com.jfinal.kit.PropKit;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年8月3日 下午6:38:36
 */
public class WsServerHandler extends AbServerHandler{
	
	private Logger log = LoggerFactory.getLogger(WsServerHandler.class);
	
	private WsServerAioHandler wsServerAioHandler;
	
	private WsServerConfig wsServerConfig;
	
	private IWsMsgHandler wsMsgHandler;
	
	public WsServerHandler() {}
	
	public WsServerHandler(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler) {
		this(new WsServerAioHandler(wsServerConfig, wsMsgHandler));
		this.wsServerConfig = wsServerConfig;
		this.wsMsgHandler = wsMsgHandler;
	}
	public WsServerHandler(WsServerAioHandler wsServerAioHandler) {
		this.wsServerAioHandler = wsServerAioHandler;
	}
	@Override
	public void init(ServerGroupContext serverGroupContext) {
		PropKit.use("app.properties");
		int port = PropKit.getInt("port");//启动端口
		this.wsServerAioHandler = new WsServerAioHandler(new WsServerConfig(port),new WsMsgHandler());
		log.info("WsServerHandler初始化完毕...");
	}

	@Override
	public boolean isProtocol(ByteBuffer buffer,ChannelContext channelContext){
		Object sessionContext = channelContext.getAttribute();
		if(sessionContext == null){//第一次连接;
			if(buffer != null){
				try{
					HttpRequest request = HttpRequestDecoder.decode(buffer, channelContext);
					if(request.getHeaders().get(HttpConst.RequestHeaderKey.Sec_WebSocket_Key) != null)
					{
						channelContext.setAttribute(new WsSessionContext());
						return true;
					}
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}else if(sessionContext instanceof WsSessionContext){
			return true;
		}
		return false;
	}

	@Override
	public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
		
		return this.wsServerAioHandler.encode(packet, groupContext, channelContext);
	}

	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		this.wsServerAioHandler.handler(packet, channelContext);
	}
	@Override
	public WsRequestPacket decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
		
		return this.wsServerAioHandler.decode(buffer, channelContext);
	}
	
	public WsResponsePacket h(WsRequestPacket websocketPacket, byte[] bytes, Opcode opcode, ChannelContext channelContext) throws Exception {
		WsResponsePacket wsResponsePacket = null;
		if (opcode == Opcode.TEXT) {
			if (bytes == null || bytes.length == 0) {
				Aio.remove(channelContext, "错误的websocket包，body为空");
				return null;
			}
			String text = new String(bytes, wsServerConfig.getCharset());
			Object retObj = wsMsgHandler.onText(websocketPacket, text, channelContext);
			String methodName = "onText";
			wsResponsePacket = processRetObj(retObj, methodName, channelContext);
			return wsResponsePacket;
		} else if (opcode == Opcode.BINARY) {
			if (bytes == null || bytes.length == 0) {
				Aio.remove(channelContext, "错误的websocket包，body为空");
				return null;
			}
			Object retObj = wsMsgHandler.onBytes(websocketPacket, bytes, channelContext);
			String methodName = "onBytes";
			wsResponsePacket = processRetObj(retObj, methodName, channelContext);
			return wsResponsePacket;
		} else if (opcode == Opcode.PING || opcode == Opcode.PONG) {
			log.error("收到" + opcode);
			return null;
		} else if (opcode == Opcode.CLOSE) {
			Object retObj = wsMsgHandler.onClose(websocketPacket, bytes, channelContext);
			String methodName = "onClose";
			wsResponsePacket = processRetObj(retObj, methodName, channelContext);
			return wsResponsePacket;
		} else {
			Aio.remove(channelContext, "错误的websocket包，错误的Opcode");
			return null;
		}
	}
	
	private WsResponsePacket processRetObj(Object obj, String methodName, ChannelContext channelContext) throws Exception {
		WsResponsePacket wsResponsePacket = null;
		if (obj == null) {
			return null;
		} else {
			if (obj instanceof String) {
				String str = (String) obj;
				wsResponsePacket = new WsResponsePacket();
				wsResponsePacket.setBody(str.getBytes(wsServerConfig.getCharset()));
				wsResponsePacket.setWsOpcode(Opcode.TEXT);
				return wsResponsePacket;
			} else if (obj instanceof byte[]) {
				wsResponsePacket = new WsResponsePacket();
				wsResponsePacket.setBody((byte[]) obj);
				wsResponsePacket.setWsOpcode(Opcode.BINARY);
				return wsResponsePacket;
			} else if (obj instanceof WsResponsePacket) {
				return (WsResponsePacket) obj;
			} else if (obj instanceof ByteBuffer) {
				wsResponsePacket = new WsResponsePacket();
				byte[] bs = ((ByteBuffer) obj).array();
				wsResponsePacket.setBody(bs);
				wsResponsePacket.setWsOpcode(Opcode.BINARY);
				return wsResponsePacket;
			} else {
				log.error("{} {}.{}()方法，只允许返回byte[]、ByteBuffer、WsResponsePacket或null，但是程序返回了{}", channelContext, this.getClass().getName(), methodName, obj.getClass().getName());
				return null;
			}
		}

	}
	@Override
	public AbServerHandler build() {
		
		return new WsServerHandler(this.wsServerAioHandler);
	}
	
	@Override
	public String name() {
		
		return Protocol.WEBSOCKET;
	}
	public WsServerAioHandler getWsServerAioHandler() {
		return wsServerAioHandler;
	}

	public void setWsServerAioHandler(WsServerAioHandler wsServerAioHandler) {
		this.wsServerAioHandler = wsServerAioHandler;
	}
}
