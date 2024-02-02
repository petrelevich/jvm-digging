package com.datasrc;

import com.datasrc.logger.MdcField;
import java.time.Duration;
import java.util.function.BiFunction;

import io.micrometer.context.ContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

public class SinksDemo implements Sinks.EmitFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(SinksDemo.class);
    private final Sinks.Many<ValueRecord> sink;
    private static final MdcField mdcField = new MdcField("seed");

    private final Scheduler timer = Schedulers.newParallel("timer-thread", 1);

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();

        ContextRegistry.getInstance().registerThreadLocalAccessor(
                mdcField.name(),
                () -> MDC.get(mdcField.name()),
                value -> MDC.put(mdcField.name(), String.valueOf(value)),
                () -> MDC.remove(mdcField.name()));

        var sinkDemo = new SinksDemo();
        sinkDemo.emit();
        sinkDemo.receive()
                .doOnNext(val -> log.info("received valueRecord:{}", val))
                .contextCapture()
                .subscribe();

        log.info("done");
    }

    public SinksDemo() {
        sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    public void emit() {
        long seed = 133;
        MDC.put(mdcField.name(), String.valueOf(seed));
        var stringSeed = "someDataStr:";
        Flux.generate(() -> seed, (BiFunction<Long, SynchronousSink<Long>, Long>) (prev, sink) -> {
                    var newValue = prev + 1;
                    sink.next(newValue);
                    return newValue;
                })
                .delayElements(Duration.ofSeconds(3), timer)
                .map(val -> new ValueRecord(stringSeed + val))
                .doOnEach(val -> log.info("debug 100:{}", val))
                .doOnNext(val -> sink.emitNext(val, SinksDemo.this))
                .contextCapture()
                .subscribe();
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

    private record ValueRecord(String value) {}
}
