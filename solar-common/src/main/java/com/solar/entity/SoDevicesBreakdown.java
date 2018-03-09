package com.solar.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SoDevicesBreakdown implements Serializable {
	private Long id;
	private String devNo;
	private Long runningDataId;
	private Date breakTime;
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

	public Long getRunningDataId() {
		return runningDataId;
	}

	public void setRunningDataId(Long runningDataId) {
		this.runningDataId = runningDataId;
	}

	public Date getBreakTime() {
		return breakTime;
	}

	public void setBreakTime(Date breakTime) {
		this.breakTime = breakTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
