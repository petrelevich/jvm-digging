package io.squad.apisec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    //curl -v http://localhost:8081/info?id="idClient"

    @GetMapping(value = "/info")
    public String info(@RequestParam(name = "id") String id) {
        logger.info("request. id:{}", id);
        return String.format("Order id:%s", id);
    }
}