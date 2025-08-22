package com.diffchecker.java_fundamentals;

import java.util.Scanner;

public class JavaFundamentals_comparing_two_numbers {
  public static void main(String[] args) {
    // Exercise 2:
    // - write a program that compares two numbers entered by user. (greater than,
    // less than, equal)

    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter first number: ");
    int x = scanner.nextInt();

    System.out.print("Enter second number: ");
    int y = scanner.nextInt();

    if (x > y) {
      System.out.println(x + " is greater than " + y);

    } else if (y > x) {
      System.out.println(y + " is greater than " + x);
    } else {
      System.out.println(x + " is equal to " + y);
    }
    scanner.close();
  }
}
