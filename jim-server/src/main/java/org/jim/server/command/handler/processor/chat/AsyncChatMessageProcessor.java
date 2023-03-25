/**
 * Copyright (C), 2016-2018, 便利蜂商贸有限公司
 * FileName: ChatMessageProcessor
 * Author:   WChao
 * Date:     2018/11/18 上午1:36
 * Description: 聊天消息业务处理
 * History:
 * <author>          <time>
 * WChao         2018/11/18 上午1:36
 */
package org.jim.server.command.handler.processor.chat;

import org.jim.common.ImChannelContext;
import org.jim.common.packets.ChatBody;
import org.jim.server.command.handler.processor.SingleProtocolCmdProcessor;
/**
 *
 * 聊天消息异步业务处理器
 *
 * @author WChao
 * @date 2018/11/18 上午1:36
 *
 */
public interface AsyncChatMessageProcessor extends SingleProtocolCmdProcessor {
    /**
     * 聊天消息异步业务处理器执行方法;
     * @param chatBody
     * @param imChannelContext
     * @throws Exception
     */
    void handler(ChatBody chatBody, ImChannelContext imChannelContext);
}