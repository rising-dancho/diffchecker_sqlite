package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class LineNumbers extends JFrame {
  
  public static void main(String[] args) {
    new LineNumbers();
  }

  public LineNumbers() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JTextArea jt1 = new JTextArea();
    JTextArea jt2 = new JTextArea();

    JScrollPane scroll1 = new JScrollPane(jt1);
    JScrollPane scroll2 = new JScrollPane(jt2);

    // Add line numbers to both scroll panes
    scroll1.setRowHeaderView(new LineNumberingTextArea(jt1));
    scroll2.setRowHeaderView(new LineNumberingTextArea(jt2));

    JPanel p1 = new JPanel(new BorderLayout());
    p1.add(scroll1, BorderLayout.CENTER);

    JPanel p2 = new JPanel(new BorderLayout());
    p2.add(scroll2, BorderLayout.CENTER);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, p2);
    splitPane.setDividerLocation(540);

    JButton btn = new JButton("Copy Text");
    btn.setSize(100, 30);
    btn.addActionListener(e -> jt2.setText(jt1.getText()));

    add(splitPane, BorderLayout.CENTER);
    add(btn, BorderLayout.SOUTH);
    setVisible(true);
  }

  // ========== Line Numbering Component ==========
  class LineNumberingTextArea extends JTextArea implements DocumentListener {
    private final JTextArea textArea;

    public LineNumberingTextArea(JTextArea textArea) {
      this.textArea = textArea;
      textArea.getDocument().addDocumentListener(this);
      setEditable(false);
      setBackground(Color.LIGHT_GRAY);
      setFont(textArea.getFont());
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
}
