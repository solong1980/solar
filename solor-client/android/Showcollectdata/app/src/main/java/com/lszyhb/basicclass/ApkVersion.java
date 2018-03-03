package com.lszyhb.basicclass;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ApkVersion extends ShowAbt implements Serializable {
	private Long id;
	/** 版本 */
	private Integer verNo;
	private int type; // 客户端类型 10:apk, 50:dmg,100:mc
	/** 版本名字 */
	private String info;
	private String path;
	private String fileName;
	//private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVerNo() {
		return verNo;
	}

	public void setVerNo(Integer verNo) {
		this.verNo = verNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	//public Date getCreateTime() {
	//	return createTime;
	//}

//	public void setCreateTime(Date createTime) {
	//	this.createTime = createTime;
//	}

}
