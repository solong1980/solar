package com.solar.entity;

@SuppressWarnings("serial")
public class SoProvinces extends SoAbtAuth {
	private Long id;
	private String provinceid;
	private String province;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
