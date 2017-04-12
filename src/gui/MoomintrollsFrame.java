package gui;

import trolls.Moomintroll;

import javax.swing.*;
import java.awt.*;

public class MoomintrollsFrame extends JPanel {
    public static int OK = JOptionPane.OK_OPTION;
    public static int CANCEL = JOptionPane.CANCEL_OPTION;
    private JFrame owner;
    private Moomintroll moomintroll;

    private JLabel nameLabel = new JLabel("Name"),
            positionLabel = new JLabel("Position"),
            kindnessLabel = new JLabel("Kindness"),
            colorTextLabel = new JLabel("Body Color: "),
            colorLabel = new JLabel(),
            genderLabel = new JLabel("Gender");
    private JTextField nameField = new JTextField(15);
    private JSpinner positionSpinner = new JSpinner();
    private JButton okButton = new JButton("OK"),
            cancelButton = new JButton("Cancel"),
            colorButton = new JButton("Choose");
    private JSlider kindnessSlider = new JSlider();
    private JToolBar buttonsToolBar = new JToolBar();
    private ButtonGroup genderButtons = new ButtonGroup();
    private JRadioButton genderMaleButton = new JRadioButton("male"),
            genderFemaleButton = new JRadioButton("female");

    public MoomintrollsFrame() {
        super(new GridLayout(5, 0, 10, 10));
        initComponents();
    }

    private void initComponents() {
        add(nameLabel);
        add(nameField);

        add(genderLabel);
        genderButtons.add(genderMaleButton);
        genderButtons.add(genderFemaleButton);
        JPanel genderButtonsPanel = new JPanel(new GridLayout(1, 2));
        genderButtonsPanel.add(genderMaleButton);
        genderButtonsPanel.add(genderFemaleButton);
        add(genderButtonsPanel);

        add(colorTextLabel);
        JPanel colorPanel = new JPanel(new GridLayout(1, 2));
        colorPanel.add(colorLabel);
        colorPanel.add(colorButton);
        add(colorPanel);

        add(kindnessLabel);
        add(kindnessSlider);

        add(positionLabel);
        add(positionSpinner);
    }

    public int showAddDialog(Component owner) {
        int result = JOptionPane.showConfirmDialog(
                owner,
                this,
                "Add",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        //TODO: add actions

        return result;
    }

    public int showEditDialog(Component owner, Moomintroll moomintroll) {
        // TODO: set components fields to moomintrolls fields values

        int result = JOptionPane.showConfirmDialog(
                owner,
                this,
                "Edit: " + moomintroll.getName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        // TODO: edit actions

        return result;
    }

    /**
     * Parse moomintroll from components content
     * @return true - if successfully parsed, else - false
     */
    private boolean parseMoomintroll() {
        // TODO: check components

        return true;
    }

    public Moomintroll getMoomintroll() {
        return moomintroll;
    }
}