package com.solar.gui.module.working;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.solar.client.ObservableMedia;
import com.solar.common.context.Consts.AddrType;
import com.solar.common.util.LocationLoader;
import com.solar.gui.component.model.TreeAddr;

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
		//btn.setBounds(new Rectangle(0, 0, 30, 30));
		//btn.setPreferredSize(new Dimension(40, 30));
		// Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		// delBtn.setFont(font);
		// delBtn.setBorder(new EmptyBorder(0, 0, 40, 0));
		// btn.setBorder(BorderFactory.createRaisedBevelBorder());
		btn.setText(text);
		return btn;
	}

	public JPanel createTree() {
		// 如果是管理员则加载所有地址
		// 如果是维护或局方则查询后台,或者在登陆的时候保存管辖位置数据
		JPanel jp = new JPanel();
		Set<String> sunPowerFilterSet = ObservableMedia.getInstance().getSunPowerLocationFilterSet();
		Set<String> smartFilterSet = ObservableMedia.getInstance().getSmartLocationFilterSet();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultMutableTreeNode sumPowerNode = new DefaultMutableTreeNode(getBoldHTML("太阳能污水处理系统"));
		DefaultMutableTreeNode smartNode = new DefaultMutableTreeNode(getBoldHTML("智能运维系统"));
		root.add(sumPowerNode);
		root.add(smartNode);

		JSONObject locations = LocationLoader.loadLocation();
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
				}
			}
		}

		tree = new JTree(root) {
			public Insets getInsets() {
				return new Insets(5, 5, 5, 5);
			}
		};
		tree.setRootVisible(false);
		tree.setEditable(false);

		jp.setLayout(new BorderLayout());
		jp.add(new JLabel("项目列表", SwingConstants.CENTER), BorderLayout.NORTH);
		jp.add(new JScrollPane(tree), BorderLayout.CENTER);
		return jp;
	}

	public void showErrorDailog(String title, String msg) {
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	public void showWarningDailog(String title, String msg) {
		JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
	}
}
