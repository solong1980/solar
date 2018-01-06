package com.solar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.solar.client.DeviceClient;
import com.solar.client.net.NetConf;

@SuppressWarnings("serial")
public class EndpotClient extends JFrame {

	DeviceClient deviceClient;

	public EndpotClient() {
		super();
		setPreferredSize(new Dimension(600, 80));
		setTitle("EndpotClient");
		pack();
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());
		JButton connectButton = new JButton("连接服务器");

		JButton sendDataButton = new JButton("发送数据");
		JButton receiveButton = new JButton("接受数据");
		JButton closeButton = new JButton("关闭");

		jPanel.add(connectButton);
		jPanel.add(sendDataButton);
		jPanel.add(receiveButton);
		jPanel.add(closeButton);

		getContentPane().add(jPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NetConf netConf = NetConf.buildHostConf();
				netConf.setDataServerPort(10124);
				deviceClient = new DeviceClient(netConf);
				connectButton.setEnabled(false);
			}
		});

		sendDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String data = "01,17DD5E6E,FFFFFFFF,233,6,225,15,0,0,0,0,0,17,0,0,0,0,20171224080052,83.872,30.473689\n";
				try {
					deviceClient.send(data.getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		receiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						while (deviceClient.isAvailable()) {
							try {
								deviceClient.recive();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deviceClient.close();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		int maxValue = Integer.MAX_VALUE;
		System.out.println(Integer.toHexString(maxValue));

		long parseLong = Long.parseLong("40bbd96d",16);
		
		String ver = "V01".replaceAll("V|v", "");
		System.out.println(ver);
		System.out.println(parseLong);
		long parseInt = Long.valueOf("FFFFFFFF",16);
		System.out.println(parseInt);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new EndpotClient();
			}
		});
	}

}