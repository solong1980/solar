package com.solar.gui.module.working;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BasePanel extends JPanel {
	protected static final String LABEL_REFRESH = "刷新";
	protected static final String LABEL_UPDATA = "修改";

	public BasePanel(LayoutManager layout) {
		super(layout);
	}

	public BasePanel() {
		super();
	}

	protected String getBoldRedHTML(String s) {
		return "<html><b><font size=\"4\" color=\"red\">" + s + "</font></b></html>";
	}

	protected String getBoldHTML(String s) {
		return "<html><b>" + s + "</b></html>";
	}

	public static JButton createTableButton(String text) {
		JButton btn = new JButton();
		// delBtn.setOpaque(true);
		// forbBtn.setOpaque(true);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setBounds(new Rectangle(0, 0, 30, 30));
		btn.setPreferredSize(new Dimension(40, 30));
		// Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		// delBtn.setFont(font);
		// delBtn.setBorder(new EmptyBorder(0, 0, 40, 0));
		//btn.setBorder(BorderFactory.createRaisedBevelBorder());
		btn.setText(text);
		return btn;
	}
}
