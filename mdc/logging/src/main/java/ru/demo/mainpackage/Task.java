package ru.demo.mainpackage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Task implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(Task.class);
    private long counter;
    private final long counterEnd;
    private final ScheduledExecutorService executor;
    private final AtomicBoolean jobDone = new AtomicBoolean(false);

    public Task(String requestId, long counterEnd) {
        this.counterEnd = counterEnd;
        this.executor = Executors.newScheduledThreadPool(1, task -> Thread.ofPlatform()
                .name(String.format("task-%s", requestId))
                .unstarted(() -> {
                    MDC.put("requestId", requestId);
                    task.run();
                }));
    }

    @Override
    public void close() {
        executor.close();
    }

    public AtomicBoolean start() {
        schedule();
        return jobDone;
    }

    private void action() {
        if (counter != counterEnd) {
            log.info("current counter:{}", counter);
            counter++;
            schedule();
        } else {
            log.info("action done, counterEnd:{}", counterEnd);
            jobDone.set(true);
        }
    }

    private void schedule() {
        executor.schedule(this::action, 3, TimeUnit.SECONDS);
    }
}
