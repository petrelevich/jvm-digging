package ru.petrelevich.generics;

import java.util.List;
import ru.petrelevich.generics.animals.Cat;
import ru.petrelevich.generics.animals.Dog;
import ru.petrelevich.generics.animals.Runner;

public class Demo {
    public static void main(String[] args) {
        var cat = new Cat("Barsic");
        var dog = new Dog("Sharic");

        List<Runner> runners = List.of(cat, dog);
        new Action().runAll(runners);
///
        var heroCat = new Hero<>(cat);
        heroCat.feat();

        var heroDog = new Hero<>(dog);
        heroDog.feat();
    }
}
