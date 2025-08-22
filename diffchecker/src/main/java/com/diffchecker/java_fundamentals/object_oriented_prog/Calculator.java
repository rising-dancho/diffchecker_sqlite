package com.diffchecker.java_fundamentals.object_oriented_prog;

// you only put parenthesis for methods not classes
public class Calculator {
  // Encapsulation: make a way to have certain parts of the system inaccessible if
  // not intended to.

  // Getters and Setters
  private Integer num1;
  private Integer num2;

  // Setters
  public void setNum1(Integer num1) {
    this.num1 = num1;
  }

  public void setNum2(Integer num2) {
    this.num2 = num2;
  }

  // Getters
  public Integer getNum1() {
    return num1;
  }

  public Integer getNum2() {
    return num2;
  }

  public Integer addTwoNumbers(int num1, int num2){
    Integer result = num1 + num2;
    return result;
  }
}