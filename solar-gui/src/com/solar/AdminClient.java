package com.solar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.solar.client.ObservableMedia;
import com.solar.entity.SoAccount;

public class AdminClient extends JFrame {
	ObservableMedia media;

	public AdminClient() {
		super();
		setPreferredSize(new Dimension(300, 80));
		setTitle("AdminClient");
		pack();
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());
		JButton connectButton = new JButton("连接服务器");

		JButton loginButton = new JButton("登陆");
		JButton upConfigbutton = new JButton("更新配置");
		JButton closeButton = new JButton("关闭");

		jPanel.add(connectButton);
		jPanel.add(loginButton);
		jPanel.add(upConfigbutton);
		jPanel.add(closeButton);

		getContentPane().add(jPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (media == null) {
					try {
						media = new ObservableMedia();
					} catch (Exception ee) {
						ee.printStackTrace();
						JOptionPane.showMessageDialog(AdminClient.this, "连接失败");
					}
				}
			}
		});

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoAccount soAccount = new SoAccount();
				soAccount.setAccount("admin");
				soAccount.setPassword("123456");
				media.login(soAccount);
			}
		});
		upConfigbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				media.sendUpdateConfig();
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				media.close();
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AdminClient();
			}
		});
	}
}