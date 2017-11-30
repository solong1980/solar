package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.solar.client.ObservableMedia;
import com.solar.common.context.ActionType;
import com.solar.gui.component.MultiComboBox;
import com.solar.gui.module.working.BasePanel;

@SuppressWarnings("serial")
public class ProjectDataPanel extends BasePanel {

	public JPanel createEditor() {
		//
		JLabel titleLable = new JLabel(getBoldHTML("项目信息更改"), JLabel.CENTER);
		JLabel nameLabel = new JLabel(getBoldHTML("项目名称"));
		JLabel addrLabel = new JLabel(getBoldHTML("项目地址"));
		JLabel equipmentLabel = new JLabel(getBoldHTML("设备列表"));
		JLabel capabilityLabel = new JLabel(getBoldHTML("设计处理量"));
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
		editorPanel.add(addrLabel, gbc);
		gbc.gridy++;
		editorPanel.add(equipmentLabel, gbc);
		gbc.gridy++;
		editorPanel.add(capabilityLabel, gbc);
		gbc.gridy++;
		editorPanel.add(emissionStandardsLabel, gbc);
		gbc.gridy++;
		editorPanel.add(workerNameLabel, gbc);
		gbc.gridy++;
		editorPanel.add(workerContactLabel, gbc);

		// admin,operator,cust_1,cust_2
		JTextField nameField = new JTextField("");
		MultiComboBox addrField = MultiComboBox.build();
		MultiComboBox equipmentField = MultiComboBox.build();
		MultiComboBox capabilityField = MultiComboBox.build();
		MultiComboBox emissionStandardsField = MultiComboBox.build();
		JTextField workerNameField = new JTextField("", 30);
		JTextField workerContactField = new JTextField("", 30);

		gbc.gridx++;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		editorPanel.add(nameField, gbc);
		gbc.gridy++;
		editorPanel.add(addrField, gbc);
		gbc.gridy++;
		editorPanel.add(equipmentField, gbc);
		gbc.gridy++;
		editorPanel.add(capabilityField, gbc);
		gbc.gridy++;
		editorPanel.add(emissionStandardsField, gbc);
		gbc.gridy++;
		editorPanel.add(workerNameField, gbc);
		gbc.gridy++;
		editorPanel.add(workerContactField, gbc);

		gbc.gridx++;
		int tmp = gbc.gridy;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		editorPanel.add(new JButton("地图"), gbc);

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		GridBagConstraints gpc = new GridBagConstraints();
		gpc.insets = new Insets(1, 15, 1, 15);
		gpc.gridy = 0;
		gpc.gridx = 0;
		jPanel.add(new JButton("提交"), gpc);
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
	}
}