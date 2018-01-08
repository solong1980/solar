package com.solar.entity;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import com.google.common.io.Files;

@SuppressWarnings("serial")
public class SoAppVersion extends SoAbt implements Serializable {
	private Long id;
	/** 版本 */
	private Integer verNo;
	private int type; // 客户端类型 10:apk, 50:dmg,100:mc
	/** 版本名字 */
	private String info;
	private String path;
	private String fileName;
	private Date createTime;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private int blockCount = 0;

	private byte[] fileData;
	private long size;
	public byte[] getFileData() {
		return fileData;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getBlockCount() {
		return blockCount;
	}

	public void load() throws IOException {
		if (path != null) {
			File file = new File(path);
			if (file.exists() && !file.isDirectory()) {
				byte[] bytes = Files.toByteArray(file);
				fileData = bytes;
				size = file.length();
				int length = fileData.length;
				int c = length / 1024;
				if (length % 1024 == 0) {
					blockCount = c;
				} else {
					blockCount = c + 1;
				}
			}
		}
	}
}
