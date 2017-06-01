package gui;

import trolls.Kindness;
import trolls.Moomintroll;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MoomintrollsTableModel extends DefaultTableModel {
    public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
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
        addColumn("Creation Date/Time");
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
                moomintroll.getPosition(),
                moomintroll.getCreationDateTime()
        };
    }

    public Moomintroll getRow(int row) {
        row = table.getRowSorter().convertRowIndexToModel(row);
        String kindness = getValueAt(row, 3).toString();
        kindness = kindness.substring(kindness.indexOf('[') + 1, kindness.indexOf(']'));
        
        return new Moomintroll(
                getValueAt(row, 0).toString(),
                getValueAt(row, 1).toString().equals("male"),
                Integer.parseInt(getValueAt(row,4).toString()),
                (Color) getValueAt(row, 2),
                new Kindness(Integer.parseInt(kindness)),
                (ZonedDateTime) getValueAt(row, 5)
        );
    }

    public void addRow(Moomintroll moomintroll) {
        addRow(moomintrollToData(moomintroll));
    }

    public void insertRow(int row, Moomintroll moomintroll) {
        insertRow(row, moomintrollToData(moomintroll));
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
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Color.class;
            case 3:
                return String.class; // TODO: replace with Kindness class
            case 4:
                return Integer.class;
            case 5:
                return ZonedDateTime.class;
        }
        return super.getColumnClass(column);
    }
}
