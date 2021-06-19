package ru.code;

//чистые функции и Side Effects
public class CleanFunction {
    public static void main(String[] args) {
        double result = cleanFunction(4);
        System.out.println(result);

        double resultNotClean = notCleanFunction(5);
        System.out.println(resultNotClean);
    }

    public static double cleanFunction(double val) {
        return val * val;
    }

    public static double notCleanFunction(double val) {
        double result = val * val;
        System.out.println("calculation result:" + result);
        return result;
    }

}
