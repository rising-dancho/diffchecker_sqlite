package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class JSplit_pane extends JFrame {
  public static void main(String[] args) {
    new JSplit_pane();
  }

  public JSplit_pane() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel p1 = new JPanel();
    p1.setBackground(Color.CYAN);

    JPanel p2 = new JPanel();
    p2.setBackground(Color.ORANGE);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, p2);

    add(splitPane);
    setVisible(true);
  }
}
