package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.alibaba.fastjson.TypeReference;
import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.Consts.AddrType;
import com.solar.common.util.JsonUtilTool;
import com.solar.common.util.LocationLoader;
import com.solar.entity.SoAbtAuth;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProject;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.entity.SoRunningData;
import com.solar.gui.component.formate.ButtonUI;
import com.solar.gui.component.formate.JTextFieldLimit;
import com.solar.gui.component.model.TreeAddr;
import com.solar.gui.module.working.BasePanel;
import com.solar.timer.RunningDataCron;

/**
 * 首页
 */
@SuppressWarnings("serial")
public class IndexPanel extends BasePanel implements Observer {

	private static final String PROJECT = "project";

	private static final String DEVICE = "device";

	private static final String EMPTY = "empty";

	JSplitPane splitPane = null;

	JScrollPane projectPanel;

	JPanel dashPanel = new JPanel();

	List<JCheckBox> workingTimeCheckBoxs = new ArrayList<>();

	RunningDataCron runningDataCron = new RunningDataCron();

	public void createDeviceDialog() {
		JComponent devicePanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel deviceNoLabel = new JLabel(getBoldHTML("设备号"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		devicePanel.add(deviceNoLabel, gbc);

		JTextField devNoField = new JTextField(30);
		devNoField.setDocument(new JTextFieldLimit(8));
		devNoField.requestFocus();

		gbc.gridx++;
		gbc.gridwidth = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		devicePanel.add(devNoField, gbc);

		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridy++;
		devicePanel.add(new JLabel("模式 "), gbc);

		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.gridy++;
		devicePanel.add(new JLabel("分机1控制 "), gbc);
		gbc.gridy++;
		devicePanel.add(new JLabel("分机2控制 "), gbc);
		gbc.gridy++;
		devicePanel.add(new JLabel("水泵1控制 "), gbc);
		gbc.gridy++;
		devicePanel.add(new JLabel("水泵2控制"), gbc);
		gbc.gridy++;
		devicePanel.add(new JLabel("继电器1控制"), gbc);
		gbc.gridy++;
		devicePanel.add(new JLabel("继电器2控制"), gbc);
		gbc.gridy++;
		devicePanel.add(new JLabel("继电器3控制"), gbc);
		gbc.gridy++;
		devicePanel.add(new JLabel("继电器4控制"), gbc);

		JRadioButton[] startBtns = new JRadioButton[8];
		JRadioButton[] stopBtns = new JRadioButton[8];
		JRadioButton[] autoBtns = new JRadioButton[8];

		ButtonGroup tg = new ButtonGroup();
		JRadioButton handModeBtn = ButtonUI.makeRadioBtn("手动模式", "");
		JRadioButton autoModeBtn = ButtonUI.makeRadioBtn("自动模式", "");

		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		tg.add(handModeBtn);
		tg.add(autoModeBtn);
		devicePanel.add(handModeBtn, gbc);
		handModeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (handModeBtn.isSelected()) {
					for (JRadioButton bt : startBtns) {
						bt.setSelected(true);
						bt.setEnabled(true);
					}
					for (JRadioButton bt : stopBtns) {
						bt.setEnabled(true);
					}
					for (JRadioButton bt : autoBtns) {
						bt.setEnabled(false);
						bt.setSelected(false);
					}
				} else {
					for (JRadioButton bt : startBtns) {
						bt.setSelected(false);
						bt.setEnabled(false);
					}
					for (JRadioButton bt : stopBtns) {
						bt.setEnabled(false);
						bt.setSelected(false);
					}
					for (JRadioButton bt : autoBtns) {
						bt.setEnabled(true);
					}
				}

			}
		});

