package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class AdaWorkingPanel extends JPanel implements ActionListener {
	JTabbedPane tabbedpane;
	ButtonGroup group;
	JRadioButton top;
	JRadioButton bottom;
	JRadioButton left;
	JRadioButton right;

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

	/**
	 * TabbedPaneDemo Constructor
	 */
	public AdaWorkingPanel() {
		setLayout(new BorderLayout());

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

		// create tab
		// Tab panel
		tabbedpane = new JTabbedPane();
		add(tabbedpane, BorderLayout.CENTER);
		// 用户管理
		String name = " 用户管理";
		JLabel pix = new JLabel(name);
		UserManagerPanel managerPanel = new UserManagerPanel();
		// tabbedpane.add(name, pix);
		tabbedpane.add(name, managerPanel);

		// 配置管理
		name = "配置管理";
		WorkingModeManagerPanel workingModeManagerPanel = new WorkingModeManagerPanel();
		tabbedpane.add(name, workingModeManagerPanel);

		// 数据查询
		name = "数据查询";
		pix = new JLabel(name);
		tabbedpane.add(name, pix);

		// 升级管理
		name = "升级管理";
		UpgradeManagerPanel upgradeManagerPanel = new UpgradeManagerPanel();
		tabbedpane.add(name, upgradeManagerPanel);
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

}
