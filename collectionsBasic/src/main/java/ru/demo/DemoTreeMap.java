package ru.demo;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class DemoTreeMap {
    public static void main(String[] args) {
        Cat murza = new Cat("Мурзик", "серый", 2);
        Cat vasy = new Cat("Василий", "чёрный", 3);
        Cat dymka = new Cat("Дымка", "белая", 1);

        TreeMap<Cat, Integer> cats = new TreeMap<>(new Comparator<Cat>() {
            @Override
            public int compare(Cat cat1, Cat cat2) {
                return cat1.getName().compareTo(cat2.getName());
            }
        });
        cats.put(murza, 1);
        cats.put(vasy, 3);
        cats.put(dymka, 6);

        for(Map.Entry<Cat, Integer> cat: cats.entrySet()) {
            System.out.println(cat);
        }
    }
}
