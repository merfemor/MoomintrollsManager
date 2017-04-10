package gui;

import trolls.Moomintroll;

import javax.swing.*;

public class MoomintrollsTree extends JTree {
    MoomintrollsTable moomintrollsTable;
    MoomintrollsTreeModel moomintrollsTreeModel;

    MoomintrollsTree(MoomintrollsTable moomintrollsTable) {
        super(new MoomintrollsTreeModel());
        this.moomintrollsTable = moomintrollsTable;
        this.moomintrollsTreeModel = (MoomintrollsTreeModel) treeModel;
        // TODO: add double-click action listener
    }

    public void removeAll() {
        moomintrollsTreeModel.removeAll();
    }

    public void add(Moomintroll moomintroll) {
        moomintrollsTreeModel.add(moomintroll);
        expandRow(0);
    }
}