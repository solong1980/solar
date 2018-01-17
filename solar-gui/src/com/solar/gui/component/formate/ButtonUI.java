package com.solar.gui.component.formate;

import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ButtonUI {
	public static ImageIcon createImageIcon(String filename, String description) {
		String path = "/resources/images/" + filename;
		return new ImageIcon(ButtonUI.class.getClass().getResource(path), description);
	}

	public static JRadioButton makeRadioBtn(String text, String description) {
		JRadioButton radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
		radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
		radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
		radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
		radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
		radio.setMargin(new Insets(0, 0, 0, 0));
		return radio;
	}

	public static JPanel pagePanel() {
		JPanel pagionationPanel = new JPanel();
		pagionationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton comp = new JButton("上一页");
		JButton comp2 = new JButton("下一页");

		JLabel gongLabel = new JLabel("共");
		JTextField totalField = new JTextField(3);
		totalField.setEnabled(false);
		JLabel tiaoLabel = new JLabel("条");
		pagionationPanel.add(comp);
		pagionationPanel.add(comp2);
		pagionationPanel.add(Box.createHorizontalGlue());
		pagionationPanel.add(gongLabel);
		pagionationPanel.add(totalField);
		pagionationPanel.add(tiaoLabel);
		return pagionationPanel;
	}
}
