package ru.demo.resilience4j;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SemaphoreDemo {
    private static final Logger log = LoggerFactory.getLogger(SemaphoreDemo.class);

    private final AtomicInteger insideCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        new SemaphoreDemo().run();
    }

    private void run() {
        var permits = 3;
        var semaphore = new Semaphore(permits);
        var taskCounter = permits + 2;
        var countDownLatch = new CountDownLatch(taskCounter);
        for (var idx = 0; idx < taskCounter; idx++) {
            Thread.ofPlatform().name(String.format("task-%d", idx)).start(() -> action(semaphore, countDownLatch));
        }
    }

    private void action(Semaphore semaphore, CountDownLatch latch) {
        var sleepSec = 3L;
        try {
            latch.countDown();
            log.info("waiting for, {}", latch.getCount());
            latch.await();
            var acquireResult = semaphore.tryAcquire(sleepSec + 2, SECONDS);
            if (acquireResult) {
                try {
                    var currentCounter = insideCounter.incrementAndGet();
                    log.info("some action, counter:{}", currentCounter);
                    Thread.sleep(Duration.ofSeconds(sleepSec));
                    insideCounter.decrementAndGet();
                    log.info("resultDone:{}", currentCounter);

                } finally {
                    semaphore.release();
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
