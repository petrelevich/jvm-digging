package ru.code;

public class QuadraticEquation {
    public static void main(String[] args)  {
        //ax^{2}+bx+c=0

        //  case1();
        //  case2();
        case3();
    }

    public static void case1()  {
        double a = 1;
        double b = 7;
        double c = 6;
        Roots roots = calcRoots(a, b, c);
        System.out.println(roots);

        double check1 = a * roots.getX1() * roots.getX1() + b * roots.getX1() + c;
        if (check1 != 0) {
            throw new AssertionError("wrong result");
        }

        double check2 = a * roots.getX2() * roots.getX2() + b * roots.getX2() + c;
        if (check2 != 0) {
            throw new AssertionError("wrong result");
        }
    }

    public static void case2() {
        double a = 2;
        double b = 7;
        double c = -4;
        Roots roots = calcRoots(a, b, c);
        System.out.println(roots);

        double check1 = a * roots.getX1() * roots.getX1() + b * roots.getX1() + c;
        if (check1 != 0) {
            throw new AssertionError("wrong result");
        }

        double check2 = a * roots.getX2() * roots.getX2() + b * roots.getX2() + c;
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

    public static Roots calcRoots(double a, double b, double c)  {
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            throw new IllegalArgumentException("Discriminant can't be less then 0, discriminant:" + discriminant);
        }

        double x1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double x2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        return new Roots(x1, x2);
    }

    public static class Roots {
        private final double x1;
        private final double x2;

        public Roots(double x1, double x2) {
            this.x1 = x1;
            this.x2 = x2;
        }

        public double getX1() {
            return x1;
        }

        public double getX2() {
            return x2;
        }

        @Override
        public String toString() {
            return "Roots{" +
                    "x1=" + x1 +
                    ", x2=" + x2 +
                    '}';
        }
    }
}
