package ru.petrelevich;

import java.util.Arrays;

public class SearchDemoLib {
    public static void main(String[] args) {
        int[] array = {4, 14, 1, 3, 8, 6, 10, 13, 7};
        Arrays.sort(array);
        System.out.println("sorted:" + Arrays.toString(array));

        int search = 34;
        boolean searchResult = Arrays.binarySearch(array, search) >= 0;
        System.out.println("searchResult:" + searchResult);
    }
}
