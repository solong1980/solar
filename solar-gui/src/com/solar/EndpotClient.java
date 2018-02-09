package com.solar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.solar.client.DeviceClient;
import com.solar.client.net.NetConf;

@SuppressWarnings("serial")
public class EndpotClient extends JFrame {

	public static int tnum = 1000;

	DeviceClient[] deviceClients = new DeviceClient[tnum];
	String data = "01,17DD5E6E,12,321,109,210,50,159003,29535,36,0,0,0,11,0,0,0,9000,20180202080734,30.026136,114.128179\n";

	boolean sendFlag = false;

	public void send() {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(tnum);
		while (true) {
			if (sendFlag) {
				for (DeviceClient deviceClient : deviceClients) {
					newFixedThreadPool.submit(new Runnable() {
						@Override
						public void run() {
							try {
								deviceClient.send(data.getBytes());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
				}
				synchronized (this) {
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
					}
				}
			}else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public EndpotClient() {
		super();

		NetConf netConf = NetConf.buildHostConf();
		netConf.setDataServerIP("127.0.0.1");
		netConf.setDataServerPort(10122);

		setPreferredSize(new Dimension(600, 80));
		setTitle("EndpotClient");
		pack();
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());
		JButton connectButton = new JButton("连接服务器");

		JButton startButton = new JButton("发送");
		JButton stopButton = new JButton("停止");
		startButton.setEnabled(false);
		stopButton.setEnabled(false);

		JButton receiveButton = new JButton("接受数据");
		JButton closeButton = new JButton("断开");

		jPanel.add(connectButton);

		jPanel.add(startButton);
		jPanel.add(stopButton);

		jPanel.add(receiveButton);
		jPanel.add(closeButton);

		getContentPane().add(jPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				for (int i = 0; i < tnum; i++) {
					DeviceClient deviceClient = new DeviceClient(netConf);
					deviceClients[i] = deviceClient;
				}

				connectButton.setEnabled(false);
				startButton.setEnabled(true);
				stopButton.setEnabled(true);
				new Thread(new Runnable() {
					@Override
					public void run() {
						send();
					}
				}).start();
			}
		});

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EndpotClient.this.sendFlag = true;
			}
		});
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EndpotClient.this.sendFlag = false;
			}
		});

		receiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						while (true) {
							for (DeviceClient deviceClient : deviceClients) {
								if (deviceClient.isAvailable()) {
									try {
										deviceClient.recive();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							}
						}
					}
				}).start();
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (DeviceClient deviceClient : deviceClients) {
					deviceClient.close();
				}
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		// int maxValue = Integer.MAX_VALUE;
		// System.out.println(Integer.toHexString(maxValue));
		//
		// long parseLong = Long.parseLong("40bbd96d", 16);
		//
		// String ver = "V01".replaceAll("V|v", "");
		// System.out.println(ver);
		// System.out.println(parseLong);
		// long parseInt = Long.valueOf("FFFFFFFF", 16);
		// System.out.println(parseInt);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new EndpotClient();
			}
		});
	}

}