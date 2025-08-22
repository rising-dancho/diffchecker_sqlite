package com.diffchecker.java_fundamentals;

import java.util.Scanner;

public class JavaFundamentals_average_array {
  public static void main(String[] args) {
    // Write a java program that allows the user to enter
    // 10 numbers and get the average

    // PSUEDO CODE
    // - take the user input
    // - declare the array with length of 10
    // - assign values to it the array by looping and link it to the scanner
    // - calculate the average
    // - show the result

    // 1.) take the user input
    Scanner scanner = new Scanner(System.in);

    // 2.) declare the array with length of 10
    int arr[] = new int[10];

    // 3.) assign values to it the array by looping and link it to the scanner
    for (int i = 0; i < arr.length; i++) {
      arr[i] = scanner.nextInt();
    }

    float average = 0;
    // 4.)  calculate the average
    for(int x : arr){
      average = average + x/arr.length;
    }

    // 5.) show the result
    System.out.println(average);
    scanner.close();
  }
}
