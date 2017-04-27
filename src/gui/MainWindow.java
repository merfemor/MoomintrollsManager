package gui;

import trolls.Moomintroll;
import trolls.SerializableMoomintrollsCollection;
import utils.Random;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;

// TODO: add thread for load()
// TODO: test multithreading i/o
// TODO: what should allow to do with collection when thread is alive?
//      1. Do not restrict - but need to very hard watch around Thread.isAlive() and create
//         flags like isChangedWhenThreadIsAlive
//      2. Only view collection - block "edit", "add", "remove" buttons - easy to implement

// TODO: add help menuItem with question form
// TODO: program license security

public class MainWindow extends JFrame {

    // components
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileIOMenu = new JMenu("File"),
            viewMenu = new JMenu("View");
    private JMenuItem open = new JMenuItem("Open.."),
            save = new JMenuItem("Save"),
            saveAs = new JMenuItem("Save As.."),
            close = new JMenuItem("Close");
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

    // I/O:
    private CollectionSession collectionSession;
    private String ENV_NAME;
    private boolean isSaved = true;
    private Thread ioThread;

    // TODO: GET RID OF THIS AS SOON AS POSSIBLE!
    private boolean operationStarted = false;
    private boolean closeStarted = false;
    private final Object ioLock = new Object();
    private final Object closeLock = new Object();

    public MainWindow(String pathVariableName) {
        super("Moomintrolls Manager");
        setName("Moomintrolls Manager");
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
        collectionSession = new CollectionSession(
                (SerializableMoomintrollsCollection) moomintrollsTable.getMoomintrollsCollection());
        loadSessionFromEnv(ENV_NAME);

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
        positionFromFilter.setPreferredSize(new Dimension(8, 1));
        positionToFilter.setPreferredSize(new Dimension(8, 1));
    }

