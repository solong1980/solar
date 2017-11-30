package com.solar.gui.module.working;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class BasePanel extends JPanel {
	protected static final String LABEL_REFRESH = "刷新";
	protected static final String LABEL_UPDATA = "修改";
	// 项目树
	JTree tree;

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

	public JMenuItem createMenuItem(JMenu menu, String label, Action action) {
		JMenuItem mi = (JMenuItem) menu.add(new JMenuItem(label));
		mi.addActionListener(action);
		if (action == null) {
			mi.setEnabled(false);
		}
		return mi;
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
		// btn.setBorder(BorderFactory.createRaisedBevelBorder());
		btn.setText(text);
		return btn;
	}

	public JScrollPane createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(getBoldHTML("项目列表"));
		DefaultMutableTreeNode catagory = null;
		DefaultMutableTreeNode artist = null;
		DefaultMutableTreeNode record = null;

		// open tree data
		URL url = getClass().getResource("/resources/tree.txt");

		try {
			// convert url to buffered string
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);

			// read one line at a time, put into tree
			String line = reader.readLine();
			while (line != null) {
				// System.out.println("reading in: ->" + line + "<-");
				char linetype = line.charAt(0);
				switch (linetype) {
				case 'C':
					catagory = new DefaultMutableTreeNode(line.substring(2));
					top.add(catagory);
					break;
				case 'A':
					if (catagory != null) {
						catagory.add(artist = new DefaultMutableTreeNode(line.substring(2)));
					}
					break;
				case 'R':
					if (artist != null) {
						artist.add(record = new DefaultMutableTreeNode(line.substring(2)));
					}
					break;
				case 'S':
					if (record != null) {
						record.add(new DefaultMutableTreeNode(line.substring(2)));
					}
					break;
				default:
					break;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
		}

		tree = new JTree(top) {
			public Insets getInsets() {
				return new Insets(5, 5, 5, 5);
			}
		};

		tree.setEditable(true);

		return new JScrollPane(tree);
	}
}
