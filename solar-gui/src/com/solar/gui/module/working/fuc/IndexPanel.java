package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.solar.client.SoRet;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts.AddrType;
import com.solar.common.util.JsonUtilTool;
import com.solar.entity.SoDevices;
import com.solar.entity.SoRunningData;
import com.solar.gui.component.BezierAnimationPanel;
import com.solar.gui.component.model.TreeAddr;
import com.solar.gui.module.working.BasePanel;

/**
 * 首页
 */
@SuppressWarnings("serial")
public class IndexPanel extends BasePanel implements Observer {

	JSplitPane splitPane = null;

	JScrollPane projectPanel;

	// JPanel dashPanel;
	// // 地理位置
	// JPanel gpsPanel;
	// // 运行状态
	// JPanel statePanel;
	// // 具体数据
	// JPanel infoPanel;

	public JPanel createDashpanel() {
		JPanel dashPanel = new JPanel();
		dashPanel.setLayout(cardLayout);

		JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new BorderLayout());
		projectPanel.add(createGPSPanel(), BorderLayout.NORTH);

		JPanel schedulePanel = new JPanel();
		schedulePanel.setLayout(new BorderLayout());
		JPanel checkPanel = new JPanel(new GridLayout(12, 2));
		for (int i = 0; i < 24; i++) {
			// 0:00-1:00
			JCheckBox box = new JCheckBox(i + ":00-" + (i + 1) + ":00");
			checkPanel.add(box);
		}
		JScrollPane scheduleCheckBoxScrollPane = new JScrollPane(checkPanel);
		schedulePanel.add(new JLabel("定时运行配置"), BorderLayout.NORTH);
		schedulePanel.add(scheduleCheckBoxScrollPane, BorderLayout.CENTER);
		projectPanel.add(schedulePanel, BorderLayout.CENTER);
		JButton updateRunMode = new JButton("设定运行模式");
		schedulePanel.add(updateRunMode, BorderLayout.SOUTH);

		JPanel empty = new JPanel();
		JPanel devicePanel = createInfoPanel();

		dashPanel.add(createInfoPanel());
		dashPanel.add(projectPanel);
		dashPanel.add(new JPanel());
		
