package com.datasrc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//  http://localhost:8082/data/5
@RestController
public class ClientDataController {
    private static final Logger log = LoggerFactory.getLogger(ClientDataController.class);

    private final WebClient client;

    public ClientDataController(WebClient.Builder builder) {
        client = builder.baseUrl("http://localhost:8081").build();
    }

    @GetMapping(value = "/data/{seed}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> data(@PathVariable("seed") long seed) {
        log.info("request for data, seed:{}", seed);

        return client.get()
                .uri(String.format("/data/%d", seed))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(StringValue.class)
                .doOnNext(val -> log.info("val:{}", val));
    }

    @GetMapping(value = "/data-mono/{seed}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<StringValue> dataMono(@PathVariable("seed") long seed) {
        log.info("request for string data-mono, seed:{}", seed);

        return client.get()
                .uri(String.format("/data-mono/%d", seed))
                .retrieve()
                .bodyToMono(StringValue.class)
                .doOnNext(val -> log.info("val:{}", val));
    }
}
