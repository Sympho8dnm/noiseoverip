/**
 * 
 */
package org.tio.im.common.packets;

/**
 * @author WChao
 *
 */
public class HandshakeBody extends Message{

	private byte hbyte;
	
	public HandshakeBody(){}
	public HandshakeBody(byte hbyte){
		this.hbyte = hbyte;
	}
	public byte getHbyte() {
		return hbyte;
	}

	public HandshakeBody setHbyte(byte hbyte) {
		this.hbyte = hbyte;
		return this;
	}
	
}