		cardLayout.addLayoutComponent(projectPanel, "project");
		cardLayout.addLayoutComponent(empty, "empty");
		cardLayout.addLayoutComponent(devicePanel, "device");
		return dashPanel;
	}

	public JPanel createCtrlPanel() {
		JPanel ctrlPanel = new JPanel();
		ctrlPanel.setBorder(BorderFactory.createTitledBorder("控制面板"));
		ctrlPanel.setLayout(new BorderLayout());
		JPanel runCtrlPanel = new JPanel();
		runCtrlPanel.setLayout(new GridBagLayout());
		runCtrlPanel.setBorder(BorderFactory.createTitledBorder("紧急启停"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(1, 1, 1, 1);
		runCtrlPanel.add(new JLabel("风机 "), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("备用风机 "), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("水泵 "), gbc);
		gbc.gridy++;
		runCtrlPanel.add(new JLabel("备用水泵 "), gbc);

		gbc.gridx++;
		gbc.gridy = 0;

		ButtonGroup[] buttonGroups = new ButtonGroup[4];
		JRadioButton[] startBtns = new JRadioButton[4];
		JRadioButton[] stopBtns = new JRadioButton[4];
		for (int i = 0; i < 4; i++) {
			buttonGroups[i] = new ButtonGroup();
			startBtns[i] = new JRadioButton("启动");
			stopBtns[i] = new JRadioButton("停止");

			buttonGroups[i].add(startBtns[i]);
			buttonGroups[i].add(stopBtns[i]);
		}

		runCtrlPanel.add(startBtns[0], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[1], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[2], gbc);
		gbc.gridy++;
		runCtrlPanel.add(startBtns[3], gbc);

		gbc.gridx++;
		gbc.gridy = 0;
		runCtrlPanel.add(stopBtns[0], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[1], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[2], gbc);
		gbc.gridy++;
		runCtrlPanel.add(stopBtns[3], gbc);

		ctrlPanel.add(runCtrlPanel, BorderLayout.CENTER);

		return ctrlPanel;
	}

	CardLayout cardLayout = new CardLayout();
	BezierAnimationPanel animationPanel;

	public JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("运行信息"));

		JPanel statePanel = createStatePanel();
		JPanel warnningPanel = new JPanel();
		warnningPanel.setLayout(new GridBagLayout());
		warnningPanel.setBorder(BorderFactory.createTitledBorder("故障报警信息"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		// gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(1, 1, 1, 1);
		warnningPanel.add(new JLabel("太阳能板故障 "), gbc);
		gbc.gridy++;
		warnningPanel.add(new JLabel("电池故障 "), gbc);
		gbc.gridy++;
		warnningPanel.add(new JLabel("风机运行故障 "), gbc);
		gbc.gridy++;
		warnningPanel.add(new JLabel("水泵运行故障 "), gbc);

		JPanel np = new JPanel(new GridLayout(1, 2));
		np.add(statePanel);
		np.add(warnningPanel);

		animationPanel = new BezierAnimationPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(np, BorderLayout.NORTH);
		infoPanel.add(animationPanel, BorderLayout.CENTER);
		infoPanel.add(createCtrlPanel(), BorderLayout.EAST);
		return infoPanel;
	}

	public JPanel createStatePanel() {
		JPanel statePanel = new JPanel();
		statePanel.setLayout(new GridBagLayout());
		statePanel.setBorder(BorderFactory.createTitledBorder("运行状态"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		statePanel.add(new JLabel("运行正常:"), gbc);
		gbc.gridy++;
		statePanel.add(new JLabel("故障报警:"), gbc);
		gbc.gridy++;
		statePanel.add(new JLabel("连续无故障运行时长："), gbc);
		gbc.gridy++;
		statePanel.add(new JLabel("上次故障发生日期："), gbc);

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		statePanel.add(new JTextField("", 20), gbc);
		gbc.gridy++;
		statePanel.add(new JTextField("", 20), gbc);
		gbc.gridy++;
		statePanel.add(new JTextField("天  小时   分", 20), gbc);
		gbc.gridy++;
		statePanel.add(new JTextField("年  月   日   小时    分", 20), gbc);
		return statePanel;
	}

	public JPanel createGPSPanel() {
		JPanel gpsPanel = new JPanel();
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
		gpsPanel.add(new JLabel(" 排放标准："), gbc);
		gbc.gridy++;
		gpsPanel.add(new JLabel("达标排放量："), gbc);
		gbc.gridy++;
		gpsPanel.add(new JLabel("太阳能发电电能："), gbc);

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gpsPanel.add(new JTextField("", 20), gbc);
		gbc.gridy++;
		gpsPanel.add(new JTextField("", 20), gbc);
		gbc.gridy++;
		gpsPanel.add(new JTextField("", 20), gbc);
		gbc.gridy++;
		gpsPanel.add(new JTextField("", 20), gbc);
		gbc.gridy++;
		gpsPanel.add(new JTextField("", 20), gbc);
		gbc.gridy++;
		gpsPanel.add(new JTextField("", 20), gbc);

		// gbc.gridx++;
		// gbc.gridy = 0;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gpsPanel.add(new JButton("地图"), gbc);

		return gpsPanel;
	}

	public IndexPanel() {
		super(new BorderLayout(5, 5));

		JPanel projectTreePanel = createTree(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				Object lastPathComponent = path.getLastPathComponent();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
				Object userObject = node.getUserObject();
				if (userObject instanceof TreeAddr) {
					TreeAddr ta = (TreeAddr) userObject;
					AddrType addrType = ta.getAddrType();
					String id = ta.getKey();
					Object value = ta.getValue();
					switch (addrType) {
					case PROJECT:
						if (node.getChildCount() > 0) {
							return;
						}
						System.out.println("PROJECT " + id);
						// Load device
						// node.add
						List<SoDevices> devices = dataClient.getDeviceIn(Long.parseLong(id));
						if (devices != null) {
							for (SoDevices device : devices) {
								node.add(new DefaultMutableTreeNode(
										new TreeAddr(AddrType.DEVICE, device.getId().toString(), device, true)));
							}
						}
						break;
					case DEVICE:
						// monitor device
						System.out.println("DEVICE " + id);
						SoDevices device = (SoDevices) value;
						String devNo = device.getDevNo();
						instance.getRunningData(devNo);

						// set
						// on click,get device info,
						// query data from db include running model,runing info
						// query data
						// query running

						break;
					default:
						break;
					}
				}
			}
		});
		JPanel dashPanel = createDashpanel();
		// add(projectPanel, BorderLayout.WEST);
		// dashPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectTreePanel, dashPanel);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(5);
		add(splitPane, BorderLayout.CENTER);
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
					SoRunningData runningData = JsonUtilTool.fromJson(ret.getRet(), SoRunningData.class);
					// update dash panel
					// animationPanel
					break;
				default:
					break;
				}
			}
		}

	}

}
