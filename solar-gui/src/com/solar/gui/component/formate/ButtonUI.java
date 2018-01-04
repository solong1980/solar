package com.solar.gui.component.formate;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

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
}
