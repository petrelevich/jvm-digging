package ru.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RequestSender implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(RequestSender.class);
    private static final int REQUEST_NUMBER = 300;
    private final RestClient restClient;
    private final ExecutorService executor;
    private final AtomicLong currentRequestCounter = new AtomicLong(0);

    public RequestSender(RestClient restClient, ExecutorService executor) {
        this.restClient = restClient;
        this.executor = executor;
    }

    @Override
    public void run(String... args) {
        for (var idx = 0; idx < REQUEST_NUMBER; idx++) {
            executor.submit(this::doRequest);
        }
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (currentRequestCounter.get() >= REQUEST_NUMBER) {
                    log.info("all done");
                    executor.shutdownNow();
                }
            }
        });
    }

    private void doRequest() {
        log.info("do request");
        var result = restClient
                .get()
                .uri("/data")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        log.info("result:{}", result);
        currentRequestCounter.incrementAndGet();
    }
}
