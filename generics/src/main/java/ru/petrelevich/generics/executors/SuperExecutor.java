package ru.petrelevich.generics.executors;

import ru.petrelevich.generics.animals.SuperCat;

public interface SuperExecutor extends Executor<SuperCat> {

    @Override
    void execute(SuperCat cat);
}
