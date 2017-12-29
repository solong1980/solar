package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.MYJOptionPane;

import com.solar.client.JsonUtilTool;
import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ActionType;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.Consts.GenVCodeType;
import com.solar.common.context.RoleType;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoAccountLocation;
import com.solar.entity.SoVCode;
import com.solar.gui.component.AddressTreeField;
import com.solar.gui.component.MultiAddressTreeField;
import com.solar.gui.component.MultiComboBox;
import com.solar.gui.component.formate.InputState;
import com.solar.gui.component.formate.JFieldBuilder;
import com.solar.gui.component.model.TreeAddr;
import com.solar.gui.module.working.fuc.AccountAuditPanel;
import com.solar.gui.module.working.fuc.DeviceUpgradeManagerPanel;
import com.solar.gui.module.working.fuc.IndexPanel;
import com.solar.gui.module.working.fuc.ProjectDataPanel;
import com.solar.gui.module.working.fuc.RunningReportPanel;
import com.solar.gui.module.working.fuc.WorkerInfoPanel;

@SuppressWarnings("serial")
public class AdaWorkingPanel extends BasePanel implements ActionListener, Observer {
	private static final int SMS_DISABLE_PERIOD = 10;

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	JTabbedPane tabbedpane;
	ButtonGroup group;
	JRadioButton top;
	JRadioButton bottom;
	JRadioButton left;
	JRadioButton right;

	private JMenuBar menuBar = null;
	private JMenu fileMenu = null;

	JMenuItem connMi;
	JMenuItem regiestMi;
	JMenuItem findBackMi;
	JMenuItem loginMi;
	JMenuItem logoutMi;
	JMenuItem exitMi;

	ObservableMedia observableMedia = ObservableMedia.getInstance();

	private SoAccount account;

	private boolean timerFlag = false;

	class AdaAction extends AbstractAction {
		private ActionType operate;
		private JComponent parent;

		protected AdaAction(ActionType operate, JComponent parent) {
			super("AdaAction");
			this.operate = operate;
		}

