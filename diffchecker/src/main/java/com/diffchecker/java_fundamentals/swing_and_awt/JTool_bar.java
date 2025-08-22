package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class JTool_bar extends JFrame {
  public static void main(String[] args) {
    new JTool_bar();
  }

  public JTool_bar() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JToolBar jToolBar = new JToolBar();
    jToolBar.setRollover(true);
    jToolBar.setFloatable(false);

    // Adding buttons to the toolbar
    JButton btn1 = new JButton("Run");
    JButton btn2 = new JButton("Terminal");
    JButton btn3 = new JButton("Help");


    jToolBar.add(btn1);
    jToolBar.addSeparator();
    jToolBar.add(btn2);
    jToolBar.addSeparator();
    jToolBar.add(btn3);
    jToolBar.addSeparator();

    // Add the toolbar to the top
    add(jToolBar, BorderLayout.NORTH);
    setVisible(true);
  }
}
