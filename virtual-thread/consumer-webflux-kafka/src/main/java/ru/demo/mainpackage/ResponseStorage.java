package ru.demo.mainpackage;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.util.annotation.NonNull;
import ru.demo.mainpackage.model.RequestId;
import ru.demo.mainpackage.model.Response;
import ru.demo.mainpackage.model.ResponseSum;

public class ResponseStorage implements Sinks.EmitFailureHandler {

    private final Sinks.Many<Response> sink;
    private final ConnectableFlux<Response> sinkConnectable;
    private static final Logger log = LoggerFactory.getLogger(ResponseStorage.class);
    private final Scheduler timeoutTimer;

    public ResponseStorage(Scheduler timeoutTimer) {
        sink = Sinks.many().multicast().onBackpressureBuffer();
        sinkConnectable = sink.asFlux().publish();
        sinkConnectable.connect();
        this.timeoutTimer = timeoutTimer;
    }

    public void put(List<Response> responses) {
        for (var response : responses) {
            log.info("put. response:{}", response);
            sink.emitNext(response, this);
        }
    }

    public Mono<ResponseSum> get(RequestId requestId, Set<String> producersForWait) {
        return sinkConnectable
                .filter(response -> {
                    log.info("waiting:{}, fact:{}", requestId.value(), response);
                    return requestId.value() == response.requestId().value();
                })
                .filter(response -> producersForWait.contains(response.producerName()))
                .takeUntil(response -> response.data() == null)
                .buffer(producersForWait.size())
                .timeout(Duration.ofSeconds(10), timeoutTimer)
                .next()
                .map(responses -> {
                    var sum = responses.stream().mapToLong(Response::data).sum();
                    return new ResponseSum(requestId, sum);
                });
    }

    @Override
    public boolean onEmitFailure(@NonNull SignalType signalType, @NonNull Sinks.EmitResult emitResult) {
        return false;
    }
}
