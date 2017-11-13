package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.solar.gui.module.FileChooser;

@SuppressWarnings("serial")
public class UpgradeManagerPanel extends BasePanel {
	FileChooser chooser;
	private File upgradeFile;
	JTextField filePathField;
	
	public UpgradeManagerPanel() {
		super(new BorderLayout(5, 5));
		chooser = new FileChooser(this,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				upgradeFile   = (File) e.getSource();
				filePathField.setText(upgradeFile.getPath());
			}
		});
		add(layoutFields(), BorderLayout.CENTER);
		add(layoutControl(), BorderLayout.SOUTH);

	}

	private Component layoutFields() {
		JLabel fileChooserLabel = new JLabel(getBoldHTML("文件选择"));
		JLabel filePathLabel = new JLabel(getBoldHTML("文件路径"));
		JLabel versionLabel = new JLabel(getBoldHTML("版本号"));
		JLabel descLabel = new JLabel(getBoldHTML("版本说明"));

		JComponent result = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 5, 5, 5);
		result.add(fileChooserLabel, gbc);
		gbc.gridy++;
		result.add(filePathLabel, gbc);
		gbc.gridy++;
		result.add(versionLabel, gbc);
		gbc.gridy++;
		result.add(descLabel, gbc);

		JButton fileChooser = chooser.createCustomFileChooserButton();
		filePathField = new JTextField("", 50);
		filePathField.setEditable(false);

		JTextField versionField = new JTextField("", 20);
		JTextField descField = new JTextField("", 50);

		gbc.gridx++;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		result.add(fileChooser, gbc);
		gbc.gridy++;
		result.add(filePathField, gbc);
		gbc.gridy++;
		result.add(versionField, gbc);
		gbc.gridy++;
		result.add(descField, gbc);
		return result;
	}

	private Component layoutControl() {
		JComponent result = new JPanel();
		result.add(new JButton("上传"));
		result.add(new JButton("提交"));
		return result;
	}

	public void run() {

	}

}
