package com.solar.client;

import java.io.IOException;
import java.util.Observable;

import com.solar.client.net.NetConf;
import com.solar.entity.SoAccount;
import com.solar.entity.SoDataServerInfo;

public class ObservableMedia extends Observable {
	HostClient hostClient = null;

	public static void main(String[] args) {
		long t = System.currentTimeMillis();
		// // host connect
		// HostClient hostClient = new HostClient(NetConf.buildHostConf());
		//
		// if (hostClient.getDataServerNetConf() == null) {
		// SoDataServerInfo dataServerInfo = new SoDataServerInfo();
		// dataServerInfo.setDevId("000000");
		// hostClient.send(ConnectAPI.DATA_SERVER_QUERY_COMMAND,
		// JsonUtilTool.toJson(dataServerInfo));
		// hostClient.recive();
		// }
		// // data connect
		// DataClient dataClient = new
		// DataClient(hostClient.getDataServerNetConf());
		// // send & receive & close
		// dataClient.dataUpload();
		// hostClient.start();
		// hostClient.close();

		ObservableMedia media = new ObservableMedia();
		media.sendData();

		System.out.println(System.currentTimeMillis() - t);
	}

	public ObservableMedia() {
		super();
		// host connect
		hostClient = new HostClient(this, NetConf.buildHostConf());
		// Get data server info
		if (hostClient.getDataServerNetConf() == null) {

			SoDataServerInfo dataServerInfo = new SoDataServerInfo();
			dataServerInfo.setDevId("000000");

			hostClient.getDataServerInfo(dataServerInfo);
			try {
				hostClient.recive();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		hostClient.start();
	}

	public void close() {
		hostClient.stop();
	}

	public void sendData() {
		// data connect
		DataClient dataClient = new DataClient(hostClient.getDataServerNetConf());
		// send & receive & close
		dataClient.dataUpload();
	}

	public void login(SoAccount account) {
		hostClient.login(account);
		setChanged();
	}

	public void sendUpdateConfig() {
		hostClient.workingModeUpdate();
		setChanged();
	}

	public void getUpdate() {
		hostClient.send(12, "");
		setChanged();
	}

	public void getConfig() {
		hostClient.send(12, "");
		setChanged();
	}
}
