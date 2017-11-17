package com.solar.entity;

import java.util.Date;

@SuppressWarnings("serial")
public class SoDevices extends SoAbt {
	private Long id;
	private String devNo;
	private Long custId;
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

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
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

}
