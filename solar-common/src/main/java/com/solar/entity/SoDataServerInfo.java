package com.solar.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SoDataServerInfo implements Serializable {
	private Long id;
	private String devId;
	private String serverIP;
	private int port;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
