package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.solar.client.JsonUtilTool;
import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ActionType;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.entity.SoAbt;
import com.solar.entity.SoDevConfig;
import com.solar.entity.SoProject;
import com.solar.gui.component.AddressTreeField;
import com.solar.gui.component.DeviceTreeField;
import com.solar.gui.component.model.TreeAddr;
import com.solar.gui.module.working.BasePanel;

@SuppressWarnings("serial")
public class ProjectDataPanel extends BasePanel implements Observer {

	private SoProject soProject;
	private JTextField nameField;
	private JComboBox<String> projectTypeField;
	private AddressTreeField addressField;
	private JTextField streetField;
	private JComboBox<String> emissionStandardsField;
	private DeviceTreeField equipmentField;
	private JComboBox<String> capabilityField;

	private JTextField workerNameField;
	private JTextField workerContactField;

	public JPanel createEditor() {
		//
		JLabel titleLable = new JLabel(getBoldHTML("项目信息更改"), JLabel.CENTER);
		JLabel nameLabel = new JLabel(getBoldHTML("项目名称"));

		JLabel projectTypeLabel = new JLabel(getBoldHTML("项目类别"));

		JLabel addrLabel = new JLabel(getBoldHTML("项目地址"));
		JLabel streetLabel = new JLabel(getBoldHTML("街道"));

		JLabel capabilityLabel = new JLabel(getBoldHTML("设计处理量"));
		JLabel equipmentLabel = new JLabel(getBoldHTML("设备列表"));
		JLabel emissionStandardsLabel = new JLabel(getBoldHTML("排放标准"));
		JLabel workerNameLabel = new JLabel(getBoldHTML("运维人员姓名"));
		JLabel workerContactLabel = new JLabel(getBoldHTML("运维人员联系方式"));

		JPanel editorPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		editorPanel.add(titleLable, gbc);

		gbc.gridwidth = 1;
		gbc.gridy++;
		editorPanel.add(nameLabel, gbc);
		gbc.gridy++;
		editorPanel.add(projectTypeLabel, gbc);
		gbc.gridy++;
		editorPanel.add(addrLabel, gbc);
		gbc.gridy++;
		editorPanel.add(streetLabel, gbc);
		gbc.gridy++;
		editorPanel.add(capabilityLabel, gbc);
		gbc.gridy++;
		editorPanel.add(equipmentLabel, gbc);
		gbc.gridy++;
		editorPanel.add(emissionStandardsLabel, gbc);
		gbc.gridy++;
		editorPanel.add(workerNameLabel, gbc);
		gbc.gridy++;
		editorPanel.add(workerContactLabel, gbc);

		nameField = new JTextField("");
		// 设计处理量
		projectTypeField = new JComboBox<>();
		String[] projectTypes = new String[] { "太阳能污水处理系统", "智能运维系统" };
		for (int i = 0; i < projectTypes.length; i++) {
			projectTypeField.addItem(projectTypes[i]);
		}

		addressField = AddressTreeField.build();
		streetField = new JTextField();
		// 设计处理量
		capabilityField = new JComboBox<>();
		String[] cabs = new String[] { "5D/T", "10D/T", "20D/T", "30D/T", "50D/T", "80D/T", "100D/T" };
		for (int i = 0; i < cabs.length; i++) {
			capabilityField.addItem(cabs[i]);
		}

		equipmentField = DeviceTreeField.build();

		// 排放标准
		String[] emises = new String[] { "一级A", "一级B" };
		// MultiComboBox emissionStandardsField = MultiComboBox.build();
		emissionStandardsField = new JComboBox<>();
		for (int i = 0; i < emises.length; i++) {
			emissionStandardsField.addItem(emises[i]);
		}

		workerNameField = new JTextField("", 30);
		workerContactField = new JTextField("", 30);

		gbc.gridx++;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		editorPanel.add(nameField, gbc);
		gbc.gridy++;
		editorPanel.add(projectTypeField, gbc);

		gbc.gridy++;
		editorPanel.add(addressField, gbc);
		gbc.gridy++;
		editorPanel.add(streetField, gbc);
		gbc.gridy++;
		editorPanel.add(capabilityField, gbc);
		gbc.gridy++;
		editorPanel.add(equipmentField, gbc);
		gbc.gridy++;
		editorPanel.add(emissionStandardsField, gbc);
		gbc.gridy++;
		editorPanel.add(workerNameField, gbc);
		gbc.gridy++;
		editorPanel.add(workerContactField, gbc);

		gbc.gridx++;
		int tmp = gbc.gridy;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		editorPanel.add(new JButton("地图"), gbc);

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		GridBagConstraints gpc = new GridBagConstraints();
		gpc.insets = new Insets(1, 15, 1, 15);
		gpc.gridy = 0;
		gpc.gridx = 0;
		JButton submitBtn = new JButton("提交");
		submitBtn.addActionListener(new EditorAction(ActionType.PROJECT_NEW_SUBMIT, this));
		jPanel.add(submitBtn, gpc);
		gpc.gridx = 1;
		jPanel.add(new JButton("取消"), gpc);

		gbc.gridx = 0;
		gbc.gridy = tmp + 1;
		gbc.gridwidth = 2;
		editorPanel.add(jPanel, gbc);

		return editorPanel;
	}

