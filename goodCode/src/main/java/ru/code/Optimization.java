package ru.code;

//Преждевременная оптимизация
public class Optimization {
    public static void main(String[] args) {
        int result = multiplication(2);
        System.out.println(result);

        result = multiplicationCommon(2);
        System.out.println(result);

        result = multiplication(3);
        System.out.println(result);

        result = multiplicationCommon(3);
        System.out.println(result);
    }

    public static int multiplication(int val) {
        return val << 1;
    }

    public static int multiplicationCommon(int val) {
        return val * 2;
    }
}
