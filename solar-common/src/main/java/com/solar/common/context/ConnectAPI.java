package com.solar.common.context;

public class ConnectAPI {

	public ConnectAPI() {
	}

	/**
	 * 指令码
	 */
	// 业务终端指令
	public static final int LOGIN_COMMAND = 0x000001;
	// 工作模式更新
	public static final int WORKING_MODE_UPDATE_COMMAND = 0x000003;
	// 获取模式设置指令
	public static final int GET_WORKING_MODE_COMMAND = 0x000004;
	// 警告通知指令
	public static final int WARNNING_COMMAND = 0x000005;

	// 升级指令
	public static final int APP_UPGRADE_COMMAND = 0x000006;

	// 查询数据服务器地址
	public static final int DATA_SERVER_QUERY_COMMAND = 0x000007;

	// apk版本查询
	public static final int APK_VERSION_QUERY_COMMAND = 0x000009;

	// 11开始
	public static final int ADDR_PROVINCES_QUERY_COMMAND = 0x000011;
	public static final int ADDR_CITIES_QUERY_COMMAND = 0x000012;
	public static final int ADDR_AREAS_QUERY_COMMAND = 0x000013;

	public static final int PROJECT_ADD_COMMAND = 0x000014;
	public static final int PROJECT_UPDATE_COMMAND = 0x000015;
	public static final int PROJECT_DELETE_COMMAND = 0x000016;

	public static final int ACCOUNT_ADD_COMMAND = 0x000017;
	public static final int ACCOUNT_UPDATE_COMMAND = 0x000018;
	public static final int ACCOUNT_DELETE_COMMAND = 0x000019;

	public static final int VCODE_GET_COMMAND = 0x000020;

	public static final int ACCOUNT_FINDBACK_COMMAND = 0x000021;
	public static final int ACCOUNT_FINDBACK_QUERY_COMMAND = 0x000022;
	public static final int ACCOUNT_FINDBACK_AUDIT_COMMAND = 0x000023;

	public static final int ACCOUNT_QUERY_COMMAND = 0x000024;
	public static final int ACCOUNT_AUDIT_COMMAND = 0x000025;
	public static final int ACCOUNT_PROJECT_CHECK_COMMAND = 0x000026;

	public static final int PROJECT_QUERY_COMMAND = 0x000027;

	public static final int PROJECT_SELECT_COMMAND = 0x000028;

	public static final int ACCOUNT_AUDIT_QUERY_COMMAND = 0x000029;

	public static final int ACCOUNT_SELECT_COMMAND = 0x000030;

	public static final int DEVICES_IN_PROJECT_COMMAND = 0x000031;

	public static final int DEVICES_RUNNINGDATA_COMMAND = 0x000032;

	public static final int DEVICES_UPDATE_COMMAND = 0x000033;
	public static final int DEVICES_ADD_COMMAND = 0x000034;
	public static final int DEVICES_SCHEDULE_COMMAND = 0x000035;
	public static final int DEVICES_UPGRADECTR_COMMAND = 0x000036;
	public static final int PROJECT_DEVICES_CTRL_COMMAND = 0x000037;
	public static final int DEVICES_DEL_COMMAND = 0x000038;
	//批量删除,添加
	public static final int DEVICES_BATCH_COMMAND = 0x000039;
	
	//查询项目电量
	public static final int PROJECT_CALC_PCHG_COMMAND = 0x000040;
	
	// 控制终端指令
	public static final int CTRL_COMMAND = 0x999999;

	/**
	 * 响应吗
	 */

	public static final int CONNECTLOST_RESPONSE = -0x100000;
	public static final int ZERO_RESPONSE = 0x100000;
	public static final int ERROR_RESPONSE = 0x100000;
	public static final int FAILURE_RESPONSE = 0x100000;

	public static final int LOGIN_RESPONSE = 0x100001;
	public static final int SUCCESS_RESPONSE = 0x100002;

