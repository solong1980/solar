package com.solar.common.context;

public enum AppType {
	APK(10), IPA(50), RPM(100);

	private int type;

	private AppType(int type) {
		this.type = type;
	}

	public int type() {
		return this.type;
	}
}
