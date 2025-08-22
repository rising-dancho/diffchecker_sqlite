package com.diffchecker.java_fundamentals;

import java.util.Scanner;

public class JavaFundamentals_sum_individual_digit {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter a number and i will sum each of it's digits: ");
    int number = scanner.nextInt();

    int original = number;

    int sum = 0;
    int last_digit = 0;

    while (number != 0) {
      // THIS EXTRACTS THE LAST DIGIT OF THE ENTERED NUMBER:
      // eg. 56 --> last digit is 6.. (loop iterates) next digit --> 5

      last_digit = number % 10;
      System.out.println(last_digit);
      sum = sum + last_digit;

      // THIS LINE PREPARES THE NEXT SUMMATION BY REMOVING THE LAST DIGIT FROM THE
      // PREVIOUS ITERATION
      number = number / 10;
      // System.out.println(number);
    }

    System.out.println();
    System.out.println("Sum of each digit of the entered number " + original + " is : " + sum);
    scanner.close();
  }
}

// EXPLANATION
// https://chatgpt.com/share/684dd8c2-8580-8000-92ec-e499585b2dde
