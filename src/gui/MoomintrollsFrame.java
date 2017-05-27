package gui;

import trolls.Kindness;
import trolls.Moomintroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

public class MoomintrollsFrame extends JPanel {
    public static int OK = JOptionPane.OK_OPTION;
    public static int CANCEL = JOptionPane.CANCEL_OPTION;
    private static String DEFAULT_NEW_MOOMINTROLL_NAME = "Moomintroll";
    private Moomintroll moomintroll;

    private JLabel nameLabel = new JLabel("Name"),
            positionLabel = new JLabel("Position"),
            kindnessLabel = new JLabel("Kindness"),
            colorTextLabel = new JLabel("Body Color"),
            colorLabel = new JLabel(),
            genderLabel = new JLabel("Gender");
    private JColorChooser colorChooser = new JColorChooser();
    private JTextField nameField = new JTextField(13);
    private JSpinner positionSpinner;
    private JButton okButton = new JButton("OK"),
            cancelButton = new JButton("Cancel"),
            colorButton = new JButton("Choose");
    private JSlider kindnessSlider = new JSlider(
            JSlider.HORIZONTAL,
            Kindness.DEVIL.value(),
            Kindness.ANGEL.value(),
            Kindness.NORMAL.value()
    );
    private JToolBar buttonsToolBar = new JToolBar();
    private ButtonGroup genderButtons = new ButtonGroup();
    private JRadioButton genderMaleButton = new JRadioButton("male"),
            genderFemaleButton = new JRadioButton("female");
    private Component owner;

    public MoomintrollsFrame() {
        super(new GridLayout(5, 0, 0, 8));
        add(nameLabel);
        add(nameField);

        add(genderLabel);
        genderButtons.add(genderMaleButton);
        genderButtons.add(genderFemaleButton);
        JPanel genderButtonsPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        genderButtonsPanel.add(genderMaleButton);
        genderButtonsPanel.add(genderFemaleButton);
        add(genderButtonsPanel);

        add(colorTextLabel);
        JPanel colorPanel = new JPanel(new GridLayout(1, 2, 45, 15));
        colorPanel.add(colorLabel);
        colorLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        colorLabel.setOpaque(true);
        colorPanel.add(colorButton);

        colorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int res = JOptionPane.showConfirmDialog(
                        colorButton,
                        colorChooser,
                        "Choose color",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                if(res == JOptionPane.OK_OPTION) {
                    colorLabel.setBackground(colorChooser.getColor());
                }
            }
        });
        add(colorPanel);

        add(kindnessLabel);
        Hashtable <Integer, JLabel> values = new Hashtable<>();
        values.put(kindnessSlider.getMaximum(), new JLabel("angel"));
        values.put((kindnessSlider.getMaximum() + kindnessSlider.getMinimum()) / 2,
                new JLabel(Kindness.NORMAL.toString()));
        values.put(kindnessSlider.getMinimum(), new JLabel("devil"));
        kindnessSlider.setLabelTable(values);
        kindnessSlider.setPaintLabels(true);
        add(kindnessSlider);

        add(positionLabel);
        positionSpinner= new JSpinner();
        ((JSpinner.NumberEditor)positionSpinner.getEditor()).getTextField().addKeyListener(
                new NumbericFieldKeyAdapter()
        );
        add(positionSpinner);
    }

    public int showAddDialog(Component owner) {
        return showEditDialog(owner, null);
    }

    public int showEditDialog(Component owner, Moomintroll moomintroll) {
        this.owner = owner;
        String title;
        if(moomintroll == null) {
            title = "Add";
            // set defaults
            nameField.setText(DEFAULT_NEW_MOOMINTROLL_NAME);
            genderMaleButton.setSelected(true);
            positionSpinner.setValue(0);
            colorLabel.setBackground(Color.LIGHT_GRAY);
            kindnessSlider.setValue((kindnessSlider.getMaximum() + kindnessSlider.getMinimum()) / 2);
        } else {
            title = "Edit " + moomintroll.getName();
            nameField.setText(moomintroll.getName());
            if(moomintroll.isMale())
                genderMaleButton.setSelected(true);
            else
                genderFemaleButton.setSelected(true);
            positionSpinner.setValue(moomintroll.getPosition());
            colorLabel.setBackground(moomintroll.getRgbBodyColor());
            kindnessSlider.setValue(moomintroll.getKindness().value());
        }
        int result;
        while (true) {
            result = JOptionPane.showConfirmDialog(
                    owner,
                    this,
                    title,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (result != JOptionPane.OK_OPTION || parseMoomintroll()) {
                break;
            }
        }
        return result;
    }

    private boolean parseMoomintroll() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    owner,
                    "Error: the name of the moomintroll can't be empty!",
                    "Error: empty name",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        moomintroll = new Moomintroll(nameField.getText().trim(),
                genderMaleButton.isSelected(),
                (int) positionSpinner.getValue(),
                colorLabel.getBackground(),
                new Kindness(kindnessSlider.getValue())
        );
        return true;
    }

    public Moomintroll getMoomintroll() {
        return moomintroll;
    }

    public static void setDefaultNewMoomintrollName(String defaultNewMoomintrollName) {
        DEFAULT_NEW_MOOMINTROLL_NAME = defaultNewMoomintrollName;
    }
}