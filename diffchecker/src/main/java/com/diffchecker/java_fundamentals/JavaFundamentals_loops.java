package com.diffchecker.java_fundamentals;

public class JavaFundamentals_loops {
  public static void main(String[] args) {
    // EXAMPLE 1: square of each number
    // System.out.println(1 + " squared is = " + (1*1));
    // System.out.println(2 + " squared is = " + (2*2));
    // System.out.println(3 + " squared is = " + (3*3));

    for (int i = 1; i <= 3; i++) {
      // System.out.println("i = " + i);
      System.out.println(i + " squared is = " + (i * i));
    }

    // EXAMPLE 2: tracing the loop
    /*
     * Step             Code          Description  
     * initialization   int i=1       Variable i is created and initialized to 1 (step 1)
     * 
     * Test1            i<=3          True, we can enter the loop (step 2)
     * Body             {...}         Execute the println with i equal to 1
     * Update           i++           Increment i, which becomes 2
     * 
     * Test2            i<=3          True, we can enter the loop (step 3)
     * Body             {...}         Execute the println with i equal to 2
     * Update           i++           Increment i, which becomes 3
     * 
     * Test3            i<=3          True, we can enter the loop (step 4)
     * Body             {...}         Execute the println with i equal to 3
     * Update           i++           Increment i, which becomes 4
     * 
     * Test4            i<=3          False, we do not enter the loop (step 5)
     * Body             {...}         Does not execute
     * Update           i++           Does not increment
     * 
     * 
     */

    //  DO WHILE
    int x = 1;

    do{
      System.out.println("x = " +x);
      x++;
    }while(x <=3);
  }
}
