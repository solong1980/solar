package com.solar.gui.module;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.solar.gui.module.working.AdaWorkingPanel;

@SuppressWarnings("serial")
public class InternalFrameModule extends FunctionModule {
	int windowCount = 0;

	private JMenuBar menuBar = null;
	private JMenu fileMenu = null;

	JDesktopPane desktop = null;

	public Integer PALETTE_LAYER = new Integer(3);
	public int PALETTE_X = 1;
	public int PALETTE_Y = 1;
	public int PALETTE_WIDTH = 160;

	public Integer WORKING_FRAME_LAYER = new Integer(1);

	public InternalFrameModule(JPanel swingset) {
		super(swingset, "工作面板", "toolbar/JDesktop.gif");

		desktop = new JDesktopPane();
		getModulePanel().add(desktop, BorderLayout.CENTER);

		createInternalFramePalette();

		createInteralFrameWorkPanel();
	}

	// 工作面板
	private void createInteralFrameWorkPanel() {
		JInternalFrame workInternalFrame = createInternalFrame(WORKING_FRAME_LAYER, 1, 1);
		int workFrameWidth = PREFERRED_WIDTH - PALETTE_X - PALETTE_WIDTH;
		int workFrameHeight = PREFERRED_HEIGHT - TASKCOL_HEIGHT - HEADER_HEIGHT;
		workInternalFrame.setBounds(PALETTE_X + PALETTE_WIDTH + 1, PALETTE_Y, workFrameWidth, workFrameHeight);

		// tab panel
		AdaWorkingPanel tabbedWorkingPanel = new AdaWorkingPanel(this);
		menuBar = tabbedWorkingPanel.createMenus();
		workInternalFrame.setJMenuBar(menuBar);

		workInternalFrame.setContentPane(tabbedWorkingPanel);
	}

	/**
	 * 本地设置面板
	 * 
	 * @return
	 */
	public JInternalFrame createInternalFramePalette() {
		JInternalFrame palette = new JInternalFrame(getString("运行菜单"));
		palette.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
		palette.getContentPane().setLayout(new BorderLayout());

		palette.setBounds(PALETTE_X, PALETTE_Y, PALETTE_WIDTH, PREFERRED_HEIGHT - TASKCOL_HEIGHT - HEADER_HEIGHT - 1);
		palette.setResizable(false);
		palette.setIconifiable(true);

		desktop.add(palette, PALETTE_LAYER);

		JButton b1 = new JButton("查询1");
		JButton b2 = new JButton("查询2");
		JButton b3 = new JButton("查询3");
		JButton b4 = new JButton("查询4");

		// add frame maker actions
		b1.addActionListener(new ShowFrameAction(this, "查询1", null));
		b2.addActionListener(new ShowFrameAction(this, "查询2", null));
		b3.addActionListener(new ShowFrameAction(this, "查询3", null));
		b4.addActionListener(new ShowFrameAction(this, "查询4", null));

		// add frame maker buttons to panel
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		JPanel buttons1 = new JPanel();
		buttons1.setLayout(new BoxLayout(buttons1, BoxLayout.Y_AXIS));

		buttons1.add(b1);
		buttons1.add(Box.createRigidArea(VGAP10));
		buttons1.add(b2);
		buttons1.add(Box.createRigidArea(VGAP10));
		buttons1.add(b3);
		buttons1.add(Box.createRigidArea(VGAP10));
		buttons1.add(b4);

		p.add(Box.createRigidArea(VGAP15));
		p.add(buttons1);
		p.add(Box.createRigidArea(VGAP15));

		palette.getContentPane().add(p, BorderLayout.NORTH);

		// ************************************
		// * Create frame property checkboxes *
		// ************************************
		p = new JPanel() {
			Insets insets = new Insets(10, 15, 10, 5);

			public Insets getInsets() {
				return insets;
			}
		};
		p.setLayout(new GridLayout(2, 1));

		Box box = new Box(BoxLayout.Y_AXIS);
		JLabel optLabel = new JLabel("设置A");
		JCheckBox windowResizable = new JCheckBox(getString("InternalFrameDemo.resizable_label"), true);
		JCheckBox windowIconifiable = new JCheckBox(getString("InternalFrameDemo.iconifiable_label"), true);
		box.add(Box.createGlue());
		box.add(optLabel);
		box.add(windowResizable);
		box.add(windowIconifiable);
		box.add(Box.createGlue());
		p.add(box);

		box = new Box(BoxLayout.Y_AXIS);
		optLabel = new JLabel("设置B");
		JCheckBox windowClosable = new JCheckBox(getString("InternalFrameDemo.closable_label"), true);
		JCheckBox windowMaximizable = new JCheckBox(getString("InternalFrameDemo.maximizable_label"), true);
		box.add(Box.createGlue());
		box.add(optLabel);
		box.add(windowClosable);
		box.add(windowMaximizable);
		box.add(Box.createGlue());
		p.add(box);

		palette.getContentPane().add(p, BorderLayout.CENTER);

		// ************************************
		// * Create Frame title textfield *
		// ************************************
		p = new JPanel() {
			Insets insets = new Insets(0, 0, 10, 0);

			public Insets getInsets() {
				return insets;
			}
		};

		JTextField windowTitleField = new JTextField(getString("InternalFrameDemo.frame_label"));
		JLabel windowTitleLabel = new JLabel(getString("InternalFrameDemo.title_text_field_label"));

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(Box.createRigidArea(HGAP5));
		p.add(windowTitleLabel, BorderLayout.NORTH);
		p.add(Box.createRigidArea(HGAP5));
		p.add(windowTitleField, BorderLayout.CENTER);
		p.add(Box.createRigidArea(HGAP5));

		palette.getContentPane().add(p, BorderLayout.SOUTH);
		palette.show();
		return palette;
	}

	class ShowFrameAction extends AbstractAction {
		InternalFrameModule module;
		Icon icon;

		public ShowFrameAction(InternalFrameModule module, String name, Icon icon) {
			super(name);
			this.module = module;
			this.icon = icon;
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public JInternalFrame createInternalFrame(Integer layer, int width, int height) {
		JInternalFrame jif = new JInternalFrame();
		jif = new JInternalFrame("Workspace");

		// set properties
		jif.setClosable(false);
		jif.setMaximizable(false);
		jif.setIconifiable(true);
		jif.setResizable(false);

		windowCount++;
		desktop.add(jif, layer);
		try {
			jif.setSelected(true);
		} catch (java.beans.PropertyVetoException e2) {

		}
		jif.show();
		return jif;
	}

}
