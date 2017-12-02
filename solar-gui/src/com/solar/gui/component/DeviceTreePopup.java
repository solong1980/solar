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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.solar.common.context.Consts.DevInfoType;
import com.solar.entity.SoDevConfig;
import com.solar.gui.component.checkable.CheckBoxTreeCellRenderer;
import com.solar.gui.component.checkable.CheckBoxTreeNode;
import com.solar.gui.component.checkable.CheckBoxTreeNodeSelectionListener;

@SuppressWarnings("serial")
public class DeviceTreePopup extends JPopupMenu {
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	private List<SoDevConfig> selectedKeys = new ArrayList<SoDevConfig>();

	private int[] devTypes = new int[] { 10, 20, 30, 40 };
	private String[] devices = new String[] { "太阳能板", "电池", "曝气系统", "太阳能控制器" };
	private int[][] devCountOpts = new int[][] { { 2, 4, 6, 8, 16, 24, 32 }, { 2, 4, 6, 8, 16, 24, 32 },
			{ 2, 2, 3, 3, 6, 9, 12 }, { 1, 1, 1, 2, 4, 6, 8 } };
	private JButton commitButton;
	private JButton cancelButton;
	public static final String COMMIT_EVENT = "commit";
	public static final String CANCEL_EVENT = "cancel";

	CheckBoxTreeNodeSelectionListener checkBoxTreeNodeSelectionListener = new CheckBoxTreeNodeSelectionListener();

	public DeviceTreePopup(Map<String, Object> valueMap, Object[] defaultValue) {
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

	private static void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}
		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	public JPanel createTree() {
		CheckBoxTreeNode root = new CheckBoxTreeNode("root");
		for (int i = 0; i < devices.length; i++) {
			CheckBoxTreeNode node1 = new CheckBoxTreeNode(new TreeDev(DevInfoType.DEV_TYPE, devTypes[i], devices[i]));
			int[] devOpt = devCountOpts[i];
			for (int j = 0; j < devOpt.length; j++) {
				// DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
				// new TreeDev(DevType.SOLAR_BOARD, devOpt[j], devOpt[j] + "个", true));
				//
				CheckBoxTreeNode node2 = new CheckBoxTreeNode(
						new TreeDev(DevInfoType.DEV_CONFIG, devOpt[j], devOpt[j] + "个", true));
				node1.add(node2);
			}
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
		TreeNode troot = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, new TreePath(troot), true);
		// tree.addTreeSelectionListener(new TreeSelectionListener() {
		// @Override
		// public void valueChanged(TreeSelectionEvent e) {
		// Object source = e.getSource();
		// TreePath path = e.getPath();
		// DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode)
		// path.getLastPathComponent();
		// if (lastNode.getChildCount() > 0) {
		// return;
		// }
		// TreeDev userObject = (TreeDev) lastNode.getUserObject();
		// System.out.println(paramString());
		// }
		// });
		tree.addMouseListener(checkBoxTreeNodeSelectionListener);

		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(new JLabel("设备配置", SwingConstants.CENTER), BorderLayout.NORTH);
		jp.add(new JScrollPane(tree), BorderLayout.CENTER);
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
		List<CheckBoxTreeNode> checkedList = checkBoxTreeNodeSelectionListener.getCheckedList();
		for (CheckBoxTreeNode checkBoxTreeNode : checkedList) {
			StringBuilder sb = new StringBuilder();
			SoDevConfig soDevConfig = new SoDevConfig();
			TreeNode[] path = checkBoxTreeNode.getPath();
			for (TreeNode treeNode : path) {
				CheckBoxTreeNode boxTreeNode = (CheckBoxTreeNode) treeNode;
				if (boxTreeNode.getUserObject().toString().equals("root"))
					continue;
				TreeDev treeDev = (TreeDev) boxTreeNode.getUserObject();
				DevInfoType devInfoType = treeDev.getDevInfoType();
				switch (devInfoType) {
				case DEV_TYPE:
					soDevConfig.setDevType(treeDev.getKey());
					break;
				case DEV_CONFIG:
					soDevConfig.setConfig(Integer.toString(treeDev.getKey()));
					break;
				default:
					break;
				}
				sb.append(treeDev.getValue()).append("->");
			}
			System.out.println(sb);
			selectedKeys.add(soDevConfig);
			selectedValues.add(sb);
		}
		return selectedValues.toArray(new Object[selectedValues.size()]);
	}

	public List<SoDevConfig> getSelectedKeys() {
		return selectedKeys;
	}

	public void commit() {
		fireActionPerformed(new ActionEvent(this, 0, COMMIT_EVENT));
	}

	public void cancel() {
		fireActionPerformed(new ActionEvent(this, 0, CANCEL_EVENT));
	}

	class TreeDev {
		private int key;
		private Object value;
		private boolean isLeaf = false;
		private DevInfoType devInfoType;

		public TreeDev(DevInfoType devInfoType, int key, Object object, boolean isLeaf) {
			super();
			this.devInfoType = devInfoType;
			this.key = key;
			this.value = object;
			this.isLeaf = isLeaf;
		}

		public Object getValue() {
			return value;
		}

		public DevInfoType getDevInfoType() {
			return devInfoType;
		}

		public TreeDev(DevInfoType devInfoType, int key, Object object) {
			super();
			this.devInfoType = devInfoType;
			this.key = key;
			this.value = object;
		}

		public boolean isLeaf() {
			return isLeaf;
		}

		public int getKey() {
			return this.key;
		}

		@Override
		public String toString() {
			return this.value.toString();
		}

	}

	public void setDefaultValue(Object[] defaultValues) {

	}

	public void cleanSelected() {
		this.selectedKeys.clear();
		List<CheckBoxTreeNode> checkedList = this.checkBoxTreeNodeSelectionListener.getCheckedList();
		for (CheckBoxTreeNode checkBoxTreeNode : checkedList) {
			checkBoxTreeNode.setSelected(false);
		}
		checkedList.clear();
	}
}
