/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MYJOptionPane extends JOptionPane {
	private static final long serialVersionUID = 1L;

	public interface FormChecker {
		public boolean isvalide(Object btnLabel);
	}

	private FormChecker formChecker;

	@SuppressWarnings("deprecation")
	public static int showOptionDialog(Component parentComponent, Object message, String title, int optionType,
			int messageType, Icon icon, Object[] options, Object initialValue, int x, int y, FormChecker formChecker)
			throws HeadlessException {
		MYJOptionPane pane = new MYJOptionPane(message, messageType, optionType, icon, options, initialValue,
				formChecker);

		pane.setInitialValue(initialValue);
		pane.setComponentOrientation(
				((parentComponent == null) ? getRootFrame() : parentComponent).getComponentOrientation());

		int style = styleFromMessageType(messageType);

		JDialog dialog = pane.createDialog(parentComponent, title, style);
		dialog.setLocation((int) (x - dialog.getSize().getWidth() / 2), y);

		pane.selectInitialValue();

		dialog.show();
		dialog.dispose();

		Object selectedValue = pane.getValue();

		if (selectedValue == null)
			return CLOSED_OPTION;
		if (options == null) {
			if (selectedValue instanceof Integer)
				return ((Integer) selectedValue).intValue();
			return CLOSED_OPTION;
		}
		for (int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
			if (options[counter].equals(selectedValue))
				return counter;
		}
		return CLOSED_OPTION;
	}

	private JDialog createDialog(Component parentComponent, String title, int style) throws HeadlessException {

		final JDialog dialog;

		Window window = MYJOptionPane.getWindowForComponent(parentComponent);
		if (window instanceof Frame) {
			dialog = new JDialog((Frame) window, title, true);
		} else {
			dialog = new JDialog((Dialog) window, title, true);
		}
		initDialog(dialog, style, parentComponent);
		return dialog;
	}

	private void initDialog(final JDialog dialog, int style, Component parentComponent) {
		dialog.setComponentOrientation(this.getComponentOrientation());
		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(this, BorderLayout.CENTER);
		dialog.setResizable(false);

		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);
		final PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				Object ev = event.getNewValue();
				if (dialog.isVisible() && event.getSource() == MYJOptionPane.this
						&& (event.getPropertyName().equals(VALUE_PROPERTY)) && event.getNewValue() != null
						&& event.getNewValue() != JOptionPane.UNINITIALIZED_VALUE) {
					if (formChecker.isvalide(ev))
						dialog.setVisible(false);
					else
						setValue(JOptionPane.UNINITIALIZED_VALUE);
				}
			}
		};

		WindowAdapter adapter = new WindowAdapter() {
			private boolean gotFocus = false;

			public void windowClosing(WindowEvent we) {
				setValue(null);
			}

			public void windowClosed(WindowEvent e) {
				removePropertyChangeListener(listener);
				dialog.getContentPane().removeAll();
			}

			public void windowGainedFocus(WindowEvent we) {
				// Once window gets focus, set initial focus
				if (!gotFocus) {
					selectInitialValue();
					gotFocus = true;
				}
			}
		};
		dialog.addWindowListener(adapter);
		dialog.addWindowFocusListener(adapter);
		dialog.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				setValue(JOptionPane.UNINITIALIZED_VALUE);
			}
		});
		addPropertyChangeListener(listener);
	}

	public static Frame getFrameForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null)
			return getRootFrame();
		if (parentComponent instanceof Frame)
			return (Frame) parentComponent;
		return MYJOptionPane.getFrameForComponent(parentComponent.getParent());
	}

	static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null)
			return getRootFrame();
		if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
			return (Window) parentComponent;
		return MYJOptionPane.getWindowForComponent(parentComponent.getParent());
	}

	public MYJOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options,
			Object initialValue, FormChecker formChecker) {

		this.message = message;
		this.options = options;
		this.initialValue = initialValue;
		this.formChecker = formChecker;
		this.icon = icon;
		setMessageType(messageType);
		setOptionType(optionType);
		value = UNINITIALIZED_VALUE;
		inputValue = UNINITIALIZED_VALUE;
		updateUI();
	}

	private static int styleFromMessageType(int messageType) {
		switch (messageType) {
		case ERROR_MESSAGE:
			return JRootPane.ERROR_DIALOG;
		case QUESTION_MESSAGE:
			return JRootPane.QUESTION_DIALOG;
		case WARNING_MESSAGE:
			return JRootPane.WARNING_DIALOG;
		case INFORMATION_MESSAGE:
			return JRootPane.INFORMATION_DIALOG;
		case PLAIN_MESSAGE:
		default:
			return JRootPane.PLAIN_DIALOG;
		}
	}

}
