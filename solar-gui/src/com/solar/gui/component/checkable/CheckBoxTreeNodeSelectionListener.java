package com.solar.gui.component.checkable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {

	private List<CheckBoxTreeNode> checkedList = new ArrayList<>();

	public List<CheckBoxTreeNode> getCheckedList() {
		return checkedList;
	}

	public void changeSelected(CheckBoxTreeNode node, boolean isSelected) {
		node.setSelected(isSelected);
		if (node.isLeaf()) {
			if (isSelected) {
				checkedList.add(node);
			} else {
				checkedList.remove(node);
			}
		}
		Enumeration<CheckBoxTreeNode> children = node.children();
		while (children.hasMoreElements()) {
			CheckBoxTreeNode item = children.nextElement();
			if (item.isSelected() != isSelected) {
				item.setSelected(isSelected);
			}
			if (isSelected) {
				checkedList.add(item);
			} else {
				checkedList.remove(item);
			}
		}
		CheckBoxTreeNode parentNode = (CheckBoxTreeNode) node.getParent();
		if (parentNode != null) {
			int v_count = parentNode.getChildCount();
			Enumeration<CheckBoxTreeNode> checkAll = parentNode.children();
			while (checkAll.hasMoreElements()) {
				if (checkAll.nextElement().isSelected())
					v_count--;
			}
			if (v_count == parentNode.getChildCount() || v_count == 0) {
				parentNode.setSelected(isSelected);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		JTree tree = (JTree) event.getSource();
		int x = event.getX();
		int y = event.getY();
		int row = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForRow(row);
		if (path != null) {
			CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
			if (node != null) {
				boolean isSelected = !node.isSelected();
				node.setSelected(isSelected);
				changeSelected(node, isSelected);
				((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
			}
		}
	}
}