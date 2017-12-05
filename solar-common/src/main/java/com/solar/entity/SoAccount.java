package com.solar.entity;

import java.util.List;

@SuppressWarnings("serial")
public class SoAccount extends SoAbtAuth {

	private Long id;
	private String account;
	private String name;// 用户姓名
	private String phone;// 手机号码
	private String email;// 邮箱地址
	private int type;// 用户类型
	private List<SoAccountLocation> locations;
	private List<SoProject> projects;
	private String password;
	private Boolean savePwd;

	private String vcode;// 验证码

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public List<SoAccountLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<SoAccountLocation> locations) {
		this.locations = locations;
	}

	public List<SoProject> getProjects() {
		return projects;
	}

	public void setProjects(List<SoProject> projects) {
		this.projects = projects;
	}

}
