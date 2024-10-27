package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final RestClient dataSourceClient;

    public DataController(RestClient dataSourceClient) {
        this.dataSourceClient = dataSourceClient;
    }

    /*
    curl http://localhost:8070/data/1
     */

    @GetMapping(value = "/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String dataSource(@PathVariable(name = "id") long id) {
        log.atDebug().setMessage("request, id:{}").addArgument(id).log();
        var result = dataSourceClient
                .get()
                .uri("/data/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        log.trace("data:{}", result);
        return result;
    }
}
