package ru.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ProducerConsumer {
    private static final Logger log = LoggerFactory.getLogger(ProducerConsumer.class);
    private final AtomicLong idSeq = new AtomicLong(0);
    private static final int QUEUE_SIZE = 100_000;
    private final BlockingQueue<Value> queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    private ScheduledExecutorService stopper;
    private ScheduledExecutorService producer;
    private ExecutorService consumer;

    public static void main(String[] args) {
        var producerConsumer = new ProducerConsumer();
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    log.info("producerConsumer stopping");
                    if (producerConsumer.stopper != null) {
                        producerConsumer.stopper.shutdownNow();
                    }
                    if (producerConsumer.producer != null) {
                        producerConsumer.producer.shutdownNow();
                    }
                    if (producerConsumer.consumer != null) {
                        producerConsumer.consumer.shutdownNow();
                    }
                })
        );
        producerConsumer.run();
    }

    private void run() {
        producer = Executors
                .newSingleThreadScheduledExecutor(r -> Thread.ofPlatform().name("producer").unstarted(r));
        producer.scheduleAtFixedRate(this::produce, 1, 1, SECONDS);

        consumer = Executors.newSingleThreadExecutor(r -> Thread.ofPlatform().name("consumer").unstarted(r));
        consumer.submit(this::consume);

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
            var offerResult = queue.offer(value, 1, SECONDS);
            if (!offerResult) {
                log.error("lost value:{}", value);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("produce terminated");
        }
    }

    private void consume() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                var value = queue.poll(1, SECONDS);
                if (value == null) {
                    log.warn("no values");
                } else {
                    log.info("queue.size:{}, consumed:{}", queue.size(), value);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("consume terminated, queue.size:{}", queue.size());
            }
        }
    }
}
