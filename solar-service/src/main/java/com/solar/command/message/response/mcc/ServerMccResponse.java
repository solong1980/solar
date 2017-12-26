package com.solar.command.message.response.mcc;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.MsgBodyWrap;
import com.solar.command.message.response.ResponseMsg;

public class ServerMccResponse implements ResponseMsg {

	protected static Logger logger = LoggerFactory.getLogger(ServerMccResponse.class);

	protected MsgBodyWrap output = MsgBodyWrap.newInstance4Out();

	private String msgCode;

	/** 必须调用此方法设置消息号 */
	public ServerMccResponse(String msgCode) {
		this.msgCode = msgCode;
	}

	@Override
	public void setMsgCode(int code) {
	}

	public synchronized IoBuffer entireMsg() {
		byte[] code = this.msgCode.getBytes();
		byte[] body = output.toByteArray();

		// 消息包总长度
		int length = code.length + 1 + body.length;
		IoBuffer buf = IoBuffer.allocate(length);
		buf.put(code);
		buf.put((byte) ',');
		buf.put(body);
		buf.flip();
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

	public static String build(String msgCode, String msg) {
		return msgCode + "," + msg;
	}

	public static String buildError() {
		return "ERROR";
	}

	public static String buildSuccess() {
		return "SUCCESS";
	}
}
