package com.datasrc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;
import reactor.util.annotation.NonNull;

public class SinksDemo implements Sinks.EmitFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(SinksDemo.class);
    private final Sinks.Many<ValueRecord> sink;
    private final AtomicLong id = new AtomicLong(0);
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        var sinkDemo = new SinksDemo();
        sinkDemo.emit();
        sinkDemo.receive()
                .doOnNext(val -> log.info("received valueRecord:{}", val))
                .subscribe();

        log.info("done");
    }

    public SinksDemo() {
        sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    public void emit() {
        executor.scheduleAtFixedRate(
                () -> sink.emitNext(new ValueRecord("value" + id.incrementAndGet()), SinksDemo.this),
                0, 3, TimeUnit.SECONDS);
    }

    public Flux<ValueRecord> receive() {
        return sink.asFlux().doOnRequest(this::onRequest);
    }

    @Override
    public boolean onEmitFailure(@NonNull SignalType signalType, @NonNull Sinks.EmitResult emitResult) {
        log.error("signalType:{}, emitResult:{}", signalType, emitResult);
        return false;
    }

    private void onRequest(long toAdd) {
        log.info("onRequest.toAdd {}", toAdd);
    }

    private record ValueRecord(String value) {
    }
}


