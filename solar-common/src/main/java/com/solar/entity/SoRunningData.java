package com.solar.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SoRunningData implements Serializable {
	private Long id;
	private Long realTime;
	private Integer warnningCode;
	private String ipAddr;
	private Float chargingVoltage;
	private Float chargingCurrent;
	private Float dcChargingVoltage;
	private Float dcChargingCurrent;
	private Float batteryVoltage;
	private Float batteryChargingCurrent;
	private Float rtNTCTemperature;
	private Float loadCurrent1;
	private Float loadCurrent2;
	private Float loadCurrent3;
	private Float loadCurrent4;
	private String gpsInfo;
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRealTime() {
		return realTime;
	}

	public void setRealTime(Long realTime) {
		this.realTime = realTime;
	}

	public Integer getWarnningCode() {
		return warnningCode;
	}

	public void setWarnningCode(Integer warnningCode) {
		this.warnningCode = warnningCode;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Float getChargingVoltage() {
		return chargingVoltage;
	}

	public void setChargingVoltage(Float chargingVoltage) {
		this.chargingVoltage = chargingVoltage;
	}

	public Float getChargingCurrent() {
		return chargingCurrent;
	}

	public void setChargingCurrent(Float chargingCurrent) {
		this.chargingCurrent = chargingCurrent;
	}

	public Float getDcChargingVoltage() {
		return dcChargingVoltage;
	}

	public void setDcChargingVoltage(Float dcChargingVoltage) {
		this.dcChargingVoltage = dcChargingVoltage;
	}

	public Float getDcChargingCurrent() {
		return dcChargingCurrent;
	}

	public void setDcChargingCurrent(Float dcChargingCurrent) {
		this.dcChargingCurrent = dcChargingCurrent;
	}

	public Float getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(Float batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public Float getBatteryChargingCurrent() {
		return batteryChargingCurrent;
	}

	public void setBatteryChargingCurrent(Float batteryChargingCurrent) {
		this.batteryChargingCurrent = batteryChargingCurrent;
	}

	public Float getRtNTCTemperature() {
		return rtNTCTemperature;
	}

	public void setRtNTCTemperature(Float rtNTCTemperature) {
		this.rtNTCTemperature = rtNTCTemperature;
	}

	public Float getLoadCurrent1() {
		return loadCurrent1;
	}

	public void setLoadCurrent1(Float loadCurrent1) {
		this.loadCurrent1 = loadCurrent1;
	}

	public Float getLoadCurrent2() {
		return loadCurrent2;
	}

	public void setLoadCurrent2(Float loadCurrent2) {
		this.loadCurrent2 = loadCurrent2;
	}

	public Float getLoadCurrent3() {
		return loadCurrent3;
	}

	public void setLoadCurrent3(Float loadCurrent3) {
		this.loadCurrent3 = loadCurrent3;
	}

	public Float getLoadCurrent4() {
		return loadCurrent4;
	}

	public void setLoadCurrent4(Float loadCurrent4) {
		this.loadCurrent4 = loadCurrent4;
	}

	public String getGpsInfo() {
		return gpsInfo;
	}

	public void setGpsInfo(String gpsInfo) {
		this.gpsInfo = gpsInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
