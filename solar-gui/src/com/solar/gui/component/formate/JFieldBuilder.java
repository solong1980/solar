package com.solar.gui.component.formate;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.solar.common.util.RegExpValidatorUtils;

public class JFieldBuilder {

	public static void isEmail(JLabel show, String msg, JTextComponent textComponent) {
		String text = textComponent.getText();
		if (text != null && !RegExpValidatorUtils.isEmail(text)) {
			showMsg(show, msg);
			textComponent.requestFocus();
			throw new RuntimeException("Is not email formate");
		}
	}

	public static void noEmpty(JLabel show, String msg, List<?> arr, JComponent comp) {
		if (arr == null || arr.isEmpty()) {
			showMsg(show, msg);
			comp.requestFocus();
			throw new RuntimeException("Empty list");
		}
	}

	public static void noEmpty(JLabel show, String msg, JTextComponent textComponent) {
		String text = textComponent.getText();
		if (text == null || text.trim().isEmpty()) {
			showMsg(show, msg);
			textComponent.requestFocus();
			throw new RuntimeException("Empty feild");
		}
	}

	public static void isPhone(JLabel show, String msg, JTextComponent textComponent) {
		String text = textComponent.getText();
		boolean isMobile = RegExpValidatorUtils.IsMobile(text);
		if (!isMobile) {
			showMsg(show, "该项为手机号码");
			textComponent.requestFocus();
			throw new RuntimeException("Not a phone");
		}
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
	}

	private static void showMsg(JLabel show, String msg) {
		if (show != null) {
			show.setForeground(Color.RED);
			show.setText("校验结果:" + msg);
		}
	}

	// NJTextField
	public static JTextField createNoEmptyField(JComponent co, JLabel show, InputState inputState, String c,
			Integer column, String msg, int maxLen) {
		JTextField field = new NJTextField(column, maxLen);
		field.setText(c);
		field.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (inputState.isClean) {
					String text = field.getText();
					if (text == null || text.trim().isEmpty()) {
						inputState.isClean = false;
						showMsg(show, msg);
						field.requestFocus();
						inputState.isClean = true;
					}
				}
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});
		return field;
	}

	// NJTextField
	public static JTextField createNoEmptyField(JComponent co, JLabel show, InputState inputState, String c,
			Integer column, String msg) {
		JTextField field = new JTextField(c, column);
		field.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (inputState.isClean) {
					String text = field.getText();
					if (text == null || text.trim().isEmpty()) {
						inputState.isClean = false;
						showMsg(show, msg);
						field.requestFocus();
						inputState.isClean = true;
					}
				}
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});
		return field;
	}

	public static JTextField createNumberField(JComponent co, JLabel show, boolean noempty) {
		JTextField field = new JTextField();
		if (noempty)
			field.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					String text = field.getText();
					if (text == null || text.trim().isEmpty()) {
						showMsg(show, "该项必填");
						field.requestFocus();
					} else {
						try {
							Integer.parseInt(text);
						} catch (Exception e1) {
							showMsg(show, "该项为整数");
							field.requestFocus();
						}
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		return field;
	}

	public static JTextField createPhoneField(JComponent co, JLabel show, InputState inputState, boolean noempty) {
		JTextField field = new JTextField("", 30);
		if (noempty)
			field.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					if (inputState.isClean) {
						String text = field.getText();
						if (noempty) {
							if (text == null || text.trim().isEmpty()) {
								inputState.isClean = false;
								field.requestFocus();
								showMsg(show, "该项必填");
								inputState.isClean = true;
								return;
							}
						}
						boolean isMobile = RegExpValidatorUtils.IsMobile(text);
						if (!isMobile) {
							inputState.isClean = false;
							field.requestFocus();
							showMsg(show, "该项为手机号码");
							inputState.isClean = true;
						}
					}
				}

				@Override
				public void focusGained(FocusEvent e) {

				}
			});
		return field;
	}

}
