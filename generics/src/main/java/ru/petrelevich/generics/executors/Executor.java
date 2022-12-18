package ru.petrelevich.generics.executors;

import ru.petrelevich.generics.animals.Cat;

public interface Executor<T extends Cat> {
    void execute(T cat);
}
