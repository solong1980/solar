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
import javax.swing.JTabbedPane;
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
public class AccountAuditPanel extends BasePanel implements Observer {

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

	public JTabbedPane createEditor() {
		JTabbedPane auditPanel = new  JTabbedPane();
		auditPanel.add("注册信息", new JPanel());
		auditPanel.add("找回信息", new JPanel());
		return auditPanel;
	}

	public AccountAuditPanel() {
		super();
		JPanel projectPanel = createTree();
		setLayout(new BorderLayout());
		add(createToolBar(), BorderLayout.PAGE_START);
		add(projectPanel, BorderLayout.WEST);
		add(createEditor(), BorderLayout.CENTER);
	}

	public JMenuBar createToolBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu editorMenu = (JMenu) menuBar.add(new JMenu("信息审核 &"));
		createMenuItem(editorMenu, "刷新", new AuditAction(ActionType.PROJECT_NEW, this));
		return menuBar;
	}

	class AuditAction extends AbstractAction {
		private static final long serialVersionUID = -6048630218852730717L;
		private ActionType operate;
		private JComponent parent;

		protected AuditAction(ActionType operate, JComponent parent) {
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
						JOptionPane.showMessageDialog(AccountAuditPanel.this, soAbt.getMsg());
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
