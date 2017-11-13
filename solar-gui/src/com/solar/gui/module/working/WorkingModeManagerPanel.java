package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class WorkingModeManagerPanel extends BasePanel {
	private Action actionRefresh;
	private Action actionUpdate;

	private JComponent layoutControl() {
		JComponent result = new JPanel();
		result.add(new JButton(actionRefresh));
		result.add(new JButton(actionUpdate));
		return result;
	}

	private JComponent layoutFields() {
		JLabel continuousLabel = new JLabel(getBoldHTML("连续工作模式"));
		JLabel timingLabel = new JLabel(getBoldHTML("定时开关工作模式"));
		JLabel pointOfTimeLabel = new JLabel(getBoldHTML("时间点"));
		JLabel fixedTimingLengthLabel = new JLabel(getBoldHTML("定时长开关工作模式"));
		JLabel timeIntervalLabel = new JLabel(getBoldHTML("时长间隔"));
		JLabel emergencyLabel = new JLabel(getBoldHTML("应急开关工作模式"));
		JLabel createTimeLabel = new JLabel(getBoldHTML("创建时间"));

		JComponent result = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		result.add(continuousLabel, gbc);
		gbc.gridy++;
		result.add(timingLabel, gbc);
		gbc.gridy++;
		result.add(pointOfTimeLabel, gbc);
		gbc.gridy++;
		result.add(fixedTimingLengthLabel, gbc);
		gbc.gridy++;
		result.add(timeIntervalLabel, gbc);
		gbc.gridy++;
		result.add(emergencyLabel, gbc);
		gbc.gridy++;
		result.add(createTimeLabel, gbc);

		JTextField continuousField = new JTextField("连续工作模式");
		JTextField timingField = new JTextField("定时开关工作模式");
		JTextField pointOfTimeField = new JTextField("时间点");
		JTextField fixedTimingLengthField = new JTextField("定时长开关工作模式");
		JTextField timeIntervalField = new JTextField("时长间隔");
		JTextField emergencyField = new JTextField("应急开关工作模式");
		JTextField createTimeField = new JTextField("创建时间");

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		result.add(continuousField, gbc);
		gbc.gridy++;
		result.add(timingField, gbc);
		gbc.gridy++;
		result.add(pointOfTimeField, gbc);
		gbc.gridy++;
		result.add(fixedTimingLengthField, gbc);
		gbc.gridy++;
		result.add(timeIntervalField, gbc);
		gbc.gridy++;
		result.add(emergencyField, gbc);
		gbc.gridy++;
		result.add(createTimeField, gbc);
		return result;
	}

	public WorkingModeManagerPanel() {
		super(new FlowLayout(FlowLayout.LEADING, 5, 5));
		actionRefresh = new AbstractAction(getBoldRedHTML(LABEL_REFRESH)) {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};
		actionUpdate = new AbstractAction(getBoldRedHTML(LABEL_UPDATA)) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] message = new Object[4];
				message[0] = layoutFields();
				String[] options = { "保存", "取消" };
				int result = JOptionPane.showOptionDialog(WorkingModeManagerPanel.this, message, "修改工作模式",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
				switch (result) {
				case 0: // yes
					JOptionPane.showMessageDialog(WorkingModeManagerPanel.this, "Submit");
					break;
				case 1: // no
					JOptionPane.showMessageDialog(WorkingModeManagerPanel.this, "Cancel");
					break;
				default:
					break;
				}

			}
		};
		add(layoutFields(), BorderLayout.CENTER);
		add(layoutControl(), BorderLayout.SOUTH);
	}

}
