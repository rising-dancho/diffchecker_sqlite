package com.diffchecker.java_fundamentals;

public class JavaFundamentals_primitives_integers {
  public static void main(String[] args) {
    byte a = Byte.MAX_VALUE;
    short b = Short.MAX_VALUE;
    int c = Integer.MAX_VALUE;
    long d = Long.MAX_VALUE;

    byte e = Byte.MIN_VALUE;
    short f = Short.MIN_VALUE;
    int g = Integer.MIN_VALUE;
    long h = Long.MIN_VALUE;

    System.out.println("max byte = " + a);
    System.out.println("max short = " + b);
    System.out.println("max int = " + c);
    System.out.println("max long = " + d);

    System.out.println();

    System.out.println("min byte = " + e);
    System.out.println("min short = " + f);
    System.out.println("min int = " + g);
    System.out.println("min long = " + h);
  }
}
/*
 * PRIMITIVE DATA TYPES: (only one value at a time)
 * - Integer (byte, short, int, long)
 * - Floating Point (float, double)
 * - Boolean (true, false)
 * - Character
 * 
 * NON-PRIMITIVE TYPES: (can hold multiple values)
 * - String
 * - Array
 * - List
 * - Map
 * - Set
 * - Object
 * 
 */