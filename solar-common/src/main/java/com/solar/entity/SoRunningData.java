package com.solar.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SoRunningData implements Serializable {
	// rev:01,17DD5E6E,FFFFFFFF, 0, 6,121, 0,0,0,0,18,0,
	// 0,0,0,20171223055949,67.925, 30.473866
	// rev:01,17DD5E6E,FFFFFFFF,233, 6,225, 15,0,0,0, 0,0,17,0,0,0 ,
	// 0,20171224080052,83.872,30.473689
	private Long id;
	private String req;
	private String uuid; // 设备号
	private String fmid; // 固件版本
	private String vssun; // 太阳能板电压
	private String ichg; // 电池充电电流
	private String vbat; // 电池电压
	private String level; // 电池剩余容量
	private String pchg; // 充电累积度数
	private String pdis; // 放电累积度数
	private String ild1; // 负载1电流
	private String ild2; // 负载2电流
	private String ild3; // 负载3电流
	private String ild4; // 负载3电流
	private String temp; // 环境温度
	private String ain1; // 第1路4-20mA
	private String ain2; // 第2路4-20mA
	private String ain3; // 第3路4-20mA
	private String stat; // 控制器状态
	private String utcTime;// GPS时间
	private String altitude;// GPS纬度
	private String longitude;// GPS经度
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReq() {
		return req;
	}

	public void setReq(String req) {
		this.req = req;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFmid() {
		return fmid;
	}

	public void setFmid(String fmid) {
		this.fmid = fmid;
	}

	public String getVssun() {
		return vssun;
	}

	public void setVssun(String vssun) {
		this.vssun = vssun;
	}

	public String getIchg() {
		return ichg;
	}

	public void setIchg(String ichg) {
		this.ichg = ichg;
	}

	public String getVbat() {
		return vbat;
	}

	public void setVbat(String vbat) {
		this.vbat = vbat;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPchg() {
		return pchg;
	}

	public void setPchg(String pchg) {
		this.pchg = pchg;
	}

	public String getPdis() {
		return pdis;
	}

	public void setPdis(String pdis) {
		this.pdis = pdis;
	}

	public String getIld1() {
		return ild1;
	}

	public void setIld1(String ild1) {
		this.ild1 = ild1;
	}

	public String getIld2() {
		return ild2;
	}

	public void setIld2(String ild2) {
		this.ild2 = ild2;
	}

	public String getIld3() {
		return ild3;
	}

	public void setIld3(String ild3) {
		this.ild3 = ild3;
	}

	public String getIld4() {
		return ild4;
	}

	public void setIld4(String ild4) {
		this.ild4 = ild4;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getAin1() {
		return ain1;
	}

	public void setAin1(String ain1) {
		this.ain1 = ain1;
	}

	public String getAin2() {
		return ain2;
	}

	public void setAin2(String ain2) {
		this.ain2 = ain2;
	}

	public String getAin3() {
		return ain3;
	}

	public void setAin3(String ain3) {
		this.ain3 = ain3;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getUtcTime() {
		return utcTime;
	}

	public void setUtcTime(String utcTime) {
		this.utcTime = utcTime;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
