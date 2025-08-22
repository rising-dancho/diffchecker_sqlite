package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JLabel_ extends JFrame {
  public static void main(String[] args) {
    new JLabel_();
  }

  public JLabel_() {
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new FlowLayout());

    // TOP LEVEL CONTAINERS
    // INTERMEDIATE CONTAINERS
    // ATOMIC COMPONENTS

    JButton btn1 = new JButton();
    btn1.setText("Change Text");
    btn1.setSize(100, 30);
    btn1.setLocation(0, 0);

    JLabel label = new JLabel();
    label.setText("Hello welcome to Jollibee~!");

    // Add action listener
    btn1.addActionListener(new ActionListener() {
      boolean check = false;

      @Override
      public void actionPerformed(ActionEvent e) {
        if (check == false) {
          label.setText("I am changed! NICE ONE!");
          check = true;
        } else {
          label.setText("Hello welcome to Jollibee~!");
          check = false;
        }
      }
    });

    add(btn1);
    add(label);
    setVisible(true);
  }
}
