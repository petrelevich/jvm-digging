package ru.demo.mainpackage.services;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import ru.demo.mainpackage.model.Stats;
import ru.demo.mainpackage.model.StatsId;
import ru.demo.mainpackage.model.StatsValue;
import ru.demo.mainpackage.repository.CounterRepository;

public class CounterRedis implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CounterRedis.class);

    private static final StatsId id = new StatsId(112);
    private final CounterRepository counterRepository;
    private final ScheduledThreadPoolExecutor scheduledExecutor;
    private final StatsValue initValue;

    public CounterRedis(
            CounterRepository counterRepository, ScheduledThreadPoolExecutor scheduledExecutor, StatsValue initValue) {
        this.counterRepository = counterRepository;
        this.scheduledExecutor = scheduledExecutor;
        this.initValue = initValue;
    }

    @Override
    public void run(String... args) {
        try {
            counterRepository.initValue(new Stats(id, initValue));
            scheduledExecutor.scheduleAtFixedRate(this::increment, 1, 1, TimeUnit.SECONDS);
            scheduledExecutor.scheduleAtFixedRate(this::showValue, 3, 3, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("init value error, value:{}", initValue, ex);
        }
    }

    public void increment() {
        var currentValue = counterRepository.increment(id);
        log.info("updated Value:{}", currentValue);
    }

    public void showValue() {
        var currentValue = counterRepository.get(id);
        currentValue.ifPresent(statsValue -> log.info("   current Value:{}", statsValue));
    }
}
