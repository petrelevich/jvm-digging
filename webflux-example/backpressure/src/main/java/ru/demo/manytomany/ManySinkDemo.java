package ru.demo.manytomany;

import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import ru.demo.generator.Subscriber;
import ru.demo.generator.Value;
import ru.demo.generator.ValueGenerator;

import java.time.Duration;
import java.util.Queue;

import static ru.demo.generator.Value.COMPLETE_VALUE;


public class ManySinkDemo {
    private static final Logger log = LoggerFactory.getLogger(ManySinkDemo.class);

    public static void main(String[] args) {

        System.setProperty("reactor.bufferSize.small", "10");
        new ManySinkDemo().start();
    }

    private void start() {
        Sinks.Many<Value> sink = Sinks.many().multicast().directBestEffort();

        Queue<Value> queue = new MpscArrayQueue<>(100);

        makeValueGenerator(queue, 1);
        makeValueGenerator(queue, 2);
        makeValueGenerator(queue, 3);

        var schedulerSub1 = Schedulers.newSingle("sub-1");
        sink.asFlux()
                .publishOn(schedulerSub1)
                .doOnNext(val -> log.info("sub-1 value:{}", val))
                .doOnComplete(schedulerSub1::dispose)
                .subscribe();

        sleep(1);
        var schedulerSub2 = Schedulers.newSingle("sub-2");
        sink.asFlux()
                .publishOn(schedulerSub2)
                .doOnNext(val -> {
                    log.info("sub-2 value:{}", val);
                    sleep(1);
                })
                .doOnComplete(schedulerSub2::dispose)
                .subscribe();

        sleep(1);
        var schedulerSub3 = Schedulers.newSingle("sub-3");
        sink.asFlux()
                .publishOn(schedulerSub3)
                .doOnNext(val -> {
                    log.info("sub-3 value:{}", val);
                    sleep(2);
                })
                .doOnComplete(schedulerSub3::dispose)
                .subscribe();

        makeSinkTransfer(queue, sink, 3);
    }

    private void makeSinkTransfer(Queue<Value> queue, Sinks.Many<Value> sink, int generatorCounter) {
        Thread.ofPlatform().name("transfer").start(() -> {
            int completeCounter = 0;
            while (!Thread.currentThread().isInterrupted()) {
                var value = queue.poll();
                if (value != null) {
                    if (COMPLETE_VALUE.equals(value)) {
                        completeCounter++;
                        if (completeCounter == generatorCounter) {
                            sink.tryEmitComplete();
                            log.info("transfer value completed");
                            return;
                        }
                    } else {
                        var emitResult = sink.tryEmitNext(value);
                        log.info("transfer value:{}, emitResult:{}", value, emitResult);
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
}