    private void initActions() {
        save.addActionListener(actionEvent -> save(false));
        saveAs.addActionListener(actionEvent -> save(true));
        open.addActionListener(actionEvent -> {
            closeFile();
            synchronized (closeLock) {
                while (closeStarted) {
                    try {
                        closeLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            open();
        });
        close.addActionListener(actionEvent -> closeFile());

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        saveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));

        NumbericFieldKeyAdapter numbericFieldKeyAdapter = new NumbericFieldKeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                parseFilters();
            }
        };
        positionFromFilter.addKeyListener(numbericFieldKeyAdapter);
        positionToFilter.addKeyListener(numbericFieldKeyAdapter);

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
            removeButton.setEnabled(selectedRowsNum > 0 && crudToolBar.isEnabled());
            editButton.setEnabled(selectedRowsNum == 1 && crudToolBar.isEnabled());
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
                if(addButton.isEnabled())
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
                closeFile();
                if(collectionSession.getFile() == null) { // if closed
                    setVisible(false);
                    System.exit(0);
                }
            }
        });

        showTree.addActionListener(actionEvent -> {
            treeScrollPane.setVisible(showTree.getState());
            revalidate();
        });
    }


    private void initHiddenFunctions() {

        // adding random moominrroll
        final String ADD_RANDOM = "Add Random";
        getRootPane().getActionMap().put(ADD_RANDOM, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                moomintrollsTable.addRow(Random.randomTroll());
                isSaved = false;
                updateTitle();
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
        int posFrom, posTo;
        try {
            posFrom = Integer.parseInt(positionFromFilter.getText());
        } catch (Exception e) {
            posFrom = Integer.MIN_VALUE;
        }
        try {
            posTo = Integer.parseInt(positionToFilter.getText());
        } catch (Exception e) {
            posTo = Integer.MAX_VALUE;
        }
        moomintrollsTable.setRowSorter(new MoomintrollsRowFilter(
                nameFilter.getText(),
                enableMales.isSelected(),
                enableFemales.isSelected(),
                null,
                posFrom,
                posTo
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
        isSaved = false;
        updateTitle();
    }

    private void loadSessionFromEnv(String envName) {
        new Thread(() -> {
            try {
                collectionSession.setFile(new File(System.getenv(ENV_NAME)));
                setCrudEnabled(false);
                collectionSession.load();
                moomintrollsTable.setMoomintrollsCollection(collectionSession.getMoomintrollsCollection());
                isSaved = true;
                System.out.println("Successful loading " + collectionSession.getFile().getPath() + " from env \"" + envName + "\"");
            } catch (Exception e) {
                System.out.println("Failed to load from env \"" + envName + "\"");
                collectionSession.setFile(null);
            } finally {
                setCrudEnabled(true);
                updateTitle();
            }
        }).start();
    }

    private File chooseSaveFile(File currentDirectory) {
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ?
                fileChooser.getSelectedFile() : null;
    }

    private File chooseOpenFile(File currentDirectory) {
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ?
                fileChooser.getSelectedFile() : null;
    }

    public void save(boolean ignoreCurrentFile) {
        synchronized (ioLock) {
            while(operationStarted) {
                try {
                    ioLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(isSaved && !ignoreCurrentFile)
            return;
        File lastFile = collectionSession.getFile();
        if(ignoreCurrentFile || lastFile == null) {
            File newFile = chooseSaveFile(
                    lastFile == null ? new File(".") : lastFile);
            if(newFile == null) {
                return;
            }
            collectionSession.setFile(newFile);
        }
        operationStarted = true;
        setCrudEnabled(false);
        collectionSession.setMoomintrollsCollection(
                (SerializableMoomintrollsCollection)
                        moomintrollsTable.getMoomintrollsCollection()
        );
        ioThread = new Thread(() -> {
            do {
                try {
                    collectionSession.save();
                    Object[] options = {"OK"};
                    if(ignoreCurrentFile || lastFile == null) {
                        JOptionPane.showOptionDialog(
                                getContentPane(),
                                "Successfully saved into\n" + collectionSession.getFile().getPath(),
                                "Successfully saved",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                options,
                                options[0]
                        );
                    }
                    isSaved = true;
                    break;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getContentPane(),
                            "Failed to save into \n" + collectionSession.getFile().getPath() + "\nSelect file again.",
                            "Error: failed to save",
                            JOptionPane.ERROR_MESSAGE
                    );
                    File newFile = chooseSaveFile(collectionSession.getFile());
                    if(newFile == null) {
                        collectionSession.setFile(lastFile);
                        break;
                    }
                    if(newFile.exists()) {
                        int reply = JOptionPane.showConfirmDialog(this,
                                "File is already exists.\nOverwrite it?",
                                "Warning: overwriting file",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if(reply == JOptionPane.NO_OPTION) {
                            collectionSession.setFile(lastFile);
                            break;
                        }
                    }
                    // TODO: update newFile instead of setFile() every time
                    collectionSession.setFile(newFile);
                }
            } while (true);
            setCrudEnabled(true);
            updateTitle();
            synchronized (ioLock) {
                System.out.println("save notifying");
                ioLock.notify();
            }
            operationStarted = false;
        });
        ioThread.start();
    }

    public void closeFile() {
        closeStarted = true;
        synchronized (ioLock) {
            while (operationStarted) {
                System.out.println("ioThread isAlive, waiting...");
                try {
                    ioLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("waited");
        }
        ioThread = new Thread(() -> {
            if(!isSaved) {
                int reply = JOptionPane.showConfirmDialog(this,
                        "Current collection is not saved.\nDo you want to save it before closing?",
                        "Warning: unsaved collection",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (reply ==  JOptionPane.YES_OPTION) {
                    save(false);
                    synchronized (ioLock) {
                        while (operationStarted) {
                            try {
                                ioLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(!isSaved) {
                        return;
                    }
                }
                if(reply == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            // TODO: set it to null
            collectionSession = new CollectionSession(new SerializableMoomintrollsCollection());
            moomintrollsTable.setMoomintrollsCollection(collectionSession.getMoomintrollsCollection());
            System.out.println("Closed");
            isSaved = true;
            updateTitle();
            closeStarted = false;
            synchronized (closeLock) {
                System.out.println("close notifying");
                closeLock.notify();
            }
        });
        ioThread.start();
    }

    private void setCrudEnabled(boolean enabled) {
        crudToolBar.setEnabled(enabled);
        int selectedRows = moomintrollsTable.getSelectedRows().length;
        removeButton.setEnabled(enabled && selectedRows > 0);
        editButton.setEnabled(enabled && selectedRows == 1);
        addButton.setEnabled(enabled);
    }

    public synchronized void open() {
        ioThread = new Thread(() -> {
            synchronized (ioLock) {
                while (operationStarted) {
                    try {
                        ioLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            operationStarted = true;
            File lastFile = collectionSession.getFile();
            setCrudEnabled(false);
            while (true) {
                File newFile = chooseOpenFile(lastFile == null? new File(".") : lastFile);
                if(newFile == null) {
                    collectionSession.setFile(lastFile);
                    break;
                }
                try {
                    collectionSession.setFile(newFile);
                    collectionSession.load();
                    moomintrollsTable.setMoomintrollsCollection(collectionSession.getMoomintrollsCollection());
                    isSaved = true;
                    break;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to open " + newFile.getPath() + "\nSelect file again.",
                            "Error: failed to open",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to read " + newFile.getPath() + "\nFile is in the wrong format.\nSelect file again.",
                            "Error: failed to read",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            setCrudEnabled(true);
            updateTitle();
            operationStarted = false;
        });
        ioThread.start();
    }

    public void updateTitle() {
        String collectionName = "";
        // if path not set
        if(collectionSession.getFile() == null) {
            if(isSaved) {
                collectionName = "New Collection";
            } else {
                collectionName = "Unsaved Collection";
            }
        } else {
            if(!isSaved) {
                collectionName = "~";
            }
            collectionName += collectionSession.getFile().getName();
        }
        setTitle(collectionName + " - " + getName());
    }
}
