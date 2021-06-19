package ru.code;

//магические числа
public class MagicNumber {
    public static final double PI = 3.1415926535;

    public static void main(String[] args) {
        double radius = 10;

        double areaMagic = calcCircleAreaMagic(radius);
        double area = calcCircleArea(radius);
        System.out.println("areaMagic:" + areaMagic + " area:" + area);

        double circumferenceMagic = calcCircumferenceMagic(radius);
        double circumference = calcCircumference(radius);
        System.out.println("circumferenceMagic:" + circumferenceMagic + " circumference:" + circumference);
    }

    private static double calcCircumferenceMagic(double radius) {
        return 2 * 3.1415926535 * radius;
    }

    public static double calcCircleAreaMagic(double radius) {
        return 3.1415926535 * radius * radius;
    }

    public static double calcCircleArea(double radius) {
        return PI * radius * radius;
    }

    private static double calcCircumference(double radius) {
        return 2 * PI * radius;
    }

}
