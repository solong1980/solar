package com.solar.gui.component.formate;

import java.awt.Color;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import com.solar.common.util.RegExpValidatorUtils;

public class JFieldBuilder {
	public static void noneChoose(JLabel show, String msg, JComboBox<?> comp) {
		int index = comp.getSelectedIndex();
		if (index == -1) {
			showMsg(show, msg);
			comp.grabFocus();
			throw new RuntimeException("None choose");
		}
		cleanMsg(show);
	}

	public static void noneChoose(JLabel show, String msg, JComboBox<?> comp, int f) {
		int index = comp.getSelectedIndex();
		if (index == f) {
			showMsg(show, msg);
			comp.grabFocus();
			throw new RuntimeException("None choose");
		}
		cleanMsg(show);
	}

	public static void isEmail(JLabel show, String msg, JTextComponent textComponent) {
		String text = textComponent.getText();
		if (text != null && !RegExpValidatorUtils.isEmail(text)) {
			showMsg(show, msg);
			textComponent.requestFocus();
			throw new RuntimeException("Is not email formate");
		}
		cleanMsg(show);
	}

	public static void noEmpty(JLabel show, String msg, List<?> arr, JComponent comp) {
		if (arr == null || arr.isEmpty()) {
			showMsg(show, msg);
			comp.requestFocus();
			throw new RuntimeException("Empty list");
		}
		cleanMsg(show);
	}

	public static void noEmpty(JLabel show, String msg, JTextComponent textComponent) {
		String text = textComponent.getText();
		if (text == null || text.trim().isEmpty()) {
			showMsg(show, msg);
			textComponent.requestFocus();
			throw new RuntimeException("Empty feild");
		}
		cleanMsg(show);
	}

	public static void isPhone(JLabel show, String msg, JTextComponent textComponent) {
		String text = textComponent.getText();
		boolean isMobile = RegExpValidatorUtils.IsMobile(text);
		if (!isMobile) {
			showMsg(show, "该项为手机号码");
			textComponent.requestFocus();
			throw new RuntimeException("Not a phone");
		}
		cleanMsg(show);
	}

	public static void isNumber(JLabel show, String msg, JTextComponent textComponent) {
		String text = textComponent.getText();
		try {
			Integer.parseInt(text);
		} catch (Exception e1) {
			showMsg(show, "该项为整数");
			textComponent.requestFocus();
			throw new RuntimeException("Not a number");
		}
		cleanMsg(show);
	}

	private static void showMsg(JLabel show, String msg) {
		if (show != null) {
			show.setForeground(Color.RED);
			show.setText("校验结果:" + msg);
		}
	}

	public static void cleanMsg(JLabel show) {
		if (show != null) {
			show.setForeground(Color.BLACK);
			show.setText("校验结果:");
		}
	}

}
