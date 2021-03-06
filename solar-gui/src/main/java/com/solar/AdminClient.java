package com.solar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.solar.client.DataClient;
import com.solar.client.ObservableMedia;
import com.solar.client.net.NetConf;
import com.solar.common.context.AppType;
import com.solar.common.context.Consts;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoAccountLocation;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoDevices;
import com.solar.entity.SoDevicesBatch;
import com.solar.entity.SoPage;
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

		JButton devicesBatchBtn = new JButton("设备批量增删");

		JButton closeButton = new JButton("关闭");

		JButton regiestBtn = new JButton("注册");
		JButton accountFindQueryBtn = new JButton("找回申请");

		jPanel.add(connectButton);
		jPanel.add(loginButton);
		jPanel.add(upConfigButton);
		jPanel.add(appVersionButton);
		
		jPanel.add(devicesBatchBtn);

		jPanel.add(provinceBtn);
		jPanel.add(cityBtn);
		jPanel.add(areaBtn);
		jPanel.add(regiestBtn);
		jPanel.add(closeButton);
		jPanel.add(accountFindQueryBtn);
		getContentPane().add(jPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		
		devicesBatchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoDevicesBatch devicesBatch = new SoDevicesBatch();
				List<SoDevices> batchAdds = new ArrayList<>();
				SoDevices dev= new SoDevices();
				dev.setDevNo("123456");
				dev.setProjectId(5L);
				dev.setSw0((short)0);
				dev.setSw1((short)0);
				dev.setSw2((short)0);
				dev.setSw3((short)0);
				dev.setSw4((short)0);
				dev.setSw5((short)0);
				dev.setSw6((short)0);
				dev.setSw7((short)0);
				dev.setLocationId("10000");
				batchAdds.add(dev);
				
				List<SoDevices> batchDels = new ArrayList<>();
				dev= new SoDevices();
				dev.setDevNo("1234256");
				dev.setProjectId(5L);
				dev.setSw0((short)0);
				dev.setSw1((short)0);
				dev.setSw2((short)0);
				dev.setSw3((short)0);
				dev.setSw4((short)0);
				dev.setSw5((short)0);
				dev.setSw6((short)0);
				dev.setSw7((short)0);
				dev.setLocationId("10000");
				batchDels.add(dev);
				
				devicesBatch.setBatchAdds(batchAdds);
				//devicesBatch.setBatchDels(batchDels);
				
				media.batchDevice(devicesBatch);
			}
		});
		
		accountFindQueryBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoAccountFind soAccountFind = new SoAccountFind();
				SoPage<SoAccountFind, List<SoAccountFind>> page = new SoPage<>(soAccountFind);
				media.accountFindQuery(page);
			}
		});
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (media == null) {
					try {
						media = ObservableMedia.getInstance();
						media.connect();
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

		upConfigButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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

		regiestBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoAccount account = new SoAccount();
				String name = "咯国内";
				String phone = "1687121212";
				account.setName(name);
				account.setPhone(phone);
				account.setEmail("aaaa@aa.com");
				int typeIndex = 1;
				account.setType(Consts.ACCOUNT_TYPE[typeIndex]);
				List<SoAccountLocation> accountLocations = new ArrayList<>();
				if (typeIndex == 0) {
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setLocationId("110105");
					accountLocations.add(accountLocation);
					accountLocation = new SoAccountLocation();
					accountLocation.setLocationId("110108");
					accountLocations.add(accountLocation);
				}
				if (typeIndex == 1) {
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setLocationId("420100");
					accountLocations.add(accountLocation);
				}
				account.setLocations(accountLocations);
				media.regiest(account);
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