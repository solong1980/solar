package com.solar.gui.component.model;

import com.solar.common.context.Consts.AddrType;

public class TreeAddr {
	private String key;
	private Object value;
	private boolean isArea = false;
	private AddrType addrType;

	public TreeAddr(AddrType addrType, String key, Object object, boolean isArea) {
		super();
		this.addrType = addrType;
		this.key = key;
		this.value = object;
		this.isArea = isArea;
	}

	public AddrType getAddrType() {
		return addrType;
	}

	public TreeAddr(AddrType addrType, String key, Object object) {
		super();
		this.addrType = addrType;
		this.key = key;
		this.value = object;
	}

	public boolean isArea() {
		return isArea;
	}

	public void setArea(boolean isArea) {
		this.isArea = isArea;
	}

	public String getKey() {
		return this.key;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

}