package com.solar.server.mina.bootstrap;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.controller.MsgDispatcher;
import com.solar.db.InitDBServers;
import com.solar.server.commons.context.ExecutorServiceManager;
import com.solar.server.mina.net.MinaHostMsgHandler;
import com.solar.server.mina.net.MinaMsgHandler;
import com.solar.server.mina.net.NetManager;

public class SolarServer {

	private static final Logger logger = LoggerFactory.getLogger(SolarServer.class);

	private static int port = 10122;
	private static int hostPort = 10123;

	private static SolarServer instance = new SolarServer();

	public static MsgDispatcher msgDispatcher = new MsgDispatcher();;

	private static NetManager netManager;

	private SolarServer() {
		netManager = new NetManager();
	}

	public static SolarServer getInstance() {
		return instance;
	}

	public static void main(String[] args) throws IOException {
		startUp();
	}

	public static void startUp() {
		try {
			logger.info("开始启动服务器 ...");
			ExecutorServiceManager.getInstance().initExecutorService();
			logger.info("初始化服务器线程池完成");

			InitDBServers.getInstance().initServersFun();
			logger.info("数据库连接初始化完成");

			netManager.startListner(new MinaMsgHandler(), port);// 前段监听端口
			logger.info("服务器监听端口:{}完成", port);
			netManager.startHostListner(new MinaHostMsgHandler(), hostPort);// 后台数据链接的时候再开一个listner
			logger.info("服务器监听端口:{}完成", hostPort);

			logger.info("solar server started...");
		} catch (Exception e) {
			logger.error("服务器启动失败");
			e.printStackTrace();
		}
	}

	public static void stop() {
		try {
			// 关闭服务器前，向所有线程玩家发送关闭消息
			// Collection<GameSession> list =
			// GameSessionManager.getInstance().sessionMap.values();
			// for (GameSession gameSession : list) {
			// gameSession.sendMsg(new CloseGameResponse(1, "closeGame"));
			// }

			logger.info("准备关闭服务器...");
			netManager.stop();
			logger.info("服务器停止网络监听");
			ExecutorServiceManager.getInstance().stop();
			logger.info("服务器线程池关闭完成");
			logger.info("服务器关闭完成");
		} catch (Exception e) {
			logger.info("服务器关闭异常");
			e.printStackTrace();
		}
	}
}
