package ru.petrelevich;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericDemo {
    public static void main(String[] args) {
        new GenericDemo().method(3L);
    }

    public <T> void method(T data) {
        List<T> list = new ArrayList<>();
        list.add(data);

        T[] elementData = (T[]) new Object[1];
        elementData[0] = data;

        Object[] elementDataObj = new Object[1];
        elementDataObj[0] = data;
    }
}
