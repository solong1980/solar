package com.solar.cli.mina.bootstrap;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cli.IPUtil;
import com.solar.cli.ServerContext;
import com.solar.cli.mina.AppExtSessionManager;
import com.solar.cli.redis.SolarJedis;
import com.solar.controller.MccMsgDispatcher;
import com.solar.controller.MsgDispatcher;
import com.solar.db.InitDBServers;
import com.solar.server.commons.context.ExecutorServiceManager;
import com.solar.server.mina.net.MinaAppMsgHandler;
import com.solar.server.mina.net.MinaMccMsgHandler;
import com.solar.server.mina.net.NetManager;

public class CliSolarServer {
	private static final Logger logger = LoggerFactory.getLogger(CliSolarServer.class);

	private static CliSolarServer instance = new CliSolarServer();

	public static MsgDispatcher msgDispatcher = new MsgDispatcher();;
	public static MccMsgDispatcher mcMsgDispatcher = new MccMsgDispatcher();

	private static NetManager netManager;

	protected static void init() throws IOException {
		logger.info("开始启动服务器 ...");
		ExecutorServiceManager.getInstance().initExecutorService();
		logger.info("初始化服务器线程池完成");

		InitDBServers.getInstance().initServersFun();
		logger.info("数据库连接初始化完成");
	}

	protected static void app(int port) throws Exception {
		netManager.startListner(new MinaAppMsgHandler(), port);// 前段监听端口
		logger.info("APP服务器监听端口:{}完成", port);
	}

	protected static void dev(int port) throws Exception {
		netManager.startMCListner(new MinaMccMsgHandler(), port);// 单片机
		logger.info("DEV服务器监听端口:{}完成", port);
	}

	private CliSolarServer() {
		netManager = new NetManager();
	}

	public static CliSolarServer getInstance() {
		return instance;
	}

	public static void startUp(String[] args) {
		try {
			CommandLineParser parser = new DefaultParser();
			Options options = new Options();
			options.addOption("dev", "device-port", true, "device server port .");
			options.addOption("app", "app-port", true, "application server port .");
			try {
				CommandLine line = parser.parse(options, args);

				int length = line.getOptions().length;
				if (length == 0) {
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("start server", options);
					return;
				}

				init();
				
				InetAddress address = IPUtil.getLocalHostLANAddress();
				String hostAddress = address.getHostAddress();
				
				ServerContext serverContext = new ServerContext();
				serverContext.setIp(hostAddress);
				
				if (line.hasOption("app")) {
					int port = Integer.parseInt(line.getOptionValue("app"));
					app(port);
					serverContext.setAppPort(port);
				} else if (line.hasOption("app-port")) {
					int port = Integer.parseInt(line.getOptionValue("app-port"));
					app(port);
					serverContext.setAppPort(port);
				}

				if (line.hasOption("dev")) {
					int port = Integer.parseInt(line.getOptionValue("dev"));
					dev(port);
					serverContext.setDevPort(port);
					SolarJedis.getInstance().lpush("DEV_SERVERS",address.getHostAddress()+":"+port);
					
				} else if (line.hasOption("device-port")) {
					int port = Integer.parseInt(line.getOptionValue("device-port"));
					dev(port);
					serverContext.setDevPort(port);
					SolarJedis.getInstance().lpush("DEV_SERVERS",address.getHostAddress()+":"+port);
				
				}

				AppExtSessionManager.getInstance().setContext(serverContext);

			} catch (ParseException exp) {
				System.out.println("Unexpected exception:" + exp.getMessage());
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("start server", options);
			}
		} catch (Exception e) {
			logger.error("服务器启动失败");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println(Arrays.toString(args));
		startUp(args);
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
