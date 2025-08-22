package com.diffchecker.java_fundamentals;

public class JavaFundamentals_string_methods {
  public static void main(String[] args) {
    // Strings are classes
    // We can create "objects" of Strings. eg. String l = "ligma";
    // We can then use it's methods ligma.concat("ballz");

    // ----> STRING METHODS <----

    // CONCAT
    String l = "ligma";
    System.out.println(l.concat("ballz"));
    // output: ligmaballz

    // think of Classes as recipes
    // "Objects" are the outputs of the Classes/recipes.
    // eg. Recipe (Class) => Output: Fried Rice (Object)
    // Scout scout1 = new Scout("Erwin");
    // Scout scout2 = new Scout("Levi");

    // CONTAINS
    String assCheeks = "assCheeks";
    System.out.println(assCheeks.contains("ass"));
    // output: true

    // INDEX OF
    // in java checking indexes always starts with 0
    // eg. "Hello" => (indexes) 0 1 2 3 4
    String abc = "Hello";
    System.out.println(abc.indexOf("o"));
    // output: 4

    // CHAR AT
    String cba = "Werld";
    System.out.println(cba.charAt(2));
    // output: r

    // REPLACE
    // example.replace(TARGET,REPLACEMENT);
    String example = "Hello World";
    System.out.println(example.replace("Wor", "Bo"));
    // output: Hello Bold

    // TO UPPER CASE, TO LOWER CASE
    String akiraNakai = "rauh welt";
    System.out.println(akiraNakai.toUpperCase());
    // output: RAUH WELT
    System.out.println(akiraNakai.toLowerCase());
    // output: rauh welt

    // Write a program that prints out the individual characters of a string
    String words = "Bitlog generation";

    for (int i = 0; i <= words.length() - 1; i++) {
      System.out.println(words.charAt(i));
    }
    /*
     * output:
     * B
     * i
     * t
     * l
     * o
     * g
     * 
     * g
     * e
     * n
     * e
     * r
     * a
     * t
     * i
     * o
     * n
     */

    // Write a program that can take parts of texts from a sentence
    String beth = "Bethany Logan";
    System.out.println(beth.substring(0, 2 + 1) + beth.substring(8, 10 + 1));

    // A MORE COMPLEX EXAMPLE
    String data = "abcdef";
    for (int i = 0; i < data.length(); i += 3) {
      // .min chooses the smaller of the two inputs
      int end = Math.min(i + 3, data.length()); // this is here just to make sure it doesnt go past the string's length
      System.out.println(data.substring(i, end));
    }

    // WHEN YOU SAY: .length in Java.. it always starts the counting with 1
    // WHEN YOU SAY: .indexOf in Java.. since you're talking about indexes it starts with 0

  }
}
