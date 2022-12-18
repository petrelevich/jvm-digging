package ru.petrelevich.generics.executors;

import ru.petrelevich.generics.animals.SuperCat;
import ru.petrelevich.objects.animals.Cat;

public class ExecutorApplication<T extends Cat> {
    private final Executor<Cat> executor;

    public ExecutorApplication(Executor<Cat> executor) {
        this.executor = executor;
    }

    public void action(Cat cat) {
        executor.execute(cat);
    }
}
