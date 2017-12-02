package com.solar.common.context;

/**
 * 常量
 */
public class Consts {
	public static final String REQUEST_TOOOOO_FAST = "request tooooo fast !";

	public static final String WORKING_MODE_KEY = "WORKING_MODE_KEY";

	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";

	public static final int[] PROJECT_TYPE = { 10, 20 };
	public static final int[] CAPS = { 5, 10, 20, 30, 50, 100 };
	public static final int[] EMISSION_STANDARDS = { 10, 20 };

	public static enum AddrType {
		PROVINCE,
		CITY,
		AREA,
		STREET;
	}

	public static enum DevInfoType {
		DEV_TYPE,
		DEV_CONFIG,
	}
}
