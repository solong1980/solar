package com.solar.test.press;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.client.DeviceClient;
import com.solar.client.net.NetConf;

public class PressClient {
	private static final Logger logger = LoggerFactory.getLogger(PressClient.class);

	public static List<String> loadDevNo() throws IOException {
		File f = new File("dev_nos");
		return Files.readAllLines(Paths.get(f.toURI()));
	}

	public static void main(String[] args) {
		NetConf netConf = NetConf.buildHostConf();
		//netConf.setDataServerIP("39.107.24.81");
		 netConf.setDataServerIP("127.0.0.1");
		netConf.setDataServerPort(10124);

		List<String> loadDevNo = Collections.emptyList();
		try {
			loadDevNo = loadDevNo();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int pressNum = 30;
		for (int i = 0; i < pressNum; i++) {
			String dev = loadDevNo.get(i);
			String msg = "01," + dev
					+ ",13,308,45,301,100,148273,55494,28,0,0,0,29,0,0,0,9002,20180425075606,30.026143,114.128077\n";
			byte[] data = msg.getBytes();
			new Thread(new Runnable() {
				@Override
				public void run() {
					DeviceClient deviceClients = new DeviceClient(netConf);
					while (true) {
						try {
							deviceClients.send(data);
							synchronized (deviceClients) {
								deviceClients.wait(10000);
							}
						} catch (Exception e) {
							logger.error("error:", e);
						}
					}
				}
			}).start();
		}

		Scanner scanner = new Scanner(System.in);
		String nextLine = scanner.nextLine();
		System.out.println(nextLine);
	}

}