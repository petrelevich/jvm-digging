package ru.demo.controller;

import com.netflix.discovery.EurekaClient;
import io.github.resilience4j.core.functions.CheckedFunction;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static ru.demo.filter.MdcFilter.MDC_REQUEST_ID;


@RestController
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private final ClientAdditionalInfoClient clientAdditionalInfoClient;
    private final EurekaClient discoveryClient;
    private final CheckedFunction<String, String> getAdditionalInfoFunction;

    //curl -v -H "X-Request-Id: 123" http://localhost:8081/info?name="testClient"

    public ClientController(ClientAdditionalInfoClient clientAdditionalInfoClient,
                            EurekaClient discoveryClient,
                            CircuitBreaker circuitBreaker,
                            RateLimiter rateLimiter) {
        this.clientAdditionalInfoClient = clientAdditionalInfoClient;
        this.discoveryClient = discoveryClient;

        this.getAdditionalInfoFunction = RateLimiter.decorateCheckedFunction(rateLimiter,
                name -> circuitBreaker.run(() -> getAdditionalInfo(name),
                        t -> {
                            log.error("delay call failed error:{}", t.getMessage());
                            return "unknown info";
                        }));
    }

    @GetMapping(value = "/info")
    public String info(@RequestParam(name = "name") String name) {
        log.info("request. name:{}", name);
        String additionalInfo = null;
        try {
            additionalInfo = getAdditionalInfoFunction.apply(name);
        } catch (Throwable ex) {
            log.error("can't execute additional info, name:{}, error:{}", name, ex.getMessage());
        }
        return String.format("ClientInfo name:%s, additional:%s", name, additionalInfo);
    }

    private String getAdditionalInfo(String name) {
        try {
            var clientInfo = discoveryClient.getNextServerFromEureka("SERVICE-CLIENT-INFO", false);
            log.info("clientInfo from Eureka:{}", clientInfo);
            var additionalInfo = clientAdditionalInfoClient.additionalInfo(
                    MDC.get(MDC_REQUEST_ID),
                    new URI(clientInfo.getHomePageUrl()),
                    name);
            log.info("additionalInfo:{}", additionalInfo);
            return additionalInfo.data();
        } catch (Exception ex) {
            log.error("can't get additional info, name:{}, error:{}", name, ex.getMessage());
            return null;
        }
    }
}