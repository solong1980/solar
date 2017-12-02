package com.solar.entity;

import java.util.Date;

@SuppressWarnings("serial")
public class SoProject extends SoAbtAuth {

	// 10.太阳能污水处理系统,20.智能运维系统
	public static final int PROJ_TYPE_SUNPOWER = 10;
	public static final int PROJ_TYPE_SMART = 20;

	// 设计处理量 (5,10,20,30,50,100)吨
	public static final int PROJ_CAP_05 = 5;
	public static final int PROJ_CAP_10 = 10;
	public static final int PROJ_CAP_20 = 20;
	public static final int PROJ_CAP_30 = 30;
	public static final int PROJ_CAP_50 = 50;
	public static final int PROJ_CAP_100 = 100;

	// 设备种配置(10.风机 20.水泵 30.控制器 40.太阳能板 50.电池)
	// 排放标准 10.一级 A,  20.一级 B
	public static final int ES_A = 10;
	public static final int ES_B = 20;

	private Long id;
	private String projectName;
	private int type;
	private int capability;
	private int devConfiure;
	private int emissionStandards;
	private int locationId;
	private String street;
	private String workerName;
	private String workerPhone;
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCapability() {
		return capability;
	}

	public void setCapability(int capability) {
		this.capability = capability;
	}

	public int getDevConfiure() {
		return devConfiure;
	}

	public void setDevConfiure(int devConfiure) {
		this.devConfiure = devConfiure;
	}

	public int getEmissionStandards() {
		return emissionStandards;
	}

	public void setEmissionStandards(int emissionStandards) {
		this.emissionStandards = emissionStandards;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerPhone() {
		return workerPhone;
	}

	public void setWorkerPhone(String workerPhone) {
		this.workerPhone = workerPhone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
