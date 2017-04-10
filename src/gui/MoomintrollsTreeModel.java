package gui;

import trolls.Moomintroll;
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
        // TODO: fix color property view
        node.insert(new DefaultMutableTreeNode("color"), 3);
        return node;
    }

    public void add(Moomintroll moomintroll) {
        insertNodeInto(toTreeNode(moomintroll), muteRoot, muteRoot.getChildCount());
    }

    public void removeAll() {
        for(int i = root.getChildCount() - 1; i >= 0; i--) {
            removeNodeFromParent((MutableTreeNode) root.getChildAt(i));
        }
    }

}
