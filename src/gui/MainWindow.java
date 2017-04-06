package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class MainWindow extends JFrame {
    private JMenuBar menuBar;
    private JToolBar toolBar;

    public MainWindow() {
        super("Moomintrolls Manager");
        initComponents();
        setSize(900, 500);

    }

    private void initComponents() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        // add menu items
        setJMenuBar(menuBar);

        toolBar = new JToolBar();
        // add toolBar items
        toolBar.setFloatable(false);
        contentPane.add(toolBar, BorderLayout.NORTH);

    }

}
