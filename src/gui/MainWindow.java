package gui;

import trolls.Moomintroll;
import trolls.SerializableMoomintrollsCollection;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class MainWindow extends JFrame {
    private String path;
    private boolean isPathSet = false;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileIOMenu = new JMenu("File");
    private JMenuItem load = new JMenuItem("Load"),
            save = new JMenuItem("Save"),
            saveAs = new JMenuItem("Save As..");
    private JMenu tableMenu = new JMenu("Table");
    private JMenuItem add_if_max = new JMenuItem("Add if max.."),
            remove_greater = new JMenuItem("Remove greater..");

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
        fileIOMenu.add(saveAs);
        menuBar.add(tableMenu);
        tableMenu.add(remove_greater);
        tableMenu.add(add_if_max);

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
        saveAs.addActionListener(actionEvent -> saveAs());
        load.addActionListener(actionEvent -> load());
    }

    public void save() {
        if(!isPathSet) {
            saveAs();
            return;
        }

        try {
            successfullSave(path);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed save to \"" + path + "\"\nSelect file again.",
                    "Error: failed to save",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        saveAs();
    }

    public void saveAs() {
        String newPath;

        while (true){
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                newPath = chooser.getSelectedFile().getPath();
                if(!newPath.endsWith(".json")) {
                    newPath += ".json";
                }
                try {
                    successfullSave(newPath);
                    this.path = newPath;
                    isPathSet = true;
                    break;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Failed save to \"" + path + "\"\nSelect file again.",
                            "Error: failed to save",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else { // pushed "cancel" button
                break;
            }
        }
    }
    private void successfullSave(String path) throws IOException {
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
        this.setTitle(new File(path).getName() + " - Moomintrolls Manager");
    }

    public void load() {
        SerializableMoomintrollsCollection moomintrollsCollection = new SerializableMoomintrollsCollection();
        moomintrollsCollection.loadFromFile(path);
        moomintrollsTable.setMoomintrollsCollection(moomintrollsCollection);
    }
}
