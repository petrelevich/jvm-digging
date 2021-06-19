package ru.code;

import java.util.Arrays;

//наименования
public class Naming {
    public static void main(String[] args) {

        int[] a1 = new int[]{1, 2, 3, 4, 5};
        int[] a2 = f(a1);

        System.out.println(Arrays.toString(a1));
        System.out.println(Arrays.toString(a2));

        //////////////
        int[] inputArray = new int[]{1, 2, 3, 4, 5};
        int[] reversedArray = reverse(inputArray);

        System.out.println(Arrays.toString(inputArray));
        System.out.println(Arrays.toString(reversedArray));
    }

    public static int[] f(int[] a1) {
        int[] a2 = new int[a1.length];
        int j = 0;
        for (int i = a1.length - 1; i >= 0; i--) {
            a2[j++] = a1[i];
        }
        return a2;
    }

    public static int[] reverse(int[] inputArray) {
        int[] reversedArray = new int[inputArray.length];
        int reversedArrayIdx = 0;
        for (int inputArrayIdx = inputArray.length - 1; inputArrayIdx >= 0; inputArrayIdx--) {
            reversedArray[reversedArrayIdx++] = inputArray[inputArrayIdx];
        }
        return reversedArray;
    }
}
