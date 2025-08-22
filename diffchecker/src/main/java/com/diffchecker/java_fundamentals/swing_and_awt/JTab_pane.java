package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class JTab_pane extends JFrame {
  public static void main(String[] args) {
    new JTab_pane();
  }

  public JTab_pane() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();
    JPanel p3 = new JPanel();

    p1.setBackground(Color.ORANGE);
    p2.setBackground(Color.yellow);
    p3.setBackground(Color.RED);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setBounds(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);


    tabbedPane.add("Main", p1);
    tabbedPane.add("Sales", p2);
    tabbedPane.add("About", p3);

    add(tabbedPane);
    setVisible(true);
  }
}
