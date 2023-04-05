/**
 * 
 */
package org.jim.common.packets;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * 版本: [1.0]
 * 功能说明: 
 * @author : WChao 创建时间: 2017年7月26日 下午3:13:47
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id;
	 */
	private String userId;
	/**
	 * user nick
	 */
	private String nick;
	/**
	 * 用户头像
	 */
	private String avatar;
	/**
	 * 在线状态(online、offline)
	 */
	private String status;
	/**
	 * 个性签名;
	 */
	private String sign;
	/**
	 * 用户所属终端;
	 */
	private String terminal;
	/**
	 * 好友列表;
	 */
	private List<Group> friends;
	/**
	 * 群组列表;
	 */
	private List<Group> groups;
	/**
	 * 扩展预留字段;
	 */
	private JSONObject extras;
	
	public User(){}

	public User(String userId , String nick){
		this.userId = userId;
		this.nick = nick;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public List<Group> getFriends() {
		return friends;
	}

	public void setFriends(List<Group> friends) {
		this.friends = friends;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public JSONObject getExtras() {
		return extras;
	}

	public void setExtras(JSONObject extras) {
		this.extras = extras;
	}

	public User clone(){
		User cloneUser = new User();
		BeanUtil.copyProperties(this, cloneUser,"friends","groups");
		return cloneUser;
	}

}
