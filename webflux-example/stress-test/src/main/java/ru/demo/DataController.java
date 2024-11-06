package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final WebClient dataSourceClient;

    public DataController(WebClient dataSourceClient) {
        this.dataSourceClient = dataSourceClient;
    }

    /*
    curl http://localhost:8070/data/1
     */

    @GetMapping(value = "/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> dataSource(@PathVariable(name = "id") long id) {
        log.atDebug().setMessage("request, id:{}").addArgument(id).log();
        return dataSourceClient
                .get()
                .uri("/data/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(val -> log.trace("data:{}", val));
    }
}
