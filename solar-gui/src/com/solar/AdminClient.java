package com.solar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.solar.client.DataClient;
import com.solar.client.ObservableMedia;
import com.solar.client.net.NetConf;
import com.solar.common.context.AppType;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoProvinces;

public class AdminClient extends JFrame {
	private static final long serialVersionUID = 1L;
	ObservableMedia media;

	public AdminClient() {
		super();
		setPreferredSize(new Dimension(800, 600));
		setTitle("AdminClient");
		pack();
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());
		JButton connectButton = new JButton("连接服务器");

		JButton loginButton = new JButton("登陆");
		JButton upConfigButton = new JButton("更新配置");

		JButton appVersionButton = new JButton("版本信息");

		JButton provinceBtn = new JButton("省");
		JButton cityBtn = new JButton("市");
		JButton areaBtn = new JButton("区");

		JButton closeButton = new JButton("关闭");

		jPanel.add(connectButton);
		jPanel.add(loginButton);
		jPanel.add(upConfigButton);
		jPanel.add(appVersionButton);

		jPanel.add(provinceBtn);
		jPanel.add(cityBtn);
		jPanel.add(areaBtn);

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
				soAccount.setPassword("1233456");
				media.login(soAccount);
			}
		});

		upConfigButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				media.sendUpdateConfig();
			}
		});

		appVersionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				media.queryAppVersion(AppType.APK.type());
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				media.close();
			}
		});

		provinceBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataClient dataClient = new DataClient(NetConf.buildHostConf());
				List<SoProvinces> provinces = dataClient.getProvinces();
				System.out.println(provinces);
			}
		});
		cityBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataClient dataClient = new DataClient(NetConf.buildHostConf());
				List<SoCities> cities = dataClient.getCitiesIn("420000");
				System.out.println(cities);
			}
		});
		areaBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataClient dataClient = new DataClient(NetConf.buildHostConf());
				List<SoAreas> areas = dataClient.getAreasIn("420100");
				System.out.println(areas);
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