package com.solar.cli.netty.bootstrap;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.solar.cli.IPUtil;
import com.solar.cli.ServerContext;
import com.solar.cli.mina.AppExtSessionManager;
import com.solar.cli.netty.controller.MsgDispatcher;
import com.solar.cli.netty.net.NetManager;
import com.solar.db.InitDBServers;

public class CliNettyServer {

	public static MsgDispatcher msgDispatcher = new MsgDispatcher();;
	private static NetManager netManager = new NetManager();

	public CliNettyServer() {
	}

	private void startDB() {
		try {
			InitDBServers.getInstance().initServersFun();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void startUp(String[] args) {
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

				startDB();

				InetAddress address = IPUtil.getLocalHostLANAddress();
				String hostAddress = address.getHostAddress();

				ServerContext serverContext = new ServerContext();
				serverContext.setIp(hostAddress);
				int appPort = -1;
				if (line.hasOption("app")) {
					appPort = Integer.parseInt(line.getOptionValue("app"));
				} else if (line.hasOption("app-port")) {
					appPort = Integer.parseInt(line.getOptionValue("app-port"));
				}

				if (appPort > 1024 && appPort < 65535) {
					serverContext.setAppPort(appPort);
					CliNettyServer.netManager.startAPPListner(appPort);
				} else {
					System.exit(-1);
				}
				
				int devPort = -1;
				if (line.hasOption("dev")) {
					devPort = Integer.parseInt(line.getOptionValue("dev"));
				} else if (line.hasOption("dev-port")) {
					devPort = Integer.parseInt(line.getOptionValue("dev-port"));
				}

				if (devPort > 1024 && devPort < 65535) {
					serverContext.setDevPort(devPort);
					CliNettyServer.netManager.startMCListner(devPort);
				} else {
					System.exit(-1);
				}

				AppExtSessionManager.getInstance().setContext(serverContext);
			} catch (ParseException exp) {
				System.out.println("Unexpected exception:" + exp.getMessage());
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("start server", options);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) throws Exception {
		new CliNettyServer().startUp(args);
	}
}