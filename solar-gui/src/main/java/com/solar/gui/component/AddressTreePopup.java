package com.solar.gui.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.solar.client.DataClient;
import com.solar.client.net.NetConf;
import com.solar.common.context.Consts.AddrType;
import com.solar.common.util.LocationLoader;
import com.solar.entity.SoAreas;
import com.solar.entity.SoCities;
import com.solar.entity.SoProject;
import com.solar.entity.SoProvinces;
import com.solar.gui.component.checkable.single.SingleTreeNodeSelectionListener;
import com.solar.gui.component.model.TreeAddr;

@SuppressWarnings("serial")
public class AddressTreePopup extends JPopupMenu {
	private List<ActionListener> listeners = new ArrayList<ActionListener>();
	private List<TreeAddr> selectedKeys = new ArrayList<TreeAddr>(1);
	private List<DefaultMutableTreeNode> selectedNodes = new ArrayList<DefaultMutableTreeNode>(1);

	SingleTreeNodeSelectionListener listener = new SingleTreeNodeSelectionListener(selectedNodes);

	private DataClient dataClient = new DataClient(NetConf.buildHostConf());

	private JButton commitButton;
	private JButton cancelButton;
	public static final String COMMIT_EVENT = "commit";
	public static final String CANCEL_EVENT = "cancel";

	private boolean leafOnly = true;

	public AddressTreePopup(Map<String, Object> valueMap, Object[] defaultValue, boolean leafOnly) {
		super();
		this.leafOnly = leafOnly;
		initComponent();
	}

	public AddressTreePopup(Map<String, Object> valueMap, Object[] defaultValue) {
		this(valueMap, defaultValue, true);
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

	JTree tree;
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

	public void createNodeMode() {
		JSONObject locations = LocationLoader.getInstance().loadLocation();
		JSONArray provinces = locations.getJSONArray("Province");
		for (int i = 0; i < provinces.size(); i++) {
			JSONObject province = provinces.getJSONObject(i);
			String provinceid = province.getString("Id");
			String provinceName = province.getString("Name");
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
					new TreeAddr(AddrType.PROVINCE, provinceid, provinceName));
			root.add(node1);
			JSONArray cities = province.getJSONArray("City");
			for (int j = 0; j < cities.size(); j++) {
				JSONObject city = cities.getJSONObject(j);
				String cityid = city.getString("Id");

				String cityName = city.getString("Name");
				DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
						new TreeAddr(AddrType.CITY, cityid, cityName));
				node1.add(node2);

				JSONArray areas = city.getJSONArray("Area");
				for (int k = 0; k < areas.size(); k++) {
					JSONObject area = areas.getJSONObject(k);
					String areaid = area.getString("Id");
					String areaName = area.getString("Name");
					DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(
							new TreeAddr(AddrType.AREA, areaid, areaName, true));
					node2.add(node3);
				}
			}
		}
	}

	public void createNodeModelUnSyc() {
		List<SoProvinces> provinces = dataClient.getProvinces();
		for (SoProvinces soProvinces : provinces) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
					new TreeAddr(AddrType.PROVINCE, soProvinces.getProvinceid(), soProvinces.getProvince()));
			root.add(node1);
		}

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
					// selectedNodes.clear();
					// selectedNodes.add(lastNode);
					break;
				default:
					break;
				}
				System.out.println(paramString());
			}
		});
	}

	public JPanel createTree() {
		createNodeMode();
		tree = new JTree(root) {
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
					break;
				case CITY:
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
		if (!leafOnly)
			tree.addMouseListener(listener);

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
				sb.append(treeAddr.getValue()).append("\\");
			}
			selectedValues.add(sb.substring(0, sb.length() - 1));
		}
		return selectedValues.toArray(new Object[selectedValues.size()]);
	}

	public List<TreeAddr> getSelectedKeys() {
		return selectedKeys;
	}

	public void expandTree(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
				TreeAddr addr = (TreeAddr) n.getUserObject();
				TreePath path = parent.pathByAddingChild(n);
				expandTree(tree, path);
				if (locationSet.contains(addr.getKey())) {
					tree.setSelectionPath(path);
					do {
						tree.expandPath(path);
						path = path.getParentPath();
					} while (path != null);
				}
			}
		}
	}

	private Set<String> locationSet = new HashSet<>();

	public void setDefaultLocations(List<TreeAddr> selectedKeys) {
		this.selectedKeys.clear();
		this.locationSet.clear();
		for (TreeAddr treeAddr : selectedKeys) {
			String key = treeAddr.getKey();
			this.locationSet.add(key);
			this.selectedKeys.add(treeAddr);
		}
		TreePath root = new TreePath(this.root);
		expandTree(tree, root);
	}

	public void setDefaultValue(Object[] defaultValue) {
	}

	public void commit() {
		fireActionPerformed(new ActionEvent(this, 0, COMMIT_EVENT));
	}

	public void cancel() {
		fireActionPerformed(new ActionEvent(this, 0, CANCEL_EVENT));
	}

	public static class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object uo = node.getUserObject();
			if (uo instanceof TreeAddr) {
				TreeAddr userObject = (TreeAddr) uo;
				leaf = leaf && userObject.isArea();
			}
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}

	}
}
