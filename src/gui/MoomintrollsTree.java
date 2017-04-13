package gui;

import trolls.Moomintroll;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MoomintrollsTree extends JTree {
    MoomintrollsTable moomintrollsTable;
    MoomintrollsTreeModel moomintrollsTreeModel;

    MoomintrollsTree(MoomintrollsTable moomintrollsTable) {
        super(new MoomintrollsTreeModel());
        this.moomintrollsTable = moomintrollsTable;
        this.moomintrollsTreeModel = (MoomintrollsTreeModel) treeModel;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() < 2) {
                    return;
                }
                TreePath treePath = getSelectionPath();
                if(treePath == null) {
                    return;
                }
                int pathCount = treePath.getPathCount();
                if(pathCount == 3) {
                    treePath = treePath.getParentPath();
                } else if(pathCount != 2) {
                    return;
                }
                int row = 0;
                for(int i = 1; i <= getRowForPath(treePath); i++) {
                    if(getPathForRow(i).getPathCount() == 2)
                        row++;
                }
                row = moomintrollsTable.convertRowIndexToView(row - 1);
                moomintrollsTable.setRowSelectionInterval(row, row);
                moomintrollsTable.scrollRectToVisible(new Rectangle(moomintrollsTable.getCellRect(row, 0, true)));
            }
        });
    }

    public void removeAll() {
        moomintrollsTreeModel.removeAll();
    }

    public void add(Moomintroll moomintroll) {
        moomintrollsTreeModel.add(moomintroll);
        expandRow(0);
    }
}