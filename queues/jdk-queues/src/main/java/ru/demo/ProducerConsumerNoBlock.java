package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ProducerConsumerNoBlock {
    private static final Logger log = LoggerFactory.getLogger(ProducerConsumerNoBlock.class);
    private final AtomicLong idSeq = new AtomicLong(0);
    private final Queue<Value> queue = new ConcurrentLinkedQueue<>();
    private ScheduledExecutorService stopper;
    private ScheduledExecutorService producer;
    private ExecutorService consumer;

    public static void main(String[] args) {
        var producerConsumer = new ProducerConsumerNoBlock();
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
        }, 10, MINUTES);
        stopper.shutdown();
    }

    private void produce() {
        var value = new Value(String.format("val:%d", idSeq.incrementAndGet()));
        queue.offer(value);
    }

    private void consume() {
        while (!Thread.currentThread().isInterrupted()) {
            var value = queue.poll();
            if (value != null) {
                log.info("consumed:{}", value);
            }
        }
    }
}
