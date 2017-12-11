package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.solar.client.JsonUtilTool;
import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ActionType;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts.AuditResult;
import com.solar.common.context.RoleType;
import com.solar.common.util.LocationLoader;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoProject;
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
		findbackAuditPanel = new FindbackAuditPanel(accountFindbackAgreeAction, accountFindbackRejectAction);

		auditPanel.add("注册信息", regiestAuditPanel);
		auditPanel.add("找回信息", findbackAuditPanel);
		auditPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (auditPanel.getSelectedIndex() == 1) {
					queryAccountFind();
				}
				System.out.println(1111);
			}
		});
		return auditPanel;
	}

	private void queryAccountFind() {
		SoAccountFind soAccountFind = new SoAccountFind();
		ObservableMedia.getInstance().accountFindQuery(soAccountFind);
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
			String actionCommand = e.getActionCommand();

			Long id = Long.parseLong(actionCommand);

			ObservableMedia instance = ObservableMedia.getInstance();
			switch (operate) {
			case ACCOUNT_FINDBACK_ADUIT_AGREE:
				// create confirm dialog
				int ret = JOptionPane.showConfirmDialog(parent, "确认通过", "找回审核", JOptionPane.OK_CANCEL_OPTION);
				if (ret == 0) {
					instance.accountFindAgree(id);
				}
				break;
			case ACCOUNT_FINDBACK_ADUIT_REJECT:
				// create confirm dialog
				ret = JOptionPane.showConfirmDialog(parent, "确认不通过", "找回审核", JOptionPane.OK_CANCEL_OPTION);
				if (ret == 0) {
					instance.accountFindReject(id);
				}
				break;
			case REGIEST_ADUIT_AGREE:
			case REGIEST_ADUIT_REJECT:
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
				switch (code) {
				case ConnectAPI.ACCOUNT_FINDBACK_QUERY_RESPONSE:
					List<SoAccountFind> accountFinds = JsonUtilTool.fromJsonArray(ret.getRet(), SoAccountFind.class);

					final Object[][] data = new Object[accountFinds.size()][9];
					for (int i = 0; i < accountFinds.size(); i++) {
						data[i] = new Object[9];
						data[i][0] = accountFinds.get(i).getId();
						data[i][1] = accountFinds.get(i).getName();
						data[i][2] = accountFinds.get(i).getOldPhone();
						data[i][3] = accountFinds.get(i).getPhone();

						int type = accountFinds.get(i).getType();
						RoleType roleType = RoleType.roleType(type);
						String typeName = roleType.typeName();
						int applyStatus = accountFinds.get(i).getStatus();
						AuditResult auditResult = AuditResult.status(applyStatus);
						String auditName = auditResult.resultName();

						data[i][4] = typeName;
						String locationIds = accountFinds.get(i).getLocationIds();
						if (locationIds != null && !locationIds.trim().isEmpty()) {
							String[] split = locationIds.split(",");
							// get location name
							StringBuilder ser = new StringBuilder();
							for (String locationId : split) {
								String locationFullName = LocationLoader.getInstance().getLocationFullName(locationId);
								ser.append(locationFullName).append(",");
							}
							data[i][5] = ser.subSequence(0, ser.length() - 1).toString();
						} else {
							data[i][5] = "";
						}
						data[i][6] = auditName;
						data[i][7] = accountFinds.get(i).getCreateTime();
						Long[] optItems = new Long[] { accountFinds.get(i).getId(), new Long(applyStatus) };
						data[i][8] = optItems;

					}
					findbackAuditPanel.updateData(data);
					break;
				case ConnectAPI.ACCOUNT_FINDBACK_AUDIT_RESPONSE:
					SoAccountFind accountFind = JsonUtilTool.fromJson(ret.getRet(), SoAccountFind.class);
					JOptionPane.showMessageDialog(this, accountFind.getMsg());
					queryAccountFind();
					break;
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
}
