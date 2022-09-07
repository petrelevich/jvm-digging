package ru.petrelevich.objects;

import java.util.List;
import ru.petrelevich.objects.animals.Cat;
import ru.petrelevich.objects.animals.Dog;
import ru.petrelevich.objects.animals.Runner;

public class Demo {
    public static void main(String[] args) {
        var cat = new Cat("Barsic");
        var dog = new Dog("Sharic");

        List runnersOk = List.of(cat, dog);
        new Action().runAll(runnersOk);

        List runnersFail = List.of(cat, dog, "мусор");
        new Action().runAll(runnersFail);

///
        var heroCat = new Hero(cat);
        heroCat.feat();

        var heroDog = new Hero(dog);
        heroDog.feat();

        var heroString = new Hero("Мусор");
        heroString.feat();
    }
}
