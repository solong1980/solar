package com.solar.controller.common;

import com.solar.command.processor.app.AccountAuditCmdProcessor;
import com.solar.command.processor.app.AccountFindAuditCmdProcessor;
import com.solar.command.processor.app.AccountFindQueryCmdProcessor;
import com.solar.command.processor.app.AccountFindbackCmdProcessor;
import com.solar.command.processor.app.AccountProjectCheckCmdProcessor;
import com.solar.command.processor.app.AccountQueryCmdProcessor;
import com.solar.command.processor.app.AccountSelectCmdProcessor;
import com.solar.command.processor.app.AccountUpdateCmdProcessor;
import com.solar.command.processor.app.AppUpgradeCmdProcessor;
import com.solar.command.processor.app.AppVersionCmdProcessor;
import com.solar.command.processor.app.DataServerInfoCmdProcessor;
import com.solar.command.processor.app.DevicesAddCmdProcessor;
import com.solar.command.processor.app.DevicesBatchCmdProcessor;
import com.solar.command.processor.app.DevicesDelCmdProcessor;
import com.solar.command.processor.app.DevicesInProjectCmdProcessor;
import com.solar.command.processor.app.DevicesRunningDataCmdProcessor;
import com.solar.command.processor.app.DevicesScheduleCmdProcessor;
import com.solar.command.processor.app.DevicesUpdateCmdProcessor;
import com.solar.command.processor.app.DevicesUpgradeCtrCmdProcessor;
import com.solar.command.processor.app.GetAreasCmdProcessor;
import com.solar.command.processor.app.GetCitiesCmdProcessor;
import com.solar.command.processor.app.GetProjectWorkingModeCmdProcessor;
import com.solar.command.processor.app.GetProvincesCmdProcessor;
import com.solar.command.processor.app.LoginCmdProcessor;
import com.solar.command.processor.app.OpenAppMsgProcessor;
import com.solar.command.processor.app.ProjectAddCmdProcessor;
import com.solar.command.processor.app.ProjectCalcIChgCmdProcessor;
import com.solar.command.processor.app.ProjectDeleteCmdProcessor;
import com.solar.command.processor.app.ProjectDevicesCtrlCmdProcessor;
import com.solar.command.processor.app.ProjectQueryCmdProcessor;
import com.solar.command.processor.app.ProjectSelectCmdProcessor;
import com.solar.command.processor.app.ProjectUpdateCmdProcessor;
import com.solar.command.processor.app.RegiestCmdProcessor;
import com.solar.command.processor.app.VcodeGetCmdProcessor;
import com.solar.command.processor.app.WorkingModeUpdateCmdProcessor;
import com.solar.common.context.ConnectAPI;

public enum MsgProcessorRegister {
	LOGIN_COMMAND(ConnectAPI.LOGIN_COMMAND, new LoginCmdProcessor()),

	WORKING_MODE_UPDATE(ConnectAPI.WORKING_MODE_UPDATE_COMMAND, new WorkingModeUpdateCmdProcessor()),
	GET_WORKING_MODE(ConnectAPI.GET_WORKING_MODE_COMMAND, new GetProjectWorkingModeCmdProcessor()),
	APP_UPGRADE_COMMAND(ConnectAPI.APP_UPGRADE_COMMAND, new AppUpgradeCmdProcessor()),

	DATA_SERVER_INFO_COMMAND(ConnectAPI.DATA_SERVER_QUERY_COMMAND, new DataServerInfoCmdProcessor()),

	APK_VERSION_QUERY_COMMAND(ConnectAPI.APK_VERSION_QUERY_COMMAND, new AppVersionCmdProcessor()),


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

	DEVICES_IN_PROJECT_COMMAND(ConnectAPI.DEVICES_IN_PROJECT_COMMAND, new DevicesInProjectCmdProcessor()),
	DEVICES_RUNNINGDATA_COMMAND(ConnectAPI.DEVICES_RUNNINGDATA_COMMAND, new DevicesRunningDataCmdProcessor()),
	DEVICES_UPDATE_COMMAND(ConnectAPI.DEVICES_UPDATE_COMMAND, new DevicesUpdateCmdProcessor()),
	DEVICES_ADD_COMMAND(ConnectAPI.DEVICES_ADD_COMMAND, new DevicesAddCmdProcessor()),
	DEVICES_SCHEDULE_COMMAND(ConnectAPI.DEVICES_SCHEDULE_COMMAND, new DevicesScheduleCmdProcessor()),
	DEVICES_UPGRADECTR_COMMAND(ConnectAPI.DEVICES_UPGRADECTR_COMMAND, new DevicesUpgradeCtrCmdProcessor()),
	DEVICES_DEL_COMMAND(ConnectAPI.DEVICES_DEL_COMMAND, new DevicesDelCmdProcessor()),
	PROJECT_DEVICES_CTRL_COMMAND(ConnectAPI.PROJECT_DEVICES_CTRL_COMMAND, new ProjectDevicesCtrlCmdProcessor()),
	DEVICES_BATCH_COMMAND(ConnectAPI.DEVICES_BATCH_COMMAND, new DevicesBatchCmdProcessor()),
	PROJECT_CALC_ICHG_COMMAND(ConnectAPI.PROJECT_CALC_ICHG_COMMAND, new ProjectCalcIChgCmdProcessor()),
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
