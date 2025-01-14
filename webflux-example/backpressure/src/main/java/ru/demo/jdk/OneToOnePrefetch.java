package ru.demo.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OneToOnePrefetch {
    private static final Logger log = LoggerFactory.getLogger(OneToOnePrefetch.class);

    public static void main(String[] args) {
        new OneToOnePrefetch().start();
    }

    private void start() {
        var generatorTimer = generatorTimer();
        var publisher = publisher(generatorTimer);

        var subscriberTimer = subscriberTimer();
        subscriber(subscriberTimer, publisher);

        log.info("end");
    }

    private StringValueProvider publisher(Executor executor) {
        return new StringValueProviderSeq(1, 500, executor);
    }

    private void subscriber(ScheduledExecutorService executor, StringValueProvider publisher) {
        publisher.consume(val -> executor.schedule(() -> {
                    log.info("---------------------sub val:{}", val);
                    

                    publisher.request(10);
                },
                3,
                TimeUnit.SECONDS));
        publisher.subscribe();
    }


    private Executor generatorTimer() {
        return Executors.newSingleThreadExecutor(r -> Thread.ofPlatform().name("generator-thread").unstarted(r));
    }

    private ScheduledExecutorService subscriberTimer() {
        return Executors.newScheduledThreadPool(1, r ->
                Thread.ofPlatform()
                        .name("subscriber-thread")
                        .unstarted(r));
    }
}
