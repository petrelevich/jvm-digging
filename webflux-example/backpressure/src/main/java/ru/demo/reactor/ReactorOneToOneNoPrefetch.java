package ru.demo.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import ru.demo.StringValue;

import java.time.Duration;

public class ReactorOneToOneNoPrefetch {
    private static final Logger log = LoggerFactory.getLogger(ReactorOneToOneNoPrefetch.class);

    public static void main(String[] args) {
        new ReactorOneToOneNoPrefetch().start();
    }

    private void start() {
        var generatorTimer = generatorTimer();
        var publisher = publisher(generatorTimer);

        var subscriberTimer = subscriberTimer();
        subscriber(subscriberTimer, publisher);

        log.info("end");
    }

    private Flux<StringValue> publisher(Scheduler timer) {
        return Flux.range(1, 500)
                .doOnRequest(req -> log.info(" new request on:{}", req))
                .map(val -> {
                    var stringValue = new StringValue(String.valueOf(val));
                    log.info(" StringValue:{}", stringValue);
                    return stringValue;
                })
                .subscribeOn(timer);
    }

    private void subscriber(Scheduler timer, Flux<StringValue> publisher) {
        publisher.delayElements(Duration.ofSeconds(3), timer)
                .doOnNext(val -> log.info("---------------------sub val:{}", val))
                .subscribe();
    }


    private Scheduler generatorTimer() {
        return Schedulers.newSingle("generator-thread");
    }

    private Scheduler subscriberTimer() {
        return Schedulers.newSingle("subscriber-thread");
    }
}
