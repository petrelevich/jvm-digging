package ru.demo.resilience4j;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkheadDemo {
    private static final Logger log = LoggerFactory.getLogger(BulkheadDemo.class);
    private final AtomicInteger execCounter = new AtomicInteger(0);
    private final AtomicInteger errorCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        new BulkheadDemo().semaphoreBulkheadDemoVirtualThread();
        new BulkheadDemo().semaphoreBulkheadDemoThread();
        new BulkheadDemo().threadPoolBulkhead();
    }

    private void semaphoreBulkheadDemoVirtualThread() throws InterruptedException {
        var concurrentCalls = 5;
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(concurrentCalls)
                .maxWaitDuration(Duration.ofSeconds(3))
                .writableStackTraceEnabled(false)
                .build();

        BulkheadRegistry registry = BulkheadRegistry.of(config);
        var eventPublisher = registry.getEventPublisher();
        eventPublisher.onEntryAdded(event -> log.info("VT added entry:{}", event.getAddedEntry()));

        var bulkhead = registry.bulkhead("test-config");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var idx = 0; idx < concurrentCalls * 2; idx++) {
                executor.submit(() -> {
                    try {
                        var execResult = bulkhead.executeCallable(this::action);
                        log.info("VT execResult:{}", execResult);
                    } catch (Exception ex) {
                        errorCounter.incrementAndGet();
                        log.error("VT error", ex);
                    }
                });
            }
            executor.shutdown();
            var awaitResult = executor.awaitTermination(10, SECONDS);
            log.info(
                    "VT awaitResult:{}, execCounter:{}, errorCounter:{}",
                    awaitResult,
                    execCounter.get(),
                    errorCounter.get());
        }
    }

    private void semaphoreBulkheadDemoThread() throws InterruptedException {
        var concurrentCalls = 5;
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(concurrentCalls)
                .maxWaitDuration(Duration.ofSeconds(3))
                .writableStackTraceEnabled(false)
                .build();

        BulkheadRegistry registry = BulkheadRegistry.of(config);
        var eventPublisher = registry.getEventPublisher();
        eventPublisher.onEntryAdded(event -> log.info("added entry:{}", event.getAddedEntry()));

        var bulkhead = registry.bulkhead("test-config");

        try (var executor = Executors.newFixedThreadPool(concurrentCalls)) {
            for (var idx = 0; idx < concurrentCalls * 2; idx++) {
                executor.submit(() -> {
                    try {
                        var execResult = bulkhead.executeCallable(this::action);
                        log.info("execResult:{}", execResult);
                    } catch (Exception ex) {
                        errorCounter.incrementAndGet();
                        log.error("error", ex);
                    }
                });
            }
            executor.shutdown();
            var awaitResult = executor.awaitTermination(10, SECONDS);
            log.info(
                    "awaitResult:{}, execCounter:{}, errorCounter:{}",
                    awaitResult,
                    execCounter.get(),
                    errorCounter.get());
        }
    }

    private void threadPoolBulkhead() throws Exception {
        var queueCapacity = 5;
        var threadPoolBulkheadConfig = ThreadPoolBulkheadConfig.custom()
                .coreThreadPoolSize(1)
                .maxThreadPoolSize(3)
                .keepAliveDuration(Duration.ofSeconds(1))
                .queueCapacity(queueCapacity)
                .writableStackTraceEnabled(false)
                // .rejectedExecutionHandler((task, executor) -> log.error("rejected task:{}", task))
                .build();

        var threadPoolBulkheadRegistry = ThreadPoolBulkheadRegistry.of(threadPoolBulkheadConfig);
        var threadPoolBulkhead = threadPoolBulkheadRegistry.bulkhead("name-test");

        var metrics = threadPoolBulkhead.getMetrics();
        for (var idx = 0; idx < queueCapacity * 2; idx++) {
            log.info(
                    "{}. ActiveThreadCount:{}, AvailableThreadCount:{}, QueueDepth:{}, RemainingQueueCapacity:{}",
                    idx,
                    metrics.getActiveThreadCount(),
                    metrics.getAvailableThreadCount(),
                    metrics.getQueueDepth(),
                    metrics.getRemainingQueueCapacity());
            try {
                threadPoolBulkhead.executeCallable(this::action);
            } catch (BulkheadFullException ex) {
                log.error("BulkheadFullException", ex);
            }
        }

        log.info(
                "ActiveThreadCount:{}, AvailableThreadCount:{}, QueueDepth:{}, RemainingQueueCapacity:{}",
                metrics.getActiveThreadCount(),
                metrics.getAvailableThreadCount(),
                metrics.getQueueDepth(),
                metrics.getRemainingQueueCapacity());

        while (metrics.getQueueDepth() > 0) {
            Thread.sleep(Duration.ofSeconds(1));
            log.info(
                    "ActiveThreadCount:{}, AvailableThreadCount:{}, QueueDepth:{}, RemainingQueueCapacity:{}",
                    metrics.getActiveThreadCount(),
                    metrics.getAvailableThreadCount(),
                    metrics.getQueueDepth(),
                    metrics.getRemainingQueueCapacity());
        }
        log.info(
                "end ActiveThreadCount:{}, AvailableThreadCount:{}, QueueDepth:{}, RemainingQueueCapacity:{}",
                metrics.getActiveThreadCount(),
                metrics.getAvailableThreadCount(),
                metrics.getQueueDepth(),
                metrics.getRemainingQueueCapacity());
        threadPoolBulkheadRegistry.close();
    }

    private String action() {
        var currentCounter = execCounter.getAndIncrement();
        log.info("some action, counter:{}", currentCounter);
        sleep();
        return String.format("resultDone:%d", currentCounter);
    }

    private void sleep() {
        try {
            Thread.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
