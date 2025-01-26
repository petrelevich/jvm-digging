package ru.demo.manytomany.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.generator.Subscriber;
import ru.demo.generator.Value;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RejectedExecutionException;

public class PublisherJdk implements Publisher {
    private static final Logger log = LoggerFactory.getLogger(PublisherJdk.class);

    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }


    @Override
    public void onEvent(Value value) {
        for(var subscriber: subscribers) {
            try {
                subscriber.onNext(value);
            } catch (RejectedExecutionException ex) {
                log.warn("can't process value:{}, subscriber:{}", value, subscriber.getName());
            } catch (Exception ex) {
                log.error("error", ex);
            }
        }
    }

    @Override
    public void onComplete() {
        for(var subscriber: subscribers) {
            try {
                subscriber.onComplete();
            } catch (Exception ex) {
                log.error("error", ex);
            }
        }
    }
}
