package ru.demo.mainpackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingDemo {
    private static final Logger log = LoggerFactory.getLogger(LoggingDemo.class);

    public static void main(String[] args) {
        log.info("begin");
        var isAlreadyDone1 = false;
        var isAlreadyDone2 = false;
        var isAlreadyDone3 = false;

        try (var task1 = new Task("1", 5);
                var task2 = new Task("2", 6);
                var task3 = new Task("3", 7); ) {
            var isDone1 = task1.start();
            var isDone2 = task2.start();
            var isDone3 = task3.start();

            while (!isDone1.get() || !isDone2.get() || !isDone3.get()) {
                if (!isAlreadyDone1 && isDone1.get()) {
                    isAlreadyDone1 = true;
                    log.info("task1 done");
                }
                if (!isAlreadyDone2 && isDone2.get()) {
                    isAlreadyDone2 = true;
                    log.info("task2 done");
                }
                if (!isAlreadyDone3 && isDone3.get()) {
                    isAlreadyDone3 = true;
                    log.info("task3 done");
                }
            }
        }
        log.info("end");
    }
}
