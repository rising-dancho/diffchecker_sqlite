package com.diffchecker.java_fundamentals.object_oriented_prog.youtube_tutorials;

public class MethodMain {

  public static void main(String args[]) {
    // instance of a class: objects
    AboutClasses example1 = new AboutClasses();
    AboutClasses example2 = new AboutClasses();

    System.out.println(example1.protein);
    System.out.println(example2.pasta);

    // calling the methods or behavior
    example1.eat();
    System.out.println(example1.cooking() + example1.cooking());
  }
}
