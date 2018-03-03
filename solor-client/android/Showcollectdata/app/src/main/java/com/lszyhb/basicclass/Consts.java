package com.lszyhb.basicclass;

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
	public static String[]  devices= new String[]{"太阳能板","电池","曝气系统","太阳能控制器"};
	public static int[][] devicecountOpts=new int[][]{{2,4,6,8,16,24,32},{2,4,6,8,16,24,32},{2,2,3,3,6,9,12}
			,{1,1,1,2,4,6,8}};
	
	
	public static final int[] ACCOUNT_TYPE = { 10, 30 , 20 };
	
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
