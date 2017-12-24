package com.solar.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.solar.client.net.NetConf;

public class DeviceClient {
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

	public DeviceClient(NetConf netConf) {
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

	public String serverCallBack(DataInputStream input) throws IOException {
		int c = 0;
		byte[] buf = new byte[1024 * 1024 * 4];
		for (;;) {
			byte readByte = input.readByte();
			buf[c] = readByte;
			if (readByte == '\n') {
				break;
			}
			c++;
		}
		return new String(buf, 0, c);
	}

	public boolean countinueTry() {
		return false;
	}

	public void recive() throws IOException {
		String ret = serverCallBack(input);
		System.out.println(ret);
	}

}