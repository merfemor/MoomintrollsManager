package gui;

import trolls.Moomintroll;
import trolls.MoomintrollsCollection;
import utils.Random;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class MoomintrollsTable extends JTable{
    class ColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)  {
            Color color = table.getBackground();
            boolean isColorCell = false;
            if(value instanceof Color) {
                isColorCell = true;
                color = (Color) value;
                value = Integer.toString(color.getRGB());
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, col);
            if(isSelected) {
                color = color.darker();
            }
            c.setBackground(color);
            Color foregroundColor = table.getForeground();
            if(isColorCell) {
                foregroundColor = c.getBackground();
            }
            c.setForeground(foregroundColor);
            return c;
        }
    }

    private MoomintrollsCollection moomintrollsCollection;
    private MoomintrollsTableModel moomintrollsDataModel;
    private MoomintrollsTree moomintrollsTree;

    MoomintrollsTable(MoomintrollsCollection moomintrollsCollection) {
        super(new MoomintrollsTableModel());
        moomintrollsDataModel = (MoomintrollsTableModel) dataModel;
        setDefaultRenderer(Object.class, new ColorRenderer());
        moomintrollsDataModel.registerTable(this);
        setAutoCreateRowSorter(true);
        this.moomintrollsCollection = moomintrollsCollection;
        for(Moomintroll moomintroll: moomintrollsCollection) {
            ((MoomintrollsTableModel) dataModel).addRow(moomintroll);
        }
    }

    public void registerMoomintrollsTree(MoomintrollsTree tree) {
        this.moomintrollsTree = tree;
        reloadTree();
    }

    private void reloadCollection() {
        this.moomintrollsCollection.clear();
        int rows = moomintrollsDataModel.getRowCount();
        for(int i = 0; i < rows; i++) {
            moomintrollsCollection.add(moomintrollsDataModel.getRow(i));
        }
    }

    public void setMoomintrollsCollection(MoomintrollsCollection moomintrollsCollection) {
        clear();
        this.moomintrollsCollection = moomintrollsCollection;
        for(Moomintroll moomintroll: moomintrollsCollection) {
            moomintrollsDataModel.addRow(moomintroll);
        }
        reloadTree();
    }

    public void clear() {
        this.moomintrollsCollection.clear();
        moomintrollsDataModel.clear();
        moomintrollsTree.removeAll();
    }

    private void reloadTree() {
        if(moomintrollsTree == null) {
            return;
        }

        // TODO: idea: remove moomintrollsCollection and store only moomintrolls data
        // TODO: idea: make special class for converting Moomintroll to Object[] data and vice versa
        // TODO: or: make moomintrolls indexes
        moomintrollsTree.removeAll();
        for(Moomintroll moomintroll: moomintrollsCollection) {
            moomintrollsTree.add(moomintroll);
        }
    }

    public void add(Moomintroll moomintroll) {
        moomintrollsCollection.add(moomintroll);
        moomintrollsDataModel.addRow(moomintroll);
        reloadTree();
    }

    public MoomintrollsCollection getMoomintrollsCollection() {
        return moomintrollsCollection;
    }

    public void removeSelectedRows() {
        int[] rows = getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            moomintrollsDataModel.removeRow(convertRowIndexToModel(rows[i]));
        }
        reloadCollection();
        reloadTree();
    }
}