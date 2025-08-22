package com.diffchecker.java_fundamentals.object_oriented_prog;

public class encapsulation {
  public static void main(String args[]) {
    Calculator calc = new Calculator();
    // Setter
    calc.setNum1(2);
    calc.setNum2(3);

    // Getter
    int x = calc.getNum1();
    int y = calc.getNum2();

    System.out.println(calc.addTwoNumbers(x, y));
  }

}