package ru.demo;

import io.github.bucket4j.Bucket;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    // curl -v http://localhost:8080/info?name="testClient"

    // curl -v http://localhost:8081/info?name="testClient-1"
    // curl -v http://localhost:8082/info?name="testClient-2"

    private final Bucket bucket;

    public RestController(Bucket bucket) {
        this.bucket = bucket;
    }

    @GetMapping(value = "/info")
    public String info(@RequestParam(name = "name") String name) throws InterruptedException {
        log.info("request. name:{}", name);

        var blockingBucket = bucket.asBlocking().asVerbose();

        var consumeResult = blockingBucket.tryConsume(1, Duration.ofSeconds(1));
        var availableTokens = consumeResult.getState().getAvailableTokens();
        boolean consumeResultValue = consumeResult.getValue();
        log.info("consumeResultValue:{}, availableTokens:{}", consumeResultValue, availableTokens);
        if (consumeResultValue) {
            return String.format("Hi, %s", name);
        } else {
            return "Not available";
        }
    }
}
