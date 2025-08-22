package com.diffchecker.java_fundamentals.swing_and_awt;

import javax.swing.JOptionPane;

public class JOption_pane {
  public static void main(String[] args) {
    // JOptionPane
    // 1 - input dialog
    // 2 - message dialog

    // obtain user input from JOptionPane input dialogs
    String firstNumber = JOptionPane.showInputDialog("Enter first integer: ");
    String secondNumber = JOptionPane.showInputDialog("Enter second integer: ");

    // convert from string to integer
    int num1 = Integer.parseInt(firstNumber);
    int num2 = Integer.parseInt(secondNumber);

    int sum = num1 + num2;

    // display results
    JOptionPane.showMessageDialog(null, "Total: " + sum, "Sum of " + num1 + " + " + num2,
        JOptionPane.PLAIN_MESSAGE);
  }
}
