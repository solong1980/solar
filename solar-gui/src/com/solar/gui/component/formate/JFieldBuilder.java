package com.solar.gui.component.formate;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.solar.common.util.RegExpValidatorUtils;

public class JFieldBuilder {
	//NJTextField
	public static JTextField createNoEmptyField(JComponent co, InputState inputState, String c, Integer column) {
		JTextField field = new JTextField(c, column);
		field.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (inputState.isClean) {
					String text = field.getText();
					if (text == null || text.trim().isEmpty()) {
						inputState.isClean = false;
						field.requestFocus();
						JOptionPane.showMessageDialog(co, "该项必填");
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

	public static JTextField createNumberField(JComponent co, boolean noempty) {
		JTextField field = new JTextField();
		if (noempty)
			field.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					String text = field.getText();
					if (text == null || text.trim().isEmpty()) {
						JOptionPane.showMessageDialog(co, "该项必填");
						field.requestFocus();
					} else {
						try {
							Integer.parseInt(text);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(co, "该项为整数");
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

	public static JTextField createPhoneField(JComponent co, InputState inputState, boolean noempty) {
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
								JOptionPane.showMessageDialog(co, "该项必填");
								inputState.isClean = true;
								return;
							}
						}
						boolean isMobile = RegExpValidatorUtils.IsMobile(text);
						if (!isMobile) {
							inputState.isClean = false;
							field.requestFocus();
							JOptionPane.showMessageDialog(co, "该项为手机号码");
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
