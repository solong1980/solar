package com.solar.client.msg;

import org.apache.mina.core.buffer.IoBuffer;

public class ClientSendRequest {
	/**
	 * 必须调用此方法设置消息号
	 *
	 * @param msgCode
	 */
	public ClientSendRequest(int msgCode) {
		setMsgCode(msgCode);
	}

	public MsgBodyWrap output = MsgBodyWrap.newInstance4Out();
	private int msgCode;

	/** 必须调用此方法设置消息号 */

	public void setMsgCode(int code) {
		msgCode = code;
	}

	public IoBuffer entireMsg() {
		byte[] body = output.toByteArray();
		/* 标志 byte 长度short */
		int length = MsgProtocol.flagSize + MsgProtocol.lengthSize + MsgProtocol.msgCodeSize + body.length;// 总长度
		IoBuffer buf = IoBuffer.allocate(length);
		buf.put(MsgProtocol.defaultFlag);// 消息开始标志,长连接分辨不同消息
		buf.putInt(body.length + MsgProtocol.msgCodeSize);// 消息长度+消息码大小
		buf.putInt(msgCode);
		buf.put(body);
		buf.flip();
		return buf;
	}

	/**
	 * 释放资源(数据流、对象引用)
	 */
	public void release() {
		if (output != null) {
			output.close();
		}
		output = null;
	}

}
