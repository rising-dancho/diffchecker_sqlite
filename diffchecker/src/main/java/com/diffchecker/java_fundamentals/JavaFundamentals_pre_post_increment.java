package com.diffchecker.java_fundamentals;

public class JavaFundamentals_pre_post_increment {
  public static void main(String[] args) {
    char[] chars = "abcde".toCharArray();
    int i = -1;

    while (++i < chars.length) {
      char c = chars[i];
      // Check for something, like a sequence
      if (c == 'c') {
        System.out.println("Found c at index " + i);
      }
    }

    // EXTRA INFO: TYPE CASTING

    int x;
    double y;

    y = 6+4; // promoting the int values into double
    x = (int) (25.75 + y); // sort of demoting the double value into an int; decimals are truncated

    System.out.println("x:" +x);
    System.out.println("y:" +y);

  }
}
