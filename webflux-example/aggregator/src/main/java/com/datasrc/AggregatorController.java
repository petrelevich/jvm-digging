package com.datasrc;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AggregatorController {
    private static final Logger log = LoggerFactory.getLogger(AggregatorController.class);
    private final MultiplierClient multiplierClient1;
    private final MultiplierClient multiplierClient2;
    private final MultiplierClient multiplierClient3;

    public AggregatorController(
            MultiplierClient multiplierClient1,
            MultiplierClient multiplierClient2,
            MultiplierClient multiplierClient3) {
        this.multiplierClient1 = multiplierClient1;
        this.multiplierClient2 = multiplierClient2;
        this.multiplierClient3 = multiplierClient3;
    }

    /*
    curl -v http://localhost:8080/agg/1
     */

    @GetMapping(value = "/agg/{id}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<String> agg(@PathVariable(name = "id") long id) {
        log.info("request, id:{}", id);

        var request1 = doRequest(multiplierClient1, id);
        var request2 = doRequest(multiplierClient2, id);
        var request3 = doRequest(multiplierClient3, id);

        return Flux.merge(request1, request2, request3)
                .doOnNext(result -> log.info("result:{}", result))
                .doOnError(error -> log.error("error, id:{}", id, error));
    }

    private Mono<String> doRequest(MultiplierClient multiplierClient, long id) {
        return multiplierClient
                .client()
                .get()
                .uri("/multiplier?value={id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Long.class)
                .map(value -> String.format("from: %s, response:%d", multiplierClient.name(), value))
                .doOnNext(val -> log.info("{}", val));
    }
}
