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
import java.security.Key;
import javax.swing.*;
import javax.swing.text.PlainDocument;

// TODO: add collection filtering
// TODO: add threads for i/o methods


public class MainWindow extends JFrame {

    private String ENV_NAME;

    // for managing current work path
    private final String NO_PATH = "New Collection";
    private final String NO_PATH_UNSAVED = "Unsaved Collection";
    private String path = NO_PATH;
    private boolean isPathSet = false;
    private boolean isSaved = true;

    // components
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileIOMenu = new JMenu("File"),
            viewMenu = new JMenu("View");
    private JMenuItem open = new JMenuItem("Open.."),
            save = new JMenuItem("Save"),
            saveAs = new JMenuItem("Save As.."),
            close = new JMenuItem("Close");
    private JMenuItem add_if_max = new JMenuItem("Add if max.."),
            remove_greater = new JMenuItem("Remove greater..");
    private JCheckBoxMenuItem showTree = new JCheckBoxMenuItem("Show tree", true);
    private JPanel toolBarsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JToolBar crudToolBar = new JToolBar(),
            filterToolBar = new JToolBar("filtering");
    // CRUD:
    private JButton addButton = new JButton("Add"),
            removeButton = new JButton("Remove"),
            editButton = new JButton("Edit");
    // Filtering:
    private JTextField nameFilter = new JTextField(15),
            positionFromFilter = new JTextField(8),
            positionToFilter = new JTextField(8);
    private JCheckBox enableMales = new JCheckBox("male", true),
            enableFemales = new JCheckBox("female", true);

    private JScrollPane treeScrollPane;
    private MoomintrollsTable moomintrollsTable;
    private MoomintrollsTree moomintrollsTree;

    public MainWindow(String pathVariableName) {
        super("Moomintrolls Manager");
        this.ENV_NAME = pathVariableName;
        //setSize(900, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initComponents();
        initActions();
        initHiddenFunctions();
        updateTitle();
        pack();
    }

    private void initComponents() {
        moomintrollsTable = new MoomintrollsTable(new SerializableMoomintrollsCollection());
        moomintrollsTree = new MoomintrollsTree(moomintrollsTable);
        moomintrollsTable.registerMoomintrollsTree(moomintrollsTree);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        setJMenuBar(menuBar);
        menuBar.add(fileIOMenu);
        fileIOMenu.add(open);
        fileIOMenu.addSeparator();
        fileIOMenu.add(save);
        fileIOMenu.add(saveAs);
        fileIOMenu.addSeparator();
        fileIOMenu.add(close);
        menuBar.add(viewMenu);
        viewMenu.add(showTree);

        JScrollPane tableScrollPane = new JScrollPane(moomintrollsTable);
        contentPane.add(toolBarsPanel, BorderLayout.NORTH);
        toolBarsPanel.add(crudToolBar);
        toolBarsPanel.add(filterToolBar);
        contentPane.add(tableScrollPane, BorderLayout.CENTER);
        treeScrollPane = new JScrollPane(moomintrollsTree);
        contentPane.add(treeScrollPane, BorderLayout.WEST);
        MoomintrollsFrame.setDefaultNewMoomintrollName("Unknown");

        removeButton.setEnabled(false);
        editButton.setEnabled(false);
        crudToolBar.add(addButton);
        crudToolBar.add(removeButton);
        crudToolBar.add(editButton);
        crudToolBar.setFloatable(false);

        filterToolBar.setFloatable(false);
        filterToolBar.add(new JLabel("Name: "));
        filterToolBar.add(nameFilter);
        filterToolBar.add(new JLabel("Gender: "));
        filterToolBar.add(enableMales);
        filterToolBar.add(enableFemales);
        filterToolBar.add(new JLabel("Position: "));
        filterToolBar.add(new JLabel("from"));
        filterToolBar.add(positionFromFilter);
        filterToolBar.add(new JLabel("to"));
        filterToolBar.add(positionToFilter);
        positionFromFilter.setTransferHandler(null);
        positionToFilter.setTransferHandler(null);
    }

    private void initActions() {
        save.addActionListener(actionEvent -> save());
        saveAs.addActionListener(actionEvent -> saveAs());
        open.addActionListener(actionEvent -> { closeFile(); open(); });
        close.addActionListener(actionEvent -> closeFile());

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        saveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));

        KeyAdapter preventLettersKeyAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                Character c = keyEvent.getKeyChar();
                JTextField component = (JTextField) keyEvent.getComponent();

                if((component.getText().length() > 8 && c != KeyEvent.VK_BACK_SPACE) || !(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_MINUS)) {
                    keyEvent.consume();
                }
                if(c == KeyEvent.VK_MINUS) {
                    if(!component.getText().contains("-")) {
                        component.setText("-" + component.getText());
                    }
                    keyEvent.consume();
                }
                if(Character.isDigit(c) && component.getSelectionStart() == 0 && component.getText().contains("-")) {
                    keyEvent.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(!((JTextField)keyEvent.getComponent()).getText().equals("-"))
                    parseFilters();
            }
        };

        positionFromFilter.addKeyListener(preventLettersKeyAdapter);
        positionToFilter.addKeyListener(preventLettersKeyAdapter);

        nameFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                parseFilters();
            }
        });
        enableMales.addActionListener(actionEvent -> parseFilters());
        enableFemales.addActionListener(actionEvent -> parseFilters());

        moomintrollsTable.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            int selectedRowsNum = moomintrollsTable.getSelectedRows().length;
            removeButton.setEnabled(selectedRowsNum > 0);
            editButton.setEnabled(selectedRowsNum == 1);
        });

        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(removeButton.isEnabled())
                    removeSelected();
            }
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


    private void initHiddenFunctions() {

        // adding random moominrroll
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
        moomintrollsTable.setRowSorter(new MoomintrollsRowFilter(
                nameFilter.getText(),
                enableMales.isSelected(),
                enableFemales.isSelected(),
                null,
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
