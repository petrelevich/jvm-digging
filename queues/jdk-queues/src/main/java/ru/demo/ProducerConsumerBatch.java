package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ProducerConsumerBatch {
    private static final Logger log = LoggerFactory.getLogger(ProducerConsumerBatch.class);
    private final AtomicLong idSeq = new AtomicLong(0);
    private static final int QUEUE_SIZE = 10;
    private final BlockingQueue<Value> queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    private ScheduledExecutorService stopper;
    private ScheduledExecutorService producer;
    private ScheduledExecutorService consumer;

    public static void main(String[] args) {
        var producerConsumer = new ProducerConsumerBatch();
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    log.info("producerConsumer stopping");
                    if (producerConsumer.stopper != null) {
                        producerConsumer.stopper.shutdown();
                    }
                    if (producerConsumer.producer != null) {
                        producerConsumer.producer.shutdown();
                    }
                    if (producerConsumer.consumer != null) {
                        producerConsumer.consumer.shutdown();
                    }
                })
        );
        producerConsumer.run();
    }

    private void run() {
        producer = Executors
                .newSingleThreadScheduledExecutor(r -> Thread.ofPlatform().name("producer").unstarted(r));
        producer.scheduleAtFixedRate(this::produce, 1, 1, TimeUnit.SECONDS);

        consumer = Executors
                .newSingleThreadScheduledExecutor(r -> Thread.ofPlatform().name("consumer").unstarted(r));
        consumer.scheduleAtFixedRate(this::consume, 1, 4, TimeUnit.SECONDS);

        stopper = Executors.newSingleThreadScheduledExecutor();
        stopper.schedule(() -> {
            producer.close();
            consumer.shutdown();
            try {
                var isTerminated = consumer.awaitTermination(1, SECONDS);
                if (!isTerminated) {
                    consumer.shutdownNow();
                    log.warn("consumer was terminated after timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("stopper interrupted");
            }
            log.info("executors closed");
        }, 1, MINUTES);
        stopper.shutdown();
    }

    private void produce() {
        var value = new Value(String.format("val:%d", idSeq.incrementAndGet()));
        try {
            var offerResult = queue.offer(value, 1, TimeUnit.SECONDS);
            if (!offerResult) {
                log.error("lost value:{}", value);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("produce terminated");
        }
    }

    private void consume() {
        var values = new ArrayList<Value>(QUEUE_SIZE);
        queue.drainTo(values);
        if (values.isEmpty()) {
            log.warn("no values");
        } else {
            log.info("consumed:{}", values.size());
        }
    }
}
