package com.diffchecker.java_fundamentals.object_oriented_prog;

public class method_overloading {
  public static void main(String args[]) {
    double sum = AddNumbers(10, 5);
    System.out.println("Sum: " + sum);
  }

  // add numbers
  public static double AddNumbers(double x, double y) {
    double result = x + y;
    return result;
  }

  // Method Overloading. multiple having the same name but different primitive
  // types
  public static int AddNumbers(int x, int y, int z) {
    int result = x + y + z;
    return result;
  }

  public static int AddNumbers(int x, int y, int z, int w) {
    int result = x + y + z + w;
    return result;
  }

}
