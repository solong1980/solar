package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.alibaba.fastjson.TypeReference;
import com.solar.client.JsonUtilTool;
import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ActionType;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts;
import com.solar.common.context.Consts.AuditResult;
import com.solar.common.context.RoleType;
import com.solar.common.util.LocationLoader;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoPage;
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
		queryAccount();
		auditPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (auditPanel.getSelectedIndex() == 1) {
					queryAccountFind();
				}
				if (auditPanel.getSelectedIndex() == 0) {
					queryAccount();
				}
			}
		});
		return auditPanel;
	}

	private void queryAccountFind() {
		SoAccountFind soAccountFind = new SoAccountFind();
		ObservableMedia.getInstance().accountFindQuery(soAccountFind);
	}

	private void queryAccount() {
		SoAccount soAccount = new SoAccount();
		soAccount.setStatus(AuditResult.WAIT_FOR_AUDIT.getStatus());
		SoPage<SoAccount, List<SoAccount>> page = new SoPage<>(soAccount);
		ObservableMedia.getInstance().accountQuery(page);
	}

	public AccountAuditPanel() {
		super();
		//JPanel projectPanel = createTree();
		setLayout(new BorderLayout());
		add(createToolBar(), BorderLayout.PAGE_START);
		//add(projectPanel, BorderLayout.WEST);
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
				// create confirm dialog
				ret = JOptionPane.showConfirmDialog(parent, "确认通过", "注册审核", JOptionPane.OK_CANCEL_OPTION);
				if (ret == 0) {
					// send for check type with projects return
					// create panel to choose project
					// send agree
					instance.checkAccountProject(id);
					// instance.regiestAgree(id);
				}
				break;
			case REGIEST_ADUIT_REJECT:
				// create confirm dialog
				ret = JOptionPane.showConfirmDialog(parent, "确认不通过", "注册审核", JOptionPane.OK_CANCEL_OPTION);
				if (ret == 0) {
					instance.regiestReject(id);
				}
				break;
			default:
				break;
			}
		}
	}

	public void projectAuditGUI(SoAccount account) {
		List<JCheckBox> checkBoxs = new ArrayList<>();

		class CheckBoxRenderer implements TableCellRenderer {
			private JCheckBox button;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value == null)
					return null;

				int size = checkBoxs.size();
				if (size < row + 1) {
					for (int i = 0; i < row + 1 - size; i++) {
						checkBoxs.add(new JCheckBox());
					}
				}
				button = checkBoxs.get(row);
				button.setText(value.toString());
				button.setHorizontalAlignment((int) 0.1f);

				return (Component) button;
			}
		}
		class CheckBoxEditor extends DefaultCellEditor implements ItemListener {
			private JCheckBox button;
			private Object value;

			public CheckBoxEditor(JCheckBox checkBox) {
				super(checkBox);
			}

			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				if (value == null)
					return null;
				button = checkBoxs.get(row);
				button.addItemListener(this);
				this.value = value;
				button.setHorizontalAlignment((int) 0.1f);
				return (Component) button;
			}

			public Object getCellEditorValue() {
				button.removeItemListener(this);
				return this.value;
			}

			public void itemStateChanged(ItemEvent e) {
				super.fireEditingStopped();
			}
		}
		// create table
		List<SoProject> projects = account.getProjects();

		final String[] names = { "ID", "项目名称", "类型", "位置", "街道", "处理能力", "维护员", "联系方式", "创建时间" };
		Object[][] data = new Object[projects.size()][9];

		for (int i = 0; i < projects.size(); i++) {
			SoProject project = projects.get(i);
			data[i][0] = project.getId();
			data[i][1] = project.getProjectName();
			data[i][2] = Consts.ProjectType.type(project.getType()).projectName();
			String locationId = project.getLocationId();
			String locationFullName = LocationLoader.getInstance().getLocationFullName(locationId);
			data[i][3] = locationFullName;
			data[i][4] = project.getStreet();
			data[i][5] = project.getCapability() + "t";
			data[i][6] = project.getWorkerName();
			data[i][7] = project.getWorkerPhone();
			data[i][8] = project.getCreateTime();
		}

		DefaultTableModel dataModel = new DefaultTableModel(data, names) {
			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return true;
				return false;
			}
		};
		// Create the table
		JTable projectTable = new JTable(dataModel) {
			public String getToolTipText(MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				int col = columnAtPoint(e.getPoint());
				String tiptextString = null;
				if (row > -1 && col > -1) {
					Object value = getValueAt(row, col);
					if (null != value && !"".equals(value))
						tiptextString = value.toString();// 悬浮显示单元格内容
				}
				return tiptextString;
			}
		};

		final int tableFirstColumn = 0;
		final JTableHeader tableHeader = projectTable.getTableHeader();
		final JCheckBox selectBox = new JCheckBox(dataModel.getColumnName(tableFirstColumn));
		// selectBox.setSelected(true);
		tableHeader.setDefaultRenderer(new TableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				String valueStr = (String) value;
				JLabel label = new JLabel(valueStr);
				label.setHorizontalAlignment(JLabel.CENTER);
				selectBox.setHorizontalAlignment(JLabel.CENTER);
				selectBox.setBorderPainted(true);
				JComponent component = (column == tableFirstColumn) ? selectBox : label;
				component.setForeground(tableHeader.getForeground());
				component.setBackground(tableHeader.getBackground());
				component.setFont(tableHeader.getFont());
				component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
				return component;

			}
		});
		tableHeader.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					int selectColumn = tableHeader.columnAtPoint(e.getPoint());
					if (selectColumn == tableFirstColumn) {
						boolean value = !selectBox.isSelected();
						selectBox.setSelected(value);
						// dataModel.selectAll(value);
						for (JCheckBox cb : checkBoxs) {
							cb.setSelected(value);
						}
						tableHeader.repaint();
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});

		projectTable.getColumn("ID").setCellEditor(new DefaultCellEditor(new JCheckBox()));
		projectTable.getColumnModel().getColumn(0).setCellEditor(new CheckBoxEditor(new JCheckBox()));
		projectTable.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());
		JComponent[] message = new JComponent[2];
		JScrollPane scrollPane = new JScrollPane(projectTable);
		scrollPane.setMinimumSize(new Dimension(1000, 300));
		scrollPane.setPreferredSize(new Dimension(1000, 300));
		scrollPane.setSize(1000, 200);
		message[0] = scrollPane;

		String[] options = { "确认", "取消" };

		int result = JOptionPane.showOptionDialog(this, message, "选择项目(审核)", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

		switch (result) {
		case 0: // yes
			List<SoProject> selProjects = new ArrayList<>();
			for (int i = 0; i < checkBoxs.size(); i++) {
				if (checkBoxs.get(i).isSelected()) {
					SoProject selProj = projects.get(i);
					selProjects.add(selProj);
				}
			}
			if (selProjects.size() == 0) {
				JOptionPane.showMessageDialog(this, "请选择项目", "未选项目", JOptionPane.WARNING_MESSAGE);
			} else {
				account.setProjects(selProjects);
				ObservableMedia.getInstance().regiestAgree(account);
			}
			break;
		case 1: // no
			break;
		default:
			break;
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

					Object[][] data = new Object[accountFinds.size()][9];
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
				case ConnectAPI.ACCOUNT_AUDIT_QUERY_RESPONSE:
					SoPage<SoAccount, List<SoAccount>> accountPage = JsonUtilTool.fromJson(ret.getRet(),
							new TypeReference<SoPage<SoAccount, List<SoAccount>>>() {
							});

					List<SoAccount> accounts = accountPage.getT();
					data = null;
					data = new Object[accounts.size()][10];
					for (int i = 0; i < accounts.size(); i++) {
						data[i] = new Object[10];
						data[i][0] = accounts.get(i).getId();
						data[i][1] = accounts.get(i).getAccount();
						data[i][2] = accounts.get(i).getName();
						data[i][3] = accounts.get(i).getPhone();
						data[i][4] = accounts.get(i).getEmail();

						int type = accounts.get(i).getType();
						RoleType roleType = RoleType.roleType(type);
						String typeName = roleType.typeName();
						data[i][5] = typeName;

						String locationIds = accounts.get(i).getLocationIds();
						if (locationIds != null && !locationIds.trim().isEmpty()) {
							String[] split = locationIds.split(",");
							// get location name
							StringBuilder ser = new StringBuilder();
							for (String locationId : split) {
								String locationFullName = LocationLoader.getInstance().getLocationFullName(locationId);
								ser.append(locationFullName).append(",");
							}
							data[i][6] = ser.subSequence(0, ser.length() - 1).toString();
						} else {
							data[i][6] = "";
						}

						int applyStatus = accounts.get(i).getStatus();
						AuditResult auditResult = AuditResult.status(applyStatus);
						String auditName = auditResult.resultName();
						data[i][7] = auditName;

						data[i][8] = accounts.get(i).getCreateTime();
						Long[] optItems = new Long[] { accounts.get(i).getId(), new Long(applyStatus) };
						data[i][9] = optItems;

					}
					regiestAuditPanel.updateData(data);
					break;
				case ConnectAPI.ACCOUNT_PROJECT_CHECK_RESPONSE:
					SoAccount account = JsonUtilTool.fromJson(ret.getRet(), SoAccount.class);
					projectAuditGUI(account);
					// create table project choose panel;
					// id,name, position
					// JOptionPane.showMessageDialog(this, account.getMsg());
					break;
				case ConnectAPI.ACCOUNT_AUDIT_RESPONSE:
					account = JsonUtilTool.fromJson(ret.getRet(), SoAccount.class);
					JOptionPane.showMessageDialog(this, account.getMsg());
					queryAccount();
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
