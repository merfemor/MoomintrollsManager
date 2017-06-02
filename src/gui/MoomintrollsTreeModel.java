package gui;

import trolls.Moomintroll;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.util.ResourceBundle;

class MoomintrollsTreeModel extends DefaultTreeModel {
    private MutableTreeNode muteRoot;
    private ResourceBundle bundle;

    public MoomintrollsTreeModel() {
        super(new DefaultMutableTreeNode("Moomintrolls Collection"));
        muteRoot = (MutableTreeNode) root;
        this.setRoot(muteRoot);
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        muteRoot.setUserObject(bundle.getObject("rootNodeName"));
        //reload();
    }

    private MutableTreeNode toTreeNode(Moomintroll moomintroll) {
        MutableTreeNode node = new DefaultMutableTreeNode(moomintroll.getName());
        node.insert(new DefaultMutableTreeNode(
                        bundle.getString("genderAttribute") + ": " +
                                (moomintroll.isMale() ?
                                        bundle.getString("genderMale") :
                                        bundle.getString("genderFemale"))),
                0
        );
        node.insert(new DefaultMutableTreeNode(
                bundle.getString("positionAttribute") + ": " + moomintroll.getPosition()), 1);
        node.insert(new DefaultMutableTreeNode(
                bundle.getString("kindnessAttribute") + ": " +
                        moomintroll.getKindness().toString(bundle, moomintroll.isMale())), 2);
        node.insert(new DefaultMutableTreeNode(
                bundle.getString("creationDateAttribute") + ": " +
                        moomintroll.getCreationDateTime().format(MoomintrollsTableModel.dateTimeFormatter)), 3);

        // TODO: make color property view
        //node.insert(new DefaultMutableTreeNode(
        //        bundle.getString("bodyColorAttribute") + ": "), 3);
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
