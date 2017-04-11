package gui;

import trolls.Moomintroll;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
}
