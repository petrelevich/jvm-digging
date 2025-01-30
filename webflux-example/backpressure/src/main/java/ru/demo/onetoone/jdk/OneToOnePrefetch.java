package ru.demo.onetoone.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class OneToOnePrefetch {
    private static final Logger log = LoggerFactory.getLogger(OneToOnePrefetch.class);

    public static void main(String[] args) {
        new OneToOnePrefetch().start();
    }

    private void start() {
        var publisherExecutor = Executors.newSingleThreadExecutor(r -> Thread.ofPlatform().name("generator-thread").unstarted(r));
        var publisher = new StringValueProviderSeq(1, 500, publisherExecutor);

        var subscriber = new SubscriberJdk(10, publisher);
        subscriber.subscribe();

        log.info("end");
    }
}
