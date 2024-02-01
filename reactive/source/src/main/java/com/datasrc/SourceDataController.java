package com.datasrc;

import static com.datasrc.config.ApplConfig.MDC_SEED;

import com.datasrc.producer.DataProducer;
import com.datasrc.producer.StringValue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@RestController
public class SourceDataController {
    private static final Logger log = LoggerFactory.getLogger(SourceDataController.class);

    private final DataProducer<Flux<StringValue>> dataProducerFlux;
    private final DataProducer<StringValue> dataProducerStringBlocked;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public SourceDataController(
            @Qualifier("dataProducerFlux") DataProducer<Flux<StringValue>> dataProducerFlux,
            @Qualifier("dataProducerStringBlocked") DataProducer<StringValue> dataProducerStringBlocked) {
        this.dataProducerFlux = dataProducerFlux;
        this.dataProducerStringBlocked = dataProducerStringBlocked;
    }

    @GetMapping(value = "/data/{seed}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> data(@PathVariable("seed") long seed) {
        log.info("request for string data, seed:{}", seed);
        var stringData = dataProducerFlux.produce(seed).contextWrite(Context.of(MDC_SEED.name(), seed));

        log.info("Method request for string data done");
        return stringData;
    }

    @GetMapping(value = "/data-mono/{seed}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<StringValue> dataMono(@PathVariable("seed") long seed) {
        log.info("request for string data-mono, seed:{}", seed);
        log.info("Method request for string data done");

        var future = CompletableFuture.supplyAsync(() -> dataProducerStringBlocked.produce(seed), executor);
        return Mono.fromFuture(future);
    }
}
