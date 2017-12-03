package com.solar.gui.component.checkable.multi;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MutltOnlyCheckBoxTreeNodeSelectionListener extends MouseAdapter {

	private List<CheckBoxTreeNode> checkedList = new ArrayList<>();

	public List<CheckBoxTreeNode> getCheckedList() {
		return checkedList;
	}

	public void changeSelected(CheckBoxTreeNode node, boolean isSelected) {
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