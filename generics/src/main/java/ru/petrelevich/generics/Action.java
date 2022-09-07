package ru.petrelevich.generics;

import java.util.List;
import ru.petrelevich.generics.animals.Runner;

public class Action {
    void runAll(List<Runner> animals) {
        animals.forEach(Runner::run);
    }
}
