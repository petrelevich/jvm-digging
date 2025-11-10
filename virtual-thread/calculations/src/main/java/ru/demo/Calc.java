package ru.demo;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Calc implements CommandLineRunner {
    private static final int SHOW_COUNTER = 5;
    private static final Logger log = LoggerFactory.getLogger(Calc.class);
    private final Map<String, Long> prevTimes = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    private final AtomicLong printCounter = new AtomicLong(0);
    private final ExecutorService a1;
    private final ExecutorService bti;

    public Calc(
            @Qualifier("a1") ExecutorService a1,
            @Qualifier("bti") ExecutorService bti,
            ScheduledExecutorService scheduledExecutor) {
        this.a1 = a1;
        this.bti = bti;
        scheduledExecutor.scheduleAtFixedRate(this::print, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run(String... args) {
        counters.put("a1-1", new AtomicLong(0));
        counters.put("a1-2", new AtomicLong(0));
        counters.put("bti-1", new AtomicLong(0));
        counters.put("bti-2", new AtomicLong(0));
        for (var idx = 0; idx < 10_000; idx++) {
            a1.submit(() -> job("a1-1", 10_000_000));
            a1.submit(() -> job("a1-2", 10_000_000));
            bti.submit(() -> job("bti-1", 5_000_000));
            bti.submit(() -> job("bti-2", 5_000_000));
        }
    }

    private void print() {
        var printCounterVal = printCounter.incrementAndGet();
        log.info("---- printCounterVal:{}", printCounterVal);
        for (var counter : counters.entrySet()) {
            log.info("{} - {}", counter.getKey(), counter.getValue());
        }
    }

    private void job(String agentName, int actionSize) {
        action(actionSize);
        var counter = counters.get(agentName).incrementAndGet();
        if (counter % SHOW_COUNTER == 0) {
            var now = System.currentTimeMillis();
            var prevTime = prevTimes.get(agentName);
            Long duration = null;
            if (prevTime != null) {
                duration = (now - prevTime) / 1000;
            }
            prevTimes.put(agentName, now);
            log.info(
                    "virtual:{}, agentName:{}, counter:{}, duration, sec:{}",
                    Thread.currentThread().isVirtual(),
                    agentName,
                    counter,
                    duration);
        }
    }

    private void action(int actionSize) {
        var src = new ArrayList<Integer>();
        for (var idx = 0; idx < actionSize; idx++) {
            src.add(idx, 0);
        }
        log.trace("count:{}", src.size());
    }
}
