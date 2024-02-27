package ru.demo.mainpackage.api;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${application.rest.api.prefix}/v1")
public class GreetingController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    // http://localhost:8080/api/v1/hello?name=ddd
    @GetMapping("/hello")
    public String sayHello(@RequestParam(name = "name") String name) {
        logger.info("processing request, name:{}", name);
        var isVirtual = Thread.currentThread().isVirtual();
        logger.info("hello, isVirtual:{}", isVirtual);
        delay();
        return String.format("Hello, %s, from rest", name);
    }

    private void delay() {
        try {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
