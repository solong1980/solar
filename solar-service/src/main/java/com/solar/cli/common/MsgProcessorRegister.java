package com.solar.cli.common;

import com.solar.cli.netty.command.LoginCmdProcessor;
import com.solar.cli.netty.command.OpenAppMsgProcessor;
import com.solar.cli.netty.controller.MsgProcessor;
import com.solar.common.context.ConnectAPI;

public enum MsgProcessorRegister {
	LOGIN_COMMAND(ConnectAPI.LOGIN_COMMAND, new LoginCmdProcessor()),

	 
	// 内部使用，服务端通知客户端断开
	EMPYTCOMMAND(ConnectAPI.ZERO_RESPONSE, new OpenAppMsgProcessor());

	private int msgCode;
	private MsgProcessor processor;

	private MsgProcessorRegister(int msgCode, MsgProcessor processor) {
		this.msgCode = msgCode;
		this.processor = processor;
	}

	/**
	 * 获取协议号
	 * 
	 * @return
	 */
	public int getMsgCode() {
		return this.msgCode;
	}

	/**
	 * 获取对应的协议解晰类对象
	 * 
	 * @return
	 */
	public MsgProcessor getMsgProcessor() {
		return this.processor;
	}
}
