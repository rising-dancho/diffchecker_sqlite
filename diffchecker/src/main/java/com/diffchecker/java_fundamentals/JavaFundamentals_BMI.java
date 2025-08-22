package com.diffchecker.java_fundamentals;

import java.util.Scanner;

public class JavaFundamentals_BMI {
  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    // ⚖️ Weight / Mass
    // Unit Equivalent
    // 1 kilogram (kg) 2.20462262185 pounds (lbs) exact
    // 1 pound (lb) 0.453592 kilograms (kg)
    // 1 kilogram 1,000 grams (g)
    // 1 gram 1,000 milligrams (mg)
    // 1 gram 1,000 milligrams (mg)
    // 1 ounce (oz) 28.3495 grams (g)
    // 1 pound (lb) 16 ounces (oz)

    // 📏 BASIC CONVERSION TABLE
    // Unit Equivalent
    // 1 feet 12 inches
    // 1 inch 2.54 centimeters
    // 1 centimeters 0.01 meters
    // 1 meters 100 centimeters

    // FOR FUN:
    // - convert kg to lbs and vice versa
    // - convert feet/inches to meters and vice versa
    // - calculate personal BMI

    System.out.print("Please enter your weight in kilograms: ");
    float weight_kg = scanner.nextFloat();

    System.out.print("Please enter height (feet only): ");
    float heightFeet = scanner.nextFloat();

    System.out.print("Please enter height (inches only): ");
    float heightInches = scanner.nextFloat();

    // CONSTANT
    float POUND = 2.20462262185f; // this also equals 1 kg

    // spacer
    System.out.println();

    // 1.) - convert kg to lbs and vice versa
    float lbs = weight_kg * POUND;
    System.out.println("Weight in Pounds = " + lbs);

    float kg = lbs / POUND;
    System.out.println("Weight in Kilograms = " + kg);

    // spacer
    System.out.println();

    // 2.) - convert foot"inches to metres and vice versa
    // psuedo code:
    // - we need to convert everything to inches and then convert
    // inches into centimeters since that's what we see on the conversion table
    // - convert foot to centimeters + add the remaining inches
    // (cenverted into
    // centimeters) to get the total centimeters
    // - convert centimeters to meters

    // CONSTANTS
    float INCHES_PER_FOOT = 12f; // this also equals 1ft
    float CM_PER_INCH = 2.54f; // this also equals 1 inch
    float CM_PER_METER = 100.0f;
    float CM_PER_FOOT = (CM_PER_INCH * INCHES_PER_FOOT);

    float heightInCM = (heightFeet * CM_PER_FOOT) + (heightInches * CM_PER_INCH);
    float heightInMeters = heightInCM / CM_PER_METER;

    System.out.println("Height in Centimeters = " + heightInCM);
    System.out.println("Height in Meters = " + heightInMeters);

    // 3.) - calculate personal BMI
    // Write a java program that allow the user
    // to enter weight and height, and display
    // the body mass index for this user.
    // the formula BMI = kg/m2 (metres squared)

    // spacer
    System.out.println();

    // Body Mass Index (BMI)
    // Underweight: < 18.5
    // Normal weight: ≤ 18.5 or < 24.9
    // Overweight: ≤ 25 or < 29.9
    // Obese I (Moderate): ≤ 30 or < 34.9
    // Obese II (Severe): ≤ 35 or < 34.9
    // Obese III (Very severe or morbid obesity): ≥ 40

    float BMI = kg / (heightInMeters * heightInMeters);
    System.out.println("Body Mass Index (BMI) = " + BMI);

    if (BMI < 18.5) {
      System.out.println("Category: Underweight");
    } else if (BMI <= 18.5 || BMI < 24.9) {
      System.out.println("Category: Normal weight");
    } else if (BMI <= 25 || BMI < 29.9) {
      System.out.println("Category: Overweight");
    } else if (BMI <= 30 || BMI < 34.9) {
      System.out.println("Category: Obese Class I");
    } else if (BMI <= 35 || BMI < 34.9) {
      System.out.println("Category: Obese Class II");
    } else {
      System.out.println("Category: Obese Class III");
    }

    scanner.close(); // Close the scanner
  }
}
