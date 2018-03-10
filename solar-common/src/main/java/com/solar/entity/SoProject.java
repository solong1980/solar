package com.solar.entity;

import java.util.List;

@SuppressWarnings("serial")
public class SoProject extends SoAbtAuth {

	// 10.太阳能污水处理系统,20.智能运维系统
	public static final int PROJ_TYPE_SUNPOWER = 10;
	public static final int PROJ_TYPE_SMART = 20;

	// 设计处理量 (5,10,20,30,50,100)吨
	// 设备种配置(10.风机 20.水泵 30.控制器 40.太阳能板 50.电池)
	// 排放标准 10.一级 A, 20.一级 B
	private Long id;
	private String projectName;
	private int type;
	private int capability;
	@SuppressWarnings("unused")
	private List<SoDevConfig> devConfiures;

	/** 项目下设备列表 */
	private List<SoDevices> devices;
	private int emissionStandards;
	private String locationId;
	private String street;
	private String workerName;
	private String workerPhone;
	private String createTime;

	private String projectTotalPChg;// 电量

	private SoProjectWorkingMode projectWorkingMode;

	private int state;

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

	public List<SoDevices> getDevices() {
		return devices;
	}

	public void setDevices(List<SoDevices> devices) {
		this.devices = devices;
	}

	public int getEmissionStandards() {
		return emissionStandards;
	}

	public void setEmissionStandards(int emissionStandards) {
		this.emissionStandards = emissionStandards;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getProjectTotalPChg() {
		return projectTotalPChg;
	}

	public void setProjectTotalPChg(String projectTotalPChg) {
		this.projectTotalPChg = projectTotalPChg;
	}

	public SoProjectWorkingMode getProjectWorkingMode() {
		return projectWorkingMode;
	}

	public void setProjectWorkingMode(SoProjectWorkingMode projectWorkingMode) {
		this.projectWorkingMode = projectWorkingMode;
	}

	@Override
	public String toString() {
		return projectName;
	}

}
