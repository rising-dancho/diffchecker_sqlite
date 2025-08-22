package com.diffchecker.java_fundamentals.object_oriented_prog;

public class static_id {
  public static void main(String args[]) {
    UserId userId1 = new UserId();
    UserId userId2 = new UserId();
    System.out.println(userId1.getUserId());
    System.out.println(userId2.getUserId());
  }

  
}
