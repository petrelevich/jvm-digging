package ru.petrelevich;

import java.util.Arrays;

public class Primitive {
    public static void main(String[] args) {
       // floatExample();
       // doubleEqual();
       // mixedTypes();
       // maxValues();

       // octalHexFormat();
       // binaryFormat();

       // charExample();
    }


    static void floatExample() {
        float f1 = 9.02F;
        System.out.println("f1:" + f1);

        float f2 = 900F;
        System.out.println("f2:" + f2);

        float f3 = 9e+2F;
        System.out.println("f3:" + f3);

        float f4 = 9e-2F;
        System.out.println("f4:" + f4);

        float f5 = .5f;
        System.out.println("f5:" + f5);

        float f6 = Float.NaN;
        System.out.println("f6:" + f6);

        float f7 = Float.NEGATIVE_INFINITY;
        System.out.println("f7:" + f7);

        float f8 = Float.POSITIVE_INFINITY;
        System.out.println("f7:" + f8);
    }

    static void doubleEqual() {
        double d1 = 1.001 - 0.001;
        double d2 = 1.0;

        if (d1 == d2) {
            System.out.println("equal");
        } else {
            System.out.println("NOT equal");
        }
        System.out.println("d1:" + d1 + " d2:" + d2);
    }

    static void mixedTypes() {
        int x1 = 5 / 6;
        System.out.println("x1:" + x1);

        double x2 = 5 / 6;
        System.out.println("x2:" + x2);

        double x3 = 5 / 6.0;
        System.out.println("x3:" + x3);
    }

    static void maxValues() {
        long max = Long.MAX_VALUE;
        System.out.println("max:" + max);
        max++;
        System.out.println("max:" + max);
        System.out.println("MIN_VALUE:" + Long.MIN_VALUE);
    }

    static void octalHexFormat() {
        int x = 8;
        System.out.println("x in octal format:" + Integer.toOctalString(x));
        int xOct = 010;
        System.out.println("xOct:" + xOct);

        int radix16 = 16;
        System.out.println("radix16 in hex format:" + Integer.toHexString(radix16));

        int xHex = 0xFF;
        System.out.println("xHex:" + xHex);
    }

    static void binaryFormat() {
        long b = 0b0010;
        System.out.println("b:" + b);
        System.out.println("asBinString:" + Long.toBinaryString(b));

        int minusOne = -1;
        System.out.println("minusOneAsBinString:" + Integer.toBinaryString(minusOne));

        byte sh = Byte.parseByte("00001000", 2);
        System.out.println("sh:" + sh);
        byte sh2 = (byte) (sh | 0b10000000); // обратите внимание на приведение типа
        System.out.println("sh:" + Integer.toBinaryString(sh2 & 0xFF));
    }

    static void charExample() {
        char ch1 = 'a';
        System.out.println("ch1:" + ch1);

        char ch2 = 97;
        System.out.println("ch2:" + ch2);

        char a = '\u0061';
        System.out.println("a:" + a);

        var charArray = "abc".toCharArray();
        System.out.println("charArray:" + Arrays.toString(charArray));
        System.out.println("charArray[1]:" + charArray[1]);
    }
}
