package ru.systema;


import java.util.function.IntConsumer;

public interface Calculator {
    int add(int x1, int x2);

    int addExternalSummand(int x1, int x2, IntSource intSource);

    void addAndGet(int x1, int x2, int counter, IntConsumer getter);
}
