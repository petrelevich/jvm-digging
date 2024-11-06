package com.datasrc;

import com.datasrc.model.Result;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.datasrc.model.Result.FAIL;
import static com.datasrc.model.Result.OK;


@RestController
public class DecisionMakerController {
    private static final Logger log = LoggerFactory.getLogger(DecisionMakerController.class);
    private final Scheduler timeoutTimer;
    private final AdviserClient adviserClient1;
    private final AdviserClient adviserClient2;
    private final AdviserClient adviserClient3;

    public DecisionMakerController(Scheduler timeoutTimer,
                                   AdviserClient adviserClient1,
                                   AdviserClient adviserClient2,
                                   AdviserClient adviserClient3) {
        this.timeoutTimer = timeoutTimer;
        this.adviserClient1 = adviserClient1;
        this.adviserClient2 = adviserClient2;
        this.adviserClient3 = adviserClient3;
    }

/*
    curl -l 'http://localhost:8080/isOk/1/options?adviserCounter=2&timeout=3'
     */

    @GetMapping(value = "/isOk/{value}/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Result> isOk(@PathVariable("value") long value,
                             @RequestParam("adviserCounter") int adviserCounter,
                             @RequestParam("timeout") int timeout) {
        log.info("request, value:{}, adviserCounter:{}, timeout:{}", value, adviserCounter, timeout);

        var request1 = doRequest(adviserClient1, value);
        var request2 = doRequest(adviserClient2, value);
        var request3 = doRequest(adviserClient3, value);

        return Flux.merge(request1, request2, request3)
                .doOnNext(result -> log.info("result:{}", result))
                .takeUntil(result -> result == 0)
                .buffer(adviserCounter)
                .timeout(Duration.ofSeconds(timeout), fallback(value),  timeoutTimer)
                .next()
                .map(results -> results.stream().reduce(0L, Long::sum))
                .map(result -> result > 100 ? OK : FAIL)
                .onErrorResume(error -> {
                    log.info("request timeout");
                    return Mono.just(FAIL);
                });
    }

    private Publisher<List<Long>> fallback(long value) {
        log.info("exec fallback");
        return adviserClient1.client()
                .get()
                .uri("/multiplier?value={id}", value)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnNext(val -> log.info("fallback val:{}", val))
                .map(List::of);
    }

    private Mono<Long> doRequest(AdviserClient adviserClient, long value) {
        return adviserClient.client()
                .get()
                .uri("/multiplier?value={id}", value)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Long.class);
    }
}
