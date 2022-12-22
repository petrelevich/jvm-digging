package ru.petrelevich;

public class ArraySearchDemo {
    public static void main(String[] args) {
        int[] array = {1, 2, 44, 323, 4, 7, 88, 6};

        int search = 2;
        boolean searchResult = false;
        for (int idx = 0; idx < array.length && !searchResult; idx++) {
            System.out.println("current item:" + array[idx]);
            if (array[idx] == search) {
                searchResult = true;
            }
        }

        System.out.println("searchResult:" + searchResult);
    }
}
