package com.diffchecker.java_fundamentals;

import java.util.Scanner;

public class JavaFundamentals_sum_array {
  public static void main(String[] args) {
    // Write a java program that allows the user to enter
    // 10 numbers and gives their sum
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter 10 numbers to sum:");
    int[] arr = new int[10]; // declaring an array with the length of 10

    // taking the input and putting the value inside arr
    for (int i = 0; i < arr.length; i++) {
      arr[i] = scanner.nextInt();
    }

    // summation of the values inside arr
    int sum = 0;
    for (int value : arr) {
      // add i to the previous sum and save it to a new sum
      sum += value;
    }

    System.out.println(sum);
    scanner.close();
  }
}
