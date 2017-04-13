package gui;

import trolls.Kindness;
import trolls.Moomintroll;
import trolls.SerializableMoomintrollsCollection;
import utils.FileManager;
import utils.Random;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileAlreadyExistsException;
import javax.swing.*;

public class MainWindow extends JFrame {
    // TODO: add collection filtering
    // TODO: add threads for i/o methods

    private final String NO_PATH = "New Collection";
    private final String NO_PATH_UNSAVED = "Unsaved Collection";
    private String ENV_NAME;
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
    private JCheckBoxMenuItem showTree = new JCheckBoxMenuItem("Show tree", true);
    private JToolBar toolBar = new JToolBar();
    // CRUD:
    private JButton addButton = new JButton("Add"),
            removeButton = new JButton("Remove"),
            editButton = new JButton("Edit");
    private MoomintrollsTable moomintrollsTable;
    private MoomintrollsTree moomintrollsTree;

    // Filtering:
    private JTextField nameFilter = new JTextField(10),
            positionFromFilter = new JTextField(5),
            positionToFilter = new JTextField(5);
    private JCheckBox enableMales = new JCheckBox("male", true),
            enableFemales = new JCheckBox("female", true);
    private JComboBox <Kindness> kindnessFilter;
    //private JSpinner positionFromFilter, positionToFilter;

    public MainWindow(String pathVariableName) {
        super("Moomintrolls Manager");
        this.ENV_NAME = pathVariableName;
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initComponents();
        updateTitle();
        pack();
    }

    private void initComponents() {
        moomintrollsTable = new MoomintrollsTable(new SerializableMoomintrollsCollection());
        moomintrollsTree = new MoomintrollsTree(moomintrollsTable);
        moomintrollsTable.registerMoomintrollsTree(moomintrollsTree);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // addRow menu items
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        saveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        setJMenuBar(menuBar);
        menuBar.add(fileIOMenu);
        fileIOMenu.add(open);
        fileIOMenu.addSeparator();
        fileIOMenu.add(save);
        fileIOMenu.add(saveAs);
        fileIOMenu.addSeparator();
        fileIOMenu.add(close);
        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        viewMenu.add(showTree);

        removeButton.setEnabled(false);
        editButton.setEnabled(false);
        toolBar.add(addButton);
        toolBar.add(removeButton);
        toolBar.add(editButton);
        toolBar.setFloatable(false);

        toolBar.add(new JLabel("Filtering: "));
        toolBar.add(nameFilter);
        toolBar.add(enableMales);
        toolBar.add(enableFemales);
        kindnessFilter = new JComboBox<>();
        for (Field field : Kindness.class.getFields()) {
            try {
                kindnessFilter.addItem((Kindness)(field.get(null)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //toolBar.add(kindnessFilter);
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                Character c = keyEvent.getKeyChar();
                JTextField component = (JTextField) keyEvent.getComponent();
                if(!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_MINUS) {
                    keyEvent.consume();
                    component.setText(component.getText().replace(Character.toString(c), ""));
                    return;
                }

                if(c == KeyEvent.VK_MINUS && !component.getText().startsWith("-")) {
                    component.setText(component.getText().replace("-", ""));
                }
                if(component.getText().equals("-0")) {
                    component.setText("0");
                }
                if(!component.getText().equals("-"))
                    parseFilters();
            }
        };
        positionFromFilter.addKeyListener(keyAdapter);
        positionToFilter.addKeyListener(keyAdapter);
        toolBar.add(new JLabel("Position: "));
        toolBar.add(positionFromFilter);
        toolBar.add(positionToFilter);
        KeyAdapter keyAdapter1 = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                parseFilters();
            }
        };
        nameFilter.addKeyListener(keyAdapter1);
        //positionFromFilter.addKeyListener(keyAdapter);
        //positionToFilter.addKeyListener(keyAdapter);
        enableMales.addActionListener(actionEvent -> parseFilters());
        enableFemales.addActionListener(actionEvent -> parseFilters());

        JScrollPane tableScrollPane = new JScrollPane(moomintrollsTable);
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(tableScrollPane, BorderLayout.CENTER);
        JScrollPane treeScrollPane = new JScrollPane(moomintrollsTree);
        contentPane.add(treeScrollPane, BorderLayout.WEST);
        MoomintrollsFrame.setDefaultNewMoomintrollName("Unknown");

        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(removeButton.isEnabled())
                    removeSelected();
            }
        });
        moomintrollsTable.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            int selectedRowsNum = moomintrollsTable.getSelectedRows().length;
            removeButton.setEnabled(selectedRowsNum > 0);
            editButton.setEnabled(selectedRowsNum == 1);
        });
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                add();
            }
        });
        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(editButton.isEnabled()) {
                    int row = moomintrollsTable.getSelectedRow();
                    editRow(row);
                }
            }
        });

        save.addActionListener(actionEvent -> save());
        saveAs.addActionListener(actionEvent -> saveAs());
        open.addActionListener(actionEvent -> { closeFile(); open(); });
        close.addActionListener(actionEvent -> closeFile());

        final String ADD_RANDOM = "Add Random";
        getRootPane().getActionMap().put(ADD_RANDOM, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                moomintrollsTable.addRow(Random.randomTroll());
            }
        });
        this.getRootPane().getInputMap().put(
                KeyStroke.getKeyStroke("control alt R"),
                ADD_RANDOM
        );

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

        showTree.addActionListener(actionEvent -> {
            treeScrollPane.setVisible(showTree.getState());
            revalidate();
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
            moomintrollsTable.addRow(moomintrollsFrame.getMoomintroll());
            isSaved = false;
            updateTitle();
        }
    }

    public void parseFilters() {
        System.out.println("reparsing");
        moomintrollsTable.setRowSorter(new MoomintrollsRowFilter(
                nameFilter.getText(),
                enableMales.isSelected(),
                enableFemales.isSelected(),
                kindnessFilter.getSelectedItem().toString(),
                positionFromFilter.getText().isEmpty()? Integer.MIN_VALUE : Integer.parseInt(positionFromFilter.getText()),
                positionToFilter.getText().isEmpty()? Integer.MAX_VALUE : Integer.parseInt(positionToFilter.getText())
        ));
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

    public void editRow(int row) {
        Moomintroll moomintroll = moomintrollsTable.getRow(row);
        MoomintrollsFrame editFrame = new MoomintrollsFrame();
        if (editFrame.showEditDialog(this, moomintroll)
                == MoomintrollsFrame.OK) {
            moomintrollsTable.setRow(row, editFrame.getMoomintroll());
        }
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
        updateTitle();
    }

    private void successfullLoad(File file) throws FileNotFoundException {
        String fileContent = FileManager.readFromFile(file.getPath());
        SerializableMoomintrollsCollection moomintrollsCollection = new SerializableMoomintrollsCollection(fileContent);
        moomintrollsTable.setMoomintrollsCollection(moomintrollsCollection);
        path = file.getPath();
        isPathSet = true;
        isSaved = true;
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
