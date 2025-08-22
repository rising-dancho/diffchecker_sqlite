package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class JText_area extends JFrame {
  public static void main(String[] args) {
    new JText_area();
  }

  public JText_area() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // TOP LEVEL CONTAINERS
    // INTERMEDIATE CONTAINERS
    // ATOMIC COMPONENTS

    // Creating TexAreas
    JTextArea jt1 = new JTextArea();
    JTextArea jt2 = new JTextArea();

    // Wrapping the TextAreas in Scrollpane
    JScrollPane scroll1 = new JScrollPane(jt1);
    JScrollPane scroll2 = new JScrollPane(jt2);

    JPanel p1 = new JPanel(new BorderLayout());
    p1.add(scroll1, BorderLayout.CENTER);

    JPanel p2 = new JPanel(new BorderLayout());
    p2.add(scroll2, BorderLayout.CENTER);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, p2);
    splitPane.setDividerLocation(540);

    JButton btn = new JButton();
    btn.setText("Copy Text");
    btn.setSize(100, 30);

    btn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String textToCopy = jt1.getText();
        jt2.setText(textToCopy);
      }

    });

    add(splitPane);
    add(btn, BorderLayout.SOUTH);
    setVisible(true);
  }
}
