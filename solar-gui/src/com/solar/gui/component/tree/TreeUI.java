package com.solar.gui.component.tree;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.solar.client.ObservableMedia;
import com.solar.common.context.Consts.AddrType;
import com.solar.common.util.LocationLoader;
import com.solar.entity.SoProject;
import com.solar.gui.component.checkable.multi.CheckBoxTreeNode;
import com.solar.gui.component.model.TreeAddr;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

@SuppressWarnings("serial")
public class TreeUI {
	protected static final String LABEL_REFRESH = "刷新";
	protected static final String LABEL_UPDATA = "修改";

	public TreeUI() {
		super();
	}

	protected String getBoldRedHTML(String s) {
		return "<html><b><font size=\"4\" color=\"red\">" + s + "</font></b></html>";
	}

	protected String getBoldHTML(String s) {
		return "<html><b>" + s + "</b></html>";
	}

	List<CheckBoxTreeNode> multiSelectedPojectsNodes = new ArrayList<CheckBoxTreeNode>();
	List<CheckBoxTreeNode> multiSelectedAddressNodes = new ArrayList<CheckBoxTreeNode>();

	public List<CheckBoxTreeNode> getMultiSelectedPojectsNodes() {
		return multiSelectedPojectsNodes;
	}

	public void setMultiSelectedPojectsNodes(List<CheckBoxTreeNode> multiSelectedPojectsNodes) {
		this.multiSelectedPojectsNodes = multiSelectedPojectsNodes;
	}

	public List<CheckBoxTreeNode> getMultiSelectedAddressNodes() {
		return multiSelectedAddressNodes;
	}

	public void setMultiSelectedAddressNodes(List<CheckBoxTreeNode> multiSelectedAddressNodes) {
		this.multiSelectedAddressNodes = multiSelectedAddressNodes;
	}

	public void upState() {
		for (CheckBoxTreeNode checkBoxTreeNode : multiSelectedPojectsNodes) {
			checkBoxTreeNode.setSelected(true);
		}
		for (CheckBoxTreeNode checkBoxTreeNode : multiSelectedAddressNodes) {
			checkBoxTreeNode.setSelected(true);
		}
	}

	HierarchySingleCheckBoxTreeNode multiSeletedProjectNodes = new HierarchySingleCheckBoxTreeNode("root");
	JTree multiSeletedProjectTree;
	HierarchySingleCheckBoxTreeNode sumPowerNode = new HierarchySingleCheckBoxTreeNode(getBoldHTML("太阳能污水处理系统"));
	HierarchySingleCheckBoxTreeNode smartNode = new HierarchySingleCheckBoxTreeNode(getBoldHTML("智能运维系统"));

	public void updateProjectNodeSelect(Set<Long> initProjectIds) {
		this.multiSelectedPojectsNodes.clear();
		TreePath rootPath = new TreePath(multiSeletedProjectNodes);
		collapseAll(this.multiSeletedProjectTree, new TreePath(multiSeletedProjectNodes));
		expandProjectTree(initProjectIds, this.multiSeletedProjectTree, rootPath);
	}

