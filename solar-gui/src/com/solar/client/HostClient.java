package com.solar.client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import com.solar.client.http.HttpClientDownloadFile;
import com.solar.client.msg.ClientSendRequest;
import com.solar.client.net.MinaClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.ConnectAPI;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoAppVersion;
import com.solar.entity.SoDataServerInfo;
import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.entity.SoRunningData;
import com.solar.entity.SoVCode;

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
					if (running)
						observableMedia.connectLost();
					running = false;
				}
				System.out.println("host client get msg " + (System.currentTimeMillis() - t));
			}
		}
	});

	// 数据服务器网路配置
	private NetConf dataServerNetConf = null;
	// 工作模式配置
	private SoProjectWorkingMode workingMode = null;
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

	public SoProjectWorkingMode getWorkingMode() {
		return workingMode;
	}

	public void setWorkingMode(SoProjectWorkingMode workingMode) {
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

	public void login(SoAccount account) {
		String json = JsonUtilTool.toJson(account);
		System.out.println(json);
		send(ConnectAPI.LOGIN_COMMAND, json);
	}

	public void deviceAccess(SoDevices devices) {
		String json = JsonUtilTool.toJson(devices);
		// send(ConnectAPI.DEVICE_ACCESS_COMMAND, json);
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
		if (soProject.getId() != null)
			send(ConnectAPI.PROJECT_UPDATE_COMMAND, json);
		else
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

	public void findBack(SoAccountFind accountFind) {
		String json = JsonUtilTool.toJson(accountFind);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_FINDBACK_COMMAND, json);
	}

	public void accountFindQuery(SoAccountFind soAccountFind) {
		String json = JsonUtilTool.toJson(soAccountFind);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_FINDBACK_QUERY_COMMAND, json);
	}

	public void accountFindAudit(SoAccountFind soAccountFind) {
		String json = JsonUtilTool.toJson(soAccountFind);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_FINDBACK_AUDIT_COMMAND, json);
	}

	public void accountQuery(SoPage<SoAccount, List<SoAccount>> page) {
		String json = JsonUtilTool.toJson(page);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_QUERY_COMMAND, json);
	}

	void regiestAudit(SoAccount account) {
		String json = JsonUtilTool.toJson(account);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_AUDIT_COMMAND, json);
	}

	public void checkAccountProject(SoAccount account) {
		String json = JsonUtilTool.toJson(account);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_PROJECT_CHECK_COMMAND, json);
	}

	public void queryProjects(SoPage<SoProject, List<SoProject>> soPage) {
		String json = JsonUtilTool.toJson(soPage);
		System.out.println(json);
		send(ConnectAPI.PROJECT_QUERY_COMMAND, json);
	}

	public void selectProject(SoProject project) {
		String json = JsonUtilTool.toJson(project);
		System.out.println(json);
		send(ConnectAPI.PROJECT_SELECT_COMMAND, json);
	}

	public void deleteProject(SoProject project) {
		String json = JsonUtilTool.toJson(project);
		System.out.println(json);
		send(ConnectAPI.PROJECT_DELETE_COMMAND, json);

	}

	public void accountSelect(SoAccount account) {
		String json = JsonUtilTool.toJson(account);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_SELECT_COMMAND, json);
	}

	public void accountUpdate(SoAccount account) {
		String json = JsonUtilTool.toJson(account);
		System.out.println(json);
		send(ConnectAPI.ACCOUNT_UPDATE_COMMAND, json);
	}

	public void getRunningData(SoRunningData runningData) {
		String json = JsonUtilTool.toJson(runningData);
		System.out.println(json);
		send(ConnectAPI.DEVICES_RUNNINGDATA_COMMAND, json);
	}

	public void getProjectWorkingMode(SoProjectWorkingMode projectWorkingMode) {
		String json = JsonUtilTool.toJson(projectWorkingMode);
		System.out.println(json);
		send(ConnectAPI.GET_WORKING_MODE_COMMAND, json);
	}

	public void updateProjectWorkingMode(SoProjectWorkingMode mode) {
		String json = JsonUtilTool.toJson(mode);
		System.out.println(json);
		send(ConnectAPI.WORKING_MODE_UPDATE_COMMAND, json);
	}

	public void updateDevice(SoDevices device) {
		String json = JsonUtilTool.toJson(device);
		System.out.println(json);
		send(ConnectAPI.DEVICES_UPDATE_COMMAND, json);
	}

	public void addDevice(SoDevices device) {
		String json = JsonUtilTool.toJson(device);
		System.out.println(json);
		send(ConnectAPI.DEVICES_ADD_COMMAND, json);
	}

	public void devUpFileBlockCacheClean() {
		send(ConnectAPI.DEVICES_UPGRADECTR_COMMAND, "");
	}
}