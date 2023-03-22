/**
 * 
 */
package org.jim.common.tcp;

import java.nio.ByteBuffer;

import org.jim.common.ImChannelContext;
import org.jim.common.ImPacket;
import org.jim.common.ImSessionContext;
import org.jim.common.exception.ImException;
import org.jim.common.protocol.AbstractProtocol;
import org.jim.common.protocol.IProtocolConverter;
import org.jim.common.utils.ImUtils;

/**
 * Tcp协议判断器
 * @author WChao
 *
 */
public class TcpProtocol extends AbstractProtocol {

	public TcpProtocol(IProtocolConverter converter){
		super(converter);
	}

	@Override
	public String name() {
		return Protocol.TCP;
	}

	@Override
	protected void init(ImChannelContext imChannelContext) {
		imChannelContext.setSessionContext(new TcpSessionContext(imChannelContext));
		ImUtils.setClient(imChannelContext);
	}

	@Override
	public boolean validateProtocol(ImSessionContext imSessionContext) throws ImException {
		if(imSessionContext instanceof TcpSessionContext){
			return true;
		}
		return false;
	}

	@Override
	public boolean validateProtocol(ByteBuffer buffer, ImChannelContext imChannelContext) throws ImException {
		//获取第一个字节协议版本号,TCP协议;
		if(buffer.get() == Protocol.VERSION){
			return true;
		}
		return false;
	}

	@Override
	public boolean validateProtocol(ImPacket imPacket) throws ImException {
		if(imPacket instanceof TcpPacket){
			return true;
		}
		return false;
	}
}
