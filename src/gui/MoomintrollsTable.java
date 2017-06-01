package gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.client.MoomintrollsClient;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.stream.LongStream;

public class MoomintrollsTable extends JTable{
    private BiMap<Long, Integer> rowById;
    private MoomintrollsCollection moomintrollsCollection;
    private MoomintrollsTableModel moomintrollsDataModel;
    private MoomintrollsTree moomintrollsTree;
    MoomintrollsTable() {
        super(new MoomintrollsTableModel());
        moomintrollsDataModel = (MoomintrollsTableModel) dataModel;
        rowById = HashBiMap.create();
        setDefaultRenderer(Color.class, new ColorRenderer());
        setDefaultRenderer(String.class, new ColorRenderer());
        setDefaultRenderer(Integer.class, new ColorRenderer());
        setDefaultRenderer(ZonedDateTime.class, new ColorRenderer());
        moomintrollsDataModel.registerTable(this);
        setAutoCreateRowSorter(true);
        getTableHeader().setReorderingAllowed(false);
        setUpdateSelectionOnSort(true);
        setMoomintrollsCollection(null);
    }

    public void setRowSorter(MoomintrollsRowFilter moomintrollsRowFilter) {
        ((TableRowSorter)getRowSorter()).setRowFilter(moomintrollsRowFilter);
        moomintrollsDataModel.fireTableDataChanged();
    }

    public void registerMoomintrollsTree(MoomintrollsTree tree) {
        this.moomintrollsTree = tree;
        moomintrollsTree.reloadFromTable();
    }

    public void addRow(Moomintroll moomintroll) {
        moomintrollsCollection.add(moomintroll);
        moomintrollsDataModel.addRow(moomintroll);
        if (moomintrollsTree != null)
            moomintrollsTree.insert(moomintroll);
    }

    public void addRow(long id, Moomintroll moomintroll) {
        if (moomintrollsDataModel.getRowCount() != rowById.size()) {
            MoomintrollsClient.log.severe("Size of rowById isn't equals to dataModel rows number: add canceled");
            return;
        }
        moomintrollsDataModel.addRow(moomintroll);
        rowById.put(id, moomintrollsDataModel.getRowCount() - 1);
        if (moomintrollsTree != null) {
            moomintrollsTree.insert(moomintroll);
        }
    }

    public void setRow(int row, Moomintroll moomintroll) {
        row = getRowSorter().convertRowIndexToModel(row);
        moomintrollsDataModel.removeRow(row);
        moomintrollsDataModel.insertRow(row, moomintroll);
        if (moomintrollsTree != null) {
            moomintrollsTree.setNode(row, moomintroll);
        }
    }

    public void update(long id, Moomintroll moomintroll) {
        if (!rowById.containsKey(id))
            return;
        int row = rowById.get(id);
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

    public long getRowId(int row) {
        return rowById.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(row))
                .findFirst()
                .orElse(null)
                .getKey();
    }

    public MoomintrollsCollection getMoomintrollsCollection() {
        moomintrollsCollection.clear();
        int rows = moomintrollsDataModel.getRowCount();
        clearSelection();

        for(int i = 0; i < rows; i++) {
            moomintrollsCollection.add(moomintrollsDataModel.getRow(i));
        }
        return moomintrollsCollection;
    }

    public void setMoomintrollsCollection(MoomintrollsCollection moomintrollsCollection) {
        if (moomintrollsCollection == null) {
            moomintrollsCollection = new MoomintrollsCollection();
        }
        this.moomintrollsCollection = moomintrollsCollection;
        moomintrollsDataModel.clear();
        rowById.clear();
        for (Moomintroll moomintroll : moomintrollsCollection) {
            moomintrollsDataModel.addRow(moomintroll);
        }
        if (moomintrollsTree != null) {
            moomintrollsTree.reloadFromTable();
        }
    }

    public void removeRows(int[] rows) {
        for (int i = rows.length - 1; i >= 0; i--) {
            int currentRow = convertRowIndexToModel(rows[i]);
            moomintrollsDataModel.removeRow(currentRow);
            if(moomintrollsTree != null) {
                moomintrollsTree.remove(currentRow);
            }
        }
        moomintrollsDataModel.fireTableDataChanged();
    }

    public void removeIds(long[] ids) {
        LongStream.of(ids)
                .boxed()
                .sorted(Collections.reverseOrder())
                .forEach(id -> {
                    if (rowById.containsKey(id)) {
                        int row = rowById.remove(id);
                        rowById.entrySet().stream()
                                .filter(e -> e.getValue() > row)
                                .forEach(e -> e.setValue(e.getValue() - 1));
                        if (row >= moomintrollsDataModel.getRowCount() || row < 0) {
                            MoomintrollsClient.log.warning("Can't remove id = " + id + ": row = " + row + " isn't correct");
                            return;
                        }
                        moomintrollsDataModel.removeRow(row);
                        if (moomintrollsTree != null) {
                            moomintrollsTree.remove(row);
                        }
                    }
                });
        moomintrollsDataModel.fireTableDataChanged();
    }

    class ColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Color color = table.getBackground();

            if (value instanceof ZonedDateTime)
                value = ((ZonedDateTime) value).format(moomintrollsDataModel.dateTimeFormatter);

            boolean isColorCell = false;
            if (value instanceof Color) {
                isColorCell = true;
                color = (Color) value;
                value = Integer.toString(color.getRGB());
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, col);
            if (isSelected) {
                color = color.darker();
            }
            c.setBackground(color);
            Color foregroundColor = table.getForeground();
            if (isColorCell) {
                foregroundColor = c.getBackground();
            }
            c.setForeground(foregroundColor);
            return c;
        }
    }
}