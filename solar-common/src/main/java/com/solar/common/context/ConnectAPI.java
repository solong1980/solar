package com.solar.common.context;

public class ConnectAPI {

	public ConnectAPI() {
	}

	/**
	 * 指令码
	 */

	// 业务终端指令
	public static int LOGIN_COMMAND = 0x000001;
	// 数据上传指令
	public static int DATA_UPLOAD_COMMAND = 0x000001;
	// 工作模式更新
	public static int WORKING_MODE_UPDATE_COMMAND = 0x000002;
	// 获取模式设置指令
	public static int GET_WORKING_MODE_COMMAND = 0x000003;
	// 警告通知指令
	public static int WARNNING_COMMAND = 0x000004;
	// 升级指令
	public static int APP_UPGRADE_COMMAND = 0x000005;
	
	// 控制终端指令
	public static int CTRL_COMMAND = 0x999999;

	/**
	 * 响应吗
	 */
	// 断开指令
	public static int ZERO_RESPONSE = 0x100000;

	public static int FAILURE_RESPONSE = 0x100001;
	public static int SUCCESS_RESPONSE = 0x100002;
	public static int GET_WORKING_MODE_RESPONSE = 0x100003;
	
	public static int APP_UPGRADE_RESPONSE = 0x100004;

	
	// 警告信息响应
	public static int WARNNING_RESPONSE = 0x100004;

}
