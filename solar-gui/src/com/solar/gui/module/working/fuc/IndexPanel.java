package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

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

import com.solar.gui.component.BezierAnimationPanel;
import com.solar.gui.module.working.BasePanel;

/**
 * 首页
 */
@SuppressWarnings("serial")
public class IndexPanel extends BasePanel {

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
		dashPanel.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(1, 1, 1, 1);
		northPanel.add(createGPSPanel(), BorderLayout.WEST);
		gbc.gridx++;
		northPanel.add(createStatePanel(), BorderLayout.CENTER);

		dashPanel.add(northPanel, BorderLayout.NORTH);
		dashPanel.add(createInfoPanel(), BorderLayout.CENTER);
		dashPanel.add(createCtrlPanel(), BorderLayout.EAST);

		return dashPanel;
	}

	public JPanel createCtrlPanel() {
		JPanel ctrlPanel = new JPanel();
		ctrlPanel.setBorder(BorderFactory.createTitledBorder("控制面板"));
		ctrlPanel.setLayout(new BorderLayout());

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

		ctrlPanel.add(warnningPanel, BorderLayout.NORTH);

		JPanel schedulePanel = new JPanel();
		schedulePanel.setLayout(new BorderLayout());
		
		JPanel checkPanel = new JPanel(new GridLayout(12, 2));
		for (int i = 0; i < 24; i++) {
			//0:00-1:00
			JCheckBox box = new JCheckBox(i+":00-"+(i+1)+":00");
			checkPanel.add(box);
		}
		JScrollPane scheduleCheckBoxScrollPane = new JScrollPane(checkPanel);
		schedulePanel.add(new JLabel("定时运行配置"),BorderLayout.NORTH);
		schedulePanel.add(scheduleCheckBoxScrollPane,BorderLayout.CENTER);
		JButton updateRunMode = new JButton("设定运行模式");
		schedulePanel.add(updateRunMode,BorderLayout.SOUTH);
		ctrlPanel.add(schedulePanel, BorderLayout.CENTER);

		JPanel runCtrlPanel = new JPanel();
		runCtrlPanel.setLayout(new GridBagLayout());
		runCtrlPanel.setBorder(BorderFactory.createTitledBorder("紧急启停"));
		gbc = new GridBagConstraints();
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

		ctrlPanel.add(runCtrlPanel, BorderLayout.SOUTH);

		return ctrlPanel;
	}

	public JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("运行信息"));

		BezierAnimationPanel animationPanel = new BezierAnimationPanel();
		infoPanel.setLayout(new BorderLayout());

		infoPanel.add(animationPanel, BorderLayout.CENTER);
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

//		gbc.gridx++;
//		gbc.gridy = 0;
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gpsPanel.add(new JButton("地图"), gbc);

		return gpsPanel;
	}

	public IndexPanel() {
		super(new BorderLayout(5, 5));

		JPanel projectPanel = createTree();
		JPanel dashPanel = createDashpanel();
		// add(projectPanel, BorderLayout.WEST);
		//dashPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectPanel, dashPanel);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(5);
		add(splitPane, BorderLayout.CENTER);
	}

}
