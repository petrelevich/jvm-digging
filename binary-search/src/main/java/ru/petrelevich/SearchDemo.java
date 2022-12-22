package ru.petrelevich;

import java.util.Arrays;

public class SearchDemo {
    public static void main(String[] args) {
        int[] array = {4, 14, 1, 3, 8, 6, 10, 13, 7};
        Arrays.sort(array);
        System.out.println("sorted:" + Arrays.toString(array));

        int search = 3;
        boolean searchResult = search(array, search);
        System.out.println("searchResult:" + searchResult);
    }

    private static boolean search(int[] array, int search) {
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int midVal = array[mid];

            if (midVal == search) {
                return true;
            }

            if (midVal < search) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }
}
