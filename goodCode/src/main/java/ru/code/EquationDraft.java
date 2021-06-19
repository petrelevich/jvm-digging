package ru.code;

import java.util.Arrays;

public class EquationDraft {
    public static void main(String[] args)  {
        case1();
        case2();
        case3();
    }

    public static void case1()  {
        double a = 1;
        double b = 7;
        double c = 6;
        double[] result = calcRoots(a, b, c);
        System.out.println(Arrays.toString(result));

        double check1 = a * result[0] * result[0] + b * result[0] + c;
        if (check1 != 0) {
            throw new AssertionError("wrong result");
        }

        double check2 = a * result[1] * result[1] + b * result[1] + c;
        if (check2 != 0) {
            throw new AssertionError("wrong result");
        }
    }

    public static void case2() {
        double a = 2;
        double b = 7;
        double c = -4;
        double[] result = calcRoots(a, b, c);
        System.out.println(Arrays.toString(result));

        double check1 = a * result[0] * result[0] + b * result[0] + c;
        if (check1 != 0) {
            throw new AssertionError("wrong result");
        }

        double check2 = a * result[1] * result[1] + b * result[1] + c;
        if (check2 != 0) {
            throw new AssertionError("wrong result");
        }
    }

    public static void case3() {
        double a = 200;
        double b = 7;
        double c = 4;

        try {
            calcRoots(a, b, c);
            throw new AssertionError("wrong result");
        } catch (IllegalArgumentException ex) {
        }
    }

    public static double[] calcRoots(double a, double b, double c) {
        if (b * b - 4 * a * c < 0) {
            throw new IllegalArgumentException("error");
        }

        double x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        double x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);

        return new double[]{x1, x2};
    }
}
