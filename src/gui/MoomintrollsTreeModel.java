package gui;

import trolls.Moomintroll;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

class MoomintrollsTreeModel extends DefaultTreeModel {
    private MutableTreeNode muteRoot;

    public MoomintrollsTreeModel() {
        super(new DefaultMutableTreeNode("Moomintrolls Collection"));
        muteRoot = (MutableTreeNode) root;
        this.setRoot(muteRoot);
    }

    private MutableTreeNode toTreeNode(Moomintroll moomintroll) {
        MutableTreeNode node = new DefaultMutableTreeNode(moomintroll.getName());
        node.insert(new DefaultMutableTreeNode("gender: " + (moomintroll.isMale()? "male" : "female") ), 0);
        node.insert(new DefaultMutableTreeNode("position: " + moomintroll.getPosition()), 1);
        node.insert(new DefaultMutableTreeNode("kindness: " + moomintroll.getKindness().toString()), 2);
        // TODO: make color property view
        node.insert(new DefaultMutableTreeNode("color"), 3);
        return node;
    }

    public void removeAll() {
        for(int i = root.getChildCount() - 1; i >= 0; i--) {
            remove(i);
        }
    }

    public void remove(int node) {
        super.removeNodeFromParent((MutableTreeNode) muteRoot.getChildAt(node));
    }

    public void insert(int node, Moomintroll moomintroll) {
        insertNodeInto(toTreeNode(moomintroll), muteRoot, node);
    }

    public int getRootChildCount() {
        return muteRoot.getChildCount();
    }
}
