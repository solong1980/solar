package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdatepicker.JDatePicker;

@SuppressWarnings("serial")
public class RunningDataPanel extends BasePanel {
	JTable tableView;

	public RunningDataPanel() {
		super();
		setLayout(new BorderLayout());
		// form
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		formPanel.setBorder(new TitledBorder("查询"));

		JLabel timeRangeLabel = new JLabel("时间");
		JDatePicker startDatePicker = new JDatePicker();
		JDatePicker endDatePicker = new JDatePicker();
		JSpinner year = timeSpinner();

		JLabel custLabel = new JLabel("姓名");
		JTextField custTextField = new JTextField("", 25);

		JCheckBox isValidCb = new JCheckBox("失效", false);
		JButton queryButton = new JButton("查询");
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
			}
		});

		JButton printButton = new JButton("打印");
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				printTable();
			}
		});

		formPanel.add(timeRangeLabel);
		formPanel.add(year);
		formPanel.add(startDatePicker);
		formPanel.add(endDatePicker);

		formPanel.add(custLabel);
		formPanel.add(custTextField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(isValidCb);
		buttonPanel.add(queryButton);
		buttonPanel.add(printButton);
		formPanel.add(buttonPanel);

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

	private JSpinner timeSpinner() {
		// 获得时间日期模型
		SpinnerDateModel model = new SpinnerDateModel();
		// 获得JSPinner对象
		JSpinner year = new JSpinner(model);
		year.setValue(new Date());
		// 设置时间格式
		JSpinner.DateEditor editor = new JSpinner.DateEditor(year, "yyyy-MM-dd HH:mm:ss");
		year.setEditor(editor);
		year.setBounds(34, 67, 219, 22);
		return year;
	}

	final int INITIAL_ROWHEIGHT = 33;

	public JScrollPane createTable() {
		final String[] names = { "ID", "设备ID", "警告码", "IP地址", "太阳能充电电压", "太阳能充电电流", "DC充电电压", "DC充电电流", "电池电压",
				"电池充电电流", "NTC实时温度值", "负载1工作电流", "负载2工作电流", "负载3工作电流", "负载4工作电流", "GPS信息", "操作" };
		String[] optItems = new String[] { "禁用", "删除" };
		final Object[][] data = {
				{ "1", "00010120", "0", "193.179.01.1", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3",
						"2.3", "2.3", "ancdeef", optItems },
				{ "1", "00010120", "0", "193.179.01.1", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3", "2.3",
						"2.3", "2.3", "ancdeef", optItems } };

		TableModel dataModel = new DefaultTableModel(data, names) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col) {
				return true;
			}
		};

		// Create the table
		tableView = new JTable(dataModel);
		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableRowSorter<TableModel> sorter = new TableRowSorter<>(dataModel);
		tableView.setRowSorter(sorter);
		tableView.setRowHeight(INITIAL_ROWHEIGHT);

		TableColumn column = tableView.getColumn("操作");
		column.setCellRenderer(new ButtonGroupRenderer());
		column.setCellEditor(new ButtonGroupEditor(new JCheckBox("")));

		DefaultTableColumnModel cmodel = (DefaultTableColumnModel) tableView.getColumnModel();
		for (int index = 0; index < cmodel.getColumnCount(); index++) {
			column = cmodel.getColumn(index);
			column.setPreferredWidth(100);
		}

		JScrollPane scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}

	private void printTable() {
		MessageFormat headerFmt = new MessageFormat("监控数据");
		MessageFormat footerFmt = new MessageFormat(new Date().toString());
		JTable.PrintMode printMode = JTable.PrintMode.FIT_WIDTH;

		try {
			boolean status = tableView.print(printMode, headerFmt, footerFmt);

			if (status) {
				JOptionPane.showMessageDialog(tableView.getParent(), "打印结束", "结果", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(tableView.getParent(), "打印取消", "结果", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException pe) {
			String errorMessage = MessageFormat.format("打印失败", new Object[] { pe.getMessage() });
			JOptionPane.showMessageDialog(tableView.getParent(), errorMessage, "结果", JOptionPane.ERROR_MESSAGE);
		} catch (SecurityException se) {
			String errorMessage = MessageFormat.format("打印失败", new Object[] { se.getMessage() });
			JOptionPane.showMessageDialog(tableView.getParent(), errorMessage, "结果", JOptionPane.ERROR_MESSAGE);
		}
	}
}