		public void actionPerformed(ActionEvent e) {
			switch (operate) {
			case CONNECT:
				try {
					observableMedia.connect();
					observableMedia.addObserver(AdaWorkingPanel.this);

					regiestMi.setEnabled(true);
					findBackMi.setEnabled(true);
					loginMi.setEnabled(true);
					logoutMi.setEnabled(true);

				} catch (Throwable e2) {
					JOptionPane.showMessageDialog(parent, "连接失败", "错误", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case REGIEST:
				registGUI();
				break;
			case FINDBACK:
				findBackGUI();
				break;
			case LOGIN:
				loginGUI();
				break;
			case LOGOUT:
				if (observableMedia != null)
					observableMedia.close();
				break;
			case EXIT:
				if (observableMedia != null)
					observableMedia.close();
				System.exit(0);
				break;
			default:
				break;
			}
		}
	}

	public JMenuBar createMenus() {
		JMenuBar menuBar = new JMenuBar();
		fileMenu = (JMenu) menuBar.add(new JMenu("开始"));
		connMi = createMenuItem(fileMenu, "连接", new AdaAction(ActionType.CONNECT, this));

		regiestMi = createMenuItem(fileMenu, "注册", new AdaAction(ActionType.REGIEST, this), false);
		findBackMi = createMenuItem(fileMenu, "找回", new AdaAction(ActionType.FINDBACK, this), false);
		loginMi = createMenuItem(fileMenu, "登陆", new AdaAction(ActionType.LOGIN, this), false);
		logoutMi = createMenuItem(fileMenu, "登出", new AdaAction(ActionType.LOGOUT, this), false);

		exitMi = createMenuItem(fileMenu, "退出", new AdaAction(ActionType.EXIT, this));
		return menuBar;
	}

	public static void main(String[] args) {
		new AdaWorkingPanel(null);
	}

	private void findBackGUI() {
		JLabel nameLabel = new JLabel(getBoldHTML("用户姓名"));
		JLabel oldPhoneLabel = new JLabel(getBoldHTML("输入原号码"));
		JLabel newPhoneLabel = new JLabel(getBoldHTML("输入新号码"));
		// JLabel emailLabel = new JLabel(getBoldHTML("邮箱地址"));
		JLabel userTypeLabel = new JLabel(getBoldHTML("用户类型"));
		JLabel projectAddr1Label = new JLabel(getBoldHTML("项目地址"));
		JLabel envAddrLabel = new JLabel(getBoldHTML("环保局地址"));
		JLabel vcodeLabel = new JLabel(getBoldHTML("验证码"));
		JComponent findBackPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		findBackPanel.add(nameLabel, gbc);
		gbc.gridy++;
		findBackPanel.add(oldPhoneLabel, gbc);
		gbc.gridy++;
		findBackPanel.add(newPhoneLabel, gbc);
		gbc.gridy++;
		findBackPanel.add(vcodeLabel, gbc);

		gbc.gridy++;
		findBackPanel.add(userTypeLabel, gbc);
		gbc.gridy++;
		findBackPanel.add(projectAddr1Label, gbc);
		gbc.gridy++;
		findBackPanel.add(envAddrLabel, gbc);

		InputState inputState = new InputState();

		JTextField nameField = JFieldBuilder.createNoEmptyField(this, inputState, "", 30, "请输入申请人姓名", 60);

		JTextField oldPhoneField = JFieldBuilder.createPhoneField(this, inputState, true);
		JTextField newPhoneField = JFieldBuilder.createPhoneField(this, inputState, true);
		// JTextField emailField = new JTextField("");
		JComboBox<String> userTypeField = new JComboBox<>();
		userTypeField.addItem("运维");
		userTypeField.addItem("局方 ");
		MultiAddressTreeField projectAddr1Field = MultiAddressTreeField.build();
		AddressTreeField envAddrField = AddressTreeField.buildFoldLeaf();
		JTextField vcodeField = new JTextField("", 30);
		envAddrLabel.setEnabled(false);
		envAddrField.setEnabled(false);

		userTypeField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = userTypeField.getSelectedIndex();
				if (selectedIndex == 0) {
					projectAddr1Label.setEnabled(true);
					projectAddr1Field.setEnabled(true);
					envAddrLabel.setEnabled(false);
					envAddrField.setEnabled(false);
				} else if (selectedIndex == 1) {
					projectAddr1Label.setEnabled(false);
					projectAddr1Field.setEnabled(false);
					envAddrLabel.setEnabled(true);
					envAddrField.setEnabled(true);
				}
			}
		});

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		findBackPanel.add(nameField, gbc);
		gbc.gridy++;
		findBackPanel.add(oldPhoneField, gbc);
		gbc.gridy++;
		findBackPanel.add(newPhoneField, gbc);
		gbc.gridy++;
		findBackPanel.add(vcodeField, gbc);

		JButton getVCodeBtn = new JButton("获取验证码");
		gbc.gridx++;
		findBackPanel.add(getVCodeBtn, gbc);

		gbc.gridx--;
		gbc.gridy++;
		findBackPanel.add(userTypeField, gbc);
		gbc.gridy++;
		findBackPanel.add(projectAddr1Field, gbc);
		gbc.gridy++;
		findBackPanel.add(envAddrField, gbc);

