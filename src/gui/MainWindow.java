package gui;

import trolls.Moomintroll;
import trolls.SerializableMoomintrollsCollection;
import utils.FileManager;
import utils.Random;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import javax.swing.*;

public class MainWindow extends JFrame {
    private final String NO_PATH = "New Collection";
    private final String NO_PATH_UNSAVED = "Unsaved Collection";
    private String path = NO_PATH;
    private boolean isPathSet = false;
    private boolean isSaved = true;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileIOMenu = new JMenu("File");
    private JMenuItem open = new JMenuItem("Open.."),
            save = new JMenuItem("Save"),
            saveAs = new JMenuItem("Save As.."),
            close = new JMenuItem("Close");
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        moomintrollsTable = new MoomintrollsTable(new SerializableMoomintrollsCollection());
        initComponents();
        updateTitle();
    }

    private void initComponents() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // add menu items
        setJMenuBar(menuBar);
        menuBar.add(fileIOMenu);
        fileIOMenu.add(open);
        fileIOMenu.addSeparator();
        fileIOMenu.add(save);
        fileIOMenu.add(saveAs);
        fileIOMenu.addSeparator();
        fileIOMenu.add(close);
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
                removeSelected();
            }
        });
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                add();
            }
        });

        save.addActionListener(actionEvent -> save());
        saveAs.addActionListener(actionEvent -> saveAs());
        open.addActionListener(actionEvent -> load());
        close.addActionListener(actionEvent -> closeFile());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if(closeFile() != JOptionPane.CANCEL_OPTION) {
                    setVisible(false);
                    System.exit(0);
                }
            }
        });
    }

    public void add() {
        Moomintroll moomintrollToAdd = Random.randomTroll();
        // TODO: create add dialog
        moomintrollsTable.add(moomintrollToAdd);
        isSaved = false;
        updateTitle();
    }

    public void removeSelected() {
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
        isSaved = false;
        updateTitle();
    }

    public void save() {
        if(isPathSet) {
            try {
                successfullSave(path, false);
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Failed save to \"" + path + "\"\nSelect file again.",
                        "Error: failed to save",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        saveAs();
    }

    public void saveAs() {
        String newPath;

        while (true){
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                newPath = chooser.getSelectedFile().getPath();
                try {
                    if(FileManager.pathExists(newPath)) {
                        int reply = JOptionPane.showConfirmDialog(this,
                                "File is already exitst.\nOverwrite it?",
                                "Warning: overwriting file",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if(reply == JOptionPane.NO_OPTION) {
                            throw new FileAlreadyExistsException(newPath);
                        }
                    }
                    successfullSave(newPath, true);
                    break;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to save into " + newPath + "\nSelect file again.",
                            "Error: failed to save",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else { // pushed "cancel" button
                break;
            }
        }
    }
    private void successfullSave(String path, boolean showDialog) throws IOException {
        ((SerializableMoomintrollsCollection) moomintrollsTable.getMoomintrollsCollection()).saveToFile(path);
        Object[] options = {"OK"};
        if(showDialog) {
            JOptionPane.showOptionDialog(this,
                    "Successfully saved into " + path,
                    "Successfully saved",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
        }
        isSaved = true;
        isPathSet = true;
        this.path = path;
        updateTitle();
    }

    public void load() {
        SerializableMoomintrollsCollection moomintrollsCollection = new SerializableMoomintrollsCollection();
        moomintrollsCollection.loadFromFile(path);
        moomintrollsTable.setMoomintrollsCollection(moomintrollsCollection);
    }

    public int closeFile() {
        if(!isPathSet && isSaved) {
            return JOptionPane.YES_OPTION;
        }
        if(!isSaved) {
            int reply  = JOptionPane.showConfirmDialog(this,
                    "Current collection is not saved.\nDo you want to save it before closing?",
                    "Warning: unsaved collection",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if(reply == JOptionPane.YES_OPTION) {
                save();
            } else if (reply == JOptionPane.CANCEL_OPTION) {
                return JOptionPane.CANCEL_OPTION;
            }
        }
        path = NO_PATH;
        isPathSet = false;
        isSaved = true;
        moomintrollsTable.removeMoomintrollsCollection();
        updateTitle();
        return JOptionPane.YES_OPTION;
    }

    public void updateTitle() {
        if(!isSaved && !isPathSet)
            path = NO_PATH_UNSAVED;
        setTitle(
                ((isSaved || !isPathSet)? "" : "~") +
                        new File(path).getName() +
                        " - Moomintrolls Manager"
        );
    }
}
