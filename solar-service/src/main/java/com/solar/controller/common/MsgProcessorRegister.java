package com.solar.controller.common;

import com.solar.command.processor.AccountFindbackCmdProcessor;
import com.solar.command.processor.AppUpgradeCmdProcessor;
import com.solar.command.processor.AppVersionCmdProcessor;
import com.solar.command.processor.DataServerInfoCmdProcessor;
import com.solar.command.processor.DataUploadCmdProcessor;
import com.solar.command.processor.DeviceAccessCmdProcessor;
import com.solar.command.processor.GetAreasCmdProcessor;
import com.solar.command.processor.GetCitiesCmdProcessor;
import com.solar.command.processor.GetProvincesCmdProcessor;
import com.solar.command.processor.GetWorkingModeCmdProcessor;
import com.solar.command.processor.LoginCmdProcessor;
import com.solar.command.processor.OpenAppMsgProcessor;
import com.solar.command.processor.ProjectAddCmdProcessor;
import com.solar.command.processor.RegiestCmdProcessor;
import com.solar.command.processor.VcodeGetCmdProcessor;
import com.solar.command.processor.WorkingModeUpdateCmdProcessor;
import com.solar.common.context.ConnectAPI;

public enum MsgProcessorRegister {
	DATA_UPLOAD(ConnectAPI.DATA_UPLOAD_COMMAND, new DataUploadCmdProcessor()),

	WORKING_MODE_UPDATE(ConnectAPI.WORKING_MODE_UPDATE_COMMAND, new WorkingModeUpdateCmdProcessor()),

	GET_WORKING_MODE(ConnectAPI.GET_WORKING_MODE_COMMAND, new GetWorkingModeCmdProcessor()),

	APP_UPGRADE_COMMAND(ConnectAPI.APP_UPGRADE_COMMAND, new AppUpgradeCmdProcessor()),

	DATA_SERVER_INFO_COMMAND(ConnectAPI.DATA_SERVER_QUERY_COMMAND, new DataServerInfoCmdProcessor()),

	APK_VERSION_QUERY_COMMAND(ConnectAPI.APK_VERSION_QUERY_COMMAND, new AppVersionCmdProcessor()),

	LOGIN_COMMAND(ConnectAPI.LOGIN_COMMAND, new LoginCmdProcessor()),

	DEVICE_ACCESS_COMMAND(ConnectAPI.DEVICE_ACCESS_COMMAND, new DeviceAccessCmdProcessor()),

	ADDR_PROVINCES_QUERY_COMMAND(ConnectAPI.ADDR_PROVINCES_QUERY_COMMAND, new GetProvincesCmdProcessor()),
	ADDR_CITIES_QUERY_COMMAND(ConnectAPI.ADDR_CITIES_QUERY_COMMAND, new GetCitiesCmdProcessor()),
	ADDR_AREAS_QUERY_COMMAND(ConnectAPI.ADDR_AREAS_QUERY_COMMAND, new GetAreasCmdProcessor()),

	ACCOUNT_ADD_COMMAND(ConnectAPI.ACCOUNT_ADD_COMMAND, new RegiestCmdProcessor()),

	VCODE_GET_COMMAND(ConnectAPI.VCODE_GET_COMMAND, new VcodeGetCmdProcessor()),

	PROJECT_ADD_COMMAND(ConnectAPI.PROJECT_ADD_COMMAND, new ProjectAddCmdProcessor()),

	ACCOUNT_FINDBACK_COMMAND(ConnectAPI.ACCOUNT_FINDBACK_COMMAND, new AccountFindbackCmdProcessor()),

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
