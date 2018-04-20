package com.solar.entity;

import java.util.Date;

@SuppressWarnings("serial")
public class SoDevices extends SoAbtAuth {
	public static final int ACCESS_SUCCESS = 0;
	public static final int ACCESS_FAILURE = 1;

	private Long id;
	private String devNo;
	private Integer boxNo;
	private String locationId;
	private Long projectId;

	private Short sw0;
	private Short sw1;
	private Short sw2;
	private Short sw3;
	private Short sw4;
	private Short sw5;
	private Short sw6;
	private Short sw7;

	private String gpsInfo;
	private String ipAddr;
	private String dataServerIp;
	private Integer dataServerPort;
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDevNo() {
		return devNo;
	}

	public void setDevNo(String devNo) {
		this.devNo = devNo;
	}

	public Integer getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(Integer boxNo) {
		this.boxNo = boxNo;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Short getSw0() {
		return sw0;
	}

	public void setSw0(Short sw0) {
		this.sw0 = sw0;
	}

	public Short getSw1() {
		return sw1;
	}

	public void setSw1(Short sw1) {
		this.sw1 = sw1;
	}

	public Short getSw2() {
		return sw2;
	}

	public void setSw2(Short sw2) {
		this.sw2 = sw2;
	}

	public Short getSw3() {
		return sw3;
	}

	public void setSw3(Short sw3) {
		this.sw3 = sw3;
	}

	public Short getSw4() {
		return sw4;
	}

	public void setSw4(Short sw4) {
		this.sw4 = sw4;
	}

	public Short getSw5() {
		return sw5;
	}

	public void setSw5(Short sw5) {
		this.sw5 = sw5;
	}

	public Short getSw6() {
		return sw6;
	}

	public void setSw6(Short sw6) {
		this.sw6 = sw6;
	}

	public Short getSw7() {
		return sw7;
	}

	public void setSw7(Short sw7) {
		this.sw7 = sw7;
	}

	public String getGpsInfo() {
		return gpsInfo;
	}

	public void setGpsInfo(String gpsInfo) {
		this.gpsInfo = gpsInfo;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getDataServerIp() {
		return dataServerIp;
	}

	public void setDataServerIp(String dataServerIp) {
		this.dataServerIp = dataServerIp;
	}

	public Integer getDataServerPort() {
		return dataServerPort;
	}

	public void setDataServerPort(Integer dataServerPort) {
		this.dataServerPort = dataServerPort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "[" + devNo + "]";
	}

	public String buildMmcMsg() {
		return this.sw0 + "," + this.sw1 + "," + this.sw2 + "," + this.sw3 + "," + this.sw4 + "," + this.sw5 + ","
				+ this.sw7 + "," + this.sw7;
	}
}
