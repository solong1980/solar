package com.solar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.solar.client.ObservableMedia;
import com.solar.entity.SoDevices;

@SuppressWarnings("serial")
public class EndpotClient extends JFrame {

	ObservableMedia media;

	public EndpotClient() {
		super();
		setPreferredSize(new Dimension(600, 80));
		setTitle("EndpotClient");
		pack();
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());
		JButton connectButton = new JButton("连接服务器");

		JButton accessButton = new JButton("接入");

		JButton runningModeButton = new JButton("获取运行模式");

		JButton sendDataButton = new JButton("发送数据");
		JButton closeButton = new JButton("关闭");

		jPanel.add(connectButton);
		jPanel.add(accessButton);
		jPanel.add(runningModeButton);
		jPanel.add(sendDataButton);
		jPanel.add(closeButton);

		getContentPane().add(jPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (media == null) {
					try {
						media = ObservableMedia.getInstance();
					} catch (Exception ee) {
						ee.printStackTrace();
						JOptionPane.showMessageDialog(EndpotClient.this, "连接失败");
					}
				}
			}
		});

		accessButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoDevices devices = new SoDevices();
				devices.setDevNo("00000000");
				media.deviceAccess(devices);
			}
		});

		runningModeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				media.getRunningMode();
			}
		});

		sendDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						while (true) {
							System.out.println("EndpotClient");
							media.sendData();
							try {
								Thread.sleep(60000);
							} catch (InterruptedException e) {
							}
						}
					}
				});
				sendDataButton.setEnabled(false);
				t.start();
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (media != null)
					media.close();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new EndpotClient();
			}
		});
	}

}