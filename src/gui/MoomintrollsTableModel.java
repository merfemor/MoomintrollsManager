package gui;

import trolls.Moomintroll;
import javax.swing.table.DefaultTableModel;

public class MoomintrollsTableModel extends DefaultTableModel {
    MoomintrollsTableModel() {
        super();
        columnIdentifiers.clear();
        columnIdentifiers.add("Name");
        columnIdentifiers.add("Gender");
        columnIdentifiers.add("Body color");
        columnIdentifiers.add("Emotional condition");
        columnIdentifiers.add("Position");
    }

    public void addRow(Moomintroll moomintroll) {
        Object[] fields = {
                moomintroll.getName(),
                moomintroll.isMale() ? "m" : "w",
                moomintroll.getRgbBodyColor(),
                moomintroll.getEmotionalCondition(),
                moomintroll.getPosition()
        };
        addRow(fields);
    }
}
