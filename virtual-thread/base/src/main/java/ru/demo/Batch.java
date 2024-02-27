package ru.demo;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Batch {
    private static final Logger log = LoggerFactory.getLogger(Batch.class);
    private static final Set<String> carrierThreads = new ConcurrentSkipListSet<>();

    public static void main(String[] args) throws InterruptedException {
        var threadList = IntStream.range(0, 100_000)
                .mapToObj(value -> Thread.ofVirtual().unstarted(() -> {
                    var thread = Thread.currentThread();
                    carrierThreads.add(getCarrierThreadName(thread));
                    log.info("thread:{}, {}", thread, value);
                }))
                .toList();

        threadList.forEach(Thread::start);
        for (Thread thread : threadList) {
            thread.join();
        }
        log.info("carrierThreads.size():{}", carrierThreads.size());
        log.info("carrierThreads:{}", carrierThreads);
    }

    private static String getCarrierThreadName(Thread thread) {
        return thread.toString().split("@")[1];
    }
}
