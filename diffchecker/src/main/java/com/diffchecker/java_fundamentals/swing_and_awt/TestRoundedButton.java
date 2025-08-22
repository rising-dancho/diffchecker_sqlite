package com.diffchecker.java_fundamentals.swing_and_awt;

import com.diffchecker.components.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class TestRoundedButton {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Rounded Button Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200);
    frame.setLayout(new FlowLayout());

    RoundedButton button = new RoundedButton("Click Me");
    button.setBackgroundColor(new Color(0x00C281));
    button.setHoverBackgroundColor(new Color(0x009966)); // <- hover color
    button.setTextColor(Color.WHITE);
    button.setBorderColor(new Color(0x00C281));
    button.setHoverBorderColor(new Color(0x009966));
    button.setBorderThickness(2);
    button.setCornerRadius(10);

    frame.add(button);
    frame.setVisible(true);
  }
}
