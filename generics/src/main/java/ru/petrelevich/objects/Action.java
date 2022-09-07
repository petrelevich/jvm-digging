package ru.petrelevich.objects;

import java.util.List;
import ru.petrelevich.objects.animals.Runner;

public class Action {
    void runAll(List animals) {
        for (var animal: animals) {
            if (animal instanceof Runner animalRunner) {
                animalRunner.run();
            } else {
                System.out.println("wrong type:" + animal);
            }
        }
    }
}
