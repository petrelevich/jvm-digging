package ru.demo;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Creating {
    private static final Logger log = LoggerFactory.getLogger(Creating.class);

    public static void main(String[] args) {
        Thread.ofPlatform().start(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                sleep();
                log.info("do something");
            }
        });

        Thread.ofVirtual().start(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                sleep();
                log.info("sleep");
            }
        });
    }

    private static void sleep() {
        try {
            Thread.sleep(Duration.of(10, ChronoUnit.SECONDS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
