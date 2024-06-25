package ru.demo.mainpackage.repository;

import java.util.Optional;
import ru.demo.mainpackage.model.Stats;
import ru.demo.mainpackage.model.StatsId;
import ru.demo.mainpackage.model.StatsValue;

public interface CounterRepository {

    void initValue(Stats stats);

    Optional<StatsValue> get(StatsId id);

    StatsValue increment(StatsId id);
}
