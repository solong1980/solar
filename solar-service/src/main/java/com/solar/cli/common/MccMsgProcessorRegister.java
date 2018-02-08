package com.solar.cli.common;

import com.solar.cli.netty.command.mcc.DataUploadCmdProcessor;
import com.solar.cli.netty.command.mcc.DeviceWareBlockCmdProcessor;
import com.solar.cli.netty.controller.MccMsgProcessor;
import com.solar.common.context.ConnectAPI;

public enum MccMsgProcessorRegister {
	DATA_UPLOAD(ConnectAPI.MC_DATA_UPLOAD_COMMAND, new DataUploadCmdProcessor()),
	MC_UPDATE_WARE_BLOCK_COMMAND(ConnectAPI.MC_UPDATE_WARE_BLOCK_COMMAND, new DeviceWareBlockCmdProcessor());

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
