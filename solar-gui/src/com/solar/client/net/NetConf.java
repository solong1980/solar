package com.solar.client.net;

public class NetConf {
	public static final String HOST_IP_ADDR = "localhost";// 服务器地址
	public static final int HOST_PORT = 10123;// 服务器端口号

	private String dataServerIP;
	private int dataServerPort;

	public NetConf(String dataServerIP, int dataServerPort) {
		super();
		this.dataServerIP = dataServerIP;
		this.dataServerPort = dataServerPort;
	}

	public NetConf() {
		super();
	}

	public String getDataServerIP() {
		return dataServerIP;
	}

	public void setDataServerIP(String dataServerIP) {
		this.dataServerIP = dataServerIP;
	}

	public int getDataServerPort() {
		return dataServerPort;
	}

	public void setDataServerPort(int dataServerPort) {
		this.dataServerPort = dataServerPort;
	}

	public static NetConf buildHostConf() {
		NetConf netConf = new NetConf();
		netConf.setDataServerIP(HOST_IP_ADDR);
		netConf.setDataServerPort(HOST_PORT);
		return netConf;
	}
}
