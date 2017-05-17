package gui;

import net.IdentifiedMoomintroll;
import net.client.CommandHandler;
import net.client.MoomintrollsClient;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;
import trolls.utils.Random;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

// TODO: program license security

public class MainWindow extends JFrame {

    // components
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileIOMenu = new JMenu("File"),
            viewMenu = new JMenu("View"),
            helpMenu = new JMenu("Help"),
            remoteMenu = new JMenu("Remote");
    private JMenuItem open = new JMenuItem("Open.."),
            save = new JMenuItem("Save"),
            saveAs = new JMenuItem("Save As.."),
            close = new JMenuItem("Close");
    private JMenuItem connect = new JMenuItem("Connect.."),
            disconnect = new JMenuItem("Disconnect"),
            reload = new JMenuItem("Reload");
    private JMenuItem about = new JMenuItem("About"),
            helpItem = new JMenuItem("Help");
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
    private CollectionSession collectionSession;
    private String ENV_NAME;

    // net
    final InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 1238);


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
        moomintrollsTable = new MoomintrollsTable(new MoomintrollsCollection());
        moomintrollsTree = new MoomintrollsTree(moomintrollsTable);
        moomintrollsTable.registerMoomintrollsTree(moomintrollsTree);
        collectionSession = new CollectionSession(moomintrollsTable.getMoomintrollsCollection());
        collectionSession.setOwner(this);
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
        remoteMenu.add(connect);
        reload.setEnabled(false);
        remoteMenu.add(reload);
        disconnect.setEnabled(false);
        remoteMenu.add(disconnect);
        menuBar.add(remoteMenu);
        helpMenu.add(helpItem);
        helpMenu.add(about);
        menuBar.add(helpMenu);

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
        save.addActionListener(actionEvent -> {
            if (save.isEnabled()) {
                new Thread(() -> {
                    setEditEnabled(false);
                    collectionSession.setMoomintrollsCollection(moomintrollsTable.getMoomintrollsCollection());
                    collectionSession.save();
                    setEditEnabled(true);
                    updateTitle();
                }).start();
            }
        });
        saveAs.addActionListener(actionEvent -> {
            if (saveAs.isEnabled()) {
                new Thread(() -> {
                    setEditEnabled(false);
                    collectionSession.setMoomintrollsCollection(moomintrollsTable.getMoomintrollsCollection());
                    collectionSession.saveAs();
                    setEditEnabled(true);
                    updateTitle();
                }).start();
            }
        });
        open.addActionListener(actionEvent -> {
            if (open.isEnabled()) {
                new Thread(() -> {
                    setEditEnabled(false);
                    if (collectionSession.close()) {
                        moomintrollsTable.setMoomintrollsCollection(null);
                        updateTitle();
                        if (collectionSession.open()) {
                            moomintrollsTable.setMoomintrollsCollection(collectionSession.getMoomintrollsCollection());
                        }
                    }
                    setEditEnabled(true);
                    updateTitle();
                }).start();
            }
        });
        close.addActionListener(actionEvent -> {
            if(close.isEnabled()) {
                new Thread(() -> {
                    setEditEnabled(false);
                    if (collectionSession.close()) {
                        moomintrollsTable.setMoomintrollsCollection(null);
                    }
                    setEditEnabled(true);
                    updateTitle();
                }).start();
            }
        });
        helpItem.addActionListener(actionEvent -> {
            Object[] options = {"Open Team Support Page"};
            int reply = JOptionPane.showOptionDialog(this,
                    "This is first version of program.\n" +
                            "So there is NO HELP.\n" +
                            "\n" +
                            "Release Notes:\n" +
                            "\tv1.0\n" +
                            "\t\t- all you see is new",
                    "Help",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if(reply == 0) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URL("https://se.ifmo.ru/~s225111/mooman-help/help.html").toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        about.addActionListener(actionEvent -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Moomintrolls Manager v1.0\n" +
                            "by me",
                    "About",
                    JOptionPane.PLAIN_MESSAGE
            );
        });


        // TODO: dialog for choosing adress
        connect.addActionListener(actionEvent -> {
            if (connect.isEnabled()) {
                connect.setEnabled(false);
                disconnect.setEnabled(true);
                reload.setEnabled(true);
                moomintrollsTable.setMoomintrollsCollection(new MoomintrollsCollection());
                if (collectionSession != null && !collectionSession.close()) {
                    return;
                }
                NetworkCollectionSession ncs;
                try {
                    ncs = new NetworkCollectionSession(
                            moomintrollsTable.getMoomintrollsCollection(),
                            inetSocketAddress);
                } catch (SocketException e) {
                    e.printStackTrace();
                    return;
                }
                ncs.getClient().setCommandHandler(new CommandHandler() {
                    @Override
                    public void add(IdentifiedMoomintroll[] moomintrolls) {
                        for (IdentifiedMoomintroll moomintroll : moomintrolls) {
                            moomintrollsTable.addRow(moomintroll.moomintroll());
                        }
                    }

                    @Override
                    public void remove(long[] ids) {

                    }

                    @Override
                    public void update(IdentifiedMoomintroll moomintroll) {

                    }

                    @Override
                    public void reload(IdentifiedMoomintroll[] moomintrolls) {
                        moomintrollsTable.setMoomintrollsCollection(new MoomintrollsCollection());
                        for (IdentifiedMoomintroll moomintroll : moomintrolls)
                            moomintrollsTable.addRow(moomintroll.moomintroll());
                    }
                });
                try {
                    ncs.reload();
                } catch (IOException e) {
                    MoomintrollsClient.log.log(Level.SEVERE, "Failed to reload", e);
                    collectionSession.close();
                }
                collectionSession = ncs;
                connect.setEnabled(true);
            }
        });

        reload.addActionListener(actionEvent -> {
            if (reload.isEnabled()) {
                try {
                    ((NetworkCollectionSession) collectionSession).reload();
                } catch (IOException e) {
                    MoomintrollsClient.log.log(Level.SEVERE, "Failed to reload", e);
                }
            }
        });

        disconnect.addActionListener(actionEvent -> {
            if (disconnect.isEnabled()) {
                reload.setEnabled(false);
                disconnect.setEnabled(false);
                collectionSession.close();
                moomintrollsTable.setMoomintrollsCollection(null);
                collectionSession = new CollectionSession(moomintrollsTable.getMoomintrollsCollection());
            }
        });

        // shortcuts
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        saveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        connect.setAccelerator(KeyStroke.getKeyStroke("control shift C"));
        helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.CTRL_DOWN_MASK));
        showTree.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.ALT_DOWN_MASK));

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
                setEditEnabled(false);
                if(collectionSession.close()) { // if closed
                    setVisible(false);
                    System.exit(0);
                } else {
                    setEditEnabled(true);
                    updateTitle();
                }
            }
        });

        showTree.addActionListener(actionEvent -> {
            treeScrollPane.setVisible(showTree.getState());
            revalidate();
        });
    }

    private void initHiddenFunctions() {

        // adding random moomintroll
        final String ADD_RANDOM = "Add Random";
        getRootPane().getActionMap().put(ADD_RANDOM, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                moomintrollsTable.addRow(Random.randomTroll());
                collectionSession.reportChange();
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
            collectionSession.reportChange();
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
        collectionSession.reportChange();
        updateTitle();
    }

    public void editRow(int row) {
        Moomintroll moomintroll = moomintrollsTable.getRow(row);
        MoomintrollsFrame editFrame = new MoomintrollsFrame();
        if (editFrame.showEditDialog(this, moomintroll)
                == MoomintrollsFrame.OK) {
            moomintrollsTable.setRow(row, editFrame.getMoomintroll());
        }
        collectionSession.reportChange();
        updateTitle();
    }

    private void loadSessionFromEnv(String envName) {
        new Thread(() -> {
            setEditEnabled(false);
            try {
                File file = new File(System.getenv(ENV_NAME));
                collectionSession.loadFromFile(file);
                collectionSession.setFile(file);
                moomintrollsTable.setMoomintrollsCollection(collectionSession.getMoomintrollsCollection());
                System.out.println("Successful loading " + file.getPath() + " from env \"" + envName + "\"");
            } catch (Exception e) {
                System.out.println("Failed to load from env \"" + envName + "\"");
            } finally {
                setEditEnabled(true);
                updateTitle();
            }
        }).start();
    }

    private void setEditEnabled(boolean enabled) {
        crudToolBar.setEnabled(enabled);
        int selectedRows = moomintrollsTable.getSelectedRows().length;
        removeButton.setEnabled(enabled && selectedRows > 0);
        editButton.setEnabled(enabled && selectedRows == 1);
        addButton.setEnabled(enabled);
        save.setEnabled(enabled);
        saveAs.setEnabled(enabled);
        open.setEnabled(enabled);
        close.setEnabled(enabled);
    }

    public void updateTitle() {
        String collectionName = "";
        // if path not set
        if(collectionSession.getFile() == null) {
            if(collectionSession.isSaved()) {
                collectionName = "New Collection";
            } else {
                collectionName = "Unsaved Collection";
            }
        } else {
            if(!collectionSession.isSaved()) {
                collectionName = "~";
            }
            collectionName += collectionSession.getFile().getName();
        }
        setTitle(collectionName + " - " + getName());
    }
}
