package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
import com.solar.common.context.Consts;
import com.solar.common.context.Consts.AddrType;
import com.solar.common.context.Consts.ProjectType;
import com.solar.common.util.LocationLoader;
import com.solar.entity.SoDevConfig;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.gui.component.AddressTreeField;
import com.solar.gui.component.formate.JFieldBuilder;
import com.solar.gui.component.formate.JTextFieldLimit;
import com.solar.gui.component.model.TreeAddr;
import com.solar.gui.module.working.BasePanel;

@SuppressWarnings("serial")
public class ProjectDataPanel extends BasePanel implements Observer {

	final int INITIAL_ROWHEIGHT = 33;

	private SoProject soProject = null;
	JLabel titleLable;
	JLabel msgLabel = new JLabel("校验结果:");

	private JTextField nameField;
	private JComboBox<String> projectTypeField;
	private AddressTreeField addressField;
	private JTextField streetField;
	private JComboBox<String> emissionStandardsField;

	private JLabel equipmentField;

	private JComboBox<String> capabilityField;

	private JTextField workerNameField;
	private JTextField workerContactField;

	List<JCheckBox> workingTimeCheckBoxs = new ArrayList<>();

	public JPanel createEditor() {
		//
		titleLable = new JLabel(getBoldHTML("项目信息更改"), JLabel.CENTER);
		JLabel nameLabel = new JLabel(getBoldHTML("项目名称"));

		JLabel projectTypeLabel = new JLabel(getBoldHTML("项目类别"));

		JLabel addrLabel = new JLabel(getBoldHTML("项目地址"));
		JLabel streetLabel = new JLabel(getBoldHTML("街道"));

		JLabel capabilityLabel = new JLabel(getBoldHTML("设计处理量"));
		JLabel equipmentLabel = new JLabel(getBoldHTML("设备列表"));
		JLabel emissionStandardsLabel = new JLabel(getBoldHTML("排放标准"));
		JLabel workerNameLabel = new JLabel(getBoldHTML("运维人员姓名"));
		JLabel workerContactLabel = new JLabel(getBoldHTML("运维人员联系方式"));

		JPanel editPanel = new JPanel(new BorderLayout());

		JPanel projectInfoPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		projectInfoPanel.add(titleLable, gbc);

		gbc.gridy++;
		gbc.gridwidth = 2;
		projectInfoPanel.add(msgLabel, gbc);

		gbc.gridwidth = 1;
		gbc.gridy++;
		projectInfoPanel.add(nameLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(projectTypeLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(addrLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(streetLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(capabilityLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(equipmentLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(emissionStandardsLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(workerNameLabel, gbc);
		gbc.gridy++;
		projectInfoPanel.add(workerContactLabel, gbc);

		// "项目名称必填"
		nameField = new JTextField(30);
		nameField.setDocument(new JTextFieldLimit(150));
		nameField.requestFocus();

		// 设计处理量
		projectTypeField = new JComboBox<>();

		String[] projectTypes = new String[] { "太阳能污水处理系统", "智能运维系统" };
		for (int i = 0; i < projectTypes.length; i++) {
			projectTypeField.addItem(projectTypes[i]);
		}

		addressField = AddressTreeField.buildLeafOnly();
		streetField = new JTextField();
		streetField.setDocument(new JTextFieldLimit(512));

		// 设计处理量
		capabilityField = new JComboBox<>();
		String[] cabs = new String[] { "", "5D/T", "10D/T", "20D/T", "30D/T", "50D/T", "80D/T", "100D/T" };
		for (int i = 0; i < cabs.length; i++) {
			capabilityField.addItem(cabs[i]);
		}

		capabilityField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int idx = capabilityField.getSelectedIndex() - 1;
				if (idx < 0) {
					equipmentField.setText("");
					return;
				}
				StringBuffer s = new StringBuffer();
				for (int i = 0; i < Consts.devices.length; i++) {
					String devName = Consts.devices[i];
					int count = Consts.devCountOpts[i][idx];
					s.append(devName).append(":").append(count).append(" / ");
				}
				equipmentField.setText(s.toString().substring(0, s.length() - 3));
			}
		});
		equipmentField = new JLabel("");

		// 排放标准

		// MultiComboBox emissionStandardsField = MultiComboBox.build();
		emissionStandardsField = new JComboBox<>();
		for (int i = 0; i < Consts.emises.length; i++) {
			emissionStandardsField.addItem(Consts.emises[i]);
		}

		workerNameField = new JTextField("", 30);
		workerNameField.setDocument(new JTextFieldLimit(50));

		workerContactField = new JTextField("", 30);
		workerContactField.setDocument(new JTextFieldLimit(50));

		gbc.gridx++;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		projectInfoPanel.add(nameField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(projectTypeField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(addressField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(streetField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(capabilityField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(equipmentField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(emissionStandardsField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(workerNameField, gbc);
		gbc.gridy++;
		projectInfoPanel.add(workerContactField, gbc);

		// gbc.gridx++;
		// int tmp = gbc.gridy;
		// gbc.gridy = 3;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// editorPanel.add(new JButton("地图"), gbc);

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

		JButton cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new EditorAction(ActionType.PROJECT_CANCEL, this));
		jPanel.add(cancelBtn, gpc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		projectInfoPanel.add(jPanel, gbc);

		JPanel schedulePanel = new JPanel();
		schedulePanel.setLayout(new BorderLayout());
		JPanel checkPanel = new JPanel(new GridLayout(12, 4));

		for (int i = 0; i < 48; i++) {
			// 0:00-1:00
			// JCheckBox box = new JCheckBox(i + ":00-" + (i + 1) + ":00");
			JCheckBox box = new JCheckBox((30 * i / 60) + ":" + String.format("%02d", (30 * i % 60)) + "-"
					+ (30 * (i + 1) / 60) + ":" + String.format("%02d", (30 * (i + 1) % 60)));
			workingTimeCheckBoxs.add(box);
			checkPanel.add(box);
		}

		JScrollPane scheduleCheckBoxScrollPane = new JScrollPane(checkPanel);
		schedulePanel.add(new JLabel("项目运行模式设置", SwingConstants.CENTER), BorderLayout.NORTH);
		schedulePanel.add(scheduleCheckBoxScrollPane, BorderLayout.CENTER);

		JCheckBox chooseAllBox = new JCheckBox("全选");
		schedulePanel.add(chooseAllBox, BorderLayout.SOUTH);
		chooseAllBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox box : workingTimeCheckBoxs) {
					box.setSelected(chooseAllBox.isSelected());
				}
			}
		});
		editPanel.add(projectInfoPanel, BorderLayout.CENTER);
		editPanel.add(schedulePanel, BorderLayout.EAST);

		return editPanel;
	}

	DefaultTableModel dataModel;
	List<JCheckBox> checkBoxs = new ArrayList<>();
	JTable projectTable;

	int rmRow = -1;

	public void updateData(final Object[][] data) {
		checkBoxs.clear();
		projectTable.clearSelection();
		projectTable.setRowMargin(1);
		projectTable.setRowSelectionAllowed(true);
		projectTable.setCellEditor(null);
		projectTable.setEditingColumn(-1);
		projectTable.setEditingRow(-1);
		dataModel.getDataVector().clear();
		for (Object[] objects : data) {
			dataModel.addRow(objects);
		}
		dataModel.fireTableDataChanged();
	}

	public JScrollPane projectListTable(List<SoProject> projects) {
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
			private JButton delBtn;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (value == null)
					return null;

				JPanel p = new JPanel();
				p.setLayout(new GridLayout(1, 2));
				updataBtn = new JButton("修改");
				delBtn = new JButton("删除");
				// updataBtn.setHorizontalAlignment((int) 0.1f);
				// delBtn.setHorizontalAlignment((int) 0.1f);
				// updataBtn.setVerticalAlignment(SwingConstants.TOP);
				p.add(updataBtn);
				p.add(delBtn);
				return (Component) p;
			}
		}

		EditorAction updateAction = new EditorAction(ActionType.PROJECT_UPDATE, this);
		EditorAction deleteAction = new EditorAction(ActionType.PROJECT_DELETE, this);

		class OptBtnEditor extends DefaultCellEditor implements ItemListener {
			private JButton updataBtn;
			private JButton delBtn;
			private Object value;

			public OptBtnEditor(JCheckBox checkBox) {
				super(checkBox);
			}

			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				if (value == null)
					return null;
				JPanel p = new JPanel();
				p.setLayout(new GridLayout(1, 2));
				updataBtn = createTableButton("修改", updateAction);
				delBtn = createTableButton("删除", deleteAction);
				String rowstr = Integer.toString(row);
				updataBtn.setActionCommand(rowstr);
				/*
				 * delete button with id and row for delete row from table directly without back
				 * request
				 */
				delBtn.setActionCommand(rowstr);
				// updataBtn.setHorizontalAlignment((int) 0.1f);
				// delBtn.setHorizontalAlignment((int) 0.1f);
				p.add(updataBtn);
				p.add(delBtn);
				this.value = value;

				updataBtn.addItemListener(this);
				delBtn.addItemListener(this);
				return (Component) p;
			}

			public Object getCellEditorValue() {
				updataBtn.removeItemListener(this);
				delBtn.removeItemListener(this);
				return this.value;
			}

			// @Override
			// public boolean stopCellEditing() {
			// boolean selected = delBtn.isSelected();
			// return true;
			// }
			// return delegate.stopCellEditing();
			// }

			public void itemStateChanged(ItemEvent e) {
				super.fireEditingStopped();
			}
		}

		// create table
		final String[] names = { "ID", "项目名称", "类型", "位置", "街道", "处理能力", "维护员", "联系方式", "创建时间", "操作" };
		Object[][] data = new Object[projects.size()][10];

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
			data[i][8] = project.getId();
		}

		dataModel = new DefaultTableModel(data, names) {
			public boolean isCellEditable(int row, int col) {
				if (col == 0 || col == 9)
					return true;
				return false;
			}
		};
		// Create the table
		projectTable = new JTable(dataModel) {
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
		projectTable.setFont(new Font("Menu.font", Font.PLAIN, 15));
		projectTable.setRowHeight(INITIAL_ROWHEIGHT);

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

		projectTable.getColumn("操作").setCellEditor(new DefaultCellEditor(new JCheckBox()));
		projectTable.getColumnModel().getColumn(9).setCellEditor(new OptBtnEditor(new JCheckBox()));
		projectTable.getColumnModel().getColumn(9).setCellRenderer(new OptBtnRenderer());

		// 设置按钮render/edit

		JScrollPane scrollPane = new JScrollPane(projectTable);
		return scrollPane;
	}

	JTextField totalField = new JTextField(3);
	JTextField pageNumField = new JTextField(3);
	private Integer pageNum = 1;

	public JPanel createListPanel() {
		// prefer form panel
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());

		JPanel formPanel = new JPanel();
		// create list panel
		// create table
		JScrollPane tablePanel = projectListTable(Collections.emptyList());
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
					queryProjects(pageNum - 1);
				} else {
					queryProjects(pageNum - 1);
				}
			}
		});
		JButton nextBtn = new JButton("下一页");
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer pageNum = getPageNum();
				queryProjects(pageNum + 1);
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

