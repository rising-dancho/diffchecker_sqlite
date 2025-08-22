package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Grid_Layout extends JFrame {
  public static void main(String[] args) {
    new Grid_Layout();
  }

  public Grid_Layout() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new GridLayout(3, 3, 10, 10)); // 3 x 3 grid; note: no parameter all items are arranged in 1 row
    // GridLayout is used to arrange the components in a rectangular grid:
    // - one component per rectangle

    // TOP LEVEL CONTAINERS
    // INTERMEDIATE CONTAINERS
    // ATOMIC COMPONENTS

    JButton btn1 = new JButton("1");
    JButton btn2 = new JButton("2");
    JButton btn3 = new JButton("3");
    JButton btn4 = new JButton("4");
    JButton btn5 = new JButton("5");
    JButton btn6 = new JButton("6");
    JButton btn7 = new JButton("7");
    JButton btn8 = new JButton("8");
    JButton btn9 = new JButton("9");

    // assign to the grid
    add(btn1);
    add(btn2);
    add(btn3);
    add(btn4);
    add(btn5);
    add(btn6);
    add(btn7);
    add(btn8);
    add(btn9);

    setVisible(true);
  }
}
