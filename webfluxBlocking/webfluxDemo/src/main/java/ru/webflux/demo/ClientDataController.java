package ru.webflux.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


//  http://localhost:8080/data/
@RestController
public class ClientDataController {

    private final LongProviderService longProviderService;

    private final Scheduler blockingPool;

    public ClientDataController(LongProviderService longProviderService, Scheduler blockingPool) {
        this.longProviderService = longProviderService;
        this.blockingPool = blockingPool;
    }

    @GetMapping(value = "/data/{value}")
    public Mono<String> data(@PathVariable("value") long value) {
        return Mono.fromCallable(() -> String.format("%d%n", longProviderService.get(value)));
    }

    @GetMapping(value = "/dataReact/{value}")
    public Mono<String> dataReact(@PathVariable("value") long value) {
        return Mono.fromCallable(() -> String.format("%d%n", value));
    }
}

/*
    @GetMapping(value = "/data/{value}")
    public Mono<String> data(@PathVariable("value") long value) {
        return Mono.fromCallable(() -> String.format("%d%n", longProviderService.get(value)))
                .publishOn(blockingPool);
    }
 */