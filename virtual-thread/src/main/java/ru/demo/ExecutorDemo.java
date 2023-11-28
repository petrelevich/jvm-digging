package ru.demo;

import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorDemo {
    private static final Logger log = LoggerFactory.getLogger(ExecutorDemo.class);

    public static void main(String[] args) {
        var factory = Thread.ofVirtual().name("routine-", 0).factory();
        try (var executor = Executors.newThreadPerTaskExecutor(factory)) {
            executor.submit(() -> log.info("task-1, thread:{}", Thread.currentThread()));
            executor.submit(() -> log.info("task-2, thread:{}", Thread.currentThread()));
            executor.submit(() -> log.info("task-3, thread:{}", Thread.currentThread()));
        }
    }
}
