package ru.demo.resilience4j;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RateLimiterDemo {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterDemo.class);
    private final AtomicInteger execCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        new RateLimiterDemo().go();
    }

    private void go() throws InterruptedException {
        var executePeriod = 500;
        var rateLimiterConfig = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(1)
                .timeoutDuration(Duration.ofMillis(executePeriod * 2L))
                .writableStackTraceEnabled(false)
                .build();

        var rateLimiterRegistry = RateLimiterRegistry.of(rateLimiterConfig);
        rateLimiterRegistry
                .getEventPublisher()
                .onEntryAdded(entryAddedEvent -> {
                    var addedRateLimiter = entryAddedEvent.getAddedEntry();
                    log.info("RateLimiter {} added", addedRateLimiter.getName());
                })
                .onEntryRemoved(entryRemovedEvent -> {
                    var removedRateLimiter = entryRemovedEvent.getRemovedEntry();
                    log.info("RateLimiter {} removed", removedRateLimiter.getName());
                });

        var rateLimiter = rateLimiterRegistry.rateLimiter("actionLimit");
        var metrics = rateLimiter.getMetrics();
        rateLimiter
                .getEventPublisher()
                .onSuccess(event -> log.info(
                        "onSuccess, AvailablePermissions:{}, WaitingThreads:{}. event:{}",
                        metrics.getAvailablePermissions(),
                        metrics.getNumberOfWaitingThreads(),
                        event))
                .onFailure(event -> log.info(
                        "onFailure, AvailablePermissions:{}, WaitingThreads:{}. event:{}",
                        metrics.getAvailablePermissions(),
                        metrics.getNumberOfWaitingThreads(),
                        event));

        var checkedRunnable = RateLimiter.decorateCheckedRunnable(rateLimiter, this::action);

        try (var executor = Executors.newScheduledThreadPool(10)) {
            executor.scheduleAtFixedRate(
                    () -> {
                        try {
                            checkedRunnable.run();
                        } catch (Throwable e) {
                            log.error("restrictedCall error", e);
                        }
                    },
                    0,
                    executePeriod,
                    MILLISECONDS);

            var terminationResult = executor.awaitTermination(1, MINUTES);
            log.info("terminationResult:{}", terminationResult);
        }

        log.info("end");
    }

    private void action() {
        var currentCounter = execCounter.incrementAndGet();
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("some action, counter:{}", currentCounter);
    }
}
