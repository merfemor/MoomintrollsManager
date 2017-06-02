package gui;

import trolls.Kindness;
import trolls.Moomintroll;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MoomintrollsTableModel extends DefaultTableModel {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
    private JTable table;
    private ResourceBundle bundle;

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

    public void setColumnNames(String[] names) {
        setColumnIdentifiers(names);
    }

    public void registerTable(JTable table) {
        this.table= table;
    }

    public Object[] moomintrollToData(Moomintroll moomintroll) {
        return new Object[]{
                moomintroll.getName(),
                moomintroll.isMale(),
                moomintroll.getRgbBodyColor(),
                moomintroll.getKindness(),
                moomintroll.getPosition(),
                moomintroll.getCreationDateTime()
        };
    }

    public Moomintroll getRow(int row) {
        return new Moomintroll(
                getValueAt(row, 0).toString(),
                (Boolean) getValueAt(row, 1),
                Integer.parseInt(getValueAt(row,4).toString()),
                (Color) getValueAt(row, 2),
                (Kindness) getValueAt(row, 3),
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
                return Boolean.class;
            case 2:
                return Color.class;
            case 3:
                return Kindness.class;
            case 4:
                return Integer.class;
            case 5:
                return ZonedDateTime.class;
        }
        return super.getColumnClass(column);
    }
}
