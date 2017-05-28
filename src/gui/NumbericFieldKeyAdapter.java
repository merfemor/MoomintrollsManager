package gui;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// TODO: is this class a bike?
// TODO: works bad on JSpinner: overflow
public class NumbericFieldKeyAdapter extends KeyAdapter{

    NumbericFieldKeyAdapter() {
        super();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        JTextField component = (JTextField) keyEvent.getComponent();
        int oldValue = 0, newValue = 0;
        try {
            if (component.getText().length() != 0)
                oldValue = Integer.parseInt(component.getText());
            newValue = Integer.parseInt(new StringBuilder(component.getText())
                    .insert(component.getSelectionStart(), keyEvent.getKeyChar()).toString());
        } catch (NumberFormatException e) {
            keyEvent.consume();
        }
        if (newValue == oldValue && component.getText().length() != 0) {
            keyEvent.consume();
        }
    }
}
