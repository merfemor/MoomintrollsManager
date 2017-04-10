package gui;

import trolls.Moomintroll;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MainWindow extends JFrame {
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private JButton addButton = new JButton("Add");
    private JButton removeButton = new JButton("Remove");
    private MoomintrollsTable moomintrollsTable = new MoomintrollsTable();

    public MainWindow() {
        super("Moomintrolls Manager");
        initComponents();
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // add menu items
        setJMenuBar(menuBar);

        toolBar.add(addButton);
        toolBar.add(removeButton);
        toolBar.setFloatable(false);

        JScrollPane jScrollPane = new JScrollPane(moomintrollsTable);
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(jScrollPane, BorderLayout.CENTER);

        addButton.addMouseListener(new MouseClickListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Moomintroll moomintrollToAdd = null;
                // TODO: create add dialog
                moomintrollsTable.add(moomintrollToAdd);
            }
        });

        removeButton.addMouseListener(new MouseClickListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int seletedRowsCount = moomintrollsTable.getSelectedRows().length;
                if(seletedRowsCount == 0) {
                    JOptionPane.showMessageDialog(null,
                            "No trolls have been selected for delete.\nSelect the trolls and try again.",
                            "Error: Nothing to remove",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                String trollsCountMessage = "moomintroll";
                if(seletedRowsCount > 1) {
                    trollsCountMessage += "s(" + seletedRowsCount + ")";
                }
                int reply = JOptionPane.showConfirmDialog(null,
                        "Are you sure want to remove " + trollsCountMessage + " from the collection?",
                        "Confirm removing",
                        JOptionPane.YES_NO_OPTION
                );
                if (reply == JOptionPane.YES_OPTION) {
                    moomintrollsTable.removeSelectedRows();
                }
            }
        });
    }

}
