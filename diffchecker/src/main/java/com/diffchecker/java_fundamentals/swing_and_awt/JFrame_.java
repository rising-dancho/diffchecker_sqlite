package com.diffchecker.java_fundamentals.swing_and_awt;

import java.awt.HeadlessException;

import javax.swing.JFrame;

public class JFrame_ extends JFrame {
  public static void main(String[] args) {
    // BASIC WAY
    // JFrame jFrame = new JFrame("First Swing GUI");
    // jFrame.setSize(1080, 720);
    // jFrame.setLocationRelativeTo(null);
    // jFrame.setVisible(true);

    // CLEANER WAY
    new JFrame_();
  }

  public JFrame_() throws HeadlessException {
    // always do these inside the constructor
    setTitle("First Swing GUI");
    setSize(1080, 720);
    setLocationRelativeTo(null);
    setVisible(true);
  }

}
