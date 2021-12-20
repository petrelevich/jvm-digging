package ru.demo;

import java.util.HashSet;

public class DemoSet {
    public static void main(String[] args) {
        Cat murza = new Cat("Мурзик", "серый", 2);
        Cat murzaSpy = new Cat("Мурзик", "серый", 2);
        Cat vasy = new Cat("Василий", "чёрный", 3);
        Cat dymka = new Cat("Дымка", "белая", 1);

        HashSet<Cat> cats = new HashSet<>();
        cats.add(murza);
        cats.add(murza);
        cats.add(murza);
        cats.add(murza);
        cats.add(murzaSpy);
        cats.add(vasy);
        cats.add(dymka);

        for(Cat cat: cats) {
            System.out.println(cat);
        }
    }
}
