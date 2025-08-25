package com.diffchecker.backup.java_fundamentals.swing_and_awt;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JButton_ extends JFrame {
  public static void main(String[] args) {
    new JButton_();
  }

  public JButton_() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    // setLayout(new BorderLayout());
    setLayout(null);

    // TOP LEVEL CONTAINERS
    // INTERMEDIATE CONTAINERS
    // ATOMIC COMPONENTS

    JButton btn1 = new JButton();
    btn1.setText("Click Me");
    btn1.setSize(100, 30);
    btn1.setLocation(0, 0);
    btn1.setBackground(Color.WHITE);

    // Add action listener
    btn1.addActionListener(new ActionListener() {
      boolean check = true;

      @Override
      public void actionPerformed(ActionEvent e) {
        // CHANGING BTN BACKGROUND COLOR
        // if (check == false) {
        // btn1.setBackground(Color.WHITE);
        // check = true;
        // } else {
        // btn1.setBackground(Color.ORANGE);
        // check = false;
        // }

        // CHANGING JFRAME BACKGROUND COLOR
        if (check == false) {
          getContentPane().setBackground(Color.WHITE);
          check = true;
        } else {
          getContentPane().setBackground(Color.ORANGE);
          check = false;
        }

        // throw new UnsupportedOperationException("Unimplemented method
        // 'actionPerformed'");
      }

    });

    add(btn1);
    setVisible(true);
  }
}
