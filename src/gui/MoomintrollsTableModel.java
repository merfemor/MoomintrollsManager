package gui;

import trolls.Moomintroll;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class MoomintrollsTableModel extends DefaultTableModel {
    MoomintrollsTableModel() {
        super();
        columnIdentifiers.clear();
        addColumn("Name");
        addColumn("Gender");
        addColumn("Body color");
        addColumn("Kindness");
        addColumn("Position");
    }

    public void addRow(Moomintroll moomintroll) {
        Object[] fields = {
                moomintroll.getName(),
                moomintroll.isMale() ? "m" : "w",
                moomintroll.getRgbBodyColor(),
                moomintroll.getKindness(),
                moomintroll.getPosition()
        };
        addRow(fields);
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
        if (column == 4) {
            return Integer.class;
        }
        return super.getColumnClass(column);
    }
}
