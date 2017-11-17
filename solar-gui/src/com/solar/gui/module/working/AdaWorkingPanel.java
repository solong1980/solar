package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.solar.client.JsonUtilTool;
import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.RoleType;
import com.solar.entity.SoAccount;

@SuppressWarnings("serial")
public class AdaWorkingPanel extends BasePanel implements ActionListener, Observer {
	JTabbedPane tabbedpane;
	ButtonGroup group;
	JRadioButton top;
	JRadioButton bottom;
	JRadioButton left;
	JRadioButton right;

	ObservableMedia observableMedia = null;

	public static void main(String[] args) {
		AdaWorkingPanel workingPanel = new AdaWorkingPanel();
		JFrame frame = new JFrame("");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(workingPanel, BorderLayout.CENTER);
		workingPanel.setPreferredSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public void loginGUI() {
		JLabel accountLabel = new JLabel(getBoldHTML("帐号"));
		JLabel passwdLabel = new JLabel(getBoldHTML("密码"));

		JComponent loginPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		loginPanel.add(accountLabel, gbc);
		gbc.gridy++;
		loginPanel.add(passwdLabel, gbc);
		
		//admin,operator,cust_1,cust_2
		JTextField accountField = new JTextField("admin");
		JTextField passwordField = new JPasswordField("123456", 10);

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		loginPanel.add(accountField, gbc);
		gbc.gridy++;
		loginPanel.add(passwordField, gbc);

		JComponent[] message = new JComponent[4];
		message[0] = loginPanel;
		String[] options = { "登录", "退出" };
		int result = JOptionPane.showOptionDialog(this, message, "登陆", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		switch (result) {
		case 0: // yes
			Thread td = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						observableMedia = new ObservableMedia();
						observableMedia.addObserver(AdaWorkingPanel.this);
						String acc = accountField.getText();
						String passwd = passwordField.getText();

						SoAccount account = new SoAccount();
						account.setAccount(acc);
						account.setPassword(passwd);
						observableMedia.login(account);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(AdaWorkingPanel.this, "连接后台失败");
						loginGUI();
					}
				}
			});
			td.start();
			break;
		case 1: // no
			System.exit(0);
			break;
		default:
			break;
		}
	}

	public AdaWorkingPanel() {
		super(new BorderLayout());
		JPanel tabControls = new JPanel();
		tabControls.add(new JLabel("Tab位置"));
		top = (JRadioButton) tabControls.add(new JRadioButton("顶部"));
		left = (JRadioButton) tabControls.add(new JRadioButton("左侧"));
		bottom = (JRadioButton) tabControls.add(new JRadioButton("底部"));
		right = (JRadioButton) tabControls.add(new JRadioButton("右侧"));
		add(tabControls, BorderLayout.NORTH);

		group = new ButtonGroup();
		group.add(top);
		group.add(bottom);
		group.add(left);
		group.add(right);

		top.setSelected(true);

		top.addActionListener(this);
		bottom.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				loginGUI();
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == top) {
			tabbedpane.setTabPlacement(JTabbedPane.TOP);
		} else if (e.getSource() == left) {
			tabbedpane.setTabPlacement(JTabbedPane.LEFT);
		} else if (e.getSource() == bottom) {
			tabbedpane.setTabPlacement(JTabbedPane.BOTTOM);
		} else if (e.getSource() == right) {
			tabbedpane.setTabPlacement(JTabbedPane.RIGHT);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof SoRet) {
			SoRet ret = (SoRet) arg;
			int code = ret.getCode();
			switch (code) {
			case ConnectAPI.LOGIN_RESPONSE:
				int status = ret.getStatus();
				if (status == 0) {
					SoAccount account = JsonUtilTool.fromJson(ret.getRet(), SoAccount.class);
					JOptionPane.showMessageDialog(this, account.getMsg());
					RoleType roleType = account.getRoleType();
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							// observableMedia.addObserver(managerPanel);
							// observableMedia.addObserver(workingModeManagerPanel);
							// observableMedia.addObserver(upgradeManagerPanel);
							// Tab panel
							tabbedpane = new JTabbedPane();
							add(tabbedpane, BorderLayout.CENTER);
							// create tab
							switch (roleType) {
							case ADMIN:
								// 用户管理
								String name = " 用户管理";
								UserManagerPanel managerPanel = new UserManagerPanel();
								tabbedpane.add(name, managerPanel);

								// 配置管理
								name = "配置管理";
								WorkingModeManagerPanel workingModeManagerPanel = new WorkingModeManagerPanel();
								tabbedpane.add(name, workingModeManagerPanel);

								// 数据查询
								name = "数据查询";
								JLabel pix = new JLabel(name);
								tabbedpane.add(name, pix);

								// 升级管理
								name = "升级管理";
								UpgradeManagerPanel upgradeManagerPanel = new UpgradeManagerPanel();
								tabbedpane.add(name, upgradeManagerPanel);
								break;
							case OPERATOR:
								// 配置管理
								name = "配置管理";
								workingModeManagerPanel = new WorkingModeManagerPanel();
								tabbedpane.add(name, workingModeManagerPanel);

								// 数据查询
								name = "数据查询";
								pix = new JLabel(name);
								tabbedpane.add(name, pix);
								break;
							case USER:
								// 数据查询
								name = "数据查询";
								pix = new JLabel(name);
								tabbedpane.add(name, pix);
								break;
							default:
								loginGUI();
								break;
							}
						}
					});
				} else {
					JOptionPane.showMessageDialog(this, "登陆失败");
				}
				break;
			default:
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						JOptionPane.showConfirmDialog(AdaWorkingPanel.this, "消息返回");
					}
				});
				break;
			}
		}
	}

}