		listPanel.add(createToolBar(), BorderLayout.PAGE_START);
		listPanel.add(formPanel, BorderLayout.NORTH);
		listPanel.add(tablePanel, BorderLayout.CENTER);
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

	public void queryProjects(Integer pageNum) {
		setPageNum(pageNum);
		// send query
		SoPage<SoProject, List<SoProject>> soPage = new SoPage<>();
		soPage.setC(new SoProject());
		soPage.setPageNum(pageNum);
		ObservableMedia.getInstance().queryProjects(soPage);
	}

	public ProjectDataPanel() {
		super();
		// set layout card layout
		setLayout(cardLayout);
		JPanel projectListPanel = createListPanel();
		JPanel projectEditPanel = createEditor();
		cardLayout.addLayoutComponent(projectListPanel, "list");
		cardLayout.addLayoutComponent(projectEditPanel, "edit");
		// JPanel projectPanel = createTree();
		add(projectListPanel);
		add(projectEditPanel);
		queryProjects(1);
	}

	public JMenuBar createToolBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu editorMenu = (JMenu) menuBar.add(new JMenu("项目管理 &"));
		createMenuItem(editorMenu, "新增", new EditorAction(ActionType.PROJECT_NEW, this));
		createMenuItem(editorMenu, "刷新", new EditorAction(ActionType.PROJECT_REFRESH, this));
		// createMenuItem(editorMenu, "修改", new EditorAction(ActionType.PROJECT_UPDATE,
		// this));
		// createMenuItem(editorMenu, "删除", new EditorAction(ActionType.PROJECT_DELETE,
		// this));
		return menuBar;
	}

	public boolean isvalid() {
		try {
			JFieldBuilder.noEmpty(msgLabel, "项目名称必填", nameField);
			JFieldBuilder.noneChoose(msgLabel, "未选择 项目类型", projectTypeField);
			JFieldBuilder.noEmpty(msgLabel, "未选项目地址", addressField.getSelectedKeys(), addressField);
			JFieldBuilder.noneChoose(msgLabel, "未选择 排放标准", emissionStandardsField);

			JFieldBuilder.noneChoose(msgLabel, "未选择 排放标准", capabilityField, 0);

			JFieldBuilder.noEmpty(msgLabel, "维护人员", workerNameField);
			JFieldBuilder.noEmpty(msgLabel, "联系方式", workerContactField);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	class EditorAction extends AbstractAction {
		private static final long serialVersionUID = -6048630218852730717L;
		private ActionType operate;
		private JComponent parent;

		protected EditorAction(ActionType operate, JComponent parent) {
			super("AdaAction");
			this.operate = operate;
			this.parent = parent;
		}

		public void actionPerformed(ActionEvent e) {
			ObservableMedia media = ObservableMedia.getInstance();
			switch (operate) {
			case PROJECT_DELETE:
				int ret = JOptionPane.showConfirmDialog(parent, "确认删除", "删除", JOptionPane.OK_CANCEL_OPTION);
				if (ret == 0) {
					String command = e.getActionCommand();
					rmRow = Integer.parseInt(command);
					Object vAtone = projectTable.getValueAt(rmRow, 0);
					Long id = Long.parseLong(vAtone.toString());
					media.deleteProject(id);
				}
				break;
			case PROJECT_UPDATE:
				String command = e.getActionCommand();
				Integer row = Integer.parseInt(command);
				Object vAtone = projectTable.getValueAt(row, 0);
				Long id = Long.parseLong(vAtone.toString());
				media.selectProject(id);
				break;
			case PROJECT_NEW:
				titleLable.setText("项目添加");
				nameField.setText("");
				projectTypeField.setSelectedItem(null);
				addressField.cleanSelected();
				streetField.setText("");
				emissionStandardsField.setSelectedItem(null);
				capabilityField.setSelectedItem(null);
				equipmentField.setText("");
				workerNameField.setText("");
				workerContactField.setText("");
				soProject = null;
				cardLayout.show(ProjectDataPanel.this, "edit");
				break;
			case PROJECT_REFRESH:
				ObservableMedia.getInstance().queryProjects(new SoPage<SoProject, List<SoProject>>());
				break;
			case PROJECT_CANCEL:
				soProject = null;
				nameField.setText("");
				projectTypeField.setSelectedItem(null);
				addressField.cleanSelected();
				streetField.setText("");
				emissionStandardsField.setSelectedItem(null);
				capabilityField.setSelectedItem(null);
				equipmentField.setText("");
				workerNameField.setText("");
				workerContactField.setText("");
				soProject = new SoProject();

				for (JCheckBox jCheckBox : workingTimeCheckBoxs) {
					jCheckBox.setSelected(false);
				}

				cardLayout.show(ProjectDataPanel.this, "list");
				break;
			case PROJECT_UPDATE_SUBMIT:
				if (soProject == null || soProject.getId() == null) {
					showWarningDailog("未选择项目", "警告");
					return;
				}

			case PROJECT_NEW_SUBMIT:
				if (isvalid()) {
					String name = nameField.getText();
					int typeIndex = projectTypeField.getSelectedIndex();
					int projectType = Consts.PROJECT_TYPE[typeIndex];
					List<TreeAddr> selectedAddress = addressField.getSelectedKeys();
					TreeAddr treeAddr = selectedAddress.get(0);
					String locationId = treeAddr.getKey();
					String street = streetField.getText();
					int emissionStandardIdx = emissionStandardsField.getSelectedIndex();
					int emissionStandard = Consts.EMISSION_STANDARDS[emissionStandardIdx];

					int capabilityIdx = capabilityField.getSelectedIndex();
					int cap = Consts.CAPS[capabilityIdx - 1];

					List<SoDevConfig> devConfigs = Collections.emptyList();

					String workerName = workerNameField.getText();
					String workerPhone = workerContactField.getText();
					// if update the soProject should not null
					if (soProject == null) {
						soProject = new SoProject();
					}

					soProject.setProjectName(name);
					soProject.setType(projectType);
					soProject.setLocationId(locationId);
					soProject.setStreet(street);
					soProject.setEmissionStandards(emissionStandard);
					soProject.setCapability(cap);
					soProject.setDevConfiures(devConfigs);
					soProject.setWorkerName(workerName);
					soProject.setWorkerPhone(workerPhone);
					SoProjectWorkingMode mode = soProject.getProjectWorkingMode();
					if (mode == null) {
						mode = new SoProjectWorkingMode();
						mode.setProjectId(soProject.getId());
						soProject.setProjectWorkingMode(mode);
					}

					for (int i = 0; i < workingTimeCheckBoxs.size(); i++) {
						JCheckBox hCheckBox = workingTimeCheckBoxs.get(i);
						Method method;
						try {
							method = SoProjectWorkingMode.class.getMethod("setH_" + i, Short.class);
							method.invoke(mode, hCheckBox.isSelected() ? (short) 1 : (short) 0);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
					}

					media.saveProject(soProject);
				} else {
					return;
				}

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
				case ConnectAPI.PROJECT_DELETE_RESPONSE:
					SoProject soAbt = JsonUtilTool.fromJson(ret.getRet(), SoProject.class);
					JOptionPane.showMessageDialog(ProjectDataPanel.this, soAbt.getMsg());
					dataModel.removeRow(rmRow);

					projectTable.setRowMargin(1);
					projectTable.setRowSelectionAllowed(true);
					projectTable.setCellEditor(null);
					projectTable.setEditingColumn(-1);
					projectTable.setEditingRow(-1);
					break;
				case ConnectAPI.PROJECT_ADD_RESPONSE:
				case ConnectAPI.PROJECT_UPDATE_RESPONSE:
					soAbt = JsonUtilTool.fromJson(ret.getRet(), SoProject.class);
					soProject = null;// 设置为返回的内容
					JOptionPane.showMessageDialog(ProjectDataPanel.this, soAbt.getMsg());
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							ObservableMedia.getInstance().queryProjects(new SoPage<SoProject, List<SoProject>>());
						}
					});
					break;
				case ConnectAPI.PROJECT_QUERY_RESPONSE:
					SoPage<SoProject, List<SoProject>> page = JsonUtilTool.fromJson(ret.getRet(),
							new TypeReference<SoPage<SoProject, List<SoProject>>>() {
							});
					// update table
					List<SoProject> t = page.getT();
					Object[][] data = new Object[t.size()][10];
					for (int i = 0; i < t.size(); i++) {
						SoProject project = t.get(i);
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
						data[i][9] = project.getId();
					}
					updateData(data);

					Integer totel = page.getTotal();
					Integer pageNum = page.getPageNum();
					totalField.setText(totel.toString());
					pageNumField.setText("第" + pageNum + "页");

					cardLayout.show(this, "list");
					break;
				case ConnectAPI.PROJECT_SELECT_RESPONSE:
					SoProject project = JsonUtilTool.fromJson(ret.getRet(), SoProject.class);
					this.soProject = project;

					titleLable.setText("项目更新");
					nameField.setText(project.getProjectName());

					int type = project.getType();
					ProjectType projectType = ProjectType.type(type);
					switch (projectType) {
					case SUN_POWER:
						projectTypeField.setSelectedIndex(0);
						break;
					case SMART:
						projectTypeField.setSelectedIndex(1);
						break;
					default:
						break;
					}

					String locationId = project.getLocationId();
					TreeAddr ta = new TreeAddr(AddrType.AREA, locationId,
							LocationLoader.getInstance().getLocationName(locationId));
					addressField.setSelectedKeys(new ArrayList<TreeAddr>(1) {
						{
							add(ta);
						}
					});
					addressField.setText(LocationLoader.getInstance().getLocationFullName(locationId));

					streetField.setText(project.getStreet());

					int emissionStandards = project.getEmissionStandards();
					emissionStandardsField.setSelectedIndex(emissionStandards == 10 ? 0 : 1);

					// 5, 10, 20, 30, 50, 100
					int capability = project.getCapability();
					capabilityField.setSelectedIndex(capability == 5 ? 1
							: (capability == 10 ? 2
									: (capability == 20 ? 3
											: (capability == 30 ? 4
													: (capability == 50 ? 5 : (capability == 80 ? 6 : 7))))));

					int idx = capabilityField.getSelectedIndex() - 1;
					if (idx < 0) {
						equipmentField.setText("");
						return;
					}
					StringBuffer s = new StringBuffer();
					for (int i = 0; i < Consts.devices.length; i++) {
						String devName = Consts.devices[i];
						int count = Consts.devCountOpts[i][idx];
						s.append(devName).append(":").append(count).append(" / ");
					}
					equipmentField.setText(s.toString().substring(0, s.length() - 3));

					workerNameField.setText(project.getWorkerName());
					workerContactField.setText(project.getWorkerPhone());

					SoProjectWorkingMode mode = project.getProjectWorkingMode();
					if (mode != null) {
						for (int i = 0; i < workingTimeCheckBoxs.size(); i++) {
							JCheckBox hCheckBox = workingTimeCheckBoxs.get(i);
							Method method;
							try {
								method = SoProjectWorkingMode.class.getMethod("getH_" + i);
								Object v = method.invoke(mode);
								Short h = (Short) v;
								hCheckBox.setSelected((h == 1) ? true : false);
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException
									| IllegalArgumentException | InvocationTargetException e1) {
								e1.printStackTrace();
							}
						}
					}
					// update table
					cardLayout.show(this, "edit");
					break;
				default:
					break;
				}
			}
		}
	}
}
