package ru.demo.onetoone.jdk;

import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.StringValue;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class SubscriberJdk {
    private static final Logger log = LoggerFactory.getLogger(SubscriberJdk.class);
    private final int prefetch;
    private final Queue<StringValue> queue;
    private final ExecutorService executor;
    private final StringValueProvider publisher;
    private final AtomicInteger onTheFly = new AtomicInteger(0);

    public SubscriberJdk(ExecutorService executor, int prefetch, StringValueProvider publisher) {
        this.executor = executor;
        this.prefetch = prefetch;
        this.queue = new SpscArrayQueue<>(prefetch);
        this.publisher = publisher;
    }

    public void subscribe() {
        startProcessing();
        publisher.consume(value -> {
            var offerResult = queue.offer(value);
            if (!offerResult) {
                log.error("lost value:{}", value);
            }
        });
        prefetch();
    }

    private void startProcessing() {
        executor.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                var value = queue.poll();
                if (value != null) {
                    sleep(2);
                    log.info("---------------------sub val:{}", value);
                    onTheFly.decrementAndGet();
                    if (onTheFly.get() == 0) {
                        prefetch();
                    }
                }
            }
        });
    }

    private void prefetch() {
        publisher.request(prefetch);
        onTheFly.set(prefetch);
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
