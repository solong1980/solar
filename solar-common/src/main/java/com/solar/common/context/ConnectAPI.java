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
	public static int DATA_UPLOAD_COMMAND = 0x000002;
	// 工作模式更新
	public static int WORKING_MODE_UPDATE_COMMAND = 0x000003;
	// 获取模式设置指令
	public static int GET_WORKING_MODE_COMMAND = 0x000004;
	// 警告通知指令
	public static int WARNNING_COMMAND = 0x000005;
	// 升级指令
	public static int APP_UPGRADE_COMMAND = 0x000006;
	// 查询数据服务器地址
	public static int DATA_SERVER_QUERY_COMMAND = 0x000007;
	// 设备接入
	public static int DEVICE_ACCESS_COMMAND = 0x000008;
	// 控制终端指令
	public static int CTRL_COMMAND = 0x999999;

	/**
	 * 响应吗
	 */
	// 错误
	public static final int ZERO_RESPONSE = 0x100000;
	// 错误信息
	public static final int ERROR_RESPONSE = 0x100000;

	public static final int FAILURE_RESPONSE = 0x100001;
	public static final int SUCCESS_RESPONSE = 0x100002;
	public static final int GET_WORKING_MODE_RESPONSE = 0x100003;
	public static final int APP_UPGRADE_RESPONSE = 0x100004;
	public static final int LOGIN_RESPONSE = 0x100005;
	public static final int DEVICES_ACCESS_RESPONSE = 0x100005;
	// 查询数据服务器地址
	public static final int DATA_SERVER_QUERY_RESPONSE = 0x100006;
	// 警告信息响应
	public static final int WARNNING_RESPONSE = 0x100004;

}
