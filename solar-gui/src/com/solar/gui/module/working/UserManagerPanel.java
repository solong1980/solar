package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")
public class UserManagerPanel extends JPanel {
	public UserManagerPanel() {
		super();
		setLayout(new BorderLayout());
		// form
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		formPanel.setBorder(new TitledBorder("帐号查询"));
		JLabel accountLabel = new JLabel("帐号");
		JLabel nameLabel = new JLabel("姓名");
		JTextField accountTextField = new JTextField("", 20);
		JTextField nameTextField = new JTextField("", 25);

		JCheckBox isValidCb = new JCheckBox("失效", false);

		JButton queryButton = new JButton("查询");
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

			}
		});
		formPanel.add(accountLabel);
		formPanel.add(accountTextField);
		formPanel.add(nameLabel);
		formPanel.add(nameTextField);

		JPanel buttons = new JPanel();
		buttons.add(isValidCb);
		buttons.add(queryButton);

		formPanel.add(buttons);

		add(formPanel, BorderLayout.NORTH);

		// table
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		JScrollPane scrollTablePanel = createTable();
		tablePanel.add(scrollTablePanel, BorderLayout.CENTER);
		add(tablePanel, BorderLayout.CENTER);

		// pagination
		JPanel pagionationPanel = new JPanel();
		pagionationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton comp = new JButton("上一页");
		JButton comp2 = new JButton("下一页");
		pagionationPanel.add(comp);
		pagionationPanel.add(comp2);
		add(pagionationPanel, BorderLayout.SOUTH);
	}

	final int INITIAL_ROWHEIGHT = 33;

	public JScrollPane createTable() {
		// final
		final String[] names = { "ID", "帐号", "密码", "姓名", "状态", "操作" };
		String[] optItems = new String[] { "禁用", "删除" };
		final Object[][] data = { { "1", "Albers", "******", "阿达", "有效", optItems },
				{ "2", "B2lbedrs", "******", "阿dddd达", "dd有效", optItems } };

		TableModel dataModel = new DefaultTableModel(data, names) {
			public boolean isCellEditable(int row, int col) {
				return true;
			}
		};

		// Create the table
		JTable tableView = new JTable(dataModel);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(dataModel);
		tableView.setRowSorter(sorter);
		tableView.setRowHeight(INITIAL_ROWHEIGHT);

		TableColumn column = tableView.getColumn("操作");
		column.setCellRenderer(new ButtonGroupRenderer());
		column.setCellEditor(new ButtonGroupEditor(new JCheckBox("")));

		JScrollPane scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}

	// dialog
	// confirm
}

@SuppressWarnings("serial")
class ButtonGroupRenderer extends JPanel implements TableCellRenderer {
	JButton delBtn;
	JButton forbBtn;

	public ButtonGroupRenderer() {
		setOpaque(true);
		delBtn = new JButton();
		forbBtn = new JButton();
		delBtn.setOpaque(true);
		forbBtn.setOpaque(true);
		add(delBtn);
		add(forbBtn);
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
		String[] optItems = (String[]) value;
		delBtn.setText(optItems[0]);
		forbBtn.setText(optItems[1]);
		return this;
	}
}

@SuppressWarnings("serial")
class ButtonGroupEditor extends DefaultCellEditor {
	JPanel jPanel;
	JButton delBtn;
	JButton forbBtn;

	private Object label;
	private boolean isPushed;

	private int row;
	private JTable table;

	public ButtonGroupEditor(JCheckBox checkBox) {
		super(checkBox);
		jPanel = new JPanel();
		delBtn = new JButton();
		forbBtn = new JButton();
		delBtn.setOpaque(true);
		forbBtn.setOpaque(true);

		jPanel.add(delBtn);
		jPanel.add(forbBtn);
		delBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
				System.out.println("-----------------------" + table.getModel().getValueAt(row, 1).toString());
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (isSelected) {
			jPanel.setForeground(table.getSelectionForeground());
			jPanel.setBackground(table.getSelectionBackground());
		} else {
			jPanel.setForeground(table.getForeground());
			jPanel.setBackground(table.getBackground());
		}

		this.row = row;
		this.table = table;

		String[] optItems = (String[]) value;
		label = value;
		delBtn.setText(optItems[0]);
		forbBtn.setText(optItems[1]);
		isPushed = true;
		return jPanel;
	}

	public Object getCellEditorValue() {
		if (isPushed) {
			JOptionPane.showMessageDialog(delBtn, label.toString() + ": Ouch!");
		}
		isPushed = false;
		return label;
	}

	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}

@SuppressWarnings("serial")
class UserTableCellRenderer extends DefaultCellEditor implements TableCellRenderer, TableCellEditor {
	JPanel jPanel;
	JButton jButton;
	JButton jButton2;

	public UserTableCellRenderer() {
		super(new JTextField());
		setClickCountToStart(0);
		jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		jButton = new JButton("启用");
		jButton2 = new JButton("禁用");
		jPanel.add(jButton);
		jPanel.add(jButton2);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		jPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		return jPanel;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		jPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		return jPanel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}