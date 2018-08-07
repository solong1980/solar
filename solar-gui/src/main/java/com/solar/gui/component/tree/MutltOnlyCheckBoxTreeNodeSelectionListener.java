package com.solar.gui.component.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.solar.common.context.Consts.AddrType;
import com.solar.gui.component.checkable.multi.CheckBoxTreeNode;
import com.solar.gui.component.model.TreeAddr;

public class MutltOnlyCheckBoxTreeNodeSelectionListener extends MouseAdapter {

	private List<CheckBoxTreeNode> checkedList = new ArrayList<>();
	private Set<AddrType> chooseEnableSet = Collections.emptySet();
	private boolean chooseLeaf = false;

	public MutltOnlyCheckBoxTreeNodeSelectionListener(Set<AddrType> chooseEnableSet) {
		super();
		this.chooseEnableSet = chooseEnableSet;
	}

	public MutltOnlyCheckBoxTreeNodeSelectionListener(boolean chooseLeaf) {
		super();
		this.chooseLeaf = chooseLeaf;
	}

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
				boolean leaf = node.isLeaf();
				Object userObject = node.getUserObject();
				if (userObject instanceof TreeAddr) {
					TreeAddr addr = (TreeAddr) userObject;
					AddrType addrType = addr.getAddrType();

					boolean doUp = false;
					if (chooseEnableSet.isEmpty()) {
						doUp = true;
					} else if (chooseEnableSet.contains(addrType)) {
						doUp = true;
					}
					if (chooseLeaf) {
						doUp = doUp && leaf && chooseLeaf;
					}
					if (doUp) {
						boolean isSelected = !node.isSelected();
						changeSelected(node, isSelected);
						((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
					}
				}
			}
		}
	}
}