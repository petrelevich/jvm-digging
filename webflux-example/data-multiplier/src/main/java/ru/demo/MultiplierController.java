package ru.demo;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.demo.config.ApplParams;

@RestController
public class MultiplierController {
    private static final Logger log = LoggerFactory.getLogger(MultiplierController.class);
    private final ApplParams applParams;

    public MultiplierController(ApplParams applParams) {
        this.applParams = applParams;
    }

    /*
    curl http://localhost:8070/multiplier?value=1
     */

    @GetMapping(value = "/multiplier", produces = MediaType.APPLICATION_JSON_VALUE)
    public long multiplier(@RequestParam(name = "value") long value) throws InterruptedException {
        log.info("request, value:{}", value);

        Thread.sleep(Duration.ofSeconds(applParams.delaySec()));
        var result = value * applParams.multiplier();
        log.info("value:{}, result:{}", value, result);
        return result;
    }
}
