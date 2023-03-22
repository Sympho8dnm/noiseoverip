package org.jim.common;

import org.jim.common.config.ImConfig;
import org.jim.common.exception.ImDecodeException;
import org.jim.common.exception.ImException;
import java.nio.ByteBuffer;

/**
 * @ClassName ImHandler
 * @Description TODO
 * @Author WChao
 * @Date 2020/1/6 2:09
 * @Version 1.0
 **/
public interface ImHandler {
    /**
     * 根据ByteBuffer解码成业务需要的Packet对象.
     * 如果收到的数据不全，导致解码失败，请返回null，在下次消息来时框架层会自动续上前面的收到的数据
     * @param buffer 参与本次希望解码的ByteBuffer
     * @param limit ByteBuffer的limit
     * @param position ByteBuffer的position，不一定是0哦
     * @param readableLength ByteBuffer参与本次解码的有效数据（= limit - position）
     * @param imChannelContext
     * @return
     * @throws ImDecodeException
     */
    ImPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ImChannelContext imChannelContext) throws ImDecodeException;

    /**
     * 编码
     * @param imPacket
     * @param imConfig
     * @param imChannelContext
     * @return
     * @author: WChao
     */
    ByteBuffer encode(ImPacket imPacket, ImConfig imConfig, ImChannelContext imChannelContext);

    /**
     * 处理消息包
     * @param imPacket
     * @param imChannelContext
     * @throws ImException
     * @author: WChao
     */
    void handler(ImPacket imPacket, ImChannelContext imChannelContext) throws ImException;
}
