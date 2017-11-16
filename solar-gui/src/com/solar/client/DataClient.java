package com.solar.client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

import com.solar.client.msg.ClientSendRequest;
import com.solar.client.net.MinaClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.ConnectAPI;
import com.solar.entity.SoAppUpgradeInfo;
import com.solar.entity.SoRunningData;

public class DataClient extends MinaClient {

	public DataClient(NetConf netConf) {
		super(netConf);
	}

	@Override
	public void recive() {
		try {
			serverCallBack(input);
		} catch (IOException e) {
			System.err.println("客户端异常:" + e.getMessage());
		}
	}

	public void send(int command, String jsonData) {
		try {
			ClientSendRequest loginSend = new ClientSendRequest(command);
			loginSend.output.writeUTF(jsonData);
			send(loginSend.entireMsg().array());
		} catch (Exception e) {
			System.err.println("客户端异常:" + e.getMessage());
			sleep();
			// 关闭重连
			if (e instanceof SocketException || e instanceof ConnectException) {
				initSocket();
			}
			send(command, jsonData);
		}
	}

	private void upgrade() {
		SoAppUpgradeInfo appUpgradeInfo = new SoAppUpgradeInfo();
		appUpgradeInfo.setInfo("the package");
		appUpgradeInfo.setVersion("2323");
		appUpgradeInfo.setVersionNo(1);
		appUpgradeInfo.setPath("");
		appUpgradeInfo.setId(1L);
		appUpgradeInfo.setCreateTime(new Date());
		String json = JsonUtilTool.toJson(appUpgradeInfo);
		send(ConnectAPI.APP_UPGRADE_COMMAND, json);
	}

	private void getWorkingMode() {
		send(ConnectAPI.GET_WORKING_MODE_COMMAND, "");
	}

	public void dataUpload() {
		SoRunningData soRunningData = new SoRunningData();
		soRunningData.setRealTime(System.currentTimeMillis());
		soRunningData.setWarnningCode(0x0000002);
		soRunningData.setIpAddr("192.168.41.65");
		soRunningData.setChargingVoltage(20.2f);
		soRunningData.setChargingCurrent(20.2f);
		soRunningData.setDcChargingVoltage(20.2f);
		soRunningData.setDcChargingCurrent(20.2f);
		soRunningData.setBatteryVoltage(20.2f);
		soRunningData.setBatteryChargingCurrent(20.2f);
		soRunningData.setRtNTCTemperature(36.2f);
		soRunningData.setLoadCurrent1(10.1f);
		soRunningData.setLoadCurrent2(12.1f);
		soRunningData.setLoadCurrent3(11.2f);
		soRunningData.setLoadCurrent4(12.4f);
		soRunningData.setGpsInfo("$GPRMC,204700,A,3403.868,N,11709.432,W,001.9,336.9,170698,013.6,E*6E");
		String json = JsonUtilTool.toJson(soRunningData);
		send(ConnectAPI.DATA_UPLOAD_COMMAND, json);
		recive();
		close();
	}

	public void serverCallBack2(DataInputStream input) {
		try {
			System.out.println("服务器端返回过来的是: ");
			input.readByte();
			int len = input.readInt();
			System.out.println(len);
			int code = input.readInt();
			System.out.println(code);
			int status = input.readInt();
			System.out.println(status);
			String ret = input.readUTF();
			System.out.println("服务器端返回过来的是: " + ret);
			// 如接收到 "OK" 则断开连接
			if ("OK".equals(ret)) {
				System.out.println("客户端将关闭连接");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void serverCallBack(DataInputStream input) throws IOException {
		// System.out.println("服务器端返回过来的是: ");
		byte flag = input.readByte();
		int len = input.readInt();
		// System.out.println(len);
		int code = input.readInt();
		// System.out.println(code);
		int status = input.readInt();
		// System.out.println(status);
		if (code == ConnectAPI.APP_UPGRADE_RESPONSE) {
			int length = input.readInt();// json字符串长度
			input.mark(length);
			String ret = input.readUTF();
			// System.out.println("服务器端返回过来的是: " + ret);
			length = input.readInt();
			input.mark(length);

			File file = new File("hka-oj-dev(New).zip");
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			FileChannel nioFileChannel = fileOutputStream.getChannel();
			// 创建一个buffer并把准备写的数据填充进去;
			ByteBuffer nioByteBuffer = ByteBuffer.allocate(length);
			byte[] apkData = new byte[1024 * 8];
			int read = input.read(apkData);
			while (true && read >= 0) {
				nioByteBuffer.put(apkData, 0, read);
				if (nioByteBuffer.position() == length)
					break;
				read = input.read(apkData);
			}
			nioByteBuffer.flip();
			nioFileChannel.write(nioByteBuffer);
			nioFileChannel.close();
			fileOutputStream.close();
		} else {
			String ret = input.readUTF();
			System.out.println("服务器端返回过来的是: " + ret);
			// 如接收到 "OK" 则断开连接
			if ("OK".equals(ret)) {
				System.out.println("客户端将关闭连接");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean countinueTry() {
		return false;
	}

}