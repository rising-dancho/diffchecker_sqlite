package com.diffchecker.java_fundamentals;

public class JavaFundamentals_remove_item_array {
  public static void main(String[] args) {
    // Write a java program that removes an element from an array

    int[] arr = { 25, 14, 16, 262, 56, 88 };

    // remove an element: 2nd element
    int removingIndex = 1;

    for (int i = removingIndex; i < arr.length - 1; i++)
      arr[i] = arr[i + 1]; // Shift elements to the left

    // Print the new array up to arr.length - 1
    for (int i = 0; i < arr.length - 1; i++) // notice that i starts at 0
      System.out.println(arr[i]);

  }
}
