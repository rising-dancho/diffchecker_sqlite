package com.diffchecker.java_fundamentals;

public class JavaFundamentals_data_type_conversion {
  public static void main(String[] args) {
    // Write a program that converts a string to integer and vice versa
    String n = "10";
    String o = "6";

    int i = Integer.parseInt(n);
    int j = Integer.parseInt(o);

    System.out.println("l (String) = " + n);
    System.out.println("m (String) = " + o);
    System.out.println();
    System.out.println("i (int) = " + i);
    System.out.println("j (int) = " + j);
    System.out.println();
    System.out.println("i + j (converted to Integer) = " + (i + j));

    String k = String.valueOf(i);
    String l = String.valueOf(j);

    System.out.println();
    System.out.println("i (int) = " + i);
    System.out.println("j (int) = " + j);
    System.out.println();
    System.out.println("k (String) = " + k);
    System.out.println("l (Sting) = " + l);
    System.out.println();
    System.out.println("k + l (converted back to String) = " + (k + l));

  }
}
