package com.solar.gui.component.checkable.single;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.solar.gui.component.checkable.multi.CheckBoxTreeNode;

public class SingleTreeNodeSelectionListener extends MouseAdapter {

	private List<DefaultMutableTreeNode> checkedList;
 
	public SingleTreeNodeSelectionListener(List<DefaultMutableTreeNode> selectedNodes) {
		checkedList = selectedNodes;
	}

	public List<DefaultMutableTreeNode> getCheckedList() {
		return checkedList;
	}

	/**
	 * 同一层级的只能选一个
	 * 
	 * @param node
	 * @param isSelected
	 */
	public void changeSelected(DefaultMutableTreeNode node, boolean isSelected) {
		checkedList.clear();
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
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			if (node != null) {
				boolean isSelected = tree.isPathSelected(path);
				changeSelected(node, isSelected);
				((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
			}
		}
	}
}