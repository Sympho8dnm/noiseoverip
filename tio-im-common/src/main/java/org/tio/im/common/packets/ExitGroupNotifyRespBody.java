/**
 * 
 */
package org.tio.im.common.packets;

/**
 * 版本: [1.0]
 * 功能说明: 退出群组通知消息体
 * 作者: WChao 创建时间: 2017年7月26日 下午5:15:18
 */
public class ExitGroupNotifyRespBody extends Message{
	private User user;
	private String group;
	
	public User getUser() {
		return user;
	}
	public ExitGroupNotifyRespBody setUser(User user) {
		this.user = user;
		return this;
	}
	public String getGroup() {
		return group;
	}
	public ExitGroupNotifyRespBody setGroup(String group) {
		this.group = group;
		return this;
	}
}
