package com.diffchecker.java_fundamentals.object_oriented_prog;

public class oop_main {
  public static void main(String args[]) {
    System.out.println();

    // now how do we use the state?
    // By creating Getters and Setters

    // OPTION 1: direct
    // this is creating an instance (or an object) of a the Student class
    // Student person = new Student("Benjamin Button", 12, "blue", 'm');
    // Getters : for the default constructor at the top
    // System.out.println("Name: " + person.name);
    // System.out.println("Age: " + person.age);
    // System.out.println("Color: " + person.color);
    // System.out.println("Gender: " + person.sex);

    Student person = new Student();

    // Setters
    String name = person.name = "Benjamin Button";
    Integer age = person.age = 12;
    String color = person.color = "blue";
    char sex = person.sex = 'm';

    // Getters : for the default constructor at the top
    System.out.println("Name: " + name);
    System.out.println("Age: " + age);
    System.out.println("Color: " + color);
    System.out.println("Gender: " + sex);

    person.Eating("burger");
    person.Drinking();
    person.Running();

    System.out.println();
    System.out.println();

    // OPTION 2: OOP Style
    Student_sexy_version john = new Student_sexy_version();
    // Sexy Version: Setters
    // these are private values  
    john.setName("John Kwik");
    john.setAge(15);
    john.setColor("Blue");
    john.setSex('M');

    // Sexy Version: Getters
    System.out.println(john.getName());
    System.out.println(john.getAge());
    System.out.println(john.getColor());
    System.out.println(john.getSex());

    // multiple parameters, returning value from a method

    Integer ageOfCelebrant = 33;
    String nameOfCelebrant = "Vitlog";
    Greeting(ageOfCelebrant, nameOfCelebrant);

    // returning_type_method test = new returning_type_method();
    double num1 = 42.5;
    double num2 = 76.87;

    double sum = AddTwoNumbers(num1, num2);
    // double sum = test.AddTwoNumbers(num1, num2);

    System.out.println(num1 + " + " + num2 + " = " + sum);

  }

  public static double AddTwoNumbers(double num1, double num2) {
    double result = num1 + num2;
    return result;
  }

  // this is a method: its asking for a parameter called "name"
  public static void Greeting(Integer ageOfCelebrant, String nameOfCelebrant) {
    System.out.println("Happy Birthday, " + nameOfCelebrant + "! (candle shows age): " + ageOfCelebrant);
  }

}
