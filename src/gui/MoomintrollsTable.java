package gui;

import trolls.Moomintroll;
import trolls.MoomintrollsCollection;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MoomintrollsTable extends JTable{
    class ColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)  {
            Color color = table.getBackground();
            if(value instanceof Color) {
                color = (Color) value;
                value = " ";
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            if(isSelected) {
                color = color.darker();
            }
            c.setBackground(color);
            return c;
        }
    }

    private MoomintrollsCollection moomintrollsCollection = new MoomintrollsCollection();
    private MoomintrollsTableModel moomintrollsDataModel;
    private MoomintrollsTree moomintrollsTree;

    MoomintrollsTable() {
        super(new MoomintrollsTableModel());
        moomintrollsDataModel = (MoomintrollsTableModel) dataModel;
        setDefaultRenderer(Object.class, new ColorRenderer());
    }

    public void registerMoomintrollsTree(MoomintrollsTree tree) {
        this.moomintrollsTree = tree;
        updateTree();
    }

    private void updateTree() {
        if(moomintrollsTree == null) {
            return;
        }
        moomintrollsTree.removeAll();
        for(Moomintroll moomintroll: moomintrollsCollection) {
            moomintrollsTree.add(moomintroll);
        }
    }

    public void add(Moomintroll moomintroll) {
        if(moomintroll == null) {
            moomintrollsCollection.add_random_troll();
            moomintroll = moomintrollsCollection.element();
        } else {
            moomintrollsCollection.add(moomintroll);
        }
        moomintrollsDataModel.addRow(moomintroll);
        updateTree();
    }

    public void removeSelectedRows() {

        int[] rows = getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            moomintrollsDataModel.removeRow(rows[i]);
        }
        // TODO: remove appropriate elements from moomintrollsCollection
        updateTree();
    }
}