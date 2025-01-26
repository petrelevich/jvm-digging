package ru.demo.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import ru.demo.StringValue;

import java.time.Duration;

public class ReactorOneToOnePrefetch {
    private static final Logger log = LoggerFactory.getLogger(ReactorOneToOnePrefetch.class);

    public static void main(String[] args) {
        new ReactorOneToOnePrefetch().start();
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
        publisher
                .concatMap(val -> {
                    log.info("just, val:{}", val);
                    return Mono.just(val);
                }, 10)
                .delayElements(Duration.ofSeconds(3), timer)
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
