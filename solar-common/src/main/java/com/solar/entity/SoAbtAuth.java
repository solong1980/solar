package com.solar.entity;


@SuppressWarnings("serial")
public class SoAbtAuth extends SoAbt {
	private Long custId;
	private Integer role;

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

}
