package ru.demo;

import java.util.HashMap;
import java.util.Map;

public class DemoMap {
    public static void main(String[] args) {
        Cat murza = new Cat("Мурзик", "серый", 2);
        Cat vasy = new Cat("Василий", "чёрный", 3);
        Cat dymka = new Cat("Дымка", "белая", 1);

        HashMap<Cat, Integer> cats = new HashMap<>();
        cats.put(murza, 1);
        cats.put(vasy, 3);
        cats.put(dymka, 6);

        for(Map.Entry<Cat, Integer> cat: cats.entrySet()) {
            System.out.println(cat);
        }
        System.out.println("Можно еще так вывести:");
        cats.entrySet().forEach(System.out::println);

        int val = cats.get(murza);
        System.out.println("murza val:" + val);

        murza.setWight(4);
        int val2 = cats.get(murza);
        System.out.println("murza val2:" + val2);

        Cat murzaSecond = new Cat(murza.getName(), murza.getColor(), murza.getWight());
        Integer valSecond = cats.get(murzaSecond);
        System.out.println("murzaSecond val:" + valSecond);
    }
}
