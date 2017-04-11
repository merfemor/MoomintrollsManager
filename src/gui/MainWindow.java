package gui;

import trolls.Moomintroll;
import trolls.SerializableMoomintrollsCollection;
import utils.FileManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;

public class MainWindow extends JFrame {
    private String path;
    private boolean isPathSet = false;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileIOMenu = new JMenu("File");
    private JMenuItem load = new JMenuItem("Load"),
    save = new JMenuItem("Save"),
    exit = new JMenuItem("Exit");
    private JMenu commandMenu = new JMenu("Command");
    private JMenuItem remove_first = new JMenuItem("Remove first"),
    add_if_max = new JMenuItem("Add if max..."),
    remove_greater = new JMenuItem("Remove greater...");

    private JToolBar toolBar = new JToolBar();
    // CRUD:
    private JButton addButton = new JButton("Add"),
            removeButton = new JButton("Remove"),
            editButton = new JButton("Edit");
    private MoomintrollsTable moomintrollsTable;
    private MoomintrollsTree moomintrollsTree = new MoomintrollsTree(moomintrollsTable);

    public MainWindow() {
        super("Moomintrolls Manager");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        moomintrollsTable = new MoomintrollsTable(new SerializableMoomintrollsCollection());
        initComponents();
    }

    private void initComponents() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // add menu items
        setJMenuBar(menuBar);
        menuBar.add(fileIOMenu);
        fileIOMenu.add(load);
        fileIOMenu.add(save);
        fileIOMenu.add(exit);
        menuBar.add(commandMenu);
        commandMenu.add(remove_first);
        commandMenu.add(remove_greater);
        commandMenu.add(add_if_max);

        toolBar.add(addButton);
        toolBar.add(removeButton);
        toolBar.add(editButton);
        toolBar.setFloatable(false);

        JScrollPane jScrollPane = new JScrollPane(moomintrollsTable);
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(jScrollPane, BorderLayout.CENTER);
        JScrollPane jScrollPane1 = new JScrollPane(moomintrollsTree);
        contentPane.add(jScrollPane1, BorderLayout.WEST);
        moomintrollsTable.registerMoomintrollsTree(moomintrollsTree);


        // TODO: same action for tree selection
        removeButton.addMouseListener(new MouseAdapter() {
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
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Moomintroll moomintrollToAdd = null;
                // TODO: create add dialog
                moomintrollsTable.add(moomintrollToAdd);
            }
        });

        save.addActionListener(actionEvent -> save());
        load.addActionListener(actionEvent -> load());
    }

    public void save() {
            try {
                ((SerializableMoomintrollsCollection) moomintrollsTable.getMoomintrollsCollection()).saveToFile(path);
                Object[] options = {"OK"};
                JOptionPane.showOptionDialog(this,
                        "Successfully saved into \"" + path + "\"",
                        "Successfully saved",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

            }
            catch (Exception e) {
                if(isPathSet) {
                    JOptionPane.showMessageDialog(null,
                            "Failed save to \"" + path + "\"\nSelect file again.",
                            "Error: failed to save",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                JFileChooser chooser = new JFileChooser();
                int res = chooser.showSaveDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    path = chooser.getSelectedFile().getPath();
                    if(!path.endsWith(".json")) {
                        path += ".json";
                    }
                    isPathSet = true;
                    save();
                }
            }
    }

    public void load() {
        SerializableMoomintrollsCollection moomintrollsCollection = new SerializableMoomintrollsCollection();
        moomintrollsCollection.loadFromFile(path);
        moomintrollsTable.setMoomintrollsCollection(moomintrollsCollection);
    }
}
