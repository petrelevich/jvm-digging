package ru.code;

import java.util.function.IntSupplier;

public class FuncAsArgs {
    public static void main(String[] args) {
        System.out.println(funcAsArgs(4, () -> 4));
    }

    static int funcAsArgs(int x, IntSupplier func) {
        return x + func.getAsInt();
    }
}
