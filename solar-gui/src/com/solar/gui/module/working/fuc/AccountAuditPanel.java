package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
import com.solar.entity.SoAbt;
import com.solar.entity.SoProject;
import com.solar.gui.component.AddressTreeField;
import com.solar.gui.component.DeviceTreeField;
import com.solar.gui.component.FindbackAuditPanel;
import com.solar.gui.component.RegiestAuditPanel;
import com.solar.gui.module.working.BasePanel;

@SuppressWarnings("serial")
public class AccountAuditPanel extends BasePanel implements Observer {

	private SoProject soProject;

	RegiestAuditPanel regiestAuditPanel;
	FindbackAuditPanel findbackAuditPanel;

	AuditAction regiestAuditAgreeAction;
	AuditAction regiestAuditRejectAction;
	AuditAction accountFindbackAgreeAction;
	AuditAction accountFindbackRejectAction;

	public JTabbedPane createEditor() {
		JTabbedPane auditPanel = new JTabbedPane();

		regiestAuditAgreeAction = new AuditAction(ActionType.REGIEST_ADUIT_AGREE, this);
		regiestAuditRejectAction = new AuditAction(ActionType.REGIEST_ADUIT_REJECT, this);
		accountFindbackAgreeAction = new AuditAction(ActionType.ACCOUNT_FINDBACK_ADUIT_AGREE, this);
		accountFindbackRejectAction = new AuditAction(ActionType.ACCOUNT_FINDBACK_ADUIT_REJECT, this);

		regiestAuditPanel = new RegiestAuditPanel(regiestAuditAgreeAction, regiestAuditRejectAction);
		findbackAuditPanel = new FindbackAuditPanel(/* accountFindbackAgreeAction, accountFindbackRejectAction */);

		auditPanel.add("注册信息", regiestAuditPanel);
		auditPanel.add("找回信息", findbackAuditPanel);
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

	public class AuditAction extends AbstractAction {
		private static final long serialVersionUID = -6048630218852730717L;
		private ActionType operate;
		private JComponent parent;

		protected AuditAction(ActionType operate, JComponent parent) {
			super("AdaAction");
			this.operate = operate;
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("Action--------------------" + e.getActionCommand());
			ObservableMedia instance = ObservableMedia.getInstance();
			switch (operate) {
			case ACCOUNT_FINDBACK_ADUIT_AGREE:
			case ACCOUNT_FINDBACK_ADUIT_REJECT:
			case REGIEST_ADUIT_AGREE:
			case REGIEST_ADUIT_REJECT:
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
			// case ConnectAPI.ACCOUNT_ADD_NEXTPAGE_RESPONSE:
			// break;
			// case ConnectAPI.ACCOUNT_FINDBACK_NEXTPAGE_RESPONSE:
			// break;
			// case ConnectAPI.ACCOUNT_ADD_ADUIT_RESPONSE:
			// break;
			// case ConnectAPI.ACCOUNT_FINDBACK_ADUIT_RESPONSE:
			// break;
			// case ConnectAPI.PROJECT_REFRESH_RESPONSE:
			// break;
			default:
				break;
			}
		}
	}
}