	public ProjectDataPanel() {
		super();
		JPanel projectPanel = createTree();
		setLayout(new BorderLayout());
		add(createToolBar(), BorderLayout.PAGE_START);
		add(projectPanel, BorderLayout.WEST);
		add(createEditor(), BorderLayout.CENTER);
	}

	public JMenuBar createToolBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu editorMenu = (JMenu) menuBar.add(new JMenu("项目管理 &"));
		createMenuItem(editorMenu, "新增", new EditorAction(ActionType.PROJECT_NEW, this));
		createMenuItem(editorMenu, "修改", new EditorAction(ActionType.PROJECT_UPDATE, this));
		createMenuItem(editorMenu, "删除", new EditorAction(ActionType.PROJECT_DELETE, this));
		return menuBar;
	}

	class EditorAction extends AbstractAction {
		private static final long serialVersionUID = -6048630218852730717L;
		private ActionType operate;
		private JComponent parent;

		protected EditorAction(ActionType operate, JComponent parent) {
			super("AdaAction");
			this.operate = operate;
		}

		public void actionPerformed(ActionEvent e) {
			ObservableMedia instance = ObservableMedia.getInstance();
			switch (operate) {
			case PROJECT_NEW:
				nameField.setText("");
				projectTypeField.setSelectedItem(null);
				addressField.cleanSelected();
				streetField.setText("");
				emissionStandardsField.setSelectedItem(null);
				capabilityField.setSelectedItem(null);
				equipmentField.cleanSelected();
				workerNameField.setText("");
				workerContactField.setText("");
				soProject = new SoProject();
				break;
			case PROJECT_NEW_SUBMIT:
				String name = nameField.getText();
				int typeIndex = projectTypeField.getSelectedIndex();
				int projectType = Consts.PROJECT_TYPE[typeIndex];

				List<TreeAddr> selectedAddress = addressField.getSelectedKeys();
				if (selectedAddress == null || selectedAddress.isEmpty()) {

				}
				TreeAddr treeAddr = selectedAddress.get(0);
				String locationId = treeAddr.getKey();
				String street = streetField.getText();
				int emissionStandardIdx = emissionStandardsField.getSelectedIndex();
				int emissionStandard = Consts.EMISSION_STANDARDS[emissionStandardIdx];

				int capabilityIdx = capabilityField.getSelectedIndex();
				int cap = Consts.CAPS[capabilityIdx];

				List<SoDevConfig> devConfigs = equipmentField.getSelectedKeys();
				String workerName = workerNameField.getText();
				String workerPhone = workerContactField.getText();

				soProject = new SoProject();
				soProject.setProjectName(name);
				soProject.setType(projectType);
				soProject.setLocationId(locationId);
				soProject.setStreet(street);
				soProject.setEmissionStandards(emissionStandard);
				soProject.setCapability(cap);
				soProject.setDevConfiures(devConfigs);
				soProject.setWorkerName(workerName);
				soProject.setWorkerName(workerPhone);
				instance.saveProject(soProject);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		if (arg instanceof SoRet) {
			SoRet ret = (SoRet) arg;
			int code = ret.getCode();
			int status = ret.getStatus();
			if (status == 0) {
				SoAbt soAbt = JsonUtilTool.fromJson(ret.getRet(), SoAbt.class);
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(ProjectDataPanel.this, soAbt.getMsg());
					}
				});
			} else {
				JOptionPane.showMessageDialog(this, ret.getRet());
			}
			switch (code) {
			case ConnectAPI.PROJECT_ADD_RESPONSE:
				break;
			default:
				break;
			}
		}
	}
}
