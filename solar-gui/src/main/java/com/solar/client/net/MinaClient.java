package com.solar.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public abstract class MinaClient {
	private static final int SOCKET_TIMEOUT = 100000;
	// public static final String SERVER_IP_ADDR = "123.56.76.77";// 服务器地址
	// public static final int PORT = 10122;// 服务器端口号
	private NetConf netConf;
	private Socket socket = null;
	protected DataInputStream input = null;
	protected DataOutputStream out = null;

	private volatile boolean available = false;
	protected int tryCount = 1;

	public void sleep() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
		}
	}

	private void bSocket() throws UnknownHostException, IOException {
		close();
		socket = new Socket(this.netConf.getDataServerIP(), this.netConf.getDataServerPort());
		// socket.setSoTimeout(SOCKET_TIMEOUT);
		int tr = 5;
		while (!socket.isConnected() && (tr--) >= 0) {
			sleep();
		}
		// 向服务器端发送数据
		out = new DataOutputStream(socket.getOutputStream());
		// 读取服务器端数据
		input = new DataInputStream(socket.getInputStream());
	}

	public void initSocket() {
		try {
			bSocket();
			available = true;
		} catch (IOException e) {
			available = false;
			System.err.println("客户端异常:" + e.getMessage());
			sleep();
			// 关闭重连
			if (countinueTry() && (e instanceof SocketException || e instanceof ConnectException)) {
				initSocket();
			}
		}
	}

	public MinaClient(NetConf netConf) {
		this.netConf = netConf;
		initSocket();
		// 长连接，退出时才关闭socket等
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				close();
			}
		}));
	}

	public void close() {
		available = false;
		try {
			if (out != null) {
				out.close();
			}
			if (input != null) {
				input.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			System.out.println("客户端 finally 异常:" + e.getMessage());
		} finally {
			input = null;
			out = null;
			socket = null;
		}
	}

	public void send(byte[] data) throws IOException {
		out.write(data);//
		out.flush();
	}

	public boolean isAvailable() {
		boolean closed = socket.isClosed();
		boolean connected = socket.isConnected();
		return this.available && !closed && connected;
	}

	public abstract void recive() throws IOException;

	public boolean countinueTry() {
		tryCount = tryCount - 1;
		return (tryCount > 0) ? true : false;
	}

}