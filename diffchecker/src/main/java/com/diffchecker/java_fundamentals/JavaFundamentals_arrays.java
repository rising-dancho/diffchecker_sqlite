package com.diffchecker.java_fundamentals;

public class JavaFundamentals_arrays {
  public static void main(String[] args) {
    // ACCESSING VALUES OF AN ARRAY USING:
    // Regular For Loop
    int[] arr = new int[] { 10, 20, 50, 100 };
    for (int i = 0; i < arr.length; i++) {
      System.out.println("array values = " + arr[i]);
    }

    // ACCESSING VALUES OF AN ARRAY USING:
    // ForEach Loop
    for (int element : arr) {
      System.out.println(element);
    }

  }
}
