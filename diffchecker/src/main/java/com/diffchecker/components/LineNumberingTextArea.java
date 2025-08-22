package com.diffchecker.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class LineNumberingTextArea extends JTextArea implements DocumentListener {
    private final JTextArea textArea;
    private static final Color BACKGROUND_COLOR = new Color(0x17181C); // Dark gray
    private static final Color NUMBER_COLOR = new Color(0x888690); // Light text

    public LineNumberingTextArea(JTextArea textArea) {
        this.textArea = textArea;
        textArea.getDocument().addDocumentListener(this);
        setEditable(false);
        setBackground(BACKGROUND_COLOR);
        setForeground(NUMBER_COLOR);
        setFont(textArea.getFont().deriveFont(Font.BOLD));
        setMargin(new Insets(0, 3, 0, 3)); // Add 5px right padding
        // setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

        updateLineNumbers();
    }

    private void updateLineNumbers() {
        StringBuilder lineNumbersText = new StringBuilder();
        int lines = textArea.getLineCount();
        for (int i = 1; i <= lines; i++) {
            lineNumbersText.append(i).append(System.lineSeparator());
        }
        setText(lineNumbersText.toString());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateLineNumbers();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateLineNumbers();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateLineNumbers();
    }
}
