package gui;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// TODO: is this class a bike?
public class NumbericFieldKeyAdapter extends KeyAdapter{
    private int MAX_DIGITS;

    NumbericFieldKeyAdapter() {
        super();
        MAX_DIGITS = Integer.toString(Integer.MAX_VALUE).length() - 1;
    }

    NumbericFieldKeyAdapter (int MAX_DIGITS) {
        super();
        this.MAX_DIGITS = MAX_DIGITS;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        Character c = keyEvent.getKeyChar();
        JTextField component = (JTextField) keyEvent.getComponent();

        if((component.getText().length() > MAX_DIGITS && c != KeyEvent.VK_BACK_SPACE) || !(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_MINUS)) {
            keyEvent.consume();
        }
        if(c == KeyEvent.VK_MINUS) {
            if(!component.getText().contains("-")) {
                int selectionStart = component.getSelectionStart(),
                        selectionEnd = component.getSelectionEnd();
                component.setText("-" + component.getText());
                component.setSelectionStart(selectionStart + 1);
                component.setSelectionEnd(selectionEnd + 1);
            }

            keyEvent.consume();
        }
        if(Character.isDigit(c) && component.getSelectionStart() == 0 && component.getText().contains("-")) {
            keyEvent.consume();
        }
    }
}
