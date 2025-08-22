package com.diffchecker.java_fundamentals.object_oriented_prog.youtube_tutorials;

public class GettersSetters {
  // attributes
  String protein = "Ground beef";
  int cookingTime = 20;
  String pasta = "Spaghetti";

  // behaviors: actions
  public String eat() {
    String result = "Eat Banana";
    return result;
  }

  public void eatFood() {
    System.out.println("Eating " + pasta);
  }

  public static void main(String args[]) {
    // instance : object
    GettersSetters john = new GettersSetters();
    GettersSetters bob = new GettersSetters();

    // use attributes
    System.out.println("John is trying to cook " + john.pasta + " and his protein is " + john.protein
        + ". He will prepare and cook everything within " + john.cookingTime + "mins.");

    // call methods
    System.out.println(bob.eat());
    bob.eatFood();

  }

}
