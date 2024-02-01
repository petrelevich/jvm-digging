package com.datasrc;

import com.datasrc.processor.DataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProcessorDataController {
    private static final Logger log = LoggerFactory.getLogger(ProcessorDataController.class);

    private final DataProcessor<Flux<StringValue>> dataProcessorStringReactorFlux;
    private final DataProcessor<Mono<StringValue>> dataProcessorStringReactorMono;
    private final WebClient client;

    public ProcessorDataController(
            WebClient.Builder builder,
            @Qualifier("dataProcessorFlux") DataProcessor<Flux<StringValue>> dataProcessorFlux,
            @Qualifier("dataProcessorMono") DataProcessor<Mono<StringValue>> dataProcessorMono) {
        this.dataProcessorStringReactorFlux = dataProcessorFlux;
        this.dataProcessorStringReactorMono = dataProcessorMono;
        client = builder.baseUrl("http://localhost:8080").build();
    }

    @GetMapping(value = "/data/{seed}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> data(@PathVariable("seed") long seed) {
        log.info("request for data, seed:{}", seed);

        var srcRequest = client.get()
                .uri(String.format("/data/%d", seed))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(StringValue.class);

        return dataProcessorStringReactorFlux.process(srcRequest);
    }

    @GetMapping(value = "/data-mono/{seed}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<StringValue> dataMono(@PathVariable("seed") long seed) {
        log.info("request for string data-mono, seed:{}", seed);

        var srcRequest = client.get()
                .uri(String.format("/data-mono/%d", seed))
                .retrieve()
                .bodyToMono(StringValue.class);

        return dataProcessorStringReactorMono.process(srcRequest);
    }
}
