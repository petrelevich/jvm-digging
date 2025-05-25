package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorsDemo {
    private static final Logger log = LoggerFactory.getLogger(ExecutorsDemo.class);

    public static void main(String[] args) {
        queueHiddenOverFlowList();
    }

    private static void queueHiddenOverFlowList() {
        RejectedExecutionHandler rejectedExecution = (Runnable task, ThreadPoolExecutor executor) -> log.warn("rejected task:{}", task);

        try (var executor = new ThreadPoolExecutor(2,
                2,
                0,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2), makeThreadFactory(), rejectedExecution)) {
            executor.submit(task("task-1"));
            executor.submit(task("task-2"));
            executor.submit(task("task-3"));
            executor.submit(task("task-4"));
            executor.submit(task("task-5"));
        }
    }

    private static Runnable task(String name) {
        return new Runnable() {
            @Override
            public void run() {
                log.info("{} started", name);
                try {
                    Thread.sleep(Duration.ofSeconds(5));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("{} done", name);
            }

            @Override
            public String toString() {
                return "task:" + name;
            }
        };
    }


    private static ThreadFactory makeThreadFactory() {
        AtomicInteger threadId = new AtomicInteger(0);
        return task -> Thread.ofPlatform().name(String.format("thread-%d", threadId.incrementAndGet())).unstarted(task);
    }
}
