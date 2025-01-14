package ru.demo.manytomany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import ru.demo.manytomany.generator.Subscriber;
import ru.demo.manytomany.generator.Value;
import ru.demo.manytomany.generator.ValueGenerator;

import java.time.Duration;

/*
-Xms128m
-Xmx128m
-verbose:gc
-XX:+UseG1GC
*/

public class SinkDemo {
    private static final Logger log = LoggerFactory.getLogger(SinkDemo.class);

    public static void main(String[] args) {

        System.setProperty("reactor.bufferSize.small", "10");
        new SinkDemo().start();
    }

    private void start() {
        //Sinks.Many<Value> sink = Sinks.many().multicast().onBackpressureBuffer(8);
        Sinks.Many<Value> sink = Sinks.many().multicast().directBestEffort();


        var end = 5000;
        var generator = new ValueGenerator(new Subscriber() {
            @Override
            public void onNext(Value value) {
                var emitResult = sink.tryEmitNext(value);
                log.info("value:{}, emitResult:{}", value, emitResult);
            }

            @Override
            public void onComplete() {
                log.info("complete");
                sink.tryEmitComplete();
            }
        }, 0, end);

        sink.asFlux()
                .publishOn(Schedulers.newSingle("sub-1"))
                .doOnNext(val -> log.info("sub-1 value:{}", val))
                .subscribe();

        sleep(1);

        sink.asFlux()
                .publishOn(Schedulers.newSingle("sub-2"))
                .doOnNext(val -> {
                    log.info("sub-2 value:{}", val);
                    sleep(1);
                })
                .subscribe();

        sleep(1);
        sink.asFlux()
                .publishOn(Schedulers.newSingle("sub-3"))
                .doOnNext(val -> {
                    log.info("sub-3 value:{}", val);
                    sleep(2);
                })
                .subscribe();

        Thread.ofPlatform().name("generator-1").start(() -> generator.request(end));
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
