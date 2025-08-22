package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Border_Layout extends JFrame {
  public static void main(String[] args) {
    new Border_Layout();
  }

  public Border_Layout() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));

    // BORDERLAYOUT is used to arrange the components in 5 regions:
    // - north
    // - east
    // - west
    // - south
    // - center

    // TOP LEVEL CONTAINERS
    // INTERMEDIATE CONTAINERS
    // ATOMIC COMPONENTS

    JButton btn1 = new JButton("North");
    JButton btn2 = new JButton("East");
    JButton btn3 = new JButton("West");
    JButton btn4 = new JButton("South");
    JButton btn5 = new JButton("Center");

    add(btn1, BorderLayout.NORTH);
    add(btn2, BorderLayout.EAST);
    add(btn3, BorderLayout.WEST);
    add(btn4, BorderLayout.SOUTH);
    add(btn5, BorderLayout.CENTER);

    setVisible(true);
  }
}