	public void expandProjectTree(Set<Long> filterSet, JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandProjectTree(filterSet, tree, path);
				if (!(n.getUserObject() instanceof TreeAddr))
					continue;
				TreeAddr addr = (TreeAddr) n.getUserObject();
				if (addr.getAddrType().equals(AddrType.PROJECT)) {
					if (filterSet.contains(Long.parseLong(addr.getKey()))) {
						do {
							tree.expandPath(path);
							path = path.getParentPath();
						} while (path != null);
						if (n instanceof CheckBoxTreeNode)
							((CheckBoxTreeNode) n).setSelected(true);
						tree.setSelectionPath(path);
					}
				}
			}
		}
	}

	public JPanel createMultiSelProjectTree(String title, Set<Long> initProjectIds) {
		ObservableMedia instance = ObservableMedia.getInstance();
		if (initProjectIds == null)
			initProjectIds = Collections.emptySet();

		MutltOnlyCheckBoxTreeNodeSelectionListener checkBoxTreeNodeSelectionListener = new MutltOnlyCheckBoxTreeNodeSelectionListener(
				new HashSet<AddrType>() {
					{
						add(AddrType.PROJECT);
					}
				});

		Set<String> sunPowerFilterSet = instance.getSunPowerLocationFilterSet();
		Set<String> smartFilterSet = instance.getSmartLocationFilterSet();

		multiSeletedProjectNodes.add(sumPowerNode);
		multiSeletedProjectNodes.add(smartNode);

		JSONObject locations = LocationLoader.getInstance().loadLocation();
		JSONArray provinces = locations.getJSONArray("Province");
		for (int i = 0; i < provinces.size(); i++) {
			JSONObject province = provinces.getJSONObject(i);
			String provinceid = province.getString("Id");
			String provinceName = province.getString("Name");
			if (!sunPowerFilterSet.contains(provinceid))
				continue;
			HierarchySingleCheckBoxTreeNode node1 = new HierarchySingleCheckBoxTreeNode(
					new TreeAddr(AddrType.PROVINCE, provinceid, provinceName));
			sumPowerNode.add(node1);

			JSONArray cities = province.getJSONArray("City");
			for (int j = 0; j < cities.size(); j++) {
				JSONObject city = cities.getJSONObject(j);
				String cityid = city.getString("Id");
				String cityName = city.getString("Name");
				if (!sunPowerFilterSet.contains(cityid))
					continue;
				HierarchySingleCheckBoxTreeNode node2 = new HierarchySingleCheckBoxTreeNode(
						new TreeAddr(AddrType.CITY, cityid, cityName));
				node1.add(node2);

				JSONArray areas = city.getJSONArray("Area");
				for (int k = 0; k < areas.size(); k++) {
					JSONObject area = areas.getJSONObject(k);
					String areaid = area.getString("Id");
					String areaName = area.getString("Name");
					if (!sunPowerFilterSet.contains(areaid))
						continue;
					HierarchySingleCheckBoxTreeNode node3 = new HierarchySingleCheckBoxTreeNode(
							new TreeAddr(AddrType.AREA, areaid, areaName));
					node2.add(node3);

					List<SoProject> sunPowerProjects = instance.getSunPowerProjects(areaid);
					for (SoProject soProject : sunPowerProjects) {
						HierarchySingleCheckBoxTreeNode node4 = new HierarchySingleCheckBoxTreeNode(
								new TreeAddr(AddrType.PROJECT, Long.toString(soProject.getId()), soProject),
								multiSelectedPojectsNodes);
						if (initProjectIds.contains(soProject.getId())) {
							node4.setSelected(true);
						}
						node3.add(node4);
					}
				}
			}
		}

		for (int i = 0; i < provinces.size(); i++) {
			JSONObject province = provinces.getJSONObject(i);
			String provinceid = province.getString("Id");
			if (!smartFilterSet.contains(provinceid))
				continue;

			String provinceName = province.getString("Name");
			HierarchySingleCheckBoxTreeNode node1 = new HierarchySingleCheckBoxTreeNode(
					new TreeAddr(AddrType.PROVINCE, provinceid, provinceName));
			smartNode.add(node1);
			JSONArray cities = province.getJSONArray("City");
			for (int j = 0; j < cities.size(); j++) {
				JSONObject city = cities.getJSONObject(j);
				String cityid = city.getString("Id");
				if (!smartFilterSet.contains(cityid))
					continue;

				String cityName = city.getString("Name");
				HierarchySingleCheckBoxTreeNode node2 = new HierarchySingleCheckBoxTreeNode(
						new TreeAddr(AddrType.CITY, cityid, cityName));
				node1.add(node2);
				JSONArray areas = city.getJSONArray("Area");
				for (int k = 0; k < areas.size(); k++) {
					JSONObject area = areas.getJSONObject(k);
					String areaid = area.getString("Id");
					if (!smartFilterSet.contains(areaid))
						continue;

					String areaName = area.getString("Name");
					HierarchySingleCheckBoxTreeNode node3 = new HierarchySingleCheckBoxTreeNode(
							new TreeAddr(AddrType.AREA, areaid, areaName));
					node2.add(node3);

					List<SoProject> smartProjects = instance.getSmartProjects(areaid);
					for (SoProject soProject : smartProjects) {
						HierarchySingleCheckBoxTreeNode node4 = new HierarchySingleCheckBoxTreeNode(
								new TreeAddr(AddrType.PROJECT, Long.toString(soProject.getId()), soProject),
								multiSelectedPojectsNodes);
						if (initProjectIds.contains(soProject.getId())) {
							node4.setSelected(true);
						}
						node3.add(node4);
					}
				}
			}
		}
		// List<SoProvinces> provinces = dataClient.getProvinces();
		// for (SoProvinces soProvinces : provinces) {
		// MyCheckBoxTreeNode node1 = new MyCheckBoxTreeNode(
		// new TreeAddr(AddrType.PROVINCE, soProvinces.getProvinceid(),
		// soProvinces.getProvince()));
		// root.add(node1);
		// }

		multiSeletedProjectTree = new JTree(multiSeletedProjectNodes) {
			public Insets getInsets() {
				return new Insets(5, 5, 5, 5);
			}
		};
		multiSeletedProjectTree.setRootVisible(false);
		multiSeletedProjectTree.setEditable(false);
		multiSeletedProjectTree.setCellRenderer(new CheckBoxTreeCellRenderer());
		// tree.addTreeSelectionListener(new TreeSelectionListener() {
		// @Override
		// public void valueChanged(TreeSelectionEvent e) {
		// TreePath path = e.getPath();
		// MyCheckBoxTreeNode lastNode = (MyCheckBoxTreeNode)
		// path.getLastPathComponent();
		// if (lastNode.getChildCount() > 0) {
		// return;
		// }
		// TreeAddr userObject = (TreeAddr) lastNode.getUserObject();
		// AddrType addrType = userObject.getAddrType();
		// switch (addrType) {
		// case PROVINCE:
		// String key = userObject.getKey();
		// List<SoCities> citiesIn = dataClient.getCitiesIn(key);
		// for (SoCities soCities : citiesIn) {
		// lastNode.add(new MyCheckBoxTreeNode(
		// new TreeAddr(AddrType.CITY, soCities.getCityid(), soCities.getCity())));
		// }
		// break;
		// case CITY:
		// key = userObject.getKey();
		// List<SoAreas> areasIn = dataClient.getAreasIn(key);
		// for (SoAreas areas : areasIn) {
		// lastNode.add(new MyCheckBoxTreeNode(
		// new TreeAddr(AddrType.AREA, areas.getAreaid(), areas.getArea(), true)));
		// }
		// break;
		// case AREA:
		// break;
		// default:
		// break;
		// }
		// // System.out.println(paramString());
		// }
		// });
		multiSeletedProjectTree.addMouseListener(checkBoxTreeNodeSelectionListener);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(title), BorderLayout.NORTH);
		panel.add(multiSeletedProjectTree, BorderLayout.CENTER);
		return panel;
	}

	public JTree createSingleSelProjectTree() {
		// 如果是管理员则加载所有地址
		// 如果是维护或局方则查询后台,或者在登陆的时候保存管辖位置数据
		ObservableMedia instance = ObservableMedia.getInstance();

		Set<String> sunPowerFilterSet = instance.getSunPowerLocationFilterSet();
		Set<String> smartFilterSet = instance.getSmartLocationFilterSet();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultMutableTreeNode sumPowerNode = new DefaultMutableTreeNode(getBoldHTML("太阳能污水处理系统"));
		DefaultMutableTreeNode smartNode = new DefaultMutableTreeNode(getBoldHTML("智能运维系统"));
		root.add(sumPowerNode);
		root.add(smartNode);

		JSONObject locations = LocationLoader.getInstance().loadLocation();
		JSONArray provinces = locations.getJSONArray("Province");
		for (int i = 0; i < provinces.size(); i++) {
			JSONObject province = provinces.getJSONObject(i);
			String provinceid = province.getString("Id");
			if (!sunPowerFilterSet.contains(provinceid))
				continue;

			String provinceName = province.getString("Name");
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
					new TreeAddr(AddrType.PROVINCE, provinceid, provinceName));
			sumPowerNode.add(node1);
			JSONArray cities = province.getJSONArray("City");
			for (int j = 0; j < cities.size(); j++) {
				JSONObject city = cities.getJSONObject(j);
				String cityid = city.getString("Id");
				if (!sunPowerFilterSet.contains(cityid))
					continue;

				String cityName = city.getString("Name");
				DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
						new TreeAddr(AddrType.CITY, cityid, cityName));
				node1.add(node2);

				JSONArray areas = city.getJSONArray("Area");
				for (int k = 0; k < areas.size(); k++) {
					JSONObject area = areas.getJSONObject(k);
					String areaid = area.getString("Id");
					if (!sunPowerFilterSet.contains(areaid))
						continue;

					String areaName = area.getString("Name");
					DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(
							new TreeAddr(AddrType.AREA, areaid, areaName));
					node2.add(node3);

					List<SoProject> sunPowerProjects = instance.getSunPowerProjects(areaid);
					for (SoProject soProject : sunPowerProjects) {
						DefaultMutableTreeNode node4 = new DefaultMutableTreeNode(
								new TreeAddr(AddrType.PROJECT, Long.toString(soProject.getId()), soProject));
						node3.add(node4);
					}
				}
			}
		}

		for (int i = 0; i < provinces.size(); i++) {
			JSONObject province = provinces.getJSONObject(i);
			String provinceid = province.getString("Id");
			if (!smartFilterSet.contains(provinceid))
				continue;

			String provinceName = province.getString("Name");
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(
					new TreeAddr(AddrType.PROVINCE, provinceid, provinceName));
			smartNode.add(node1);
			JSONArray cities = province.getJSONArray("City");
			for (int j = 0; j < cities.size(); j++) {
				JSONObject city = cities.getJSONObject(j);
				String cityid = city.getString("Id");
				if (!smartFilterSet.contains(cityid))
					continue;

				String cityName = city.getString("Name");
				DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
						new TreeAddr(AddrType.CITY, cityid, cityName));
				node1.add(node2);
				JSONArray areas = city.getJSONArray("Area");
				for (int k = 0; k < areas.size(); k++) {
					JSONObject area = areas.getJSONObject(k);
					String areaid = area.getString("Id");
					if (!smartFilterSet.contains(areaid))
						continue;

					String areaName = area.getString("Name");
					DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(
							new TreeAddr(AddrType.AREA, areaid, areaName));
					node2.add(node3);

					List<SoProject> smartProjects = instance.getSmartProjects(areaid);
					for (SoProject soProject : smartProjects) {
						DefaultMutableTreeNode node4 = new DefaultMutableTreeNode(
								new TreeAddr(AddrType.PROJECT, Long.toString(soProject.getId()), soProject));
						node3.add(node4);
					}
				}
			}
		}

		JTree tree = new JTree(root) {
			public Insets getInsets() {
				return new Insets(5, 5, 5, 5);
			}
		};
		tree.setRootVisible(false);
		tree.setEditable(false);

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				Object lastPathComponent = path.getLastPathComponent();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
				Object userObject = node.getUserObject();
				if (userObject instanceof TreeAddr) {
					TreeAddr ta = (TreeAddr) userObject;
					AddrType addrType = ta.getAddrType();
					String id = ta.getKey();
					Object value = ta.getValue();

					switch (addrType) {
					case PROJECT:
						// Load device
						// node.add
						break;
					case DEVICE:
						// monitor device

						break;
					default:
						break;
					}
				}
			}
		});

		return tree;
	}

	HierarchySingleCheckBoxTreeNode multiAddressRoot = new HierarchySingleCheckBoxTreeNode("root");
	JTree multiAddressTree;

	public void updateAddressNodeSelect(Set<String> initAddressIds) {
		this.multiSelectedAddressNodes.clear();
		TreePath root = new TreePath(this.multiAddressRoot);
		collapseAll(multiAddressTree, root);
		expandAddressTree(initAddressIds, this.multiAddressTree, root);
	}

	public void collapseAll(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				CheckBoxTreeNode n = (CheckBoxTreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				collapseAll(tree, path);
				n.setSelected(false);
				tree.collapsePath(path);
			}
		}
	}

	public void expandAddressTree(Set<String> filterSet, JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
				TreeAddr addr = (TreeAddr) n.getUserObject();
				TreePath path = parent.pathByAddingChild(n);
				expandAddressTree(filterSet, tree, path);
				if (filterSet.contains(addr.getKey())) {
					do {
						tree.expandPath(path);
						path = path.getParentPath();
					} while (path != null);
					if (n instanceof CheckBoxTreeNode)
						((CheckBoxTreeNode) n).setSelected(true);
					tree.setSelectionPath(path);
				}
			}
		}
	}

	public JPanel createMultiAddressTree(String title, Set<String> initAddressIds) {
		JPanel jp = new JPanel();
		if (initAddressIds == null)
			initAddressIds = Collections.emptySet();
		MutltOnlyCheckBoxTreeNodeSelectionListener checkBoxTreeNodeSelectionListener = new MutltOnlyCheckBoxTreeNodeSelectionListener(
				new HashSet<AddrType>() {
					{
						super.add(AddrType.AREA);
						super.add(AddrType.CITY);
						super.add(AddrType.PROVINCE);
					}
				});

		JSONObject locations = LocationLoader.getInstance().loadLocation();
		JSONArray provinces = locations.getJSONArray("Province");
		for (int i = 0; i < provinces.size(); i++) {
			JSONObject province = provinces.getJSONObject(i);
			String provinceid = province.getString("Id");
			String provinceName = province.getString("Name");
			HierarchySingleCheckBoxTreeNode node1 = new HierarchySingleCheckBoxTreeNode(
					new TreeAddr(AddrType.PROVINCE, provinceid, provinceName), multiSelectedAddressNodes);

			if (initAddressIds.contains(provinceid))
				node1.setSelected(true);

			multiAddressRoot.add(node1);
			JSONArray cities = province.getJSONArray("City");
			for (int j = 0; j < cities.size(); j++) {
				JSONObject city = cities.getJSONObject(j);
				String cityid = city.getString("Id");
				String cityName = city.getString("Name");
				HierarchySingleCheckBoxTreeNode node2 = new HierarchySingleCheckBoxTreeNode(
						new TreeAddr(AddrType.CITY, cityid, cityName), multiSelectedAddressNodes);

				if (initAddressIds.contains(cityid))
					node2.setSelected(true);

				node1.add(node2);
				JSONArray areas = city.getJSONArray("Area");
				for (int k = 0; k < areas.size(); k++) {
					JSONObject area = areas.getJSONObject(k);
					String areaid = area.getString("Id");
					String areaName = area.getString("Name");
					HierarchySingleCheckBoxTreeNode node3 = new HierarchySingleCheckBoxTreeNode(
							new TreeAddr(AddrType.AREA, areaid, areaName), multiSelectedAddressNodes);

					if (initAddressIds.contains(areaid))
						node3.setSelected(true);

					node2.add(node3);
				}
			}
		}

		multiAddressTree = new JTree(multiAddressRoot) {
			public Insets getInsets() {
				return new Insets(5, 5, 5, 5);
			}
		};
		multiAddressTree.addMouseListener(checkBoxTreeNodeSelectionListener);
		multiAddressTree.setCellRenderer(new CheckBoxTreeCellRenderer());
		multiAddressTree.setRootVisible(false);
		multiAddressTree.setEditable(false);

		jp.setLayout(new BorderLayout());
		jp.add(new JLabel(title, SwingConstants.LEFT), BorderLayout.NORTH);
		jp.add(new JScrollPane(multiAddressTree), BorderLayout.CENTER);
		return jp;
	}

	public JPanel createSingleAddressTree() {
		JPanel jp = new JPanel();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
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
							new TreeAddr(AddrType.AREA, areaid, areaName));
					node2.add(node3);
				}
			}
		}

		JTree tree = new JTree(root) {
			public Insets getInsets() {
				return new Insets(5, 5, 5, 5);
			}
		};
		tree.setRootVisible(false);
		tree.setEditable(false);

		jp.setLayout(new BorderLayout());
		jp.add(new JLabel("地址列表", SwingConstants.CENTER), BorderLayout.NORTH);
		jp.add(new JScrollPane(tree), BorderLayout.CENTER);
		return jp;
	}

}
