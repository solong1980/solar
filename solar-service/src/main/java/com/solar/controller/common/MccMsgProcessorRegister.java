package com.solar.controller.common;

import com.solar.command.processor.mcc.DataUploadCmdProcessor;
import com.solar.command.processor.mcc.DeviceAccessCmdProcessor;
import com.solar.common.context.ConnectAPI;

public enum MccMsgProcessorRegister {
	DEVICES_ACCESS_COMMAND(ConnectAPI.MC_DEVICES_ACCESS_COMMAND, new DeviceAccessCmdProcessor()),
	DATA_UPLOAD(ConnectAPI.MC_DATA_UPLOAD_COMMAND, new DataUploadCmdProcessor());

	private String msgCode;
	private MccMsgProcessor processor;

	private MccMsgProcessorRegister(String msgCode, MccMsgProcessor processor) {
		this.msgCode = msgCode;
		this.processor = processor;
	}

	/**
	 * 获取协议号
	 * 
	 * @return
	 */
	public String getMsgCode() {
		return this.msgCode;
	}

	/**
	 * 获取对应的协议解晰类对象
	 * 
	 * @return
	 */
	public MccMsgProcessor getMsgProcessor() {
		return this.processor;
	}
}
