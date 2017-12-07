package com.solar.entity;

import java.util.List;

@SuppressWarnings("serial")
public class SoAccountFind extends SoAbtAuth {

	private Long id;
	private String name;
	private String phone;
	private String locationIds;
	private List<SoAccountLocation> locations;
	private int type;// 用户类型
	private String createTime;
	private String vcode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocationIds() {
		return locationIds;
	}

	public void setLocationIds(String locationIds) {
		this.locationIds = locationIds;
	}

	public List<SoAccountLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<SoAccountLocation> locations) {
		this.locations = locations;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

}
