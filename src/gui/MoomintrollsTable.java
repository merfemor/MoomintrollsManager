package gui;

import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
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
        setDefaultRenderer(Color.class, new ColorRenderer());
        setDefaultRenderer(String.class, new ColorRenderer());
        setDefaultRenderer(Integer.class, new ColorRenderer());
        moomintrollsDataModel.registerTable(this);
        setAutoCreateRowSorter(true);
        getTableHeader().setReorderingAllowed(false);
        setUpdateSelectionOnSort(true);
        this.moomintrollsCollection = moomintrollsCollection;
        for(Moomintroll moomintroll: moomintrollsCollection) {
            ((MoomintrollsTableModel) dataModel).addRow(moomintroll);
        }
    }

    public void setRowSorter(MoomintrollsRowFilter moomintrollsRowFilter) {
        ((TableRowSorter)getRowSorter()).setRowFilter(moomintrollsRowFilter);
        moomintrollsDataModel.fireTableDataChanged();
    }

    public void registerMoomintrollsTree(MoomintrollsTree tree) {
        this.moomintrollsTree = tree;
        moomintrollsTree.reloadFromTable();
    }

    public void setMoomintrollsCollection(MoomintrollsCollection moomintrollsCollection) {
        if(moomintrollsCollection == null) {
            moomintrollsCollection = new MoomintrollsCollection();
        }
        this.moomintrollsCollection = moomintrollsCollection;
        moomintrollsDataModel.clear();
        for(Moomintroll moomintroll: moomintrollsCollection) {
            moomintrollsDataModel.addRow(moomintroll);
        }
        if(moomintrollsTree != null) {
            moomintrollsTree.reloadFromTable();
        }
    }

    public void addRow(Moomintroll moomintroll) {
        moomintrollsCollection.add(moomintroll);
        moomintrollsDataModel.addRow(moomintroll);
        if(moomintrollsTree != null)
            moomintrollsTree.insert(moomintroll);
    }


    public void setRow(int row, Moomintroll moomintroll) {
        row = getRowSorter().convertRowIndexToModel(row);
        moomintrollsDataModel.removeRow(row);
        moomintrollsDataModel.insertRow(row, moomintroll);
        if(moomintrollsTree != null) {
            moomintrollsTree.setNode(row, moomintroll);
        }
    }

    public Moomintroll getRow(int row) {
        int[] selectedRows = getSelectedRows();
        clearSelection();
        Moomintroll moomintroll = moomintrollsDataModel.getRow(row);
        for (int selectedRow : selectedRows) {
            setRowSelectionInterval(selectedRow, selectedRow);
        }
        return moomintroll;
    }

    public MoomintrollsCollection getMoomintrollsCollection() {
        moomintrollsCollection.clear();
        int rows = moomintrollsDataModel.getRowCount();

        for(int i = 0; i < rows; i++) {
            moomintrollsCollection.add(moomintrollsDataModel.getRow(i));
        }
        return moomintrollsCollection;
    }

    public void removeSelectedRows() {
        int[] rows = getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            int currentRow = convertRowIndexToModel(rows[i]);
            moomintrollsDataModel.removeRow(currentRow);
            if(moomintrollsTree != null) {
                moomintrollsTree.remove(currentRow);
            }
        }
        moomintrollsDataModel.fireTableDataChanged();
    }
}