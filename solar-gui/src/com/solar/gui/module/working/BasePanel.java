package com.solar.gui.module.working;

import java.awt.LayoutManager;

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

}
