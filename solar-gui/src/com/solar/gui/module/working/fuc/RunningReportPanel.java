package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.solar.gui.module.working.BasePanel;

@SuppressWarnings("serial")
public class RunningReportPanel extends BasePanel {

	public JTabbedPane createReportPanel() {
		JTabbedPane jTabbedPane = new JTabbedPane();
		jTabbedPane.add("太阳能发电量", new JPanel());
		jTabbedPane.add("电池剩余电量", new JPanel());
		jTabbedPane.add("风机消耗电能", new JPanel());
		jTabbedPane.add("水泵消耗电能", new JPanel());
		jTabbedPane.add("电池温度", new JPanel());
		return jTabbedPane;
	}

	public RunningReportPanel() {
		super();
		setLayout(new BorderLayout());

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());
		jPanel.add(createReportPanel(), BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.add(new JButton("导出图表"));
		btnPanel.add(new JButton("返回"));
		jPanel.add(btnPanel, BorderLayout.SOUTH);

		add(createTree(), BorderLayout.WEST);
		add(jPanel, BorderLayout.CENTER);
	}

}
