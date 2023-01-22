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

    private final Scheduler workerPool;

    public ClientDataController(LongProviderService longProviderService, Scheduler workerPool) {
        this.longProviderService = longProviderService;
        this.workerPool = workerPool;
    }

    @GetMapping(value = "/data/{value}")
    public Mono<String> data(@PathVariable("value") long value) {
        return Mono.fromCallable(() -> String.format("%d%n", longProviderService.get(value)))
                .publishOn(workerPool);
    }
}
