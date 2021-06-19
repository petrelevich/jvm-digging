package ru.code;

//Single Responsibility
public class SingleResponsibility {
    public static void main(String[] args) {
        calcSquareArea(2);

        double squareArea = calcSquareAreaSR(2);
        System.out.println(squareArea);
    }

    public static void calcSquareArea(double side) {
        double squareArea = side * side;
        System.out.println(squareArea);
    }

    private static double calcSquareAreaSR(double side) {
        return side * side;
    }

}
