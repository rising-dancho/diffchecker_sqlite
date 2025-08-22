package com.diffchecker.java_fundamentals.object_oriented_prog;

public class UserId {
  private static int idCounter = 0; // shared id counter for all objects created
  private int userId;

  public UserId() {
    userId = ++idCounter; // pre increment is crucial, increment the idCounter first BEFORE assigning to userId
  }

  public int getUserId() {
    return userId;
  }

}
