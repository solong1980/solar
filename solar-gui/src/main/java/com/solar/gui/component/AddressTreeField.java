package com.solar.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

import com.solar.gui.component.model.TreeAddr;

@SuppressWarnings("serial")
public class AddressTreeField extends JPanel {

	private Map<String, Object> values;

	public Object[] defaultValues;

	public List<TreeAddr> selectedKeys;

	private List<ActionListener> listeners = new ArrayList<ActionListener>();

	private AddressTreePopup popup;
	private JTextField editor;

	protected JButton arrowButton;
	private String valueSperator;
	private static final String DEFAULT_VALUE_SPERATOR = ",";

	EditorHandler l = new EditorHandler();

	public AddressTreeField(Map<String, Object> value, Object[] defaultValue, boolean leafOnly) {
		this(value, defaultValue, DEFAULT_VALUE_SPERATOR, leafOnly);
	}

	public AddressTreeField(Map<String, Object> value, Object[] defaultValue) {
		this(value, defaultValue, DEFAULT_VALUE_SPERATOR, true);
	}

	public AddressTreeField(Map<String, Object> value, Object[] defaultValue, String valueSperator, boolean leafOnly) {
		this.values = value;
		defaultValues = defaultValue;
		this.valueSperator = valueSperator;
		initComponent(leafOnly);
	}

	public void cleanSelected() {
		editor.setText("");
		popup.cleanSelected();
	}

	private void initComponent(boolean leafOnly) { // 暂时使用该布局,后续自己写个布局
		this.setLayout(new FlowLayout());
		popup = new AddressTreePopup(values, defaultValues, leafOnly);
		popup.addActionListener(new PopupAction());
		editor = new JTextField("", 35);
		editor.setBackground(Color.WHITE);
		editor.setEditable(false);
		editor.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		arrowButton = createArrowButton();
		editor.addMouseListener(l);
		arrowButton.addMouseListener(l);
		add(editor);
		add(arrowButton);
		setText();

	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			editor.addMouseListener(l);
			arrowButton.addMouseListener(l);
		} else {
			editor.removeMouseListener(l);
			arrowButton.removeMouseListener(l);
		}
	}

	public Object[] getSelectedValues() {
		return popup.getSelectedValues();
	}

	public void addActionListener(ActionListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	protected void fireActionPerformed(ActionEvent e) {
		for (ActionListener l : listeners) {
			l.actionPerformed(e);
		}
	}

	private class PopupAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(AddressTreePopup.CANCEL_EVENT)) {
			} else if (e.getActionCommand().equals(AddressTreePopup.COMMIT_EVENT)) {
				defaultValues = popup.getSelectedValues();
				selectedKeys = popup.getSelectedKeys();
				setText(); // 把事件继续传递出去
				fireActionPerformed(e);
				System.out.println(selectedKeys);
			}
			JComponent component = (JComponent) e.getSource();
			int compHeight = component.getHeight();
			Point mousePoint = MouseInfo.getPointerInfo().getLocation();
			Point frameLocationOnScreen = component.getRootPane().getLocationOnScreen();
			togglePopup(mousePoint.x - frameLocationOnScreen.x, compHeight);
		}
	}

	private void togglePopup(int x, int y) {
		if (popup.isVisible()) {
			popup.setVisible(false);
		} else {
			popup.setDefaultValue(defaultValues);
			popup.show(this, x, y);
		}
	}

	private void setText() {
		StringBuilder builder = new StringBuilder();
		for (Object dv : defaultValues) {
			builder.append(dv);
			builder.append(valueSperator);
		}
		System.out.println(selectedKeys);
		editor.setText(builder.substring(0, builder.length() > 0 ? builder.length() - 1 : 0).toString());
	}

	private class EditorHandler implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			JComponent component = (JComponent) source;
			int compHeight = component.getHeight();

			// Point mousePoint = MouseInfo.getPointerInfo().getLocation();
			// Point frameLocationOnScreen = component.getRootPane().getLocationOnScreen();

			Point p = e.getPoint();
			togglePopup(p.x, e.getY() + compHeight);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// g.setColor(Color.LIGHT_GRAY);
		// g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dCheck = arrowButton.getPreferredSize();
		Dimension dLabel = editor.getPreferredSize();
		return new Dimension(dCheck.width + dLabel.width,
				dCheck.height < dLabel.height ? dLabel.height : dCheck.height);
	}

	@Override
	public void doLayout() {
		Dimension dCheck = editor.getPreferredSize();
		Dimension dLabel = arrowButton.getPreferredSize();
		int yCheck = 0;
		int yLabel = 0;
		if (dCheck.height < dLabel.height)
			yCheck = (dLabel.height - dCheck.height) / 2;
		else
			yLabel = (dCheck.height - dLabel.height) / 2;
		editor.setLocation(0, yCheck);
		editor.setBounds(0, yCheck, dCheck.width, dCheck.height);
		arrowButton.setLocation(dCheck.width, yLabel);
		arrowButton.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
	}

	protected JButton createArrowButton() {
		JButton button = new BasicArrowButton(BasicArrowButton.SOUTH, UIManager.getColor("ComboBox.buttonBackground"),
				UIManager.getColor("ComboBox.buttonShadow"), UIManager.getColor("ComboBox.buttonDarkShadow"),
				UIManager.getColor("ComboBox.buttonHighlight"));
		button.setName("ComboBox.arrowButton");
		return button;
	}

	@SuppressWarnings("unused")
	private class MulitComboboxLayout implements LayoutManager {
		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public Dimension preferredLayoutSize(Container parent) {
			return parent.getPreferredSize();
		}

		public Dimension minimumLayoutSize(Container parent) {
			return parent.getMinimumSize();
		}

		public void layoutContainer(Container parent) {
			int w = parent.getWidth();
			int h = parent.getHeight();
			Insets insets = parent.getInsets();
			h = h - insets.top - insets.bottom;
		}
	}

	public void addMultCombox(JComponent component, Map<String, Object> values, Object[] defaultValues) {
		AddressTreeField mulit = new AddressTreeField(values, defaultValues);
		component.add(mulit);
	}

	public List<TreeAddr> getSelectedKeys() {
		return selectedKeys;
	}

	public void setSelectedKeys(List<TreeAddr> selectedKeys) {
		popup.setDefaultLocations(selectedKeys);
		this.selectedKeys = selectedKeys;
	}
	public void setText(String fullName) {
		this.editor.setText(fullName);
	}

	public static AddressTreeField buildLeafOnly() {
		Map<String, Object> values = new HashMap<String, Object>() {
		};

		Object[] defaultValue = new String[] {};
		AddressTreeField mulit = new AddressTreeField(values, defaultValue);
		return mulit;
	}

	public static AddressTreeField buildFoldLeaf() {
		Map<String, Object> values = new HashMap<String, Object>() {
		};

		Object[] defaultValue = new String[] {};
		AddressTreeField mulit = new AddressTreeField(values, defaultValue, false);
		return mulit;
	}
}
