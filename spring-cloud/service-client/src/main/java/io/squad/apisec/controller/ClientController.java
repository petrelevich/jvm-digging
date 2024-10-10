package io.squad.apisec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    //curl -v http://localhost:8081/info?name="testClient"

    @GetMapping(value = "/info")
    public String info(@RequestParam(name = "name") String name) {
        logger.info("request. name:{}", name);
        return String.format("ClientInfo name:%s", name);
    }
}