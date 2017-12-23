package com.solar.entity;

import java.util.Date;

@SuppressWarnings("serial")
public class SoDevices extends SoAbtAuth {
	public static final int ACCESS_SUCCESS = 0;
	public static final int ACCESS_FAILURE = 1;

	private Long id;
	private String devNo;
	private String locationId;
	private Long projectId;

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

}
