package com.solar.command.message.response.mcc;

import java.io.IOException;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.command.message.MsgBodyWrap;
import com.solar.command.message.response.ResponseMsg;
import com.solar.common.context.ConnectAPI;

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

	/**
	 * 构造一个通用的响应
	 * 
	 * @param status
	 *            状态 0 正常 1异常
	 * @param rmd
	 *            响应指令
	 * @param json
	 *            对象json字符串
	 * @return
	 */
	public static String build(String msgCode, String msg) {
		// ServerMccResponse mcResponse = new ServerMccResponse(msg);
		// try {
		// mcResponse.output.writeBytes(msg.getBytes());
		// } catch (IOException e) {
		// logger.error("output write RESPONSE:" + msgCode + " fail", e);
		// } finally {
		// mcResponse.output.close();
		// }
		return msgCode + "," + msg;
	}

	public static ServerMccResponse buildError() {
		ServerMccResponse mcResponse = new ServerMccResponse(ConnectAPI.MC_ERROR_RESPONSE);
		try {
			mcResponse.output.writeBytes("ERROR".getBytes());
		} catch (IOException e) {
			logger.error("output write RESPONSE:" + ConnectAPI.MC_ERROR_RESPONSE + " fail", e);
		} finally {
			mcResponse.output.close();
		}
		return mcResponse;
	}

	public static ServerMccResponse buildSuccess() {
		ServerMccResponse mcResponse = new ServerMccResponse(ConnectAPI.MC_SUCCESS_RESPONSE);
		try {
			mcResponse.output.writeBytes("SUCCESS".getBytes());
		} catch (IOException e) {
			logger.error("output write RESPONSE:" + ConnectAPI.MC_SUCCESS_RESPONSE + " fail", e);
		} finally {
			mcResponse.output.close();
		}
		return mcResponse;
	}
}
