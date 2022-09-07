package ru.petrelevich.objects;

import ru.petrelevich.objects.animals.Heroic;
import ru.petrelevich.objects.animals.Runner;

public class Hero {
    private final Object animal;

    public Hero(Object animal) {
        this.animal = animal;
    }

    void feat() {
        if (animal instanceof Heroic animalHeroic) {
            animalHeroic.feat();
        } else {
            System.out.println("wrong type:" + animal);
        }
    }
}
