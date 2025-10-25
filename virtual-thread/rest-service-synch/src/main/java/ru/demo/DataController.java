package ru.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final AnyLiba anyLiba;

    public DataController(AnyLiba anyLiba) {
        this.anyLiba = anyLiba;
    }

    @GetMapping("/data/{id}")
    public String data(@PathVariable("id") long id) throws InterruptedException {
        log.info("get request for data:{}", id);
        return anyLiba.action();
    }
}
