package com.solar.cli;

public class ServerContext {
	private String ip;
	private int devPort;
	private int appPort;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getDevPort() {
		return devPort;
	}

	public void setDevPort(int devPort) {
		this.devPort = devPort;
	}

	public int getAppPort() {
		return appPort;
	}

	public void setAppPort(int appPort) {
		this.appPort = appPort;
	}

}
