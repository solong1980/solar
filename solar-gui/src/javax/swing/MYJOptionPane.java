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

	/**
	 * @see #getUIClassID
	 * @see #readObject
	 */
	private static final String uiClassID = "OptionPaneUI";

	public static int showOptionDialog(Component parentComponent, Object message, String title, int optionType,
			int messageType, Icon icon, Object[] options, Object initialValue, int x, int y) throws HeadlessException {
		MYJOptionPane pane = new MYJOptionPane(message, messageType, optionType, icon, options, initialValue);

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
		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				dialog.setUndecorated(true);
				getRootPane().setWindowDecorationStyle(style);
			}
		}
		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);

		final PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				// Let the defaultCloseOperation handle the closing
				// if the user closed the window without selecting a button
				// (newValue = null in that case). Otherwise, close the dialog.
				if (dialog.isVisible() && event.getSource() == MYJOptionPane.this
						&& (event.getPropertyName().equals(VALUE_PROPERTY)) && event.getNewValue() != null
						&& event.getNewValue() != JOptionPane.UNINITIALIZED_VALUE) {
					dialog.setVisible(false);
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
				// reset value to ensure closing works properly
				setValue(JOptionPane.UNINITIALIZED_VALUE);
			}
		});

		addPropertyChangeListener(listener);
	}

	/**
	 * Returns the specified component's <code>Frame</code>.
	 *
	 * @param parentComponent
	 *            the <code>Component</code> to check for a <code>Frame</code>
	 * @return the <code>Frame</code> that contains the component, or
	 *         <code>getRootFrame</code> if the component is <code>null</code>, or
	 *         does not have a valid <code>Frame</code> parent
	 * @exception HeadlessException
	 *                if <code>GraphicsEnvironment.isHeadless</code> returns
	 *                <code>true</code>
	 * @see #getRootFrame
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 */
	public static Frame getFrameForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null)
			return getRootFrame();
		if (parentComponent instanceof Frame)
			return (Frame) parentComponent;
		return MYJOptionPane.getFrameForComponent(parentComponent.getParent());
	}

	/**
	 * Returns the specified component's toplevel <code>Frame</code> or
	 * <code>Dialog</code>.
	 *
	 * @param parentComponent
	 *            the <code>Component</code> to check for a <code>Frame</code> or
	 *            <code>Dialog</code>
	 * @return the <code>Frame</code> or <code>Dialog</code> that contains the
	 *         component, or the default frame if the component is
	 *         <code>null</code>, or does not have a valid <code>Frame</code> or
	 *         <code>Dialog</code> parent
	 * @exception HeadlessException
	 *                if <code>GraphicsEnvironment.isHeadless</code> returns
	 *                <code>true</code>
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 */
	static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null)
			return getRootFrame();
		if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
			return (Window) parentComponent;
		return MYJOptionPane.getWindowForComponent(parentComponent.getParent());
	}

	private static final Object sharedFrameKey = JOptionPane.class;

	/**
	 * Creates a <code>JOptionPane</code> with a test message.
	 */
	public MYJOptionPane() {
		this("JOptionPane message");
	}

	/**
	 * Creates a instance of <code>JOptionPane</code> to display a message using the
	 * plain-message message type and the default options delivered by the UI.
	 *
	 * @param message
	 *            the <code>Object</code> to display
	 */
	public MYJOptionPane(Object message) {
		this(message, PLAIN_MESSAGE);
	}

	/**
	 * Creates an instance of <code>JOptionPane</code> to display a message with the
	 * specified message type and the default options,
	 *
	 * @param message
	 *            the <code>Object</code> to display
	 * @param messageType
	 *            the type of message to be displayed: <code>ERROR_MESSAGE</code>,
	 *            <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
	 *            <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
	 */
	public MYJOptionPane(Object message, int messageType) {
		this(message, messageType, DEFAULT_OPTION);
	}

	/**
	 * Creates an instance of <code>JOptionPane</code> to display a message with the
	 * specified message type and options.
	 *
	 * @param message
	 *            the <code>Object</code> to display
	 * @param messageType
	 *            the type of message to be displayed: <code>ERROR_MESSAGE</code>,
	 *            <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
	 *            <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
	 * @param optionType
	 *            the options to display in the pane: <code>DEFAULT_OPTION</code>,
	 *            <code>YES_NO_OPTION</code>, <code>YES_NO_CANCEL_OPTION</code>,
	 *            <code>OK_CANCEL_OPTION</code>
	 */
	public MYJOptionPane(Object message, int messageType, int optionType) {
		this(message, messageType, optionType, null);
	}

	/**
	 * Creates an instance of <code>JOptionPane</code> to display a message with the
	 * specified message type, options, and icon.
	 *
	 * @param message
	 *            the <code>Object</code> to display
	 * @param messageType
	 *            the type of message to be displayed: <code>ERROR_MESSAGE</code>,
	 *            <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
	 *            <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
	 * @param optionType
	 *            the options to display in the pane: <code>DEFAULT_OPTION</code>,
	 *            <code>YES_NO_OPTION</code>, <code>YES_NO_CANCEL_OPTION</code>,
	 *            <code>OK_CANCEL_OPTION</code>
	 * @param icon
	 *            the <code>Icon</code> image to display
	 */
	public MYJOptionPane(Object message, int messageType, int optionType, Icon icon) {
		this(message, messageType, optionType, icon, null);
	}

	/**
	 * Creates an instance of <code>JOptionPane</code> to display a message with the
	 * specified message type, icon, and options. None of the options is initially
	 * selected.
	 * <p>
	 * The options objects should contain either instances of
	 * <code>Component</code>s, (which are added directly) or <code>Strings</code>
	 * (which are wrapped in a <code>JButton</code>). If you provide
	 * <code>Component</code>s, you must ensure that when the <code>Component</code>
	 * is clicked it messages <code>setValue</code> in the created
	 * <code>JOptionPane</code>.
	 *
	 * @param message
	 *            the <code>Object</code> to display
	 * @param messageType
	 *            the type of message to be displayed: <code>ERROR_MESSAGE</code>,
	 *            <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
	 *            <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
	 * @param optionType
	 *            the options to display in the pane: <code>DEFAULT_OPTION</code>,
	 *            <code>YES_NO_OPTION</code>, <code>YES_NO_CANCEL_OPTION</code>,
	 *            <code>OK_CANCEL_OPTION</code>
	 * @param icon
	 *            the <code>Icon</code> image to display
	 * @param options
	 *            the choices the user can select
	 */
	public MYJOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options) {
		this(message, messageType, optionType, icon, options, null);
	}

	/**
	 * Creates an instance of <code>JOptionPane</code> to display a message with the
	 * specified message type, icon, and options, with the initially-selected option
	 * specified.
	 *
	 * @param message
	 *            the <code>Object</code> to display
	 * @param messageType
	 *            the type of message to be displayed: <code>ERROR_MESSAGE</code>,
	 *            <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
	 *            <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
	 * @param optionType
	 *            the options to display in the pane: <code>DEFAULT_OPTION</code>,
	 *            <code>YES_NO_OPTION</code>, <code>YES_NO_CANCEL_OPTION</code>,
	 *            <code>OK_CANCEL_OPTION</code>
	 * @param icon
	 *            the Icon image to display
	 * @param options
	 *            the choices the user can select
	 * @param initialValue
	 *            the choice that is initially selected; if <code>null</code>, then
	 *            nothing will be initially selected; only meaningful if
	 *            <code>options</code> is used
	 */
	public MYJOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options,
			Object initialValue) {

		this.message = message;
		this.options = options;
		this.initialValue = initialValue;
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
