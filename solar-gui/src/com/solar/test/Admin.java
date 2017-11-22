package com.solar.test;

import com.solar.client.HostClient;
import com.solar.client.net.NetConf;
import com.solar.entity.SoAccount;

public class Admin {

	public static void main(String[] args) {
		// TODO 维护500个Customer长连接
		HostClient[] hostClients = new HostClient[10000];
		for (int i = 0; i < 10000; i++) {
			HostClient client = new HostClient(NetConf.buildHostConf());
			hostClients[i] = client;
		}
		SoAccount soAccount = new SoAccount();
		soAccount.setAccount("admin");
		soAccount.setPassword("123456");
		for (HostClient hostClient : hostClients) {
			hostClient.login(soAccount);
		}
	}

}
