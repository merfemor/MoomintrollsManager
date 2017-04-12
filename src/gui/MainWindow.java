package gui;

import trolls.SerializableMoomintrollsCollection;
import utils.FileManager;
import utils.Random;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import javax.swing.*;

public class MainWindow extends JFrame {
    private final String NO_PATH = "New Collection";
    private final String NO_PATH_UNSAVED = "Unsaved Collection";
    private String ENV_NAME;
    private String PROPERTY_NAME = "MOOMINTROLLS_MANAGER_COLLECTION_PATH";
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

    public MainWindow(String pathVariableName) {
        super("Moomintrolls Manager");
        this.ENV_NAME = pathVariableName;
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
        moomintrollsTable.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            removeButton.setEnabled(moomintrollsTable.getSelectedRows().length == 0);
        });
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                add();
            }
        });

        save.addActionListener(actionEvent -> save());
        saveAs.addActionListener(actionEvent -> saveAs());
        open.addActionListener(actionEvent -> { closeFile(); open(); });
        close.addActionListener(actionEvent -> closeFile());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if(closeFile() != JOptionPane.CANCEL_OPTION) {
                    setVisible(false);
                    System.exit(0);
                }
            }

            @Override
            public void windowOpened(WindowEvent windowEvent) {
                if(loadFromEnv(ENV_NAME)) {
                    System.out.println("Successful loading " + path + " from env \"" + ENV_NAME + "\"");
                } else {
                    System.out.println("Failed to load from env");
                }
            }
        });
    }

    public boolean loadFromEnv(String envName) {
        try {
            successfullLoad(new File(System.getenv(envName)));
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public void add() {
        MoomintrollsFrame moomintrollsFrame = new MoomintrollsFrame();
        if(moomintrollsFrame.showAddDialog(this) == MoomintrollsFrame.OK) {
            moomintrollsTable.add(moomintrollsFrame.getMoomintroll());
            isSaved = false;
            updateTitle();
        }
    }

    public void addRandom() {
        moomintrollsTable.add(Random.randomTroll());
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
            JFileChooser chooser = new JFileChooser(isPathSet? path : new File(".").getPath());
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
                            "Failed to save into \n" + newPath + "\nSelect file again.",
                            "Error: failed to save",
                            JOptionPane.ERROR_MESSAGE
                    );
                    e.printStackTrace();
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
                    "Successfully saved into\n" + path,
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
        System.setProperty(PROPERTY_NAME, path);
        updateTitle();
    }

    private void successfullLoad(File file) throws FileNotFoundException {
        String fileContent = FileManager.readFromFile(file.getPath());
        SerializableMoomintrollsCollection moomintrollsCollection = new SerializableMoomintrollsCollection(fileContent);
        moomintrollsTable.setMoomintrollsCollection(moomintrollsCollection);
        path = file.getPath();
        isPathSet = true;
        isSaved = true;
        System.setProperty(PROPERTY_NAME, path);
        updateTitle();
    }

    public void open() {
        JFileChooser chooser = new JFileChooser(isPathSet? path : new File(".").getPath());
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        while (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            try {
                successfullLoad(file);
                break;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to open " + file.getPath() + "\nSelect file again.",
                        "Error: failed to open",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to read " + file.getPath() + "\nFile is in the wrong format.\nSelect file again.",
                        "Error: failed to read",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public int closeFile() {
        int reply = JOptionPane.YES_OPTION;
        if(!isSaved) {
            reply  = JOptionPane.showConfirmDialog(this,
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
        moomintrollsTable.clear();
        moomintrollsTable.setMoomintrollsCollection(new SerializableMoomintrollsCollection());
        updateTitle();
        return reply;
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
