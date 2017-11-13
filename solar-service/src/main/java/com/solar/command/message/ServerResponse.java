package com.solar.command.message;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.server.mina.net.codec.MsgProtocol;

/**
 * 服务端发给客户端的消息。 所有返回给客户端的消息都最好继承于它.<br>
 * 这里封装了基本的输出字节操作。
 * 
 * @author dyz
 * 
 */
public class ServerResponse implements ResponseMsg {

	protected static Logger logger = LoggerFactory.getLogger(ServerResponse.class);

	protected MsgBodyWrap output = MsgBodyWrap.newInstance4Out();
	private int msgCode;
	private int status;
	private int FIX_LENGTH = MsgProtocol.flagSize + MsgProtocol.lengthSize + MsgProtocol.msgCodeSize  + 4;
	/** 必须调用此方法设置消息号 */
	public ServerResponse(int status, int msgCode) {
		setMsgCode(msgCode);
		setStatus(status);
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMsgCode(int code) {
		msgCode = code;
	}

	public synchronized IoBuffer entireMsg() {
		byte[] body = output.toByteArray();
		// 消息包总长度
		int length = FIX_LENGTH + body.length;
		IoBuffer buf = IoBuffer.allocate(length);
		buf.put(MsgProtocol.defaultFlag);// flag byte
		buf.putInt(length); // lengh int
		buf.putInt(msgCode); // msgCode int
		buf.putInt(status); // status int
		buf.put(body);
		buf.flip();
		// System.out.println("buf实际长度---"+buf.capacity());
		return buf;
	}

	/**
	 * 释放资源(数据流、对象引用)
	 */
	public synchronized void release() {
		if (output != null) {
			output.close();
			output = null;
		}
		output = null;
	}
}
