package com.datasrc;

import com.datasrc.model.StringValue;
import java.time.Duration;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;

public class StringValueSource implements ValueSource {
    private static final Logger log = LoggerFactory.getLogger(StringValueSource.class);

    private final Scheduler scheduler;

    public StringValueSource(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Flux<StringValue> makeValueFlow() {
        return Flux.generate(() -> 0L, (BiFunction<Long, SynchronousSink<Long>, Long>) (prev, sink) -> {
                    var newValue = prev + 1;
                    sink.next(newValue);
                    return newValue;
                })
                .delayElements(Duration.ofSeconds(1), scheduler)
                .map(id -> new StringValue(id, "stVal:" + id))
                .doOnNext(val -> log.info("val:{}", val));
    }
}
