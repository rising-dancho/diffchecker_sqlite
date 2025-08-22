package com.diffchecker.java_fundamentals.swing_and_awt;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class BlackTextAreaWithMargin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Black TextArea with Margin");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JTextArea textArea = new JTextArea();
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.WHITE);
            textArea.setCaretColor(Color.WHITE);
            textArea.setBorder(null);
            textArea.setMargin(new Insets(5, 5, 5, 5));
            textArea.setFont(new Font("Consolas", Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(Color.BLACK);

            // Wrap the scroll pane in a panel with margin
            JPanel marginPanel = new JPanel(new BorderLayout());
            marginPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Top, Left, Bottom, Right
            marginPanel.setBackground(Color.BLACK);
            marginPanel.add(scrollPane, BorderLayout.CENTER);

            frame.add(marginPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

