package ru.systema;

import java.util.function.IntConsumer;

public class CalculatorAlfa implements Calculator {

    @Override
    public int add(int x1, int x2) {
        return x1 + x2;
    }

    @Override
    public int addExternalSummand(int x1, int x2, IntSource intSource) {
        return x1 + x2 + intSource.getInt();
    }

    @Override
    public void addAndGet(int x1, int x2, int counter, IntConsumer getter) {
        for(var idx = 0 ; idx < counter; idx ++) {
            getter.accept(add(x1, x2 + idx));
        }
    }
}
