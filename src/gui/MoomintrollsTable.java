package gui;

import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import javax.swing.*;

public class MoomintrollsTable extends JTable{
    MoomintrollsTableModel tableModel;
    MoomintrollsCollection moomintrollsCollection = new MoomintrollsCollection();

    MoomintrollsTable() {
        super(new MoomintrollsTableModel());
        tableModel = (MoomintrollsTableModel) getModel();
    }

    public void add(Moomintroll moomintroll) {
        if(moomintroll == null) {
            moomintrollsCollection.add_random_troll();
            moomintroll = moomintrollsCollection.element();
        } else {
            moomintrollsCollection.add(moomintroll);
        }
        tableModel.addRow(moomintroll);
    }

    public void removeSelectedRows() {

        int[] rows = getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            tableModel.removeRow(rows[i]);
        }
    }

}