	public static final int WORKING_MODE_UPDATE_RESPONSE = 0x100003;
	public static final int GET_WORKING_MODE_RESPONSE = 0x100004;

	// 警告信息响应
	public static final int WARNNING_RESPONSE = 0x100005;

	public static final int APP_UPGRADE_RESPONSE = 0x100006;

	// 查询数据服务器地址
	public static final int DATA_SERVER_QUERY_RESPONSE = 0x100007;

	public static final int APP_VERSION_QUERY_RESPONSE = 0x100009;

	public static final int ADDR_PROVINCES_QUERY_RESPONSE = 0x100011;
	public static final int ADDR_CITIES_QUERY_RESPONSE = 0x100012;
	public static final int ADDR_AREAS_QUERY_RESPONSE = 0x100013;

	public static final int PROJECT_ADD_RESPONSE = 0x100014;
	public static final int PROJECT_UPDATE_RESPONSE = 0x100015;
	public static final int PROJECT_DELETE_RESPONSE = 0x100016;

	public static final int ACCOUNT_ADD_RESPONSE = 0x100017;
	public static final int ACCOUNT_UPDATE_RESPONSE = 0x100018;
	public static final int ACCOUNT_DELETE_RESPONSE = 0x100019;

	public static final int VCODE_GET_RESPONSE = 0x000020;

	public static final int ACCOUNT_FINDBACK_RESPONSE = 0x100021;
	public static final int ACCOUNT_FINDBACK_QUERY_RESPONSE = 0x100022;
	public static final int ACCOUNT_FINDBACK_AUDIT_RESPONSE = 0x100023;

	public static final int ACCOUNT_QUERY_RESPONSE = 0x100024;
	public static final int ACCOUNT_AUDIT_RESPONSE = 0x100025;
	public static final int ACCOUNT_PROJECT_CHECK_RESPONSE = 0x100026;

	public static final int PROJECT_QUERY_RESPONSE = 0x100027;
	public static final int PROJECT_SELECT_RESPONSE = 0x100028;

	public static final int ACCOUNT_AUDIT_QUERY_RESPONSE = 0x100029;

	public static final int ACCOUNT_SELECT_RESPONSE = 0x100030;

	public static final int DEVICES_IN_PROJECT_RESPONSE = 0x100031;

	public static final int DEVICES_RUNNINGDATA_RESPONSE = 0x100032;

	public static final int DEVICES_UPDATE_RESPONSE = 0x100033;
	public static final int DEVICES_ADD_RESPONSE = 0x100034;
	public static final int DEVICES_SCHEDULE_RESPONSE = 0x100035;
	public static final int DEVICES_UPGRADECTR_RESPONSE = 0x100036;
	public static final int PROJECT_DEVICES_CTRL_RESPONSE = 0x100037;
	public static final int DEVICES_DEL_RESPONSE = 0x100038;
	public static final int DEVICES_BATCH_RESPONSE = 0x100039;
	public static final int PROJECT_CALC_PCHG_RESPONSE = 0x100040;
	
	// 单片机
	public static final String MC_ERROR_RESPONSE = "00";
	public static final String MC_SUCCESS_RESPONSE = "99";
	// 设备接入
	public static final String MC_DEVICES_ACCESS_COMMAND = "98";
	public static final String MC_DEVICES_ACCESS_RESPONSE = "98";
	// 数据上传指令
	public static final String MC_DATA_UPLOAD_COMMAND = "01";
	public static final String MC_DATA_UPLOAD_RESPONSE = "01";

	public static final String MC_DEVICES_RUNNING_CTRL_RESPONSE = "02";

	public static final String MC_UPDATE_WARE_BLOCK_RESPONE = "03";
	public static final String MC_UPDATE_WARE_BLOCK_COMMAND = "04";
	
	public static final String MC_PROJECT_RUNNINGMODE_RESPONSE = "05";

}
