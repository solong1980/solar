package com.solar.entity;

import java.io.Serializable;

/**
 * 按照需要继承,可以提供业务响应码和提示信息
 * 
 * @author longlh
 *
 */
@SuppressWarnings("serial")
public abstract class SoAbt implements Serializable {
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;

	// 业务响应码
	private int retCode = SUCCESS;
	// 提示信息
	private String msg;

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
