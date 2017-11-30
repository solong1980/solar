package com.solar.entity;

@SuppressWarnings("serial")
public class SoAccount extends SoAbtAuth {

	private Long id;
	private String account;
	private String password;
	private Boolean savePwd;

	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Boolean getSavePwd() {
		return savePwd;
	}

	public void setSavePwd(Boolean savePwd) {
		this.savePwd = savePwd;
	}

}
