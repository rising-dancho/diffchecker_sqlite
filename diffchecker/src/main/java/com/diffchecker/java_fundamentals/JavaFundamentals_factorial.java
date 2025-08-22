package com.diffchecker.java_fundamentals;

public class JavaFundamentals_factorial {
  public static void main(String[] args) {
    // Exercise 1:
    // - write a program that calculates the factorial of 10
    // eg. 10! = 10*9*8*7*6*5*4*3*2*1

    int result = 1;
    for (int i = 10; i >= 1; i--) {
      // Multuply the previous index to the new index in the loop
      // to simulate the factorial
      result = result * i;
      // System.out.println(result);

      // CONDITIONAL FOR SHOWING THE RESULT OF THE FACTORIAL
      if (i == 1) {
        System.out.print(i + " = " + result);
        return;
      }
      // ONLY FOR VISUALIZING THE FACTORIAL
      System.out.print(i + "*");
    }

  }
}
