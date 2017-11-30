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
public class WorkerInfoPanel extends BasePanel {

	public JPanel createEditor() {
		//
		JLabel titleLable = new JLabel(getBoldHTML("维护人员信息更改"), JLabel.CENTER);
		JLabel workerLabel = new JLabel(getBoldHTML("运维人员姓名"));
		JLabel contactLabel = new JLabel(getBoldHTML("运维人员联系方式"));
		JLabel projectNameLabel = new JLabel(getBoldHTML("项目名称"));
		JLabel addrLabel = new JLabel(getBoldHTML("项目地址"));
		JLabel projectTypeLabel = new JLabel(getBoldHTML("项目类别"));

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
		editorPanel.add(workerLabel, gbc);
		gbc.gridy++;
		editorPanel.add(contactLabel, gbc);
		gbc.gridy++;
		editorPanel.add(projectNameLabel, gbc);
		gbc.gridy++;
		editorPanel.add(addrLabel, gbc);
		gbc.gridy++;
		editorPanel.add(projectTypeLabel, gbc);

		JTextField workerNameField = new JTextField("", 30);
		JTextField workerContactField = new JTextField("", 30);
		MultiComboBox projectField = MultiComboBox.build();
		MultiComboBox addrField = MultiComboBox.build();
		MultiComboBox projectTypeField = MultiComboBox.build();

		gbc.gridx++;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		editorPanel.add(workerNameField, gbc);
		gbc.gridy++;
		editorPanel.add(workerContactField, gbc);
		gbc.gridy++;
		editorPanel.add(projectField, gbc);
		gbc.gridy++;
		editorPanel.add(addrField, gbc);
		gbc.gridy++;
		editorPanel.add(projectTypeField, gbc);

		gbc.gridx++;
		int tmp = gbc.gridy;
		gbc.gridy = 4;
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

	public WorkerInfoPanel() {
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

class WorkerAction extends AbstractAction {
	private static final long serialVersionUID = -6048630218852730717L;
	private ActionType operate;
	private JComponent parent;

	protected WorkerAction(ActionType operate, JComponent parent) {
		super("AdaAction");
		this.operate = operate;
	}

	public void actionPerformed(ActionEvent e) {
		ObservableMedia instance = ObservableMedia.getInstance();
	}
}