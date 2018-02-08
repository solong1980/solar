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
import com.solar.cli.netty.controller.MsgDispatcher;
import com.solar.cli.netty.net.NetManager;
import com.solar.cli.netty.session.NettyExtSessionManager;
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

				int devPort = -1;
				if (line.hasOption("dev")) {
					devPort = Integer.parseInt(line.getOptionValue("dev"));
				} else if (line.hasOption("dev-port")) {
					devPort = Integer.parseInt(line.getOptionValue("dev-port"));
				}

				NettyExtSessionManager.getInstance().setContext(serverContext);

				if (appPort > 1024 && appPort < 65535) {
					serverContext.setAppPort(appPort);
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								CliNettyServer.netManager.startAPPListner(serverContext.getAppPort());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.setDaemon(false);
					thread.start();
				}
				if (devPort > 1024 && devPort < 65535) {
					serverContext.setDevPort(devPort);
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								CliNettyServer.netManager.startMCListner(serverContext.getDevPort());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.setDaemon(false);
					thread.start();
				}

				if ((devPort < 1024 || devPort > 65535) && (appPort < 1024 || devPort > appPort)) {
					System.exit(-1);
				}

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