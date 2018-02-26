package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.solar.common.context.Consts;
import com.solar.common.context.Consts.AddrType;
import com.solar.common.util.LocationLoader;
import com.solar.entity.SoDevices;
import com.solar.entity.SoProject;
import com.solar.gui.component.model.TreeAddr;
import com.solar.gui.module.working.BasePanel;
import com.solar.gui.module.working.chart.RealTimeChart;
import com.solar.gui.module.working.chart.RunningLineChart;
import com.solar.timer.RunningDataCron;

@SuppressWarnings("serial")
public class RunningReportPanel extends BasePanel {
	private DefaultMutableTreeNode selectedNode = null;
	private SoDevices device = null;
	RunningDataCron runningDataCron = new RunningDataCron();

	public JPanel menuTree() {
		JPanel projectTreePanel = createTree(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				Object lastPathComponent = path.getLastPathComponent();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
				RunningReportPanel.this.selectedNode = node;
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
						break;
					}
				} else {
					RunningReportPanel.this.selectedNode = null;
				}
			}

			private void updateDevicePanelInfo(SoDevices device) {
				RunningReportPanel.this.device = device;
			}

			private void updateProjectPanelInfo(SoProject project) {
				 
			}
		});
		
		return projectTreePanel;
	}
	
	public JTabbedPane createReportPanel() {
		JTabbedPane jTabbedPane = new JTabbedPane();
		JPanel sumPowerPanel = RunningLineChart.createDemoPanel();
		jTabbedPane.add("太阳能发电量", sumPowerPanel);
		
		RealTimeChart elecPanel = new RealTimeChart("电力剩余", "时间", "数值");
		jTabbedPane.add("电池剩余电量",elecPanel);
		(new Thread(elecPanel)).start();
		
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
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuTree(), jPanel);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(5);
		add(splitPane, BorderLayout.CENTER);
		
		add(splitPane, BorderLayout.CENTER);
	}

}
