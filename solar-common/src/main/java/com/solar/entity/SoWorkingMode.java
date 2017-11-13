package com.solar.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SoWorkingMode implements Serializable {
	private Long id;
	private Boolean continuous;
	private Boolean timing;
	private Date pointOfTime;
	private Boolean fixedTimingLength;
	private Integer timeInterval;
	private Boolean emergency;
	private Date createTime;
	private Long createBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getContinuous() {
		return continuous;
	}

	public void setContinuous(Boolean continuous) {
		this.continuous = continuous;
	}

	public Boolean getTiming() {
		return timing;
	}

	public void setTiming(Boolean timing) {
		this.timing = timing;
	}

	public Date getPointOfTime() {
		return pointOfTime;
	}

	public void setPointOfTime(Date pointOfTime) {
		this.pointOfTime = pointOfTime;
	}

	public Boolean getFixedTimingLength() {
		return fixedTimingLength;
	}

	public void setFixedTimingLength(Boolean fixedTimingLength) {
		this.fixedTimingLength = fixedTimingLength;
	}

	public Integer getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(Integer timeInterval) {
		this.timeInterval = timeInterval;
	}

	public Boolean getEmergency() {
		return emergency;
	}

	public void setEmergency(Boolean emergency) {
		this.emergency = emergency;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

}