		Timer timer = new Timer();
		getVCodeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getVCodeBtn.setEnabled(false);
				String phone = newPhoneField.getText();
				SoVCode soVCode = new SoVCode();
				soVCode.setType(GenVCodeType.ACCOUNT_FIND.getType());
				soVCode.setPhone(phone);
				ObservableMedia.getInstance().getVCode(soVCode);
				timerFlag = true;
				timer.scheduleAtFixedRate(new TimerTask() {
					int w = SMS_DISABLE_PERIOD;

					@Override
					public void run() {
						if (timerFlag) {
							getVCodeBtn.setText("等待" + w + "s");
							if (w == 0) {
								getVCodeBtn.setText("获取验证码");
								getVCodeBtn.setEnabled(true);
								timerFlag = false;
								w = SMS_DISABLE_PERIOD;
							}
							w = w - 1;
						}
					}
				}, 0, 1000);
			}
		});

		JComponent[] message = new JComponent[4];
		message[0] = findBackPanel;
		String[] options = { "提交审核", "取消" };

		int result = MYJOptionPane.showOptionDialog(this, message, "用  户  找  回(需审核)", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1], (int) screenSize.getWidth() / 2, 100);

		timer.cancel();
		switch (result) {
		case 0: // yes
			SoAccountFind accountFind = new SoAccountFind();
			String name = nameField.getText();
			String oldPhone = oldPhoneField.getText();
			String newPhone = newPhoneField.getText();
			String vcode = vcodeField.getText();
			accountFind.setName(name);
			accountFind.setOldPhone(oldPhone);
			accountFind.setPhone(newPhone);
			accountFind.setVcode(vcode);

			int typeIndex = userTypeField.getSelectedIndex();
			accountFind.setType(Consts.ACCOUNT_TYPE[typeIndex]);
			List<SoAccountLocation> accountLocations = new ArrayList<>();
			if (typeIndex == 0) {
				List<TreeAddr> workerAddrs = projectAddr1Field.getSelectedKeys();
				for (TreeAddr treeAddr : workerAddrs) {
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setLocationId(treeAddr.getKey());
					accountLocations.add(accountLocation);
				}
			} else if (typeIndex == 1) {
				List<TreeAddr> envAddrs = envAddrField.getSelectedKeys();
				for (TreeAddr treeAddr : envAddrs) {
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setLocationId(treeAddr.getKey());
					accountLocations.add(accountLocation);
				}
			}
			accountFind.setLocations(accountLocations);
			ObservableMedia.getInstance().findBack(accountFind);
			break;
		case 1: // no
			break;
		default:
			break;
		}
	}

	private void registGUI() {
		/*
		 * 邮箱地址 用于找回用户信息 用户类型 （下拉复选框） 环保部门工作人员 系统运维人员 项目地址1 （下拉复选框） 省市（地区）区（县市）街道（镇）
		 * 项目地址2 （下拉复选框） 省市（地区）区（县市）街道（镇） 项目地址3 （下拉复选框） 省市（地区）区（县市）街道（镇） 环保局地址 （下拉复选框）
		 * 省市（地区）区（县市）街道（镇）
		 */
		JLabel accountLabel = new JLabel(getBoldHTML("账       号"));
		JLabel passwordLabel = new JLabel(getBoldHTML("密      码"));
		JLabel rePasswordLabel = new JLabel(getBoldHTML("确认密 码"));

		JLabel nameLabel = new JLabel(getBoldHTML("用户姓名"));
		JLabel phoneLabel = new JLabel(getBoldHTML("手机号码"));
		JLabel vcodeLabel = new JLabel(getBoldHTML("验证码"));

		JLabel emailLabel = new JLabel(getBoldHTML("邮箱地址"));
		JLabel userTypeLabel = new JLabel(getBoldHTML("用户类型"));
		JLabel projectAddr1Label = new JLabel(getBoldHTML("项目地址"));
		JLabel envAddrLabel = new JLabel(getBoldHTML("环保局地址"));
		JComponent regiestPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		regiestPanel.add(accountLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(passwordLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(rePasswordLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(nameLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(phoneLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(vcodeLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(emailLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(userTypeLabel, gbc);
		gbc.gridy++;
		regiestPanel.add(projectAddr1Label, gbc);
		gbc.gridy++;
		regiestPanel.add(envAddrLabel, gbc);

		JTextField accountField = new JTextField("");
		JPasswordField passwordField = new JPasswordField();
		JPasswordField rePasswordField = new JPasswordField();
		JTextField nameField = new JTextField("");
		JTextField phoneField = new JTextField("", 30);
		JTextField vcodeField = new JTextField("");
		JTextField emailField = new JTextField("");
		JComboBox<String> userTypeField = new JComboBox<>();
		userTypeField.addItem("运维");
		userTypeField.addItem("局方 ");
		MultiAddressTreeField projectAddr1Field = MultiAddressTreeField.build();
		AddressTreeField envAddrField = AddressTreeField.buildFoldLeaf();

		envAddrLabel.setEnabled(false);
		envAddrField.setEnabled(false);

		userTypeField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = userTypeField.getSelectedIndex();
				if (selectedIndex == 0) {
					projectAddr1Label.setEnabled(true);
					projectAddr1Field.setEnabled(true);
					envAddrLabel.setEnabled(false);
					envAddrField.setEnabled(false);
				}
				if (selectedIndex == 1) {
					projectAddr1Label.setEnabled(false);
					projectAddr1Field.setEnabled(false);
					envAddrLabel.setEnabled(true);
					envAddrField.setEnabled(true);
				}
			}
		});

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		regiestPanel.add(accountField, gbc);
		gbc.gridy++;
		regiestPanel.add(passwordField, gbc);
		gbc.gridy++;
		regiestPanel.add(rePasswordField, gbc);
		gbc.gridy++;
		regiestPanel.add(nameField, gbc);
		gbc.gridy++;
		regiestPanel.add(phoneField, gbc);
		gbc.gridy++;
		regiestPanel.add(vcodeField, gbc);

		JButton getVCodeBtn = new JButton("获取验证码");
		gbc.gridx++;
		regiestPanel.add(getVCodeBtn, gbc);

		gbc.gridx--;
		gbc.gridy++;
		regiestPanel.add(emailField, gbc);
		gbc.gridy++;
		regiestPanel.add(userTypeField, gbc);
		gbc.gridy++;
		regiestPanel.add(projectAddr1Field, gbc);
		gbc.gridy++;
		regiestPanel.add(envAddrField, gbc);

		Timer timer = new Timer();
		getVCodeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getVCodeBtn.setEnabled(false);
				String phone = phoneField.getText();
				SoVCode soVCode = new SoVCode();
				soVCode.setType(GenVCodeType.REGIEST.getType());
				soVCode.setPhone(phone);
				ObservableMedia.getInstance().getVCode(soVCode);
				timerFlag = true;
				timer.scheduleAtFixedRate(new TimerTask() {
					int w = SMS_DISABLE_PERIOD;

					@Override
					public void run() {
						if (timerFlag) {
							getVCodeBtn.setText("等待" + w + "s");
							if (w == 0) {
								getVCodeBtn.setText("获取验证码");
								getVCodeBtn.setEnabled(true);
								timerFlag = false;
								w = SMS_DISABLE_PERIOD;
							}
							w = w - 1;
						}
					}
				}, 0, 1000);
			}
		});

		JComponent[] message = new JComponent[4];
		message[0] = regiestPanel;
		String[] options = { "提交审核", "取消" };
		int result = JOptionPane.showOptionDialog(this, message, "注册新用户(需审核)", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

		timer.cancel();

		switch (result) {
		case 0: // yes
			SoAccount account = new SoAccount();
			String name = nameField.getText();
			String phone = phoneField.getText();
			String email = emailField.getText();
			String vcode = vcodeField.getText();
			account.setName(name);
			account.setPhone(phone);
			account.setEmail(email);

			int typeIndex = userTypeField.getSelectedIndex();
			account.setType(Consts.ACCOUNT_TYPE[typeIndex]);
			List<SoAccountLocation> accountLocations = new ArrayList<>();
			if (typeIndex == 0) {
				List<TreeAddr> workerAddrs = projectAddr1Field.getSelectedKeys();
				for (TreeAddr treeAddr : workerAddrs) {
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setLocationId(treeAddr.getKey());
					accountLocations.add(accountLocation);
				}
			}
			if (typeIndex == 1) {
				List<TreeAddr> envAddrs = envAddrField.getSelectedKeys();
				for (TreeAddr treeAddr : envAddrs) {
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setLocationId(treeAddr.getKey());
					accountLocations.add(accountLocation);
				}
			}
			account.setLocations(accountLocations);
			account.setVcode(vcode);
			ObservableMedia.getInstance().regiest(account);
			break;
		case 1: // no
			break;
		default:
			break;
		}
	}

	String accLabelStr = "帐 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号";
	String pwdLabelStr = "密  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码";

	public void loginGUI() {
		JLabel accountLabel = new JLabel(getBoldHTML(accLabelStr));
		JLabel passwdLabel = new JLabel(getBoldHTML(pwdLabelStr));
		// JLabel savePwdLabel = new JLabel(getBoldHTML(""));

		JComponent loginPanel = new JPanel(new GridBagLayout());
		loginPanel.setPreferredSize(new Dimension(300, 100));
		GridBagConstraints gbc = new GridBagConstraints();

		JTextField accountField = new JTextField("admin");
		JTextField passwordField = new JPasswordField("123456", 10);
		JCheckBox phoneLoginCheckBox = new JCheckBox(getBoldHTML("手机号登录"));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridwidth = 3;
		loginPanel.add(phoneLoginCheckBox, gbc);

		gbc.gridwidth = 1;
		gbc.gridy++;
		loginPanel.add(accountLabel, gbc);
		gbc.gridy++;
		loginPanel.add(passwdLabel, gbc);
		// gbc.gridy++;
		// loginPanel.add(savePwdLabel, gbc);

		gbc.gridx++;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		loginPanel.add(accountField, gbc);
		gbc.gridy++;
		gbc.gridwidth = 2;
		loginPanel.add(passwordField, gbc);

		phoneLoginCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (phoneLoginCheckBox.isSelected()) {
					accountLabel.setText(getBoldHTML("手机号码"));
				} else {
					accountLabel.setText(getBoldHTML(accLabelStr));
				}
			}
		});

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
						String acc = accountField.getText();
						String passwd = passwordField.getText();
						account = new SoAccount();
						if (phoneLoginCheckBox.isSelected())
							account.setPhone(acc);
						else
							account.setAccount(acc);
						account.setPassword(passwd);
						account.setSavePwd(phoneLoginCheckBox.isSelected());
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
			break;
		default:
			break;
		}
	}

	public void singleFrame() {
		JFrame frame = new JFrame("");
		menuBar = createMenus();
		frame.setJMenuBar(menuBar);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setBackground(Color.DARK_GRAY);
		// this.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	public AdaWorkingPanel(JComponent parent) {
		super(new BorderLayout());
		if (parent == null)
			singleFrame();
	}

	@SuppressWarnings("unused")
	private void addTabLayoutCtrlPanel() {
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
			int status = ret.getStatus();
			if (status == 0) {
				switch (code) {
				case ConnectAPI.LOGIN_RESPONSE:
					SoAccount account = JsonUtilTool.fromJson(ret.getRet(), SoAccount.class);
					// 保存会话信息
					ObservableMedia.getInstance().setSessionAccount(account);

					JOptionPane.showMessageDialog(this, account.getMsg());

					int retCode = account.getRetCode();

					if (retCode != SoAccount.SUCCESS) {
						loginGUI();
						return;
					}

					RoleType roleType = RoleType.roleType(account.getRole());

					Boolean savePwd = AdaWorkingPanel.this.account.getSavePwd();
					if (savePwd) {
						//
					}
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							// disable connect , regiest and findback
							connMi.setEnabled(false);
							regiestMi.setEnabled(false);
							findBackMi.setEnabled(false);
							loginMi.setEnabled(false);

							// Tab panel
							tabbedpane = new JTabbedPane();
							add(tabbedpane, BorderLayout.CENTER);

							String tabName = "";

							// RunningDataPanel runningDataPanel;
							// WorkingModeManagerPanel workingModeManagerPanel;
							// create tab
							switch (roleType) {
							case ADMIN:
								tabName = " 首页";
								IndexPanel indexPanel = new IndexPanel();
								tabbedpane.add(tabName, indexPanel);
								observableMedia.addObserver(indexPanel);

								ProjectDataPanel projectDataPanel = new ProjectDataPanel();
								tabbedpane.add("项目信息", projectDataPanel);
								observableMedia.addObserver(projectDataPanel);

								WorkerInfoPanel workerInfoPanel = new WorkerInfoPanel();
								tabbedpane.add("维护人员信息", workerInfoPanel);
								observableMedia.addObserver(workerInfoPanel);

								RunningReportPanel reportPanel = new RunningReportPanel();
								tabbedpane.add("运行信息查询", reportPanel);
								
								AccountAuditPanel accountAuditPanel = new AccountAuditPanel();
								observableMedia.addObserver(accountAuditPanel);
								tabbedpane.add("注册找回信息审核", accountAuditPanel);
								
								
								DeviceUpgradeManagerPanel deviceUpgradeManagerPanel = new DeviceUpgradeManagerPanel();
								observableMedia.addObserver(deviceUpgradeManagerPanel);
								tabbedpane.add("升级块缓存", deviceUpgradeManagerPanel);
								
								// 用户管理
								// tabName = " 用户管理";
								// UserManagerPanel userManagerPanel = new UserManagerPanel();
								// tabbedpane.add(tabName, userManagerPanel);
								// observableMedia.addObserver(userManagerPanel);

								// 升级管理
								// tabName = "升级管理";
								// UpgradeManagerPanel upgradeManagerPanel = new UpgradeManagerPanel();
								// tabbedpane.add(tabName, upgradeManagerPanel);
								// observableMedia.addObserver(upgradeManagerPanel);
								break;
							case OPERATOR:
								tabName = " 首页";
								indexPanel = new IndexPanel();
								tabbedpane.add(tabName, indexPanel);
								// 配置管理
								// tabName = "配置管理";
								// workingModeManagerPanel = new WorkingModeManagerPanel();
								// tabbedpane.add(tabName, workingModeManagerPanel);
								// observableMedia.addObserver(workingModeManagerPanel);
								break;
							case USER:
								tabName = " 首页";
								indexPanel = new IndexPanel();
								tabbedpane.add(tabName, indexPanel);
								// 数据查询
								// tabName = "数据查询";
								// runningDataPanel = new RunningDataPanel();
								// tabbedpane.add(tabName, runningDataPanel);
								// observableMedia.addObserver(runningDataPanel);
								break;
							default:
								loginGUI();
								break;
							}

						}
					});
					break;
				case ConnectAPI.ACCOUNT_ADD_RESPONSE:
					account = JsonUtilTool.fromJson(ret.getRet(), SoAccount.class);
					JOptionPane.showMessageDialog(this, account.getMsg());
					break;
				case ConnectAPI.ACCOUNT_FINDBACK_RESPONSE:
					SoAccountFind accountFind = JsonUtilTool.fromJson(ret.getRet(), SoAccountFind.class);
					JOptionPane.showMessageDialog(this, accountFind.getMsg());
					break;
				default:
					break;
				}
			}
			if (status == 1) {
				switch (code) {
				case ConnectAPI.LOGIN_RESPONSE:
					JOptionPane.showMessageDialog(this, "登陆失败");
					break;
				case ConnectAPI.CONNECTLOST_RESPONSE:
					// 连接丢失,disable所有,弹出提示
					int componentCount = this.getComponentCount();
					for (int i = 0; i < componentCount; i++) {
						Component component2 = this.getComponent(i);
						this.remove(component2);
					}
					repaint();
				default:
					JOptionPane.showMessageDialog(this, ret.getRet());
				}
			}
		}
	}

}
