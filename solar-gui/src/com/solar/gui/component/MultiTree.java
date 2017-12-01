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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

@SuppressWarnings("serial")
public class MultiTree extends JPanel {

	private Map<String, Object> values;

	public Object[] defaultValues;

	public List<String> selectedKeys;

	private List<ActionListener> listeners = new ArrayList<ActionListener>();

	private MultiTreePopup popup;
	private JTextField editor;

	protected JButton arrowButton;
	private String valueSperator;
	private static final String DEFAULT_VALUE_SPERATOR = ",";

	public MultiTree(Map<String, Object> value, Object[] defaultValue) {
		this(value, defaultValue, DEFAULT_VALUE_SPERATOR);
	}

	public MultiTree(Map<String, Object> value, Object[] defaultValue, String valueSperator) {
		this.values = value;
		defaultValues = defaultValue;
		this.valueSperator = valueSperator;
		initComponent();
	}

	private void initComponent() { // 暂时使用该布局,后续自己写个布局
		this.setLayout(new FlowLayout());
		// this.setBackground(Color.LIGHT_GRAY);
		popup = new MultiTreePopup(values, defaultValues);
		popup.addActionListener(new PopupAction());
		editor = new JTextField();
		editor.setBackground(Color.WHITE);
		editor.setEditable(false);
		editor.setPreferredSize(new Dimension(350, 20)); //
		editor.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		editor.addMouseListener(new EditorHandler());
		arrowButton = createArrowButton();
		arrowButton.addMouseListener(new EditorHandler());
		add(editor);
		add(arrowButton);
		setText();
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
			if (e.getActionCommand().equals(MultiTreePopup.CANCEL_EVENT)) {
			} else if (e.getActionCommand().equals(MultiTreePopup.COMMIT_EVENT)) {
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
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
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
		MultiTree mulit = new MultiTree(values, defaultValues);
		component.add(mulit);
	}

	public List<String> getSelectedKeys() {
		return selectedKeys;
	}

	public static MultiTree build() {
		Map<String, Object> values = new HashMap<String, Object>() {
			{
				put("110000", "北京市");
				put("120000", "天津市");
			}
		};

		Object[] defaultValue = new String[] {};
		MultiTree mulit = new MultiTree(values, defaultValue);
		return mulit;
	}

	public static void main(String[] args) {
		JLabel label3 = new JLabel("Media Outlets:");
		Map<String, Object> values = new HashMap<String, Object>() {
			{
				put("110000", "北京市");
				put("120000", "天津市");
			}
		};

		Object[] defaultValue = new String[] {};
		MultiTree mulit = new MultiTree(values, defaultValue);
		JFrame frame = new JFrame("CheckBoxTreeDemo");
		frame.setBounds(200, 200, 400, 400);
		frame.getContentPane().add(label3);
		frame.getContentPane().add(mulit);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