		gbc.gridx = 3;
		gbc.gridwidth = 1;
		devicePanel.add(autoModeBtn, gbc);
		autoModeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (autoModeBtn.isSelected()) {
					for (JRadioButton bt : startBtns) {
						bt.setSelected(false);
						bt.setEnabled(false);
					}
					for (JRadioButton bt : stopBtns) {
						bt.setEnabled(false);
						bt.setSelected(false);
					}
					for (JRadioButton bt : autoBtns) {
						bt.setEnabled(true);
						bt.setSelected(true);
					}
				} else {
					for (JRadioButton bt : startBtns) {
						bt.setEnabled(true);
					}
					for (JRadioButton bt : stopBtns) {
						bt.setEnabled(true);
					}
					for (JRadioButton bt : autoBtns) {
						bt.setSelected(false);
						bt.setEnabled(false);
					}
				}
			}
		});
		autoModeBtn.setSelected(true);

		gbc.gridwidth = 1;
		gbc.gridy = 2;

		ButtonGroup[] buttonGroups = new ButtonGroup[8];
		for (int i = 0; i < 8; i++) {
			buttonGroups[i] = new ButtonGroup();
			startBtns[i] = ButtonUI.makeRadioBtn("持续运行", "");
			stopBtns[i] = ButtonUI.makeRadioBtn("停止运行", "");
			autoBtns[i] = ButtonUI.makeRadioBtn("自动控制", "");
			autoBtns[i].setSelected(true);
			startBtns[i].setEnabled(false);
			stopBtns[i].setEnabled(false);
			buttonGroups[i].add(startBtns[i]);
			buttonGroups[i].add(stopBtns[i]);
			buttonGroups[i].add(autoBtns[i]);
			gbc.gridx = 1;
			devicePanel.add(startBtns[i], gbc);
			gbc.gridx = 2;
			devicePanel.add(stopBtns[i], gbc);
			gbc.gridx = 3;
			devicePanel.add(autoBtns[i], gbc);
			gbc.gridy++;
		}

		JComponent[] message = new JComponent[1];
		message[0] = devicePanel;
		String[] options = { "提交", "取消" };
		int result = JOptionPane.showOptionDialog(this, message, "新增设备", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		switch (result) {
		case 0: // yes
			String devNo = devNoField.getText();
			if (devNo == null || devNo.trim().isEmpty()) {
				// "设备号必填"
				JOptionPane.showMessageDialog(this, "设备号不能为空");
				return;
			} else {
				if (project == null) {
					JOptionPane.showMessageDialog(this, "未选择项目");
					return;
				}
				SoDevices devices = new SoDevices();
				devices.setDevNo(devNo);
				devices.setProjectId(projectId);
				devices.setLocationId(project.getLocationId());
				devices.setSw0((short) (startBtns[0].isSelected() ? 1 : (stopBtns[0].isSelected() ? 0 : 2)));
				devices.setSw1((short) (startBtns[1].isSelected() ? 1 : (stopBtns[1].isSelected() ? 0 : 2)));
				devices.setSw2((short) (startBtns[2].isSelected() ? 1 : (stopBtns[2].isSelected() ? 0 : 2)));
				devices.setSw3((short) (startBtns[3].isSelected() ? 1 : (stopBtns[3].isSelected() ? 0 : 2)));
				devices.setSw4((short) (startBtns[4].isSelected() ? 1 : (stopBtns[4].isSelected() ? 0 : 2)));
				devices.setSw5((short) (startBtns[5].isSelected() ? 1 : (stopBtns[5].isSelected() ? 0 : 2)));
				devices.setSw6((short) (startBtns[6].isSelected() ? 1 : (stopBtns[6].isSelected() ? 0 : 2)));
				devices.setSw7((short) (startBtns[7].isSelected() ? 1 : (stopBtns[7].isSelected() ? 0 : 2)));
				ObservableMedia.getInstance().addDevice(devices);

			}
			break;
		case 1: // no
			break;
		default:
			break;
		}
	}

	public void createDashpanel() {
		dashPanel.setLayout(cardLayout);

		JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new BorderLayout());
		projectPanel.add(createGPSPanel(), BorderLayout.NORTH);

		JPanel schedulePanel = new JPanel();
		schedulePanel.setLayout(new BorderLayout());
		JPanel checkPanel = new JPanel(new GridLayout(12, 4));
		for (int i = 0; i < 48; i++) {
			JPanel jPanel = new JPanel(new BorderLayout());
			// 0:00-0:30
			JCheckBox box = new JCheckBox((30 * i / 60) + ":" + String.format("%02d", (30 * i % 60)) + "-"
					+ (30 * (i + 1) / 60) + ":" + String.format("%02d", (30 * (i + 1) % 60)));

			jPanel.add(new JLabel(String.format("%02d", i + 1) + "."), BorderLayout.WEST);
			jPanel.add(box, BorderLayout.CENTER);

			workingTimeCheckBoxs.add(box);
			checkPanel.add(jPanel);
		}
		JScrollPane scheduleCheckBoxScrollPane = new JScrollPane(checkPanel);
		schedulePanel.add(new JLabel("定时运行配置"), BorderLayout.NORTH);
		schedulePanel.add(scheduleCheckBoxScrollPane, BorderLayout.CENTER);
		projectPanel.add(schedulePanel, BorderLayout.CENTER);
		JButton updateRunMode = new JButton("设定运行模式");
		updateRunMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (IndexPanel.this.projectId == null) {
					return;
				}
				SoProjectWorkingMode mode = IndexPanel.this.workingMode;
				if (mode == null) {
					mode = new SoProjectWorkingMode();
					mode.setProjectId(IndexPanel.this.projectId);
				}
				for (int i = 0; i < workingTimeCheckBoxs.size(); i++) {
					JCheckBox hCheckBox = workingTimeCheckBoxs.get(i);
					Method method;
					try {
						method = SoProjectWorkingMode.class.getMethod("setH_" + i, Short.class);
						method.invoke(mode, hCheckBox.isSelected() ? (short) 1 : (short) 0);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}

				instance.updateProjectWorkingMode(mode);
			}
		});
		schedulePanel.add(updateRunMode, BorderLayout.SOUTH);

		JPanel empty = new JPanel();
		JPanel devicePanel = createInfoPanel();

		dashPanel.add(empty);
		dashPanel.add(devicePanel);
		dashPanel.add(projectPanel);

		cardLayout.addLayoutComponent(empty, EMPTY);
		cardLayout.addLayoutComponent(devicePanel, DEVICE);
		cardLayout.addLayoutComponent(projectPanel, PROJECT);
	}

	JRadioButton[] startBtns = new JRadioButton[8];
	JRadioButton[] stopBtns = new JRadioButton[8];
	JRadioButton[] autoBtns = new JRadioButton[8];
	JRadioButton handModeBtn = ButtonUI.makeRadioBtn("手动模式", "");
	JRadioButton autoModeBtn = ButtonUI.makeRadioBtn("自动模式", "");

	public JPanel createCtrlPanel() {
		// JPanel ctrlPanel = new JPanel();
		// ctrlPanel.setBorder(BorderFactory.createTitledBorder("控制面板"));
		// ctrlPanel.setLayout(new BorderLayout());
		JPanel runCtrlPanel = new JPanel();
		runCtrlPanel.setLayout(new GridBagLayout());
		runCtrlPanel.setBorder(BorderFactory.createTitledBorder("紧急启停"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(1, 1, 1, 1);

		runCtrlPanel.add(new JLabel("模式 "), gbc);

		gbc.gridy++;
		runCtrlPanel.add(new JLabel("分机1控制 "), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("分机2控制 "), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("水泵1控制 "), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("水泵2控制"), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("继电器1控制"), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("继电器2控制"), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("继电器3控制"), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("继电器4控制"), gbc);

		ButtonGroup[] buttonGroups = new ButtonGroup[8];
		for (int i = 0; i < 8; i++) {
			buttonGroups[i] = new ButtonGroup();
			startBtns[i] = ButtonUI.makeRadioBtn("启动", "");
			stopBtns[i] = ButtonUI.makeRadioBtn("停止", "");
			autoBtns[i] = ButtonUI.makeRadioBtn("自动", "");
			autoBtns[i].setSelected(true);
			buttonGroups[i].add(startBtns[i]);
			buttonGroups[i].add(stopBtns[i]);
			buttonGroups[i].add(autoBtns[i]);
		}

		ButtonGroup tg = new ButtonGroup();
		tg.add(handModeBtn);
		tg.add(autoModeBtn);
		handModeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handBtnSet();
			}

		});
		autoModeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				autoButtonSet();
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		runCtrlPanel.add(handModeBtn, gbc);
		gbc.gridwidth = 1;
		gbc.gridy++;
		runCtrlPanel.add(startBtns[0], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[1], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[2], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[3], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[4], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[5], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[6], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[7], gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;

		gbc.gridy++;
		runCtrlPanel.add(stopBtns[0], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[1], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[2], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[3], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[4], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[5], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[6], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[7], gbc);

		gbc.gridx = 3;
		gbc.gridy = 0;
		runCtrlPanel.add(autoModeBtn, gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[0], gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[1], gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[2], gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[3], gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[4], gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[5], gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[6], gbc);
		gbc.gridy++;
		runCtrlPanel.add(autoBtns[7], gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 3;
		gbc.gridwidth = 3;
		JButton submitBtn = new JButton("提交启停设置 ");
		submitBtn.addActionListener(new ActionListener() {

			private void setV(int j, short v) {
				try {
					Method method = SoDevices.class.getMethod("setSw" + j, Short.class);
					method.invoke(IndexPanel.this.device, v);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (IndexPanel.this.device == null) {
					System.out.println("device null");
					return;
				}
				for (int j = 0; j < buttonGroups.length; j++) {
					JRadioButton start = startBtns[j];
					JRadioButton stop = stopBtns[j];
					JRadioButton auto = autoBtns[j];
					if (start.isSelected()) {
						setV(j, (short) 1);
					}
					if (stop.isSelected()) {
						setV(j, (short) 0);
					}
					if (auto.isSelected()) {
						setV(j, (short) 2);
					}
				}
				instance.updateDevice(IndexPanel.this.device);
			}
		});
		runCtrlPanel.add(submitBtn, gbc);

		// ctrlPanel.add(runCtrlPanel, BorderLayout.CENTER);
		return runCtrlPanel;
	}

	CardLayout cardLayout = new CardLayout();
	//BezierAnimationPanel animationPanel;
	DevRunningAnimationPanel animationPanel;
	private void handBtnSet() {
		if (handModeBtn.isSelected()) {
			for (JRadioButton bt : startBtns) {
				bt.setEnabled(true);
			}
			for (JRadioButton bt : stopBtns) {
				bt.setEnabled(true);
			}
			for (JRadioButton bt : autoBtns) {
				bt.setEnabled(false);
			}
		}

	}

	private void autoButtonSet() {
		if (autoModeBtn.isSelected()) {
			for (JRadioButton bt : startBtns) {
				bt.setEnabled(false);
			}
			for (JRadioButton bt : stopBtns) {
				bt.setEnabled(false);
			}
			for (JRadioButton bt : autoBtns) {
				bt.setEnabled(true);
			}
		}
	}

	JButton solarBoardBtn = ButtonUI.makeImgBtn("buttons/rbp.gif");
	JButton batteryBtn = ButtonUI.makeImgBtn("buttons/rbp.gif");
	JButton fanOperationBtn = ButtonUI.makeImgBtn("buttons/rbp.gif");
	JButton pumpOperationBtn = ButtonUI.makeImgBtn("buttons/rbp.gif");

	public JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("运行信息"));

		JPanel statePanel = createStatePanel();
		JPanel warnningPanel = new JPanel();
		warnningPanel.setLayout(new GridBagLayout());
		warnningPanel.setBorder(BorderFactory.createTitledBorder("报警信息"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		// gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(1, 1, 1, 1);
		warnningPanel.add(new JLabel("太阳能板"), gbc);
		gbc.gridy++;
		warnningPanel.add(new JLabel("电池 "), gbc);
		gbc.gridy++;
		warnningPanel.add(new JLabel("风机运行 "), gbc);
		gbc.gridy++;
		warnningPanel.add(new JLabel("水泵运行 "), gbc);

		gbc.gridy = 0;
		gbc.gridx++;
		warnningPanel.add(solarBoardBtn, gbc);
		gbc.gridy++;
		warnningPanel.add(batteryBtn, gbc);
		gbc.gridy++;
		warnningPanel.add(fanOperationBtn, gbc);
		gbc.gridy++;
		warnningPanel.add(pumpOperationBtn, gbc);
		gbc.gridy++;

		JPanel np = new JPanel(new GridLayout(1, 2));
		np.add(statePanel);
		np.add(warnningPanel);

		//animationPanel = new BezierAnimationPanel();
		animationPanel = new DevRunningAnimationPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(np, BorderLayout.NORTH);
		infoPanel.add(animationPanel, BorderLayout.CENTER);
		infoPanel.add(createCtrlPanel(), BorderLayout.EAST);
		return infoPanel;
	}

	/**
	 * 运行状态
	 * 
	 * @return
	 */
	JButton correctBtn = ButtonUI.makeImgBtn("buttons/rbp.gif");
	JButton breakBtn = ButtonUI.makeImgBtn("buttons/rbp.gif");
	JTextField safeDaysField = new JTextField("天  小时   分", 20);
	JTextField lastBreakTimeField = new JTextField("年  月   日   小时    分", 20);

	public JPanel createStatePanel() {
		safeDaysField.setEditable(false);
		lastBreakTimeField.setEditable(false);
		
		JPanel statePanel = new JPanel();
		statePanel.setLayout(new GridBagLayout());
		statePanel.setBorder(BorderFactory.createTitledBorder("运行状态"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);

		statePanel.add(new JLabel("正常:"), gbc);
		gbc.gridx++;
		statePanel.add(new JLabel("故障:"), gbc);
		gbc.gridx--;

		gbc.gridy++;

		statePanel.add(correctBtn, gbc);
		gbc.gridx++;
		statePanel.add(breakBtn, gbc);

		gbc.gridx--;
		gbc.gridy++;
		statePanel.add(new JLabel("连续无故障运行时长："), gbc);
		gbc.gridy++;
		statePanel.add(new JLabel("上次故障发生日期："), gbc);

		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 2;
		statePanel.add(safeDaysField, gbc);
		gbc.gridy++;
		statePanel.add(lastBreakTimeField, gbc);
		return statePanel;
	}

	private Long projectId = null;
	private SoProject project = null;
	private SoProjectWorkingMode workingMode = null;
	private SoDevices device = null;
	private DefaultMutableTreeNode selectedNode = null;

	JTextField projectNameField = new JTextField("", 20);
	JTextField locationField = new JTextField("", 20);
	JTextField cabField = new JTextField("", 20);
	JTextField standField = new JTextField("", 20);
	JTextField standDichageField = new JTextField("", 20);
	JTextField solarEnergyPowerField = new JTextField("", 20);

	public JPanel createGPSPanel() {
		JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel gpsPanel = new JPanel();
		{
			gpsPanel.setBorder(BorderFactory.createTitledBorder("项目信息"));
			gpsPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(5, 5, 5, 5);
			gpsPanel.add(new JLabel("项目名称:"), gbc);
			gbc.gridy++;
			gpsPanel.add(new JLabel("项目位置:"), gbc);
			gbc.gridy++;
			gpsPanel.add(new JLabel("设计处理量："), gbc);
			gbc.gridy++;
			gpsPanel.add(new JLabel("排放标准："), gbc);
			gbc.gridy++;
			gpsPanel.add(new JLabel("达标排放量："), gbc);
			gbc.gridy++;
			gpsPanel.add(new JLabel("太阳能发电电能："), gbc);

			gbc.gridx++;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gpsPanel.add(projectNameField, gbc);
			gbc.gridy++;
			gpsPanel.add(locationField, gbc);
			gbc.gridy++;
			gpsPanel.add(cabField, gbc);
			gbc.gridy++;
			gpsPanel.add(standField, gbc);
			gbc.gridy++;
			gpsPanel.add(standDichageField, gbc);
			gbc.gridy++;
			gpsPanel.add(solarEnergyPowerField, gbc);
			solarEnergyPowerField.setEditable(false);

			gbc.gridx = 0;
			gbc.gridy++;
			gbc.gridwidth = 2;
			JButton addDeviceBtn = new JButton("添加设备");
			addDeviceBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					createDeviceDialog();
				}
			});
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gpsPanel.add(addDeviceBtn, gbc);
			// gbc.gridx++;
			// gbc.gridy = 0;
			// gbc.fill = GridBagConstraints.HORIZONTAL;
			// gpsPanel.add(new JButton("地图"), gbc);
		}

		JComponent devicePanel = new JPanel(new GridBagLayout());
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			gbc.insets = new Insets(5, 5, 5, 5);

			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridy++;
			devicePanel.add(new JLabel("模式 "), gbc);

			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridy++;
			devicePanel.add(new JLabel("分机1控制 "), gbc);
			gbc.gridy++;
			devicePanel.add(new JLabel("分机2控制 "), gbc);
			gbc.gridy++;
			devicePanel.add(new JLabel("水泵1控制 "), gbc);
			gbc.gridy++;
			devicePanel.add(new JLabel("水泵2控制"), gbc);
			gbc.gridy++;
			devicePanel.add(new JLabel("继电器1控制"), gbc);
			gbc.gridy++;
			devicePanel.add(new JLabel("继电器2控制"), gbc);
			gbc.gridy++;
			devicePanel.add(new JLabel("继电器3控制"), gbc);
			gbc.gridy++;
			devicePanel.add(new JLabel("继电器4控制"), gbc);

			JRadioButton[] startBtns = new JRadioButton[8];
			JRadioButton[] stopBtns = new JRadioButton[8];
			JRadioButton[] autoBtns = new JRadioButton[8];

			ButtonGroup tg = new ButtonGroup();
			JRadioButton handModeBtn = ButtonUI.makeRadioBtn("手动模式", "");
			JRadioButton autoModeBtn = ButtonUI.makeRadioBtn("自动模式", "");

			gbc.gridy = 1;
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			tg.add(handModeBtn);
			tg.add(autoModeBtn);
			devicePanel.add(handModeBtn, gbc);
			handModeBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (handModeBtn.isSelected()) {
						for (JRadioButton bt : startBtns) {
							bt.setSelected(true);
							bt.setEnabled(true);
						}
						for (JRadioButton bt : stopBtns) {
							bt.setEnabled(true);
						}
						for (JRadioButton bt : autoBtns) {
							bt.setEnabled(false);
							bt.setSelected(false);
						}
					} else {
						for (JRadioButton bt : startBtns) {
							bt.setSelected(false);
							bt.setEnabled(false);
						}
						for (JRadioButton bt : stopBtns) {
							bt.setEnabled(false);
							bt.setSelected(false);
						}
						for (JRadioButton bt : autoBtns) {
							bt.setEnabled(true);
						}
					}

				}
			});

			gbc.gridx = 3;
			gbc.gridwidth = 1;
			devicePanel.add(autoModeBtn, gbc);
			autoModeBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (autoModeBtn.isSelected()) {
						for (JRadioButton bt : startBtns) {
							bt.setSelected(false);
							bt.setEnabled(false);
						}
						for (JRadioButton bt : stopBtns) {
							bt.setEnabled(false);
							bt.setSelected(false);
						}
						for (JRadioButton bt : autoBtns) {
							bt.setEnabled(true);
							bt.setSelected(true);
						}
					} else {
						for (JRadioButton bt : startBtns) {
							bt.setEnabled(true);
						}
						for (JRadioButton bt : stopBtns) {
							bt.setEnabled(true);
						}
						for (JRadioButton bt : autoBtns) {
							bt.setSelected(false);
							bt.setEnabled(false);
						}
					}
				}
			});
			autoModeBtn.setSelected(true);

			gbc.gridwidth = 1;
			gbc.gridy = 2;

			ButtonGroup[] buttonGroups = new ButtonGroup[8];
			for (int i = 0; i < 8; i++) {
				buttonGroups[i] = new ButtonGroup();
				startBtns[i] = ButtonUI.makeRadioBtn("持续运行", "");
				stopBtns[i] = ButtonUI.makeRadioBtn("停止运行", "");
				autoBtns[i] = ButtonUI.makeRadioBtn("自动控制", "");
				autoBtns[i].setSelected(true);
				startBtns[i].setEnabled(false);
				stopBtns[i].setEnabled(false);
				buttonGroups[i].add(startBtns[i]);
				buttonGroups[i].add(stopBtns[i]);
				buttonGroups[i].add(autoBtns[i]);
				gbc.gridx = 1;
				devicePanel.add(startBtns[i], gbc);
				gbc.gridx = 2;
				devicePanel.add(stopBtns[i], gbc);
				gbc.gridx = 3;
				devicePanel.add(autoBtns[i], gbc);
				gbc.gridy++;
			}

			gbc.gridx = 4;
			gbc.gridy = 3;
			gbc.weightx = 1;
			gbc.gridwidth = 2;
			JButton submitBtn = new JButton("提交启停设置 ");
			submitBtn.addActionListener(new ActionListener() {
				private void setV(SoDevices device, int j, short v) {
					try {
						Method method = SoDevices.class.getMethod("setSw" + j, Short.class);
						method.invoke(device, v);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					if (IndexPanel.this.projectId != null && IndexPanel.this.project != null) {
						List<SoDevices> devices = IndexPanel.this.project.getDevices();
						if (devices != null) {
							for (SoDevices device : devices) {
								for (int j = 0; j < buttonGroups.length; j++) {
									JRadioButton start = startBtns[j];
									JRadioButton stop = stopBtns[j];
									JRadioButton auto = autoBtns[j];
									if (start.isSelected()) {
										setV(device, j, (short) 1);
									}
									if (stop.isSelected()) {
										setV(device, j, (short) 0);
									}
									if (auto.isSelected()) {
										setV(device, j, (short) 2);
									}
								}
							}
							// 批量更新设备启停状态
							instance.updateDevice(devices);
						}
					} else {
						System.out.println("project id empty");
						return;
					}
				}
			});
			devicePanel.add(submitBtn, gbc);
		}

		projectPanel.add(gpsPanel);
		projectPanel.add(devicePanel);

		return projectPanel;
	}

	JPopupMenu devTreePopmenu = new JPopupMenu();
	JMenuItem rmDevFromProj = new JMenuItem("从项目移除");
	// JMenuItem addDevToProj = new JMenuItem("添加到项目");

	public void showmenu(int x, int y) {
		devTreePopmenu.show(deviceTree, x, y);
	}

	private TreePath popSelPath = null;

	public IndexPanel() {
		super(new BorderLayout(5, 5));

		devTreePopmenu.add(rmDevFromProj);
		// devTreePopmenu.add(addDevToProj);
		rmDevFromProj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int ret = JOptionPane.showConfirmDialog(IndexPanel.this, "确认删除", "删除", JOptionPane.OK_CANCEL_OPTION);
				if (ret == 0) {
					String devNo = e.getActionCommand();
					instance.deleteDevices(devNo);
					rmDevFromProj.setActionCommand(null);
				}
			}
		});
		runningDataCron.startFetchRunningData();
		JPanel projectTreePanel = createTree(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				Object lastPathComponent = path.getLastPathComponent();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
				IndexPanel.this.selectedNode = node;
				Object userObject = node.getUserObject();
				if (userObject instanceof TreeAddr) {
					TreeAddr ta = (TreeAddr) userObject;
					AddrType addrType = ta.getAddrType();
					String id = ta.getKey();
					Object value = ta.getValue();
					switch (addrType) {
					case PROJECT:
						runningDataCron.setRunning(false);
						SoProject project = (SoProject) value;
						updateProjectPanelInfo(project);
						if (node.getChildCount() > 0) {
							return;
						}
						// Load device
						// node.add
						List<SoDevices> devices = dataClient.getDeviceIn(Long.parseLong(id));
						if (devices != null) {
							for (SoDevices device : devices) {
								node.add(new DefaultMutableTreeNode(
										new TreeAddr(AddrType.DEVICE, device.getId().toString(), device, true)));
							}
							project.setDevices(devices);
						}

						break;
					case DEVICE:
						// monitor device
						System.out.println("Show DEVICE " + id);
						SoDevices device = (SoDevices) value;
						String devNo = device.getDevNo();
						instance.getRunningData(devNo);
						updateDevicePanelInfo(device);
						runningDataCron.setDevNo(devNo);
						runningDataCron.setRunning(true);
						break;
					default:
						runningDataCron.setRunning(false);
						cardLayout.show(dashPanel, EMPTY);
						break;
					}
				} else {
					IndexPanel.this.selectedNode = null;
				}
			}

			private void updateDevicePanelInfo(SoDevices device) {
				IndexPanel.this.device = device;
				Method[] methods = SoDevices.class.getMethods();
				for (Method method : methods) {
					String mn = method.getName();
					if (mn.startsWith("getSw")) {
						try {
							String no = mn.replaceAll("getSw", "");
							Object v = method.invoke(device);
							Short f = (Short) v;
							if (f == (short) 1) {
								startBtns[Integer.parseInt(no)].setSelected(true);
								handModeBtn.setSelected(true);
							} else if (f == (short) 0) {
								stopBtns[Integer.parseInt(no)].setSelected(true);
								handModeBtn.setSelected(true);
							} else {
								autoBtns[Integer.parseInt(no)].setSelected(true);
								autoModeBtn.setSelected(true);
							}
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
				if (handModeBtn.isSelected()) {
					for (JRadioButton bt : autoBtns) {
						bt.setSelected(false);
						bt.setEnabled(false);
					}
					for (JRadioButton bt : startBtns) {
						bt.setEnabled(true);
					}
					for (JRadioButton bt : stopBtns) {
						bt.setEnabled(true);
					}
				} else if (autoModeBtn.isSelected()) {
					for (JRadioButton bt : autoBtns) {
						bt.setEnabled(true);
					}
					for (JRadioButton bt : startBtns) {
						bt.setSelected(false);
						bt.setEnabled(false);
					}
					for (JRadioButton bt : stopBtns) {
						bt.setSelected(false);
						bt.setEnabled(false);
					}
				}
				cardLayout.show(dashPanel, DEVICE);
			}

			private void updateProjectPanelInfo(SoProject project) {
				IndexPanel.this.projectId = project.getId();
				IndexPanel.this.project = project;
				instance.getProjectWorkingMode(project.getId());

				// make a pause
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				instance.getProjectCalcIChg(project.getId());

				projectNameField.setText(project.getProjectName());
				locationField.setText(LocationLoader.getInstance().getLocationFullName(project.getLocationId()));
				cabField.setText(project.getCapability() + "/t");
				standField.setText(Consts.EMISES.type(project.getEmissionStandards()).typeName());

				standDichageField.setText("");
				solarEnergyPowerField.setText("");

				cardLayout.show(dashPanel, PROJECT);
			}
		});

		deviceTree.add(devTreePopmenu);
		deviceTree.addMouseListener(new MouseAdapter() {
			// final TreePath visiblePath = new
			// TreePath(((DefaultTreeModel)deviceTree.getModel()).getPathToRoot(null));
			public void mousePressed(MouseEvent event) {
				// int selRow = deviceTree.getRowForLocation(event.getX(), event.getY());
				TreePath selPath = deviceTree.getPathForLocation(event.getX(), event.getY());
				if (selPath != null) {
					if (((event.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
							&& (deviceTree.getSelectionCount() > 0)) {

						Object lp = selPath.getLastPathComponent();
						if (lp instanceof DefaultMutableTreeNode) {
							DefaultMutableTreeNode n = (DefaultMutableTreeNode) lp;
							Object uo = n.getUserObject();
							if (uo instanceof TreeAddr) {
								TreeAddr t = (TreeAddr) uo;
								AddrType addrType = t.getAddrType();
								switch (addrType) {
								case PROJECT:
									// addDevToProj.setEnabled(true);
									rmDevFromProj.setEnabled(false);
									showmenu(event.getX(), event.getY());
									break;
								case DEVICE:
									// addDevToProj.setEnabled(false);
									rmDevFromProj.setEnabled(true);
									String key = ((SoDevices) t.getValue()).getDevNo();
									rmDevFromProj.setActionCommand(key);
									popSelPath = selPath;
									showmenu(event.getX(), event.getY());
									break;
								default:
									break;
								}

							}
						}
					}
				}
			}
		});

		createDashpanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectTreePanel, dashPanel);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(5);
		add(splitPane, BorderLayout.CENTER);
		
		//将动画面板加入观察者队列中
		instance.addObserver(animationPanel);
	}

	/**
	 * 更新运行状态信息
	 * 
	 * @param soRunningData
	 */
	public void updateDeviceRunningPanelData(SoRunningData soRunningData) {

		Date breakTime = soRunningData.getBreakTime();// 上次故障时间
		Long safeTime = soRunningData.getSafeTime();// 安全运行时间
		String stat = soRunningData.getStat();
		try {
			int status = Integer.parseInt(stat,16);
			if ((status & 8) == 8) {
				correctBtn.setIcon(ButtonUI.createImageIcon("buttons/rb.gif", ""));
				breakBtn.setIcon(ButtonUI.createImageIcon("buttons/rbrs.gif", ""));
			} else {
				correctBtn.setIcon(ButtonUI.createImageIcon("buttons/rbs.gif", ""));
				breakBtn.setIcon(ButtonUI.createImageIcon("buttons/rb.gif", ""));
			}

			if ((status & 32768) == 32768) {
				solarBoardBtn.setIcon(ButtonUI.createImageIcon("buttons/rbrs.gif", ""));
			} else {
				solarBoardBtn.setIcon(ButtonUI.createImageIcon("buttons/rbs.gif", ""));
			}
			if ((status & 16384) == 16384) {
				batteryBtn.setIcon(ButtonUI.createImageIcon("buttons/rbrs.gif", ""));
			} else {
				batteryBtn.setIcon(ButtonUI.createImageIcon("buttons/rbs.gif", ""));
			}
			if ((status & 8192) == 8192) {
				fanOperationBtn.setIcon(ButtonUI.createImageIcon("buttons/rbrs.gif", ""));
			} else {
				fanOperationBtn.setIcon(ButtonUI.createImageIcon("buttons/rbs.gif", ""));
			}
			if ((status & 4096) == 4096) {
				pumpOperationBtn.setIcon(ButtonUI.createImageIcon("buttons/rbrs.gif", ""));
			} else {
				pumpOperationBtn.setIcon(ButtonUI.createImageIcon("buttons/rbs.gif", ""));
			}
			if(breakTime!=null)
				lastBreakTimeField.setText(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(breakTime));
			String formatSecond = formatSecond(safeTime);
			safeDaysField.setText(formatSecond);
		} catch (Exception e) {
			e.printStackTrace();
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
				case ConnectAPI.DEVICES_RUNNINGDATA_RESPONSE:
					List<SoRunningData> runningDatas = JsonUtilTool.fromJson(ret.getRet(),
							new TypeReference<List<SoRunningData>>() {
							});
					if (runningDatas.size() > 0) {
						updateDeviceRunningPanelData(runningDatas.get(0));
					}
					break;
				case ConnectAPI.DEVICES_ADD_RESPONSE:
					SoDevices devices = JsonUtilTool.fromJson(ret.getRet(), SoDevices.class);
					selectedNode.add(new DefaultMutableTreeNode(
							new TreeAddr(AddrType.DEVICE, devices.getId().toString(), devices, true)));
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							IndexPanel.this.deviceTree.updateUI();
						}
					});
				case ConnectAPI.WORKING_MODE_UPDATE_RESPONSE:
				case ConnectAPI.DEVICES_UPDATE_RESPONSE:
				case ConnectAPI.DEVICES_DEL_RESPONSE:
					SoAbtAuth soAbt = JsonUtilTool.fromJson(ret.getRet(), SoAbtAuth.class);
					JOptionPane.showMessageDialog(this, soAbt.getMsg());
					if (popSelPath != null && code == ConnectAPI.DEVICES_DEL_RESPONSE) {
						DefaultTreeModel dm = (DefaultTreeModel) IndexPanel.this.deviceTree.getModel();
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) popSelPath
								.getLastPathComponent();
						dm.removeNodeFromParent(selectedNode);
						popSelPath = null;
					}
					break;
				case ConnectAPI.PROJECT_CALC_PCHG_RESPONSE:
					SoProject project = JsonUtilTool.fromJson(ret.getRet(), SoProject.class);
					String projectTotalPChg = project.getProjectTotalPChg();
					
					try {
						float ichg = Long.parseLong(projectTotalPChg)/1000.0f;
						solarEnergyPowerField.setText(String.format("%.3f ", ichg)+"度");
					} catch (Exception e) {
					}
					
					break;
				case ConnectAPI.PROJECT_DEVICES_CTRL_RESPONSE:
					List<SoDevices> deviceList = JsonUtilTool.fromJson(ret.getRet(),
							new TypeReference<List<SoDevices>>() {
							});
					StringBuilder sb = new StringBuilder("");
					for (SoDevices soDevices : deviceList) {
						int retCode = soDevices.getRetCode();
						if (retCode == 1) {
							sb.append(soDevices.getDevNo()).append(",");
						}
					}
					if (sb.length() > 0) {
						JOptionPane.showMessageDialog(this, "设备" + sb.substring(0, sb.length() - 1) + "未连接");
					} else {
						JOptionPane.showMessageDialog(this, "操做成功");
					}
					break;
				case ConnectAPI.GET_WORKING_MODE_RESPONSE:
					SoProjectWorkingMode workingMode = JsonUtilTool.fromJson(ret.getRet(), SoProjectWorkingMode.class);
					IndexPanel.this.workingMode = workingMode;
					if (workingMode != null) {
						Method[] methods = SoProjectWorkingMode.class.getMethods();
						for (Method method : methods) {
							String mn = method.getName();
							if (mn.startsWith("getH_")) {
								String h = mn.replaceAll("getH_", "");
								try {
									Object v = method.invoke(workingMode);
									JCheckBox hCheckBox = workingTimeCheckBoxs.get(Integer.parseInt(h));
									Short f = (Short) v;
									if (f == null)
										f = (short) 0;
									hCheckBox.setSelected(f == 1);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						for (JCheckBox jCheckBox : workingTimeCheckBoxs) {
							jCheckBox.setSelected(false);
						}
					}
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 将秒数转换成天具体的天时分秒 比如172800S转换成2天0时0分0秒
	 * 
	 * @param second
	 * @return
	 */
	public String formatSecond(Object second) {
		String timeStr = "0秒";
		if (second != null) {
			Long s = (Long) second;
			String format;
			Object[] array;
			Integer days = (int) (s / (60 * 60 * 24));
			Integer hours = (int) (s / (60 * 60) - days * 24);
			Integer minutes = (int) (s / 60 - hours * 60 - days * 24 * 60);
			Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60 - days * 24 * 60 * 60);
			if (days > 0) {
				format = "%1$,d天%2$,d时%3$,d分%4$,d秒";
				array = new Object[] { days, hours, minutes, seconds };
			} else if (hours > 0) {
				format = "%1$,d时%2$,d分%3$,d秒";
				array = new Object[] { hours, minutes, seconds };
			} else if (minutes > 0) {
				format = "%1$,d分%2$,d秒";
				array = new Object[] { minutes, seconds };
			} else {
				format = "%1$,d秒";
				array = new Object[] { seconds };
			}
			timeStr = String.format(format, array);
		}
		return timeStr;
	}
}
