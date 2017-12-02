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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.solar.client.DataClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.Consts.AddrType;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoProvinces;
import com.solar.gui.component.model.TreeAddr;

@SuppressWarnings("serial")
public class AddressTreePopup extends JPopupMenu {
	private List<ActionListener> listeners = new ArrayList<ActionListener>();

	private List<TreeAddr> selectedKeys = new ArrayList<TreeAddr>(1);

	private List<DefaultMutableTreeNode> selectedNodes = new ArrayList<DefaultMutableTreeNode>(1);
	private DataClient dataClient = new DataClient(NetConf.buildHostConf());

	private JButton commitButton;
	private JButton cancelButton;
	public static final String COMMIT_EVENT = "commit";
	public static final String CANCEL_EVENT = "cancel";

	public AddressTreePopup(Map<String, Object> valueMap, Object[] defaultValue) {
		super();
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

	public void cleanSelected() {
		selectedNodes.clear();
		selectedKeys.clear();
	}

	public JPanel createTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		List<SoProvinces> provinces = dataClient.getProvinces();
		for (SoProvinces soProvinces : provinces) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
					new TreeAddr(AddrType.PROVINCE, soProvinces.getProvinceid(), soProvinces.getProvince()));
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
				TreePath path = e.getPath();
				DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (lastNode.getChildCount() > 0) {
					return;
				}
				TreeAddr userObject = (TreeAddr) lastNode.getUserObject();
				AddrType addrType = userObject.getAddrType();
				switch (addrType) {
				case PROVINCE:
					String key = userObject.getKey();
					List<SoCities> citiesIn = dataClient.getCitiesIn(key);
					for (SoCities soCities : citiesIn) {
						lastNode.add(new DefaultMutableTreeNode(
								new TreeAddr(AddrType.CITY, soCities.getCityid(), soCities.getCity())));
					}
					break;
				case CITY:
					key = userObject.getKey();
					List<SoAreas> areasIn = dataClient.getAreasIn(key);
					for (SoAreas areas : areasIn) {
						lastNode.add(new DefaultMutableTreeNode(
								new TreeAddr(AddrType.AREA, areas.getAreaid(), areas.getArea(), true)));
					}
					break;
				case AREA:
					selectedNodes.clear();
					selectedNodes.add(lastNode);
					break;
				default:
					break;
				}
				System.out.println(paramString());
			}
		});
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(new JLabel("项目地址", SwingConstants.CENTER), BorderLayout.NORTH);
		jp.add(tree, BorderLayout.CENTER);
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

	protected void fireActionPerformed(ActionEvent e) {
		for (ActionListener l : listeners) {
			l.actionPerformed(e);
		}
	}

	public Object[] getSelectedValues() {
		List<Object> selectedValues = new ArrayList<Object>();
		selectedKeys.clear();

		for (DefaultMutableTreeNode mutableTreeNode : selectedNodes) {
			TreeAddr sn = (TreeAddr) mutableTreeNode.getUserObject();
			selectedKeys.add(sn);

			StringBuilder sb = new StringBuilder();
			TreeNode[] path = mutableTreeNode.getPath();
			for (TreeNode treeNode : path) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode;
				if (node.getUserObject().toString().equals("root"))
					continue;
				TreeAddr treeAddr = (TreeAddr) node.getUserObject();
				sb.append(treeAddr.getValue()).append("->");
			}
			System.out.println(sb);
			selectedValues.add(sb);
		}
		return selectedValues.toArray(new Object[selectedValues.size()]);
	}

	public List<TreeAddr> getSelectedKeys() {
		return selectedKeys;
	}

	public void setDefaultValue(Object[] defaultValue) {
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
			TreeAddr userObject = (TreeAddr) node.getUserObject();
			leaf = leaf && userObject.isArea();
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}

	}
}
