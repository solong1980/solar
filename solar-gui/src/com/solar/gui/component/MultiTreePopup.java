package com.solar.gui.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.solar.client.DataClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.Consts.AddrType;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoProvinces;

@SuppressWarnings("serial")
public class MultiTreePopup extends JPopupMenu {
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	// private Object[] values;
	private Object[] defaultValues;

	private List<String> selectedKeys = new ArrayList<String>();

	private DataClient dataClient = new DataClient(NetConf.buildHostConf());

	private JButton commitButton;
	private JButton cancelButton;
	public static final String COMMIT_EVENT = "commit";
	public static final String CANCEL_EVENT = "cancel";

	public MultiTreePopup(Map<String, Object> valueMap, Object[] defaultValue) {
		super();
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

	public JPanel createTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		List<SoProvinces> provinces = dataClient.getProvinces();
		for (SoProvinces soProvinces : provinces) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
					new TreeO(AddrType.PROVINCE, soProvinces.getProvinceid(), soProvinces.getProvince()));
			root.add(node1);
		}

		JTree tree = new JTree(root) {
			public Insets getInsets() {
				return new Insets(5, 5, 5, 5);
			}
		};
		tree.setRootVisible(false);
		tree.setEditable(false);
		tree.setCellRenderer(new MyDefaultTreeCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object source = e.getSource();
				TreePath path = e.getPath();
				DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (lastNode.getChildCount() > 0) {
					return;
				}
				TreeO userObject = (TreeO) lastNode.getUserObject();
				AddrType addrType = userObject.getAddrType();
				switch (addrType) {
				case PROVINCE:
					String key = userObject.getKey();
					List<SoCities> citiesIn = dataClient.getCitiesIn(key);
					for (SoCities soCities : citiesIn) {
						lastNode.add(new DefaultMutableTreeNode(
								new TreeO(AddrType.CITY, soCities.getCityid(), soCities.getCity())));
					}
					break;
				case CITY:
					key = userObject.getKey();
					List<SoAreas> areasIn = dataClient.getAreasIn(key);
					for (SoAreas areas : areasIn) {
						lastNode.add(new DefaultMutableTreeNode(
								new TreeO(AddrType.AREA, areas.getAreaid(), areas.getArea(), true)));
					}
					break;
				default:
					break;
				}
				System.out.println(paramString());
			}
		});

		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(new JLabel("项目列表", SwingConstants.CENTER), BorderLayout.NORTH);
		jp.add(new JScrollPane(tree), BorderLayout.CENTER);
		return jp;
	}

	private void initComponent() {
		JPanel checkboxPane = new JPanel();
		JPanel buttonPane = new JPanel();
		JScrollPane sp = new JScrollPane(checkboxPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(300, 300));
		this.setMaximumSize(new Dimension(300, 300));

		checkboxPane.setLayout(new BorderLayout());
		checkboxPane.add(createTree(), BorderLayout.CENTER);
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
		return selectedValues.toArray(new Object[selectedValues.size()]);
	}

	public List<String> getSelectedKeys() {
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

	class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			TreeO userObject = (TreeO) node.getUserObject();
			leaf = leaf && userObject.isArea();
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}

	}

	class TreeO {
		private String key;
		private Object value;
		private boolean isArea = false;
		private AddrType addrType;

		public TreeO(AddrType addrType, String key, Object object, boolean isArea) {
			super();
			this.addrType = addrType;
			this.key = key;
			this.value = object;
			this.isArea = isArea;
		}

		public AddrType getAddrType() {
			return addrType;
		}

		public TreeO(AddrType addrType, String key, Object object) {
			super();
			this.addrType = addrType;
			this.key = key;
			this.value = object;
		}

		public boolean isArea() {
			return isArea;
		}

		public void setArea(boolean isArea) {
			this.isArea = isArea;
		}

		public String getKey() {
			return this.key;
		}

		@Override
		public String toString() {
			return this.value.toString();
		}

	}
}
