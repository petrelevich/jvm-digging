package ru.demo.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OneToOnePrefetch {
    private static final Logger log = LoggerFactory.getLogger(OneToOnePrefetch.class);

    public static void main(String[] args) {
        new OneToOnePrefetch().start();
    }

    private void start() {
        var publisherExecutor = publisherTimer();
        var publisher = publisher(publisherExecutor);

        var subscriber = new SubscriberJdk(10, publisher);
        subscriber.subscribe();

        log.info("end");
    }

    private StringValueProvider publisher(Executor executor) {
        return new StringValueProviderSeq(1, 500, executor);
    }

    private Executor publisherTimer() {
        return Executors.newSingleThreadExecutor(r -> Thread.ofPlatform().name("generator-thread").unstarted(r));
    }
}
