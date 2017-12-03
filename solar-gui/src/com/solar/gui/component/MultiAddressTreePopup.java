package com.solar.gui.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.solar.client.DataClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.Consts.AddrType;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoProvinces;
import com.solar.gui.component.checkable.CheckBoxTreeCellRenderer;
import com.solar.gui.component.checkable.multi.CheckBoxTreeNode;
import com.solar.gui.component.checkable.multi.MutltOnlyCheckBoxTreeNodeSelectionListener;
import com.solar.gui.component.model.TreeAddr;

@SuppressWarnings("serial")
public class MultiAddressTreePopup extends JPopupMenu {
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	MutltOnlyCheckBoxTreeNodeSelectionListener checkBoxTreeNodeSelectionListener = new MutltOnlyCheckBoxTreeNodeSelectionListener();

	private List<TreeAddr> selectedKeys = new ArrayList<TreeAddr>(1);

	private List<CheckBoxTreeNode> selectedNodes = new ArrayList<CheckBoxTreeNode>(10);

	private DataClient dataClient = new DataClient(NetConf.buildHostConf());

	private JButton commitButton;
	private JButton cancelButton;
	public static final String COMMIT_EVENT = "commit";
	public static final String CANCEL_EVENT = "cancel";

	public MultiAddressTreePopup(Map<String, Object> valueMap, Object[] defaultValue) {
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
		MyCheckBoxTreeNode root = new MyCheckBoxTreeNode("root");
		List<SoProvinces> provinces = dataClient.getProvinces();
		for (SoProvinces soProvinces : provinces) {
			MyCheckBoxTreeNode node1 = new MyCheckBoxTreeNode(
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
		tree.setCellRenderer(new CheckBoxTreeCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				MyCheckBoxTreeNode lastNode = (MyCheckBoxTreeNode) path.getLastPathComponent();
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
						lastNode.add(new MyCheckBoxTreeNode(
								new TreeAddr(AddrType.CITY, soCities.getCityid(), soCities.getCity())));
					}
					break;
				case CITY:
					key = userObject.getKey();
					List<SoAreas> areasIn = dataClient.getAreasIn(key);
					for (SoAreas areas : areasIn) {
						lastNode.add(new MyCheckBoxTreeNode(
								new TreeAddr(AddrType.AREA, areas.getAreaid(), areas.getArea(), true)));
					}
					break;
				case AREA:
					break;
				default:
					break;
				}
				System.out.println(paramString());
			}
		});

		tree.addMouseListener(checkBoxTreeNodeSelectionListener);

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
		for (CheckBoxTreeNode checkBoxTreeNode : selectedNodes) {
			TreeAddr sn = (TreeAddr) checkBoxTreeNode.getUserObject();
			selectedKeys.add(sn);

			StringBuilder sb = new StringBuilder();
			TreeNode[] path = checkBoxTreeNode.getPath();
			for (TreeNode treeNode : path) {
				MyCheckBoxTreeNode node = (MyCheckBoxTreeNode) treeNode;
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

	class MyCheckBoxTreeNode extends CheckBoxTreeNode {
		private static final long serialVersionUID = 1L;

		public MyCheckBoxTreeNode(TreeAddr treeAddr) {
			super(treeAddr);
		}

		public MyCheckBoxTreeNode(String name) {
			super(name);
		}

		public void disChildSelect(Enumeration children) {
			if (children != null) {
				while (children.hasMoreElements()) {
					CheckBoxTreeNode node = (CheckBoxTreeNode) children.nextElement();
					selectedNodes.remove(node);
					if (node.isSelected())
						node.setSelected(false);
					disChildSelect(node.children());
				}
			}
		}

		public void disParentSelect(CheckBoxTreeNode parent) {
			if (parent != null) {
				if (parent.isSelected()) {
					parent.setSelected(false);
				}
				selectedNodes.remove(parent);
				MyCheckBoxTreeNode pNode = (MyCheckBoxTreeNode) parent.getParent();
				disParentSelect(pNode);
			}
		}

		public void setSelected(boolean _isSelected) {
			this.isSelected = _isSelected;
			if (_isSelected) {
				selectedNodes.add(this);
				// 如果选中，则将其所有的子结点都选中
				disChildSelect(children());

				// 向上检查，如果父结点的所有子结点都被选中，那么将父结点也选中
				MyCheckBoxTreeNode pNode = (MyCheckBoxTreeNode) parent;
				disParentSelect(pNode);
			} else {
				selectedNodes.remove(this);
			}

		}

		public boolean isLeaf() {
			Object o = super.getUserObject();
			if (!o.toString().equals("root")) {
				TreeAddr treeAddr = (TreeAddr) super.getUserObject();
				AddrType addrType = treeAddr.getAddrType();
				switch (addrType) {
				case PROVINCE:
				case CITY:
					return false;
				default:
					break;
				}
			}
			return (getChildCount() == 0);
		}

	}
}
