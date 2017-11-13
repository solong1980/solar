package com.solar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.solar.Client;
import com.solar.common.context.ActionType;

public class BtnListener implements Observer {

	private Client client;
	private Map<ActionType, JLabel> labelMap = new HashMap<>();

	public BtnListener() {
		super();
		client = new Client(BtnListener.this);
	}

	public void add(JButton button, JLabel label, ActionType type, JTextField observe) {
		labelMap.put(type, label);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = observe.getText();
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		String type = arg.toString();
		JLabel jLabel = labelMap.get(type);
		jLabel.setText("新值");
	}
}
