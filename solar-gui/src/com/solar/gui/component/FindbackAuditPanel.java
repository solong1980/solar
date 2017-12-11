package com.solar.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Observer;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.solar.gui.module.working.BasePanel;
import com.solar.gui.module.working.fuc.AccountAuditPanel.AuditAction;

@SuppressWarnings("serial")
public class FindbackAuditPanel extends JPanel implements Observer {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		FindbackAuditPanel comp = new FindbackAuditPanel(null, null);
		frame.getContentPane().add(comp);
		frame.setVisible(true);

	}

	final int INITIAL_ROWHEIGHT = 33;
	JTable regiestTable;
	final String[] names = { "ID", "用户姓名", "原手机号码", "新手机号码", "用户类型", "地址", "状态", "申请时间", "操作" };
	String[] optItems = new String[] { "审核通过", "审核不通过" };
	DefaultTableModel dataModel;

	AuditAction agreeAction, rejectAction;

	public FindbackAuditPanel(AuditAction agreeAction, AuditAction rejectAction) {
		this.agreeAction = agreeAction;
		this.rejectAction = rejectAction;
		findBackAuditPanel();
	}

	public void updateData(final Object[][] data) {
		dataModel.getDataVector().clear();
		for (Object[] objects : data) {
			dataModel.addRow(objects);
		}
		dataModel.fireTableDataChanged();
	}

	public JScrollPane createTable(Object[][] data) {

		// final Object[][] data = { { "1", "龙良华名", "1567323233", "环保菊",
		// "北京->aaa->aaaa,上海->aaa->aaaa,上海->aaa->aaaa,上海->aaa->aaaa上海->aaa->aaaa,上海->aaa->aaaa,上海->aaa->aaaa上海->aaa->aaaa,上海->aaa->aaaa,上海->aaa->aaaa辨别辨别",
		// " 审核通过", optItems }, { "2", "郑辉", "1597323233", "环保菊", "天津,广州", " 待审核",
		// optItems } };
		dataModel = new DefaultTableModel(data, names) {
			public boolean isCellEditable(int row, int col) {
				return true;
			}
		};
		// Create the table
		JTable tableView = new JTable(dataModel) {
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
		TableColumnModel columnModel = tableView.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(0).setMaxWidth(50);

		columnModel.getColumn(1).setPreferredWidth(80);
		columnModel.getColumn(1).setMaxWidth(80);
		columnModel.getColumn(2).setPreferredWidth(100);
		columnModel.getColumn(2).setMaxWidth(100);
		columnModel.getColumn(3).setPreferredWidth(100);
		columnModel.getColumn(3).setMaxWidth(100);
		columnModel.getColumn(4).setPreferredWidth(100);
		columnModel.getColumn(4).setMaxWidth(100);

		columnModel.getColumn(6).setPreferredWidth(80);
		columnModel.getColumn(6).setMaxWidth(80);
		columnModel.getColumn(7).setPreferredWidth(150);
		columnModel.getColumn(7).setMaxWidth(150);
		columnModel.getColumn(8).setPreferredWidth(150);
		columnModel.getColumn(8).setMaxWidth(150);

		tableView.setFont(new Font("Menu.font", Font.PLAIN, 15));
		tableView.setRowHeight(INITIAL_ROWHEIGHT);

		TableRowSorter<TableModel> sorter = new TableRowSorter<>(dataModel);
		tableView.setRowSorter(sorter);

		TableColumn operateColumn = tableView.getColumn("操作");
		operateColumn.setCellRenderer(new OperateBtnGroupRenderer());
		operateColumn.setCellEditor(new OperateBtnGroupEditor(new JCheckBox("")));

		JScrollPane scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}

	public void findBackAuditPanel() {
		regiestTable = new JTable();
		setLayout(new BorderLayout());
		JScrollPane scrollTablePanel = createTable(new Object[0][9]);
		add(scrollTablePanel, BorderLayout.CENTER);
		JPanel pagionationPanel = new JPanel();
		pagionationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton comp = new JButton("上一页");
		JButton comp2 = new JButton("下一页");
		pagionationPanel.add(comp);
		pagionationPanel.add(comp2);
		add(pagionationPanel, BorderLayout.SOUTH);
	}

	class OperateBtnGroupRenderer extends JPanel implements TableCellRenderer {
		JButton agreeBtn;
		JButton rejectBtn;

		public OperateBtnGroupRenderer() {
			setBackground(Color.BLUE);
			setLayout(new FlowLayout(FlowLayout.LEFT));
			setOpaque(true);
			agreeBtn = BasePanel.createTableButton("通过", agreeAction);
			rejectBtn = BasePanel.createTableButton("不通过", agreeAction);
			add(agreeBtn);
			add(rejectBtn);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			// 传入状态,id等信息,用于绑定事件,设置是否disable
			Long[] v = (Long[]) value;
			Long id = v[0];
			Integer status = v[1].intValue();
			if (status == 60 || status == 50) {
				removeAll();
				repaint();
			} else {
				add(agreeBtn);
				add(rejectBtn);
				agreeBtn.setActionCommand(Long.toString(id));
				rejectBtn.setActionCommand(Long.toString(id));
			}
			return this;
		}
	}

	class OperateBtnGroupEditor extends DefaultCellEditor {
		private Object value;

		public OperateBtnGroupEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			JPanel jPanel;
			JButton agreeBtn;
			JButton rejectBtn;
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			Long[] v = (Long[]) value;
			Long id = v[0];
			Integer status = v[1].intValue();
			if (status == 60 || status == 50) {
				jPanel.removeAll();
				repaint();
			} else {
				agreeBtn = BasePanel.createTableButton("通过", agreeAction);
				rejectBtn = BasePanel.createTableButton("不通过", rejectAction);

				agreeBtn.setActionCommand(Long.toString(id));
				rejectBtn.setActionCommand(Long.toString(id));

				// 传入状态,id等信息,用于绑定事件,设置是否disable
				jPanel.add(agreeBtn);
				jPanel.add(rejectBtn);

			}
			if (isSelected) {
				jPanel.setForeground(table.getSelectionForeground());
				jPanel.setBackground(table.getSelectionBackground());
			} else {
				jPanel.setForeground(table.getForeground());
				jPanel.setBackground(table.getBackground());
			}

			// value穿id号
			this.value = value;
			return jPanel;
		}

		public Object getCellEditorValue() {
			return this.value;
		}

		public boolean stopCellEditing() {
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}

	@Override
	public void update(java.util.Observable o, Object arg) {

	}

}
