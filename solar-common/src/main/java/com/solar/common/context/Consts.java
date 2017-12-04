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

	public static final int[] ACCOUNT_TYPE = { 20, 30 };

	public static final String REGIEST_VCODE_KEY = "REGIEST_VCODE_KEY";
	public static final String ACCOUNT_FIND_VCODE_KEY = "ACCOUNT_FIND_VCODE_KEY";

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

	public static enum GenVCodeType {
		REGIEST(10),
		ACCOUNT_FIND(20);

		private int type;

		private GenVCodeType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public static GenVCodeType type(int type) {
			if (type == REGIEST.type) {
				return GenVCodeType.REGIEST;
			} else
				return GenVCodeType.ACCOUNT_FIND;
		}
	}
}
