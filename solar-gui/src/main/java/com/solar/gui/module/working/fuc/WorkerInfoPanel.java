package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.alibaba.fastjson.TypeReference;
import com.solar.client.JsonUtilTool;
import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ActionType;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.Consts.AuditResult;
import com.solar.common.context.RoleType;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountLocation;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.gui.component.checkable.multi.CheckBoxTreeNode;
import com.solar.gui.component.formate.JFieldBuilder;
import com.solar.gui.component.formate.JTextFieldLimit;
import com.solar.gui.component.model.TreeAddr;
import com.solar.gui.component.tree.TreeUI;
import com.solar.gui.module.working.BasePanel;

@SuppressWarnings("serial")
public class WorkerInfoPanel extends BasePanel implements Observer {

	private static final int INITIAL_ROWHEIGHT = 33;
	SoAccount account = null;

	JLabel msgLabel = new JLabel("校验结果:");

	JTextField nameField = new JTextField("", 30);
	JTextField phoneField = new JTextField("", 30);
	JTextField emailField = new JTextField("", 30);
	JPanel editorPanel;
	JPanel multiAddressPanel;
	JPanel multiSelProjectPanel;
	JPanel btnPanel;
	TreeUI treeUI = new TreeUI();

	public JPanel createEditForm() {

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(2, 2));
		editorPanel = createEditPanel();
		multiAddressPanel = treeUI.createMultiAddressTree("运维注册地址", null);
		multiSelProjectPanel = treeUI.createMultiSelProjectTree("运维管理工程列表", null);
		btnPanel = createBtnPanel();

