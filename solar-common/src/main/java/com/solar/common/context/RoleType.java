package com.solar.common.context;

import java.util.HashMap;
import java.util.Map;

public enum RoleType {
	ADMIN(10),
	OPERATOR(20),
	USER(30),
	DEVICE(40),
	UNKNOW(50);

	private static Map<Integer, RoleType> typeMap = new HashMap<>();
	static {
		RoleType[] values = RoleType.values();
		for (RoleType roleType : values) {
			typeMap.put(roleType.getRole(), roleType);
		}
	}

	private int role;

	private RoleType(int role) {
		this.role = role;
	}

	public int getRole() {
		return role;
	}

	public static RoleType roleType(int role) {
		return typeMap.get(role);
	}

	public String typeName() {
		switch (this) {
		case ADMIN:
			return "管理员";
		case OPERATOR:
			return "运维人员";
		case USER:
			return "环保局人员";
		case DEVICE:
			return "控制设备";
		default:
			return "未知人员";
		}
	}
}
