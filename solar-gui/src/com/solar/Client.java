package com.solar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.solar.common.context.ActionType;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.WarnningCode;
import com.solar.common.util.JsonUtilTool;
import com.solar.entity.SoAppUpgradeInfo;
import com.solar.entity.SoRunningData;
import com.solar.entity.SoWorkingMode;
import com.solar.gui.BtnListener;

public class Client extends Observable {
	private static final int SOCKET_TIMEOUT = 100000;
	public static final String IP_ADDR = "localhost";// 服务器地址
	public static final int PORT = 10122;// 服务器端口号

	public Client(BtnListener btnListener) {
		super();
		if (btnListener != null)
			addObserver(btnListener);
	}

	public void send(ActionType actionType, int command, String jsonData) {
		Socket socket = null;
		DataInputStream input = null;
		DataOutputStream out = null;
		try {
			socket = new Socket(IP_ADDR, PORT);
			socket.setSoTimeout(SOCKET_TIMEOUT);

			int tr = 5;
			while (!socket.isConnected() && (tr--) >= 0) {
				Thread.sleep(100);
			}
			// 向服务器端发送数据
			out = new DataOutputStream(socket.getOutputStream());
			// 读取服务器端数据
			input = new DataInputStream(socket.getInputStream());

			ClientSendRequest loginSend = new ClientSendRequest(command);
			loginSend.output.writeUTF(jsonData);
			out.write(loginSend.entireMsg().array());//
			out.flush();

			serverCallBack(input);

			out.close();
			input.close();
			notifyObservers(actionType);
		} catch (Exception e) {
			System.out.println("客户端异常:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					System.out.println("客户端 finally 异常:" + e.getMessage());
				}
			}
		}

	}

	public static void main(String[] args) {
		//dataUpload();
		concurency();
	}

	private static void concurency() {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 3000; i++) {
			newFixedThreadPool.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					long currentTimeMillis = System.currentTimeMillis();
					for (int j = 0; j < 10; j++) {
						dataUpload();
						//Thread.currentThread().sleep(100);
					}
					long a = System.currentTimeMillis() - currentTimeMillis;
					System.out.println(a);
					return false;
				}
			});
		}
		newFixedThreadPool.shutdown();
	}

	private static void upgrade() {
		SoAppUpgradeInfo appUpgradeInfo = new SoAppUpgradeInfo();
		appUpgradeInfo.setInfo("the package");
		appUpgradeInfo.setVersion("2323");
		appUpgradeInfo.setVersionNo(1);
		appUpgradeInfo.setPath("");
		appUpgradeInfo.setId(1L);
		appUpgradeInfo.setCreateTime(new Date());
		String json = JsonUtilTool.toJson(appUpgradeInfo);
		new Client(null).send(ActionType.WORKING_MODE_UPDATE, ConnectAPI.APP_UPGRADE_COMMAND, json);
	}

	private static void getWorkingMode() {
		new Client(null).send(ActionType.GET_WORKING_MODE, ConnectAPI.GET_WORKING_MODE_COMMAND, "");
	}

	private static void WorkingModeUpdate() {
		SoWorkingMode workingMode = new SoWorkingMode();
		workingMode.setContinuous(false);
		workingMode.setCreateBy(10000L);
		workingMode.setEmergency(false);
		workingMode.setFixedTimingLength(true);
		workingMode.setTimeInterval(1000);
		workingMode.setTiming(false);
		workingMode.setPointOfTime(new Date());
		String json = JsonUtilTool.toJson(workingMode);
		new Client(null).send(ActionType.WORKING_MODE_UPDATE, ConnectAPI.WORKING_MODE_UPDATE_COMMAND, json);
	}

	private static void dataUpload() {
		SoRunningData soRunningData = new SoRunningData();
		soRunningData.setRealTime(System.currentTimeMillis());
		soRunningData.setWarnningCode(WarnningCode.Error_000002);
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
		new Client(null).send(ActionType.DATA_UPLOAD, ConnectAPI.DATA_UPLOAD_COMMAND, json);
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
			Object arg = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void serverCallBack(DataInputStream input) {
		try {
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
				// System.out.println("服务器端返回过来的是: " + ret);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}