		formPanel.add(editorPanel);
		formPanel.add(multiAddressPanel);
		formPanel.add(multiSelProjectPanel);
		formPanel.add(btnPanel);
		return formPanel;
	}

	private JPanel createBtnPanel() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		GridBagConstraints gpc = new GridBagConstraints();
		gpc.insets = new Insets(1, 15, 1, 15);
		gpc.gridy = 0;
		gpc.gridx = 0;
		JButton submitBtn = new JButton("提交");
		submitBtn.setAction(new WorkerAction("提交", ActionType.WORKER_UPDATE_SUBMIT, this));
		JButton cancelBtn = new JButton("取消");
		cancelBtn.setAction(new WorkerAction("取消", ActionType.WORKER_CANCEL, this));
		jPanel.add(submitBtn, gpc);
		gpc.gridx = 1;
		jPanel.add(cancelBtn, gpc);
		return jPanel;
	}

	private JPanel createEditPanel() {
		JPanel editorPanel = new JPanel(new GridBagLayout());
		JLabel titleLable = new JLabel(getBoldHTML("维护人员信息更改"), JLabel.CENTER);
		JLabel nameLabel = new JLabel(getBoldHTML("姓名"));
		JLabel phoneLabel = new JLabel(getBoldHTML("联系方式"));
		JLabel emailLabel = new JLabel(getBoldHTML("邮箱"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		editorPanel.add(titleLable, gbc);
		gbc.gridy = 1;
		editorPanel.add(msgLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 2;
		editorPanel.add(nameLabel, gbc);
		gbc.gridx = 1;
		editorPanel.add(nameField, gbc);
		nameField.setDocument(new JTextFieldLimit(60));
		gbc.gridy++;
		gbc.gridx = 0;
		editorPanel.add(phoneLabel, gbc);
		gbc.gridx = 1;
		editorPanel.add(phoneField, gbc);
		phoneField.setDocument(new JTextFieldLimit(15));

		gbc.gridy++;
		gbc.gridx = 0;
		editorPanel.add(emailLabel, gbc);
		gbc.gridx = 1;
		editorPanel.add(emailField, gbc);
		emailField.setDocument(new JTextFieldLimit(50));
		return editorPanel;
	}

	DefaultTableModel dataModel;
	List<JCheckBox> checkBoxs = new ArrayList<>();
	JTable workerTable;
	int rmRow = -1;

	public JMenuBar createToolBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu editorMenu = (JMenu) menuBar.add(new JMenu("运维管理 &"));
		createMenuItem(editorMenu, "刷新", new WorkerAction(ActionType.WORKER_REFRESH, this));
		// createMenuItem(editorMenu, "禁用", new
		// WorkerAction(ActionType.WORKER_FORBIDDEN, this));
		return menuBar;
	}

	public void updateData(final Object[][] data) {
		checkBoxs.clear();
		workerTable.clearSelection();
		workerTable.setRowMargin(1);
		workerTable.setRowSelectionAllowed(true);
		workerTable.setCellEditor(null);
		workerTable.setEditingColumn(-1);
		workerTable.setEditingRow(-1);
		dataModel.getDataVector().clear();
		for (Object[] objects : data) {
			dataModel.addRow(objects);
		}
		dataModel.fireTableDataChanged();
	}

	public JScrollPane createWorkerListPanel(List<SoAccount> workers) {

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

		class OptBtnRenderer implements TableCellRenderer {
			private JButton updataBtn;
			// private JButton delBtn;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value == null)
					return null;

				JPanel p = new JPanel();
				p.setLayout(new GridLayout(1, 2));
				updataBtn = new JButton("修改");
				// delBtn = new JButton("删除");
				p.add(updataBtn);
				// p.add(delBtn);
				return (Component) p;
			}
		}

		WorkerAction updateAction = new WorkerAction(ActionType.WORKER_UPDATE, this);
		// WorkerAction deleteAction = new WorkerAction(ActionType.WORKER_FORBIDDEN,
		// this);

		class OptBtnEditor extends DefaultCellEditor implements ItemListener {
			private JButton updataBtn;
			// private JButton delBtn;
			private Object value;

			public OptBtnEditor(JCheckBox checkBox) {
				super(checkBox);
			}

			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				if (value == null)
					return null;
				JPanel p = new JPanel();
				p.setLayout(new GridLayout(1, 1));
				updataBtn = createTableButton("修改", updateAction);
				// delBtn = createTableButton("删除", deleteAction);
				String rowstr = Integer.toString(row);
				updataBtn.setActionCommand(rowstr);
				// delBtn.setActionCommand(rowstr);
				p.add(updataBtn);
				// p.add(delBtn);
				this.value = value;

				updataBtn.addItemListener(this);
				// delBtn.addItemListener(this);
				return (Component) p;
			}

			public Object getCellEditorValue() {
				updataBtn.removeItemListener(this);
				// delBtn.removeItemListener(this);
				return this.value;
			}

			public void itemStateChanged(ItemEvent e) {
				super.fireEditingStopped();
			}
		}

		// create table
		final String[] names = { "ID", "姓名", "手机", "邮箱", "类型", "创建时间", "操作" };
		Object[][] data = new Object[workers.size()][10];

		for (int i = 0; i < workers.size(); i++) {
			SoAccount account = workers.get(i);
			data[i][0] = account.getId();
			data[i][1] = account.getName();
			data[i][2] = account.getPhone();
			data[i][3] = account.getEmail();
			int type = account.getType();
			data[i][4] = RoleType.roleType(type).typeName();
			data[i][5] = account.getCreateTime();
			data[i][6] = account.getId();
		}

		dataModel = new DefaultTableModel(data, names) {
			public boolean isCellEditable(int row, int col) {
				if (col == 0 || col == 6)
					return true;
				return false;
			}
		};
		// Create the table
		workerTable = new JTable(dataModel) {
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
		workerTable.setFont(new Font("Menu.font", Font.PLAIN, 15));
		workerTable.setRowHeight(INITIAL_ROWHEIGHT);

		final int tableFirstColumn = 0;
		final JTableHeader tableHeader = workerTable.getTableHeader();
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

		workerTable.getColumn("ID").setCellEditor(new DefaultCellEditor(new JCheckBox()));
		workerTable.getColumnModel().getColumn(0).setCellEditor(new CheckBoxEditor(new JCheckBox()));
		workerTable.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());

		workerTable.getColumn("操作").setCellEditor(new DefaultCellEditor(new JCheckBox()));
		workerTable.getColumnModel().getColumn(6).setCellEditor(new OptBtnEditor(new JCheckBox()));
		workerTable.getColumnModel().getColumn(6).setCellRenderer(new OptBtnRenderer());

		// 设置按钮render/edit

		JScrollPane scrollPane = new JScrollPane(workerTable);
		return scrollPane;
	}

	private Integer pageNum = 1;
	JTextField totalField = new JTextField(3);
	JTextField pageNumField = new JTextField(3);

	public JPanel createListPanel() {
		// prefer form panel
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());

		JPanel formPanel = new JPanel();
		// create list panel
		// create table
		JScrollPane tablePanel = createWorkerListPanel(Collections.emptyList());
		// page panel
		listPanel.add(createToolBar(), BorderLayout.PAGE_START);
		listPanel.add(formPanel, BorderLayout.NORTH);
		listPanel.add(tablePanel, BorderLayout.CENTER);

		// page panel
		JPanel pagionationPanel = new JPanel();
		pagionationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton preBtn = new JButton("上一页");

		if (getPageNum() == 1)
			preBtn.setEnabled(false);

		preBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer pageNum = getPageNum();
				if ((pageNum - 1) < 1) {
					preBtn.setEnabled(false);
					return;
				} else if ((pageNum - 1) == 1) {
					preBtn.setEnabled(false);
					queryWorker(pageNum - 1);
				} else {
					queryWorker(pageNum - 1);
				}
			}
		});
		JButton nextBtn = new JButton("下一页");
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer pageNum = getPageNum();
				queryWorker(pageNum + 1);
				preBtn.setEnabled(true);
			}
		});
		JLabel gongLabel = new JLabel("共");
		totalField.setEnabled(false);
		JLabel tiaoLabel = new JLabel("条");

		pagionationPanel.add(preBtn);
		pagionationPanel.add(nextBtn);

		pageNumField.setEditable(false);
		pageNumField.setText("第" + getPageNum() + "页");

		pagionationPanel.add(pageNumField);
		pagionationPanel.add(Box.createHorizontalGlue());
		pagionationPanel.add(gongLabel);
		pagionationPanel.add(totalField);
		pagionationPanel.add(tiaoLabel);
		listPanel.add(pagionationPanel, BorderLayout.SOUTH);

		return listPanel;
	}

	private int getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	CardLayout cardLayout = new CardLayout();

	public WorkerInfoPanel() {
		super();
		setLayout(cardLayout);
		// list
		JPanel workerListPanel = createListPanel();
		JPanel editorPanel = createEditForm();
		cardLayout.addLayoutComponent(workerListPanel, "list");
		cardLayout.addLayoutComponent(editorPanel, "edit");
		add(workerListPanel);
		add(editorPanel);

		queryWorker(1);
	}

	private void queryWorker(Integer pageNum) {
		setPageNum(pageNum);
		// send query
		SoPage<SoAccount, List<SoAccount>> soPage = new SoPage<>();
		SoAccount soAccount = new SoAccount();
		soAccount.setStatus(AuditResult.AGREE.getStatus());
		soPage.setC(soAccount);
		soPage.setPageNum(pageNum);
		ObservableMedia.getInstance().accountQuery(soPage);
	}

	public boolean isvalid() {
		try {
			JFieldBuilder.noEmpty(msgLabel, "姓名必填", nameField);
			JFieldBuilder.noEmpty(msgLabel, "电话号码必填", phoneField);
			JFieldBuilder.isPhone(msgLabel, "电话号码格式错误", phoneField);
			JFieldBuilder.noEmpty(msgLabel, "邮箱必填", emailField);
			JFieldBuilder.isEmail(msgLabel, "邮箱格式错误", emailField);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	class WorkerAction extends AbstractAction {
		private static final long serialVersionUID = -6048630218852730717L;
		private ActionType operate;
		private JComponent parent;

		protected WorkerAction(ActionType operate, JComponent parent) {
			super("AdaAction");
			this.operate = operate;
			this.parent = parent;
		}

		public WorkerAction(String text, ActionType operate, JComponent parent) {
			super(text);
			this.operate = operate;
			this.parent = parent;
		}

		public void actionPerformed(ActionEvent e) {
			switch (operate) {
			case WORKER_REFRESH:
				queryWorker(getPageNum());
				break;
			case WORKER_UPDATE:
				// query by id
				String command = e.getActionCommand();
				Integer row = Integer.parseInt(command);
				Object valueAtOne = dataModel.getValueAt(row, 0);
				Long id = Long.parseLong(valueAtOne.toString());
				ObservableMedia.getInstance().accountSelect(id);
				break;
			case WORKER_CANCEL:
				account = null;
				nameField.setText("");
				phoneField.setText("");
				emailField.setText("");
				cardLayout.show(parent, "list");
				break;
			case WORKER_UPDATE_SUBMIT:
				if (!isvalid()) {
					return;
				}
				id = account.getId();
				account.setName(nameField.getText());
				account.setPhone(phoneField.getText());
				account.setEmail(emailField.getText());
				List<CheckBoxTreeNode> multiSelectedPojectsNodes = treeUI.getMultiSelectedPojectsNodes();
				List<SoProject> projects = new ArrayList<>();
				for (CheckBoxTreeNode checkBoxTreeNode : multiSelectedPojectsNodes) {
					Object userObject = checkBoxTreeNode.getUserObject();
					TreeAddr p = (TreeAddr) userObject;
					SoProject value = (SoProject) p.getValue();
					projects.add(value);
				}
				account.setProjects(projects);

				List<CheckBoxTreeNode> multiSelectedAddressNodes = treeUI.getMultiSelectedAddressNodes();
				List<SoAccountLocation> accountLocations = new ArrayList<>();
				for (CheckBoxTreeNode checkBoxTreeNode : multiSelectedAddressNodes) {
					Object userObject = checkBoxTreeNode.getUserObject();
					TreeAddr p = (TreeAddr) userObject;
					String key = p.getKey();
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setAccountId(id);
					accountLocation.setLocationId(key);
					accountLocations.add(accountLocation);
				}
				account.setLocations(accountLocations);
				ObservableMedia.getInstance().accountUpdate(account);
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
				case ConnectAPI.ACCOUNT_QUERY_RESPONSE:
					SoPage<SoAccount, List<SoAccount>> page = JsonUtilTool.fromJson(ret.getRet(),
							new TypeReference<SoPage<SoAccount, List<SoAccount>>>() {
							});

					List<SoAccount> accounts = page.getT();
					Object[][] data = new Object[accounts.size()][7];
					for (int i = 0; i < accounts.size(); i++) {
						SoAccount account = accounts.get(i);
						data[i][0] = account.getId();
						data[i][1] = account.getName();
						data[i][2] = account.getPhone();
						data[i][3] = account.getEmail();
						int type = account.getType();
						data[i][4] = RoleType.roleType(type).typeName();
						data[i][5] = account.getCreateTime();
						data[i][6] = account.getId();
					}
					updateData(data);

					Integer totel = page.getTotal();
					Integer pageNum = page.getPageNum();
					totalField.setText(totel.toString());
					pageNumField.setText("第" + pageNum + "页");
					break;
				case ConnectAPI.ACCOUNT_UPDATE_RESPONSE:
					account = JsonUtilTool.fromJson(ret.getRet(), SoAccount.class);
					JOptionPane.showMessageDialog(this, account.getMsg());

					cardLayout.show(this, "list");
					queryWorker(getPageNum());
					break;
				case ConnectAPI.ACCOUNT_SELECT_RESPONSE:
					account = JsonUtilTool.fromJson(ret.getRet(), SoAccount.class);
					cardLayout.show(this, "edit");
					nameField.setText(account.getName());
					phoneField.setText(account.getPhone());
					emailField.setText(account.getEmail());

					List<SoAccountLocation> locations = account.getLocations();
					Set<String> locationIds = new HashSet<>();
					for (SoAccountLocation soAccountLocation : locations) {
						locationIds.add(soAccountLocation.getLocationId());
					}

					List<SoProject> projects = account.getProjects();
					Set<Long> projectIds = new HashSet<>();
					for (SoProject soProject : projects) {
						projectIds.add(soProject.getId());
					}
					try {
						EventQueue.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								treeUI.updateAddressNodeSelect(locationIds);
							}
						});
						treeUI.updateProjectNodeSelect(projectIds);
					} catch (InvocationTargetException | InterruptedException e) {
						e.printStackTrace();
					}

					break;
				default:
					break;
				}
			}
		}
	}
}
