package ru.petrelevich.generics;

import ru.petrelevich.generics.animals.Heroic;

public class Hero<T extends Heroic> {
    private final T animal;

    public Hero(T animal) {
        this.animal = animal;
    }

    void feat() {
        animal.feat();
    }
}
