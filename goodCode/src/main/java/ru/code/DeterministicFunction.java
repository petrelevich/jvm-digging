package ru.code;

//deterministic
public class DeterministicFunction {
    public static void main(String[] args) {
        String deterministicResult = deterministic("aaa");
        System.out.println(deterministicResult);

        String notDeterministicResult = notDeterministic("aaa");
        System.out.println(notDeterministicResult);

        String notDeterministicResult2 = notDeterministic("aaa");
        System.out.println(notDeterministicResult2);
    }

    public static String deterministic(String str) {
        return str.toUpperCase();
    }

    public static String notDeterministic(String str) {
        return str.toUpperCase() + System.currentTimeMillis();
    }
}
