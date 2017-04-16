package gui;

import trolls.Moomintroll;
import trolls.MoomintrollsCollection;
import utils.Random;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
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
        reloadTable();
        reloadTree();
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

    private void reloadTable() {
        moomintrollsDataModel.clear();
        for(Moomintroll moomintroll: moomintrollsCollection) {
            moomintrollsDataModel.addRow(moomintroll);
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
        moomintrollsTree.removeAll();
        for(int i = 0; i < getRowCount(); i++) {
            moomintrollsTree.add(getRow(i));
        }
    }

    public void addRow(Moomintroll moomintroll) {
        moomintrollsCollection.add(moomintroll);
        moomintrollsDataModel.addRow(moomintroll);
        if(moomintrollsTree != null)
            moomintrollsTree.add(moomintroll);
    }


    public void setRow(int row, Moomintroll moomintroll) {
        row = getRowSorter().convertRowIndexToModel(row);
        moomintrollsDataModel.removeRow(row);
        moomintrollsDataModel.insertRow(row, moomintroll);
        reloadCollection();
        clearSelection();
        row = convertRowIndexToView(row);
        setRowSelectionInterval(row, row);
        reloadTree();
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