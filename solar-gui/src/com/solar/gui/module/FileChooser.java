package com.solar.gui.module;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {

	private JComponent parent;

	public static Dimension HGAP10 = new Dimension(10, 1);
	public static Dimension VGAP10 = new Dimension(1, 10);

	JDialog dialog;
	JFileChooser fc;

	private final ActionListener actionListener;

	public FileChooser(JComponent parent, ActionListener listener) {
		super();
		this.parent = parent;
		this.actionListener = listener;

	}

	@SuppressWarnings("serial")
	public Action createCancelAction() {
		return new AbstractAction(getString("取消")) {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		};
	}

	@SuppressWarnings("serial")
	public Action createOKAction() {
		return new AbstractAction(getString("确定")) {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				if (!e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION) && fc.getSelectedFile() != null) {
					File selectedFile = fc.getSelectedFile();
					FileChooser.this.actionListener.actionPerformed(new ActionEvent(selectedFile, 1, "ChooseFile"));
				}
			}
		};
	}

	@SuppressWarnings("serial")
	public JButton createButton(Action a) {
		JButton b = new JButton(a) {
			public Dimension getMaximumSize() {
				int width = Short.MAX_VALUE;
				int height = super.getMaximumSize().height;
				return new Dimension(width, height);
			}
		};
		return b;
	}

	private String createFileNameFilterDescriptionFromExtensions(String description, String[] extensions) {
		String fullDescription = (description == null) ? "(" : description + " (";
		fullDescription += "." + extensions[0];
		for (int i = 1; i < extensions.length; i++) {
			fullDescription += ", .";
			fullDescription += extensions[i];
		}
		fullDescription += ")";
		return fullDescription;
	}

	private javax.swing.filechooser.FileFilter createFileFilter(String description, String... extensions) {
		description = createFileNameFilterDescriptionFromExtensions(description, extensions);
		return new FileNameExtensionFilter(description, extensions);
	}

	public JFileChooser createFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setDragEnabled(true);
		return fc;
	}

	public JButton createCustomFileChooserButton() {
		@SuppressWarnings("serial")
		Action a = new AbstractAction(getString("选择升级文件")) {
			public void actionPerformed(ActionEvent e) {
				fc = createFileChooser();
				javax.swing.filechooser.FileFilter filter = createFileFilter(getString("文件类型"), "apk", "dmg");

				fc.addChoosableFileFilter(filter);
				fc.setControlButtonsAreShown(false);

				JPanel custom = new JPanel();
				custom.setLayout(new BoxLayout(custom, BoxLayout.Y_AXIS));
				custom.add(Box.createRigidArea(VGAP10));
				JLabel description = new JLabel(getString("选择升级文件"));
				description.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				custom.add(description);
				custom.add(Box.createRigidArea(VGAP10));
				custom.add(fc);

				Action okAction = createOKAction();
				fc.addActionListener(okAction);

				JPanel buttons = new JPanel();
				buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

				buttons.add(Box.createRigidArea(HGAP10));
				buttons.add(createButton(okAction));
				buttons.add(Box.createRigidArea(HGAP10));
				buttons.add(createButton(createCancelAction()));
				buttons.add(Box.createRigidArea(HGAP10));

				custom.add(buttons);
				custom.add(Box.createRigidArea(VGAP10));

				Frame parent = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, FileChooser.this.parent);
				dialog = new JDialog(parent, getString("文件选择"), true);
				dialog.setMinimumSize(new Dimension(800, 400));
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dialog.getContentPane().add(custom, BorderLayout.CENTER);
				dialog.pack();
				dialog.setLocationRelativeTo(FileChooser.this.parent);
				dialog.setVisible(true);
			}
		};
		return createButton(a);
	}

	private String getString(String str) {
		return str;
	}
}