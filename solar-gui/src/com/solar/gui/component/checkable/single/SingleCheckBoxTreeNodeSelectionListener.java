package com.solar.gui.component.checkable.single;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.solar.gui.component.checkable.multi.CheckBoxTreeNode;

public class SingleCheckBoxTreeNodeSelectionListener extends MouseAdapter {

	private List<CheckBoxTreeNode> checkedList = new ArrayList<>();

	public List<CheckBoxTreeNode> getCheckedList() {
		return checkedList;
	}

	/**
	 * 同一层级的只能选一个
	 * 
	 * @param node
	 * @param isSelected
	 */
	public void changeSelected(CheckBoxTreeNode node, boolean isSelected) {
		checkedList.clear();
		CheckBoxTreeNode parentNode = (CheckBoxTreeNode) node.getParent();
		if (parentNode != null) {
			Enumeration<CheckBoxTreeNode> checkAll = parentNode.children();
			while (checkAll.hasMoreElements()) {
				CheckBoxTreeNode nextElement = checkAll.nextElement();
				if (nextElement.isSelected())
					nextElement.setSelected(false);
				Enumeration<CheckBoxTreeNode> children = nextElement.children();
				if (children != null) {
					while (children.hasMoreElements()) {
						CheckBoxTreeNode nextElement2 = children.nextElement();
						if (nextElement2.isSelected())
							nextElement2.setSelected(false);
					}
				}
			}
		}
		node.setSelected(isSelected);
		if (isSelected) {
			checkedList.add(node);
		} else {
			checkedList.remove(node);
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