package ru.demo;


import java.util.ArrayList;
import java.util.LinkedList;

public class DemoList {
    public static void main(String[] args) {
        Cat murza = new Cat("Мурзик", "серый", 2);
        Cat vasy = new Cat("Василий", "чёрный", 3);
        Cat dymka = new Cat("Дымка", "белая", 1);
////
        System.out.println("---- ArrayList ----");
        ArrayList<Cat> arrayList = new ArrayList<>();
        arrayList.add(murza);
        arrayList.add(vasy);
        arrayList.add(dymka);

        for (Cat cat : arrayList) {
            System.out.println(cat);
        }
        System.out.println(arrayList.get(0));
        System.out.println(arrayList.get(1));
        System.out.println(arrayList.get(2));

////
        System.out.println("---- LinkedList ----");
        LinkedList<Cat> linkedList = new LinkedList<>();
        linkedList.add(murza);
        linkedList.add(vasy);
        linkedList.add(dymka);

        System.out.println("---- все ----");
        for (Cat cat : linkedList) {
            System.out.println(cat);
        }
        System.out.println("---- доступ по индексу ----");
        System.out.println(linkedList.get(0));
        System.out.println(linkedList.get(1));
        System.out.println(linkedList.get(2));
        //Сравните реализацию get для ArrayList и LinkedList

        System.out.println("---- добавили кота в `стакан` ----");
        linkedList.push(new Cat("Барсик", "пельный", 5));
        for (Cat cat : linkedList) {
            System.out.println(cat);
        }

        Cat catPop = linkedList.pop();
        System.out.println("---- забрали кота из `стакана` ----: " + catPop);
        for (Cat cat : linkedList) {
            System.out.println(cat);
        }

        System.out.println("---- добавили кота в `очередь` ----");
        linkedList.offer(new Cat("Мурка", "белая", 2));
        for (Cat cat : linkedList) {
            System.out.println(cat);
        }

        Cat catPoll = linkedList.poll();
        System.out.println("---- забрали кота из `очереди` ----: " + catPoll);
        for (Cat cat : linkedList) {
            System.out.println(cat);
        }
    }
}
