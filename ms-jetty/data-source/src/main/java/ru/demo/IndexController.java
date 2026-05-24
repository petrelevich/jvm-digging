package ru.demo;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    // curl -v http://localhost:8081/value?val=5
    @GetMapping("/value")
    public int hi(@RequestParam(name = "val") int value) throws InterruptedException {
        log.info("get request:{}", value);
        Thread.sleep(Duration.ofMillis(100));
        return value;
    }
}
