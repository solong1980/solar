package com.solar.controller.common;

import com.solar.command.processor.AppUpgradeCmdProcessor;
import com.solar.command.processor.DataServerInfoCmdProcessor;
import com.solar.command.processor.DataUploadCmdProcessor;
import com.solar.command.processor.DeviceAccessCmdProcessor;
import com.solar.command.processor.GetWorkingModeCmdProcessor;
import com.solar.command.processor.LoginCmdProcessor;
import com.solar.command.processor.OpenAppMsgProcessor;
import com.solar.command.processor.WorkingModeUpdateCmdProcessor;
import com.solar.common.context.ConnectAPI;

public enum MsgProcessorRegister {
	DATA_UPLOAD(ConnectAPI.DATA_UPLOAD_COMMAND, new DataUploadCmdProcessor()),
	WORKING_MODE_UPDATE(ConnectAPI.WORKING_MODE_UPDATE_COMMAND, new WorkingModeUpdateCmdProcessor()),
	GET_WORKING_MODE(ConnectAPI.GET_WORKING_MODE_COMMAND, new GetWorkingModeCmdProcessor()),
	APP_UPGRADE_COMMAND(ConnectAPI.APP_UPGRADE_COMMAND, new AppUpgradeCmdProcessor()),
	DATA_SERVER_INFO_COMMAND(ConnectAPI.DATA_SERVER_QUERY_COMMAND, new DataServerInfoCmdProcessor()),

	LOGIN_COMMAND(ConnectAPI.LOGIN_COMMAND, new LoginCmdProcessor()),
	DEVICE_ACCESS_COMMAND(ConnectAPI.DEVICE_ACCESS_COMMAND, new DeviceAccessCmdProcessor()),

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
