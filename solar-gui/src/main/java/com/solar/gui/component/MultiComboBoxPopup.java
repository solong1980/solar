package com.solar.gui.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class MultiComboBoxPopup extends JPopupMenu {
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	// private Object[] values;
	private Object[] defaultValues;

	private List<Long> selectedKeys = new ArrayList<Long>();

	private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();

	// checkbox key map
	private Map<JCheckBox, Long> boxKeyMap = new HashMap<>();
	// key content map
	private Map<Long, Object> valueMap = null;

	private JButton commitButton;
	private JButton cancelButton;
	public static final String COMMIT_EVENT = "commit";
	public static final String CANCEL_EVENT = "cancel";

	public MultiComboBoxPopup(Map<Long, Object> valueMap, Object[] defaultValue) {
		super();
		this.valueMap = valueMap;
		defaultValues = defaultValue;
		initComponent();
	}

	public void addActionListener(ActionListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	private void initComponent() {
		JPanel checkboxPane = new JPanel();
		JPanel buttonPane = new JPanel();
		JScrollPane sp = new JScrollPane(checkboxPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(300, 300));
		this.setMaximumSize(new Dimension(300, 300));
		for (Entry<Long, Object> v : valueMap.entrySet()) {
			JCheckBox temp = new JCheckBox(v.getValue().toString(), selected(v.getValue()));
			checkBoxList.add(temp);
			boxKeyMap.put(temp, v.getKey());
		}
		if (checkBoxList.get(0).getText().equals("Selected All"))
			checkBoxList.get(0).addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					System.out.println("被选中状态 " + checkBoxList.get(0).isSelected());
					// Select All 被选中
					if (checkBoxList.get(0).isSelected()) {
						// 检查其他的是否被选中乳沟没有就选中他们
						for (int i = 1; i < checkBoxList.size(); i++) {
							if (!checkBoxList.get(i).isSelected())
								checkBoxList.get(i).setSelected(true);
						}
					} else {
						for (int i = 1; i < checkBoxList.size(); i++) {
							if (checkBoxList.get(i).isSelected())
								checkBoxList.get(i).setSelected(false);
						}
					}
				}
			});

		checkboxPane.setLayout(new GridLayout(checkBoxList.size(), 1, 3, 3));
		for (JCheckBox box : checkBoxList) {
			checkboxPane.add(box);
		}
		commitButton = new JButton("确定");
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commit();
			}
		});
		cancelButton = new JButton("取消");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		buttonPane.add(commitButton);
		buttonPane.add(cancelButton);
		this.add(sp, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.SOUTH);
	}

	private boolean selected(Object v) {
		for (Object dv : defaultValues) {
			if (dv.equals(v)) {
				return true;
			}
		}
		return false;
	}

	protected void fireActionPerformed(ActionEvent e) {
		for (ActionListener l : listeners) {
			l.actionPerformed(e);
		}
	}

	public Object[] getSelectedValues() {
		List<Object> selectedValues = new ArrayList<Object>();
		selectedKeys.clear();
		if (checkBoxList.get(0).getText().equals("全选")) {
			if (checkBoxList.get(0).isSelected()) {
				for (int i = 1; i < checkBoxList.size(); i++) {
					JCheckBox temp = checkBoxList.get(i);
					Long key = boxKeyMap.get(temp);
					selectedKeys.add(key);
					selectedValues.add(valueMap.get(key));
				}
			} else {
				for (int i = 1; i < checkBoxList.size(); i++) {
					if (checkBoxList.get(i).isSelected()) {
						JCheckBox temp = checkBoxList.get(i);
						Long key = boxKeyMap.get(temp);
						selectedKeys.add(key);
						selectedValues.add(valueMap.get(key));
					}
				}
			}
		} else
			for (int i = 0; i < checkBoxList.size(); i++) {
				if (checkBoxList.get(i).isSelected()) {
					JCheckBox temp = checkBoxList.get(i);
					Long key = boxKeyMap.get(temp);
					selectedKeys.add(key);
					selectedValues.add(valueMap.get(key));
				}
			}
		return selectedValues.toArray(new Object[selectedValues.size()]);
	}

	public List<Long> getSelectedKeys() {
		return selectedKeys;
	}

	public void setDefaultValue(Object[] defaultValue) {
		defaultValues = defaultValue;
	}

	public void commit() {
		fireActionPerformed(new ActionEvent(this, 0, COMMIT_EVENT));
	}

	public void cancel() {
		fireActionPerformed(new ActionEvent(this, 0, CANCEL_EVENT));
	}
}
