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

import com.solar.client.http.HttpClientDownloadFile;
import com.solar.client.msg.ClientSendRequest;
import com.solar.client.net.MinaClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.ConnectAPI;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAppVersion;
import com.solar.entity.SoDataServerInfo;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProject;
import com.solar.entity.SoVCode;
import com.solar.entity.SoWorkingMode;

public class HostClient extends MinaClient {

	private Thread recThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (running) {
				System.out.println("host client wait for msg");
				long t = System.currentTimeMillis();
				try {
					recive();
				} catch (IOException e) {
					running = false;
				}
				System.out.println("host client get msg " + (System.currentTimeMillis() - t));
			}
		}
	});

	// 数据服务器网路配置
	private NetConf dataServerNetConf = null;
	// 工作模式配置
	private SoWorkingMode workingMode = null;
	// 响应中介
	private ObservableMedia observableMedia = null;

	// 消息接受循环标志
	private volatile boolean running = false;

	public HostClient(NetConf netConf) {
		super(netConf);
	}

	public HostClient(ObservableMedia om, NetConf netConf) {
		super(netConf);
		if (!isAvailable()) {
			throw new RuntimeException("连接后台失败");
		}
		this.observableMedia = om;
	}

	/**
	 * 启动接受线程
	 */
	public void start() {
		running = true;
		this.recThread.start();
	}

	public void stop() {
		running = false;
		this.recThread.interrupt();
		close();
	}

	public SoWorkingMode getWorkingMode() {
		return workingMode;
	}

	public void setWorkingMode(SoWorkingMode workingMode) {
		this.workingMode = workingMode;
	}

	public NetConf getDataServerNetConf() {
		return dataServerNetConf;
	}

	public void setDataServerNetConf(NetConf dataServerNetConf) {
		this.dataServerNetConf = dataServerNetConf;
	}

	public void send(int command, String jsonData) {
		try {
			ClientSendRequest clientSendRequest = new ClientSendRequest(command);
			clientSendRequest.output.writeUTF(jsonData);
			send(clientSendRequest.entireMsg().array());
		} catch (Exception e) {
			System.err.println("客户端异常:" + e.getMessage());
			sleep();
			if (!running)
				return;
			// 关闭重连
			if (e instanceof SocketException || e instanceof ConnectException) {
				initSocket();
			}
			send(command, jsonData);
		}
	}

	public void serverCallBack(DataInputStream input) throws IOException {
		try {
			input.readByte();
			int len = input.readInt();
			int code = input.readInt();
			int status = input.readInt();
			String ret = input.readUTF();
			System.out.println("服务器端返回过来的头: len:" + len + ",code:" + code + ",status:" + status);
			System.out.println("服务器端返回过来的内容: " + ret);

			switch (code) {
			case ConnectAPI.DATA_SERVER_QUERY_RESPONSE:
				SoDataServerInfo dataServerInfo = JsonUtilTool.fromJson(ret, SoDataServerInfo.class);
				// get data server info
				String serverIP = dataServerInfo.getServerIP();
				int port = dataServerInfo.getPort();
				dataServerNetConf = new NetConf(serverIP, port);
				break;
			case ConnectAPI.GET_WORKING_MODE_RESPONSE:
				workingMode = JsonUtilTool.fromJson(ret, SoWorkingMode.class);
				break;
			case ConnectAPI.APP_VERSION_QUERY_RESPONSE:
				SoAppVersion appVersion = JsonUtilTool.fromJson(ret, SoAppVersion.class);
				if (appVersion.getRetCode() == 0) {
					Long id = appVersion.getId();
					HttpClientDownloadFile clientDownloadFile = new HttpClientDownloadFile();
					clientDownloadFile.upfile(id);
				}
				break;
			default:
				SoRet soRet = new SoRet();
				soRet.setCode(code);
				soRet.setLen(len);
				soRet.setStatus(status);
				soRet.setRet(ret);
				observableMedia.notifyObservers(soRet);
				break;
			}

			// 如接收到 "OK" 则断开连接
			if ("OK".equals(ret)) {
				System.out.println("客户端将关闭连接");
				sleep();
			}
		} catch (IOException e) {
			if (e instanceof SocketException) {
				// 套接字连接异常
				throw e;
			}
			e.printStackTrace();
		}
	}

	public void readFile(DataInputStream input) {
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
				System.out.println("服务器端返回过来的是: " + ret);
				// 如接收到 "OK" 则断开连接
				if ("OK".equals(ret)) {
					System.out.println("客户端将关闭连接");
					sleep();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void recive() throws IOException {
		serverCallBack(input);
	}

	public void workingModeUpdate() {
		SoWorkingMode workingMode = new SoWorkingMode();

		workingMode.setContinuous(false);
		workingMode.setCreateBy(10000L);
		workingMode.setEmergency(false);
		workingMode.setFixedTimingLength(true);
		workingMode.setTimeInterval(1000);
		workingMode.setTiming(false);
		workingMode.setPointOfTime(new Date());
		String json = JsonUtilTool.toJson(workingMode);
		send(ConnectAPI.WORKING_MODE_UPDATE_COMMAND, json);
	}

	public void login(SoAccount account) {
		String json = JsonUtilTool.toJson(account);
		System.out.println(json);
		send(ConnectAPI.LOGIN_COMMAND, json);
	}

	public void deviceAccess(SoDevices devices) {
		String json = JsonUtilTool.toJson(devices);
		send(ConnectAPI.DEVICE_ACCESS_COMMAND, json);
	}

	public void getDataServerInfo(SoDataServerInfo dataServerInfo) {
		String json = JsonUtilTool.toJson(dataServerInfo);
		send(ConnectAPI.DATA_SERVER_QUERY_COMMAND, json);
	}

	public void runningMode() {
		send(ConnectAPI.GET_WORKING_MODE_COMMAND, "");
	}

	public void queryAppVersion(SoAppVersion appVersion) {
		String json = JsonUtilTool.toJson(appVersion);
		send(ConnectAPI.APK_VERSION_QUERY_COMMAND, json);
	}

	public void saveProject(SoProject soProject) {
		String json = JsonUtilTool.toJson(soProject);
		send(ConnectAPI.PROJECT_ADD_COMMAND, json);
	}

	public void regiest(SoAccount account) {
		String json = JsonUtilTool.toJson(account);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_ADD_COMMAND, json);
	}

	public void getVCode(SoVCode soVCode) {
		String json = JsonUtilTool.toJson(soVCode);
		System.out.println(json);
		send(ConnectAPI.VCODE_GET_COMMAND, json);
	}

}