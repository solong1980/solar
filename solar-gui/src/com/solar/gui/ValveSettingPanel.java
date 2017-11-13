package com.solar.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.solar.common.context.ActionType;

public class ValveSettingPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private GridBagLayout layout = new GridBagLayout();
	private BtnListener btnListener = new BtnListener();
	String[] str = { "水位", "电压", "电流" };
	String[] label = { "项目名", "当前值", "修改值", "操作" };

	public void addTableTitle() {
		JLabel itemName = new JLabel(label[0], SwingConstants.CENTER);
		JLabel origianl = new JLabel(label[1], SwingConstants.CENTER);
		JLabel inputarea = new JLabel(label[2], SwingConstants.CENTER);
		JLabel submitBtn = new JLabel(label[3], SwingConstants.CENTER);
		JPanel tailspan = new JPanel();

		this.add(itemName);
		this.add(origianl);
		this.add(inputarea);
		this.add(submitBtn);
		this.add(tailspan);

		GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，
		s.fill = GridBagConstraints.BOTH;

		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(itemName, s);

		s.gridwidth = 1;
		s.weightx = 0.1;
		s.weighty = 0;
		layout.setConstraints(origianl, s);

		s.gridwidth = 5;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(inputarea, s);

		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(submitBtn, s);

		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(tailspan, s);
	}

	public void addItem(String type, String original, String btn) {
		JLabel typeName = new JLabel(type, SwingConstants.CENTER);
		typeName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel origianl = new JLabel(original, SwingConstants.CENTER);
		origianl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JTextField inputarea = new JTextField();
		JButton submitBtn = new JButton(btn);
		JPanel tailspan = new JPanel();

		btnListener.add(submitBtn, origianl, ActionType.WORKING_MODE_UPDATE, inputarea);

		this.add(typeName);
		this.add(origianl);
		this.add(inputarea);
		this.add(submitBtn);
		this.add(tailspan);

		GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，
		s.fill = GridBagConstraints.BOTH;

		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(typeName, s);

		s.gridwidth = 1;
		s.weightx = 0.1;
		s.weighty = 0;
		layout.setConstraints(origianl, s);

		s.gridwidth = 5;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(inputarea, s);

		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(submitBtn, s);

		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(tailspan, s);
	}

	public void addOptions() {
		JComboBox<String> j5 = new JComboBox<>(str);
		JTextField j6 = new JTextField();
		JButton j7 = new JButton("清空");

		GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，
		// 是用来控制添加进的组件的显示位置
		s.fill = GridBagConstraints.BOTH;
		this.add(j5);
		this.add(j6);
		this.add(j7);

		s.gridwidth = 2;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(j5, s);

		s.gridwidth = 4;
		s.weightx = 1;
		s.weighty = 0;
		layout.setConstraints(j6, s);

		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(j7, s);
	}

	public void addBottom() {
		JButton j1 = new JButton("打开");
		JButton j2 = new JButton("保存");
		JButton j3 = new JButton("另存为");
		JPanel j4 = new JPanel();

		this.add(j1);// 把组件添加进jframe
		this.add(j2);
		this.add(j3);
		this.add(j4);

		GridBagConstraints s = new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;
		// 该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
		// NONE：不调整组件大小。
		// HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
		// VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
		// BOTH：使组件完全填满其显示区域。
		s.gridwidth = 1;// 该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
		s.weightx = 0;// 该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		s.weighty = 0;// 该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		layout.setConstraints(j1, s);// 设置组件
		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(j2, s);
		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(j3, s);

		s.gridwidth = 0;// 该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
		s.weightx = 0;// 不能为1，j4是占了4个格，并且可以横向拉伸，
		// 但是如果为1，后面行的列的格也会跟着拉伸,导致j7所在的列也可以拉伸
		// 所以应该是跟着j6进行拉伸
		s.weighty = 0;
		layout.setConstraints(j4, s);
	}

	public void addInfo() {
		GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，
		// 是用来控制添加进的组件的显示位置
		s.fill = GridBagConstraints.BOTH;

		JList<String> j8 = new JList<>(str);
		JTextArea j9 = new JTextArea();
		j9.setBackground(Color.PINK);// 为了看出效果，设置了颜色
		JPanel j4 = new JPanel();

		this.add(j8);
		this.add(j9);
		this.add(j4);

		s.gridwidth = 4;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(j8, s);

		s.gridwidth = 3;
		s.weightx = 1;
		s.weighty = 0;
		layout.setConstraints(j9, s);

		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(j4, s);
	}

	public ValveSettingPanel() throws HeadlessException {
		super();
		this.setLayout(layout);
		addTableTitle();

		createSplit();

		addItem("电压", "2", "提交");
		addItem("水位", "2", "提交");
		addItem("电流位", "2", "提交");

		// addOptions();
		// addBottom();
		addInfo();

	}

	private void createSplit() {
		GridBagConstraints s = new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;
		JLabel j8 = new JLabel();
		j8.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(j8);
		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(j8, s);
	}

}
