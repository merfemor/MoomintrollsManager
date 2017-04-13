package gui;

import trolls.Kindness;
import trolls.Moomintroll;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class MoomintrollsTableModel extends DefaultTableModel {
    private JTable table;

    MoomintrollsTableModel() {
        super();
        // TODO: get rid of many cols number dependencies
        columnIdentifiers.clear();
        addColumn("Name");
        addColumn("Gender");
        addColumn("Body color");
        addColumn("Kindness");
        addColumn("Position");
    }

    public void registerTable(JTable table) {
        this.table= table;
    }

    public Object[] moomintrollToData(Moomintroll moomintroll) {
        return new Object[]{
                moomintroll.getName(),
                moomintroll.isMale() ? "male" : "female",
                moomintroll.getRgbBodyColor(),
                moomintroll.getKindness().toString() + " [" + moomintroll.getKindness().value() + "]",
                moomintroll.getPosition()
        };
    }

    public Moomintroll getRow(int row) {
        String kindness = getValueAt(row, 3).toString();
        kindness = kindness.substring(kindness.indexOf('[') + 1, kindness.indexOf(']'));
        TableCellRenderer cellRenderer = table.getCellRenderer(row, 2);
        Color bodyColor = table.prepareRenderer(cellRenderer, row, 2).getBackground();
        return new Moomintroll(
                getValueAt(row, 0).toString(),
                getValueAt(row, 1).toString().equals("male"),
                Integer.parseInt(getValueAt(row,4).toString()),
                bodyColor,
                new Kindness(Integer.parseInt(kindness))
        );
    }

    public void addRow(Moomintroll moomintroll) {
        addRow(moomintrollToData(moomintroll));
    }

    public void clear() {
        for(int i = getRowCount() - 1; i >= 0; i--) {
            removeRow(i);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        // TODO: beautiful sorting for Colour column
        // TODO: sorting position as integer without breaking cellrender work
        return super.getColumnClass(column);
    }
}
