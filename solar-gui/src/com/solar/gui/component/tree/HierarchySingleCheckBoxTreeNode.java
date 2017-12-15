package com.solar.gui.component.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.solar.common.context.Consts.AddrType;
import com.solar.gui.component.checkable.multi.CheckBoxTreeNode;
import com.solar.gui.component.model.TreeAddr;

public class HierarchySingleCheckBoxTreeNode extends CheckBoxTreeNode {
	private static final long serialVersionUID = 1L;

	private List<CheckBoxTreeNode> selectedNodes = new ArrayList<CheckBoxTreeNode>();

	public HierarchySingleCheckBoxTreeNode(TreeAddr treeAddr) {
		super(treeAddr);
	}

	public HierarchySingleCheckBoxTreeNode(String name) {
		super(name);
	}

	public HierarchySingleCheckBoxTreeNode(TreeAddr treeAddr, List<CheckBoxTreeNode> selectedNodes) {
		super(treeAddr);
		this.selectedNodes = selectedNodes;
	}

	public HierarchySingleCheckBoxTreeNode(String name, List<CheckBoxTreeNode> selectedNodes) {
		super(name);
		this.selectedNodes = selectedNodes;
	}

	public void disChildSelect(@SuppressWarnings("rawtypes") Enumeration children) {
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
			CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent.getParent();
			disParentSelect(pNode);
		}
	}

	public void setSelected(boolean _isSelected) {
		this.isSelected = _isSelected;
		if (_isSelected) {
			//System.out.println("add:" + this.toString());
			selectedNodes.add(this);
			// 如果选中，则将其所有的子结点都选中
			disChildSelect(children());

			// 向上检查，如果父结点的所有子结点都被选中，那么将父结点也选中
			CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;
			disParentSelect(pNode);
		} else {
			//System.out.println("remove:" + this.toString());
			selectedNodes.remove(this);
		}

	}

	public boolean isLeaf() {
		Object o = super.getUserObject();
		if (o instanceof TreeAddr) {
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