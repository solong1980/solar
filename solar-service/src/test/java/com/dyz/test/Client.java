package com.dyz.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.solar.common.context.ConnectAPI;

public class Client {
	public static final String IP_ADDR = "localhost";// 服务器地址
	public static final int PORT = 10122;// 服务器端口号

	public static void main(String[] args) {

		while (true) {
			Socket socket = null;
			DataInputStream input = null;
			DataOutputStream out = null;
			try {
				// 创建一个流套接字并将其连接到指定主机上的指定端口号
				socket = new Socket(IP_ADDR, PORT);
				
				Thread.sleep(100);
				
				// 读取服务器端数据
				input = new DataInputStream(socket.getInputStream());
				// 向服务器端发送数据
				out = new DataOutputStream(socket.getOutputStream());
				System.out.println("请输入: \t");

				ClientSendRequest loginSend = new ClientSendRequest(ConnectAPI.CTRL_COMMAND);
				loginSend.output.writeUTF("鳄名牌");
				out.write(loginSend.entireMsg().array());//
				out.flush();
				serverCallBack(input);

				out.close();
				input.close();
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
	}

	public static void serverCallBack(DataInputStream input) {
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
}