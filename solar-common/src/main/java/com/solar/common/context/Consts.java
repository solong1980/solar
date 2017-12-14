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
	public static final int[] CAPS = { 5, 10, 20, 30, 50, 80, 100 };
	public static final int[] EMISSION_STANDARDS = { 10, 20 };

	public static final int[] ACCOUNT_TYPE = { 20, 30 };

	public static final String REGIEST_VCODE_KEY = "REGIEST_VCODE_KEY";
	public static final String ACCOUNT_FIND_VCODE_KEY = "ACCOUNT_FIND_VCODE_KEY";

	/** 设备类型 及配置参数 */
	public static int[] devTypes = new int[] { 10, 20, 30, 40 };
	public static String[] devices = new String[] { "太阳能板", "电池", "曝气系统", "太阳能控制器" };
	public static int[][] devCountOpts = new int[][] { { 2, 4, 6, 8, 16, 24, 32 }, { 2, 4, 6, 8, 16, 24, 32 },
			{ 2, 2, 3, 3, 6, 9, 12 }, { 1, 1, 1, 2, 4, 6, 8 } };

	public static enum ProjectType {
		SUN_POWER(10),
		SMART(20);
		private int type;

		private ProjectType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public static ProjectType type(int type) {
			if (type == SUN_POWER.type) {
				return ProjectType.SUN_POWER;
			} else
				return ProjectType.SMART;
		}

		public String projectName() {
			if (this == SUN_POWER) {
				return "太阳能污水控制系统";
			} else
				return "智能运维系统";
		}
	}

	public static enum AddrType {
		PROVINCE,
		CITY,
		AREA,
		STREET,
		PROJECT,
		DEVICE;
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

	public static enum AuditResult {
		WAIT_FOR_AUDIT(10),
		AGREE(50),
		REJECT(60);
		private int status;

		private AuditResult(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		public static AuditResult status(int status) {
			if (status == AGREE.status) {
				return AuditResult.AGREE;
			} else if (status == REJECT.status) {
				return AuditResult.REJECT;
			} else
				return AuditResult.WAIT_FOR_AUDIT;
		}

		public String resultName() {
			if (status == AGREE.status) {
				return "审核通过";
			} else if (status == REJECT.status) {
				return "审核不通过";
			} else
				return "待审核";
		}

	}
}
