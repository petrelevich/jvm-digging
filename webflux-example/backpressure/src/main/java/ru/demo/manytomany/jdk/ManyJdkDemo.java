package ru.demo.manytomany.jdk;

import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.generator.Subscriber;
import ru.demo.generator.Value;
import ru.demo.generator.ValueGenerator;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static ru.demo.generator.Value.COMPLETE_VALUE;

public class ManyJdkDemo {
    private static final Logger log = LoggerFactory.getLogger(ManyJdkDemo.class);

    public static void main(String[] args) {
        new ManyJdkDemo().start();
    }

    private void start() {
        Publisher publisher = new PublisherJdk();
        Queue<Value> queue = new MpscArrayQueue<>(100);

        makeValueGenerator(queue, 1);
        makeValueGenerator(queue, 2);
        makeValueGenerator(queue, 3);

        var subscriber1 = makeSubscriber("sub-1", 0);
        publisher.subscribe(subscriber1);
        sleep(1);
        var subscriber2 = makeSubscriber("sub-2", 1);
        publisher.subscribe(subscriber2);
        sleep(2);
        var subscriber3 = makeSubscriber("sub-3", 4);
        publisher.subscribe(subscriber3);

        makeSinkTransfer(queue, publisher, 3);
    }

    private void makeSinkTransfer(Queue<Value> queue, Publisher publisher, int generatorCounter) {
        Thread.ofPlatform().name("transfer").start(() -> {
            int completeCounter = 0;
            while (!Thread.currentThread().isInterrupted()) {
                var value = queue.poll();
                if (value != null) {
                    if (COMPLETE_VALUE.equals(value)) {
                        completeCounter++;
                        if (completeCounter == generatorCounter) {
                            publisher.onComplete();
                            log.info("transfer value completed");
                            return;
                        }
                    } else {
                        publisher.onEvent(value);
                        log.info("transfer value:{}", value);
                    }
                }
            }
        });
    }

    private void makeValueGenerator(Queue<Value> queue, int idx) {
        var beginVal = idx * 100;
        var endVal = beginVal + 100;
        var generator = new ValueGenerator(new Subscriber() {

            @Override
            public void onNext(Value value) {
                var offerResult = queue.offer(value);
                log.info("value_{}:{}, offerResult:{}", idx, value, offerResult);
            }

            @Override
            public void onComplete() {
                log.info("complete_{}", idx);
                boolean offerResult;
                do
                    offerResult = queue.offer(COMPLETE_VALUE);
                while (!offerResult);
            }

            @Override
            public String getName() {
                return String.format("generator:%d", idx);
            }
        }, beginVal, endVal);
        Thread.ofPlatform().name(String.format("generator-%d", idx)).start(() -> generator.request(endVal));
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Subscriber makeSubscriber(String name, int delaySec) {
        var executor = makeExecutor(name);
        return new Subscriber() {
            @Override
            public void onNext(Value val) {
                executor.execute(() -> {
                    log.info("{} value:{}", name, val);
                    if (delaySec > 0) {
                        sleep(delaySec);
                    }
                });
            }

            @Override
            public void onComplete() {
                executor.shutdownNow();
                log.info("{} Completed", name);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    private ExecutorService makeExecutor(String threadName) {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
        RejectedExecutionHandler handler = (r, executor) -> {
            throw new RejectedExecutionException("can't accept a new task");
        };

        return new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                workQueue, task -> Thread.ofPlatform().name(threadName).unstarted(task),
                handler);
    }
}
