package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);

    /*
    curl http://localhost:8070/data/1
     */

    @GetMapping(value = "/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> dataSource(@PathVariable(name = "id") long id) {
        log.debug("request, id:{}", id);

        return Mono.just(calc(id));
    }

    private String calc(long id) {
        log.debug("calc, id:{}", id);
        var result = id;
        for (var idxOut = 0; idxOut < Integer.MAX_VALUE; idxOut++) {
            for (var idx = 0; idx < 100; idx++) {
                result++;
            }
        }

        var resultStr = String.valueOf(result);
        log.debug("resultStr:{}", resultStr);
        return resultStr;
    }
}
