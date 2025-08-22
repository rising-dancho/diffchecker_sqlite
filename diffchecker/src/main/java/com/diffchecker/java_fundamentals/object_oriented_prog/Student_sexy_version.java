package com.diffchecker.java_fundamentals.object_oriented_prog;

public class Student_sexy_version {
  // Classes - are blueprints, it defines states and behaviors.
  // Objects - are the living instances of the Class.

  // States
  private String name;
  private Integer age;
  private String color;
  private char sex; // f or m

  // Getters
  public String getName(){
    return name;
  }

  public Integer getAge(){
    return age;
  }

  public String getColor(){
    return color;
  }

  public char getSex(){
    return sex;
  }

  // Setters
  public void setName(String name){
    this.name = name;
  }

  public void setAge(Integer age){
    this.age = age;
  }

  public void setColor(String color){
    this.color = color;
  }

  public void setSex(char sex){
    this.sex = sex;
  }

  // Behaviors
  // (methods) are block of codes which only runs when called
  public void Eating(String food) {
    System.out.println("Eating " + food);
  }

  public void Drinking() {
    System.out.println("Drinking");
  }

  public void Running() {
    System.out.println("Running");
  }

}
