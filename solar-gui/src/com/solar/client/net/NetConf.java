package com.solar.client.net;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class NetConf {
	// 123.56.76.77
	// localhost
	public static final String HOST_IP_ADDR = "localhost";// 服务器地址
	public static final int HOST_PORT = 10122;// 服务器端口号

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
		Properties loadLocation = loadConf();
		String ip = loadLocation.getProperty("host_ip");
		String port = loadLocation.getProperty("app_port");
		if (ip == null || port == null) {
			System.exit(1);
		}
		return new NetConf(ip, Integer.parseInt(port));
	}

	public static Properties loadConf() {
		try {
			URL resource = Thread.currentThread().getContextClassLoader().getResource("resources/config.properties");
			Properties conf = new Properties();
			conf.load(resource.openStream());
			return conf;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static NetConf build() {
		NetConf netConf = new NetConf();
		netConf.setDataServerIP(HOST_IP_ADDR);
		netConf.setDataServerPort(HOST_PORT);
		return netConf;
	}
}
