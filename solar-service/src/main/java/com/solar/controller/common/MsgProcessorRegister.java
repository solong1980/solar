package com.solar.controller.common;

import com.solar.command.processor.AccountAuditCmdProcessor;
import com.solar.command.processor.AccountFindAuditCmdProcessor;
import com.solar.command.processor.AccountFindQueryCmdProcessor;
import com.solar.command.processor.AccountFindbackCmdProcessor;
import com.solar.command.processor.AccountProjectCheckCmdProcessor;
import com.solar.command.processor.AccountQueryCmdProcessor;
import com.solar.command.processor.AccountSelectCmdProcessor;
import com.solar.command.processor.AccountUpdateCmdProcessor;
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
import com.solar.command.processor.ProjectDeleteCmdProcessor;
import com.solar.command.processor.ProjectQueryCmdProcessor;
import com.solar.command.processor.ProjectSelectCmdProcessor;
import com.solar.command.processor.ProjectUpdateCmdProcessor;
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

	VCODE_GET_COMMAND(ConnectAPI.VCODE_GET_COMMAND, new VcodeGetCmdProcessor()),

	ACCOUNT_FINDBACK_COMMAND(ConnectAPI.ACCOUNT_FINDBACK_COMMAND, new AccountFindbackCmdProcessor()),
	ACCOUNT_FINDBACK_QUERY_COMMAND(ConnectAPI.ACCOUNT_FINDBACK_QUERY_COMMAND, new AccountFindQueryCmdProcessor()),
	ACCOUNT_FINDBACK_AUDIT_COMMAND(ConnectAPI.ACCOUNT_FINDBACK_AUDIT_COMMAND, new AccountFindAuditCmdProcessor()),

	ACCOUNT_ADD_COMMAND(ConnectAPI.ACCOUNT_ADD_COMMAND, new RegiestCmdProcessor()),
	ACCOUNT_UPDATE_COMMAND(ConnectAPI.ACCOUNT_UPDATE_COMMAND, new AccountUpdateCmdProcessor()),
	ACCOUNT_QUERY_COMMAND(ConnectAPI.ACCOUNT_QUERY_COMMAND, new AccountQueryCmdProcessor()),
	ACCOUNT_AUDIT_QUERY_COMMAND(ConnectAPI.ACCOUNT_AUDIT_QUERY_COMMAND, new AccountQueryCmdProcessor()),
	ACCOUNT_AUDIT_COMMAND(ConnectAPI.ACCOUNT_AUDIT_COMMAND, new AccountAuditCmdProcessor()),
	ACCOUNT_PROJECT_CHECK_COMMAND(ConnectAPI.ACCOUNT_PROJECT_CHECK_COMMAND, new AccountProjectCheckCmdProcessor()),
	ACCOUNT_SELECT_COMMAND(ConnectAPI.ACCOUNT_SELECT_COMMAND, new AccountSelectCmdProcessor()),

	PROJECT_ADD_COMMAND(ConnectAPI.PROJECT_ADD_COMMAND, new ProjectAddCmdProcessor()),
	PROJECT_UPDATE_COMMAND(ConnectAPI.PROJECT_UPDATE_COMMAND, new ProjectUpdateCmdProcessor()),
	PROJECT_DELETE_COMMAND(ConnectAPI.PROJECT_DELETE_COMMAND, new ProjectDeleteCmdProcessor()),
	PROJECT_QUERY_COMMAND(ConnectAPI.PROJECT_QUERY_COMMAND, new ProjectQueryCmdProcessor()),
	PROJECT_SELECT_COMMAND(ConnectAPI.PROJECT_SELECT_COMMAND, new ProjectSelectCmdProcessor()),

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
