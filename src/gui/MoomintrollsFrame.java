package gui;

import trolls.Kindness;
import trolls.Moomintroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZonedDateTime;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class MoomintrollsFrame extends JPanel {
    public static int OK = JOptionPane.OK_OPTION;
    public static int CANCEL = JOptionPane.CANCEL_OPTION;
    private static String DEFAULT_NEW_MOOMINTROLL_NAME = "Moomintroll";
    private ResourceBundle bundle;
    private Moomintroll moomintroll;

    private String addTitle = "Add", editTitle = "Edit";
    private JLabel nameLabel = new JLabel("Name"),
            positionLabel = new JLabel("Position"),
            kindnessLabel = new JLabel("Kindness"),
            colorTextLabel = new JLabel("Body Color"),
            colorLabel = new JLabel(),
            genderLabel = new JLabel("Gender");
    private JColorChooser colorChooser = new JColorChooser();
    private JTextField nameField = new JTextField(13);
    private JSpinner positionSpinner;
    private JButton colorButton = new JButton("Choose");
    private JSlider kindnessSlider = new JSlider(
            JSlider.HORIZONTAL,
            Kindness.DEVIL.value(),
            Kindness.ANGEL.value(),
            Kindness.NORMAL.value()
    );

    private ButtonGroup genderButtons = new ButtonGroup();
    private JRadioButton genderMaleButton = new JRadioButton("male"),
            genderFemaleButton = new JRadioButton("female");
    private Component owner;

    private Hashtable<Integer, JLabel> kindnessValues;

    public MoomintrollsFrame(ResourceBundle resourceBundle) {
        super(new GridLayout(5, 0, 0, 8));
        this.bundle = resourceBundle;
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
        JPanel colorPanel = new JPanel(new GridLayout(1, 2, 20, 10));
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
                        bundle.getString("chooseColor"),
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
        kindnessValues = new Hashtable<>();
        kindnessValues.put(kindnessSlider.getMaximum(), new JLabel(Kindness.DEVIL.toString(resourceBundle, true)));
        kindnessValues.put((kindnessSlider.getMaximum() + kindnessSlider.getMinimum()) / 2,
                new JLabel(Kindness.NORMAL.toString(resourceBundle, true)));
        kindnessValues.put(kindnessSlider.getMinimum(), new JLabel(Kindness.DEVIL.toString(resourceBundle, true)));
        kindnessSlider.setLabelTable(kindnessValues);
        kindnessSlider.setPaintLabels(true);
        add(kindnessSlider);

        add(positionLabel);
        positionSpinner= new JSpinner();
        ((JSpinner.NumberEditor)positionSpinner.getEditor()).getTextField().addKeyListener(
                new NumbericFieldKeyAdapter()
        );
        add(positionSpinner);
        genderMaleButton.addActionListener((e) -> {
            updateKindnessValues(genderMaleButton.isSelected());
            kindnessSlider.repaint();
        });
        genderFemaleButton.addActionListener((e) -> {
            updateKindnessValues(genderMaleButton.isSelected());
            kindnessSlider.repaint();
        });
        updateLocale(resourceBundle);
    }

    public static void setDefaultNewMoomintrollName(String defaultNewMoomintrollName) {
        DEFAULT_NEW_MOOMINTROLL_NAME = defaultNewMoomintrollName;
    }

    public int showAddDialog(Component owner) {
        return showEditDialog(owner, null);
    }

    public int showEditDialog(Component owner, Moomintroll moomintroll) {
        this.owner = owner;
        String title;
        if(moomintroll == null) {
            title = addTitle;
            // set defaults
            nameField.setText(DEFAULT_NEW_MOOMINTROLL_NAME);
            genderMaleButton.setSelected(true);
            positionSpinner.setValue(0);
            colorLabel.setBackground(Color.LIGHT_GRAY);
            kindnessSlider.setValue((kindnessSlider.getMaximum() + kindnessSlider.getMinimum()) / 2);
        } else {
            title = editTitle + " " + moomintroll.getName();
            nameField.setText(moomintroll.getName());
            if(moomintroll.isMale())
                genderMaleButton.setSelected(true);
            else
                genderFemaleButton.setSelected(true);
            positionSpinner.setValue(moomintroll.getPosition());
            colorLabel.setBackground(moomintroll.getRgbBodyColor());
            kindnessSlider.setValue(moomintroll.getKindness().value());
            updateKindnessValues(moomintroll.isMale());
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
            if (result != JOptionPane.OK_OPTION || parseMoomintroll(moomintroll)) {
                break;
            }
        }
        return result;
    }

    private boolean parseMoomintroll(Moomintroll oldMoomintroll) {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    owner,
                    bundle.getString("emptyStringErrorMessage"),
                    bundle.getString("emptyStringErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        moomintroll = new Moomintroll(nameField.getText().trim(),
                genderMaleButton.isSelected(),
                (int) positionSpinner.getValue(),
                colorLabel.getBackground(),
                new Kindness(kindnessSlider.getValue()),
                oldMoomintroll == null ? ZonedDateTime.now() : oldMoomintroll.getCreationDateTime()
        );
        return true;
    }

    public Moomintroll getMoomintroll() {
        return moomintroll;
    }

    private void updateKindnessValues(boolean isMale) {
        kindnessValues.get(kindnessSlider.getMaximum()).setText(Kindness.ANGEL.toString(bundle, isMale));
        kindnessValues.get(kindnessSlider.getMinimum()).setText(Kindness.DEVIL.toString(bundle, isMale));
        kindnessValues.get((kindnessSlider.getMaximum() + kindnessSlider.getMinimum()) / 2)
                .setText(Kindness.NORMAL.toString(bundle, isMale));
        kindnessSlider.setLabelTable(kindnessValues);
    }

    public void updateLocale(ResourceBundle bundle) {
        this.bundle = bundle;
        nameLabel.setText(bundle.getString("nameAttribute"));
        genderLabel.setText(bundle.getString("genderAttribute"));
        colorButton.setText(bundle.getString("chooseColorButton"));
        kindnessLabel.setText(bundle.getString("kindnessAttribute"));
        genderMaleButton.setText(bundle.getString("genderMale"));
        genderFemaleButton.setText(bundle.getString("genderFemale"));
        colorTextLabel.setText(bundle.getString("bodyColorAttribute"));
        positionLabel.setText(bundle.getString("positionAttribute"));
        setDefaultNewMoomintrollName(bundle.getString("defaultNewMoomintrollName"));
        addTitle = bundle.getString("adding");
        editTitle = bundle.getString("editing");
        updateKindnessValues(true);
    }
}